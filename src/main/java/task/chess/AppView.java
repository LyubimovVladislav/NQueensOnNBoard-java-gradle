package task.chess;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;

public class AppView {
	private AnchorPane anchorPane;
	private Image lightSquare, blackSquare, queen;
	private Board board;
	private final Integer dimension;
	private final double scale;
	private SimpleIntegerProperty nOfSolution;
	private LinkedList<ImageView> queens = new LinkedList<>();
	
	
	public AppView(Board board) throws FileNotFoundException {
//		lightSquare = new Image(new FileInputStream("src/main/resources/LightSquare.png"));
//		blackSquare = new Image(new FileInputStream("src/main/resources/BlackSquare.png"));
		queen = new Image(new FileInputStream("src/main/resources/Queen.png"));
		this.board = board;
		this.dimension = board.getDimensions();
		scale = 850f / dimension;
		
		createAnchorPane();
		placeQueens(0);
	}
	
	public final int getNOfSolution() {
		return nOfSolutionStringProperty().get();
	}
	
	public final void setNOfSolution(int value) {
		nOfSolutionStringProperty().set(value);
	}
	
	public final IntegerProperty nOfSolutionStringProperty() {
		if (nOfSolution == null)
			nOfSolution = new SimpleIntegerProperty(1);
		return this.nOfSolution;
	}
	
	public AnchorPane getAnchorPane() {
		return anchorPane;
	}
	
	
	private void createAnchorPane() {
		anchorPane = new AnchorPane();
//		anchorPane.getChildren().add(new Rectangle(47, 47, 856, 856));
		createUIBoard();
		makePanel();
	}
	
	private void placeQueens(int n) {
		anchorPane.getChildren().removeAll(queens);
		ChessPiece[][] solution = board.getSolution(n);
		if (solution == null)
			return;
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (solution[j][i] != null) {
					ImageView queenImg = new ImageView(queen);
					queenImg.setX(50 + (scale * i));
					queenImg.setY(50 + (scale * j));
					queenImg.setFitHeight(scale);
					queenImg.setFitWidth(scale);
					queens.add(queenImg);
					anchorPane.getChildren().add(queenImg);
				}
			}
		}
	}
	
	private void createUIBoard() {
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				Rectangle rect;
				if ((j + i) % 2 == 0) {
					rect = new Rectangle(50 + (i * scale), 50 + (j * scale), scale, scale);
					rect.setFill(Color.valueOf("f0d9b5"));
				} else {
					rect = new Rectangle(50 + (i * scale), 50 + (j * scale), scale, scale);
					rect.setFill(Color.valueOf("b58863"));
				}
				anchorPane.getChildren().add(rect);
			}
		}
	}
	
	private void makePanel() {
		Rectangle black = new Rectangle(1000, 50, 300, 850);
		black.setFill(Color.rgb(0, 0, 0));
		Rectangle white = new Rectangle(1002, 52, 296, 846);
		white.setFill(Color.rgb(255, 255, 255)); //Color.valueOf("f5f5f5")


		
		Label fenLabel = new Label("FEN:");
		fenLabel.setLayoutX(1050);
		fenLabel.setLayoutY(150);
		TextField fen = new TextField();
		board.toFenString(0);
		fen.textProperty().bind(board.fenStringProperty());
		fen.setLayoutX(1100);
		fen.setLayoutY(150);
		fen.setEditable(false);
		

//		Button del = new Button("delete");
		Button forwards = new Button("Forward");
		Button backwards = new Button("Back");
		Button forwardsHundred = new Button("Forward 100");
		Button backwardsHundred = new Button("Back 100");
		
		Label solutionLabel = new Label("1");
		Label size = new Label("Size of the board: " + dimension.toString());
		Label solutionSizeLabel = new Label("out of " + board.getSolutionSize().toString());
		solutionLabel.textProperty().bind(nOfSolutionStringProperty().asString());
		
		forwards.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
			if (nOfSolution.get() + 1 <= board.getSolutionSize()) {
				nOfSolution.set(nOfSolution.get() + 1);
				placeQueens(nOfSolution.get() - 1);
			}
			if (nOfSolution.get() == board.getSolutionSize()) {
				forwards.setDisable(true);
				forwardsHundred.setDisable(true);
			}
			backwards.setDisable(false);
			if (nOfSolution.get() + 100 > board.getSolutionSize())
				forwardsHundred.setDisable(true);
			if (nOfSolution.get() - 100 >= 1)
				backwardsHundred.setDisable(false);
			board.toFenString(nOfSolution.get() - 1);
		});
		
		backwards.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
			if (nOfSolution.get() - 1 >= 1) {
				nOfSolution.set(nOfSolution.get() - 1);
				placeQueens(nOfSolution.get() - 1);
			}
			if (nOfSolution.get() == 1) {
				backwards.setDisable(true);
				backwardsHundred.setDisable(true);
			}
			forwards.setDisable(false);
			if (nOfSolution.get() - 100 < 1)
				backwardsHundred.setDisable(true);
			if (nOfSolution.get() + 100 <= board.getSolutionSize())
				forwardsHundred.setDisable(false);
			board.toFenString(nOfSolution.get() - 1);
		});
		
		forwardsHundred.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
			if (nOfSolution.get() + 100 <= board.getSolutionSize()) {
				nOfSolution.set(nOfSolution.get() + 100);
				placeQueens(nOfSolution.get() - 1);
			}
			forwardsHundred.setDisable(nOfSolution.get() + 100 >= board.getSolutionSize());
			backwardsHundred.setDisable(false);
			forwards.setDisable(nOfSolution.get() == board.getSolutionSize());
			backwards.setDisable(false);
			board.toFenString(nOfSolution.get() - 1);
		});
		
		backwardsHundred.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
			if (nOfSolution.get() - 100 >= 1) {
				nOfSolution.set(nOfSolution.get() - 100);
				placeQueens(nOfSolution.get() - 1);
			}
			backwards.setDisable(nOfSolution.get() == 1);
			backwardsHundred.setDisable(nOfSolution.get() - 100 < 1);
			forwardsHundred.setDisable(false);
			forwards.setDisable(false);
			if (nOfSolution.get() + 100 <= board.getSolutionSize())
				forwardsHundred.setDisable(false);
			board.toFenString(nOfSolution.get() - 1);
		});
		
		backwardsHundred.setDisable(true);
		backwards.setDisable(true);
		if (board.getSolutionSize() <= 1) {
			if (board.getSolutionSize() == 0)
				nOfSolution.set(0);
			forwards.setDisable(true);
			forwardsHundred.setDisable(true);
		} else if (board.getSolutionSize() < 100)
			forwardsHundred.setDisable(true);

//		del.addEventHandler(MouseEvent.ANY, (e) -> del.requestFocus());
//		del.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> anchorPane.getChildren().removeAll(queens));
//
//		Button add = new Button("add");
//		add.addEventHandler(MouseEvent.MOUSE_CLICKED, (event -> anchorPane.getChildren().add(imageView)));


		size.setLayoutX(1050);
		size.setLayoutY(100);
		forwards.setLayoutX(1150);
		forwards.setLayoutY(200);
		backwards.setLayoutX(1050);
		backwards.setLayoutY(200);
		forwardsHundred.setLayoutX(1150);
		forwardsHundred.setLayoutY(300);
		backwardsHundred.setLayoutX(1050);
		backwardsHundred.setLayoutY(300);
		solutionLabel.setLayoutX(1050);
		solutionLabel.setLayoutY(250);
		solutionSizeLabel.setLayoutX(1150);
		solutionSizeLabel.setLayoutY(250);
		
		
		anchorPane.getChildren().addAll(black, white, fen, forwards, size,
				backwards, solutionLabel, solutionSizeLabel, fenLabel, forwardsHundred, backwardsHundred);
	}
	

}

package task.chess;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.Optional;

public class AppMain extends Application {
	private BorderPane root;
	private AppView appView;
	
	
	@Override
	public void start(Stage primaryStage) throws FileNotFoundException {
		root = new BorderPane();
		
		primaryStage.setMinWidth(300);
		primaryStage.setMinHeight(300);
		appView = new AppView(new Board(8));
		root.setCenter(appView.getAnchorPane());
		
		primaryStage.setTitle("Queens");
		Scene scene = new Scene(root, 1400, 1000);
		root.setTop(addMenu());
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	private MenuBar addMenu()  {
		Menu changeMenu = new Menu("Change");
		MenuItem changeDimension = new MenuItem("Change board size");
		changeDimension.setOnAction(e -> {
			TextInputDialog dialog = new TextInputDialog("8");
			dialog.setTitle("Changing the value");
			dialog.setHeaderText("Type new value that is greater than 0");
			dialog.setContentText("Size of the board:");
			dialog.getEditor().setTextFormatter(new TextFormatter<Object>(change -> change.getControlNewText().matches("[\\d\\s]*") ? change : null));
			
			Optional<String> result = dialog.showAndWait();

			try {
				if(result.isPresent())
					appView = new AppView(new Board(Integer.parseInt(result.get())));
			} catch (FileNotFoundException fileNotFoundException) {
				fileNotFoundException.printStackTrace();
			}
			assert appView != null;
			root.setCenter(appView.getAnchorPane());
		});
		changeMenu.getItems().add(changeDimension);
		Menu exitMenu = new Menu("Exit");
		MenuItem exit = new MenuItem("Exit");
		exitMenu.getItems().add(exit);
		exit.setOnAction(e -> Platform.exit());
		return new MenuBar(changeMenu, exitMenu);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

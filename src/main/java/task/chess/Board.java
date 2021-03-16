package task.chess;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Arrays;
import java.util.LinkedList;

public class Board {
	private ChessPiece[][] board;
	private int dimensions;
	private int[][] underAttackBoard;
	private LinkedList<ChessPiece> attackMatrix;
	private SimpleStringProperty fenString;
	private LinkedList<ChessPiece[][]> solved;
	private boolean isSolved = false;
	
	public Board(int dimensions) {
		createBlankBoards(dimensions);
		this.dimensions = dimensions;
		this.setFenString(this.toFenString());
		solved = new LinkedList<>();
		fillAttackMatrix();
		backtrack(0);
	}
	
	private void createBlankBoards(int n) {
		board = new ChessPiece[n][n];
		underAttackBoard = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				board[i][j] = null;
				underAttackBoard[i][j] = 0;
			}
		}
	}
	
	
	public void fillAttackMatrix() {
		attackMatrix = new LinkedList<>();
		for (int i = 0; i < dimensions; i++) {
			for (int j = 0; j < dimensions; j++)
				attackMatrix.add(new ChessPiece(dimensions, i, j));
		}
	}
	
	public ChessPiece[][] getSolution(int n) {
		if (n>=solved.size())
			return null;
		try {
			return solved.get(n);
		} catch (IndexOutOfBoundsException e){
			System.out.println("\nOut Of bounds!\n");
		}
		return null;
	}
	
	private void backtrack(int level) {
		if (dimensions >= 14 && isSolved)
			return;
		if (level < dimensions) {
			for (int i = 0; i < dimensions; i++) {
				if (underAttackBoard[level][i] == 0) {
					addUp(attackMatrix.get((level * dimensions) + i), true);
					addQueen(level, i);
					backtrack(level + 1);
					addUp(attackMatrix.get((level * dimensions) + i), false);
					delQueen(level, i);
				}
			}
		}
		if (level == dimensions) {
			isSolved = true;
			ChessPiece[][] copy = Arrays.stream(board).map(ChessPiece[]::clone).toArray(ChessPiece[][]::new);
			solved.add(copy);
		}
	}
	
	public Integer getSolutionSize(){
		return solved.size();
	}
	
	
	private void addUp(ChessPiece piece, boolean flag) {
		int[][] matrix = piece.getUnderAttackByPiece();
		for (int i = 0; i < dimensions; i++)
			for (int j = 0; j < dimensions; j++)
				if (flag)
					underAttackBoard[i][j] += matrix[i][j];
				else
					underAttackBoard[i][j] -= matrix[i][j];
	}
	
	public String toFenString() {
		StringBuilder str = new StringBuilder();
		int spaces = 0;
		for (int i = 0; i < dimensions; i++) {
			for (int j = 0; j < dimensions; j++) {
				if (board[i][j] == null) {
					spaces++;
				} else {
					if (spaces != 0)
						str.append(spaces);
					str.append("Q");
					spaces = 0;
				}
			}
			if (spaces != 0)
				str.append(spaces);
			spaces = 0;
			if (i + 1 != dimensions)
				str.append("/");
		}
		str.append(" w - - 0 1");
		return str.toString();
	}
	
	public void toFenString(int n) {
		StringBuilder str = new StringBuilder();
		if (solved.size()==0)
			return;
		ChessPiece[][] board = solved.get(n);
		int spaces = 0;
		for (int i = 0; i < dimensions; i++) {
			for (int j = 0; j < dimensions; j++) {
				if (board[i][j] == null) {
					spaces++;
				} else {
					if (spaces != 0)
						str.append(spaces);
					str.append("Q");
					spaces = 0;
				}
			}
			if (spaces != 0)
				str.append(spaces);
			spaces = 0;
			if (i + 1 != dimensions)
				str.append("/");
		}
		str.append(" w - - 0 1");
		fenString.set(str.toString());
	}
	
	public Board addQueen(int i, int j) {
		board[i][j] = new ChessPiece(dimensions, i, j);
		return this;
	}
	
	public Board delQueen(int i, int j) {
		board[i][j] = null;
		return this;
	}
	
	public String toConsole() {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < dimensions; i++) {
			for (int j = 0; j < dimensions; j++) {
				if (board[i][j] == null)
					str.append("-");
				else
					str.append("Q");
			}
			str.append("\n");
		}
		return str.toString();
	}
	
	public final String getFenString() {
		return fenStringProperty().get();
	}
	
	public final void setFenString(String value) {
		fenStringProperty().set(value);
	}
	
	public final StringProperty fenStringProperty() {
		if (fenString == null)
			fenString = new SimpleStringProperty();
		return this.fenString;
	}
	
	public int getDimensions() {
		return dimensions;
	}
}

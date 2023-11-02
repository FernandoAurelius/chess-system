package chess;

import boardgame.Position;

public class ChessPosition {
	
	private char column;
	private int row;
	
	public ChessPosition(char column, int row) {
		if (column < 'a' || column > 'h' || row < 1 || row > 8) {
			throw new ChessException("Erro instanciando a Posição de Xadrez! Valores válidos são de a1 até h8.");
		}
		this.column = column;
		this.row = row;
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	// Método para transformar uma posição de Xadrez em posição de matriz
	protected Position toPosition() {
		return new Position(8 - row, column - 'a');
	}
	
	// Método para transformar uma posição de matriz em posição de Xadrez
	protected static ChessPosition fromPosition(Position position) {
		return new ChessPosition((char)('a' - position.getColumn()), 8 - position.getRow());
	}
	
	// Método toString básico retornando uma concatenação entre a coluna e a fileira
	@Override
	public String toString() {
		return "" + column + row;
	}
	
}
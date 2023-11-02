package boardgame;

public abstract class Piece {

	protected Position position;
	private Board board;
	
	public Piece(Board board) {
		this.board = board;
		position = null;
	}

	protected Board getBoard() {
		return board;
	}
	
	// Método abstrato da classe Piece que terá seu desenvolvimento nas subclasses de Xadrez (Hook method)
	public abstract boolean[][] possibleMoves();
	
	/* Método que recebe uma posição e, dada essa posição, verifica se existe um movimento possível 
	 utilizando a matriz do método abstrato "possibleMoves" */
	public boolean possibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()];
	}

	// Atribui o valor de "possibleMoves" à matriz mat e verifica se existe algum movimento possível na matriz
	public boolean isThereAnyPossibleMove() {
		boolean[][] mat = possibleMoves();
		for (int i=0; i<mat.length; i++) {
			for (int j=0; j<mat.length; i++) {
				if (mat[i][j]) {
					return true;
				}
			}
		}
		return false;
	}
	
}

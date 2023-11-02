package boardgame;

public class Board {
	
	private int rows;
	private int columns;
	private Piece[][] pieces;
	
	public Board(int rows, int columns) {
		if (rows < 1 || columns < 1) {
			throw new BoardException("Erro criando o tabuleiro: devem existir ao menos 1 linha e 1 coluna");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
	
	// Método para colocar uma peça na matriz de peças do tabuleiro
	public Piece piece(int row, int column) {
		if (!positionExists(row, column)) {
			throw new BoardException("Posição fora do tabuleiro!");
		}
		return pieces[row][column];
	}
	
	// Sobrecarga do método para colocar uma peça na matriz de peças, recebendo uma posição
	public Piece piece (Position position) {
		if (!positionExists(position)) {
			throw new BoardException("Posição fora do tabuleiro!");
		}
		return pieces[position.getRow()][position.getColumn()];
	}
	
	// Método para colocar uma peça no tabuleiro, recebendo a peça e a posição, e armazenando-as na matriz
	public void placePiece(Piece piece, Position position) {
		if (thereIsAPiece(position)) {
			throw new BoardException("Já existe uma peça na posição: " + position);
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position;
	}
	
	/* Método para verificar se uma posição existe
	 * Recebe linha e coluna
	 * Retorna verdadeiro se ambas linha e coluna estão dentro das dimensões do tabuleiro
	 */
	private boolean positionExists(int row, int column) {
		return row >= 0 && row < rows && column >= 0 && column < columns;
	}
	
	// Sobrecarga do método de verificação de posição, recebendo uma posição
	public boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn());
	}
	
	// Método para verificar a existência de uma peça em uma posição dada
	public boolean thereIsAPiece(Position position) {
		if (!positionExists(position)) {
			throw new BoardException("Posição fora do tabuleiro!");
		}
		return piece(position) != null;
	}
	
	// Método para remover uma peça de uma posição dada
	public Piece removePiece(Position position) {
		if (!positionExists(position)) {
			throw new BoardException("Posição fora do tabuleiro!");
		}
		if (piece(position) == null) {
			return null;
		}
		Piece aux = piece(position);
		aux.position = null;
		pieces[position.getRow()][position.getColumn()] = null;
		return aux;
	}
	
}
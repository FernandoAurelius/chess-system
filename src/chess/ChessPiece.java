package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece {

	private Color color;
	private int moveCount;

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	public int getMoveCount() {
		return moveCount;
	}
	
	// Método que incrementa o valor da variável "moveCount" (contador de movimento)
	public void increaseMoveCount() {
		moveCount++;
	}
	
	// Método decrementa o valor da variável "moveCount" (contador de movimento)
	public void decreaseMoveCount() {
		moveCount--;
	}
	
	// Método que retorna retorna uma posição de Xadrezde uma peça de uma posição de matriz
	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(position);
	}
	
	// Método para verificar se há uma peça do oponente na posição fornecida
	protected boolean isThereOpponentPiece(Position position) {
	    ChessPiece p = (ChessPiece)getBoard().piece(position); 
	    return p != null && p.getColor() != color; 
	}
	
}

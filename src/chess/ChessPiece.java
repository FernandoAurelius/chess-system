package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece {

	private Color color;

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}	
	
	// Método para verificar se há uma peça do oponente na posição fornecida
	protected boolean isThereOpponentPiece(Position position) {
	    ChessPiece p = (ChessPiece)getBoard().piece(position); 
	    return p != null && p.getColor() != color; 
	}
	
}

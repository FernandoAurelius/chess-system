package chess.pieces;

import boardgame.Board;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece{

	public King(Board board, Color color) {
		super(board, color);
	}

	// Método toString simples para imprimir a inicial da peça "Rei" nas casas do tabuleiro
	@Override
	public String toString() {
		return "R";
	}

	@Override
	// Método provisório que receberá a lógica do movimento do Rei no Xadrez
	public boolean[][] possibleMoves() {
		
		// Matriz booleana de mesmas dimensões do tabuleiro (inicialmente recebe falso por padrão)
		boolean mat[][] = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		/* O Rei provisóriamente não terá movimentos possíveis, 
		mas o método já foi implementado para não haver erros no corpo do código */
		return mat;
	}
	
}

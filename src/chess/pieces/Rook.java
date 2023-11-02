package chess.pieces;

import boardgame.Board;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece {

	
	public Rook(Board board, Color color) {
		super(board, color);
	}

	
	@Override
	// Método toString simples para imprimir a inicial da peça "Torre" nas casas do tabuleiro
	public String toString() {
		return "T";
	}

	@Override
	// Método provisório que receberá a lógica do movimento da Torre no Xadrez
		public boolean[][] possibleMoves() {
			
			// Matriz booleana de mesmas dimensões do tabuleiro (inicialmente recebe falso por padrão)
			boolean mat[][] = new boolean[getBoard().getRows()][getBoard().getColumns()];
			
			/* A Torre provisóriamente não terá movimentos possíveis, 
			mas o método já foi implementado para não haver erros no corpo do código */
			return mat;
		}
}

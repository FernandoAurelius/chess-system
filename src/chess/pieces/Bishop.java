package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Bishop extends ChessPiece {

	public Bishop(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	// Método toString simples para imprimir a inicial da peça "Bispo" nas casas do tabuleiro
	public String toString() {
		return "B";
	}

	@Override
	// Método que receberá a lógica do movimento do Bispo no Xadrez
		public boolean[][] possibleMoves() {
			
			// Matriz booleana de mesmas dimensões do tabuleiro (inicialmente recebe falso por padrão)
			boolean mat[][] = new boolean[getBoard().getRows()][getBoard().getColumns()];
			
			Position p = new Position(0, 0);
			
			// Verifica se há uma peça à noroeste do Bispo e se a peça é inimiga
			p.setValues(position.getRow() - 1, position.getColumn() - 1);
			while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
				p.setValues(p.getRow() - 1, p.getColumn() - 1);
			}
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			// Verifica se há uma peça à nordeste do Bispo e se a peça é inimiga
			p.setValues(position.getRow() - 1, position.getColumn() + 1);
			while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
				p.setValues(p.getRow() - 1, p.getColumn() + 1);;
			}
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			// Verifica se há uma peça à sudoeste do Bispo e se a peça é inimiga
			p.setValues(position.getRow() + 1, position.getColumn() - 1);
			while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
				p.setValues(p.getRow() + 1, p.getColumn() - 1);;
			}
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			// Verifica se há uma peça à sudeste do Bispo e se a peça é inimiga
			p.setValues(position.getRow() + 1, position.getColumn() + 1);
			while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
				p.setValues(p.getRow() + 1, p.getColumn() + 1);
			}
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			return mat;
		}

}

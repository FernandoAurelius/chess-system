package chess.pieces;

import boardgame.Board;
import boardgame.Position;
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
	// Método que receberá a lógica do movimento da Torre no Xadrez
		public boolean[][] possibleMoves() {
			
			// Matriz booleana de mesmas dimensões do tabuleiro (inicialmente recebe falso por padrão)
			boolean mat[][] = new boolean[getBoard().getRows()][getBoard().getColumns()];
			
			Position p = new Position(0, 0);
			
			// Verifica se há uma peça acima da Torre
			p.setValues(position.getRow() - 1, position.getColumn());
			while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
				p.setRow(p.getRow() - 1);
			}
			
			// Verifica se a peça acima da Torre é inimiga
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			// Verifica se há uma peça à esquerda da Torre
			p.setValues(position.getRow(), position.getColumn() - 1);
			while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
				p.setColumn(p.getColumn() - 1);
			}
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			// Verifica se há uma peça à direita da Torre
			p.setValues(position.getRow(), position.getColumn() + 1);
			while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
				p.setColumn(p.getColumn() + 1);
			}
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			// Verifica se há uma peça abaixo da Torre
			p.setValues(position.getRow() + 1, position.getColumn());
			while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
				p.setRow(p.getRow() + 1);
			}
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			return mat;
		}
}

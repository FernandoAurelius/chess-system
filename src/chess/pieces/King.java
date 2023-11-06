package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

public class King extends ChessPiece{

	private ChessMatch chessMatch;
	
	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}

	// Método toString simples para imprimir a inicial da peça "Rei" nas casas do tabuleiro
	@Override
	public String toString() {
		return "R";
	}

	/* Método que retorna verdadeiro se o Rei possui movimentos legais (caso uma casa adjacente esteja vazia
	 * ou caso a peça em uma casa adjacente seja de cor diferente
	 */
	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p == null || p.getColor() != getColor();
	}
	
	// Método para verificar se uma Torre está apta a fazer o Roque
	private boolean testRookCastling(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
	}
	
	@Override
	// Método que receberá a lógica do movimento do Rei no Xadrez
	public boolean[][] possibleMoves() {
		
		// Matriz booleana de mesmas dimensões do tabuleiro (inicialmente recebe falso por padrão)
		boolean mat[][] = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0,0);
		
		// Verifica se há uma peça acima do Rei
		p.setValues(position.getRow() - 1, position.getColumn());
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// Verifica se há uma peça abaixo do Rei
		p.setValues(position.getRow() + 1, position.getColumn());
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// Verifica se há uma peça à esquerda do Rei
		p.setValues(position.getRow(), position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// Verifica se há uma peça à direita do Rei
		p.setValues(position.getRow(), position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// Verifica se há uma peça à noroeste do Rei
		p.setValues(position.getRow() - 1, position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// Verifica se há uma peça à nordeste do Rei
		p.setValues(position.getRow() - 1, position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// Verifica se há uma peça à sudoeste do Rei
		p.setValues(position.getRow() + 1, position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// Verifica se há uma peça à sudeste do Rei
		p.setValues(position.getRow() + 1, position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// Verifica se o Rei está apto a fazer o movimento especial Roque
		if (getMoveCount() == 0 && !chessMatch.getCheck()) {
			// Roque pequeno
			Position r1 = new Position(position.getRow(), position.getColumn() + 3);
			if (testRookCastling(r1)) {
				Position p1 = new Position(position.getRow(), position.getColumn() + 1);
				Position p2 = new Position(position.getRow(), position.getColumn() + 2);
				if (!getBoard().thereIsAPiece(p1) && !getBoard().thereIsAPiece(p2)) {
					mat[position.getRow()][position.getColumn() + 2] = true;
				}
			}
			
			// Roque grande
			Position r2 = new Position(position.getRow(), position.getColumn() - 4);
			if (testRookCastling(r2)) {
				Position p1 = new Position(position.getRow(), position.getColumn() - 1);
				Position p2 = new Position(position.getRow(), position.getColumn() - 2);
				Position p3 = new Position(position.getRow(), position.getColumn() - 3);
				if (!getBoard().thereIsAPiece(p1) && !getBoard().thereIsAPiece(p2) && !getBoard().thereIsAPiece(p3)) {
					mat[position.getRow()][position.getColumn() - 2] = true;
				}
			}
		}
		
		return mat;
	}
	
}

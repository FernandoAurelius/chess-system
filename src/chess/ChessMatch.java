package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	
	/* Instancia um novo tabuleiro de dimensões 8x8 e chama a função "initialSetup", 
	 * que posiciona as peças inicias no tabuleiro, além de dar o primeiro lance para
	 * as brancas
	 */
	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.BRANCO;
		initialSetup();
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}
	
	public ChessPiece getPromoted() {
		return promoted;
	}
	
	// Método de implementação inicial do tabuleiro que retorna uma matriz de peças de Xadrez
	public ChessPiece[][] getPieces() {
		
		/* Cria uma nova matriz bidimensional do tipo ChessPiece
		 * O tamanho da matriz é determinado pelo número de linhas e colunas do tabuleiro
		 */
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		
		// Percorre as linhas do tabuleiro
		for (int i=0; i < board.getRows(); i++) { 
			// Percorre as colunas do tabuleiro
			for (int j=0; j < board.getColumns(); j++) { 
				// Adiciona uma peça de Xadrez em todas as casas do tabuleiro
				mat[i][j] = (ChessPiece) board.piece(i, j); 
			}
		}
		//Retorna a matriz de peças de implementação inicial do tabuleiro
		return mat;
	}
	
	// Imprime os movimentos possíveis de uma peça dada uma posição de Xadrez
	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}	
	/* Realiza um movimento de Xadrez e retorna a posição capturada durante o movimento
	 * Recebe uma posição de origem e destino do tipo "ChessPosition"
	 */
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		
		/* Converte as posições de origem e destino do tipo "ChessPosition" para "Position"
		 * Armazena as posições convertidas nas variáveis "source" e "target"
		 */
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		
		/* Valida a posição de origem (se existe uma peça e, 
		 * se existir, se a peça possui movimentos possíveis
		 */
		validateSourcePosition(source);
		
		// Valida a posição de destino
		validateTargetPosition(source, target);
		
		// Realiza o movimento de Xadrez da peça de origem e retorna a peça capturada durante o movimento
		Piece capturedPiece = makeMove(source, target);
		
		// Testa se o movimento realizado pelo jogador o deixou em xeque
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("Você não pode se colocar em xeque!");
		}
		
		ChessPiece movedPiece = (ChessPiece)board.piece(target);
		
		// Movimento especial de Promoção
		promoted = null;
		if (movedPiece instanceof Pawn) {
			if(movedPiece.getColor() == Color.BRANCO && target.getRow() == 0 || (movedPiece.getColor() == Color.PRETO && target.getRow() == 7)) {
				promoted = (ChessPiece)board.piece(target);
				promoted = replacePromotedPiece("D");
			}
		}
		
		// Testa se o movimento realizado deu xeque-mate no oponente
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		if(testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		
		// Testa se o movimento realizado pelo jogador deixou o oponente em xeque
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		nextTurn();
		
		// Movimento especial de En Passant
		if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
			enPassantVulnerable = movedPiece;
		}
		else {
			enPassantVulnerable = null;
		} 
		
		return (ChessPiece)capturedPiece;
	}
	
	// Método para trocar um peão promovido para a peça desejada
	public ChessPiece replacePromotedPiece(String type) {
		if (promoted == null) {
			throw new IllegalStateException("Não há peça para ser promovida");
		}
		if (!type.equals("B") && !type.equals("C") && !type.equals("T") && !type.equals("D")) {
			return promoted;
		}
		
		Position pos = promoted.getChessPosition().toPosition();
		Piece p = board.removePiece(pos);
		piecesOnTheBoard.remove(p);
		
		ChessPiece newPiece = newPiece(type, promoted.getColor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);
		
		return newPiece;
	}
	
	// Método auxiliar de "replacePromotedPiece" que instancia a nova peça desejada durante a promoção
	private ChessPiece newPiece(String type, Color color) {
		if (type.equals("B")) return new Bishop(board, color);
		if (type.equals("C")) return new Knight(board, color);
		if (type.equals("D")) return new Queen(board, color);
		return new Rook(board, color);
	}
	
	// Método para realizar o movimento de uma peça, recebendo uma posição de origem e destino
	private Piece makeMove(Position source, Position target) {
		
		// Remove a peça que irá ser movida da posição de origem através do método "removePiece"
		ChessPiece p = (ChessPiece)board.removePiece(source);
		p.increaseMoveCount();
		
		// Remove a peça que será capturada da posição de destino através do mesmo método
		Piece capturedPiece = board.removePiece(target);
		
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		// Coloca a peça que está realizando o movimento na posição de destino através do método "placePiece"
		board.placePiece(p, target);
		
		// Movimento especial de Roque pequeno
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceR = new Position(source.getRow(), source.getColumn() + 3);
			Position targetR = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceR);
			board.placePiece(rook, targetR);
			rook.increaseMoveCount();
		}
		
		// Movimento especial de Roque grande
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceR = new Position(source.getRow(), source.getColumn() - 4);
			Position targetR = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceR);
			board.placePiece(rook, targetR);
			rook.increaseMoveCount();
		}
		
		// Movimento especial de En Passant
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position pawnPosition;
				if (p.getColor() == Color.BRANCO) {
					pawnPosition = new Position(target.getRow() + 1, target.getColumn());
				}
				else {
					pawnPosition = new Position(target.getRow() - 1, target.getColumn());
				}
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}
		
		// Retorna a peça capturada
		return capturedPiece;
	}
	
	// Método auxiliar para a lógica de xeque, desfaz um movimento já realizado
	private void undoMove(Position source, Position target, Piece capturedPiece){
		ChessPiece p = (ChessPiece)board.removePiece(target);
		p.decreaseMoveCount();
		
		board.placePiece(p, source);
		
		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
		
		// Movimento especial de Roque pequeno
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceR = new Position(source.getRow(), source.getColumn() + 3);
			Position targetR = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetR);
			board.placePiece(rook, sourceR);
			rook.decreaseMoveCount();
		}
		
		// Movimento especial de Roque grande
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceR = new Position(source.getRow(), source.getColumn() - 4);
			Position targetR = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetR);
			board.placePiece(rook, sourceR);
			rook.decreaseMoveCount();
		}
		
		// Movimento especial de En Passant
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
				ChessPiece pawn = (ChessPiece)board.removePiece(target);
				Position pawnPosition;
				if (p.getColor() == Color.BRANCO) {
					pawnPosition = new Position(3, target.getColumn());
				}
				else {
					pawnPosition = new Position(4, target.getColumn());
				}
				board.placePiece(pawn, pawnPosition);
			}
		}
	}	
	
	/* Método de validação de posições de origem no tabuleiro. 
	 * Também valida os movimentos possíveis de uma peça
	 */
	private void validateSourcePosition(Position position) {
		
		// Através de uma negação do método "thereIsAPiece", lança uma exceção do tipo "ChessException"
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("Não existe peça na posição de origem!");
		}
		
		// Verifica se a peça escolhida na posição de origem é da mesma cor do jogador
		if (currentPlayer != (((ChessPiece) board.piece(position)).getColor())) {
			throw new ChessException("A peça escolhida não é sua!");
		}
		
		/* Através de uma negação do método "isThereAnyPossibleMove" de uma peça em uma posição do tabuleiro, 
		 * lança uma exceção do tipo "ChessException"
		 */
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("Não existem movimentos possíveis para essa peça!");
		}
	}
	
	/* Valida a posição de destino escolhida,
	 * Caso a posição de origem da peça escolhida não possua um movimento possível, 
	 * que inclua a posição de destino
	 */
	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("A peça escolhida não pode se mover para a posição de destino");
		}
	}	
	
	// Método para passar as rodadas e alternar a vez entre os jogadores branco e preto
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.BRANCO) ? Color.PRETO : Color.BRANCO;
	}
	
	// Método que retorna a cor do oponente
	public Color opponent(Color color) {
		return (color == Color.BRANCO ? Color.PRETO : Color.BRANCO);
	}
	
	// Método que percorre a lista de peças de um jogo de Xadrez e localiza o Rei de determinada cor
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("Não existe rei " + color + "no tabuleiro!");
	}
	
	// Testa se um movimento possível de uma peça adversária cai na casa do Rei, o que seria o xeque
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}
	
	/* Método que testa o xeque-mate ao criar uma matriz booleana com todos os movimentos possíveis de uma peça adversária
	 * Para qualquer movimento possível de uma peça adversária no tabuleiro, faz-se um movimento e, se no final de um movimento
	 * A peça continuar em xeque, então o programa retorna verdadeiro e a partida acaba
	 */
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (mat[i][j]) {
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	// Método que coloca uma peça numa posição do tabuleiro, recebendo a peça, a linha e a coluna
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		
		/* Chama o método "placePiece" 
		 * Recebe a peça e instanciando uma nova posição de xadrez com a linha e coluna
		 */
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	// Método para configurar inicialmente o tabuleiro de Xadrez
	private void initialSetup() {
		
		// Coloca as peças brancas no tabuleiro através do método "placeNewPiece"
		placeNewPiece('a', 1, new Rook(board, Color.BRANCO));
		placeNewPiece('h', 1, new Rook(board, Color.BRANCO));
		placeNewPiece('b', 1, new Knight(board, Color.BRANCO));
		placeNewPiece('g', 1, new Knight(board, Color.BRANCO));
		placeNewPiece('c', 1, new Bishop(board, Color.BRANCO));
		placeNewPiece('f', 1, new Bishop(board, Color.BRANCO));
		placeNewPiece('d', 1, new Queen(board, Color.BRANCO));
		placeNewPiece('e', 1, new King(board, Color.BRANCO, this));
		placeNewPiece('a', 2, new Pawn(board, Color.BRANCO, this));
		placeNewPiece('b', 2, new Pawn(board, Color.BRANCO, this));
		placeNewPiece('c', 2, new Pawn(board, Color.BRANCO, this));
		placeNewPiece('d', 2, new Pawn(board, Color.BRANCO, this));
		placeNewPiece('e', 2, new Pawn(board, Color.BRANCO, this));
		placeNewPiece('f', 2, new Pawn(board, Color.BRANCO, this));
		placeNewPiece('g', 2, new Pawn(board, Color.BRANCO, this));
		placeNewPiece('h', 2, new Pawn(board, Color.BRANCO, this));
        
        // Coloca as peças pretas no tabuleiro através do mesmo método
		placeNewPiece('a', 8, new Rook(board, Color.PRETO));
		placeNewPiece('h', 8, new Rook(board, Color.PRETO));
		placeNewPiece('b', 8, new Knight(board, Color.PRETO));
		placeNewPiece('g', 8, new Knight(board, Color.PRETO));
		placeNewPiece('c', 8, new Bishop(board, Color.PRETO));
		placeNewPiece('f', 8, new Bishop(board, Color.PRETO));
		placeNewPiece('d', 8, new Queen(board, Color.PRETO));
		placeNewPiece('e', 8, new King(board, Color.PRETO, this));
		placeNewPiece('a', 7, new Pawn(board, Color.PRETO, this));
		placeNewPiece('b', 7, new Pawn(board, Color.PRETO, this));
		placeNewPiece('c', 7, new Pawn(board, Color.PRETO, this));
		placeNewPiece('d', 7, new Pawn(board, Color.PRETO, this));
		placeNewPiece('e', 7, new Pawn(board, Color.PRETO, this));
		placeNewPiece('f', 7, new Pawn(board, Color.PRETO, this));
		placeNewPiece('g', 7, new Pawn(board, Color.PRETO, this));
		placeNewPiece('h', 7, new Pawn(board, Color.PRETO, this));
	}
}
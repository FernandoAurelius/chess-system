package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	
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
		
		// Testa se o movimento realizado pelo jogador deixou o oponente em xeque
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		nextTurn();
		return (ChessPiece)capturedPiece;
	}
	
	// Método para realizar o movimento de uma peça, recebendo uma posição de origem e destino
	private Piece makeMove(Position source, Position target) {
		
		// Remove a peça que irá ser movida da posição de origem através do método "removePiece"
		Piece p = board.removePiece(source);
		
		// Remove a peça que será capturada da posição de destino através do mesmo método
		Piece capturedPiece = board.removePiece(target);
		
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		// Coloca a peça que está realizando o movimento na posição de destino através do método "placePiece"
		board.placePiece(p, target);
		
		// Retorna a peça capturada
		return capturedPiece;
	}
	
	// Método auxiliar para a lógica de xeque, desfaz um movimento já realizado
	private void undoMove(Position source, Position target, Piece capturedPiece){
		Piece p = board.removePiece(target);
		board.placePiece(p, source);
		
		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
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
	
	private Color opponent(Color color) {
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
		placeNewPiece('c', 1, new Rook(board, Color.BRANCO));
        placeNewPiece('c', 2, new Rook(board, Color.BRANCO));
        placeNewPiece('d', 2, new Rook(board, Color.BRANCO));
        placeNewPiece('e', 2, new Rook(board, Color.BRANCO));
        placeNewPiece('e', 1, new Rook(board, Color.BRANCO));
        placeNewPiece('d', 1, new King(board, Color.BRANCO));
        
        // Coloca as peças pretas no tabuleiro através do mesmo método
        placeNewPiece('c', 7, new Rook(board, Color.PRETO));
        placeNewPiece('c', 8, new Rook(board, Color.PRETO));
        placeNewPiece('d', 7, new Rook(board, Color.PRETO));
        placeNewPiece('e', 7, new Rook(board, Color.PRETO));
        placeNewPiece('e', 8, new Rook(board, Color.PRETO));
        placeNewPiece('d', 8, new King(board, Color.PRETO));
	}
}
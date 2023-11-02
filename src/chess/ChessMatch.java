package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	
	/* Instancia um novo tabuleiro de dimensões 8x8 e chama a função "initialSetup", 
	 * que posiciona as peças inicias no tabuleiro
	 */
	public ChessMatch() {
		board = new Board(8, 8);
		initialSetup();
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
		return (ChessPiece)capturedPiece;
	}
	
	// Método para realizar o movimento de uma peça, recebendo uma posição de origem e destino
	private Piece makeMove(Position source, Position target) {
		
		// Remove a peça que irá ser movida da posição de origem através do método "removePiece"
		Piece p = board.removePiece(source);
		
		// Remove a peça que será capturada da posição de destino através do mesmo método
		Piece capturedPiece = board.removePiece(target);
		
		// Coloca a peça que está realizando o movimento na posição de destino através do método "placePiece"
		board.placePiece(p, target);
		
		// Retorna a peça capturada
		return capturedPiece;
	}
	
	/* Método de validação de posições de origem no tabuleiro. 
	 * Também valida os movimentos possíveis de uma peça
	 */
	private void validateSourcePosition(Position position) {
		
		// Através de uma negação do método "thereIsAPiece", lança uma exceção do tipo "ChessException"
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("Não existe peça na posição de origem!");
		}
		
		/* Através de uma negação do método "isThereAnyPossibleMove" de uma peça em uma posição do tabuleiro, 
		 * lança uma exceção do tipo "ChessException"
		 */
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("Não existem movimentos possíveis para essa peça!");
		}
	}
	
	/* Valida a posição de destino escolhida,
	 * Caso a posição de origem da peça escolhida não possua um movimento possível que inclua a posição de destino
	 */
	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("A peça escolhida não pode se mover para a posição de destino");
		}
	}	
	// Método que coloca uma peça numa posição do tabuleiro, recebendo a peça, a linha e a coluna
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		
		/* Chama o método "placePiece" 
		 * Recebe a peça e instanciando uma nova posição de xadrez com a linha e coluna
		 */
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}
	
	// Método para configurar inicialmente o tabuleiro de Xadrez
	private void initialSetup() {
		
		// Coloca as peças brancas no tabuleiro através do método "placeNewPiece"
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));
        
        // Coloca as peças pretas no tabuleiro através do mesmo método
        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));
	}
}
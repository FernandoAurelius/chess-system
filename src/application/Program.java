package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();
		List<ChessPiece> captured = new ArrayList<>();

		// Enquanto não houver xeque-mate, o jogo é impresso e repetido
		while (!chessMatch.getCheckMate()) {
			 try {
				 
				// Imprime a partida e o tabuleiro, perguntando a posição da peça de origem do lance
				UI.clearScreen();
				UI.printMatch(chessMatch, captured);
				System.out.println();
				System.out.print("Posição de origem: ");
				ChessPosition source = UI.readChessPosition(sc);
				
				// Mostra os movimentos possíveis para a peça na posição de origem
				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				UI.clearScreen();
				UI.printBoard(chessMatch.getPieces(), possibleMoves);
				
				// Pergunta a posição de destino da peça
				System.out.print("Posição de destino: ");
				ChessPosition target = UI.readChessPosition(sc);
				
				/* Faz o movimento e verifica se houve captura. 
				 * Se sim, adiciona a peça capturada na lista de peças capturadas.
				 */
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
				if (capturedPiece != null) {
					captured.add(capturedPiece);
				}
				
				/* Verifica se houve promoção de peças. 
				 * Se sim, pergunta qual peça deseja-se obter na promoção
				 * Também verifica se a peça está entre as opções válidas de Cavalo, Bispo, Torre ou Dama
				 */
				if (chessMatch.getPromoted() != null) {
					System.out.print("Entre a peça para promoção (D/C/B/T): ");
					String type = sc.nextLine().toUpperCase();
					
					while (!type.equals("B") && !type.equals("C") && !type.equals("T") && !type.equals("D")) {
						System.out.print("Valor inválido! Entre a peça para promoção (D/C/B/T): ");
						type = sc.nextLine().toUpperCase();
					}
					
					chessMatch.replacePromotedPiece(type);
				}
			}  catch (ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			} 
		}
		
		// Depois do xeque-mate, mostra as informações da partida e a lista de peças capturadas.
		UI.clearScreen();
		UI.printMatch(chessMatch, captured);
	}
}
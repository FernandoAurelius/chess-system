package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

public class UI {
	
	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
	// Constantes para imprimir cores no terminal
	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	// https://stackoverflow.com/questions/2979383/java-clear-the-console
	// Método para limpeza da tela
	public static void clearScreen() {
	 System.out.print("\033[H\033[2J");
	 System.out.flush();
	} 
	
	/* Método para ler uma posição de Xadrez
	 * Lê a posição em String, recorta a letra e o número e instancia uma nova posição de Xadrez
	 */
	public static ChessPosition readChessPosition(Scanner sc) {
		try {
			String s = sc.nextLine();
			char column = s.charAt(0);
			int row = Integer.parseInt(s.substring(1));
		
			return new ChessPosition(column, row);
		} catch (RuntimeException e){
			throw new InputMismatchException("Erro lendo Posição de Xadrez! Valores válidos são de a1 até h8.");
		}
	}
	
	public static void printMatch(ChessMatch chessMatch) {
		printBoard(chessMatch.getPieces());
		System.out.println();
		System.out.println("Turno: " + chessMatch.getTurn());
		System.out.println("Esperando jogador: " + chessMatch.getCurrentPlayer());
	}
	
	/* Imprime o tabuleiro de xadrez no console. 
	* Percorre todas as linhas e colunas do tabuleiro e imprime a peça na posição atual.
	*/
	public static void printBoard(ChessPiece[][] pieces) {
		for (int i = 0; i < pieces.length; i++) {
			System.out.print(ANSI_BLUE + (8 - i) + " ");
			for (int j = 0; j < pieces.length; j++) {
				printPiece(pieces[i][j], false);
			}
			System.out.println();
		}
		System.out.println(ANSI_BLUE + ("  a b c d e f g h"));
	}
	
	/* Sobrecarga do método de impressão do tabuleiro
	 * Imprime também os movimentos possíveis de uma peça
	 */
	public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
		for (int i = 0; i < pieces.length; i++) {
			System.out.print(ANSI_BLUE + (8 - i) + " ");
			for (int j = 0; j < pieces.length; j++) {
				printPiece(pieces[i][j], possibleMoves[i][j]);
			}
			System.out.println();
		}
		System.out.println(ANSI_BLUE + ("  a b c d e f g h"));
	}
	
	/* Imprime uma peça de xadrez no console. 
	 * Se a peça for nula (ou seja, não há peça na posição atual), ele imprime um hífen. 
	 * Se houver uma peça na posição atual, ele verifica a cor da peça. 
	 * Se a cor da peça for branca, ele imprime a peça em branco. 
	 * Se a cor da peça for preta, ele imprime a peça em preto.
	 */
	private static void printPiece(ChessPiece piece, boolean background) {
		if (background) {
			System.out.print(ANSI_GREEN_BACKGROUND);
		}
    	if (piece == null) {
            System.out.print(ANSI_BLACK + ("-") + ANSI_RESET);
        }
        else {
            if (piece.getColor() == Color.BRANCO) {
                System.out.print(ANSI_WHITE + piece + ANSI_RESET);
            }
            else {
                System.out.print(ANSI_BLACK + piece + ANSI_RESET);
            }
        }
        System.out.print(" ");
	}
	
}

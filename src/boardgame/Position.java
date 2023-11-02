package boardgame;

public class Position {

	private int row;
	private int column;
	
	public Position(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	
	// Método para definir o valor de ambas linha e coluna 
	public void setValues (int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	
	// Método toString simples pra retornar a linha concatenada com um espaço em branco e a coluna da posição
	@Override
	public String toString() {
		return row + ", " + column;
	}
	
}
package edu.cmu.ooad.game.ttt;

public class GameBoard {

	Board currentBoard =null;
	protected Board[] boardMoveHistory =  new Board[9];
	protected boolean gameOver = false;
	protected char winner = ' ';

	public GameBoard(Board currentBoard) {
		// TODO Auto-generated constructor stub
		this.currentBoard=currentBoard;
	}
	
	
	public void saveBoardAndIncrementMovesCount(int row, int col,char whiplayer){
		//boardMoveHistory[currentBoard++] =new Board(currentBoard._3x3grid);
		char[][] newGrid = Board.boardCopy(currentBoard._3x3grid);
		boardMoveHistory[currentBoard.getMovesSoFar()] =currentBoard;
		newGrid[row][col]= whiplayer;
		currentBoard = new Board(newGrid);
	}
	
	// row, col are the row and column on which the last move occurred.
	// Returns ' ' if no one won (yet), or 'X' or 'O' to indicate the winner.
	public static char getWinnerForMove(int row, int col, char[][] grid) {
		// Look for three in a row horizontally, on the row on which the last
		// move was made.
		if ((grid[row][0] == grid[row][1]) && (grid[row][0] == grid[row][2]))
			return grid[row][0];
		// Look for three in a column vertically, on the column on which the
		// last move was made.
		if ((grid[0][col] == grid[1][col]) && (grid[0][col] == grid[2][col]))
			return grid[0][col];
		if ((row == col) || (row == (2 - col))) {
			// The row and column are equal or opposite, which means the move
			// fell on a
			// diagonal line.
			// Look for three in a row diagonally.
			if ((grid[0][0] == grid[1][1]) && (grid[0][0] == grid[2][2]))
				return grid[0][0];
			if ((grid[0][2] == grid[1][1]) && (grid[0][2] == grid[2][0]))
				return grid[0][2];
		}
		return ' ';
	}
	
	public void showPlayHistory() {
		for (int i = 1; i < currentBoard.getMovesSoFar(); i++) {
			showGrid(boardMoveHistory[i]._3x3grid);
		}
		showGrid(currentBoard._3x3grid);
	}

	public static void showGrid(char[][] grid) {
		System.out.println("-------");
		for (int r = 0; r < 3; r++) {
			System.out.print('|');
			System.out.print(grid[r][0]);
			System.out.print('|');
			System.out.print(grid[r][1]);
			System.out.print('|');
			System.out.print(grid[r][2]);
			System.out.println('|');
			if (r < 2)
				System.out.println("|-----|");
		}
		System.out.println("-------");
	}

	
}

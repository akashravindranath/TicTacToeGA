package edu.cmu.ooad.game.ttt;

import java.io.PrintStream;
import java.util.Random;

import edu.cmu.ooad.game.ttt.player.GeneticPlayer;
import edu.cmu.ooad.game.ttt.player.Player;
import edu.cmu.ooad.game.ttt.player.StandardPlayer;

public class Game {
	// / protected boolean forcePlayToEnd = false;

	GameBoard gameBoard=null;
	protected char[] movePlayer = new char[9];
	protected int[] moveRow = new int[9];
	protected int[] moveCol = new int[9];
	protected char winner = ' ';
	protected boolean done = false;

	protected Player xPlayer, oPlayer;

	protected Random rand = new Random();

	public Game(Player xPlayer, Player oPlayer) {
		this.xPlayer = xPlayer;
		this.oPlayer = oPlayer;
		init();
	}

	// / public boolean getForcePlayToEnd() { return forcePlayToEnd; }

	// / public void setForcePlayToEnd(boolean forcePlayToEnd) {
	// this.forcePlayToEnd = forcePlayToEnd; }

	public void playGame(PrintStream out, boolean outputGridAtEachStep, boolean outputWinnerAtEnd) {
		init();
		char whichPlayer = (rand.nextInt(2) != 0) ? 'O' : 'X';
		while (!nextMove(whichPlayer)) {
			if ((out != null) && (outputGridAtEachStep)) {
				showGrid();
				out.println();
			}
			whichPlayer = (whichPlayer == 'X') ? 'O' : 'X';
		}
		if ((out != null) && (outputGridAtEachStep))
			showGrid();
		if ((out != null) && (outputWinnerAtEnd)) {
			char winner = getWinner();
			if (winner != ' ') {
				System.out.println(winner + " won.");
			} else {
				System.out.println("Nobody won.");
			}
		}
	}

	public void init() {
		char[][] grid = new char[3][3];

		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				grid[r][c] = ' ';
			}
		}
		//movesSoFar = 0;
		winner = ' ';
		done = false;
		this.gameBoard=new GameBoard(new Board(grid));
	}
	
	public void reset(Board startingBoard){
		this.gameBoard = new GameBoard(startingBoard);			
		this.winner = ' ';
		this.done = false;
		this.moveRow = new int[9];
		this.moveCol = new int[9];

	}

	public void showGrid() {
		GameBoard.showGrid(gameBoard.currentBoard._3x3grid);
	}


	// whichPlayer must be 'X' or 'O'.
	// returns true if this game is over.
	public boolean nextMove(char whichPlayer) {
		if (done)
			return true;
		if ((whichPlayer != 'X') && (whichPlayer != 'O')) {
			whichPlayer = Character.toUpperCase(whichPlayer);
			if ((whichPlayer != 'X') && (whichPlayer != 'O')) {
				throw new Error("nextMove(): whichPlayer must be either 'X' or 'O'");
			}
		}
		Player player = (whichPlayer == 'X') ? xPlayer : oPlayer;
		int idx = player.chooseNextMove(gameBoard.currentBoard._3x3grid, whichPlayer);
		int row = (idx / 3), col = idx - (row * 3);
		if (gameBoard.currentBoard._3x3grid[row][col] != ' ') {
			throw new InternalError("Attempt to put an " + whichPlayer	+ " in row,col " + row + "," + col	+ " which already contains an " + gameBoard.currentBoard._3x3grid[row][col]);
		}
		
		gameBoard.saveBoardAndIncrementMovesCount(row, col, whichPlayer);

		//grid[row][col] = whichPlayer;
		if (((winner = GameBoard.getWinnerForMove(row, col,gameBoard.currentBoard._3x3grid)) != ' ')	
				|| ((gameBoard.currentBoard.getMovesSoFar()-9) == 1)) {
			done = true;
			return true;
		}
		// / if (!forcePlayToEnd) {
		if (!anyPossibleWins(gameBoard.currentBoard._3x3grid)) {
			done = true;
			return true;
		}
		// / }
		return false;
	}



	public char getMovePlayer(int moveIdx) {
		return movePlayer[moveIdx];
	}





	// Examine the grid and see if there are any possible wins. Return true if
	// yes, false if no.
	public static boolean anyPossibleWins(char[][] grid) {
		int xc, oc, sc;
		// Look for possible horizontal runs.
		for (int r = 0; r < 3; r++) {
			xc = oc = sc = 0;
			for (int c = 0; c < 3; c++) {
				switch (grid[r][c]) {
				case 'X':
					xc++;
					break;
				case 'O':
					oc++;
					break;
				default:
					sc++;
					break;
				}
			}
			if (sc >= 2)
				return true;
			if ((sc == 1) && ((xc == 2) || (oc == 2)))
				return true;
			if ((xc == 3) || (oc == 3))
				return true;
		}
		// Look for possible vertical runs.
		for (int c = 0; c < 3; c++) {
			xc = oc = sc = 0;
			for (int r = 0; r < 3; r++) {
				switch (grid[r][c]) {
				case 'X':
					xc++;
					break;
				case 'O':
					oc++;
					break;
				default:
					sc++;
					break;
				}
			}
			if (sc >= 2)
				return true;
			if ((sc == 1) && ((xc == 2) || (oc == 2)))
				return true;
			if ((xc == 3) || (oc == 3))
				return true;
		}
		// Look for possible diagonal runs.
		xc = oc = sc = 0;
		for (int rc = 0; rc < 3; rc++) {
			switch (grid[rc][rc]) {
			case 'X':
				xc++;
				break;
			case 'O':
				oc++;
				break;
			default:
				sc++;
				break;
			}
		}
		if (sc >= 2)
			return true;
		if ((sc == 1) && ((xc == 2) || (oc == 2)))
			return true;
		if ((xc == 3) || (oc == 3))
			return true;
		xc = oc = sc = 0;
		for (int rc = 0; rc < 3; rc++) {
			switch (grid[rc][2 - rc]) {
			case 'X':
				xc++;
				break;
			case 'O':
				oc++;
				break;
			default:
				sc++;
				break;
			}
		}
		if (sc >= 2)
			return true;
		if ((sc == 1) && ((xc == 2) || (oc == 2)))
			return true;
		if ((xc == 3) || (oc == 3))
			return true;
		// The game can't be won.
		return false;
	}

	// Examine the grid and see if there is a win.
	// Returns 'X' or 'O' for the winner, or ' ' for no winner.
	public static char getWinner(char[][] grid) {
		for (int rc = 0; rc < 3; rc++) {
			if (((grid[rc][0] == 'X') || (grid[rc][0] == 'O'))
					&& (grid[rc][1] == grid[rc][0])
					&& (grid[rc][2] == grid[rc][0])) {
				return grid[rc][0];
			}
			if (((grid[0][rc] == 'X') || (grid[0][rc] == 'O'))
					&& (grid[1][rc] == grid[0][rc])
					&& (grid[2][rc] == grid[0][rc])) {
				return grid[0][rc];
			}
		}
		if (((grid[0][0] == 'X') || (grid[0][0] == 'O'))
				&& (grid[1][1] == grid[1][1]) && (grid[2][2] == grid[2][2])) {
			return grid[0][0];
		}
		if (((grid[0][2] == 'X') || (grid[0][2] == 'O'))
				&& (grid[1][1] == grid[1][1]) && (grid[2][0] == grid[2][0])) {
			return grid[0][2];
		}
		return ' ';
	}

	// Returns ' ' if no one won (yet), or 'X' or 'O' to indicate the winner.
	public char getWinner() {
		return winner;
	}

	// row, col are the row and column on which the last move occurred.
	// Returns ' ' if no one won (yet), or 'X' or 'O' to indicate the winner.
	private char getWinnerForMove(int row, int col, char[][] grid) {
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

	public static void main(String[] args) {
		TicTacToeGeneticAlgorithm ttt = new TicTacToeGeneticAlgorithm();

		// Do training.
		System.out.println("Learning...");
		for (int freeCount = 1; freeCount <= 9; freeCount++) {
			for (int pass = 1; pass <= 10; pass++) {
				ttt.createAndTrainFirsteGeneration(freeCount);
				System.out.println("Trained free count " + freeCount+ " generation " + pass);
			}
		}
		
		
		// / Tic tic = new Tic(new RandomPlayer(), new IdealPlayer());
		//Tic tic = new Tic(new IdealPlayer(), new RandomPlayer());
		Game tic = new Game(new GeneticPlayer(), new GeneticPlayer());
		int xWinCount = 0, oWinCount = 0, nWinCount = 0;
		for (int i = 0; i < 10000; i++) {
			// /System.out.println();
			// /System.out.println();
			// /System.out.println();
			// /System.out.println();
			// /System.out.println("==============================");
			tic.playGame(System.out, false, false);
			switch (tic.getWinner()) {
			case 'X':
				xWinCount++;
				break;
			case 'O':
				oWinCount++;
				break;
			default:
				nWinCount++;
				break;
			}
			// /if (tic.getWinner() != ' ') {
			// /if (tic.getWinner() == 'O') {
			// / tic.showPlayHistory(System.out);
			// / break;
			// /}
			tic.showGrid();
		}
		// Output test result counts.
		System.out.println();
		System.out.println("X won: " + xWinCount);
		System.out.println("O won: " + oWinCount);
		System.out.println("Nobody won: " + nWinCount);
	}
	
	
	public static void main1(String[] args) {
		TicTacToeGeneticAlgorithm ttt = new TicTacToeGeneticAlgorithm();

		// Do training.
		System.out.println("Learning...");
		for (int freeCount = 1; freeCount <= 9; freeCount++) {
			for (int pass = 1; pass <= 10; pass++) {
				ttt.createAndTrainFirsteGeneration(freeCount);
				System.out.println("Trained free count " + freeCount+ " generation " + pass);
			}
		}
		
		GeneticPlayer geneticPlayer = new GeneticPlayer();
		// Do competition.
		int numTestPasses = 100000;
		for (int whichOpponent = 1; whichOpponent <= 2; whichOpponent++) {
			Player opponent = new GeneticPlayer();
			System.out.println("Competing against "	+ opponent.getClass().getName() + "...");
			Game tic = new Game(geneticPlayer, opponent);
			int xWinCount = 0, oWinCount = 0, nWinCount = 0;
			for (int pass = 0; pass < numTestPasses; pass++) {
				tic.playGame(null, false, false);
				switch (tic.getWinner()) {
				case 'X':
					xWinCount++;
					break;
				case 'O':
					oWinCount++;
					break;
				default:
					nWinCount++;
					break;
				}
			}
			// Output test result counts.
			System.out.println("X won: " + xWinCount);
			System.out.println("O won: " + oWinCount);
			System.out.println("Nobody won: " + nWinCount);
		}
	}
}

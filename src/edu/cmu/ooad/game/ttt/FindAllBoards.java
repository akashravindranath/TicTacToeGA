package edu.cmu.ooad.game.ttt;

import java.util.ArrayList;
import java.util.List;

public class FindAllBoards {


	public static Board[] findAllBoards() {
		List<Board> allBoards = new ArrayList<Board>();
		char[] possiblePlayerBoard = new char[9];

		Board flippedOrRotatedBoard = new Board(possiblePlayerBoard);
		int counter=1;
		// Each element = 0 for space, 1 for X, 2 for O.
		int[] possibleNumericBoard = new int[9];
		for (boolean hasMorePossibleBoards = false; !hasMorePossibleBoards;) {
			
			convertNextBoardToPlayerMoves(possibleNumericBoard,possiblePlayerBoard);		
			hasMorePossibleBoards = nextBoard(possibleNumericBoard);
			// Don't save any winning boards.
			// Don't save any full boards, or 
			// Any boards where one player is more than one move ahead of the other player.			
			if (hasAnyPlayerWon(possiblePlayerBoard) || !Board.hasMoreMoves(possiblePlayerBoard) || Board.isAnyPlayerAheadOfOther(possiblePlayerBoard)) {
				continue;
			}

			// Don't save any boards which are duplicates of other boards,
			// including rotated and/or flipped duplicates.
			Board board = new Board(possiblePlayerBoard);
			board.copyTo(flippedOrRotatedBoard);
			boolean isDup = false;
			for (int flipPass = 1; ((flipPass <= 2) && (!isDup)); flipPass++) {
				if (allBoards.contains(flippedOrRotatedBoard) || 
						allBoards.contains(flippedOrRotatedBoard.rotateBoard90DegreesRight())  || 
						allBoards.contains(flippedOrRotatedBoard.rotateBoard90DegreesRight()) || 
						allBoards.contains(flippedOrRotatedBoard.rotateBoard90DegreesRight())) {
					counter++;
					isDup = true;
					break;
				}
				flippedOrRotatedBoard.mirrorHorizontally();
			}
			if (isDup) {
				continue;
			}
			
			allBoards.add(board);
			
		}
		//System.out.println("counter==="+counter);
		return (Board[]) allBoards.toArray(new Board[allBoards.size()]);
	}
	
	public static void convertNextBoardToPlayerMoves(int[] cellVal,char[] grid){
		for (int i = 0; i < 9; i++) {
			switch (cellVal[i]) {
			case 0:
				grid[i] = ' ';
				break;
			case 1:
				grid[i] = 'X';
				break;
			case 2:
				grid[i] = 'O';
				break;
			}
		}
	}
	
	public static boolean nextBoard(int[] cellVal){
		for (int i = 0; i < 9; i++) {
			cellVal[i]++;
			if (cellVal[i] < 3)
				break;
			cellVal[i] = 0;
			if (i == 8)
				return true;
			
				
		}
		
		return false;
	}
	
	public static boolean hasAnyPlayerWon(char[] possiblePlayerBoard){
		boolean isWin = false;
		for (int rc = 0; rc < 3; rc++) {
			if ((possiblePlayerBoard[(rc * 3)] != ' ')
					&& (possiblePlayerBoard[(rc * 3)] == possiblePlayerBoard[(rc * 3) + 1])
					&& (possiblePlayerBoard[(rc * 3)] == possiblePlayerBoard[(rc * 3) + 2])) {
				isWin = true;
				break;
			}
			if ((possiblePlayerBoard[rc] != ' ') && (possiblePlayerBoard[rc] == possiblePlayerBoard[3 + rc])
					&& (possiblePlayerBoard[rc] == possiblePlayerBoard[6 + rc])) {
				isWin = true;
				break;
			}
		}
		if(!isWin){
			if ((possiblePlayerBoard[0] != ' ') && (possiblePlayerBoard[0] == possiblePlayerBoard[4])
					&& (possiblePlayerBoard[0] == possiblePlayerBoard[8])) {
				isWin = true;
			} else {
				if ((possiblePlayerBoard[2] != ' ') && (possiblePlayerBoard[2] == possiblePlayerBoard[4])
						&& (possiblePlayerBoard[2] == possiblePlayerBoard[6])) {
					isWin = true;
				}
			}
		}
		
		return isWin;
	}
	

	public static void main(String[] args) {
		Board[] boards = findAllBoards();
		for (int i = 0; i < boards.length; i++) {
			System.out.println(boards[i]);
		}
		// /
		System.err.println("Total unique boards: " + boards.length);
	}
}

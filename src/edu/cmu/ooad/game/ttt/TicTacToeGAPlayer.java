package edu.cmu.ooad.game.ttt;


public class TicTacToeGAPlayer {

	Board[] allBoards = null;
	private int[] allBoardsXMoves = null;
	private int[] allBoardsOMoves = null;
	
	public void createAndTrainFirsteGeneration(int freeCount) {
		allBoards = FindAllBoards.findAllBoards();
		allBoardsXMoves = new int[allBoards.length];
		allBoardsOMoves = new int[allBoards.length];

	
	}
}

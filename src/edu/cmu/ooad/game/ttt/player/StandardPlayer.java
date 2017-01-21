package edu.cmu.ooad.game.ttt.player;

import edu.cmu.ooad.game.ttt.GameBoard;

public class StandardPlayer extends AbstractPlayer implements Player {

	private  char whichPlayer;
	private int firstMove;
	private boolean firstMoveDone = false;
	
	public StandardPlayer(char whichPlayer){
		this.whichPlayer=whichPlayer;
	}
	
	public StandardPlayer(){
		
	}
	
	@Override
	public int chooseNextMove(char[][] grid, char whichPlayer) {
		if (whichPlayer != this.whichPlayer) {
			throw new InternalError("Expected whichPlayer to be '"+ this.whichPlayer + "'");
		}
		setGridAndFindFreeCells(grid);
		if (!firstMoveDone) {
			firstMoveDone = true;
			if (grid[firstMove / 3][firstMove % 3] != ' ') {
				GameBoard.showGrid(grid);
				throw new InternalError("Attempted first move into non-empty space; firstMove=" + firstMove);
			}
			return firstMove;
		}
		return chooseNextMove(grid, whichPlayer);
	}

	
			
	
}

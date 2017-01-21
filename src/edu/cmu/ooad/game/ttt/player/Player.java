package edu.cmu.ooad.game.ttt.player;

public interface Player {

	public abstract int chooseNextMove(char[][] grid, char whichPlayer);
}

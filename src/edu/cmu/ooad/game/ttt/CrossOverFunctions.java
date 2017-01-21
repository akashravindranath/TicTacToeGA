package edu.cmu.ooad.game.ttt;

public interface CrossOverFunctions {

	public void randomCrossOverFuntion(boolean[] parent1, boolean[] parent2, boolean[] child);
	public void incrementCrossOverFuntion(boolean[] parent1, boolean[] parent2, boolean[] child);
	public void onePointCrossOver(boolean[] parent1, boolean[] parent2, boolean[] child);
}

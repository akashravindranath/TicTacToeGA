package edu.cmu.ooad.game.ttt;

import java.util.Random;

public class ReproductionFunction implements CrossOverFunctions {

	
	public ReproductionFunction(double mutationRate){
		this.mutationRate = mutationRate;
	}
	
	public void replication(boolean[] parent1,boolean[] child){
		
	}

	public void mutation(boolean[] parent1,boolean[] child){
		
	}

	private boolean[] bits;
	@Override
	public void incrementCrossOverFuntion(boolean[] parent1, boolean[] parent2, boolean[] child) {
		// TODO Auto-generated method stub
		System.arraycopy(bits, 0, child, 0, bits.length);
		for (int i = bits.length - 1; i >= 0; i--) {
			bits[i] = !bits[i];
			if (bits[i])
				break;
		}
	}
	
	protected double mutationRate;
	protected Random rand = new Random();
	@Override
	public void onePointCrossOver(boolean[] parent1, boolean[] parent2, boolean[] child) {
		// TODO Auto-generated method stub

		int i, crossoverPoint = rand.nextInt(child.length);
		for (i = 0; i < crossoverPoint; i++) {
			child[i] = parent1[i];
		}
		for (i = crossoverPoint; i < child.length; i++) {
			child[i] = parent2[i];
		}
		for (i = 0; i < child.length; i++) {
			if (rand.nextDouble() < mutationRate) {
				child[i] = !child[i];
			}
		}
	
		
	}
	
	@Override
	public void randomCrossOverFuntion(boolean[] parent1, boolean[] parent2, boolean[] child) {
		// TODO Auto-generated method stub
		for (int i = 0; i < child.length; i++) {
			child[i] = (rand.nextInt(2) != 0) ? true : false;
		}
	}
}

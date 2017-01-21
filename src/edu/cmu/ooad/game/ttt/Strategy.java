package edu.cmu.ooad.game.ttt;

import java.util.Comparator;
import java.util.Random;

public class Strategy implements Individual , Comparator<Strategy>{
	// Gene Representation
	boolean geneSequence[] = null;
	protected final Random rand = new Random();

	protected FitnessFunction fitnessFunction;
	protected CrossOverFunctions crossOverFunctions;
	protected double fitness;
	public static int BITS_PER_INDIVIDUAL =4;
	public Strategy() {
		// TODO Auto-generated constructor stub
		geneSequence = new boolean[4];
	}
	
	public int getNextMoveKey(){
		int moveKey = (geneSequence[0] ? 8 : 0) | (geneSequence[1] ? 4 : 0) | (geneSequence[2] ? 2 : 0) | (geneSequence[3] ? 1 : 0);
		return moveKey;
	}
	
	@Override
	public double getFitness() {
		// TODO Auto-generated method stub
		return fitness;
	}
	

	public boolean[] getGeneSequence() {
		return geneSequence;
	}

	public void setGeneSequence(boolean[] geneSequence) {
		this.geneSequence = geneSequence;
	}

	public Strategy(FitnessFunction fitnessFunction,
			CrossOverFunctions crossOverFunctions) {

		geneSequence = new boolean[4];
		this.fitnessFunction = fitnessFunction;
		this.crossOverFunctions = crossOverFunctions;
	}

	public void randomize() {
		for (int i = 0; i < geneSequence.length; i++) {
			geneSequence[i] = rand.nextBoolean();
		}
	}

	public void calculateFitness() {
		fitness = fitnessFunction.calculateFitness(this);
	}

	public void reproduce(Individual spouse, Individual child) {
		crossOverFunctions.onePointCrossOver(geneSequence, spouse.getGeneSequence(), child.getGeneSequence());
	//	crossOverFunctions.randomCrossOverFuntion(bits, spouse.getGeneSequence(), child.getGeneSequence());
	//	crossOverFunctions.incrementCrossOverFuntion(bits, spouse.getGeneSequence(), child.getGeneSequence());
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Individual) {
			if (((Individual) o).getGeneSequence().length == geneSequence.length) {
				for (int i = 0; i < geneSequence.length; i++) {
					if (((Individual) o).getGeneSequence()[i] != geneSequence[i]) {
						return false;
					}
				}
			}
		}
		return super.equals(o);
	}

	@Override
	public int compare(Strategy o1, Strategy o2) {
		if (((Individual) o1).getFitness() > ((Individual) o2).getFitness())
			return -1;
		if (((Individual) o1).getFitness() < ((Individual) o2).getFitness())
			return 1;
		return 0;
	}
}

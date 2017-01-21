package edu.cmu.ooad.game.ttt;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;


public class GeneticAlgorithm {
	protected final Random rand = new Random();
	private boolean recalcFitnessForSurvivors = false;
	protected FitnessFunction fitnessFunction;
	public static final int NUM_INDIVIDUALS = 32;
	public static final int NUM_SURVIVORS =4;

	protected Comparator reverseFitnessComparator = new Comparator() {
		@Override
		public int compare(Object o1, Object o2) {
			if (((Individual) o1).getFitness() > ((Individual) o2).getFitness())
				return -1;
			if (((Individual) o1).getFitness() < ((Individual) o2).getFitness())
				return 1;
			return 0;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			return false;
		}
	};
	protected Individual[] individuals;

	public GeneticAlgorithm(FitnessFunction fitnessFunction,
			CrossOverFunctions crossOverFunctions) {

		this.fitnessFunction = fitnessFunction;
		individuals = new Individual[NUM_INDIVIDUALS];

		for (int i = 0; i < NUM_INDIVIDUALS; i++) {
			individuals[i] = new Strategy(fitnessFunction,	crossOverFunctions);
		}

		randomize();
	}

	public boolean getRecalcFitnessForSurvivors() {
		return recalcFitnessForSurvivors;
	}

	public void setRecalcFitnessForSurvivors(boolean recalcFitnessForSurvivors) {
		this.recalcFitnessForSurvivors = recalcFitnessForSurvivors;
	}

	public void randomize() {
		for (int i = 0; i < NUM_INDIVIDUALS; i++) {
			individuals[i].randomize();
			individuals[i].calculateFitness();
		}
		Arrays.sort(individuals,reverseFitnessComparator);
	}

	public void runOneGeneration() {
		for (int ci = NUM_SURVIVORS, i1 = 0, i2 = 0; ci < NUM_INDIVIDUALS;) {
			i1 = rand.nextInt(NUM_SURVIVORS);
			i2 = rand.nextInt(NUM_SURVIVORS);
			if ((i1 != i2) && (!individuals[i1].equals(individuals[i2]))) {
				individuals[i1].reproduce(individuals[i2], individuals[ci]);
				if (!recalcFitnessForSurvivors) {
					individuals[ci].calculateFitness();
				}
				ci++;
			}
		}
		if (recalcFitnessForSurvivors) {
			for (int i = 0; i < NUM_INDIVIDUALS; i++) {
				individuals[i].calculateFitness();
			}
		}

		Arrays.sort(individuals, reverseFitnessComparator);
		// Shuffle the most fit individuals.
		int numWithMostFitScore = 0;
		while ((numWithMostFitScore < NUM_INDIVIDUALS)
				&& (individuals[numWithMostFitScore].getFitness() == individuals[0].getFitness())) {
			numWithMostFitScore++;
		}
		if (numWithMostFitScore >= 2) {
			for (int swaps = numWithMostFitScore - 1; swaps > 0;) {
				int i1 = rand.nextInt(numWithMostFitScore);
				int i2 = rand.nextInt(numWithMostFitScore);
				if (i1 != i2) {
					Individual ind1 = individuals[i1];
					individuals[i1] = individuals[i2];
					individuals[i2] = ind1;
					swaps--;
				}
			}
		}
	}

	public boolean[] getIndividualSolution(int individualIdx) {
		return individuals[individualIdx].getGeneSequence();
	}

	public double getIndividualFitness(int individualIdx) {
		return individuals[individualIdx].getFitness();
	}
}

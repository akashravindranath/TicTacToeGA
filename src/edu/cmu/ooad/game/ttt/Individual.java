package edu.cmu.ooad.game.ttt;

public interface Individual {

	public double getFitness();
	
	public boolean[] getGeneSequence();

	public void setGeneSequence(boolean[] geneSequence);
	
	public void randomize();

	public void calculateFitness();
	
	public void reproduce(Individual spouse, Individual child);
}

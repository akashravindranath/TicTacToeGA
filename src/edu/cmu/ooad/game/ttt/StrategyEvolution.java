package edu.cmu.ooad.game.ttt;

public class StrategyEvolution {

	//916* 16;
	final int populationsize = 916*16;
			
	public void createInitialPopulation(){
		Individual individual = null;
		for(int index =0;index<=populationsize;index++){
			individual = new Strategy();
		}
	}
}

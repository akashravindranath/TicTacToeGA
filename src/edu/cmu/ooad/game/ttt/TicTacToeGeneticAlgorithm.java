package edu.cmu.ooad.game.ttt;

import java.util.Arrays;
import java.util.Comparator;

public class TicTacToeGeneticAlgorithm {

	public static Board[] allBoards = null;
	private static int[] allBoardsXMoves = null;
	private static int[] allBoardsOMoves = null;
	private static GeneticAlgorithm[] gax;
	private static GeneticAlgorithm[] gao;
	
	private static final double MUTATION_RATE = 0.2;
	private static final int NUM_INDIVIDUALS = 32;
	private static final int NUM_SURVIVORS = 4;
	
	
	@SuppressWarnings("unchecked")
	public static void createAndTrainFirsteGeneration(int freeCount) {
		if (allBoards == null) {
			//allBoards = FindAllBoards.findAllBoards();
			char grid[][]={{'X','O','O'},{'O','X','X'},{'O',' ',' '}};
			Board test = new Board(grid);
			Board array[] ={test};
			allBoards = array;
			
			Arrays.sort(allBoards, new Comparator<Board>() {
				@Override
				public int compare(Board o1, Board o2) {
					char[][] b1 = o1.get3x3Grid();
					char[][] b2 = o2.get3x3Grid();
					int n1 = 0, n2 = 0;
					for (int r = 0; r < 3; r++) {
						for (int c = 0; c < 3; c++) {
							if (b1[r][c] == ' ')
								n1++;
							if (b2[r][c] == ' ')
								n2++;
						}
					}
					if (n1 > n2)
						return 1;
					if (n1 < n2)
						return -1;
					return 0;
				}

				@Override
				public boolean equals(Object obj) {
					if (obj == this)
						return true;
					return false;
				}
			});
			

			allBoardsXMoves = new int[allBoards.length];
			allBoardsOMoves = new int[allBoards.length];
			gax = new GeneticAlgorithm[allBoards.length];
			gao = new GeneticAlgorithm[allBoards.length];

			char ch;
			int movesSoFar;
			Board currentBoard = null;
			for (int boardIdx = 0; boardIdx < allBoards.length; boardIdx++) {
				currentBoard = allBoards[boardIdx];
				if (currentBoard.getMovesPlayerX() <= currentBoard.getMovesPlayerO()) {
					gax[boardIdx] = new GeneticAlgorithm(new FitnessFunctionGAPlayer(allBoards[boardIdx],'X'),	new ReproductionFunction(MUTATION_RATE));
					gax[boardIdx].setRecalcFitnessForSurvivors(true);
				}
				if (currentBoard.getMovesPlayerO() <= currentBoard.getMovesPlayerX()) {
					gao[boardIdx] = new GeneticAlgorithm(new FitnessFunctionGAPlayer(allBoards[boardIdx],'O'),
							new ReproductionFunction(MUTATION_RATE));
					gao[boardIdx].setRecalcFitnessForSurvivors(true);
				}
			}
		} // if (allBoards == null)

		for (int boardIdx = 0; boardIdx < allBoards.length; boardIdx++) {
			if ((9 - (allBoardsXMoves[boardIdx] + allBoardsOMoves[boardIdx])) == freeCount) {
				if (gax[boardIdx] != null) {
					gax[boardIdx].runOneGeneration();
					// No need to recalculate fitness for survivors after the
					// first pass.
					gax[boardIdx].setRecalcFitnessForSurvivors(false);
				}
				if (gao[boardIdx] != null) {
					gao[boardIdx].runOneGeneration();
					// No need to recalculate fitness for survivors after the
					// first pass.
					gao[boardIdx].setRecalcFitnessForSurvivors(false);
				}
			}
		}
	}


	public static GeneticAlgorithm[] getXPlayerEvolutions() {
		return gax;
	}


	public static GeneticAlgorithm[] getOPlayerEvolutions() {
		return gao;
	}
	
	
}

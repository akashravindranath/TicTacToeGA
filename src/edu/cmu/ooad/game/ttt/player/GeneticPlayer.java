package edu.cmu.ooad.game.ttt.player;

import java.util.Arrays;
import java.util.Random;

import edu.cmu.ooad.game.ttt.Board;
import edu.cmu.ooad.game.ttt.GameBoard;
import edu.cmu.ooad.game.ttt.GeneticAlgorithm;
import edu.cmu.ooad.game.ttt.Individual;
import edu.cmu.ooad.game.ttt.Strategy;
import edu.cmu.ooad.game.ttt.TicTacToeGeneticAlgorithm;

public class GeneticPlayer extends AbstractPlayer {

	private static final int BITS_PER_INDIVIDUAL = 4;
	private static final double MUTATION_RATE = 0.2;
	private static final int NUM_SURVIVORS = 4;
	
	private Board[] allBoards = null;

	private Random rand = new Random();
	private boolean initialized = false;

	private Individual individual = null;
	private boolean firstMoveDone = false;
	
	public GeneticPlayer(Individual individual){
		this.individual=individual;
	}
	
	public GeneticPlayer(){
	}
	
	public int chooseFirstMove(char[][] grid, char whichPlayer) {
		boolean[] geneSequence = individual.getGeneSequence();
		int firstMove = (geneSequence[0] ? 8 : 0) | (geneSequence[1] ? 4 : 0)	| (geneSequence[2] ? 2 : 0)	| (geneSequence[3] ? 1 : 0);
		setGridAndFindFreeCells(grid);
		if (!firstMoveDone) {
			firstMoveDone = true;
			if (firstMove >= 9 || grid[firstMove / 3][firstMove % 3] != ' ') {
				GameBoard.showGrid(grid);
				throw new InternalError("Attempted first move into non-empty space; firstMove=" + firstMove);
			}
			return firstMove;
		}
		return chooseNextMove(grid, whichPlayer);
	}
	
	@Override
	public synchronized int chooseNextMove(char[][] grid, char whichPlayer) {
		//GameBoard.showGrid(grid);;
		if(individual!=null && !firstMoveDone){
			return chooseFirstMove(grid,whichPlayer);
		}
		allBoards = TicTacToeGeneticAlgorithm.allBoards;
		//Arrays.sort(allBoards);
		char[][] tmpGrid = new char[3][3];

		setGridAndFindFreeCells(grid);

		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				tmpGrid[r][c] = grid[r][c];
			}
		}
		Board tempBoard = null;
		GeneticAlgorithm[] ga = (whichPlayer == 'X') ? TicTacToeGeneticAlgorithm.getXPlayerEvolutions() : TicTacToeGeneticAlgorithm.getOPlayerEvolutions();

		int unrotflpmove = -1, move = -1;
		boolean boardFound = false;
		int boardIdx = -1;
		for (int rotPass = 0; ((rotPass < 4) && (!boardFound)); rotPass++) {
			for (int flipPass = 0; ((flipPass < 2) && (!boardFound)); flipPass++) {
				for (int i = 0; i < allBoards.length; i++) {
					if (ga[i] != null) {
						tempBoard = new Board(tmpGrid);
						if (allBoards[i].equals(tempBoard)) {
							boardFound = true;
							boardIdx = i;
							boolean[] solution = ga[i].getIndividualSolution(0);
							unrotflpmove = move = (solution[0] ? 8 : 0) | (solution[1] ? 4 : 0)	| (solution[2] ? 2 : 0)	| (solution[3] ? 1 : 0);
							if ((move >= 0) && (move < 9)) {
								int mr = move / 3, mc = move % 3;
								if (tmpGrid[mr][mc] == ' ') {
									if (flipPass > 0) {
										move = Board.unflipHMove(move);
									}
									for (int rp = 0; rp < rotPass; rp++) {
										move = Board.unrotate90CCWMove(move);
									}
									return move;
								}
							}
							break;
						} // if (match)
					} // if (ga[i] != null)
				} // for (int i = 0; i < allBoards.length; i++)
				Board.flipHGrid(tmpGrid);
			} // for (int flipPass = 0; ( (flipPass < 2) && (!boardFound) );
				// flipPass++)
			Board.rotate90CCWGrid(tmpGrid);
		} // for (int rotPass = 0; ( (rotPass < 4) && (!boardFound) );
			// rotPass++)

		if (initialized) {
			if (boardIdx >= 0) {
				for (int j = 0; j < GeneticAlgorithm.NUM_INDIVIDUALS; j++) {
					System.out.print(" ind " + j + " fitness="
							+ ga[boardIdx].getIndividualFitness(j) + " ");
					boolean[] bits = ga[boardIdx].getIndividualSolution(j);
					for (int k = 0; k < Strategy.BITS_PER_INDIVIDUAL; k++) {
						System.out.print(bits[k] ? '1' : '0');
					}
					System.out.println();
				}
				System.out.println("Found board:");
				GameBoard.showGrid(allBoards[boardIdx].get3x3Grid());
			}
			System.out.println("Game board:");
			GameBoard.showGrid(grid);
			System.out.println("whichPlayer=" + whichPlayer + " boardFound="
					+ boardFound + " boardIdx=" + boardIdx
					+ " allBoardsXHasDoubleWin[boardIdx]="/*
														 * +allBoardsXHasDoubleWin
														 * [boardIdx]+
														 * " allBoardsOHasDoubleWin[boardIdx]="
														 * +
														 * allBoardsOHasDoubleWin
														 * [boardIdx]
														 */+ " unrotflpmove="
					+ unrotflpmove + " move=" + move);
			throw new InternalError("got random move after initialization");
		}
		if(tempBoard!=null)
			freeCount = 9-tempBoard.getMovesSoFar();
		else
			freeCount = 9-Board.getMovesSoFar(tmpGrid);
		
		int freeIdx = rand.nextInt(freeCount);
		return (freeRow[freeIdx] * 3) + freeCol[freeIdx];
	}
}

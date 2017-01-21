package edu.cmu.ooad.game.ttt;

import edu.cmu.ooad.game.ttt.player.GeneticPlayer;
import edu.cmu.ooad.game.ttt.player.Player;
import edu.cmu.ooad.game.ttt.player.SequencePlayer;

public class FitnessFunctionGAPlayer implements FitnessFunction {

	private final char[][] startingBoard;
	private final int startingBoardMovesSoFar;
	private final char whichPlayer, opponent;
	private final SequencePlayer opponentPlayer = new SequencePlayer();
	private int firstMove;
	private  Game game;
	
	public FitnessFunctionGAPlayer(Board startingBoard, char whichPlayer) {

		this.startingBoard = startingBoard._3x3grid;
		this.startingBoardMovesSoFar = startingBoard.getMovesSoFar();
		this.whichPlayer = whichPlayer;
		this.opponent = (whichPlayer == 'X') ? 'O' : 'X';

		
	}
	
	@Override
	public double calculateFitness(Individual individual) {
		boolean strategy[] =  individual.getGeneSequence();
		Player gPlayer = new GeneticPlayer(individual);

		if (whichPlayer == 'X') {
			game = new Game(gPlayer, opponentPlayer);
		} else {
			game = new Game(opponentPlayer, gPlayer);
		}
		
		firstMove = (strategy[0] ? 8 : 0) | (strategy[1] ? 4 : 0) | (strategy[2] ? 2 : 0) | (strategy[3] ? 1 : 0);
		if (firstMove >= 9) {
			// Move position is out of bounds.
			return -1.0;
		}
		if (startingBoard[firstMove / 3][firstMove % 3] != ' ') {
			// Move position already contains a move.
			return -1.0;
		}

		// Play some games against a sequence player, starting with the reference
		// board and letting the individual select the next move based on its solution,
		// and then playing moves we've already learned from there forward.
		// The number of games the individual wins or ties is the fitness.

		int numGames = 1;
		for (int fc = (9 - startingBoardMovesSoFar) - 1; fc > 0; fc -= 2)
			numGames *= fc;
		//System.out.println("************************************START****************************************");
		int numGamesPlayed = 0, fitness = 0;
		opponentPlayer.reset((9 - startingBoardMovesSoFar) - 1);
		do {
			//GameBoard.showGrid(startingBoard);
			game.reset(new Board(startingBoard));			

			char wp = whichPlayer;
			while (!game.nextMove(wp)) {
				wp = (wp == 'X') ? 'O' : 'X';
			}
			numGamesPlayed++;
			char winner = game.getWinner();
			// If the first move causes a win, return numGames+1
			// to make this move score higher than all others.
			if ((winner == whichPlayer)
					&& (game.gameBoard.currentBoard.getMovesSoFar() == (startingBoardMovesSoFar + 1))) {
				return numGames + 1;
			}
			if (winner != opponent) {
				fitness++;
			}
			opponentPlayer.gameDone();
		} while (!opponentPlayer.isSequenceDone());
		//System.out.println("************************************END****************************************");
		if (numGamesPlayed != numGames) {
			throw new InternalError("Unexpected # games played; numGames="+ numGames + " numGamesPlayed=" + numGamesPlayed);
		}

		return fitness;
	}
	

}

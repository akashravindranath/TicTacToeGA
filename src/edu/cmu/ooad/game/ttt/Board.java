package edu.cmu.ooad.game.ttt;

import java.util.Arrays;
import java.util.Comparator;


public class Board implements Comparable<Board>, Comparator<Board>{
	char[][] _3x3grid = new char[3][3];
	public final char[] _grid = new char[9];
	private int movesPlayerX =0;
	private int movesPlayerO =0;
	private int movesSoFar =0;
	private boolean hasMoreMoves =true;
	
	public Board(char[] grid) {
		System.arraycopy(grid, 0, this._grid, 0, 9);
		copyToTwoDArray();
		countMovesSoFar();
	}

	public Board(char[][] _3x3grid) {
//		System.arraycopy(_3x3grid, 0, this._3x3grid, 0, 3);
		System.arraycopy(_3x3grid, 0, this._3x3grid, 0, _3x3grid.length);
		copyToOneDArray();
		countMovesSoFar();
	}
	
	public void copyToOneDArray(){
		for(int i=0;i<3;i++){
			System.arraycopy(this._3x3grid[i], 0, this._grid, (i*3), 3);
		}
	}
	
	public void copyToTwoDArray(){
		for(int i=0;i<3;i++){
			System.arraycopy(this._grid, (i*3), this._3x3grid[i], 0, 3);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(_3x3grid);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (!Arrays.deepEquals(_3x3grid, other._3x3grid))
			return false;
		return true;
	}
	
	public void copyTo(Board dest) {
		System.arraycopy(_grid, 0, dest._grid, 0, 9);
		System.arraycopy(_3x3grid, 0, dest._3x3grid, 0, _3x3grid.length);
	}

	public char[][] get3x3Grid() {
		return _3x3grid;
	}
	
	
	public int getMovesPlayerX() {
		return movesPlayerX;
	}

	public void setMovesPlayerX(int movesPlayerX) {
		this.movesPlayerX = movesPlayerX;
	}

	public int getMovesPlayerO() {
		return movesPlayerO;
	}

	public void setMovesPlayerO(int movesPlayerO) {
		this.movesPlayerO = movesPlayerO;
	}

	public boolean hasMoreMoves() {
		return hasMoreMoves;
	}


	public int getMovesSoFar() {
		return movesSoFar;
	}

	public void setMovesSoFar(int movesSoFar) {
		this.movesSoFar = movesSoFar;
	}

	public Board rotateBoard90DegreesRight(){
		//System.out.println(_3x3grid.length);
		int numberofLayers =_3x3grid.length / 2;
		
		for (int layer = 0; layer <numberofLayers; layer++) {
			int layerStart = layer;
			//System.out.println("Layer ---------------> "+layer );
			int layerEnd = (_3x3grid.length) - 1 - layer;		
			//System.out.println("last ----------------> "+layerEnd );
			for (int i = layerStart; i < layerEnd; ++i) {
				int offset = i - layerStart;
				
				char top = _3x3grid[layerStart][i];
				
				// left -> top
				_3x3grid[layerStart][i] =_3x3grid[layerEnd-offset][layerStart];
				//printArray(matrix);
				
				// bottom -> left
				_3x3grid[layerEnd-offset][layerStart] =_3x3grid[layerEnd][layerEnd-offset];
				//printArray(matrix);
				
				// right -> bottom
				_3x3grid[layerEnd][layerEnd-offset] =_3x3grid[i][layerEnd];
				
				// top -> right
				_3x3grid[i][layerEnd] =top;
				//printArray(matrix);
				
				//System.out.println("================");
			}
		//	System.out.println("***********************");
		}
		copyToOneDArray();
		return this;
	}
	
	public char[][] mirrorVerically() {
		int height = _3x3grid.length;
		
		for (int i = 0; i < height; i++) {
			int width = _3x3grid[i].length;
			int numberofLayers =width / 2;
			for (int layer = 0; layer <numberofLayers; layer++) {
				char temp = _3x3grid[i][width-layer-1];
				_3x3grid[i][width-layer-1] = _3x3grid[i][layer];
				_3x3grid[i][layer] = temp;
			}
		}
		copyToOneDArray();
		return _3x3grid;
	}
	
	
	public char[][] mirrorHorizontally() {
		int height = _3x3grid.length;
		int numberofLayers =height / 2;
		for (int h = 0; h < numberofLayers; h++) {
			int width = _3x3grid[h].length;
			for (int w = 0; w <width; w++) {
				char temp = _3x3grid[height-h-1][w];
				_3x3grid[height-h-1][w] = _3x3grid[h][w];
				_3x3grid[h][w] = temp;
			}
		}
		copyToOneDArray();
		return _3x3grid;
	}

	
	public static char[][] boardCopy(char[][] arr){
    	if(arr!=null){
    		char[][] arrCopy = new char[arr.length][] ;
    		for(int i=0;i<arr.length;i++){
    			//System.arraycopy(this._grid, (i*3), this._3x3grid[i], 0, 3);
    			arrCopy[i] = new char[arr[i].length];
    			System.arraycopy(arr[i], 0, arrCopy[i], 0, arr[i].length);
    		}
	        return arrCopy;
    	}else { 
    		return new char[][]{};
    	}
	}
	
	public String toHumanReadable() {
		String nl = System.getProperty("line.separator");
		StringBuffer sb = new StringBuffer();
		for (int r = 0; r < 3; r++) {
			if (r > 0) {
				sb.append("-+-+-");
				sb.append(nl);
			}
			sb.append(_grid[(r * 3)]);
			sb.append('|');
			sb.append(_grid[(r * 3) + 1]);
			sb.append('|');
			sb.append(_grid[(r * 3) + 2]);
			sb.append(nl);
		}
		return sb.toString();
	}
	
	
	public  static boolean hasMoreMoves(char[] possiblePlayerBoard){
		boolean hasEmptyCell = false;
		for (int i = 0; i < 9; i++) {
			switch (possiblePlayerBoard[i]) {
			case 'X':
				break;
			case 'O':
				break;
			default:
				hasEmptyCell = true;
				break;
			}
		}
		return (hasEmptyCell);
	}
	
	
	public void countMovesSoFar(){
		int countXmoves= 0;
		int countOmoves= 0;
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				switch (_3x3grid[r][c]) {
				case 'X':
					countXmoves++;
					break;
				case 'O':
					countOmoves++;
					break;
				default:
					hasMoreMoves = true;
					break;					
				}
			}
		}
		setMovesPlayerX(countXmoves);
		setMovesPlayerO(countOmoves);
		setMovesSoFar(countXmoves+countOmoves);
	
	}
	
	public static int getMovesSoFar(char[][] grid){
		int countXmoves= 0;
		int countOmoves= 0;
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				switch (grid[r][c]) {
				case 'X':
					countXmoves++;
					break;
				case 'O':
					countOmoves++;
					break;
				default:
					break;					
				}
			}
		}
		return countOmoves+countXmoves;
	}
	
	public  static boolean isAnyPlayerAheadOfOther(char[] possiblePlayerBoard){
		int xMoves = 0, oMoves = 0;
		for (int i = 0; i < 9; i++) {
			switch (possiblePlayerBoard[i]) {
			case 'X':
				xMoves++;
				break;
			case 'O':
				oMoves++;
				break;
			}
		}
		return (Math.abs(xMoves - oMoves) > 1);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 9; i++)
			sb.append(_grid[i]);
		return sb.toString();
	}
	
	@Override
	public int compareTo(Board board) {
		// TODO Auto-generated method stub
			char[][] b1 = (char[][]) this._3x3grid;
			char[][] b2 = (char[][]) board._3x3grid;
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
	public int compare(Board o1, Board o2) {
		char[][] b1 = (char[][]) o1._3x3grid;
		char[][] b2 = (char[][]) o2._3x3grid;
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


	// [0][0] [0][1] [0][2] -> [0][2] [1][2] [2][2]
	// [1][0] [1][1] [1][2] -> [0][1] [1][1] [2][1]
	// [2][0] [2][1] [2][2] -> [0][0] [1][0] [2][0]
	public static void rotate90CCWGrid(char[][] grid) {
		char tmp = grid[0][0];
		grid[0][0] = grid[0][2];
		grid[0][2] = grid[2][2];
		grid[2][2] = grid[2][0];
		grid[2][0] = tmp;

		tmp = grid[0][1];
		grid[0][1] = grid[1][2];
		grid[1][2] = grid[2][1];
		grid[2][1] = grid[1][0];
		grid[1][0] = tmp;
	}

	// [0][0] [0][1] [0][2] -> [0][2] [1][2] [2][2]
	// [1][0] [1][1] [1][2] -> [0][1] [1][1] [2][1]
	// [2][0] [2][1] [2][2] -> [0][0] [1][0] [2][0]
	public static void rotate90CCWRowColInts(int[] rows, int[] cols, int count) {
		int r, c;
		for (int i = 0; i < count; i++) {
			r = 2 - cols[i];
			c = rows[i];
			rows[i] = r;
			cols[i] = c;
		}
	}

	public static int unrotate90CCWMove(int move) {
		switch (move) {
		case 0:
			return 2;
		case 2:
			return 8;
		case 8:
			return 6;
		case 6:
			return 0;
		case 1:
			return 5;
		case 5:
			return 7;
		case 7:
			return 3;
		case 3:
			return 1;
		default:
			return move;
		}
	}

	public static void flipHGrid(char[][] grid) {
		char tmp = grid[0][0];
		grid[0][0] = grid[0][2];
		grid[0][2] = tmp;

		tmp = grid[1][0];
		grid[1][0] = grid[1][2];
		grid[1][2] = tmp;

		tmp = grid[2][0];
		grid[2][0] = grid[2][2];
		grid[2][2] = tmp;
	}

	public static void flipHColInts(int[] cols, int count) {
		for (int i = 0; i < count; i++) {
			if (cols[i] == 0) {
				cols[i] = 2;
			} else if (cols[i] == 2) {
				cols[i] = 0;
			}
		}
	}

	public static int unflipHMove(int move) {
		switch (move) {
		case 0:
			return 2;
		case 2:
			return 0;
		case 3:
			return 5;
		case 5:
			return 3;
		case 6:
			return 8;
		case 8:
			return 6;
		default:
			return move;
		}
	}
	
	
}

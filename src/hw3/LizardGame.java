package hw3;


import static api.Direction.*;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;

import api.*;

/**
 * Class that models a game.
 */
public class LizardGame {
	private ShowDialogListener dialogListener;
	private ScoreUpdateListener scoreListener;
	private int height;
	private int width;
	private Cell[][] game;

	/**
	 * Constructs a new LizardGame object with given grid dimensions.
	 * 
	 * @param width  number of columns
	 * @param height number of rows
	 */
	public LizardGame(int width, int height) {
		this.width = width;
		this.height = height;
		this.game = new Cell[width][height];
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				Cell cell = new Cell(i, j);
				game[i][j] = cell;
			}
		}
	}
	/**
	 * Get the grid's width.
	 * 
	 * @return width of the grid
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the grid's height.
	 * 
	 * @return height of the grid
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Adds a wall to the grid.
	 * <p>
	 * Specifically, this method calls placeWall on the Cell object associated with
	 * the wall (see the Wall class for how to get the cell associated with the
	 * wall). This class assumes a cell has already been set on the wall before
	 * being called.
	 * 
	 * @param wall to add
	 */
	public void addWall(Wall wall) {
		Cell cell = wall.getCell();
		cell.placeWall(wall);
		game[width-1][height-1] = cell;

	}

	/**
	 * Adds an exit to the grid.
	 * <p>
	 * Specifically, this method calls placeExit on the Cell object associated with
	 * the exit (see the Exit class for how to get the cell associated with the
	 * exit). This class assumes a cell has already been set on the exit before
	 * being called.
	 * 
	 * @param exit to add
	 */
	public void addExit(Exit exit) {
		exit.getCell().placeExit(exit);
		Cell cell = exit.getCell();
		cell.placeExit(exit);
		game[width-1][height-1] = cell;
	}

	/**
	 * Gets a list of all lizards on the grid. Does not include lizards that have
	 * exited.
	 * 
	 * @return lizards list of lizards
	 */
	public ArrayList<Lizard> getLizards() {
		ArrayList<Lizard> lizards= new ArrayList<>();
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				Cell cell = getCell(j, i);
				Lizard liz = cell.getLizard();
				if(liz != null && !lizards.contains(liz)){
					lizards.add(liz);
				}
			}
		}
		return lizards;
	}

	/**
	 * Adds the given lizard to the grid.
	 * <p>
	 * The scoreListener to should be updated with the number of lizards.
	 * 
	 * @param lizard to add
	 */
	public void addLizard(Lizard lizard) {
		ArrayList<BodySegment> segments = lizard.getSegments();
		for(BodySegment segment : segments){
			Cell cell = segment.getCell();
			cell.placeLizard(lizard);
			game[cell.getCol()][cell.getRow()] = cell;
		}
		ArrayList<Lizard> lizards = getLizards();
		int score = lizards.size();
		scoreListener.updateScore(score);
	}

	/**
	 * Removes the given lizard from the grid. Be aware that each cell object knows
	 * about a lizard that is placed on top of it. It is expected that this method
	 * updates all cells that the lizard used to be on, so that they now have no
	 * lizard placed on them.
	 * <p>
	 * The scoreListener to should be updated with the number of lizards using
	 * updateScore().
	 * 
	 * @param lizard to remove
	 */
	public void removeLizard(Lizard lizard) {
		ArrayList<BodySegment> segments = lizard.getSegments();
		for(BodySegment segment : segments){
			Cell cell = segment.getCell();
			cell.removeLizard();
			game[cell.getCol()][cell.getRow()] = cell;
		}
		ArrayList<Lizard> lizards = getLizards();
		int score = lizards.size();
		scoreListener.updateScore(score);
	}

	/**
	 * Gets the cell for the given column and row.
	 * <p>
	 * If the column or row are outside of the boundaries of the grid the method
	 * returns null.
	 * 
	 * @param col column of the cell
	 * @param row of the cell
	 * @return the cell or null
	 */
	public Cell getCell(int col, int row) {
		try {
			if (col > width || row > height || col < 0 || height < 0) {
				return null;
			} else {
				return game[col][row];
			}
		} catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}

	/**
	 * Gets the cell that is adjacent to (one over from) the given column and row,
	 * when moving in the given direction. For example (1, 4, UP) returns the cell
	 * at (1, 3).
	 * <p>
	 * If the adjacent cell is outside of the boundaries of the grid, the method
	 * returns null.
	 * 
	 * @param col the given column
	 * @param row the given row
	 * @param dir the direction from the given column and row to the adjacent cell
	 * @return the adjacent cell or null
	 */
	public Cell getAdjacentCell(int col, int row, Direction dir) {
		switch(dir) {
			case LEFT:
				try {
					return getCell(col - 1, row);
				} catch(Exception e){
					return null;
				}
			case RIGHT:
				try{
					return getCell(col + 1, row);
				} catch(Exception e){
					return null;
				}
			case UP:
				try{
					return getCell(col, row - 1);
				} catch(Exception e){
					return null;
				}
			case DOWN:
				try{
					return getCell(col, row + 1);
				} catch(Exception e){
					return null;
				}
			default:
				return null;
		}
	}

	/**
	 * Resets the grid. After calling this method the game should have a grid of
	 * size width x height containing all empty cells. Empty means cells with no
	 * walls, exits, etc.
	 * <p>
	 * All lizards should also be removed from the grid.
	 * 
	 * @param width  number of columns of the resized grid
	 * @param height number of rows of the resized grid
	 */
	public void resetGrid(int width, int height) {
		this.width = width;
		this.height = height;
		this.game = new Cell[width][height];
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				Cell cell = new Cell(i, j);
				game[i][j] = cell;
			}
		}
	}

	/**
	 * Returns true if a given cell location (col, row) is available for a lizard to
	 * move into. Specifically the cell cannot contain a wall or a lizard. Any other
	 * type of cell, including an exit is available.
	 * 
	 * @param row of the cell being tested
	 * @param col of the cell being tested
	 * @return true if the cell is available, false otherwise
	 */
	public boolean isAvailable(int col, int row) {
		Cell cell = getCell(col, row);
		if(cell.getWall() != null || cell.getLizard() != null){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * Move the lizard specified by its body segment at the given position (col,
	 * row) one cell in the given direction. The entire body of the lizard must move
	 * in a snake like fashion, in other words, each body segment pushes and pulls
	 * the segments it is connected to forward or backward in the path of the
	 * lizard's body. The given direction may result in the lizard moving its body
	 * either forward or backward by one cell.
	 * <p>
	 * The segments of a lizard's body are linked together and movement must always
	 * be "in-line" with the body. It is allowed to implement movement by either
	 * shifting every body segment one cell over or by creating a new head or tail
	 * segment and removing an existing head or tail segment to achieve the same
	 * effect of movement in the forward or backward direction.
	 * <p>
	 * If any segment of the lizard moves over an exit cell, the lizard should be
	 * removed from the grid.
	 * <p>
	 * If there are no lizards left on the grid the player has won the puzzle the
	 * the dialog listener should be used to display (see showDialog) the message
	 * "You win!".
	 * <p>
	 * It is possible that the given direction is not in-line with the body of the
	 * lizard (as described above), in that case this method should do nothing.
	 * <p>
	 * It is possible that the given column and row are outside the bounds of the
	 * grid, in that case this method should do nothing.
	 * <p>
	 * It is possible that there is no lizard at the given column and row, in that
	 * case this method should do nothing.
	 * <p>
	 * It is possible that the lizard is blocked and cannot move in the requested
	 * direction, in that case this method should do nothing.
	 * <p>
	 * <b>Developer's note: You may have noticed that there are a lot of details
	 * that need to be considered when implement this method method. It is highly
	 * recommend to explore how you can use the public API methods of this class,
	 * Grid and Lizard (hint: there are many helpful methods in those classes that
	 * will simplify your logic here) and also create your own private helper
	 * methods. Break the problem into smaller parts are work on each part
	 * individually.</b>
	 * 
	 * @param col the given column of a selected segment
	 * @param row the given row of a selected segment
	 * @param dir the given direction to move the selected segment
	 */
	public void move(int col, int row, Direction dir) {
		try {
			Lizard liz = getSpecificLizard(getCell(col, row));
			Cell cell = getAdjacentCell(col, row, dir);
			ArrayList<BodySegment> newSegments = new ArrayList<>();
			ArrayList<BodySegment> oldSegments = liz.getSegments();
			boolean isHead = getCell(col, row) == liz.getHeadSegment().getCell();
			boolean isTail = getCell(col, row) == liz.getTailSegment().getCell();
			int newcol = getAdjacentCell(col, row, dir).getCol();
			int newrow = getAdjacentCell(col, row, dir).getRow();
			if (((isHead && dir != liz.getDirectionToSegmentBehind(liz.getHeadSegment()) || isAvailable(newcol, newrow)) || (isTail && dir != liz.getDirectionToSegmentAhead(liz.getTailSegment()) || isAvailable(newcol, newrow))) && cell.getExit() == null) {
				if(isHead){
					oldSegments.getFirst().getCell().removeLizard();
					for(int i = 1; i < oldSegments.size(); i++){
						newSegments.add(oldSegments.get(i));
					}
					Cell cellH = getAdjacentCell(liz.getHeadSegment().getCell().getCol(), liz.getHeadSegment().getCell().getRow(), dir);
					newSegments.add(new BodySegment(liz, cellH));
					liz.setSegments(newSegments);
				} else if(isTail){
					oldSegments.getLast().getCell().removeLizard();
					for(int i = 0; i < oldSegments.size() - 1; i++){
						newSegments.add(oldSegments.get(i));
					}
					Cell cellT = getAdjacentCell(liz.getTailSegment().getCell().getCol(), liz.getTailSegment().getCell().getRow(), dir);
					newSegments.add(0, new BodySegment(liz, cellT));
					//Collections.reverse(newSegments);
					liz.setSegments(newSegments);
				}
			} else if(isTail && dir == liz.getDirectionToSegmentAhead(liz.getTailSegment()) && cell.getExit() == null){
				oldSegments.getFirst().getCell().removeLizard();
				oldSegments.remove(0);
				for(int i = 0; i < oldSegments.size(); i++){
					newSegments.add(oldSegments.get(i));
				}
				newSegments.add(new BodySegment(liz, getAdjacentCell(liz.getHeadSegment().getCell().getCol(), liz.getHeadSegment().getCell().getRow(), dir)));
				liz.setSegments(newSegments);
			} else if(isHead && dir == liz.getDirectionToSegmentBehind(liz.getHeadSegment()) && cell.getExit() == null){
				oldSegments.getLast().getCell().removeLizard();
				for(int i = oldSegments.size() - 1; i >= 0; i--){
					newSegments.add(oldSegments.get(i));
				}
				newSegments.add(new BodySegment(liz,getAdjacentCell(liz.getTailSegment().getCell().getCol(), liz.getTailSegment().getCell().getRow(), dir)));
				Collections.reverse(newSegments);
				liz.setSegments(newSegments);
			} else if(!isTail && !isHead && cell.getExit() == null){
				BodySegment current = liz.getSegmentAt(getCell(col, row));
				if(dir == liz.getDirectionToSegmentAhead(current)){
					oldSegments.getFirst().getCell().removeLizard();
					for(int i = 1; i < oldSegments.size(); i++){
						newSegments.add(oldSegments.get(i));
					}
					newSegments.add(new BodySegment(liz, getAdjacentCell(liz.getHeadSegment().getCell().getCol(), liz.getHeadSegment().getCell().getRow(), liz.getHeadDirection())));
					liz.setSegments(newSegments);
				} else if(dir == liz.getDirectionToSegmentBehind(current)){
					oldSegments.getLast().getCell().removeLizard();
					for(int i = 0; i < oldSegments.size() - 1; i++){
						newSegments.add(oldSegments.get(i));
					}
					newSegments.add(0, new BodySegment(liz, getAdjacentCell(liz.getTailSegment().getCell().getCol(), liz.getTailSegment().getCell().getRow(), liz.getTailDirection())));
					liz.setSegments(newSegments);
				}
			}

			else if(cell.getExit() != null){
				removeLizard(liz);
				if(getLizards().size() == 0){
					dialogListener.showDialog("Game Over!");
				}

			}
		} catch(NullPointerException e){
			System.out.println("Null Pointer");
		}

	}


	private Lizard getSpecificLizard(Cell cell) {
		return cell.getLizard();
	}


	/**
	 * Sets callback listeners for game events.
	 * 
	 * @param dialogListener listener for creating a user dialog
	 * @param scoreListener  listener for updating the player's score
	 */
	public void setListeners(ShowDialogListener dialogListener, ScoreUpdateListener scoreListener) {
		this.dialogListener = dialogListener;
		this.scoreListener = scoreListener;
	}

	/**
	 * Load the game from the given file path
	 * 
	 * @param filePath location of file to load
	 */
	public void load(String filePath) {
		GameFileUtil.load(filePath, this);
	}

	@Override
	public String toString() {
		String str = "---------- GRID ----------\n";
		str += "Dimensions:\n";
		str += getWidth() + " " + getHeight() + "\n";
		str += "Layout:\n";
		for (int y = 0; y < getHeight(); y++) {
			if (y > 0) {
				str += "\n";
			}
			for (int x = 0; x < getWidth(); x++) {
				str += getCell(x, y);
			}
		}
		str += "\nLizards:\n";
		for (Lizard l : getLizards()) {
			str += l;
		}
		str += "\n--------------------------\n";
		return str;
	}
}

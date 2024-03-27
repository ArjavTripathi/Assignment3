package hw3;

import static api.Direction.*;

import java.security.UnresolvedPermission;
import java.util.ArrayList;

import api.BodySegment;
import api.Cell;
import api.Direction;

/**
 * Represents a Lizard as a collection of body segments.
 */
public class Lizard {
	private ArrayList<BodySegment> segments;
	/**
	 * Constructs a Lizard object.
	 */
	public Lizard() {
		// TODO: method stub
	}

	/**
	 * Sets the segments of the lizard. Segments should be ordered from tail to
	 * head.
	 * 
	 * @param segments list of segments ordered from tail to head
	 */
	public void setSegments(ArrayList<BodySegment> segments) {
		this.segments = segments;
	}

	/**
	 * Gets the segments of the lizard. Segments are ordered from tail to head.
	 * 
	 * @return a list of segments ordered from tail to head
	 */
	public ArrayList<BodySegment> getSegments() {
		return segments;
	}

	/**
	 * Gets the head segment of the lizard. Returns null if the segments have not
	 * been initialized or there are no segments.
	 * 
	 * @return the head segment
	 */
	public BodySegment getHeadSegment() {
		try{
			if(segments.isEmpty()){
				return null;
			} else {
				BodySegment last = segments.get(segments.size() - 1);
				return last;
			}
		}
		catch(Exception e) {
			return null;
		}
	}

	/**
	 * Gets the tail segment of the lizard. Returns null if the segments have not
	 * been initialized or there are no segments.
	 * 
	 * @return the tail segment
	 */
	public BodySegment getTailSegment() {
		try{
			if(segments.isEmpty()){
				return null;
			} else {
				BodySegment first = segments.get(0);
				return first;
			}
		}
		catch(Exception e) {
			return null;
		}
	}

	/**
	 * Gets the segment that is located at a given cell or null if there is no
	 * segment at that cell.
	 * 
	 * @param cell to look for lizard
	 * @return the segment that is on the cell or null if there is none
	 */
	public BodySegment getSegmentAt(Cell cell) {
		//later
		for(BodySegment ele : segments){
			if(ele.getCell() == cell){
				return ele;
			}
		}
		return null;
	}

	/**
	 * Get the segment that is in front of (closer to the head segment than) the
	 * given segment. Returns null if there is no segment ahead.
	 * 
	 * @param segment the starting segment
	 * @return the segment in front of the given segment or null
	 */
	public BodySegment getSegmentAhead(BodySegment segment) {
		if(isSegmentFilled()){
			int index = 0;
			for(BodySegment ele : segments){
				if(ele == segment && index + 1 < segments.size()){
					return segments.get(index + 1);
				}
				index++;
			}

			return null;
		}else{
			return null;
		}

	}

	/**
	 * Get the segment that is behind (closer to the tail segment than) the given
	 * segment. Returns null if there is not segment behind.
	 * 
	 * @param segment the starting segment
	 * @return the segment behind of the given segment or null
	 */
	public BodySegment getSegmentBehind(BodySegment segment) {
		int index = 0;
		for(BodySegment ele : segments){
			if(ele == segment && index - 1 >= 0){
				return segments.get(index - 1);
			}
			index++;
		}

		return null;
	}

	/**
	 * Gets the direction from the perspective of the given segment point to the
	 * segment ahead (in front of) of it. Returns null if there is no segment ahead
	 * of the given segment.
	 * 
	 * @param segment the starting segment
	 * @return the direction to the segment ahead of the given segment or null
	 */
	public Direction getDirectionToSegmentAhead(BodySegment segment) {
		if(isSegmentFilled()){
			try {
				BodySegment nextSeg = getSegmentAhead(segment);
				int nextRow = nextSeg.getCell().getRow();
				int nextCol = nextSeg.getCell().getCol();
				int currRow = segment.getCell().getRow();
				int currCol = segment.getCell().getCol();

				if(nextRow == currRow){
					return nextCol > currCol ? RIGHT : LEFT;
				} else if (nextCol == currCol) {
					return nextRow > currRow ? DOWN : UP;
				} else {
					return null;
				}
			} catch(NullPointerException e){
				return null;
			}
		} else {
			return null;
		}

	}

	/**
	 * Gets the direction from the perspective of the given segment point to the
	 * segment behind it. Returns null if there is no segment behind of the given
	 * segment.
	 * 
	 * @param segment the starting segment
	 * @return the direction to the segment behind of the given segment or null
	 */
	public Direction getDirectionToSegmentBehind(BodySegment segment) {
		if(isSegmentFilled()){
			try{
				BodySegment behindSeg = getSegmentBehind(segment);
				int prevRow = behindSeg.getCell().getRow();
				int prevCol = behindSeg.getCell().getCol();
				int currRow = segment.getCell().getRow();
				int currCol = segment.getCell().getCol();
				if(prevCol == currCol) {
					return prevRow > currRow ? DOWN : UP;
				} else if(prevRow == currRow){
					return prevCol > currCol ? RIGHT : LEFT;
				} else {
					return null;
				}
			} catch(NullPointerException e){
				return null;
			}
		} else {
			return null;
		}

	}

	/**
	 * Gets the direction in which the head segment is pointing. This is the
	 * direction formed by going from the segment behind the head segment to the
	 * head segment. A lizard that does not have more than one segment has no
	 * defined head direction and returns null.
	 * 
	 * @return the direction in which the head segment is pointing or null
	 */
	public Direction getHeadDirection() {
		if(segments.size() > 1){
			Direction oppdir = getDirectionToSegmentBehind(getHeadSegment());
			switch (oppdir) {
				case DOWN:
					return UP;
				case UP:
					return DOWN;
				case RIGHT:
					return LEFT;
				case LEFT:
					return RIGHT;
				default:
					return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Gets the direction in which the tail segment is pointing. This is the
	 * direction formed by going from the segment ahead of the tail segment to the
	 * tail segment. A lizard that does not have more than one segment has no
	 * defined tail direction and returns null.
	 * 
	 * @return the direction in which the tail segment is pointing or null
	 */
	public Direction getTailDirection() {
		if(segments.size() > 1){
			Direction oppdir = getDirectionToSegmentAhead(getTailSegment());
			switch (oppdir) {
				case DOWN:
					return UP;
				case UP:
					return DOWN;
				case RIGHT:
					return LEFT;
				case LEFT:
					return RIGHT;
				default:
					return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		String result = "";
		for (BodySegment seg : getSegments()) {
			result += seg + " ";
		}
		return result;
	}

	private boolean isSegmentFilled(){
		return segments.size() > 1 ? true : false;
	}
}

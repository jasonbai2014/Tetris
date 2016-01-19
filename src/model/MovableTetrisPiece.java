/*
 * TCSS 305
 * 
 * An implementation of the classic game "Tetris".
 */

package model;

import java.util.Objects;

/**
 * Represents a TetrisPiece with a position and a rotation.
 * 
 * A MovableTetrisPiece is immutable.
 * 
 * @author Alan Fowler
 * @version Winter 2015
 */
public final class MovableTetrisPiece {
    
    /**
     * The number of Points in a TetrisPiece.
     */
    private static final int BLOCKS = 4;
    
    /**
     * The TetrisPiece.
     */
    private final TetrisPiece myTetrisPiece;
    
    /**
     * The board position of this TetrisPiece.
     */
    private final Point myPosition;

    /**
     * The rotation value of this TetrisPiece.
     */
    private final Rotation myRotation;

    // Standard constructor
    /**
     * Constructor of a movable TetrisPiece.
     * 
     * @param theTetrisPiece the type of TetrisPiece.
     * @param thePosition the position on the Board.
     */
    public MovableTetrisPiece(final TetrisPiece theTetrisPiece,
                              final Point thePosition) {
        
        this(theTetrisPiece, thePosition, Rotation.NONE);
    }

    // Non-standard constructor allows creation of pieces in any rotation
    /**
     * Constructor of a movable TetrisPiece with initial rotation.
     * 
     * @param theTetrisPiece the type of TetrisPiece.
     * @param thePosition the position on the Board.
     * @param theRotation the initial angle of the TetrisPiece.
     */
    public MovableTetrisPiece(final TetrisPiece theTetrisPiece,
                              final Point thePosition, 
                              final Rotation theRotation) {
        
        myTetrisPiece = theTetrisPiece;
        myPosition = thePosition;
        myRotation = theRotation;
    }

    /**
     * Get the TetrisPiece type of this movable TetrisPiece.
     * 
     * @return The TetrisPiece describing this piece.
     */
    public TetrisPiece getTetrisPiece() {
        return myTetrisPiece;
    }
    
    /**
     * The current board position of the TetrisPiece.
     * 
     * @return the board position.
     */
    public Point getPosition() {
        return myPosition;
    }

    /**
     * Get the current rotation value of the movable TetrisPiece.
     * 
     * @return current rotation value.
     */
    public Rotation getRotation() {
        return myRotation;
    }

    /**
     * Get the String representation of the TetrisPiece.
     * 
     * @return String representation of the TetrisPiece.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(myPosition.toString());
        sb.append('\n');
        final String[][] blocks = new String[BLOCKS][BLOCKS];
        for (int h = 0; h < BLOCKS; h++) {
            for (int w = 0; w < BLOCKS; w++) {
                blocks[w][h] = "   ";
            }
        }       
        for (final Point block : getLocalPoints()) {
            blocks[block.y()][block.x()] = "[ ]";
        }

        for (int h = BLOCKS - 1; h >= 0; h--) {
            for (int w = 0; w < BLOCKS; w++) {
                if (blocks[h][w] != null) {
                    sb.append(blocks[h][w]);
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
    
    /**
     * Rotates the TetrisPiece clockwise.
     * 
     * @return A new rotated movable TetrisPiece
     */
    public MovableTetrisPiece rotateCW() {
        return new MovableTetrisPiece(myTetrisPiece,
                                      myPosition,
                                      myRotation.clockwise());
    }

    /**
     * Rotates the TetrisPiece counter clockwise.
     * 
     * @return A new rotated movable TetrisPiece
     */
    public MovableTetrisPiece rotateCCW() {
        return new MovableTetrisPiece(myTetrisPiece,
                                      myPosition,
                                      myRotation.counterClockwise());
    }

    /**
     * Moves the TetrisPiece to the left on the game board.
     * 
     * @return A new left moved movable TetrisPiece
     */
    public MovableTetrisPiece left() {
        return new MovableTetrisPiece(myTetrisPiece, myPosition.transform(-1, 0), myRotation);
    }

    /**
     * Moves the TetrisPiece to the right on the game board.
     * 
     * @return A new right moved movable TetrisPiece
     */
    public MovableTetrisPiece right() {
        return new MovableTetrisPiece(myTetrisPiece, myPosition.transform(1, 0), myRotation);
    }

    /**
     * Moves the TetrisPiece down on the game board.
     * 
     * @return A new movable TetrisPiece moved down.
     */
    public MovableTetrisPiece down() {
        return new MovableTetrisPiece(myTetrisPiece, myPosition.transform(0, -1), myRotation);
    }
    
    /**
     * Returns a new MovableTetrisPiece of the current piece type and same Rotation
     * at the specified location.
     * 
     * @param thePosition the location for the returned MovableTetrisPiece
     * @return A new movable TetrisPiece at the specified location
     */
    protected MovableTetrisPiece setPosition(final Point thePosition) {
        return new MovableTetrisPiece(myTetrisPiece, thePosition, myRotation);
    }

    /**
     * Gets the local points of the TetrisPiece rotated.
     * 
     * @return array of TetrisPiece block points.
     */
    public Point[] getLocalPoints() {
        return getPoints(null);
    }

    /**
     * Gets the TetrisPiece points rotated and translated to board coordinates.
     * 
     * @return the board points for the TetrisPiece blocks.
     */
    public Point[] getBoardPoints() {
        return getPoints(myPosition);
    }
    
    // private methods

    /**
     * Get the block points of the TetrisPiece transformed by x and y.
     * 
     * @param thePoint the point to transform the points around.
     * @return array of TetrisPiece block points.
     */
    private Point[] getPoints(final Point thePoint) {

        final Point[] blocks = myTetrisPiece.getPoints();
        
        for (int i = 0; i < blocks.length; i++) {
            final Point block = blocks[i];
            if (myTetrisPiece != TetrisPiece.O) {
                switch (myRotation) {
                    case QUARTER:
                        blocks[i] = new Point(block.y(),
                                              myTetrisPiece.getWidth() - block.x() - 1);
                        
                        break;
                    case HALF:
                        blocks[i] = new Point(myTetrisPiece.getWidth() - block.x() - 1,
                                              myTetrisPiece.getWidth() - block.y() - 1);
                        
                        break;
                    case THREEQUARTER:                 
                        blocks[i] = new Point(myTetrisPiece.getWidth() - block.y() - 1,
                                              block.x());
                        
                        
                        break;
                    default:
                }
            }
            if (thePoint != null) {
                blocks[i] = blocks[i].transform(thePoint);
            }
        }

        return blocks;
    }

    /**
     * Gets the height of the TetrisPiece based on current rotation.
     * 
     * @return the height of the TetrisPiece.
     */
    public int getHeight() {
        int height = myTetrisPiece.getHeight();
        switch (myRotation) {
            case QUARTER:
            case THREEQUARTER:
                height = myTetrisPiece.getWidth();
                break;
            default:
        }
        return height;
    }

    /**
     * Gets the width of the TetrisPiece based on current rotation.
     * 
     * @return the width of the TetrisPiece.
     */
    public int getWidth() {
        int width = myTetrisPiece.getWidth();
        switch (myRotation) {
            case QUARTER:
            case THREEQUARTER:
                width = myTetrisPiece.getHeight();
                break;
            default:
        }
        return width;
    }

    /**
     * Override of the equals method to test if TetrisPiece, position, and rotation
     * are equal.
     * 
     * @param theOther Object to test for equality.
     * @return true if the object matches otherwise false.
     */
    @Override
    public boolean equals(final Object theOther) {
        boolean match = false;
        if (theOther == this) {
            match = true;
        } else if (theOther != null && theOther.getClass() == getClass()) {
            final MovableTetrisPiece p = (MovableTetrisPiece) theOther;
            match = Objects.equals(myTetrisPiece, p.myTetrisPiece)
                 && Objects.equals(myRotation, p.myRotation)        
                 && Objects.equals(myPosition, p.myPosition);
        }
        return match;
    }

    /**
     * Override of the hash code method.
     * 
     * @return Integer has of toString.
     */
    @Override
    public int hashCode() {
        return Objects.hash(myTetrisPiece, myPosition, myRotation);
    }
}

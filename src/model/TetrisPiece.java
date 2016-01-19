/*
 * TCSS 305
 * 
 * An implementation of the classic game "Tetris".
 */

package model;

import java.awt.Color;
import java.util.Random;

/**
 * Enumeration of the TetrisPiece types.
 * 
 * @author Alan Fowler
 * @version Winter 2015
 */
public enum TetrisPiece {

    // The enum constants
    /**
     * The 'I' TetrisPiece.
     */
    I(4, 1, Color.CYAN, new Point(0, 2), new Point(1, 2), new Point(2, 2), new Point(3, 2)),

    /**
     * The 'J' TetrisPiece.
     */
    J(3, 2, Color.BLUE, new Point(0, 2), new Point(0, 1), new Point(1, 1), new Point(2, 1)),

    /**
     * The 'L' TetrisPiece.
     */
    L(3, 2, //Color.ORANGE,
      new Color(233, 164, 0),
      new Point(2, 2), new Point(0, 1), new Point(1, 1), new Point(2, 1)),

    /**
     * The 'O' TetrisPiece.
     */
    O(3, 2, Color.YELLOW, new Point(1, 2), new Point(2, 2), new Point(1, 1), new Point(2, 1)),

    /**
     * The 'S' TetrisPiece.
     */
    S(3, 2, Color.GREEN, new Point(1, 2), new Point(2, 2), new Point(0, 1), new Point(1, 1)),

    /**
     * The 'T' TetrisPiece.
     */
    T(3, 2, //Color.MAGENTA.darker().darker(),
      new Color(185, 33, 255),
      new Point(1, 2), new Point(0, 1), new Point(1, 1), new Point(2, 1)),

    /**
     * The 'Z' TetrisPiece.
     */
    Z(3, 2, Color.RED, new Point(0, 2), new Point(1, 2), new Point(1, 1), new Point(2, 1));

    
    // Other class constants

    /**
     * A Random Object.
     */
    private static final Random RANDOM = new Random();

    
    // instance fields
    /**
     * The width of the TetrisPiece.
     */
    private final int myWidth;

    /**
     * The height of the TetrisPiece.
     */
    private final int myHeight;

    /**
     * The 4 Points of the TetrisPiece.
     */
    private final Point[] myPoints;

    /**
     * Color of the TetrisPiece.
     */
    private final Color myColor;

    /**
     * The TetrisPiece constructor.
     * 
     * @param theWidth width of the TetrisPiece.
     * @param theHeight height of the TetrisPiece.
     * @param theColor Color of the TetrisPiece.
     * @param thePoints the initial position of the blocks of the TetrisPiece.
     */
    private TetrisPiece(final int theWidth, final int theHeight, final Color theColor,
                        final Point... thePoints) {
        myWidth = theWidth;
        myHeight = theHeight;
        myColor = theColor;
        myPoints = thePoints.clone();
    }

    /**
     * Returns the Points of the TetrisPiece.
     * 
     * @return the Points of the TetrisPiece.
     */
    public Point[] getPoints() {
        return myPoints.clone();
    }

    /**
     * Return the width of the TetrisPiece.
     * 
     * @return the width of the TetrisPiece.
     */
    public int getWidth() {
        return myWidth;
    }

    /**
     * Return the height of the TetrisPiece.
     * 
     * @return the height of the TetrisPiece.
     */
    public int getHeight() {
        return myHeight;
    }

    /**
     * Return the color of the TetrisPiece.
     * 
     * @return The color of the TetrisPiece.
     */
    public Color getColor() {
        return myColor;
    }

    /**
     * Get a random TetrisPiece.
     * 
     * @return a random TetrisPiece.
     */
    public static TetrisPiece getRandomPiece() {
        return values()[RANDOM.nextInt(values().length)];
    }
}

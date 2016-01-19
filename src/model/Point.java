/*
 * TCSS 305
 * 
 * An implementation of the classic game "Tetris".
 */

package model;

import java.util.Objects;

/**
 * Represents a 2D Point with x and y coordinates.
 * Point objects are immutable.
 * (Compare to java.awt.Point which are mutable)
 * 
 * @author Alan Fowler
 * @version Winter 2015
 */
public final class Point {

    // Instance Fields
    /**
     * The X coordinate.
     */
    private final int myX;

    /**
     * The Y coordinate.
     */
    private final int myY;

    /**
     * Constructs a Point using the provided coordinates.
     * 
     * @param theX the X coordinate.
     * @param theY the Y coordinate.
     */
    public Point(final int theX, final int theY) {
        myX = theX;
        myY = theY;
    }

    // Queries
    /**
     * Returns the X coordinate.
     * 
     * @return the X coordinate of the Point.
     */
    public int x() {
        return myX;
    }

    /**
     * Returns the Y coordinate.
     * 
     * @return the Y coordinate of the Point.
     */
    public int y() {
        return myY;
    }

    /**
     * Creates a new point transformed by x and y.
     * 
     * @param theX the X factor to transform by.
     * @param theY the Y factor to transform by.
     * @return the new transformed Point.
     */
    public Point transform(final int theX, final int theY) {
        return new Point(myX + theX, myY + theY);
    }
    
    /**
     * Creates a new point transformed by another Point.
     * 
     * @param thePoint the Point to transform with.
     * @return the new transformed Point.
     */
    public Point transform(final Point thePoint) {
        return transform(thePoint.x(), thePoint.y());
    }

    // overridden methods of class Object
    /**
     * {@inheritDoc}
     * Compare the x and y coordinates of this Point
     * to those of theOther Point
     * to determine equality of the Point objects.
     * 
     * @param theOther object to compare to
     * @return true if the coordinate match; false otherwise
     */
    @Override
    public boolean equals(final Object theOther) {
        boolean match = false;
        if (theOther == this) {
            match = true;
        } else if (theOther != null && theOther.getClass() == getClass()) {
            final Point p = (Point) theOther;
            match = myX == p.myX && myY == p.myY;
        }
        return match;
    }

    /**
     * {@inheritDoc}
     * Return a hash code based on the current coordinates of this Point.
     * 
     * @return a hash code for this Point.
     */
    @Override
    public int hashCode() {
        return Objects.hash(myX, myY);
    }

    /**
     * {@inheritDoc}
     * Return the coordinate pair for this Point as a String.
     * 
     * @return the coordinate pair for this Point
     */
    @Override
    public String toString() {
        return String.format("(%d, %d)", myX, myY);
    }
}

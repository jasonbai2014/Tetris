/*
 * TCSS 305
 * 
 * An implementation of the classic game "Tetris".
 */

package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

/**
 * Represents a Tetris board.
 * 
 * @author Alan Fowler
 * @version Winter 2015
 */
public class Board extends Observable {

    // Class constants
    
    /**
     * Default width of a Tetris game board.
     */
    private static final int DEFAULT_WIDTH = 10;

    /**
     * Default height of a Tetris game board.
     */
    private static final int DEFAULT_HEIGHT = 20;

    
    // Instance fields
    
    /**
     * Width of the game board.
     */
    private final int myWidth;

    /**
     * Height of the game board.
     */
    private final int myHeight;
    
    /**
     * The frozen blocks on the board.
     */
    private final List<Color[]> myFrozenBlocks;
    
    /**
     * Current board status.
     * This is used to track game over status.
     */
    private final GameStatus myGameStatus;

    /**
     * Contains a non random sequence of TetrisPieces to loop through.
     */
    private List<TetrisPiece> myNonRandomPieces;

    /**
     * The current index in the non random piece sequence.
     */
    private int mySequenceIndex;
    
    /**
     * Piece that is next to play.
     */
    private TetrisPiece myNextPiece;
    
    /**
     * Piece that is currently movable.
     */
    private MovableTetrisPiece myCurrentPiece;
    
    // Constructors

    /**
     * Default Tetris board constructor.
     * Creates a standard size tetris game board.
     */
    public Board() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Tetris board constructor for non-default sized boards.
     * 
     * @param theWidth Width of the Tetris game board.
     * @param theHeight Height of the Tetris game board.
     */
    public Board(final int theWidth, final int theHeight) {
        super();
        myWidth = theWidth;
        myHeight = theHeight;
        myFrozenBlocks = new LinkedList<Color[]>();
        for (int h = 0; h < myHeight; h++) {
            myFrozenBlocks.add(new Color[myWidth]);
        }
        myGameStatus = new GameStatus();
         
        myNonRandomPieces = new ArrayList<TetrisPiece>();
        mySequenceIndex = 0;
        
        /*  myNextPiece and myCurrentPiece
         *  are initialized by the clear() method.
         */
    }
    

    // public queries
    
    /**
     * Get the width of the board.
     * 
     * @return Width of the board.
     */
    public int getWidth() {
        return myWidth;
    }

    /**
     * Get the height of the board.
     * 
     * @return Height of the board.
     */
    public int getHeight() {
        return myHeight;
    }
    
    
    // public commands

    /**
     * Resets the board for a new game.
     * This method must be called before the first game
     * and before each new game.
     */
    public void clear() {
        mySequenceIndex = 0;
        myFrozenBlocks.clear();
        for (int h = 0; h < myHeight; h++) {
            myFrozenBlocks.add(new Color[myWidth]);
        }
        myCurrentPiece = getNextMovablePiece(true);
        myGameStatus.setGameOver(false);
        
        setChanged();
        notifyObservers(new BoardData());
    }

    /**
     * Sets a non random sequence of pieces to loop through.
     * 
     * @param thePieces the List of non random TetrisPieces.
     */
    public void setPieceSequence(final List<TetrisPiece> thePieces) {
        myNonRandomPieces = new ArrayList<TetrisPiece>(thePieces);
        mySequenceIndex = 0;
        myCurrentPiece = getNextMovablePiece(true);
    }
    
    /**
     * Advances the board by one 'step'.
     * 
     * This could include
     * - moving the current piece down 1 line
     * - freezing the current piece if appropriate
     * - clearing full lines as needed
     */
    public void step() {
        /*
         * Calling the down() method from here should be sufficient
         * to advance the board by one 'step'.
         * However, more code could be added to this method
         * to implement additional functionality
         */
        down();
    }
    
    /**
     * Try to move the movable piece down.
     * Freeze the Piece in position if down tries to move into an illegal state.
     * Clear full lines.
     */
    public void down() {
        if (!move(myCurrentPiece.down())) {
            // the piece froze, so clear lines and update current piece
            addPieceToBoardData(myFrozenBlocks, myCurrentPiece);
            checkRows();
            myCurrentPiece = getNextMovablePiece(false);
            setChanged();
            notifyObservers(new BoardData());
        }
    }

    /**
     * Try to move the movable piece left.
     */
    public void left() {
        if (myCurrentPiece != null) {
            move(myCurrentPiece.left());
        }
    }

    /**
     * Try to move the movable piece right.
     */
    public void right() {
        if (myCurrentPiece != null) {
            move(myCurrentPiece.right());
        }
    }

    /**
     * Try to rotate the movable piece in the clockwise direction.
     */
    public void rotateCW() {
        if (myCurrentPiece != null) {
            if (myCurrentPiece.getTetrisPiece() == TetrisPiece.O) {
                move(myCurrentPiece.rotateCW());
            } else {
                final MovableTetrisPiece cwPiece = myCurrentPiece.rotateCW();
                final Point[] offsets = WallKick.getWallKicks(cwPiece.getTetrisPiece(),
                                                    myCurrentPiece.getRotation(),
                                                    cwPiece.getRotation());
                for (final Point p : offsets) {
                    final Point offsetLocation = cwPiece.getPosition().transform(p);
                    final MovableTetrisPiece temp = cwPiece.setPosition(offsetLocation);
                    if (move(temp)) {
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Try to rotate the movable piece in the counter-clockwise direction.
     */
    public void rotateCCW() {
        if (myCurrentPiece != null) {
            if (myCurrentPiece.getTetrisPiece() == TetrisPiece.O) {
                move(myCurrentPiece.rotateCCW());
            } else {
                final MovableTetrisPiece ccwPiece = myCurrentPiece.rotateCCW();
                final Point[] offsets = WallKick.getWallKicks(ccwPiece.getTetrisPiece(),
                                                    myCurrentPiece.getRotation(),
                                                    ccwPiece.getRotation());
                for (final Point p : offsets) {
                    final Point offsetLocation = ccwPiece.getPosition().transform(p);
                    final MovableTetrisPiece temp = ccwPiece.setPosition(offsetLocation);
                    if (move(temp)) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Drop the piece until piece is set.
     */
    public void drop() {
        if (!myGameStatus.isGameOver()) {
            while (isPieceLegal(myCurrentPiece.down())) {
                down();  // move down as far as possible
            }
            down();  // move down one more time to freeze in place
        }
    }
    
    
    
    // Overridden methods of class Object

    /**
     * String representation of the Tetris Board.
     * 
     * @return String representation of the Tetris Board.
     */
    @Override
    public String toString() {
        final List<Color[]> board = getBoard();
        board.add(new Color[myWidth]);
        board.add(new Color[myWidth]);
        board.add(new Color[myWidth]);
        board.add(new Color[myWidth]);
        if (myCurrentPiece != null) {
            addPieceToBoardData(board, myCurrentPiece);
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = board.size() - 1; i >= 0; i--) {
            final Color[] row = board.get(i);
            sb.append('|');
            for (final Color c : row) {
                if (c == null) {
                    sb.append(' ');
                } else {
                    sb.append('*');
                }
            }
            sb.append("|\n");
            if (i == this.myHeight) {
                sb.append(' ');
                for (int j = 0; j < this.myWidth; j++) {
                    sb.append('-');
                }
                sb.append('\n');
            }
        }
        sb.append('|');
        for (int w = 0; w < myWidth; w++) {
            sb.append('-');
        }
        sb.append('|');
        return sb.toString();
    }
    
    
    
    
    // private helper methods
    
    /**
     * Helper function to check if the current piece can be shifted to the
     * specified position.
     * 
     * @param theMovedPiece the position to attempt to shift the current piece
     * @return True if the move succeeded
     */
    private boolean move(final MovableTetrisPiece theMovedPiece) {
        boolean result = false;
        if (isPieceLegal(theMovedPiece)) {
            myCurrentPiece = theMovedPiece;
            result = true;
            setChanged();
            notifyObservers(new BoardData());
        }
        return result;
    }

    /**
     * Helper function to test if the piece is in a legal state.
     * 
     * Illegal states:
     * - points of the piece exceed the bounds of the board
     * - points of the piece collide with frozen blocks on the board
     * 
     * @param thePiece MovableTetrisPiece to test.
     * @return Returns true if the piece is in a legal state; false otherwise
     */
    private boolean isPieceLegal(final MovableTetrisPiece thePiece) {
        boolean result = true;
        
        for (final Point p : thePiece.getBoardPoints()) {
            if (p.x() < 0 || p.x() >= myWidth) {
                result = false;
            }
            if (p.y() < 0) {
                result = false;
            }
        }
        return result && !collision(thePiece);      
    }

    /**
     * Adds a movable Tetris piece into a list of board color data.
     * 
     * Allows a single data structure to represent the current piece
     * and the frozen blocks.
     * 
     * @param theFrozenBlocks Board to set the piece on.
     * @param thePiece Piece to set on the board.
     */
    private void addPieceToBoardData(final List<Color[]> theFrozenBlocks,
                                     final MovableTetrisPiece thePiece) {
        for (final Point p : thePiece.getBoardPoints()) {
            setPoint(theFrozenBlocks, p, thePiece.getTetrisPiece().getColor());
        }
    }

    /**
     * Checks the board for complete rows.
     */
    private void checkRows() {
        final List<Integer> completeRows = new ArrayList<Integer>();
        for (final Color[] row : myFrozenBlocks) {
            boolean complete = true;
            for (final Color c : row) {
                if (c == null) {
                    complete = false;
                    break;
                }
            }
            if (complete) {
                completeRows.add(myFrozenBlocks.indexOf(row));
                setChanged();
            }
        }
        // loop through list backwards removing items by index
        if (!completeRows.isEmpty()) {
            for (int i = completeRows.size() - 1; i >= 0; i--) {
                final Color[] row = myFrozenBlocks.get(completeRows.get(i));
                myFrozenBlocks.remove(row);
                myFrozenBlocks.add(new Color[myWidth]);
            }
        }
        notifyObservers(new CompletedLines(completeRows));
    }
    
    /**
     * Helper function to copy the board.
     * 
     * @return A new copy of the board.
     */
    private List<Color[]> getBoard() {
        final List<Color[]> board = new ArrayList<Color[]>();
        for (final Color[] row : myFrozenBlocks) {
            board.add(row.clone());
        }
        return board;
    }

    /**
     * Determines if a point is on the game board.
     * 
     * @param theBoard Board to test.
     * @param thePoint Point to test.
     * @return True if the point is on the board otherwise false.
     */
    private boolean isPointOnBoard(final List<Color[]> theBoard, final Point thePoint) {
        return thePoint.x() >= 0 && thePoint.x() < myWidth && thePoint.y() >= 0
               && thePoint.y() < theBoard.size();
    }

    /**
     * Sets a block as color at a board point.
     * 
     * @param theBoard Board to set the point on.
     * @param thePoint Board point.
     * @param theColor Color to set at board point.
     */
    private void setPoint(final List<Color[]> theBoard,
                          final Point thePoint,
                          final Color theColor) {
        
        if (isPointOnBoard(theBoard, thePoint)) {
            final Color[] row = theBoard.get(thePoint.y());
            row[thePoint.x()] = theColor;
        } else if (!myGameStatus.isGameOver()) {
            myGameStatus.setGameOver(true);
        }
    }

    /**
     * Returns the block color at a specific board point.
     * 
     * @param thePoint the specific Point to check
     * @return Color of the block at point or null if no block exists.
     */
    private Color getPoint(final Point thePoint) {
        Color c = null;
        if (isPointOnBoard(myFrozenBlocks, thePoint)) {
            c = myFrozenBlocks.get(thePoint.y())[thePoint.x()];
        }
        return c;
    }

    /**
     * Helper function to determine of a movable block has collided with set
     * blocks.
     * 
     * @param theTest movable TetrisPiece to test for collision.
     * @return Returns true if any of the blocks has collided with a set board
     *         block.
     */
    private boolean collision(final MovableTetrisPiece theTest) {
        boolean res = false;
        for (final Point p : theTest.getBoardPoints()) {
            if (getPoint(p) != null) {
                res = true;
            }
        }
        return res;
    }

    /**
     * Gets the next MovableTetrisPiece.
     * 
     * @param theRestart Restart the non random cycle.
     * @return A new MovableTetrisPiece.
     */
    private MovableTetrisPiece getNextMovablePiece(final boolean theRestart) {
        MovableTetrisPiece piece;
        if (myNextPiece == null || theRestart) {
            prepareNextMovablePiece();
        }
        int startY = myHeight - 1;
        if (myNextPiece == TetrisPiece.I) {
            startY--; 
        }
        piece = new MovableTetrisPiece(myNextPiece,
                    new Point((myWidth - myNextPiece.getWidth()) / 2, startY));
        
        prepareNextMovablePiece();
        return piece;
    }
    
    /**
     * Prepares the Next movable piece.
     */
    private void prepareNextMovablePiece() {
        final boolean share = myNextPiece != null;
        if (myNonRandomPieces == null || myNonRandomPieces.isEmpty()) {
            myNextPiece = TetrisPiece.getRandomPiece();
        } else {
            mySequenceIndex %= myNonRandomPieces.size();
            myNextPiece = myNonRandomPieces.get(mySequenceIndex++);
        }
        if (share && !myGameStatus.isGameOver()) {
            setChanged();
            notifyObservers(myNextPiece);
        }
    }    

    
    
    
    
    // Inner classes

    /**
     * A class to describe the board data to registered Observers.
     * The board data includes the current piece and the frozen blocks.
     */
    public final class BoardData {
        
        /**
         * The board data to pass to observers.
         */
        private final List<Color[]> myBoardData;

        /**
         * Constructor of the Board Data object.
         */
        protected BoardData() {
            myBoardData = getBoard();
            myBoardData.add(new Color[myWidth]);
            myBoardData.add(new Color[myWidth]);
            myBoardData.add(new Color[myWidth]);
            myBoardData.add(new Color[myWidth]);
            if (myCurrentPiece != null) {
                addPieceToBoardData(myBoardData, myCurrentPiece);
            }
        }

        /**
         * Copy and return the board's data.
         * 
         * @return Copy of the Board Data.
         */
        public List<Color[]> getBoardData() {
            final List<Color[]> board = new ArrayList<Color[]>();
            for (final Color[] row : myBoardData) {
                board.add(row.clone());
            }
            return board;
        }
        
    } // end inner class BoardData

    /**
     * A class to describe the lines that have been cleared to registered Observers.
     */
    public final class CompletedLines {

        /**
         * A list containing the indexes of lines which were cleared.
         */
        private final List<Integer> myClearedLines;

        /**
         * Completed Lines constructor.
         * 
         * @param theClearedLines a list containing the indexes of lines which were cleared
         */
        protected CompletedLines(final List<Integer> theClearedLines) {
            myClearedLines = Collections.unmodifiableList(theClearedLines);
        }

        /**
         * Return a list containing the indexes of lines which were cleared.
         * 
         * @return a List of completed lines.
         */
        public List<Integer> getCompletedLines() {
            return myClearedLines;
        }
        
    } // end inner class CompletedLines
    

    /**
     * Class that tracks the 'game over' status.
     */
    public final class GameStatus {

        /**
         * The game over state.
         */
        private boolean myGameOver;

        /**
         * Board status constructor.
         */
        protected GameStatus() {
            myGameOver = false;
        }

        /**
         * Set the game over status of the board.
         * 
         * @param theGameOverFlag the game over status to set
         */
        private void setGameOver(final boolean theGameOverFlag) {
            myGameOver = theGameOverFlag;
            setChanged();
            notifyObservers(this);
        }

        /**
         * Query to check if game is over.
         * 
         * @return True if game is over, False otherwise
         */
        public boolean isGameOver() {
            return myGameOver;
        }
        
    } // end inner class GameStatus
    
}

/*
 * TCSS 305
 * Assignment 6 ¨C Tetris
 */

package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.Point;
import model.TetrisPiece;

/**
 * This is a class used to create a panel that shows next tetris piece.
 * 
 * @author Qing Bai
 * @version 5 March 2015
 */
@SuppressWarnings("serial")
public class TetrisNextPieceDisplayBoard extends JPanel implements Observer {
    
    /**
     * This is number of rows and columns this panel has.
     */
    public static final int BLOCK_NUM = 4;
    
    /**
     * This is length of one tetris block.
     */
    public static final int TETRIS_LENGTH = 20;
    
    /**
     * This is next tetris piece shown on the display board.
     */
    private TetrisPiece myNextPiece;
    
    /**
     * This is a constructor of this class.
     */
    public TetrisNextPieceDisplayBoard() {
        super();
        setPreferredSize(new Dimension(BLOCK_NUM * TETRIS_LENGTH, BLOCK_NUM * TETRIS_LENGTH));
        setBackground(Color.WHITE);
    }
    
    /**
     * This paints the display board.
     * 
     * @param theGraphics draws on this display board
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D pen = (Graphics2D) theGraphics;

        if (myNextPiece != null) {
            pen.setPaint(myNextPiece.getColor());
            final Point[] tetrisPoints = myNextPiece.getPoints();
            // draws every single block of this tetris piece
            for (final Point point : tetrisPoints) {
                final int x = point.x();
                final int y = point.y();
                pen.fill3DRect(x * TETRIS_LENGTH, (BLOCK_NUM - y - 1) * TETRIS_LENGTH, 
                               TETRIS_LENGTH - 1, TETRIS_LENGTH - 1, true);
            }
        }
        // draws border of this display panel
        pen.setPaint(Color.BLACK);
        pen.draw(new Rectangle2D.Double(0, 0, BLOCK_NUM * TETRIS_LENGTH - 1,
                                        BLOCK_NUM * TETRIS_LENGTH - 1));
    }

    /**
     * This repaints the board when board data is updated.
     * 
     * @param theObservable is an observable object
     * @param theData is the data for changes being made
     */
    @Override
    public void update(final Observable theObservable, final Object theData) {
        if (theData instanceof TetrisPiece) {
            myNextPiece = (TetrisPiece) theData;
            repaint();
        } 
    }
}

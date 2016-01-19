/*
 * TCSS 305
 * Assignment 6 ¨C Tetris
 */

package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.JPanel;

import model.Board.BoardData;
import model.Board.GameStatus;

/**
 * This is a class used to represent a display board for this game.
 * 
 * @author Qing Bai
 * @version 12 March 2015
 */
@SuppressWarnings("serial")
public class TetrisDisplayBoard extends JPanel implements Observer {

    /**
     * This shows how many tetris blocks are in one row.
     */
    public static final int BOARD_COLUMN = 10;
    
    /**
     * This shows how many tetris blocks are in one column.
     */
    public static final int BOARD_ROW = 20;
    
    /**
     * This shows length of one tetris block.
     */
    public static final int TETRIS_LENGTH = 20;
    
    /**
     * This is number of extra rows hidden on top of this display board.
     */
    public static final int EXTRA_ROWS = 4;
    
    /**
     * This is the font size used for this display board.
     */
    public static final int FONT_SIZE = 24;
    
    /**
     * This is the ending message shown when game is over.
     */
    public static final String END_MESSAGE = "GAME OVER!";
    
    /**
     * This is the random generator used for this display board.
     */
    public static final Random RANDOM = new Random();
    
    /**
     * The frozen tetris blocks on the board.
     */
    private List<Color[]> myTetrisBlocks;
    
    /**
     * This indicates whether or not a game is over.
     */
    private boolean myGameOver;
    
    /**
     * This indicates whether or not this game is in mirror image mode.
     */
    private boolean myMirrorMode;
    
    /**
     * This is a constructor of this class.
     */
    public TetrisDisplayBoard() {
        super(true);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(BOARD_COLUMN * TETRIS_LENGTH, 
                                       BOARD_ROW * TETRIS_LENGTH));
        myTetrisBlocks = new ArrayList<Color[]>();
        myGameOver = false;
        myMirrorMode = false;
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
        // draws border of this display board
        pen.draw(new Rectangle2D.Double(0, 0, BOARD_COLUMN * TETRIS_LENGTH - 1, 
                                        BOARD_ROW * TETRIS_LENGTH - 1));
        // extra rows on top of this display board not shown
        final int rows = myTetrisBlocks.size() - EXTRA_ROWS;
        
        for (int i = 0; i < rows; i++) {
            // gets information about tetris blocks in one row
            final Color[] blocks = myTetrisBlocks.get(i);
            // draws tetris blocks in one row
            for (int j = 0; j < blocks.length; j++) {
                final Color color = blocks[j];
                
                if (color == null && myMirrorMode) {
                    pen.setPaint(createRandomColor());
                    drawTetrisBlock(pen, i, j);
                } else if (color != null && !myMirrorMode) {
                    pen.setPaint(color);
                    drawTetrisBlock(pen, i, j);
                } 
            }
        }  
        
        if (myGameOver) {
            pen.setPaint(Color.BLACK);
            displayEndingMessage(pen);
        }
    }
    
    /**
     * This draws block of a tetris piece.
     * 
     * @param theGraphics draws on this display board
     * @param theRow is the row where this tetris block is located
     * @param theCol is the column where this tetris block is located
     */
    private void drawTetrisBlock(final Graphics2D theGraphics, final int theRow, 
                                 final int theCol) {
        theGraphics.fill3DRect(theCol * TETRIS_LENGTH, (BOARD_ROW - theRow - 1) * TETRIS_LENGTH
                               , TETRIS_LENGTH - 1, TETRIS_LENGTH - 1, true);
    }
    
    /**
     * This manages the game mode showing on this display board.
     * 
     * @param theMode shows whether or not this game is in mirror image mode
     */
    public void setMirrorMode(final boolean theMode) {
        myMirrorMode = theMode;
        repaint();
    }
    
    /**
     * This creates a random color.
     * 
     * @return a random color
     */
    private Color createRandomColor() {
        return new Color(RANDOM.nextFloat(), RANDOM.nextFloat(), RANDOM.nextFloat());
    }
    
    /**
     * This shows an ending message on the display board when game is over.
     * 
     * @param theGraphics draws on this display board
     */
    private void displayEndingMessage(final Graphics2D theGraphics) {
        final Font font = new Font(Font.SERIF, Font.BOLD, FONT_SIZE);
        theGraphics.setFont(font);
        
        final FontRenderContext renderContext = theGraphics.getFontRenderContext();
        final GlyphVector glyphVector = font.createGlyphVector(renderContext, END_MESSAGE);
        final Rectangle2D bounds = glyphVector.getVisualBounds().getBounds();
        // shows the ending message in the center of the board
        final int x = (int) ((getWidth() - bounds.getWidth()) / 2);
        final int y = (int) ((getHeight() - bounds.getHeight()) / 2);
        theGraphics.drawString(END_MESSAGE, x, y);
    }
    
    /**
     * This repaints the board when board data is updated or game is over.
     * 
     * @param theObservable is an observable object
     * @param theData is the data for changes being made
     */
    @Override
    public void update(final Observable theObservable, final Object theData) {
        if (theData instanceof BoardData) {
            myTetrisBlocks = ((BoardData) theData).getBoardData();
            repaint();
        } else if (theData instanceof GameStatus) {
            myGameOver = ((GameStatus) theData).isGameOver();
            if (myGameOver) {
                repaint();
            }
        }
    }  
    
}

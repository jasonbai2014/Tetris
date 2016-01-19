/*
 * TCSS 305
 * Assignment 6 ¨C Tetris
 */

package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import model.Board;
import model.Board.GameStatus;
import sound.SoundPlayer;

/**
 * This is a listener class used to control tetris pieces' movements on the board.
 * 
 * @author Qing Bai
 * @version 12 March 2015
 */
public class ControlKeyListener extends KeyAdapter implements PropertyChangeListener, 
    Observer {
    
    /**
     * This is left control key index in the control key texts array.
     */
    public static final int LEFT_INDEX = 0;
    
    /**
     * This is right control key index in the control key texts array.
     */
    public static final int RIGHT_INDEX = 1;
    
    /**
     * This is down control key index in the control key texts array.
     */
    public static final int DOWN_INDEX = 2;
    
    /**
     * This is drop control key index in the control key texts array.
     */
    public static final int DROP_INDEX = 3;
    
    /**
     * This is rotate clockwise control key index in the control key texts array.
     */
    public static final int ROTATE_CW_INDEX = 4;
    
    /**
     * This is rotate counter-clockwise control key index in the control key texts array.
     */
    public static final int ROTATE_CCW_INDEX = 5;
    
    /**
     * This is background music of this game.
     */
    public static final String BACKGROUND_MUSIC = "music/background_sound.wav";
    
    /**
     * This is a sound clip for game over.
     */
    public static final String GAME_OVER_SOUND = "music/gameover_sound.wav";
    
    /**
     * This is a sound clip for movements such as turning left, right, moving down one line,
     * rotating clockwise and counter-clockwise.
     */
    public static final String MOVE_SOUND = "music/move_sound.wav";
    
    /**
     * This is a sound clip for instantly dropping a tetris piece.
     */
    public static final String DROP_SOUND = "music/drop_sound.wav";
    
    /**
     * This shows whether or not a game is in progress.
     */
    private boolean myGameInProgress;
    
    /**
     * This is the board that manipulates tetris blocks.
     */
    private final Board myBoard;
    
    /**
     * This is the timer used in this game.
     */
    private final Timer myTimer;
    
    /**
     * This is the sound player in this game.
     */
    private final SoundPlayer myPlayer;

    /**
     * This is control key texts in an order of left, right, down, drop, rotateCW, 
     * and rotateCCW.
     */
    private String[] myControlKeyTexts;
    
    /**
     * This is the constructor of this listener.
     * 
     * @param theBoard is the board that manipulates tetris blocks in this game
     * @param theControlKeyTexts is an array of control key texts
     * @param theTimer is the timer used in this game
     * @param thePlayer is the sound player used in this game
     */
    public ControlKeyListener(final Board theBoard, final String[] theControlKeyTexts,
                              final Timer theTimer, final SoundPlayer thePlayer) {
        super();
        myBoard = theBoard;
        myTimer = theTimer;
        myPlayer = thePlayer;
        myControlKeyTexts = theControlKeyTexts.clone();
        myGameInProgress = false;
    }
    
    /**
     * This allows a player to move a tetris piece to left or right, or rotate the piece
     * clockwise or counter-clockwise, or move tetris piece down one line or drop the piece.
     * 
     * @param theEvent is the event
     */
    @Override
    public void keyPressed(final KeyEvent theEvent) {
        String input = null;

        if (myTimer.isRunning()) {
            input = KeyEvent.getKeyText(theEvent.getKeyCode());
        }
        
        // only functions when a game is running
        if (myControlKeyTexts[LEFT_INDEX].equals(input)) {
            myBoard.left();
            myPlayer.play(MOVE_SOUND);
        } else if (myControlKeyTexts[RIGHT_INDEX].equals(input)) {
            myBoard.right();
            myPlayer.play(MOVE_SOUND);
        } else if (myControlKeyTexts[DOWN_INDEX].equals(input)) {
            myBoard.down();
            myPlayer.play(MOVE_SOUND);
        } else if (myControlKeyTexts[DROP_INDEX].equals(input)) {
            myBoard.drop();
            myPlayer.play(DROP_SOUND);
        } else if (myControlKeyTexts[ROTATE_CW_INDEX].equals(input)) {
            myBoard.rotateCW();
            myPlayer.play(MOVE_SOUND);
        } else if (myControlKeyTexts[ROTATE_CCW_INDEX].equals(input)) {
            myBoard.rotateCCW();
            myPlayer.play(MOVE_SOUND);
        }
    }
    
    /**
     * This starts, resumes, or pauses game only when the key is released.
     * 
     * @param theEvent is the event
     */
    @Override
    public void keyReleased(final KeyEvent theEvent) {
        final int keycode = theEvent.getKeyCode();
        
        if (KeyEvent.VK_ENTER == keycode && !myGameInProgress) {
            // starts a new game when there is no game in progress
            myBoard.clear();
            myTimer.start();
            myPlayer.loop(BACKGROUND_MUSIC);
            myGameInProgress = true;
        } else if (KeyEvent.VK_ENTER == keycode && myGameInProgress) {
            // ends current game so that player can start a new game
            myTimer.stop();
            myTimer.setDelay(myTimer.getInitialDelay());
            myPlayer.stop(BACKGROUND_MUSIC);
            myGameInProgress = false;
            JOptionPane.showMessageDialog(null, "The game ends. Press Enter to start new game",
                                          null, JOptionPane.INFORMATION_MESSAGE);
        } else if (KeyEvent.VK_SPACE == keycode && myTimer.isRunning()) {
            // pause the game when game is running
            myTimer.stop();
            myPlayer.pause(BACKGROUND_MUSIC);
        } else if (KeyEvent.VK_SPACE == keycode && !myTimer.isRunning() && myGameInProgress) {
            // resume a paused game and it won't work before a game starts
            myTimer.start();
            myPlayer.loop(BACKGROUND_MUSIC);
        }
    }
    
    /**
     * This receives new control keys information and replaces old keys with the new keys.
     * 
     * @param theEvent is the event containing the info for changes being made on control keys
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if ("controlKeys".equals(theEvent.getPropertyName())) {
            myControlKeyTexts = (String[]) theEvent.getNewValue();
        }      
    }
    
    /**
     * This stops and ends game when game is over.
     * 
     * @param theObservable is an observable object
     * @param theData is the data for changes being made
     */
    @Override
    public void update(final Observable theObservable, final Object theData) {
        // this avoids an issue that you have to end the game before starting a new game
        // even though game is actually over
        if (theData instanceof GameStatus && ((GameStatus) theData).isGameOver()) {
            myGameInProgress = false;
            myTimer.stop();
            myTimer.setDelay(myTimer.getInitialDelay());
            myPlayer.stop(BACKGROUND_MUSIC);
            myPlayer.play(GAME_OVER_SOUND);
        }
    }
}

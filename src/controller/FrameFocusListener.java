/*
 * TCSS 305
 * Assignment 6 ¨C Tetris
 */

package controller;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Timer;

import sound.SoundPlayer;


/**
 * This is a listener class for the JFrame. 
 * 
 * @author Qing Bai
 * @version 12 March 2015
 */
public class FrameFocusListener implements FocusListener {
    
    /**
     * This is the background music used in this game.
     */
    public static final String BACKGROUND_MUSIC = "music/background_sound.wav";
    
    /**
     * This is the timer used in this game.
     */
    private final Timer myTimer;
    
    /**
     * This is the sound player in this game.
     */
    private final SoundPlayer myPlayer;
    
    /**
     * This indicates whether or not the timer can be unpaused when focus is gained.
     */
    private boolean myUnpause;
    
    /**
     * This is a constructor of this listener class.
     * 
     * @param theTimer is the timer used in this game
     * @param thePlayer is the sound player in this game
     */
    public FrameFocusListener(final Timer theTimer, final SoundPlayer thePlayer) {
        myTimer = theTimer;
        myPlayer = thePlayer;
        myUnpause = false;
    }
    
    /**
     * This resumes the game and background music only when the game is paused due to 
     * loss of focus.
     * 
     * @param theEvent is the event
     */
    @Override
    public void focusGained(final FocusEvent theEvent) {
        if (myUnpause) {
            myTimer.start();
            myUnpause = false;
            myPlayer.loop(BACKGROUND_MUSIC);
        }
    }
    
    /**
     * This pauses the game and background music only when timer is running and focus is lost.
     * 
     * @param theEvent is the event
     */
    @Override
    public void focusLost(final FocusEvent theEvent) {
        if (myTimer.isRunning()) {
            myTimer.stop();
            myPlayer.pause(BACKGROUND_MUSIC);
            myUnpause = true;
        } 
    }
}

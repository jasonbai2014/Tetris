/*
 * TCSS 305
 * Assignment 6 ¨C Tetris
 */

package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Board;

/**
 * This is a listener class for the timer.
 * 
 * @author Qing Bai
 * @version 5 March 2015
 */
public class TimerListener implements ActionListener {

    /**
     * This is the board that manipulates tetris blocks.
     */
    private final Board myBoard;
    
    /**
     * This is the constructor of this listener.
     * 
     * @param theBoard is the board that manipulates tetris blocks in this game
     */
    public TimerListener(final Board theBoard) {
        myBoard = theBoard;
    }
    
    /**
     * This advances the board by one step.
     * 
     * @param theEvent is the event
     */
    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        myBoard.step();
    }

}

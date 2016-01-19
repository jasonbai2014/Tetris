/*
 * TCSS 305
 * Assignment 6 ¨C Tetris
 */

package view;

import java.awt.EventQueue;

/**
 * This is an utility class used to start this program.
 * 
 * @author Qing Bai
 * @version 5 March 2015
 */
public final class TetrisMain {

    /**
     * Private constructor to prevent construction of instances.
     */
    private TetrisMain() {
        // do nothing 
    }
    
    /**
     * The start point of this program.
     * 
     * @param theArgs command line arguments, ignored in this program
     */
    public static void main(final String[] theArgs) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // this is initial control key texts in an order of left, right, down, drop, 
                // rotateCW, and rotateCCW.
                final String[] initialControlKeys = {"A", "D", "S", "X", "Q", "E"};
                new TetrisGUI(initialControlKeys).start();
            }   
        });
    }
}


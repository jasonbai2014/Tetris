/*
 * TCSS 305
 * Assignment 6 ¨C Tetris
 */

package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * This is a listener class used to set new control key.
 * 
 * @author Qing Bai
 * @version 5 March 2015
 */
public class SettingKeyListener extends KeyAdapter {

    /**
     * This is the text field showing key text.
     */
    private final JTextField myTextField;
    
    /**
     * This is a constructor of this listener.
     * 
     * @param theTextField is text field used to show key text
     */
    public SettingKeyListener(final JTextField theTextField) {
        super();
        myTextField = theTextField;
    }
    
    /**
     * This makes sure that text won't show up until the key is released.
     * 
     * @param theEvent is a key event
     */
    @Override
    public void keyPressed(final KeyEvent theEvent) {
        myTextField.setText("");
    }
    
    /**
     * This shows key text in this text field when the key is released.
     * 
     * @param theEvent is a key event
     */
    @Override
    public void keyReleased(final KeyEvent theEvent) {
        final int keycode = theEvent.getKeyCode();
        final String keytext = KeyEvent.getKeyText(keycode);
        if ("Enter".equals(keytext) || "Space".equals(keytext)) {
            JOptionPane.showMessageDialog(null, "Enter and Space key have been reserved", 
                                          null, JOptionPane.WARNING_MESSAGE);
        } else {
            myTextField.setText(keytext);
        }
    }
}

/*
 * TCSS 305
 * Assignment 6 ¨C Tetris
 */

package view;

import controller.SettingKeyListener;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * This is a class used to create a control setting panel.
 * 
 * @author Qing Bai
 * @version 5 March 2015
 */
@SuppressWarnings("serial")
public class TetrisSettingPanel extends JPanel {
    
    /**
     * This is a number of columns for all text fields in this panel.
     */
    public static final int TEXT_FIELD_LENGTH = 8;
    
    /**
     * This is number of rows for labels and text fields.
     */
    public static final int ROWS = 6;
    
    /**
     * This is control key texts in an order of left, right, down, drop, rotateCW, 
     * and rotateCCW.
     */
    private String[] myControlKeyTexts;
    
    /**
     * This is an array of JTextFields for corresponding control key texts. 
     */
    private JTextField[] myTextFields;
    
    /**
     * This is a constructor of this class.
     * 
     * @param theControlKeyTexts is an array that contains key texts of control keys
     */
    public TetrisSettingPanel(final String[] theControlKeyTexts) {
        super();
        myControlKeyTexts = theControlKeyTexts.clone();
        myTextFields = new JTextField[myControlKeyTexts.length];
        setup();
    }
    
    /**
     * This sets up this panel.
     */
    private void setup() {
        setLayout(new BorderLayout());
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(ROWS, 2));
        
        final String[] labels = {" Left:", " Right:", " Down:", " Drop:", " RotateCW:", 
                                 " RotateCCW:"};
        
        for (int i = 0; i < myControlKeyTexts.length; i++) {
            // creates a text field for a corresponding control key text 
            final JTextField field = new JTextField(TEXT_FIELD_LENGTH);
            field.setText(myControlKeyTexts[i]);
            myTextFields[i] = field;
            field.addKeyListener(new SettingKeyListener(field));
            // adds label and its corresponding text field in the same row of the grid layout
            panel.add(new JLabel(labels[i]));
            panel.add(field);
        }

        add(panel, BorderLayout.CENTER);
        add(createUpdateButton(), BorderLayout.SOUTH);
        addFocusFunction();
    }
    
    /**
     * This creates a button that updates changes made on the control keys.
     * 
     * @return a button that updates changes made on the control keys
     */
    private JButton createUpdateButton() {
        final JButton updateButton = new JButton("Update");
        
        updateButton.addActionListener(new ActionListener() {
            
            /**
             * This fires a property change event for control key changes.
             * 
             * @param theEvent is theEvent
             */
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                final String[] newKeys = getNewKeyTexts();
                
                if (isInputValid(newKeys)) {
                    firePropertyChange("controlKeys", myControlKeyTexts, newKeys.clone());
                    myControlKeyTexts = newKeys;
                } else {
                    JOptionPane.showMessageDialog(null, "Can't use same keys", "Error",
                                                  JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        return updateButton;
    }
    
    /**
     * This gets new key texts from the text fields.
     * 
     * @return an array of String that contains new control key texts
     */
    private String[] getNewKeyTexts() {
        final String[] newKeys = new String[myTextFields.length];
        
        for (int i = 0; i < newKeys.length; i++) {
            newKeys[i] = myTextFields[i].getText();
        }
        
        return newKeys;
    }
    
    /**
     * This makes sure each single key text is different.
     * 
     * @param theInputKeyTexts contains new control key texts that a player enters
     * @return true when each key text is different from one another. Otherwise, false
     */
    private boolean isInputValid(final String[] theInputKeyTexts) {
        boolean result = true;
        
        for (int i = 0; i < theInputKeyTexts.length && result; i++) {
            final String keyText = theInputKeyTexts[i];
            
            for (int j = i + 1; j < theInputKeyTexts.length && result; j++) {
                if (keyText.equals(theInputKeyTexts[j])) {
                    result = false;
                }
            }
        }
        
        return result;
    }
    
    /**
     * This ensures the only texts shown in the text fields are key texts being used.
     */
    private void addFocusFunction() {
        setFocusable(true);
       
        addFocusListener(new FocusAdapter() {
            
            /**
             * This avoids showing unused key texts in case that player changes control keys
             * but doesn't click the update button.
             * 
             * @param theEvent is the event
             */
            @Override
            public void focusGained(final FocusEvent theEvent) {
                for (int i = 0; i < myControlKeyTexts.length; i++) {
                    myTextFields[i].setText(myControlKeyTexts[i]);
                }
            }
        });
    }
    
}

/*
 * TCSS 305
 * Assignment 6 ¨C Tetris
 */

package view;

import controller.ControlKeyListener;
import controller.FrameFocusListener;
import controller.TimerListener;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.Timer;

import model.Board;
import sound.SoundPlayer;

/**
 * This is a class for graphic user interface of this game.
 * 
 * @author Qing Bai
 * @version 12 March 2015
 */
@SuppressWarnings("serial")
public class TetrisGUI extends JFrame {
    
    /**
     * This is initial control key texts in an order of left, right, down, drop, rotateCW, 
     * and rotateCCW.
     */
    private final String[] myInitialControlKeys;
    
    /**
     * This is a constructor of this class.
     * 
     * @param theInitialControlKeys is an array of String that contains control keys info
     */
    public TetrisGUI(final String[] theInitialControlKeys) {
        super("Tetris");
        myInitialControlKeys = theInitialControlKeys.clone();
    }
    
    /**
     * This sets up GUI of this game.
     */
    public void start() {
        final Board gameBoard = new Board();
        final Timer timer = new Timer(1000, new TimerListener(gameBoard));
        final SoundPlayer player = new SoundPlayer();
        final TetrisInfoPanel infoPanel = new TetrisInfoPanel(timer, player);
        final TetrisNextPieceDisplayBoard nextPiecePanel = new TetrisNextPieceDisplayBoard();
        final TetrisDisplayBoard displayPanel = new TetrisDisplayBoard();
        final ControlKeyListener keyListener = new ControlKeyListener(gameBoard, 
                                                                      myInitialControlKeys, 
                                                                      timer, player);
        gameBoard.addObserver(keyListener);
        gameBoard.addObserver(displayPanel);
        gameBoard.addObserver(nextPiecePanel);
        gameBoard.addObserver(infoPanel);
        
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(nextPiecePanel, BorderLayout.NORTH);
        panel.add(infoPanel, BorderLayout.CENTER);
        
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(createSettingMenu(keyListener));
        menuBar.add(createModeMenu(displayPanel));
        menuBar.add(createHelpMenu());
        
        addKeyListener(keyListener);
        addFocusListener(new FrameFocusListener(timer, player));
        setJMenuBar(menuBar);
        add(displayPanel, BorderLayout.CENTER);
        add(panel, BorderLayout.EAST);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }
    
    /**
     * This creates a setting menu for menu bar.
     * 
     * @param theKeyListener is the listener for control keys
     * @return a functional setting menu
     */
    private JMenu createSettingMenu(final ControlKeyListener theKeyListener) {
        final JMenu settingMenu = new JMenu("Setting");
        final JMenu controlSettingSubmenu = new JMenu("Control Setting");
        
        final TetrisSettingPanel settingPanel = new TetrisSettingPanel(myInitialControlKeys);
        settingPanel.addPropertyChangeListener(theKeyListener);
        
        controlSettingSubmenu.add(settingPanel);
        settingMenu.add(controlSettingSubmenu);
        
        return settingMenu;
    }
    
    /**
     * This creates a help menu for showing "how to play" and "About" information.
     * 
     * @return a menu that contains menu items showing "how to play" and "About" information
     */
    private JMenu createHelpMenu() {
        final JMenu helpMenu = new JMenu("Help");
        final JMenuItem howToPlay = new JMenuItem("How To Play");
        final JMenuItem about = new JMenuItem("About");
        
        howToPlay.addActionListener(new ActionListener() {
            /**
             * This shows a message for how to play this game.
             * 
             * @param theEvent is the event
             */
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                final String info = "Default control keys:\n" + "Enter = start / end game\n" 
                                    + "Space = pause / resume game\n" + "A = turn left\n" 
                                    + "D = turn right\n" + "S = down a line\n" 
                                    + "X = drop instantly\n" + "Q = rotate clockwise\n"  
                                    + "E = rotate counter-clockwise\n\n" + "Scoring:\n"
                                    + "1 line = 50 pt\n" + "2 lines = 120 pt\n" 
                                    + "3 lines = 190 pt\n" + "4 lines = 260 pt\n" 
                                    + "Every 200 pt for one level.";
                JOptionPane.showMessageDialog(null, info, "How to play", 
                                              JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        about.addActionListener(new ActionListener() {
            /**
             * This shows a message for source of the sound clips.
             * 
             * @param theEvent is the event
             */
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                JOptionPane.showMessageDialog(null, "All sound clips are from http://www."
                                              + "talkingwav.com", "Copyright information", 
                                              JOptionPane.INFORMATION_MESSAGE);
            }     
        });
        
        helpMenu.add(howToPlay);
        helpMenu.add(about);
        return helpMenu;
    }
    
    /**
     * This creates a mode menu that allows a player to switch between different game modes.
     * 
     * @param theDisplayBoard is a board displaying all moving pieces and filled blocks
     * @return a menu that contains two radio buttons that allow user to switch between modes
     */
    private JMenu createModeMenu(final TetrisDisplayBoard theDisplayBoard) {
        final JMenu gameModes = new JMenu("Modes");
        final JMenuItem normalMode = new JRadioButtonMenuItem("Normal");
        final JMenuItem mirrorMode = new JRadioButtonMenuItem("Mirror");
        final ButtonGroup buttonGroup = new ButtonGroup();
        
        normalMode.addActionListener(new ActionListener() {
            /**
             * This causes the display board to show tetris pieces in an normal way
             * 
             * @param theEvent is the event
             */
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                theDisplayBoard.setMirrorMode(false);
            } 
        });
        
        mirrorMode.addActionListener(new ActionListener() {
            /**
             * This causes the display board to show a mirror image mode.
             * 
             * @param theEvent is the event
             */
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                theDisplayBoard.setMirrorMode(true);
            } 
        });
        
        normalMode.setSelected(true);
        buttonGroup.add(normalMode);
        buttonGroup.add(mirrorMode);
        gameModes.add(normalMode);
        gameModes.add(mirrorMode);
        return gameModes;
    }
}

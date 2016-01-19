/*
 * TCSS 305
 * Assignment 6 ¨C Tetris
 */

package view;

import java.awt.Component;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Board.CompletedLines;
import model.Board.GameStatus;
import sound.SoundPlayer;


/**
 * This is a class used to create an info panel showing scores, levels, and number
 * of lines cleared.
 * 
 * @author Qing Bai
 * @version 12 March 2015
 */
@SuppressWarnings("serial")
public class TetrisInfoPanel extends JPanel implements Observer {
    
    /**
     * This is the font size used for labels.
     */
    public static final int FONT_SIZE = 18;
    
    /**
     * This is the maximum difficulty level.
     */
    public static final int MAXIMUM_LEVEL = 10;
    
    /**
     * This is a score needed to advance to next level.
     */
    public static final int SCORE_PER_LEVEL = 200;
    
    /**
     * This is base score for clearing one line.
     */
    public static final int BASE_SCORE = 50;
    
    /**
     * This is extra score used when multiple lines are cleared.
     */
    public static final int EXTRA_SCORE = 20;
    
    /**
     * This is the sound clip when lines are cleared.
     */
    public static final String CLEAR_SOUND = "music/clear_sound.wav";
    
    /**
     * This is difficulty level.
     */
    private int myLevel;
    
    /**
     * This is score.
     */
    private int myScore;
    
    /**
     * This is number of lines cleared.
     */
    private int myLines;
    
    /**
     * This is difficulty level label.
     */
    private final JLabel myLevelLabel;
    
    /**
     * This is score label.
     */
    private final JLabel myScoreLabel;
    
    /**
     * This is lines label.
     */
    private final JLabel myLinesLabel;
    
    /**
     * This is the timer used in this game.
     */
    private final Timer myTimer;
    
    /**
     * This is the sound player in this game.
     */
    private final SoundPlayer myPlayer;
    
    /**
     * This is a constructor of this info panel.
     * 
     * @param theTimer is the timer used in this game
     * @param thePlayer is the sound player in this game
     */
    public TetrisInfoPanel(final Timer theTimer, final SoundPlayer thePlayer) {
        super();
        myLevel = 1;
        myScore = 0;
        myLines = 0;
        myTimer = theTimer;
        myLevelLabel = new JLabel(Integer.toString(myLevel));
        myScoreLabel = new JLabel(Integer.toString(myScore));
        myLinesLabel = new JLabel(Integer.toString(myLines));
        myPlayer = thePlayer;
        setup();
    }
    
    /**
     * This sets up this panel.
     */
    private void setup() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        final JLabel[] labels = {new JLabel("Level"), myLevelLabel, new JLabel("Score"),
                                 myScoreLabel, new JLabel("Lines"), myLinesLabel};
        final Font labelFont = new Font(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE);
        
        for (final JLabel label : labels) {
            label.setFont(labelFont);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(label);
        }
    }
    
    /**
     * This updates current difficulty level if necessary.
     */
    private void updateLevel() {
        final int level = (int) Math.ceil(myScore * 1.0 / SCORE_PER_LEVEL);

        if (level <= MAXIMUM_LEVEL && level > myLevel) {
            myLevel = level;
            myLevelLabel.setText(Integer.toString(myLevel));
            // this calculates a ratio using the current level and the maximum level first
            // then use a decimal number from this ratio to multiply with the initial
            // delay of the timer
            final int delay = (int) (((MAXIMUM_LEVEL - myLevel + 1.0) / MAXIMUM_LEVEL) 
                              * myTimer.getInitialDelay());
            myTimer.setDelay(delay);
        }  
    }
    
    /**
     * This updates total number of lines that have been cleared.
     * 
     * @param theNumLinesCleared is number of lines that are just cleared
     */
    private void updateLines(final int theNumLinesCleared) {
        myLines += theNumLinesCleared;
        myLinesLabel.setText(Integer.toString(myLines));
    }
    
    /**
     * This updates score of this game.
     * 
     * @param theNumLinesCleared is number of lines that are just cleared
     */
    private void updateScore(final int theNumLinesCleared) {
        // 1 line = 50 pt, 2 lines = 120 pt, 3 lines = 190 pt, 4 lines = 260 pt
        myScore += theNumLinesCleared * BASE_SCORE + (theNumLinesCleared - 1) * EXTRA_SCORE;
        myScoreLabel.setText(Integer.toString(myScore));
    }
    
    /**
     * This resets score, level and number of lines cleared as well as the corresponding 
     * labels to their initial status.
     */
    private void reset() {
        myLevel = 1;
        myScore = 0;
        myLines = 0;
        myLevelLabel.setText(Integer.toString(myLevel));
        myScoreLabel.setText(Integer.toString(myScore));
        myLinesLabel.setText(Integer.toString(myLines));
    }
    
    /**
     * This updates data for scores, number of lines cleared, and level.
     * 
     * @param theObservable is an observable object
     * @param theData is the data for changes being made
     */
    @Override
    public void update(final Observable theObservable, final Object theData) {
        if (theData instanceof CompletedLines) {
            final CompletedLines lines = (CompletedLines) theData;
            // get number of lines that are just cleared
            final int numLines = lines.getCompletedLines().size();
            myPlayer.play(CLEAR_SOUND);
            updateLines(numLines);
            updateScore(numLines);
            updateLevel();
        } else if (theData instanceof GameStatus && !((GameStatus) theData).isGameOver()) {
            // ensures score, level, and number of lines cleared are in their initial status
            // whenever a player starts a new game
            reset();
        }
    }
}

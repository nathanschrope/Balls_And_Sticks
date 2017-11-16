import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Class BnsUI provides the graphical user interface for the game of Balls and
 * Sticks.
 *
 * @author  Alan Kaminsky and Nathan Schrope
 * @version 10-Nov-2017
 */
public class BnsUI
    implements ModelListener
{

// Hidden constants.

    private static final int GAP = 10;

// Hidden data members.

    private JFrame frame;
    private JBoard board;
    private JTextField messageField;
    private JButton newGameButton;
    private ViewListener viewListener;

// Hidden constructors.

    /**
     * Construct a new game UI.
     *
     * @param  name  Player's name.
     */
    private BnsUI
    (String name)
    {
        frame = new JFrame ("B & S -- "+name);
        JPanel panel = new JPanel();
        panel.setLayout (new BoxLayout (panel, BoxLayout.Y_AXIS));
        panel.setBorder (BorderFactory.createEmptyBorder (GAP, GAP, GAP, GAP));
        frame.add (panel);

        board = new JBoard();
        board.setFocusable (false);
        panel.add (board);
        panel.add (Box.createVerticalStrut (GAP));

        messageField = new JTextField (1);
        panel.add (messageField);
        messageField.setEditable (false);
        messageField.setFocusable (false);
        panel.add (Box.createVerticalStrut (GAP));

        newGameButton = new JButton ("New Game");
        newGameButton.setAlignmentX (0.5f);
        newGameButton.setFocusable (false);
        panel.add (newGameButton);

        frame.pack();
        frame.setVisible (true);
        //Nathan Schrope added
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                 sendQuit();
            }
        });
        newGameButton.setEnabled(false);
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewListener.newBoard();
            }
        });
        messageField.setText("Waiting for partner");
    }

    //Nathan Schrope added

    /**
     * Makes the i stick disappear
     * @param  i  Stick index (0..11).
     */
    @Override
    public void stickChange(int i, boolean visible) {
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                board.setStickVisible(i,visible);
            }
        });
    }

    /**
     * Makes the i ball disappear
     * @param  i  Ball index (0..8).
     */
    @Override
    public void ballChange(int i, boolean visible) {
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                board.setBallVisible(i,visible);
            }
        });
    }

    /**
     * Throws out frame. Exits
     */
    public void quit(){
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                frame.dispose();
                System.exit(0);
            }
        });
    }

    /**
     * Starts the game by enabling the new game button
     */
    @Override
    public void start() {
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                newGameButton.setEnabled(true);
            }
        });
    }

    /**
     * Changes message on view to given string
     * @param name
     */
    @Override
    public void changeMessage(String name) {
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                messageField.setText(name);
            }
        });
    }

    /**
     * Changes the viewListener to the given view listener
     * @param vl
     */
    public void setViewListener(final ViewListener vl) {
        onSwingThreadDo(new Runnable() {
            public void run() {
                BnsUI.this.viewListener = vl;
                board.setBoardListener(new BoardListener() {
                    @Override
                    public void ballClicked(int i) {
                        viewListener.ballClicked(i,null);
                    }

                    @Override
                    public void stickClicked(int i) {
                        viewListener.stickClicked(i,null);
                    }
                });
            }
        });
    }

    /**
     * Runs swing thread
     * @param task : task to be run
     */
    private static void onSwingThreadDo(Runnable task){
        try{
            SwingUtilities.invokeAndWait(task);
        }catch(Throwable e){
            System.out.println("Runnable Task Error");
            System.exit(1);
        }
    }

    /**
     * creates and runs a new BnsUI
     * @param session : name
     * @return a working view
     */
    public static BnsUI create(final String session){
        final BnsUIRef ref = new BnsUIRef();
        onSwingThreadDo(new Runnable() {
            @Override
            public void run() {
                ref.ui = new BnsUI(session);
            }
        });
        return ref.ui;
    }

    /**
     * Wrapper class
     */
    private static class BnsUIRef{
        public BnsUI ui;
    }

    /**
     * Tells the viewlistener to send a quit message
     */
    private void sendQuit(){
        viewListener.quit();
    }
}
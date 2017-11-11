import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Class BnsUI provides the graphical user interface for the game of Balls and
 * Sticks.
 *
 * @author  Alan Kaminsky
 * @version 12-Oct-2017
 */
public class BnsUI
    implements ModelListener
{

// Hidden constants.

    private static final int GAP = 10;

    /**
     * Number of balls.
     */
    public static final int N_BALLS = 9;

    /**
     * Number of sticks.
     */
    public static final int N_STICKS = 12;

// Hidden data members.

    private JFrame frame;
    private JBoard board;
    private JTextField messageField;
    private JButton newGameButton;
    private ViewListener viewListener;
    private Boolean turn = false;

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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                 viewListener.quit();
            }
        });
    }

    public JBoard getBoard() {
        return board;
    }

    @Override
    public void stickClicked(int i) {
        board.setStickVisible(i,false);
    }

    @Override
    public void ballClicked(int i) {
        board.setBallVisible(i,false);
    }

    public void clearBoard(boolean turn){
        for(int x = 0; x < N_BALLS; x++){
            board.setBallVisible(x,true);
        }
        for(int x = 0; x < N_STICKS; x++){
            board.setStickVisible(x,true);
        }
        this.turn = turn;
        if(turn){
            board.setBoardListener(viewListener);
        }
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewListener.newBoard();
            }
        });
    }

    public void quit(){
        frame.dispose();
    }

    @Override
    public void lose(String name) {
        messageField.setText(name + " wins!");
    }

    @Override
    public void win() {
        messageField.setText("You win!");
    }

    public void setViewListener(final ViewListener vl) {
        onSwingThreadDo(new Runnable() {
            public void run() {
                BnsUI.this.viewListener = vl;
                board.setBoardListener(null);
            }
        });
    }

    private static void onSwingThreadDo(Runnable task){
        try{
            SwingUtilities.invokeAndWait(task);
        }catch(Throwable e){
            System.out.println("Runnable Task Error");
            System.exit(1);
        }
    }

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

    private static class BnsUIRef{
        public BnsUI ui;
    }

    public void changeTurn(){
        turn = !turn;
        if(turn){
            board.setBoardListener(viewListener);
        }else{
            board.setBoardListener(null);
        }
    }
}
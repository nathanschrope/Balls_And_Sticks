import javax.swing.text.View;

/**
 * View Listener for the game balls and sticks
 */
public interface ViewListener {

    /**
     * Join a session
     * @param name : player name
     */
    public void join(String name, ViewProxy view);

    /**
     * Reset Board
     */
    public void newBoard();

    /**
     * Quit Game
     */
    public void quit();

    public void stickClicked(int x, ModelListener ml);

    public void ballClicked(int x, ModelListener ml);
}
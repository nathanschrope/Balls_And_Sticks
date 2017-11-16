/**
 * View Listener for the game balls and sticks
 */
public interface ViewListener {

    /**
     * Join a session
     * @param name : player name
     * @param view : The view trying to join
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

    /**
     * notifies that a x stick was clicked
     * @param x : the stick number
     * @param ml : the model listener that received the message
     */
    public void stickClicked(int x, ModelListener ml);

    /**
     * notifies that a x ball was clicked
     * @param x : the ball number
     * @param ml : the model listener that received the message
     */
    public void ballClicked(int x, ModelListener ml);
}
/**
 * View Listener for the game balls and sticks
 */
public interface ViewListener extends BoardListener{

    /**
     * Join a session
     * @param name : player name
     * @param proxy : client
     */
    public void join(String name, ViewProxy proxy);

    /**
     * Reset Board
     */
    public void newBoard();

    /**
     * Quit Game
     */
    public void quit();
}
/**
 * Model listener for the balls and stick game
 */
public interface ModelListener{

    /**
     * Changes the turn
     */
    public void changeMessage(String name);


    public void ballChange(int num, boolean visible);

    public void stickChange(int num, boolean visible);

    /**
     * Exits
     */
    public void quit();

    public void start();
}

/**
 * Model listener for the balls and stick game
 */
public interface ModelListener{

    /**
     * Changes the turn
     */
    public void changeMessage(String name);

    /**
     * Changes visibility of num stick to the boolean visible
     * @param num
     * @param visible
     */
    public void ballChange(int num, boolean visible);

    /**
     * Changes visibility of num ball to the boolean visible
     * @param num
     * @param visible
     */
    public void stickChange(int num, boolean visible);

    /**
     * Exits
     */
    public void quit();

    /**
     * Set up
     */
    public void start();
}

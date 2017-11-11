/**
 * Model listener for the balls and stick game
 */
public interface ModelListener extends BoardListener{

    /**
     * Starts the game board over
     * @param turn : is it my turn?
     */
    public void clearBoard(boolean turn);

    /**
     * Changes the turn
     */
    public void changeTurn();

    /**
     * Sets Game over message to the other player winning
     */
    public void lose();

    /**
     * Sets Game over message to this player winning
     */
    public void win();

    /**
     * Exits
     */
    public void quit();

    public void start(String name);
}

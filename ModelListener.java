public interface ModelListener extends BoardListener{

    public void clearBoard(boolean turn);

    public void changeTurn();

    public void lose(String name);

    public void win();

    public void quit();

}

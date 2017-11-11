public interface ViewListener extends BoardListener{

    public void join(String name, ViewProxy proxy);

    public void newBoard();

    public void quit();
}
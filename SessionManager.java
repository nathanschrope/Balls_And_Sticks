/**
 * Controls what game clients connect to. Clients are initially connected here only to be connected to a session
 */
public class SessionManager implements ViewListener {

    /**
     * List of current sessions
     */
    private BnsModel curModel = null;

    private int numClients = 0;

    /**
     * Default constructor
     */
    public SessionManager(){}

    /**
     * Gives a client a session to join
     * @param name : player name
     * @param view : client
     */
    @Override
    public synchronized void join(String name, ViewProxy view) {
        if(curModel == null || numClients >= 2){
            curModel = new BnsModel();
            numClients = 1;
        }else{
            numClients++;
        }
        curModel.addBoardListener(view,name);
        view.setViewListener(curModel);
    }

    /**
     * does nothing
     */
    @Override
    public void newBoard() { }

    /**
     * does nothing
     * @param x : ball number
     */
    @Override
    public void ballClicked(int x, ModelListener ml) { }

    /**
     * does nothing
     * @param x : stick number
     */
    @Override
    public void stickClicked(int x, ModelListener ml) { }

    /**
     * does nothing
     */
    @Override
    public void quit() {

    }
}
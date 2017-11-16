import java.util.ArrayList;

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
     * @param proxy : client
     */
    @Override
    public synchronized void join(String name, ViewProxy proxy) {
        if(curModel == null || numClients >= 2){
            curModel = new BnsModel();
            numClients = 1;
        }else{
            numClients++;
        }
        curModel.addBoardListener(proxy,name);
        proxy.setViewListener(curModel);
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
    public void ballClicked(int x) { }

    /**
     * does nothing
     * @param x : stick number
     */
    @Override
    public void stickClicked(int x) { }

    /**
     * does nothing
     */
    @Override
    public void quit() {

    }
}
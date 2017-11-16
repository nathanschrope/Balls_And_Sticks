/**
 * Controls what game clients connect to. Clients are initially connected here only to be connected to a session
 */
public class SessionManager implements ViewListener {

    /**
     * List of current sessions
     */
    private BnsModel curModel = null;
    /**
     * viewProxy already in the current model if there is one so far
     */
    private ViewProxy viewProxy = null;

    /**
     * the number of clients in the model(session)
     */
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
            viewProxy = view;
            numClients = 1;
            curModel.addBoardListener(view,name);
            view.setViewListener(curModel);
        }else{
            numClients++;
            if(viewProxy.isAlive()) {
                curModel.addBoardListener(view, name);
                view.setViewListener(curModel);
            }else{
                viewProxy.exit();
                curModel = new BnsModel();
                viewProxy = view;
                numClients = 1;
                curModel.addBoardListener(view,name);
                view.setViewListener(curModel);
            }
        }
    }

    /**
     * does nothing
     */
    @Override
    public void newBoard() { }

    /**
     * does nothing
     * @param x
     * @param ml
     */
    @Override
    public void ballClicked(int x, ModelListener ml) { }

    /**
     * does nothing
     * @param x
     * @param ml
     */
    @Override
    public void stickClicked(int x, ModelListener ml) { }

    /**
     * does nothing
     */
    @Override
    public void quit() { }
}
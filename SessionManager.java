import java.util.ArrayList;

/**
 * Controls what game clients connect to. Clients are initially connected here only to be connected to a session
 */
public class SessionManager implements ViewListener {

    /**
     * List of current sessions
     */
    private SessionList sessions = new SessionList();

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
        BnsModel model = sessions.getOpen();
        model.addBoardListener(proxy,name);
        proxy.setViewListener(model);
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

    /**
     * A specific Array List that holds Models
     */
    public class SessionList extends ArrayList<BnsModel>{

        /**
         * returns an non filled Model
         * @return Model to be joined
         */
        public BnsModel getOpen(){
            for(BnsModel temp:this){
                if(temp.getNumberOfClients() < 2){
                    return temp;
                }
            }
            this.add(new BnsModel());
            return this.get(this.size()-1);
        }

    }

}
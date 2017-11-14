import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Backend of Game, does all commputations for Bns Server, to be run with multiple Clients
 *
 * __author__ = Nathan Schrope @nks9452
 */
public class BnsModel implements ViewListener{

    /**
     * All listeners in this session
     */
    private LinkedList<ModelListener> listeners = new LinkedList<>();
    /**
     * Names of players in this session
     */
    private LinkedList<String> names = new LinkedList<>();
    /**
     * Current number of Clients in this session
     */
    private int numberOfClients = 0;
    /**
     * Number of balls left while playing the game
     */
    private int ballsLeft;
    /**
     * first player turn?
     */
    private boolean first;

    public ArrayList<ArrayList<Integer>> ballAndSticks = new ArrayList<>();

    /**
     * default constructor
     */
    public BnsModel(){
        ballAndSticks.add(new ArrayList<>());
        ballAndSticks.get(0).add(0);    ballAndSticks.get(0).add(6);
        ballAndSticks.add(new ArrayList<>());
        ballAndSticks.get(1).add(0);    ballAndSticks.get(1).add(1);    ballAndSticks.get(1).add(8);
        ballAndSticks.add(new ArrayList<>());
        ballAndSticks.get(2).add(1);    ballAndSticks.get(2).add(10);
        ballAndSticks.add(new ArrayList<>());
        ballAndSticks.get(3).add(2);    ballAndSticks.get(3).add(6);    ballAndSticks.get(3).add(7);
        ballAndSticks.add(new ArrayList<>());
        ballAndSticks.get(4).add(2);    ballAndSticks.get(4).add(3);    ballAndSticks.get(4).add(8);    ballAndSticks.get(4).add(9);
        ballAndSticks.add(new ArrayList<>());
        ballAndSticks.get(5).add(3);    ballAndSticks.get(5).add(10);    ballAndSticks.get(5).add(11);
        ballAndSticks.add(new ArrayList<>());
        ballAndSticks.get(6).add(4);    ballAndSticks.get(6).add(7);
        ballAndSticks.add(new ArrayList<>());
        ballAndSticks.get(7).add(4);    ballAndSticks.get(7).add(5);    ballAndSticks.get(7).add(9);
        ballAndSticks.add(new ArrayList<>());
        ballAndSticks.get(8).add(5);    ballAndSticks.get(8).add(11);
    }

    /**
     * Adds client to session
     * @param modelListener : client to be added
     * @param name : player name
     */
    public synchronized void addBoardListener(ModelListener modelListener,String name){
        listeners.add(modelListener);
        names.add(name);
        numberOfClients++;
        if(numberOfClients == 2){
            start();
            newBoard();
        }

    }

    /**
     * Makes changes to board and alerts the clients connected to this session
     * @param x : stick number
     */
    @Override
    public synchronized void stickClicked(int x) {
        Iterator<ModelListener> iter = listeners.iterator();
        while(iter.hasNext()){
            ModelListener listener = iter.next();
            listener.stickClicked(x);
            listener.changeTurn();
            first = !first;
        }
    }

    /**
     * Makes changes to board. Takes sticks connected to ball out as well. Alerts clients connected to this session
     * @param x : ball number
     */
    @Override
    public synchronized void ballClicked(int x) {
        Iterator<ModelListener> iter = listeners.iterator();
        while(iter.hasNext()){
            ModelListener listener = iter.next();
            listener.ballClicked(x);
            listener.changeTurn();
            first = !first;
        }

        for(int stick:ballAndSticks.get(x)){
            Iterator<ModelListener> iterator = listeners.iterator();
            while(iterator.hasNext()){
                ModelListener temp = iterator.next();
                temp.stickClicked(stick);
            }
        }

        ballsLeft--;
        checkEnd();

    }

    /**
     * Sets all balls and sticks visible and alerts all clients connected to this session
     */
    @Override
    public synchronized void newBoard() {
        ballsLeft = JBoard.N_BALLS;
        first = true;
        listeners.get(0).clearBoard(true);
        listeners.get(1).clearBoard(false);

    }

    /**
     * @param name : player name
     * @param proxy : client
     */
    @Override
    public synchronized void join(String name, ViewProxy proxy) { }

    /**
     * Closes session and alerts clients to close
     */
    public synchronized void quit(){
        for(ModelListener temp: listeners){
            temp.quit();
        }
        listeners.clear();

    }

    /**
     * returns the number of clients
     * @return
     */
    public synchronized int getNumberOfClients() {
        return numberOfClients;
    }

    /**
     * Checks end game conditions
     * Alerts clients if game is over
     */
    public synchronized void checkEnd(){
        if(ballsLeft == 0){
            if(first){
                listeners.get(1).win();
                listeners.get(0).lose();
            }else{
                listeners.get(0).win();
                listeners.get(1).lose();
            }
        }
    }

    private synchronized void start(){
        listeners.get(0).start(names.get(1));
        listeners.get(1).start(names.get(0));
    }
}

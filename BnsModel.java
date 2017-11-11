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
     * The main board
     */
    private JBoard board = new JBoard();
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

    /**
     * default constructor
     */
    public BnsModel(){ }

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
        board.setStickVisible(x,false);
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
        board.setBallVisible(x,false);
        Iterator<ModelListener> iter = listeners.iterator();
        while(iter.hasNext()){
            ModelListener listener = iter.next();
            listener.ballClicked(x);
            listener.changeTurn();
            first = !first;
        }

        ArrayList<Integer> sticks = new ArrayList<>();

        switch(x){
            case 0:
                sticks.add(0);
                sticks.add(6);
                break;
            case 1:
                sticks.add(0);
                sticks.add(1);
                sticks.add(8);
                break;
            case 2:
                sticks.add(1);
                sticks.add(10);
                break;
            case 3:
                sticks.add(2);
                sticks.add(6);
                sticks.add(7);
                break;
            case 4:
                sticks.add(2);
                sticks.add(3);
                sticks.add(8);
                sticks.add(9);
                break;
            case 5:
                sticks.add(3);
                sticks.add(10);
                sticks.add(11);
                break;
            case 6:
                sticks.add(7);
                sticks.add(4);
                break;
            case 7:
                sticks.add(4);
                sticks.add(5);
                sticks.add(9);
                break;
            case 8:
                sticks.add(5);
                sticks.add(11);
            default:
                System.out.println("Not a ball Number");
                break;
        }

        for(int stick:sticks){
            board.setStickVisible(stick,false);
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
        for(int balls = 0; balls < JBoard.N_BALLS;balls++){
            board.setBallVisible(balls,true);
        }
        for(int sticks = 0; sticks < JBoard.N_STICKS;sticks++){
            board.setStickVisible(sticks,true);
        }

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
    public int getNumberOfClients() {
        return numberOfClients;
    }

    /**
     * Checks end game conditions
     * Alerts clients if game is over
     */
    public void checkEnd(){
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

    private void start(){
        listeners.get(0).start(names.get(1));
        listeners.get(1).start(names.get(0));
    }
}

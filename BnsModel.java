import java.util.ArrayList;
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
            listeners.get(0).start();
            listeners.get(1).start();
            newBoard();
        }

    }

    /**
     * Makes changes to board and alerts the clients connected to this session
     * @param x : stick number
     */
    public synchronized void stickClicked(int x, ModelListener ml) {
        if((first && ml == listeners.get(0)) || (!first && ml == listeners.get(1))){
            listeners.get(0).stickChange(x,false);
            listeners.get(1).stickChange(x,false);
            changeTurn();
        }
    }

    /**
     * Takes sticks connected to ball out as well. Alerts clients connected to this session
     * @param x : ball number
     */
    public synchronized void ballClicked(int x, ModelListener ml) {
        if((first && ml == listeners.get(0)) || (!first && ml == listeners.get(1))){
            ballsLeft--;
            listeners.get(0).ballChange(x,false);
            listeners.get(1).ballChange(x,false);
            for(int temp: ballAndSticks.get(x)){
                listeners.get(0).stickChange(temp,false);
                listeners.get(1).stickChange(temp,false);
            }
            if(ballsLeft == 0){
                if(!first){
                    listeners.get(0).changeMessage("You win!");
                    listeners.get(1).changeMessage(names.get(0) + " wins!");
                }else{
                    listeners.get(0).changeMessage(names.get(1) + " wins!");
                    listeners.get(1).changeMessage("You win!");
                }
            }else{
                changeTurn();
            }
        }
    }

    /**
     * Sets all balls and sticks visible and alerts all clients connected to this session
     */
    public synchronized void newBoard() {
        ballsLeft = JBoard.N_BALLS;
        for(int x = 0; x < JBoard.N_BALLS; x++){
            listeners.get(0).ballChange(x,true);
            listeners.get(1).ballChange(x,true);
        }
        for(int x = 0; x < JBoard.N_STICKS; x++){
            listeners.get(0).stickChange(x,true);
            listeners.get(1).stickChange(x,true);
        }
        listeners.get(0).changeMessage("Your turn");
        listeners.get(1).changeMessage(names.get(0) + "'s turn");
        first = true;
    }

    /**
     * @param name : player name
     */
    @Override
    public synchronized void join(String name, ViewProxy view) { }

    /**
     * Closes session and alerts clients to close
     */
    public synchronized void quit(){
        for(ModelListener temp: listeners){
            temp.quit();
        }
        listeners.clear();

    }

    private synchronized void changeTurn(){
        first = !first;
        if(first){
            listeners.get(0).changeMessage("Your turn");
            listeners.get(1).changeMessage(names.get(0) + "'s turn");
        }else{
            listeners.get(0).changeMessage(names.get(1) + "'s turn");
            listeners.get(1).changeMessage("Your turn");
        }
    }
}

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class BnsModel implements ViewListener{
    /**
     * Number of balls.
     */
    public static final int N_BALLS = 9;

    /**
     * Number of sticks.
     */
    public static final int N_STICKS = 12;

    private JBoard board = new JBoard();
    private LinkedList<ModelListener> listeners = new LinkedList<>();
    private LinkedList<String> names = new LinkedList<>();
    private int numberOfClients = 0;
    private int ballsLeft;
    private boolean first;

    public BnsModel(){ }

    public synchronized void addBoardListener(ModelListener modelListener,String name){
        listeners.add(modelListener);
        names.add(name);
        numberOfClients++;
        if(numberOfClients == 2){
            newBoard();
        }

    }

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

    @Override
    public synchronized void newBoard() {
        ballsLeft = N_BALLS;
        first = true;
        for(int balls = 0; balls < N_BALLS;balls++){
            board.setBallVisible(balls,true);
        }
        for(int sticks = 0; sticks < N_STICKS;sticks++){
            board.setStickVisible(sticks,true);
        }

        listeners.get(0).clearBoard(true);
        listeners.get(1).clearBoard(false);

    }

    @Override
    public synchronized void join(String name, ViewProxy proxy) { }

    public synchronized void quit(){
        for(ModelListener temp: listeners){
            temp.quit();
        }

    }

    public int getNumberOfClients() {
        return numberOfClients;
    }

    public void checkEnd(){
        if(ballsLeft == 0){
            if(first){
                listeners.get(1).win();
                listeners.get(0).lose(names.get(0));
            }else{
                listeners.get(0).win();
                listeners.get(1).lose(names.get(1));
            }
        }
    }
}

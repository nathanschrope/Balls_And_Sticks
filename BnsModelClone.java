import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BnsModelClone implements ModelListener{

    private ModelListener modelListener;
    private JBoard board = new JBoard();
    private boolean turn;

    public BnsModelClone(){
    }

    public JBoard getBoard() {
        return board;
    }

    @Override
    public void start(String name) {
        modelListener.start(name);
    }

    @Override
    public void quit() {
        modelListener.quit();
    }

    @Override
    public void win() {
        modelListener.win();
    }

    /**
     * changes turn
     */
    @Override
    public void changeTurn(){
        turn = !turn;
        if(turn){
            board.setBoardListener(modelListener);
        }else{
            board.setBoardListener(null);
        }
    }

    @Override
    public void ballClicked(int i) {

    }

    @Override
    public void stickClicked(int i) {

    }

    @Override
    public void clearBoard(boolean turn) {
        this.turn = turn;
        for(int x = 0; x < JBoard.N_BALLS; x++){
            board.setBallVisible(x,true);
        }
        for(int x = 0; x < JBoard.N_STICKS; x++){
            board.setStickVisible(x,true);
        }
        modelListener.clearBoard(turn);
    }

    @Override
    public void lose() {

    }

    public void setModelListener(ModelListener ml){
        modelListener = ml;
    }
}

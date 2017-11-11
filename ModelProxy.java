import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Sends messages to and receives messages form the Server. Messages received change Client view
 */
public class ModelProxy implements ViewListener {

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private ModelListener modelListener;

    /**
     * Only Constructor
     * @param socket : socket to connect to
     */
    public ModelProxy(Socket socket) {
        try {
            this.socket = socket;
            socket.setTcpNoDelay(true);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        }catch(IOException e){
            System.out.println("Model Proxy failed to set up.");
            System.exit(1);
        }
    }

    /**
     * Sets the board listener
     * @param bl : board listener to be change to
     */
    public void setBoardListener(ModelListener bl){
        this.modelListener = bl;
        new ReaderThread() .start();
    }

    /**
     * Sends a message to the server that a stick was clicked
     * @param x : stick number
     */
    @Override
    public void stickClicked(int x) {
        try{
            out.writeByte('S');
            out.writeByte(x);
            out.flush();
        }catch(IOException e){
            System.out.println("Printer Error");
            System.exit(1);
        }
    }

    /**
     * Sends a message to the server saying a ball was clicked
     * @param x : ball number
     */
    @Override
    public void ballClicked(int x) {
        try{
            out.writeByte('B');
            out.writeByte(x);
            out.flush();
        }catch(IOException e){
            System.out.println("Printer Error");
            System.exit(1);
        }
    }

    /**
     * Sends the server a message to reset the board
     */
    @Override
    public void newBoard() {
        try{
            out.writeByte('C');
            out.flush();
        }catch(IOException e){
            System.out.println("Printer Error");
            System.exit(1);
        }
    }

    /**
     * sends message to join
     * @param name : player name
     * @param proxy : client
     */
    @Override
    public void join(String name, ViewProxy proxy) {
        try {
            out.writeByte('J');
            out.writeUTF(name);
            out.flush();
        }catch(IOException e){
            System.out.println("Message failure");
        }
    }

    /**
     * Sends a message to the server to end game
     */
    @Override
    public void quit() {
        try {
            out.writeByte('Q');
            out.flush();
        }catch(IOException e){
            System.out.println("Message failure");
        }
    }

    /**
     * Reads in the input from the server
     */
    private class ReaderThread extends Thread{
        @Override
        public void run() {
            try{
                for(;;){
                    int x;
                    boolean turn;
                    byte b = in.readByte();
                    String name;
                    switch(b){
                        case 'S':
                            x = in.readByte();
                            modelListener.stickClicked(x);
                            break;
                        case 'B':
                            x = in.readByte();
                            modelListener.ballClicked(x);
                            break;
                        case 'C':
                            turn= in.readBoolean();
                            modelListener.clearBoard(turn);
                            break;
                        case 'T':
                            modelListener.changeTurn();
                            break;
                        case 'L':
                            modelListener.lose();
                            break;
                        case 'W':
                            modelListener.win();
                            break;
                        case 'Q':
                            modelListener.quit();
                            in.close();
                            out.close();
                            break;
                        case 'I':
                            name = in.readUTF();
                            modelListener.start(name);
                        default:
                            System.out.println("Bad Message");
                            break;
                    }
                }
            }catch(IOException e){
                System.out.println("Reader Error");
                System.exit(1);
            }
        }
    }

}

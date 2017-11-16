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
     * Sets the model listener
     * @param ml : model listener to be change to
     */
    public void setModelListener(ModelListener ml){
        this.modelListener = ml;
        new ReaderThread() .start();
    }

    /**
     * Sends a message to the server that a stick was clicked
     * @param x : stick number
     * @param ml : nothing for this instance
     */
    @Override
    public void stickClicked(int x, ModelListener ml) {
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
     * @param ml : not used in this instance
     */
    @Override
    public void ballClicked(int x,ModelListener ml) {
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
     * Sends a message to join
     * @param name : player name
     * @param view : not used in this instance
     */
    @Override
    public void join(String name, ViewProxy view) {
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
                    boolean bool;
                    String string;
                    byte b = in.readByte();
                    switch(b){
                        case 'S':
                            x = in.readByte();
                            bool = in.readBoolean();
                            modelListener.stickChange(x,bool);
                            break;
                        case 'B':
                            x = in.readByte();
                            bool = in.readBoolean();
                            modelListener.ballChange(x,bool);
                            break;
                        case 'I':
                            modelListener.start();
                            break;
                        case 'M':
                            string = in.readUTF();
                            modelListener.changeMessage(string);
                            break;
                        case 'Q':
                            modelListener.quit();
                            in.close();
                            out.close();
                            break;
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

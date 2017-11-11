import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * View Proxy for the Server to send and receive messages from client
 */
public class ViewProxy implements ModelListener{

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private ViewListener viewListener;

    /**
     * Only Constructor
     * @param socket : socket to connect to
     */
    public ViewProxy(Socket socket) {
        this.socket = socket;
        try {
            socket.setTcpNoDelay(true);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (SocketException e) {
            System.out.println("Socket Error");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Socket IO Error");
            System.exit(1);
        }
    }

    /**
     * Sets the view Listener. Starts a new thread if it has not already
     * @param vl : viewlistener to be change to
     */
    public void setViewListener(ViewListener vl){
        if(this.viewListener == null){
            this.viewListener = vl;
            new ReaderThread() .start();
        }else{
            this.viewListener = vl;
        }
    }

    /**
     * Tells the client to remove stick
     * @param  i  Stick index (0..11).
     */
    @Override
    public void stickClicked(int i) {
        try {
            out.writeByte('S');
            out.writeByte(i);
            out.flush();
        }catch(IOException e){
            System.out.println("Printer Error");
            System.exit(1);
        }
    }

    /**
     * Tells client to remove ball
     * @param  i  Ball index (0..8).
     */
    @Override
    public void ballClicked(int i) {
        try{
            out.writeByte('B');
            out.writeByte(i);
            out.flush();
        }catch(IOException e){
            System.out.println("Printer Error");
            System.exit(1);
        }
    }

    /**
     * Tells client to reset board
     * @param turn : clients trun
     */
    @Override
    public void clearBoard(boolean turn) {
        try{
            out.writeByte('C');
            out.writeBoolean(turn);
            out.flush();
        }catch(IOException e){
            System.out.println("Printer Error");
            System.exit(1);
        }
    }

    /**
     * Tells client to change turns
     */
    @Override
    public void changeTurn() {
        try{
            out.write('T');
            out.flush();
        }catch(IOException e){
            System.out.println("Printer Error");
            System.exit(1);
        }
    }

    /**
     * Tells the client who they lost to
     */
    public void lose(){
        try{
            out.write('L');
            out.flush();
        }catch(IOException e){
            System.out.println("Printer Error");
            System.exit(1);
        }
    }

    /**
     * Tells the client they won
     */
    public void win(){
        try{
            out.write('W');
            out.flush();
        }catch(IOException e){
            System.out.println("Printer Error");
            System.exit(1);
        }
    }

    /**
     * Tells the client to exit
     */
    public void quit(){
        try{
            out.write('Q');
            out.flush();
            out.close();
            in.close();
        }catch(IOException e){
            System.out.println("Printer Error");
            System.exit(1);
        }
    }

    @Override
    public void start(String name) {
        try{
            out.write('I');
            out.writeUTF(name);
            out.flush();
        }catch(IOException e){
            System.out.println("Printer Error");
            System.exit(1);
        }
    }

    /**
     * Reads in bytes from client and changes the model
     */
    private class ReaderThread extends Thread{
        @Override
        public void run() {
            try{
                for(;;){
                    int x;
                    byte b = in.readByte();
                    String name;
                    switch (b){
                        case 'J':
                            name = in.readUTF();
                            viewListener.join(name,ViewProxy.this);
                            break;
                        case 'B':
                            x = in.readByte();
                            viewListener.ballClicked(x);
                            break;
                        case 'S':
                            x = in.readByte();
                            viewListener.stickClicked(x);
                            break;
                        case 'C':
                            viewListener.newBoard();
                            break;
                        case 'Q':
                            viewListener.quit();
                            break;
                        default:
                            System.out.println("Bad Message");
                            break;
                    }
                }
            }catch(IOException e){
            }finally {
                try{
                    socket.close();
                }catch(IOException e){
                    System.out.println("Closing Socket Error");
                    System.exit(1);
                }
            }
        }
    }

}

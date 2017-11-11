import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ModelProxy implements ViewListener {

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private ModelListener modelListener;

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

    public void setBoardListener(ModelListener bl){
        this.modelListener = bl;
        new ReaderThread() .start();
    }

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

    @Override
    public void quit() {
        try {
            out.writeByte('Q');
            out.flush();
        }catch(IOException e){
            System.out.println("Message failure");
        }
    }

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
                            name = in.readUTF();
                            modelListener.lose(name);
                            break;
                        case 'W':
                            modelListener.win();
                            break;
                        case 'Q':
                            modelListener.quit();
                            break;
                        default:
                            System.out.println("Bad Message");
                            break;
                    }
                }
            }catch(IOException e){
            }
        }
    }

}

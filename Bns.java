import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Start a client in the Balls and Sticks game
 */
public class Bns
{

    /**
     * Main program.
     */
    public static void main(String[] args)
    {
        if (args.length != 3) usage();
        String host = args[0];
        int port = Integer.parseInt (args[1]);
        String session = args[2];

        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port));
            BnsUI view = BnsUI.create (session);
            ModelProxy proxy = new ModelProxy (socket);
            view.setViewListener (proxy);
            proxy.setBoardListener (view);
            proxy.join(session,null);
        }catch(IOException e){
            System.out.println("Socket Error");
            System.exit(1);
        }
    }

    /**
     * Print a usage message and exit.
     */
    private static void usage()
    {
        System.err.println ("Usage: java Client <host> <port> <name>");
        System.exit (1);
    }

}
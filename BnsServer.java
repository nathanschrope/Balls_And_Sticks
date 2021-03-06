import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Connects Clients to session manager so they can be put in a game.
 */
public class BnsServer {

    /**
     * Main program.
     */
    public static void main(String[] args)
    {
        if (args.length != 2) usage();
        String host = args[0];
        int port = Integer.parseInt (args[1]);

        ServerSocket serversocket = null; //should not be used when null
        try {
            serversocket = new ServerSocket();
            serversocket.bind(new InetSocketAddress(host, port));

        SessionManager manager = new SessionManager();

        for (;;)
        {
            Socket socket = serversocket.accept();
            ViewProxy proxy = new ViewProxy (socket);
            proxy.setViewListener (manager);
        }
        }catch(IOException e){
            System.out.println("Socket error");
            System.exit(1);
        }
    }

    /**
     * Print a usage message and exit.
     */
    private static void usage()
    {
        System.err.println ("Usage: java BnsServer <host> <port>");
        System.exit (1);
    }

}

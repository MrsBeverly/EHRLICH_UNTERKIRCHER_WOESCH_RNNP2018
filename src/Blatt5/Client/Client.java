package Blatt5.Client;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;


public class Client {

    private Socket socket;

    private boolean debug = false;

    private BufferedReader reader;
    private BufferedWriter writer;

    private static final int DEFAULT_PORT = 110;




    public void connect(String host, int port) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        if (debug)
            System.out.println("Connected to the host");
    }





//End Class
}


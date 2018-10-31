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



    public void connect(String host) throws IOException {
        connect(host, DEFAULT_PORT);
    }



    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }


    public void disconnect() throws IOException {
        if (!isConnected())
            throw new IllegalStateException("Not connected to a host");
        socket.close();
        reader = null;
        writer = null;
        if (debug)
            System.out.println("Disconnected from the host");
    }




    protected String readResponseLine() throws IOException{
        String response = reader.readLine();
        if (debug) {
            System.out.println("DEBUG [in] : " + response);
        }
        if (response.startsWith("-ERR"))
            throw new RuntimeException("Server has returned an error: " + response.replaceFirst("-ERR ", ""));
        return response;
    }





    protected String sendCommand(String command) throws IOException {
        if (debug) {
            System.out.println("DEBUG [out]: " + command);
        }
        writer.write(command + "\n");
        writer.flush();
        return readResponseLine();
    }




    public void connect(String host, int port) throws IOException {
//…
        readResponseLine();
    }








//End Class
}


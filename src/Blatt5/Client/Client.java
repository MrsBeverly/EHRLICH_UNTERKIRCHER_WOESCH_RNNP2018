package Blatt5.Client;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Client {

    private Socket socket;

    private boolean debug = true;

    private BufferedReader reader;
    private BufferedWriter writer;

    private static final int DEFAULT_PORT = 6789;




    public void connect(String host, int port) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        readResponseLine();

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




    public void login(String username, String password) throws IOException {
        sendCommand("USER " + username);
        sendCommand("PASS " + password);
    }

    public void logout() throws IOException {
        sendCommand("QUIT");
    }





    public int getNumberOfNewMessages() throws IOException {
        String response = sendCommand("STAT");
        String[] values = response.split(" ");
        return Integer.parseInt(values[1]);
    }





    protected Message getMessage(int i) throws IOException {
        String response = sendCommand("RETR " + i);
        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        String headerName = null;
// process headers
        while ((response = readResponseLine()).length() != 0) {
            if (response.startsWith("\t")) {
                continue; //no process of multiline headers
            }
            int colonPosition = response.indexOf(":");
            headerName = response.substring(0, colonPosition);
            String headerValue;
            if (headerName.length() > colonPosition) {
                headerValue = response.substring(colonPosition + 2);
            } else {
                headerValue = "";
            }
            List<String> headerValues = headers.get(headerName);
            if (headerValues == null) {
                headerValues = new ArrayList<String>();
                headers.put(headerName, headerValues);
            }
            headerValues.add(headerValue);
        }
// process body
        StringBuilder bodyBuilder = new StringBuilder();
        while (!(response = readResponseLine()).equals(".")) {
            bodyBuilder.append(response + "\n");
        }
        return new Message(headers, bodyBuilder.toString());
    }

// get all messages from the Mailbox. returns a List of messages.
    public List<Message> getMessages() throws IOException {

        // get number of messages in mailbox
        int numOfMessages = getNumberOfNewMessages();

        // create ArrayList and add all messages
        List<Message> messageList = new ArrayList<Message>();
        for (int i = 0; i < numOfMessages; i++) {
            messageList.add(getMessage(i + 1));
        }

        return messageList;
    }



    public static void main(String[] args) throws IOException {
        Client myClient = new Client();
        //myClient.setDebug(true);
        //myClient.connect("pop.a1.net");
        myClient.connect("localhost");
        myClient.login("beverly-ehrlich@aon.at", "Beverly1701Chris");
        System.out.println("Number of new emails: " + myClient.getNumberOfNewMessages());
        List<Message> messages = myClient.getMessages();
        for (int index = 0; index < messages.size(); index++) {
            System.out.println("--- Message num. " + index + " ---");
            System.out.println(messages.get(index).getBody());
        }
        myClient.logout();
        myClient.disconnect();
    }

//End Class
}
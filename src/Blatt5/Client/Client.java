/*WRITTEN BY EHRLICH BEVERLY, UNTERKIRCHER CHRISTOPH AND WÖSCH TIMON*/
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
    private static final int DEFAULT_PORT = 110;

    //Connects the Client to the Server using Host-address and port.
    // Also generates the readers needed later.
    public void connect(String host, int port) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        readResponseLine();

        if (debug)
            System.out.println("Connected");
    }

    //connects using the DEFAULT-PORT
    public void connect(String host) throws IOException {
        connect(host, DEFAULT_PORT);
    }

    //checks the connection
    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    //Cuts Connection and resets the reader to null
    public void disconnect() throws IOException {
        if (!isConnected())
            throw new IllegalStateException("No connection found.");
        socket.close();
        reader = null;
        writer = null;
        if (debug)
            System.out.println("Disconnected successfully");
    }

    //Checks if the next item in the buffer is an error,
    // if no does nothing,
    // if yes handles the error.
    protected String readResponseLine() throws IOException{
        String response = reader.readLine();
        if (debug) {
            System.out.println("DEBUG [in] : " + response);
        }
        if (response.startsWith("-ERR"))
            throw new RuntimeException("Server has returned an error: " + response.replaceFirst("-ERR ", ""));
        return response;
    }

    //sends the command which is given as a parametrer and checks the response.
    protected String sendCommand(String command) throws IOException {
        if (debug) {
            System.out.println("DEBUG [out]: " + command);
        }
        writer.write(command + "\r\n");
        writer.flush();
        return readResponseLine();
    }

    //sends username and password
    public void login(String username, String password) throws IOException {
        sendCommand("USER " + username);
        sendCommand("PASS " + password);
    }

    //sends command to cut connection
    public void logout() throws IOException {
        sendCommand("QUIT");
    }

    //returns number of messages by cutting the information oud of response of the STAT command
    public int getNumberOfNewMessages() throws IOException {
        String response = sendCommand("STAT");
        String[] values = response.split(" ");
        return Integer.parseInt(values[1]);
    }

    //reads headers and payload and builds the message.
    protected Message getMessage(int i) throws IOException {
        String response = sendCommand("RETR " + i);
        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        String headerName = null;

        // read header data
        while ((response = readResponseLine()).length() != 0) {
            if (response.startsWith("\t")) {
                continue; //no process of multiline headers
            }
           if(response.indexOf(":")==-1){continue;}
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

        // read payload data
        StringBuilder bodyBuilder = new StringBuilder();
        while (!(response = readResponseLine()).equals(".")) {
            bodyBuilder.append(response + "\n ");
        }
        return new Message(headers, bodyBuilder.toString());
    }

// runs getMessage() method for each message.
    public List<Message> getMessages() throws IOException {

        // get number of messages in mailbox
        int numOfMessages = getNumberOfNewMessages();

        // create ArrayList and add all messages
        List<Message> messageList = new ArrayList<Message>();
        for (int i = 1; i <= numOfMessages; i++) {
            messageList.add(getMessage(i));
        }

        return messageList;
    }

    public static void main(String[] args) throws IOException {
        Client myClient = new Client();
        //myClient.connect("pop.a1.net");
        myClient.connect("localhost",6789);
        myClient.login("beverly-ehrlich@aon.at", "Beverly1701Chris");
        System.out.println("Number of Mails: " + myClient.getNumberOfNewMessages());
        List<Message> messages = myClient.getMessages();
        for (int index = 0; index < messages.size(); index++) {
            System.out.println("--- Message num. " + index + " ---");
            System.out.println(messages.get(index).getBody());
        }

        myClient.list();
        myClient.list(2);
        try {
            myClient.list(80);
        }catch(Exception e){
        }

        myClient.logout();
        myClient.disconnect();
    }

    public String list(int idx) throws IOException {
        return sendCommand("LIST " + idx);
    }

    public String[] list() throws IOException {
        String str = sendCommand("LIST");
        String[] list = new String[str.split( "").length];
        int numberOfMessages = Integer.valueOf(str.split(" ")[1]);
        for(int i=0; i<numberOfMessages; i++)
        {
            list[i] = readResponseLine();
        }
        readResponseLine();
        return list;
    }

//End Class
}
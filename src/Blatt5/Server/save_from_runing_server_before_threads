/*WRITTEN BY EHRLICH BEVERLY, UNTERKIRCHER CHRISTOPH AND WÖSCH TIMON*/
package Blatt5.Server;

import Blatt5.SampleDataBase;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private final static String positiveResponse = "+OK ";
    private final static String negativeResponse = "-ERR ";
    private final static String endOfResponse = "\r";
    private final static String terminationCharacter = ".";

    public void run(Socket connectionSocket) {

    }

    public static void main(String argv[]) throws Exception {
        String clientSentence;
        Server server = new Server();

        //new server with port 6789
        ServerSocket welcomeSocket = new ServerSocket(6789);

        while (true) {
            //waiting for a new client
            Socket connectionSocket = welcomeSocket.accept();

            //open input and output stream
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            //notify client that the connection has succeeded
            server.sendPositiveResponse(outToClient, "Connected");

            while (true) {

                //reading what the client has to say
                clientSentence = inFromClient.readLine();

                // getting the Command from the clientSentence
                switch (clientSentence.split(" ")[0]) {
                    case "USER":
                        //checking if user is valid (not implemented)
                        server.sendPositiveResponse(outToClient, "USER Accepted");
                        break;
                    case "PASS":
                        //checking if the password for the user is valid (not implemented)
                        server.sendPositiveResponse(outToClient, "PASS Accepted");
                        break;
                    case "STAT":
                        //returning number of messages and the size in Bits (???)
                        server.sendPositiveResponse(outToClient, SampleDataBase.messages.size() + " 51197");
                        break;
                    case "RETR":
                        //returning a specific message
                        server.sendPositiveResponse(outToClient, SampleDataBase.messages.get(Integer.valueOf(clientSentence.split(" ")[1]) - 1));
                        //termination of the Response
                        server.sendTerminationCharacter(outToClient);
                        break;
                    case "QUIT":
                        //goto UPDATE STATE
                        // TERMINATE
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void sendPositiveResponse(DataOutputStream outToClient, String response) throws Exception {
        outToClient.writeBytes(positiveResponse + response + endOfResponse);
        outToClient.flush();
    }

    public void sendNegativeResponse(DataOutputStream outToClient, String response) throws Exception {
        outToClient.writeBytes(negativeResponse + response + endOfResponse);
        outToClient.flush();
    }

    public void sendTerminationCharacter(DataOutputStream outToClient) throws Exception {
        outToClient.writeBytes(terminationCharacter + endOfResponse);
        outToClient.flush();
    }
}

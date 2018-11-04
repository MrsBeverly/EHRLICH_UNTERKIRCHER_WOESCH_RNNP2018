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
    private final Socket connectionSocket;

    public Server(Socket connection_Socket){
        connectionSocket = connection_Socket;
    }

    public void run(){
        String clientSentence;
        boolean flag = false;

        try {
            //open input and output stream
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            //notify client that the connection has succeeded
            sendPositiveResponse(outToClient, "Connected");

            while(!flag){
                clientSentence = inFromClient.readLine();
                //checking if user is valid (not implemented)
                sendPositiveResponse(outToClient, "USER Accepted");
                //sendNegativeResponse(outToClient, "USER does not exist");
                flag=true;
            }

            flag=false;

            while(!flag){
                clientSentence = inFromClient.readLine();
                //checking if the password for the user is valid (not implemented)
                sendPositiveResponse(outToClient, "PASS Accepted");
                //sendNegativeResponse(outToClient, "PASS is not correct");
                flag=true;
            }

            flag=false;

            //Transaction State
            while (!flag){
                //reading what the client has to say
                clientSentence = inFromClient.readLine();

                // getting the Command from the clientSentence
                switch (clientSentence.split(" ")[0]) {
                    case "STAT":
                        //returning number of messages and the size in Bits (???)
                        sendPositiveResponse(outToClient, SampleDataBase.messages.size() + " 51197");
                        break;
                    case "RETR":
                        //returning a specific message
                        try {
                            sendPositiveResponse(outToClient, SampleDataBase.messages.get(Integer.valueOf(clientSentence.split(" ")[1]) - 1));
                            //termination of the Response
                            sendTerminationCharacter(outToClient);
                        }catch(Exception e){
                            //message not found
                            sendNegativeResponse(outToClient,"no such message");
                        }
                        break;
                    case "QUIT":
                        //goto UPDATE STATE
                        // TERMINATE
                        flag=true;
                        break;
                    default:
                        break;
                }
            }
        }catch (Exception e){
            System.out.println("Exception = " + e);
        }
    }

    public static void main(String argv[]) throws Exception {
        Server server;

        //new server with port 6789
        ServerSocket welcomeSocket = new ServerSocket(6789);

        while (true) {
            //waiting for a new client
            Socket connectionSocket = welcomeSocket.accept();
            //start new Thread and give him the connectionSocket
            server = new Server(connectionSocket);
            server.start();
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

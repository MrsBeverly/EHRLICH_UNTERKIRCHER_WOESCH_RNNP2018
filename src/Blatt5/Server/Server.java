package Blatt5.Server;

import Blatt5.SampleDataBase;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final String positiveResponse = "+OK ";
    private final String negativeResponse = "-ERR ";
    private final String endOfResponse = "\r";
    private final String terminationCharacter = ".";

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
            outToClient.writeBytes(server.sendPositiveResponse(
                    "Connected"
            ));
            outToClient.flush();

            while (true) {

                //reading what the client has to say
                clientSentence = inFromClient.readLine();

                // getting the Command from the clientSentence
                switch (clientSentence.split(" ")[0])
                {
                    case "USER":
                        //checking if user is valid (not implemented)
                        outToClient.writeBytes(server.sendPositiveResponse(
                                "USER Accepted"
                        ));
                        outToClient.flush();
                        break;
                    case "PASS":
                        //checking if the password for the user is valid (not implemented)
                        outToClient.writeBytes(server.sendPositiveResponse(
                                "PASS Accepted"
                        ));
                        outToClient.flush();
                        break;
                    case "STAT":
                        //returning number of messages and the size in Bits (???)
                        outToClient.writeBytes(
                                // example : +OK 6 51197
                                server.sendPositiveResponse(SampleDataBase.messages.size() + " 51197"
                                ));
                        outToClient.flush();
                        break;
                    case "RETR":
                        //returning a specific message
                        outToClient.writeBytes(server.sendPositiveResponse(
                                //returning the data of a message
                                SampleDataBase.messages.get(Integer.valueOf(clientSentence.split(" ")[1])-1
                                )));
                        //termination of the Response
                        outToClient.writeBytes(
                                server.sendTerminationCharacter()
                        );
                        outToClient.flush();
                        break;
                    case "QUIT":
                        //goto UPDATE STATE
                        // TERMINATE
                        break;
                    default: break;
                }
            }
        }
    }

    protected String sendPositiveResponse(String response)
    {
        return positiveResponse+response+endOfResponse;
    }

    protected String sendNegativeResponse(String response)
    {
        return negativeResponse+response+endOfResponse;
    }

    protected String sendTerminationCharacter()
    {
        return terminationCharacter +endOfResponse;
    }
}

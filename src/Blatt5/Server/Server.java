package Blatt5.Server;

import Blatt5.SampleDataBase;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String argv[]) throws Exception {
        String clientSentence;

        //new server with port 6789
        ServerSocket welcomeSocket = new ServerSocket(6789);

        while (true) {
            //waiting for a new client
            Socket connectionSocket = welcomeSocket.accept();

            //open input and output stream
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            //notify client that the connection has succeeded
            outToClient.writeBytes("+OK Connected\r");
            outToClient.flush();

            while (true) {

                //reading what the client has to say
                clientSentence = inFromClient.readLine();

                // getting the Command from the clientSentence
                switch (clientSentence.split(" ")[0])
                {
                    case "USER":
                        //checking if user is valid (not implemented)
                        outToClient.writeBytes("+OK USER Accepted\r");
                        outToClient.flush();
                        break;
                    case "PASS":
                        //checking if the password for the user is valid (not implemented)
                        outToClient.writeBytes("+OK PASS Accepted\r");
                        outToClient.flush();
                        break;
                    case "STAT":
                        //returning number of messages (and the size in Bits)
                        outToClient.writeBytes("+OK " + SampleDataBase.messages.size() + " 51197\r");
                        outToClient.flush();
                        break;
                    case "RETR":
                        //returning a specific massage
                        outToClient.writeBytes("+OK "+ SampleDataBase.messages.get(Integer.valueOf(clientSentence.split(" ")[1])-1) +"\r");
                        outToClient.writeBytes(".\r");
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

}

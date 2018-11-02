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
        String capitalizedSentence;

        ServerSocket welcomeSocket = new ServerSocket(6789);
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            outToClient.writeBytes("+OK Connected\r");
            outToClient.flush();

            while (true) {

                clientSentence = inFromClient.readLine();

                switch (clientSentence.split(" ")[0])
                {
                    case "USER":
                        outToClient.writeBytes("+OK USER Accepted\r");
                        outToClient.flush();
                        break;
                    case "PASS":
                        outToClient.writeBytes("+OK PASS Accepted\r");
                        outToClient.flush();
                        break;
                    case "STAT":
                        outToClient.writeBytes("+OK " + SampleDataBase.messages.size() + " 51197\r");
                        outToClient.flush();
                        break;
                    case "RETR":
                        outToClient.writeBytes("+OK "+ SampleDataBase.messages.get(Integer.valueOf(clientSentence.split(" ")[1])-1) +"\r");
                        outToClient.writeBytes(".\r");
                        outToClient.flush();
                        break;
                    default: break;
                }
            }
        }
    }

}

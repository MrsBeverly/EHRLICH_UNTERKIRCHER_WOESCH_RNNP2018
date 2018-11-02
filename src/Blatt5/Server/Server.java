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

            outToClient.writeChars("+OK Connected\r");
            outToClient.flush();

            while (true) {

                clientSentence = inFromClient.readLine();

                switch (clientSentence.split(" ")[0])
                {
                    case "USER":
                        outToClient.writeChars("+OK USER Accepted\r");
                        outToClient.flush();
                        break;
                    case "PASS":
                        outToClient.writeChars("+OK PASS Accepted\r");
                        outToClient.flush();
                        break;
                    case "STAT":
                        outToClient.writeChars("+OK ");
                        outToClient.write(6);
                        outToClient.writeChars(" ");
                        outToClient.write(51197);
                        outToClient.writeChars("\r");
                        outToClient.flush();
                        break;
                    default: break;
                }
            }
        }
    }

}

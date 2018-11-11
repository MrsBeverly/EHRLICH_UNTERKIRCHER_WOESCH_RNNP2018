/*WRITTEN BY EHRLICH BEVERLY, UNTERKIRCHER CHRISTOPH AND WÖSCH TIMON*/
package Blatt6;

import Blatt5.Server.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer_single {
    private static Boolean debug = true;
    public static void main(String[] args) throws Exception {
        Server server;
        ServerSocket welcomeSocket = new ServerSocket(8000);

        while (true) {
            //waiting for a new client
            Socket connectionSocket = welcomeSocket.accept();

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());


            System.out.println("server = " + inFromClient.readLine());

            File file = new File("D:\\Cloud\\OneDrive - Alpen-Adria Universität Klagenfurt\\UNI_Beverly\\7. Semester(WS18)\\Rechnernetze- und Netzwerkprogrammierung\\PR_RNNP\\RNNP_UB6\\documentRoot\\index.html");
            BufferedReader br = new BufferedReader(new FileReader(file));

            while(true) {
                String string = br.readLine();
                if(string==null) {
                    System.exit(1);
                }

                if(debug) System.out.println("br.readLine() = " +string);

                String stringTest = "HTTP/0.9 200 OK\r\n\r\n" + string;
                //outToClient.writeBytes(string);
                connectionSocket.getOutputStream().write(stringTest.getBytes("UTF-8"));
            }
        }
    }
}

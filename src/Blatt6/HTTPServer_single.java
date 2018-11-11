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
        ServerSocket welcomeSocket = new ServerSocket(6789);

        while (true) {
            //waiting for a new client
            Socket connectionSocket = welcomeSocket.accept();

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());


            System.out.println("server = " + inFromClient.readLine());

            File file = new File("C:\\Users\\Timon\\Desktop\\Uni\\S7\\Rechnernetze und Netzwerktechnik\\UE\\EHRLICH_UNTERKIRCHER_WOESCH_RNNP2018\\src\\Blatt6\\documentRoot\\index.html");
            BufferedReader br = new BufferedReader(new FileReader(file));

            while(true) {
                String string = br.readLine();
                if(string==null) {
                    System.exit(1);
                }

                if(debug) System.out.println("br.readLine() = " +string);

                outToClient.writeBytes(string);
            }
        }
    }
}

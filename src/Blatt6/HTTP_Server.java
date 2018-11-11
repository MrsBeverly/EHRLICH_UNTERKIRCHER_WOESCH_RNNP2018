package Blatt6;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class HTTP_Server {

    public final  Socket connectionSocket;
    public HTTP_Server(Socket connection_Socket) {
        connectionSocket = connection_Socket;
    }
    public static void main(String[] args) throws IOException {

        HTTP_Server myserver;
        ServerSocket mySSocket =new ServerSocket(1234);


    }
}

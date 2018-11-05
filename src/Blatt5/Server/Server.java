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

    public Server(Socket connection_Socket) {
        connectionSocket = connection_Socket;
    }

    public void run() {
        String clientSentence;
        String[] split_clientSentence;
        boolean flag = false;

        try {
            //open input and output stream
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            //notify client that the connection has succeeded
            sendPositiveResponse(outToClient, "Connected");

            //Authorization State
            authorizationState(inFromClient, outToClient, null);

            //Transaction State
            transactionState(inFromClient, outToClient, null);

        } catch (Exception e) {
            System.out.println("Exception = " + e);
        }
    }

    private void transactionState(BufferedReader inFromClient, DataOutputStream outToClient, String inClientSentence) throws Exception {
        String clientSentence;
        String[] split_clientSentence;
        Boolean flag = false;
        int idx, count;

        // Transaction State actions
        while (!flag) {
            //reading what the client has to say
            clientSentence = inFromClient.readLine();
            split_clientSentence=clientSentence.split(" ");

            switch (split_clientSentence[0]) {
                case "STAT":
                    //returning number of messages and the size in Bits (???)
                    sendPositiveResponse(outToClient, SampleDataBase.messages.size() + " size?");

                    break;
                case "RETR":
                    //returning a specific message
                    idx = Integer.valueOf(split_clientSentence[1]);
                    if(idx<=SampleDataBase.messages.size()){
                        sendPositiveResponse(outToClient, "size?");
                        sendResponse(outToClient, SampleDataBase.messages.get(idx - 1));
                        sendTerminationCharacter(outToClient);
                    } else{
                        sendNegativeResponse(outToClient, "no such message");
                    }

                    break;
                case "LIST":
                    //listing messages
                    if(split_clientSentence.length==1){
                        //list all
                        sendPositiveResponse(outToClient, SampleDataBase.messages.size() + " messages " + "size?");
                        for(int i=0; i<SampleDataBase.messages.size(); i++){
                            sendResponse(outToClient, i+1 + " " + "size?");
                        }
                        sendTerminationCharacter(outToClient);
                    }else{
                        //list specific
                        idx = Integer.valueOf(split_clientSentence[1]);
                        if(idx<SampleDataBase.messages.size()) {
                            sendPositiveResponse(outToClient, idx + " " + "size?");
                        }else{
                            sendNegativeResponse(outToClient, "no such message, only " + SampleDataBase.messages.size() + " messages in maildrop");
                        }
                    }

                    break;
                case "TOP":
                    //listing a specific starting symbols of a message
                    idx = Integer.valueOf(split_clientSentence[1]);
                    count = Integer.valueOf(split_clientSentence[2]);

                    idx = Integer.valueOf(split_clientSentence[1]);
                    if(idx<SampleDataBase.messages.size()) {
                        sendPositiveResponse(outToClient, "");
                        sendResponse(outToClient, SampleDataBase.messages.get(Integer.valueOf(idx)).substring(0,count-1));
                        sendTerminationCharacter(outToClient);
                    }else{
                        sendNegativeResponse(outToClient, "no such message, only " + SampleDataBase.messages.size() + " messages in maildrop");
                    }

                    break;
                case "QUIT":
                    //Update STATE
                    updateState(inFromClient, outToClient, clientSentence);
                    flag = true;

                    break;
                case "DELE":
                //removing a specific message
                idx = Integer.valueOf(split_clientSentence[1]);
                if(idx<=SampleDataBase.messages.size()){
                    //SampleDataBase.messages.remove(idx);
                    sendPositiveResponse(outToClient, "message deleted");
                } else{
                    sendNegativeResponse(outToClient, "no such message");
                }

                break;
                default:

                    break;
            }//end switch
        }//end while
    }

    private void updateState(BufferedReader inFromClient, DataOutputStream outToClient, String inClientSentence) throws Exception {
        String clientSentence;
        String[] split_clientSentence;
        Boolean flag = false;

        //if there is a sentence from the State before
        if(inClientSentence!=null) {
            clientSentence=inClientSentence;
            split_clientSentence=clientSentence.split(" ");
            switch (split_clientSentence[0]) {
                case "QUIT":
                    sendPositiveResponse(outToClient, "See you later :)");
                    flag=true;
                    break;
                default:
                    break;
            }
        }

        //Update State actions
        while (!flag){
            clientSentence=inClientSentence;
            split_clientSentence=clientSentence.split(" ");
            switch (split_clientSentence[0]) {
                default:
                    break;
            }
            //exit
            flag=true;
        }
    }

    private void authorizationState(BufferedReader inFromClient, DataOutputStream outToClient, String inClientSentence) throws Exception {
        String clientSentence;
        Boolean flag = false;

        //checking USER
        while (!flag) {
            clientSentence = inFromClient.readLine();
            //checking if user is valid (not implemented)
            sendPositiveResponse(outToClient, "USER Accepted");
            //sendNegativeResponse(outToClient, "USER does not exist");
            flag = true;
        }

        flag = false;
        //checking PASS
        while (!flag) {
            clientSentence = inFromClient.readLine();
            //checking if the password for the user is valid (not implemented)
            sendPositiveResponse(outToClient, "PASS Accepted");
            //sendNegativeResponse(outToClient, "PASS is not correct");
            flag = true;
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

    public void sendResponse(DataOutputStream outToClient, String response) throws Exception {
        outToClient.writeBytes( response + endOfResponse);
        outToClient.flush();
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
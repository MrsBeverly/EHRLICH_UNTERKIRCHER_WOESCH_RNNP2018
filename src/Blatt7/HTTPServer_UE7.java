/*WRITTEN BY EHRLICH BEVERLY, UNTERKIRCHER CHRISTOPH AND WÖSCH TIMON*/
package Blatt7;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class HTTPServer_UE7 extends Thread{
    private static Boolean debug = true;
    private static String path2DocumentRoot ="src/Blatt7/documentRoot/wwwroot";
    private Socket socket;

    public HTTPServer_UE7(Socket socket_in){
        socket=socket_in;
    }

    public void run() {
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
            String in;
            StringTokenizer in_tokens;

            in = inFromClient.readLine();
            if (debug) System.out.println("[DEBUG] "+ socket +" in  = " + in);

            in_tokens = new StringTokenizer(in);

            //checking if there are 3 Tokens
            if(in_tokens.countTokens()!=3){
                socket.close();
                System.exit(-1);
            }

            switch (in_tokens.nextToken()) {
                case "GET":
                    // GET actions
                    GETActions(outToClient, in_tokens.nextToken());
                    break;
                case "POST":
                    // POST actions
                    POSTActions(outToClient,in_tokens);
                    break;
                default:
                    break;
            }
            socket.close();
        }catch (Exception e){
            //Exception
        }
    }

    private void POSTActions(DataOutputStream outToClient, StringTokenizer in_tokens){
        // TODO POST Actions
        if(debug) System.out.println("[DEBUG] "+socket+" in: "+in_tokens.nextToken());

    }

    private void GETActions(DataOutputStream outToClient, String s) throws IOException {
        if (s.equals("/")) {
            getHTMLFile(outToClient,path2DocumentRoot+"/index.html");
        } else if (s.endsWith(".png")) {
            getFile(path2DocumentRoot + s, "gif", outToClient);
        } else if (s.endsWith(".gif")) {
            getFile(path2DocumentRoot + s, "png", outToClient);
        } else if (s.endsWith(".jpg")) {
            getFile(path2DocumentRoot + s, "jpg", outToClient);
        } else if (s.endsWith(".html")){
            getHTMLFile(outToClient,path2DocumentRoot+s);
        }
    }

    private void getHTMLFile(DataOutputStream outToClient,String path2Get) throws IOException {
        File file;
        BufferedReader br;
        file = new File(path2Get);
        br = new BufferedReader(new FileReader(file));
        Boolean flag = false;

        //info:  readline frisst crlf auf

        //sobald man diese "OK" message schickt, erwartet sich der browser einen header
        //das ist weil kein browser mehr mit 0.9 läuft

        //Status response
        outToClient.write("HTTP/1.1 200 OK\r\n".getBytes("UTF-8"));
        //header
        outToClient.write("\r\n".getBytes("UTF-8"));

        while (!flag) {
            String string = br.readLine();
            if (debug) System.out.println("[DEBUG] " + socket + " out = " + string);

            if (string == null) {
                flag = true;
            } else {
                outToClient.write(string.getBytes("UTF-8"));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //checking args
        if(args.length!=2){
            System.out.println("Port and Document root necessary");
            System.exit(-1);
        }
        if(debug) System.out.println("[DEBUG] MAIN: Port = " + args[0] + "\n[DEBUG] MAIN: Document root = " + args[1]);

        //setting up server
        HTTPServer_UE7 server;
        ServerSocket welcomeSocket = new ServerSocket(Integer.valueOf(args[0]));
        path2DocumentRoot = args[1];

        while (true) {
            //waiting for a new client
            Socket connectionSocket = welcomeSocket.accept();
            server= new HTTPServer_UE7(connectionSocket);
            if(debug) System.out.println("[DEBUG] MAIN: Thread with Socket = " + connectionSocket + " starting");
            server.start();
        }
    }

    public void getFile(String path, String fileType, DataOutputStream myOut) throws IOException {

        File input_File = new File(path);
        BufferedImage myImage = ImageIO.read(input_File);

        ByteArrayOutputStream myByteArrOutStr =new ByteArrayOutputStream();

        //kopiert direkt binär auf outputstream
        //file.copy(path, destinationStream) geht auch
        //destinationStream = myOuts
        ImageIO.write(myImage,fileType,myByteArrOutStr);

        //status
        myOut.write("HTTP/0.9 200 OK\r\n".getBytes("UTF-8"));
        //header
        myOut.write("\r\n".getBytes("UTF-8"));

        if(debug) System.out.println("[DEBUG] " + socket + " out: Returning File");

        myOut.write(myByteArrOutStr.toByteArray());
        myOut.flush();

    }
}

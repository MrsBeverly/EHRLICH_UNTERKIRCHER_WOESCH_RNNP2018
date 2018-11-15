/*WRITTEN BY EHRLICH BEVERLY, UNTERKIRCHER CHRISTOPH AND WÖSCH TIMON*/
package Blatt6;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer_UE7 extends Thread{
    private static Boolean debug = true;
    private static final String Path2DocumentRoot ="src/Blatt6/documentRoot";
    private Socket socket;

    public HTTPServer_UE7(Socket socket_in){
        socket=socket_in;
    }

    public void run() {
        try {
            //wichtig ist bufferedReader
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
            String in;
            String[] split_in;

            in = inFromClient.readLine();
            if (debug) System.out.println("[DEBUG] in  = " + in);

            //sollte man eher mit tokenizer machen
            //prüfen, ob alle 3 tokens auch vorhanden sind!!!
            //sonst fehler! hier prüfen
            split_in = in.split(" ");

            if (split_in[1].equals("/")) {//root pfad -> /index.html
                //print index.html
                getIndex(outToClient);

            } else if (split_in[1].endsWith(".png")) {
                getFile(Path2DocumentRoot + split_in[1], "gif", outToClient);

            } else if (split_in[1].endsWith(".gif")) {
                getFile(Path2DocumentRoot + split_in[1], "png", outToClient);

            } else if (split_in[1].endsWith(".jpg")) {
                getFile(Path2DocumentRoot + split_in[1], "jpg", outToClient);
            }

            socket.close();
        }catch (Exception e){
            //Exception
        }
    }

    private void getIndex(DataOutputStream outToClient) throws IOException {
        File file;
        BufferedReader br;
        //file = new File("C:\\Users\\Timon\\Desktop\\Uni\\S7\\Rechnernetze und Netzwerktechnik\\UE\\EHRLICH_UNTERKIRCHER_WOESCH_RNNP2018\\src\\Blatt6\\documentRoot\\index.html");
        file = new File(Path2DocumentRoot + "/index.html");
        br = new BufferedReader(new FileReader(file));
        Boolean flag = false;


        //info:  readline frisst crlf auf


        //sobald man diese "OK" message schickt, erwartet sich der browser einen header
        //das ist weil kein browser mehr mit 0.9 läuft
        outToClient.write("HTTP/0.9 200 OK\r\n\r\n".getBytes("UTF-8"));//zwei CLRF weil die headerfiles nicht da sind; bei höherer Implementierung kommt hier die headerfile

        while (!flag) {
            String string = br.readLine();
            if (debug) System.out.println("[DEBUG] out = " + string);

            if (string == null) {
                flag = true;
            } else {
                outToClient.write(string.getBytes("UTF-8"));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        HTTPServer_UE7 server;
        ServerSocket welcomeSocket = new ServerSocket(8800);

        //von commandline anlesen
        //Path2DocumentRoot = args[0];
        //port = args[1];

        while (true) {
            //waiting for a new client
            Socket connectionSocket = welcomeSocket.accept();
            server= new HTTPServer_UE7(connectionSocket);
            server.start();
        }
    }

    public void getFile(String path, String fileType, DataOutputStream myOut) throws IOException {

        File input_File = new File(path);
        BufferedImage myImage = ImageIO.read(input_File);

        ByteArrayOutputStream myByteArrOutStr =new ByteArrayOutputStream();

        //kopiert direkt binär auf outputstream
        //file.copy(path, destinationStream) geht auch
        //destinationStream = myOut
        ImageIO.write(myImage,fileType,myByteArrOutStr);

        myOut.write("HTTP/0.9 200 OK\r\n\r\n".getBytes("UTF-8"));


        myOut.write(myByteArrOutStr.toByteArray());
        myOut.flush();

    }
}

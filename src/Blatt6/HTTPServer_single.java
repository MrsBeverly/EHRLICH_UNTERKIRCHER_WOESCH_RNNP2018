/*WRITTEN BY EHRLICH BEVERLY, UNTERKIRCHER CHRISTOPH AND WÖSCH TIMON*/
package Blatt6;

import Blatt5.Server.Server;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer_single {
    private static Boolean debug = true;
    private static final String Img1GIF="C:\\Daten\\GoogleDrive\\Uni_Chris\\9.Semester\\Rechnernetze\\EHRLICH_UNTERKIRCHER_WOESCH_RNNP2018\\src\\Blatt6\\documentRoot\\images\\logo.gif";
    private  static final String Img2PNG="C:\\Daten\\GoogleDrive\\Uni_Chris\\9.Semester\\Rechnernetze\\EHRLICH_UNTERKIRCHER_WOESCH_RNNP2018\\src\\Blatt6\\documentRoot\\images\\TechnikErleben.png";
    public static void main(String[] args) throws Exception {
        Server server;
        ServerSocket welcomeSocket = new ServerSocket(8800);



        while (true) {
            //waiting for a new client
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            String in;
            File file;
            BufferedReader br;
            Boolean flag=false;
            String[] split_in;

            HTTPServer_single myHTTPServer = new HTTPServer_single();


            in = inFromClient.readLine();
            if(debug) System.out.println("[DEBUG] in  = " + in);

            split_in = in.split(" ");

            if(split_in[1].equals("/")){

                file = new File("C:\\Users\\Timon\\Desktop\\Uni\\S7\\Rechnernetze und Netzwerktechnik\\UE\\EHRLICH_UNTERKIRCHER_WOESCH_RNNP2018\\src\\Blatt6\\documentRoot\\index.html");
                br = new BufferedReader(new FileReader(file));

                outToClient.write("HTTP/0.9 200 OK\r\n\r\n".getBytes("UTF-8"));

                while (!flag) {
                    String string = br.readLine();
                    if (debug) System.out.println("[DEBUG] out = " + string);

                    if (string == null) {
                        flag=true;
                    }else {
                        outToClient.write(string.getBytes("UTF-8"));
                    }
                }
            } else if(split_in[1].endsWith(".png")){


                myHTTPServer.sendNudes(Img1GIF,"gif",outToClient);
            }else if(split_in[1].endsWith(".gif")){
                myHTTPServer.sendNudes(Img2PNG,"png",outToClient);

            }
        }
    }


    public void sendNudes(String path,String fileType, DataOutputStream myOut) throws IOException {

        File input_File = new File(path);
        BufferedImage myImage = ImageIO.read(input_File);

        ByteArrayOutputStream myByteArrOutStr =new ByteArrayOutputStream();

        ImageIO.write(myImage,fileType,myByteArrOutStr);

        myOut.write(myByteArrOutStr.toByteArray());
        myOut.flush();

    }
}

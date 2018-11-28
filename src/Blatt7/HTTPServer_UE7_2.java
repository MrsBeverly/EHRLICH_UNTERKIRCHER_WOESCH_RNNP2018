/*WRITTEN BY EHRLICH BEVERLY, UNTERKIRCHER CHRISTOPH AND WÖSCH TIMON*/
package Blatt7;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class HTTPServer_UE7_2 extends Thread {
    private static ResponseMessages myRespMsgs = new ResponseMessages();
    private static Boolean debug = true;
    private static String path2DocumentRoot = "src/Blatt7/documentRoot/wwwroot";
    private Socket socket;
    public static String serverVersion = "HTTP/1.1";
    private boolean flagStayAlive=true;
    public HTTPServer_UE7_2(Socket socket_in) {
        socket = socket_in;
    }

    public void run() {

        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());

            try {

                String in;
                StringTokenizer in_tokens;



                //repears until flag is thrown, then stops thread
                while (flagStayAlive) {
                in = inFromClient.readLine();
                if (debug) System.out.println("[DEBUG] " + socket.getPort() + " in  = " + in);

                    in_tokens = new StringTokenizer(in);

                    //checking if there are 3 Tokens
                    if (in_tokens.countTokens() != 3) {
                        outToClient.write((serverVersion + " " + myRespMsgs.badRequest400 + myRespMsgs.newLine).getBytes("UTF-8"));
                        flagStayAlive=false;
                        break;

                    }


                    switch (in_tokens.nextToken()) {
                        case "GET":
                            // GET actions
                            GETActions(outToClient,inFromClient ,in_tokens.nextToken());
                            break;
                        case "POST":
                            // POST actions
                            POSTActions(outToClient, inFromClient);
                            break;
                        default:
                            break;
                    }
                }




                socket.close();
            } catch (Exception e) {
                outToClient.write((serverVersion + " " + myRespMsgs.internalServerError500 + myRespMsgs.newLine).getBytes("UTF-8"));
            }
        } catch (Exception e) {
        }
    }



    private void GETActions(DataOutputStream outToClient, BufferedReader inFromClient,String s) throws IOException {

        /*
         * Manages and distinguishes different GET requests
         * */

        List<String> headers = getHeaders(inFromClient);
        setStayAlive(headers);

        if (s.equals("/")) {
            getHTMLFile(outToClient, path2DocumentRoot + "/index.html");
        } else if (s.endsWith(".png")) {
            getFile(path2DocumentRoot + s, "gif", outToClient);
        } else if (s.endsWith(".gif")) {
            getFile(path2DocumentRoot + s, "png", outToClient);
        } else if (s.endsWith(".jpg")) {
            getFile(path2DocumentRoot + s, "jpg", outToClient);
        } else if (s.endsWith("favicon.ico")) {
            getFile(path2DocumentRoot + "/favicon" + s, "ico", outToClient);
        } else if (s.endsWith(".html")) {
            getHTMLFile(outToClient, path2DocumentRoot + s);
        }
    }

    private void POSTActions(DataOutputStream outToClient, BufferedReader inFromClient) throws IOException {

        /*
         * reads posted information, filters content and returns it to a new html form.
         * */


        //get headers
        List<String> headers = getHeaders(inFromClient);

        //get Content Length
        //StringTokenizer splits string and puts it on stack (if read, stringpart is gone)
        StringTokenizer w_header;

        //sets flagStayAlive to false if necessary
        setStayAlive(headers);

        w_header = getOneHeader(headers, "Content-Length");
        //Error?
        if (!w_header.nextToken().equals("Content-Length:")) {
            //error
           flagStayAlive=false;
        }else {

            //get content
            int idx = Integer.valueOf(w_header.nextToken());
            char[] arr = new char[idx];
            //actual Content
            inFromClient.read(arr, 0, idx);
            if (debug) System.out.println("[DEBUG] " + socket.getPort() + " in  = " + String.valueOf(arr));


            //response message
            outToClient.write((serverVersion + " " + myRespMsgs.ok200 + myRespMsgs.newLine).getBytes("UTF-8"));



            /*message looks like this: txtField1=Mickey&txtField2=Mouse
            *squishes array arr into one string and splits it at &
            *The resulting Array looks like [txtField1=Mickey] [txtField2=Mouse]
            * Then we build the Answer as a String that consists of HTML statements for displaying,
            * the sentence we shall return and the field-name and content we split out of the array.
            **/

            String[] clientContent = String.valueOf(arr).split("&");
            String returnHtml = "<html><body> <p> Received form variable with name "
                    //splits at = to get actual values
                    + clientContent[0].split("=")[0]
                    + " and value "
                    + clientContent[0].split("=")[1]
                    + "</p> <p> Received form variable with name "
                    + clientContent[1].split("=")[0]
                    + " and value "
                    + clientContent[1].split("=")[1]
                    + "</p> </body></html>";
            outToClient.write(("Content-Length: " + returnHtml.getBytes("UTF-8").length + myRespMsgs.newLine).getBytes("UTF-8"));
            outToClient.write((myRespMsgs.contentTypeHTML +myRespMsgs.newLine).getBytes("UTF-8"));
            outToClient.write(myRespMsgs.newLine.getBytes("UTF-8"));
            outToClient.write(returnHtml.getBytes("UTF-8"));
        }
    }

    private StringTokenizer getOneHeader(List<String> headers, String want) {

        /*
        * Finds the header you searched for out of a list of headers.
        * */

        for (String i : headers) {
            if (i.startsWith(want)) {
                return new StringTokenizer(i);
            }
        }
        return null;
    }

    private List<String> getHeaders(BufferedReader inFromClient) throws IOException {

        /*
        * Reads all headers out og the message and writes them into a list.
        * */

        List<String> in = new LinkedList<String>();
        boolean flag = false;
        //read until ""
        while (!flag) {
            in.add(inFromClient.readLine());
            if (debug)
                System.out.println("[DEBUG] " + socket.getPort() + " in  = " + ((LinkedList<String>) in).getLast());
            if (((LinkedList<String>) in).getLast().equals("")) {
                flag = true;
            }
        }
        return in;
    }

    private void getHTMLFile(DataOutputStream outToClient, String path2Get) throws IOException {
        File file;
        BufferedReader br;
        try {
            file = new File(path2Get);
            br = new BufferedReader(new FileReader(file));


            Boolean flag = false;

            //info:  readline frisst crlf auf

            //sobald man diese "OK" message schickt, erwartet sich der browser einen header
            //das ist weil kein browser mehr mit 0.9 läuft

            //Status response
            outToClient.write((serverVersion + " " + myRespMsgs.ok200 + myRespMsgs.newLine).getBytes("UTF-8"));
            //header
            outToClient.write("\r\n".getBytes("UTF-8"));

            while (!flag) {
                String string = br.readLine();
                if (debug) System.out.println("[DEBUG] " + socket.getPort() + " out = " + string);

                if (string == null) {
                    flag = true;
                } else {
                    outToClient.write(string.getBytes("UTF-8"));
                }
            }

        } catch (Exception e) {
            //Status response
            outToClient.write((serverVersion + " " + myRespMsgs.notFound404 + myRespMsgs.newLine).getBytes("UTF-8"));
            //header - doesn't make any difference
            //outToClient.write("\r\n".getBytes("UTF-8"));
        }
    }

    public static void main(String[] args) throws Exception {
        //checking args
        if (args.length != 2) {
            System.out.println("Port and Document root necessary");
            System.exit(-1);
        }
        if (debug) System.out.println("[DEBUG] MAIN: Port = " + args[0] + "\n[DEBUG] MAIN: Document root = " + args[1]);

        //setting up server
        HTTPServer_UE7_2 server;
        ServerSocket welcomeSocket = new ServerSocket(Integer.valueOf(args[0]));
        path2DocumentRoot = args[1];

        while (true) {
            //waiting for a new client
            Socket connectionSocket = welcomeSocket.accept();
            server = new HTTPServer_UE7_2(connectionSocket);
            if (debug)
                System.out.println("[DEBUG] MAIN: Thread with Socket = " + connectionSocket.getPort() + " starting");
            server.start();
        }
    }

    public void getFile(String path, String fileType, DataOutputStream myOut) throws IOException {

        File input_File = new File(path);
        BufferedImage myImage = ImageIO.read(input_File);

        ByteArrayOutputStream myByteArrOutStr = new ByteArrayOutputStream();

        //kopiert direkt binär auf outputstream
        //file.copy(path, destinationStream) geht auch
        //destinationStream = myOuts
        ImageIO.write(myImage, fileType, myByteArrOutStr);

        //status
        myOut.write((serverVersion + " " + myRespMsgs.ok200 + myRespMsgs.newLine).getBytes("UTF-8"));
        //header
        myOut.write(myRespMsgs.newLine.getBytes("UTF-8"));

        if (debug) System.out.println("[DEBUG] " + socket + " out: Returning File");

        myOut.write(myByteArrOutStr.toByteArray());
        myOut.flush();

    }

    private void setStayAlive(List<String> headers){
        /*
         *This function is used to check if the message we received contains another connection token then "stay-alive".
         *If yes, it sets the flag: stayAlive to false. The rest stays as it is.
         * The result is that the current request is progressed as usual, but the thread doesn't wait for another request.
         * The Thread teminates.
          */

        StringTokenizer strTok=getOneHeader(headers,"Connection:");
        strTok.nextToken();
        String connectionToken=String.valueOf(strTok.nextToken());
        if(debug){System.out.println("[DEBUG] Connection Token read: "+connectionToken);}

        if(!connectionToken.equals("keep-alive")){
            flagStayAlive=false;
        }






    }
}

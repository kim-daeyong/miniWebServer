package my.examples.miniwebserver;

import javax.xml.ws.spi.http.HttpHandler;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;

public class WebServer {
    private int port;


    public WebServer(int port){
        this.port = port;
    }

    public void run(){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while(true){
                System.out.println("접속 대기중입니다.");
                Socket socket = serverSocket.accept();
                HttpHandle httpHandler = new HttpHandle(socket);
                httpHandler.start();

                }
        }catch(IOException ioe){


        }finally{

          try {
              serverSocket.close();
          }catch(Exception ignore){

          }
        }

    }


}

class HttpHandle extends Thread{

    private Socket socket;

    public HttpHandle(Socket socket){
        this.socket = socket;

    }

    public void run(){
        final String baseDir = "/Users/bigyo/DEVEL/kimdaeyong";

        BufferedReader in = null;
        PrintStream out = null;
        FileInputStream fis = null;

    try {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintStream(socket.getOutputStream());

        String requestLine = in.readLine();
        String[] s = requestLine.split(" ");
        String httpMethod = s[0];
        String httpPath = s[1];
        if (httpPath.equals("/"))
            httpPath = "/kimdaeyong.html";
        String filePath = baseDir + httpPath;

        File file = new File(filePath);
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType == null)
            mimeType = "text/html";

        String line = null;
        while((line = in.readLine()) != null){
            if("".equals(line)){ break;}
            System.out.println("헤더 정보" + line);

            out.println("HTTP/1.1 200 OK");
            out.println("Content=Type: " +mimeType);
            out.println("Content-Length: " + file.length());

            byte[] buffer = new byte[1024];
            int readCount = 0;
            fis = new FileInputStream(file);
            while((readCount = fis.read(buffer)) != -1){

                out.write(buffer, 0, readCount);
            }

        }

    }catch(IOException io){

    } finally {
        try {
            fis.close();
        } catch (Exception ignore) {
        }
        try {
            in.close();
        } catch (Exception ignore) {
        }
        try {
            out.close();
        } catch (Exception ignore) {
        }
        try {
            socket.close();
        } catch (Exception ignore) {
        }
    }
    }
}
package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        ServerSocket serverSocket;
        try{
            serverSocket = new ServerSocket(8080);
            while(true){
                Socket socket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String requestHeader;
                int contentLength = 0;
                while((requestHeader=reader.readLine()) != null && !requestHeader.isEmpty()){
                    System.out.println(requestHeader);
                    if(requestHeader.startsWith("GET")){
                        int begin = requestHeader.indexOf("/?") + 2;
                        int end = requestHeader.indexOf("HTTP/");
                        String condition = requestHeader.substring(begin, end);
                        System.out.println("GET参数是： " + condition);
                    }

                    if(requestHeader.startsWith("Content-Length")){
                        int begin = requestHeader.indexOf("Content-Length:") + "Content-Length:".length();
                        String postParamterLength = requestHeader.substring(begin).trim();
                        contentLength = Integer.parseInt(postParamterLength);
                        System.out.println("POST参数长度是：" + Integer.parseInt(postParamterLength));
                    }
                }

                StringBuffer stringBuffer = new StringBuffer();
                if(contentLength > 0){
                    for(int i = 0;i < contentLength;i++){
                        stringBuffer.append((char)reader.read());
                    }
                    System.out.println("POST参数是：" + stringBuffer.toString());
                }

                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.println("HTTP/1.1 200 OK");
                printWriter.println("Content-type:text/html;charset=utf-8");
                printWriter.println();
                printWriter.println("<h1>访问成功</h1>");

                printWriter.flush();
                /*InputStream is = socket.getInputStream();
                is.read(new byte[2048]);
                OutputStream os = socket.getOutputStream();

                os.write("HTTP/1.1 200 OK\r\n".getBytes());
                os.write("Content-Type:text/html;charset=utf-8\r\n".getBytes());
                os.write("Content-Length:38/r/n".getBytes());
                os.write("Servers:gybs\r\n".getBytes());
                os.write(("Date:" + new Date() + "\r\n").getBytes());
                os.write("\r\n".getBytes());
                os.write("<h1>hello</h1>".getBytes());
                os.write("<h3>HTTP服务器</h3>".getBytes("utf-8"));

                os.close();*/
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

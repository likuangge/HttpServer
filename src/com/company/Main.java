package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        ServerSocket serverSocket;
        Map<String, Object> mapParams;
        try{
            serverSocket = new ServerSocket(8888);
            while(true){
                Socket socket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String requestHeader;
                int contentLength = 0;
                mapParams = new HashMap<String, Object>();
                while((requestHeader=reader.readLine()) != null && !requestHeader.isEmpty()){
                    System.out.println(requestHeader + "每一行");
                    if(requestHeader.startsWith("GET")){
                        int begin = requestHeader.indexOf("/?") + 2;
                        int end = requestHeader.indexOf("HTTP/");
                        String getParams = requestHeader.substring(begin, end);
                        System.out.println("GET所有参数是： " + getParams);
                        String[] params = getParams.split("&");
                        for(int i = 0;i< params.length;i++){
                            System.out.println("GET参数: " + params[i]);
                            String[] temp = params[i].split("=");
                            System.out.println("resName: " + temp[0]);
                            System.out.println("resValue: " + temp[1]);
                            mapParams.put(temp[0], temp[1]);
                        }
                    }

                    if(requestHeader.startsWith("POST")){
                        System.out.println("每一行: " + requestHeader);
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

                /*File file = new File("");
                String localPath = file.getCanonicalPath();
                System.out.println("Project Path:" + localPath);
                String queryDirPath = localPath + "\\" + mapParams.get("dir_name");
                System.out.println("查询路径: " + queryDirPath);
                File queryDir = new File(queryDirPath);
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.println("HTTP/1.1 200 OK");
                printWriter.println("Content-type:text/html;charset=utf-8");
                printWriter.println();
                printWriter.println("type=queryDir&dir_name=" + queryDirPath);
                returnList(queryDir, printWriter);*/

                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void returnList(File file, PrintWriter printWriter){
        try {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getCanonicalPath());
                printWriter.println("type=dir&dir_name=" + file.getName());
                File[] files = file.listFiles();
                for (File childFile : files) {
                    returnList(childFile, printWriter);
                }
            } else {
                System.out.println("File: " + file.getName());
                System.out.println("FileParentPath: " + file.getParent());
                printWriter.println("type=file&file_name=" + file.getName() + "&file_parent_dir=" + file.getParent());
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            printWriter.flush();
        }
    }
}

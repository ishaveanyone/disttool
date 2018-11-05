package com.dist;

import java.io.*;
import java.net.Socket;

/**
 * Created by Administrator on 2018/9/27.
 */
public class ClientDealThread extends  Thread {
    private Socket socket;
    private PrintWriter writer;//输出流
    private BufferedReader reader;

    private String msg;

    public ClientDealThread(Socket socket) {
        this.socket = socket;
        try {
            writer=new PrintWriter(new OutputStreamWriter( socket.getOutputStream(),"UTF-8"));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //
    @Override
    public void run(){
        String message=null;
        try {
            //while 循环监听 数据 返回
            while((message = reader.readLine())!=null){
                System.out.println(message);
                msg=message;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public  void  sendMsg(String msg){
        writer.println(msg);
        writer.flush();
    }

}

package com.dist;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by Administrator on 2018/9/27.
 */
public class TestClient extends Thread{
    //定义一个Socket对象
    Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    private ClientDealThread clientDealThread;

    public TestClient(String host, int port) {
        try {
            //需要服务器的IP地址和端口号，才能获得正确的Socket对象
            socket = new Socket(host, port);
            clientDealThread=new  ClientDealThread(socket);
            clientDealThread.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void run(){
        String message=null;

        try {

            System.out.println("客户端运行成功");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String readMsg(){
        return clientDealThread.getMsg();
    }


    public void sendMsg(String msg){


        clientDealThread.sendMsg(msg);
    }



}

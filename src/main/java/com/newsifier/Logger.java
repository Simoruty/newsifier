package com.newsifier;

import com.newsifier.controller.WebSocketServer;

public class Logger {

    public static void log(String msg){
        System.out.println(msg);
    }

    public static void webLog(String msg){
        WebSocketServer.sendMessageOnSocket(msg);
    }

    public static void logErr(String msg){
        System.err.println(msg);
    }
}

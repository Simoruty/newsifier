package com.newsifier.utils;

import com.newsifier.controller.WebSocketServer;

/**
 * This class provides an abstraction for logging.
 * It allows to logs on STDOUT and a WebSocket.
 */
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

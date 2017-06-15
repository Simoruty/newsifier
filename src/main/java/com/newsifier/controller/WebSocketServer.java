package com.newsifier.controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
 
@ServerEndpoint(value = "/websocket")
public class WebSocketServer {
 
	private static Session socketSession;
	
    @OnOpen
    public void onOpen(Session session) {
    	socketSession = session;
    }
 
    public static void sendMessageOnSocket(String message){
		try {
			socketSession.getBasicRemote().sendText(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @OnMessage
    public String onMessage(String message, Session session) {
        return "Server received [" + message + "]";
    }
 
    @OnClose
    public void onClose(Session session) {
    }
 
    @OnError
    public void onError(Throwable exception, Session session) {
    }
}
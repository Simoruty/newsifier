package com.newsifier.controller;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.newsifier.Logger;
 
@ServerEndpoint(value = "/websocket")
public class WebSocketServer {
 
	private static Session socketSession;
	
    @OnOpen
    public void onOpen(Session session) {
    	socketSession = session;
    	Logger.log("The socket with id " + session.getId() + " has been opened.");
    }
 
    public static void sendMessageOnSocket(String message){
		try {
			socketSession.getBasicRemote().sendText(message);
		} catch (Exception e) {
			//e.printStackTrace();
			Logger.logErr("Error sending message to the client: " + e.getMessage());
		}
    }
    
    @OnMessage
    public String onMessage(String message, Session session) {
        return "Server received [" + message + "]";
    }
 
    @OnClose
    public void onClose(Session session) {
    	Logger.log("The socket with id " + session.getId() + " has been closed.");
    }
 
    @OnError
    public void onError(Throwable exception, Session session) {
    }
}
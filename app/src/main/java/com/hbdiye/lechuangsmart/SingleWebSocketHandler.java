package com.hbdiye.lechuangsmart;

import de.tavendo.autobahn.WebSocketConnection;

public class SingleWebSocketHandler {
    private static MyWebSocketHandler myWebSocketHandler;
    public static MyWebSocketHandler getInstance(WebSocketConnection mConnection, String message ){
        if (myWebSocketHandler==null){
            myWebSocketHandler= new MyWebSocketHandler(mConnection,message);
        }
        return myWebSocketHandler;
    }
}

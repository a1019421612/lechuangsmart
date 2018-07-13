package com.hbdiye.lechuangsmart;

import android.util.Log;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketHandler;

public class MyWebSocketHandler extends WebSocketHandler {

    public SocketSendMessage mSocketSendMessage;

    public void SetSocketsendMessage(SocketSendMessage mSocketSendMessage) {
        this.mSocketSendMessage = mSocketSendMessage;
    }

    private String message;
    private WebSocketConnection mConnection;
    private String tag;

    public MyWebSocketHandler(WebSocketConnection mConnection, String message) {
        this.mConnection = mConnection;
        this.message = message;

    }

    @Override
    public void onOpen() {
        Log.e("websocket", "onOpen");
//        mConnection.sendTextMessage(message);
    }

    @Override
    public void onTextMessage(String payload) {
        Log.e("websocket", "TextMessage: " + payload);
        if (payload.contains("{\"pn\":\"HRQP\"}")) {
            mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
        }
        mSocketSendMessage.websocketSendMessage(payload);
    }

    @Override
    public void onClose(int code, String reason) {
        Log.e("websocket", "close");
    }
}

package com.hbdiye.lechuangsmart.Global;

import android.content.Intent;

import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.activity.LoginActivity;

import de.tavendo.autobahn.WebSocketHandler;

public class CWebSocketHandler extends WebSocketHandler {
    @Override
    public void onTextMessage(String payload) {
        if (payload.contains("{\"pn\":\"PRTP\"}")) {
            MyApp.finishAllActivity();
            Intent intent = new Intent(MyApp.getContextObject(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            MyApp.getContextObject().startActivity(intent);
        }
    }
}

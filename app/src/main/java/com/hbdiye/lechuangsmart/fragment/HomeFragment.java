package com.hbdiye.lechuangsmart.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.Global.InterfaceManager;
import com.hbdiye.lechuangsmart.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.drafts.Draft_75;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;
import okhttp3.Call;

public class HomeFragment extends Fragment{
    private static final String TAG=HomeFragment.class.getName();
    private WebSocketClient webSocketClient;

    private WebSocketConnection mConnection ;
    WebSocketOptions mOptions=new WebSocketOptions();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mConnection= new WebSocketConnection();
        try {
            mConnection.connect("ws://39.104.105.10:18888/mobilephone=15944444444&password=123",new MyWebSocketHandler());

        } catch (WebSocketException e) {
            e.printStackTrace();
            SmartToast.show("网络连接错误");
        }
        return view;
    }
    class MyWebSocketHandler extends WebSocketHandler{
        @Override
        public void onOpen() {
            Log.e("TAG", "open");
        }

        @Override
        public void onTextMessage(String payload) {
            Log.e("TAG", "onTextMessage"+payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")){
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
            }
        }

        @Override
        public void onClose(int code, String reason) {
            Log.e("TAG", "onClose");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            // 隐藏
            Log.e("TAG","home"+"隐藏");
            mConnection.disconnect();

        } else {
            // 可视
            Log.e("TAG","home"+"显示");
            if (mConnection!=null){
                try {
                    mConnection.connect("ws://39.104.105.10:18888/mobilephone=15944444444&password=123",new MyWebSocketHandler());

                } catch (WebSocketException e) {
                    e.printStackTrace();
                    SmartToast.show("网络连接错误");
                }
            }
        }
    }
}

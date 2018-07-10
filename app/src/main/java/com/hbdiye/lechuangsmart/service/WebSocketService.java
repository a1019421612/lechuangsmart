package com.hbdiye.lechuangsmart.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.fragment.SceneFragment;
import com.hbdiye.lechuangsmart.util.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class WebSocketService extends Service {
    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;
    private String TAG=WebSocketService.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        mobilephone = (String) SPUtils.get(this, "mobilephone", "");
        password = (String) SPUtils.get(this, "password", "");
        new Thread(new Runnable() {
            @Override
            public void run() {
                mConnection=new WebSocketConnection();
                try {
                    mConnection.connect("ws://39.104.119.0:18888/mobilephone=" + mobilephone + "&password=" + password, new MyWebSocketHandler());

                } catch (WebSocketException e) {
                    e.printStackTrace();
                    SmartToast.show("网络连接错误");
                }
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    class MyWebSocketHandler extends WebSocketHandler{
        @Override
        public void onOpen() {
            Log.e(TAG, "open");
//            mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
        }

        @Override
        public void onTextMessage(String payload) {
            Log.e(TAG, "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
            }
//            if (payload.contains("\"pn\":\"STLTP\"")) {
//                parseData(payload);
//            }
//            if (payload.contains("\"pn\":\"SUTP\"")) {
//                //修改场景名称
//                try {
//                    JSONObject jsonObject = new JSONObject(payload);
//                    boolean status = jsonObject.getBoolean("status");
//                    if (status) {
//                        sceneDialog.dismiss();
//                        SmartToast.show("修改成功");
//                        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (payload.contains("\"pn\":\"STATP\"")) {
//                //添加设备
//                try {
//                    JSONObject jsonObject = new JSONObject(payload);
//                    boolean status = jsonObject.getBoolean("status");
//                    if (status) {
//                        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
//                        drawerLayout.closeDrawers();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (payload.contains("\"pn\":\"STUTP\"")) {
//                //设置延时STUTP
//                try {
//                    JSONObject jsonObject = new JSONObject(payload);
//                    boolean status = jsonObject.getBoolean("status");
//                    if (status) {
//                        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (payload.contains("\"pn\":\"STDTP\"")) {
//                //删除设备STDTP
//                try {
//                    JSONObject jsonObject = new JSONObject(payload);
//                    boolean status = jsonObject.getBoolean("status");
//                    if (status) {
//                        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
        }

        @Override
        public void onClose(int code, String reason) {
            Log.e(TAG, "onClose");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mConnection.sendTextMessage("{\"pn\":\"HRP\"}");
    }
}

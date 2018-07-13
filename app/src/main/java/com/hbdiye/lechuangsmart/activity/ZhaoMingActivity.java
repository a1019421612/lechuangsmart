package com.hbdiye.lechuangsmart.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.SingleWebSocketConnection;
import com.hbdiye.lechuangsmart.adapter.AnFangAdapter;
import com.hbdiye.lechuangsmart.bean.AnFangBean;
import com.hbdiye.lechuangsmart.util.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class ZhaoMingActivity extends BaseActivity {
    @BindView(R.id.rv_zhaoming)
    RecyclerView rvZhaoming;
    private WebSocketConnection mConnection;
    private HomeReceiver homeReceiver;
    private List<AnFangBean.Devices> mList=new ArrayList<>();
    private AnFangAdapter adapter;
    private List<Boolean> mList_status=new ArrayList<>();

    @Override
    protected void initData() {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("DGLTP");
        intentFilter.addAction("ATP");
        intentFilter.addAction("SDOSTP");
        homeReceiver = new HomeReceiver();
        registerReceiver(homeReceiver,intentFilter);
        mConnection = SingleWebSocketConnection.getInstance();
        mConnection.sendTextMessage("{\"pn\":\"DGLTP\", \"classify\":\"protype\", \"id\":\"PROTYPE05\"}");
    }

    @Override
    protected String getTitleName() {
        return "照明设备列表";
    }

    @Override
    protected void initView() {
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvZhaoming.setLayoutManager(manager);
        adapter=new AnFangAdapter(mList,mList_status);
        rvZhaoming.setAdapter(adapter);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_zhao_ming;
    }

    class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String payload = intent.getStringExtra("message");
            if (action.equals("DGLTP")) {
                parseData(payload);
            }
            if (action.equals("ATP")){
                mConnection.sendTextMessage("{\"pn\":\"DGLTP\", \"classify\":\"protype\", \"id\":\"PROTYPE05\"}");
            }
            if(action.equals("SDOSTP")){
                try {
                    JSONObject jsonObject=new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    String sdMAC = jsonObject.getString("sdMAC");
                    //子设备在线
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).mac.equals(sdMAC)){
                            mList_status.set(i,status);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
    class MyWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e("TAG", "open");
            mConnection.sendTextMessage("{\"pn\":\"DGLTP\", \"classify\":\"protype\", \"id\":\"PROTYPE05\"}");
        }

        @Override
        public void onTextMessage(String payload) {

            Log.e("TAG", "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
            }
            if (payload.contains("\"pn\":\"DOSTP\"")) {

            }
            if (payload.contains("{\"pn\":\"PRTP\"}")) {
                MyApp.finishAllActivity();
                Intent intent = new Intent(ZhaoMingActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            if (payload.contains("\"pn\":\"DGLTP\"")) {
                parseData(payload);
            }
            if (payload.contains("\"pn\":\"SDOSTP\"")) {
                try {
                    JSONObject jsonObject=new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    String sdMAC = jsonObject.getString("sdMAC");
                    //子设备在线
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).mac.equals(sdMAC)){
                            mList_status.set(i,status);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onClose(int code, String reason) {
            Log.e("TAG", "onClose");
        }
    }

    private void parseData(String payload) {
        AnFangBean anFangBean = new Gson().fromJson(payload, AnFangBean.class);
        if (mList.size()>0){
            mList.clear();
        }
        List<AnFangBean.Devices> devices = anFangBean.devices;
        if (devices.size()>0){
            mList.addAll(devices);
            for (int i = 0; i < devices.size(); i++) {
                mList_status.add(false);
            }
//        adapter.notifyDataSetChanged();
            mConnection.sendTextMessage("{\"pn\":\"SDOSTP\"}");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
    }
}

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
import com.hbdiye.lechuangsmart.adapter.ChuanGanQiAdapter;
import com.hbdiye.lechuangsmart.adapter.ChuangLianAdapter;
import com.hbdiye.lechuangsmart.bean.ChuanGanQiBean;
import com.hbdiye.lechuangsmart.bean.ChuangLianBean;
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

public class ChuanganqiActivity extends BaseActivity {
    @BindView(R.id.rv_chuanganqi)
    RecyclerView rvChuanganqi;
    private WebSocketConnection mConnection;
    private HomeReceiver homeReceiver;
    private List<ChuanGanQiBean.Devices> mList = new ArrayList<>();
    private ChuanGanQiAdapter adapter;
    private List<Boolean> mList_status = new ArrayList<>();

    @Override
    protected void initData() {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("DGLTP");
        intentFilter.addAction("ATP");
        intentFilter.addAction("SDOSTP");
        homeReceiver = new HomeReceiver();
        registerReceiver(homeReceiver,intentFilter);
        mConnection = SingleWebSocketConnection.getInstance();
        mConnection.sendTextMessage("{\"pn\":\"DGLTP\", \"classify\":\"protype\", \"id\":\"PROTYPE07\"}");
    }

    @Override
    protected String getTitleName() {
        return "传感器设备列表";
    }

    @Override
    protected void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvChuanganqi.setLayoutManager(manager);
        adapter = new ChuanGanQiAdapter(mList, mList_status);
        rvChuanganqi.setAdapter(adapter);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_chuanganqi;
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
                mConnection.sendTextMessage("{\"pn\":\"DGLTP\", \"classify\":\"protype\", \"id\":\"PROTYPE07\"}");
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
            mConnection.sendTextMessage("{\"pn\":\"DGLTP\", \"classify\":\"protype\", \"id\":\"PROTYPE07\"}");
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
                Intent intent = new Intent(ChuanganqiActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            if (payload.contains("\"pn\":\"ATP\"")){
                mConnection.sendTextMessage("{\"pn\":\"DGLTP\", \"classify\":\"protype\", \"id\":\"PROTYPE07\"}");
            }
            if (payload.contains("\"pn\":\"DGLTP\"")) {
                parseData(payload);
            }
            if (payload.contains("\"pn\":\"SDOSTP\"")) {
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    String sdMAC = jsonObject.getString("sdMAC");
                    //子设备在线
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).mac.equals(sdMAC)) {
                            mList_status.set(i, status);
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
        ChuanGanQiBean chuanGanQiBean=new Gson().fromJson(payload,ChuanGanQiBean.class);
        if (mList.size()>0){
            mList.clear();
        }
        List<ChuanGanQiBean.Devices> devices=chuanGanQiBean.devices;
        mList.addAll(devices);
//        ChuangLianBean chuangLianBean = new Gson().fromJson(payload, ChuangLianBean.class);
//        if (mList.size() > 0) {
//            mList.clear();
//        }
//        List<ChuangLianBean.Devices> devices = chuangLianBean.devices;
//        mList.addAll(devices);
        for (int i = 0; i < devices.size(); i++) {
            mList_status.add(false);
        }
        mConnection.sendTextMessage("{\"pn\":\"SDOSTP\"}");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
    }
}

package com.hbdiye.lechuangsmart.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.SingleWebSocketConnection;
import com.hbdiye.lechuangsmart.adapter.ChuangLianAdapter;
import com.hbdiye.lechuangsmart.adapter.KaiGuanAdapter;
import com.hbdiye.lechuangsmart.bean.ChuangLianBean;
import com.hbdiye.lechuangsmart.bean.KaiGuanBean;
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

public class KaiguanActivity extends BaseActivity {

    @BindView(R.id.rv_kaiguan)
    RecyclerView rvKaiguan;
    private WebSocketConnection mConnection;
    private HomeReceiver homeReceiver;
    private List<KaiGuanBean.Devices> mList = new ArrayList<>();
    private KaiGuanAdapter adapter;
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
        mConnection.sendTextMessage("{\"pn\":\"DGLTP\", \"classify\":\"protype\", \"id\":\"PROTYPE04\"}");
    }

    @Override
    protected String getTitleName() {
        return "开关设备列表";
    }

    @Override
    protected void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvKaiguan.setLayoutManager(manager);
        adapter = new KaiGuanAdapter(mList, mList_status);
        rvKaiguan.setAdapter(adapter);

        handlerClick();
    }

    private void handlerClick() {
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                KaiGuanBean.Devices devices = mList.get(position);
                switch (view.getId()) {
                    case R.id.iv_kg_left:
                        int value_left = devices.deviceAttributes.get(0).value;
                        if (value_left==0){
                            //value=0 关
                            String onId = devices.deviceAttributes.get(0).actions.get(0).id;//开id
                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\""+mList.get(position).id+"\",\"proActID\":\""+onId+"\",\"param\":\"\"}");
                        }else {
                            //开
                            String offId = devices.deviceAttributes.get(0).actions.get(1).id;//关id
                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\""+mList.get(position).id+"\",\"proActID\":\""+offId+"\",\"param\":\"\"}");
                        }
                        break;
                    case R.id.iv_kg_middle:
                        int value_middle = devices.deviceAttributes.get(1).value;
                        if (value_middle==0){
                            String onId = devices.deviceAttributes.get(1).actions.get(0).id;//开id
                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\""+mList.get(position).id+"\",\"proActID\":\""+onId+"\",\"param\":\"\"}");
                        }else {
                            String offId = devices.deviceAttributes.get(1).actions.get(1).id;//关id
                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\""+mList.get(position).id+"\",\"proActID\":\""+offId+"\",\"param\":\"\"}");
                        }
                        break;
                    case R.id.iv_kg_right:
                        int value_right = devices.deviceAttributes.get(2).value;
                        if (value_right==0){
                            String onId = devices.deviceAttributes.get(2).actions.get(0).id;//开id
                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\""+mList.get(position).id+"\",\"proActID\":\""+onId+"\",\"param\":\"\"}");
                        }else {
                            String offId = devices.deviceAttributes.get(2).actions.get(1).id;//关id
                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\""+mList.get(position).id+"\",\"proActID\":\""+offId+"\",\"param\":\"\"}");
                        }
                        break;
//                    case R.id.checkbox_left:
//                        int value_left = devices.deviceAttributes.get(0).value;
//                        if (value_left==0){
//                            //value=0 关
//                            String onId = devices.deviceAttributes.get(0).actions.get(0).id;//开id
//                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\""+mList.get(position).id+"\",\"proActID\":\""+onId+"\",\"param\":\"\"}");
//                        }else {
//                            //开
//                            String offId = devices.deviceAttributes.get(0).actions.get(1).id;//关id
//                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\""+mList.get(position).id+"\",\"proActID\":\""+offId+"\",\"param\":\"\"}");
//                        }
//                        break;
//                    case R.id.checkbox_middle:
//                        int value_middle = devices.deviceAttributes.get(1).value;
//                        if (value_middle==0){
//                            String onId = devices.deviceAttributes.get(1).actions.get(0).id;//开id
//                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\""+mList.get(position).id+"\",\"proActID\":\""+onId+"\",\"param\":\"\"}");
//                        }else {
//                            String offId = devices.deviceAttributes.get(1).actions.get(1).id;//关id
//                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\""+mList.get(position).id+"\",\"proActID\":\""+offId+"\",\"param\":\"\"}");
//                        }
//                        break;
//                    case R.id.checkbox_right:
//                        int value_right = devices.deviceAttributes.get(2).value;
//                        if (value_right==0){
//                            String onId = devices.deviceAttributes.get(2).actions.get(0).id;//开id
//                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\""+mList.get(position).id+"\",\"proActID\":\""+onId+"\",\"param\":\"\"}");
//                        }else {
//                            String offId = devices.deviceAttributes.get(2).actions.get(1).id;//关id
//                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\""+mList.get(position).id+"\",\"proActID\":\""+offId+"\",\"param\":\"\"}");
//                        }
//                        break;
                }
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_kaiguan;
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
                mConnection.sendTextMessage("{\"pn\":\"DGLTP\", \"classify\":\"protype\", \"id\":\"PROTYPE04\"}");
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
            mConnection.sendTextMessage("{\"pn\":\"DGLTP\", \"classify\":\"protype\", \"id\":\"PROTYPE04\"}");
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
                Intent intent = new Intent(KaiguanActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            if (payload.contains("\"pn\":\"ATP\"")){
                mConnection.sendTextMessage("{\"pn\":\"DGLTP\", \"classify\":\"protype\", \"id\":\"PROTYPE04\"}");
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
        KaiGuanBean anFangBean = new Gson().fromJson(payload, KaiGuanBean.class);
        if (mList.size()>0){
            mList.clear();
        }
        List<KaiGuanBean.Devices> devices = anFangBean.devices;
        mList.addAll(devices);
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

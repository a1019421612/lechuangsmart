package com.hbdiye.lechuangsmart.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.hbdiye.lechuangsmart.R;
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
    private String mobilephone;
    private String password;

    private List<KaiGuanBean.Devices> mList = new ArrayList<>();
    private KaiGuanAdapter adapter;
    private List<Boolean> mList_status = new ArrayList<>();

    @Override
    protected void initData() {
        mobilephone = (String) SPUtils.get(this, "mobilephone", "");
        password = (String) SPUtils.get(this, "password", "");
        mConnection = new WebSocketConnection();
        socketConnect();
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
                    case R.id.checkbox_left:
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
                    case R.id.checkbox_middle:
                        int value_middle = devices.deviceAttributes.get(1).value;
                        if (value_middle==0){
                            String onId = devices.deviceAttributes.get(1).actions.get(0).id;//开id
                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\""+mList.get(position).id+"\",\"proActID\":\""+onId+"\",\"param\":\"\"}");
                        }else {
                            String offId = devices.deviceAttributes.get(1).actions.get(1).id;//关id
                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\""+mList.get(position).id+"\",\"proActID\":\""+offId+"\",\"param\":\"\"}");
                        }
                        break;
                    case R.id.checkbox_right:
                        int value_right = devices.deviceAttributes.get(2).value;
                        if (value_right==0){
                            String onId = devices.deviceAttributes.get(2).actions.get(0).id;//开id
                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\""+mList.get(position).id+"\",\"proActID\":\""+onId+"\",\"param\":\"\"}");
                        }else {
                            String offId = devices.deviceAttributes.get(2).actions.get(1).id;//关id
                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\""+mList.get(position).id+"\",\"proActID\":\""+offId+"\",\"param\":\"\"}");
                        }
                        break;
                }
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_kaiguan;
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
    protected void onStop() {
        super.onStop();
        mConnection.disconnect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mConnection != null) {
            socketConnect();
        }
    }

    private void socketConnect() {
        try {
            mConnection.connect("ws://39.104.105.10:18888/mobilephone=" + mobilephone + "&password=" + password, new MyWebSocketHandler());

        } catch (WebSocketException e) {
            e.printStackTrace();
            SmartToast.show("网络连接错误");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConnection.disconnect();
    }
}

package com.hbdiye.lechuangsmart.activity;

import android.app.Activity;
import android.content.Intent;
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
import com.hbdiye.lechuangsmart.adapter.KaiGuanAdapter;
import com.hbdiye.lechuangsmart.adapter.YaoKongAdapter;
import com.hbdiye.lechuangsmart.bean.KaiGuanBean;
import com.hbdiye.lechuangsmart.bean.YaoKongBean;
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

/**
 * 红外设备列表
 */
public class YaokongqiActivity extends BaseActivity {
    @BindView(R.id.rv_yaokong)
    RecyclerView rvYaokong;
    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;

    private List<YaoKongBean.Devices> mList = new ArrayList<>();
    private YaoKongAdapter adapter;
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
        return "遥控器设备列表";
    }

    @Override
    protected void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvYaokong.setLayoutManager(manager);
        adapter = new YaoKongAdapter(mList, mList_status);
        rvYaokong.setAdapter(adapter);

        handlerClick();
    }

    private void handlerClick() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(YaokongqiActivity.this, YaoKongListActivity.class)
                        .putExtra("deviceID", mList.get(position).id)
                        .putExtra("deviceName", mList.get(position).name)
                        .putExtra("mac", mList.get(position).mac));
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_yaokongqi;
    }

    private List<String> mList_a= new ArrayList<>();
    class MyWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e("TAG", "open");
            mConnection.sendTextMessage("{\"pn\":\"DGLTP\", \"classify\":\"protype\", \"id\":\"PROTYPE09\"}");
        }

        @Override
        public void onTextMessage(String payload) {

            Log.e("TAG", "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
            }
            if (payload.contains("{\"pn\":\"PRTP\"}")) {
                for (Activity activity : MyApp.mActivitys) {
                    String packageName = activity.getLocalClassName();
                    Log.e("LLL",packageName);
                    mList_a.add(packageName);
                }
                if (mList_a.get(mList_a.size()-1).equals("YaokongqiActivity")){
                    MyApp.finishAllActivity();
                    Intent intent = new Intent(YaokongqiActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
            if (payload.contains("\"pn\":\"DOSTP\"")) {

            }
            if (payload.contains("\"pn\":\"ATP\"")){
                mConnection.sendTextMessage("{\"pn\":\"DGLTP\", \"classify\":\"protype\", \"id\":\"PROTYPE09\"}");
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
        YaoKongBean anFangBean = new Gson().fromJson(payload, YaoKongBean.class);
        if (mList.size() > 0) {
            mList.clear();
        }
        List<YaoKongBean.Devices> devices = anFangBean.devices;
        mList.addAll(devices);
        for (int i = 0; i < devices.size(); i++) {
            mList_status.add(false);
        }
        mConnection.sendTextMessage("{\"pn\":\"SDOSTP\"}");
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        mConnection.disconnect();
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        if (mConnection != null) {
//            socketConnect();
//        }
//    }

    private void socketConnect() {
        try {
            mConnection.connect("ws://39.104.119.0:18888/mobilephone=" + mobilephone + "&password=" + password, new MyWebSocketHandler());

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

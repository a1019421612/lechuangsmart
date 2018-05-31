package com.hbdiye.lechuangsmart.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.adapter.DeviceTriggeredAdapter;
import com.hbdiye.lechuangsmart.bean.DeviceTriggerBean;
import com.hbdiye.lechuangsmart.bean.LinkageSettingBean;
import com.hbdiye.lechuangsmart.util.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class DeviceTriggeredActivity extends AppCompatActivity {

    @BindView(R.id.iv_base_back)
    ImageView ivBaseBack;
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.toolbar_ll)
    LinearLayout toolbarLl;
    @BindView(R.id.tv_drawer)
    TextView tvDrawer;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.tv_device_trig_name)
    TextView tvDeviceTrigName;
    @BindView(R.id.ll_device_triggered)
    LinearLayout llDeviceTriggered;
    @BindView(R.id.tv_device_trig_attr)
    TextView tvDeviceTrigAttr;
    @BindView(R.id.tv_device_trig_condition)
    TextView tvDeviceTrigCondition;
    @BindView(R.id.et_device_trig_value)
    EditText etDeviceTrigValue;
    @BindView(R.id.rv_device_trig)
    RecyclerView rvDeviceTrig;
    private boolean isOpen = false;//默认侧边栏关闭
    private String TAG=DeviceTriggeredActivity.class.getSimpleName();

    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;

    private List<DeviceTriggerBean.Devices> mList=new ArrayList<>();
    private DeviceTriggeredAdapter adapter;

    private LinkageSettingBean.Linkage linkage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_triggered);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        linkage= (LinkageSettingBean.Linkage) getIntent().getSerializableExtra("LinkageData");
        mobilephone = (String) SPUtils.get(this, "mobilephone", "");
        password = (String) SPUtils.get(this, "password", "");
        mConnection = new WebSocketConnection();
        socketConnection();

        String name = linkage.device.name;
        tvDeviceTrigName.setText(name);

    }

    private void initView() {
        tvBaseTitle.setText("设置设备触发条件");
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.setFocusableInTouchMode(false);

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                isOpen = true;
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                isOpen = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDeviceTrig.setLayoutManager(manager);
        adapter=new DeviceTriggeredAdapter(mList);
        rvDeviceTrig.setAdapter(adapter);
    }


    @OnClick({R.id.iv_base_back, R.id.tv_base_title, R.id.ll_device_triggered, R.id.tv_device_trig_attr, R.id.tv_device_trig_condition})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_base_back:
                if (isOpen) {
                    drawerLayout.closeDrawers();
                } else {
                    finish();
                }
                break;
            case R.id.tv_base_title:
                break;
            case R.id.ll_device_triggered:
                if (isOpen) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
                break;
            case R.id.tv_device_trig_attr:
                break;
            case R.id.tv_device_trig_condition:
                break;
        }
    }
    class MyWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e(TAG, "open");
//            mConnection.sendTextMessage("{\"pn\":\"LCTP\",\"linkageID\":\"" + linkageID + "\"}");
            mConnection.sendTextMessage("{\"pn\":\"LDLTP\",\"type\":0}");
        }

        @Override
        public void onTextMessage(String payload) {
            Log.e(TAG, "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");

            }
            if (payload.contains("\"pn\":\"LDLTP\"")) {
                //设备列表
                parseDeviceData(payload);
            }
        }

        @Override
        public void onClose(int code, String reason) {
            Log.e(TAG, "onClose");
        }
    }

    private void parseDeviceData(String payload) {
        try {
            DeviceTriggerBean deviceTriggerBean = new Gson().fromJson(payload, DeviceTriggerBean.class);
            List<DeviceTriggerBean.Devices> devices = deviceTriggerBean.devices;
            if (mList.size()>0){
                mList.clear();
            }
            mList.addAll(devices);
            adapter.notifyDataSetChanged();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (!isOpen) {
            super.onBackPressed();
            return;
        } else {
            drawerLayout.closeDrawers();
        }
    }
    private void socketConnection() {
        try {
            mConnection.connect("ws://39.104.105.10:18888/mobilephone=" + mobilephone + "&password=" + password, new MyWebSocketHandler());

        } catch (WebSocketException e) {
            e.printStackTrace();
            SmartToast.show("网络连接错误");
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onstop");
        mConnection.disconnect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onrestart");
        if (mConnection != null) {
            socketConnection();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onstop");
        mConnection.disconnect();
    }
}

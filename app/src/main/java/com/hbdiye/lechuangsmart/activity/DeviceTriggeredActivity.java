package com.hbdiye.lechuangsmart.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
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

    String[] items_fanwei={"大于","小于","等于"};
    int[] items_value={-1,0,1};
    private int type=-2;
    private int flag_type=-2;

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
        int value = linkage.value;
        type = linkage.type;
        if (type ==-1){
            tvDeviceTrigCondition.setText("大于");
        }else if (type ==0){
            tvDeviceTrigCondition.setText("小于");
        }else if (type ==1){
            tvDeviceTrigCondition.setText("等于");
        }
        etDeviceTrigValue.setText(value+"");
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

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }
        });
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
                List<LinkageSettingBean.Linkage.Device.DeviceAttributes> deviceAttributes = linkage.device.deviceAttributes;
                String[] items_attr=new String[deviceAttributes.size()];
                for (int i = 0; i < deviceAttributes.size(); i++) {
                    String attName = deviceAttributes.get(i).proAtt.name;
                    items_attr[i]=attName;
                }
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setSingleChoiceItems(items_attr, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
                break;
            case R.id.tv_device_trig_condition:
                //选择大于小于等于
                for (int i = 0; i < items_value.length; i++) {
                    if (items_value[i]==type){
                        flag_type=i;
                    }
                }
                AlertDialog.Builder builder1=new AlertDialog.Builder(this);
                builder1.setSingleChoiceItems(items_fanwei, flag_type, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            type=items_value[i];
                            SmartToast.show(type+"");
                            tvDeviceTrigCondition.setText(items_fanwei[i]);
                        dialogInterface.dismiss();
                    }
                });
                builder1.show();
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

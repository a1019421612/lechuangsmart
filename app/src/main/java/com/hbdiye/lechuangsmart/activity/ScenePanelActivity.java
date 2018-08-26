package com.hbdiye.lechuangsmart.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.SingleWebSocketConnection;
import com.hbdiye.lechuangsmart.adapter.ScenePanelAdapter;
import com.hbdiye.lechuangsmart.adapter.ScenePanelDeviceAdapter;
import com.hbdiye.lechuangsmart.bean.RoomBean;
import com.hbdiye.lechuangsmart.bean.ScenePanelBean;
import com.hbdiye.lechuangsmart.bean.ScenePanelDeviceBean;
import com.hbdiye.lechuangsmart.views.SceneDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.tavendo.autobahn.WebSocketConnection;

public class ScenePanelActivity extends AppCompatActivity {
    @BindView(R.id.iv_base_back)
    ImageView ivBaseBack;
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.iv_base_right)
    ImageView ivBaseRight;
    @BindView(R.id.toolbar_ll)
    LinearLayout toolbarLl;
    @BindView(R.id.tv_drawer)
    TextView tvDrawer;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.rv_panel)
    RecyclerView rvPanel;
    @BindView(R.id.ll_add_panel)
    LinearLayout llAddPanel;
    @BindView(R.id.rv_panel_device)
    RecyclerView rvPanelDevice;
    private boolean isOpen = false;//默认侧边栏关闭

    private WebSocketConnection mConnection;
    private HomeReceiver homeReceiver;
    private ScenePanelAdapter adapter;
    private List<ScenePanelBean.Devices> mList = new ArrayList<>();
    private boolean editStatus = false;//编辑状态标志，默认false

    private SceneDialog sceneDialog;
    private int flag = -1;
    private String sceneID = "";

    private ScenePanelDeviceAdapter mAdapter;
    private List<ScenePanelDeviceBean.Devices> mList_device = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_panel);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        sceneID = getIntent().getStringExtra("sceneID");
        if (TextUtils.isEmpty(sceneID)) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("RGLTP");
        intentFilter.addAction("RUTP");
        intentFilter.addAction("RATP");
        intentFilter.addAction("RDTP");
        intentFilter.addAction("SDRTP");
        intentFilter.addAction("DUTP");
        homeReceiver = new HomeReceiver();
        registerReceiver(homeReceiver, intentFilter);
        mConnection = SingleWebSocketConnection.getInstance();
//        mConnection.sendTextMessage("{\"pn\":\"RGLTP\"}");
        mConnection.sendTextMessage("{\"pn\":\"SDRTP\",\"method\":\"LIST\",\"sceneID\":\"" + sceneID + "\"}");
        mConnection.sendTextMessage("{\"pn\":\"SDRTP\",\"method\":\"REL\"}");
    }

    private void initView() {
        tvBaseTitle.setText("情景面板设置");
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.setFocusableInTouchMode(false);
        ivBaseRight.setVisibility(View.VISIBLE);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPanel.setLayoutManager(manager);
        adapter = new ScenePanelAdapter(mList);
        rvPanel.setAdapter(adapter);

        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.VERTICAL);
        rvPanelDevice.setLayoutManager(manager1);
        mAdapter = new ScenePanelDeviceAdapter(mList_device);
        rvPanelDevice.setAdapter(mAdapter);

        handlerClick();
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
            public void onDrawerStateChanged(int newStateF) {

            }
        });

    }

    private void handlerClick() {
        ivBaseRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editStatus) {
                    ivBaseRight.setImageResource(R.drawable.bianji2);
                    adapter.sceneStatusChange(editStatus);
                    editStatus = false;
                } else {
                    ivBaseRight.setImageResource(R.drawable.duigou);
                    adapter.sceneStatusChange(editStatus);
                    editStatus = true;
                }
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String message = "{\"pn\":\"SDRTP\",\"method\":\"A\",\"sceneID\":\"" + sceneID + "\",\"deviID\":\"" + mList_device.get(position).id + "\"}";
                mConnection.sendTextMessage(message);
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                flag = position;
                switch (view.getId()) {
                    case R.id.ll_scene_item_del:
                        mConnection.sendTextMessage("{\"pn\":\"SDRTP\",\"method\":\"D\",\"sdrID\":\""+mList.get(position).id+"\"}");
                        break;
                    case R.id.ll_scene_item_edt:
                        sceneDialog = new SceneDialog(ScenePanelActivity.this, R.style.MyDialogStyle, dailogClicer, "修改情景名称");
                        sceneDialog.setCanceledOnTouchOutside(true);
                        sceneDialog.show();
                        break;
                    case R.id.ll_scene_device:
                        if (!editStatus) {
                            startActivity(new Intent(ScenePanelActivity.this, RoomActivity.class).putExtra("roomId", mList.get(position).id));
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public View.OnClickListener dailogClicer = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.app_cancle_tv:
                    sceneDialog.dismiss();
                    break;
                case R.id.app_sure_tv:
                    String sceneName = sceneDialog.getSceneName().trim();
                    if (TextUtils.isEmpty(sceneName)) {
                        SmartToast.show("情景名称不能为空");
                    } else {
//                        mConnection.sendTextMessage("{\"pn\":\"RUTP\",\"roomID\":\"" + mList.get(flag).id + "\",\"updateType\":\"rn\",\"roomName\":\"" + sceneName + "\"}");
                        String devicemac = mList.get(flag).mac;
                        mConnection.sendTextMessage("{\"pn\":\"DUTP\",\"deviceMAC\":\"" + devicemac + "\",\"newName\": \"" + sceneName + "\"}");
                    }
                    break;
                    default:
                        break;
            }
        }
    };
    public View.OnClickListener addroomClicer = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.app_cancle_tv:
                    sceneDialog.dismiss();
                    break;
                case R.id.app_sure_tv:
                    String roomName = sceneDialog.getSceneName().trim();
                    mConnection.sendTextMessage("{\"pn\":\"RATP\",\"roomName\":\"" + roomName + "\"}");
                    break;
            }
        }
    };

    @OnClick({R.id.iv_base_back, R.id.tv_base_title, R.id.iv_base_right, R.id.ll_add_panel})
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
            case R.id.iv_base_right:

                break;
            case R.id.ll_add_panel:
//                sceneDialog = new SceneDialog(ScenePanelActivity.this, R.style.MyDialogStyle, addroomClicer, "添加房间名称");
//                sceneDialog.setCanceledOnTouchOutside(true);
//                sceneDialog.show();
                if (isOpen) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
                break;
            default:
                break;
        }
    }

    class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String payload = intent.getStringExtra("message");
            if (action.equals("SDRTP")) {
                if (payload.contains("LIST")) {
                    //情景面板列表
                    parseData(payload);
                } else if (payload.contains("REL")) {
                    //可关联设备列表
                    Log.e("REL", payload);
                    parseDeviceData(payload);
                } else if (payload.contains("\"method\":\"A\"")) {
                    try {
                        JSONObject jsonObject = new JSONObject(payload);
                        String stCode = jsonObject.getString("stCode");
                        if (stCode.equals("200")) {
                            SmartToast.show("添加成功");
                            drawerLayout.closeDrawers();
                            mConnection.sendTextMessage("{\"pn\":\"SDRTP\",\"method\":\"LIST\",\"sceneID\":\"" + sceneID + "\"}");
                        } else if (stCode.equals("304")) {
                            SmartToast.show("网关不在线");
                        } else if (stCode.equals("404")) {
                            SmartToast.show("设备不在线");
                        } else if (stCode.equals("481")) {
                            SmartToast.show("组号设置失败");
                        } else if (stCode.equals("483")) {
                            SmartToast.show("场景不存在");
                        } else if (stCode.equals("487")) {
                            SmartToast.show("情景面板不支持绑定该场景");
                        } else {
                            SmartToast.show("添加失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (payload.contains("\"method\":\"D\"")) {
                    try {
                        JSONObject jsonObject = new JSONObject(payload);
                        String stCode = jsonObject.getString("stCode");
                        if (stCode.equals("200")) {
                            SmartToast.show("删除成功");
                            drawerLayout.closeDrawers();
                            mConnection.sendTextMessage("{\"pn\":\"SDRTP\",\"method\":\"LIST\",\"sceneID\":\"" + sceneID + "\"}");
                        } else if (stCode.equals("304")) {
                            SmartToast.show("网关不在线");
                        }else if (stCode.equals("481")) {
                            SmartToast.show("组号设置失败");
                        } else if (stCode.equals("999")) {
                            SmartToast.show("协议非法");
                        } else {
                            SmartToast.show("添加失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (action.contains("DUTP")){
                //修改情景名称
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        sceneDialog.dismiss();
                        SmartToast.show("修改成功");
                        mConnection.sendTextMessage("{\"pn\":\"SDRTP\",\"method\":\"LIST\",\"sceneID\":\"" + sceneID + "\"}");
                    }else {
                        SmartToast.show("修改成功");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (action.equals("RUTP")) {
                //修改房间名称 RUTP
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        sceneDialog.dismiss();
                        SmartToast.show("修改房间名称成功");
                        mConnection.sendTextMessage("{\"pn\":\"RGLTP\"}");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (action.equals("RATP")) {
                //添加新房间 RATP
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        sceneDialog.dismiss();
                        SmartToast.show("添加房间成功");
                        mConnection.sendTextMessage("{\"pn\":\"RGLTP\"}");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (action.equals("RDTP")) {
                //删除房间 RDTP
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        SmartToast.show("删除房间成功");
                        mConnection.sendTextMessage("{\"pn\":\"RGLTP\"}");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void parseDeviceData(String payload) {
        ScenePanelDeviceBean scenePanelDeviceBean = new Gson().fromJson(payload, ScenePanelDeviceBean.class);
        List<ScenePanelDeviceBean.Devices> devices = scenePanelDeviceBean.devices;
        if (mList_device.size() > 0) {
            mList_device.clear();
        }
        mList_device.addAll(devices);
        mAdapter.notifyDataSetChanged();
    }

    private void parseData(String payload) {

        ScenePanelBean scenePanelBean = new Gson().fromJson(payload, ScenePanelBean.class);
        List<ScenePanelBean.Devices> devices = scenePanelBean.devices;

        if (mList.size() > 0) {
            mList.clear();
        }
        mList.addAll(devices);
        adapter.notifyDataSetChanged();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
    }
//    private boolean isOpen=false;//默认侧边栏关闭
//    @BindView(R.id.drawer_layout)
//    DrawerLayout drawerLayout;
//
//    @Override
//    protected void initData() {
//
//    }
//
//    @Override
//    protected String getTitleName() {
//        return "关于我们";
//    }
//
//    @Override
//    protected void initView() {
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        drawerLayout.setFocusableInTouchMode(false);
//        ivBaseEdit.setVisibility(View.VISIBLE);
//        ivBaseBack.setOnClickListener(this);
//        ivBaseEdit.setOnClickListener(this);
//        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
//            @Override
//            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
//
//            }
//
//            @Override
//            public void onDrawerOpened(@NonNull View drawerView) {
//                isOpen=true;
//            }
//
//            @Override
//            public void onDrawerClosed(@NonNull View drawerView) {
//                isOpen=false;
//            }
//
//            @Override
//            public void onDrawerStateChanged(int newState) {
//
//            }
//        });
//    }
//
//    @Override
//    protected int getLayoutID() {
//        return R.layout.activity_about_us;
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.iv_base_back:
//                if (isOpen){
//                    drawerLayout.closeDrawers();
//                }else {
//                    finish();
//                }
//                break;
//            case R.id.iv_base_right:
//                if (isOpen){
//                    drawerLayout.closeDrawers();
//                }else {
//                    drawerLayout.openDrawer(GravityCompat.END);
//                }
//                break;
//        }
//    }

}

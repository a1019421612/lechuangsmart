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
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.adapter.RoomAdapter;
import com.hbdiye.lechuangsmart.adapter.RoomDeviceAdapter;
import com.hbdiye.lechuangsmart.bean.RoomBean;
import com.hbdiye.lechuangsmart.bean.RoomDeviceBean;
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

public class FangjianActivity extends AppCompatActivity {

    @BindView(R.id.iv_base_back)
    ImageView ivBaseBack;
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.iv_base_right)
    ImageView ivBaseRight;
    @BindView(R.id.tv_drawer)
    TextView tvDrawer;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.rv_device_room)
    RecyclerView rvDeviceRoom;
    @BindView(R.id.rv_room)
    RecyclerView rvRoom;
    @BindView(R.id.tv_fangjian_name)
    TextView tvFangjianName;
    private boolean isOpen = false;//默认侧边栏关闭

    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;

    private RoomAdapter roomAdapter;
    private List<RoomBean.Rooms> list_room = new ArrayList<>();

    private RoomDeviceAdapter adapter;
    private List<RoomDeviceBean.Devices> mList=new ArrayList<>();
    private List<Boolean> mList_status=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fangjian);
        ButterKnife.bind(this);
        initView();
        initData();
        handlerClick();
    }

    private void handlerClick() {
        roomAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                tvFangjianName.setText(list_room.get(position).name);
                mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\""+list_room.get(position).id+"\"}");
                drawerLayout.closeDrawers();
            }
        });
    }

    protected void initData() {
        mobilephone = (String) SPUtils.get(this, "mobilephone", "");
        password = (String) SPUtils.get(this, "password", "");
        mConnection = new WebSocketConnection();
        socketConnect();
    }

    private void socketConnect() {
        try {
            mConnection.connect("ws://39.104.105.10:18888/mobilephone=" + mobilephone + "&password=" + password, new MyWebSocketHandler());

        } catch (WebSocketException e) {
            e.printStackTrace();
            SmartToast.show("网络连接错误");
        }
    }

    class MyWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e("TAG", "open");
//            mConnection.sendTextMessage("{\"pn\":\"DGLTP\", \"classify\":\"protype\", \"id\":\"PROTYPE02\"}");
            mConnection.sendTextMessage("{\"pn\":\"RGLTP\"}");
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
            if (payload.contains("\"pn\":\"RGLTP\"")) {
                parseRoomData(payload);
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

    private void parseRoomData(String payload) {
        RoomBean roomBean = new Gson().fromJson(payload, RoomBean.class);
        List<RoomBean.Rooms> rooms = roomBean.rooms;
        if (list_room.size() > 0) {
            list_room.clear();
        }
        list_room.addAll(rooms);
        roomAdapter.notifyDataSetChanged();
        if (list_room.size()>0){
            tvFangjianName.setText(list_room.get(0).name);
            mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\""+list_room.get(0).id+"\"}");
        }
    }

    private void parseData(String payload) {
        RoomDeviceBean roomDeviceBean = new Gson().fromJson(payload, RoomDeviceBean.class);
        List<RoomDeviceBean.Devices> devices = roomDeviceBean.devices;
        if (mList.size()>0){
            mList.clear();
        }
        mList.addAll(devices);
        if (mList_status.size()>0){
            mList_status.clear();
        }
        for (int i = 0; i < devices.size(); i++) {
            mList_status.add(false);
        }
        mConnection.sendTextMessage("{\"pn\":\"SDOSTP\"}");
//        adapter.notifyDataSetChanged();
    }

    private void initView() {
        tvBaseTitle.setText("房间");
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.setFocusableInTouchMode(false);
        ivBaseRight.setVisibility(View.VISIBLE);
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

        LinearLayoutManager manager1=new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.VERTICAL);
        rvDeviceRoom.setLayoutManager(manager1);
        adapter=new RoomDeviceAdapter(mList,mList_status);
        rvDeviceRoom.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRoom.setLayoutManager(manager);
        roomAdapter = new RoomAdapter(list_room);
        rvRoom.setAdapter(roomAdapter);
    }

    @OnClick({R.id.iv_base_back, R.id.iv_base_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_base_back:
                if (isOpen) {
                    drawerLayout.closeDrawers();
                } else {
                    finish();
                }
                break;
            case R.id.iv_base_right:
                if (isOpen) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
                break;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConnection.disconnect();
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
}

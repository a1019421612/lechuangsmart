package com.hbdiye.lechuangsmart.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.SingleWebSocketConnection;
import com.hbdiye.lechuangsmart.adapter.RoomAdapter;
import com.hbdiye.lechuangsmart.adapter.RoomDeviceAdapter;
import com.hbdiye.lechuangsmart.bean.RoomBean;
import com.hbdiye.lechuangsmart.bean.RoomDeviceBean;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hbdiye.lechuangsmart.views.GetGatewayPopwindow;
import com.hbdiye.lechuangsmart.views.GetPhotoPopwindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.tavendo.autobahn.WebSocketConnection;
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
    @BindView(R.id.ll_parent)
    LinearLayout llParent;
    private boolean isOpen = false;//默认侧边栏关闭

    private WebSocketConnection mConnection;
    private HomeReceiver homeReceiver;
    private RoomAdapter roomAdapter;
    private List<RoomBean.Rooms> list_room = new ArrayList<>();

    private RoomDeviceAdapter adapter;
    private List<RoomDeviceBean.Devices> mList = new ArrayList<>();
    private List<Boolean> mList_status = new ArrayList<>();

    private int flagRoomPosition = -1;
    String[] array_place;
    int intoRoomPositon=0;
    private int device_position=-1;
    private String roomId = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fangjian);
        ButterKnife.bind(this);
        initView();
        initData();
        handlerClick();
    }

    private String[] gateway = {};
    private GetGatewayPopwindow getPhotoPopwindow;

    private void handlerClick() {
        roomAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                flagRoomPosition = position;
                tvFangjianName.setText(list_room.get(position).name);
                roomId=list_room.get(position).id;
                mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\"" +roomId + "\"}");
                drawerLayout.closeDrawers();
            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (mList.get(position).product.modelPath.equals("pro_gateway")) {
                    device_position=position;
                    getPhotoPopwindow = new GetGatewayPopwindow(FangjianActivity.this, photoclicer, position,true);
                    getPhotoPopwindow.showPopupWindowBottom(llParent);
                }else {
                    device_position=position;
                    getPhotoPopwindow = new GetGatewayPopwindow(FangjianActivity.this, photoclicer, position,false);
                    getPhotoPopwindow.showPopupWindowBottom(llParent);
                }
                return false;
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                RoomDeviceBean.Devices devices = mList.get(position);
                switch (view.getId()) {
                    case R.id.iv_fj_left:
                        int value_left = devices.deviceAttributes.get(0).value;
                        if (value_left == 0) {
                            //value=0 关
                            String onId = devices.deviceAttributes.get(0).actions.get(0).id;//开id
                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\"" + mList.get(position).id + "\",\"proActID\":\"" + onId + "\",\"param\":\"\"}");
                        } else {
                            //开
                            String offId = devices.deviceAttributes.get(0).actions.get(1).id;//关id
                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\"" + mList.get(position).id + "\",\"proActID\":\"" + offId + "\",\"param\":\"\"}");
                        }
                        break;
                    case R.id.iv_fj_middle:
                        int value_middle = devices.deviceAttributes.get(1).value;
                        if (value_middle == 0) {
                            String onId = devices.deviceAttributes.get(1).actions.get(0).id;//开id
                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\"" + mList.get(position).id + "\",\"proActID\":\"" + onId + "\",\"param\":\"\"}");
                        } else {
                            String offId = devices.deviceAttributes.get(1).actions.get(1).id;//关id
                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\"" + mList.get(position).id + "\",\"proActID\":\"" + offId + "\",\"param\":\"\"}");
                        }
                        break;
                    case R.id.iv_fj_right:
                        int value_right = devices.deviceAttributes.get(2).value;
                        if (value_right == 0) {
                            String onId = devices.deviceAttributes.get(2).actions.get(0).id;//开id
                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\"" + mList.get(position).id + "\",\"proActID\":\"" + onId + "\",\"param\":\"\"}");
                        } else {
                            String offId = devices.deviceAttributes.get(2).actions.get(1).id;//关id
                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\"" + mList.get(position).id + "\",\"proActID\":\"" + offId + "\",\"param\":\"\"}");
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
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mList.get(position).product.modelPath.equals("pro_dispatcher")) {
                    startActivity(new Intent(FangjianActivity.this, YaoKongListActivity.class)
                            .putExtra("deviceID", mList.get(position).id)
                            .putExtra("deviceName", mList.get(position).name)
                            .putExtra("mac", mList.get(position).mac));
                }
            }
        });
    }

    public View.OnClickListener photoclicer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = getPhotoPopwindow.getPosition();
            switch (v.getId()) {
                case R.id.item_popupwindows_camera:
                    //设备入网
                    String message = "{\"pn\":\"GSTP\",\"gatewayMAC\":\"" + mList.get(position).mac + "\",\"time\":\"120\"}";
                    mConnection.sendTextMessage(message);
//                    mConnection.disconnect();
//                    getPhotoPopwindow.dismiss();
                    break;
                case R.id.item_popupwindows_Photo:
                    //调试入网
                    mConnection.sendTextMessage("{\"pn\":\"GSTP\",\"gatewayMAC\":\"" + mList.get(position).mac + "\",\"time\":\"255\"}");
//                    getPhotoPopwindow.dismiss();
                    break;
                case R.id.item_popupwindows_stop:
                    //停止入网
                    mConnection.sendTextMessage("{\"pn\":\"GSTP\",\"time\":\"0\"}");
//                    getPhotoPopwindow.dismiss();
                    break;
                case R.id.item_popupwindows_remove:
                    //移动设备
//                    RoomBean roomBean = new Gson().fromJson(payload, RoomBean.class);
//                    rooms = roomBean.rooms;
//                    list_room
                    if (list_room.size()>0){

                        array_place=new String[list_room.size()];

                        for (int i = 0; i < list_room.size(); i++) {
                            array_place[i]=list_room.get(i).name;
                        }

                        AlertDialog.Builder builder=new AlertDialog.Builder(FangjianActivity.this);
                        builder.setTitle("放置地点");
                        builder.setSingleChoiceItems(array_place, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                intoRoomPositon=which;
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                    SmartToast.show(array_place[intoRoomPositon]);
                                mConnection.sendTextMessage("{\"pn\":\"DDUTP\",\"deviMAC\":\""+mList.get(device_position).mac+"\",\"method\":\"M\",\"deviName\": \"\",\"roomID\": \""+list_room.get(intoRoomPositon).id+"\",\"orderNo\":\"\"}");
                            }
                        });
                        builder.setNegativeButton("取消",null);
                        builder.show();
                    }
                    break;
                case R.id.item_popupwindows_cancel:
                    getPhotoPopwindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    protected void initData() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("DGLTP");
        intentFilter.addAction("ATP");
        intentFilter.addAction("SDOSTP");
        intentFilter.addAction("RGLTP");
        intentFilter.addAction("GSTP");
        intentFilter.addAction("DDUTP");
        homeReceiver = new HomeReceiver();
        registerReceiver(homeReceiver, intentFilter);
        mConnection = SingleWebSocketConnection.getInstance();
        mConnection.sendTextMessage("{\"pn\":\"RGLTP\"}");
    }

    class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String payload = intent.getStringExtra("message");
            if (action.equals("DGLTP")) {
                parseData(payload);
            }
            if (action.equals("ATP")) {
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    String deviceID = jsonObject.getString("deviceID");
                    String proAttID = jsonObject.getString("proAttID");
                    int value = Integer.parseInt(jsonObject.getString("value"));
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).id.equals(deviceID)) {
                            RoomDeviceBean.Devices devices = mList.get(i);
                            List<RoomDeviceBean.Devices.DeviceAttributes> deviceAttributes = devices.deviceAttributes;
                            for (int j = 0; j < deviceAttributes.size(); j++) {
                                if (deviceAttributes.get(j).proAttID.equals(proAttID)) {
//                                    mList.get(i).deviceAttributes.get(j).value;
                                    deviceAttributes.get(j).value = value;
                                    mList.set(i, devices);
                                    adapter.notifyItemChanged(i);
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (action.equals("SDOSTP")) {
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
            if (action.equals("RGLTP")) {
                parseRoomData(payload);
            }
            if (action.equals("GSTP")) {
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    String ecode = jsonObject.getString("ecode");
                    if (ecode.equals("200")) {
                        SmartToast.show("成功");
                        if (getPhotoPopwindow != null) {
                            getPhotoPopwindow.dismiss();
                        }
                    } else if (ecode.equals("304")) {
                        SmartToast.show("网关未在线");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (action.contains("DDUTP")){
                if (payload.contains("\"method\":\"M\"")){
                    //设备移动
                    try {
                        JSONObject jsonObject = new JSONObject(payload);
                        String ecode = jsonObject.getString("ecode");
                        if (ecode.equals("200")){
                            SmartToast.show("操作成功");
                            if (getPhotoPopwindow!=null){
                                getPhotoPopwindow.dismiss();
                            }
                            mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\"" + roomId + "\"}");
                        }else if (ecode.equals("404")){
                            SmartToast.show("设备不存在");
                        }else if (ecode.equals("531")){
                            SmartToast.show("房间不存在");
                        }else if (ecode.equals("801")){
                            SmartToast.show("非法数据");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        JSONObject jsonObject = new JSONObject(payload);
                        String ecode = jsonObject.getString("ecode");
                        if (ecode.equals("200")){
                            SmartToast.show("删除成功");
                            mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\"" + roomId + "\"}");
                        }else if (ecode.equals("404")){
                            SmartToast.show("设备不存在");
                        }else if (ecode.equals("304")){
                            SmartToast.show("网关未在线");
                        }else if (ecode.equals("402")){
                            SmartToast.show("设备删除失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private List<String> mList_a = new ArrayList<>();

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
            if (payload.contains("{\"pn\":\"PRTP\"}")) {
                for (Activity activity : MyApp.mActivitys) {
                    String packageName = activity.getLocalClassName();
                    Log.e("LLL", packageName);
                    mList_a.add(packageName);
                }
                if (mList_a.get(mList_a.size() - 1).equals("FangjianActivity")) {
                    MyApp.finishAllActivity();
                    Intent intent = new Intent(FangjianActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
            if (payload.contains("\"pn\":\"ATP\"")) {
//                    mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\"" + list_room.get(flagRoomPosition).id + "\"}");
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    String deviceID = jsonObject.getString("deviceID");
                    String proAttID = jsonObject.getString("proAttID");
                    int value = Integer.parseInt(jsonObject.getString("value"));
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).id.equals(deviceID)) {
                            RoomDeviceBean.Devices devices = mList.get(i);
                            List<RoomDeviceBean.Devices.DeviceAttributes> deviceAttributes = devices.deviceAttributes;
                            for (int j = 0; j < deviceAttributes.size(); j++) {
                                if (deviceAttributes.get(j).proAttID.equals(proAttID)) {
//                                    mList.get(i).deviceAttributes.get(j).value;
                                    deviceAttributes.get(j).value = value;
                                    mList.set(i, devices);
                                    adapter.notifyItemChanged(i);
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (payload.contains("\"pn\":\"DGLTP\"")) {
                parseData(payload);
            }
            if (payload.contains("\"pn\":\"RGLTP\"")) {
                parseRoomData(payload);
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

    private void parseRoomData(String payload) {
        RoomBean roomBean = new Gson().fromJson(payload, RoomBean.class);
        List<RoomBean.Rooms> rooms = roomBean.rooms;
        if (list_room.size() > 0) {
            list_room.clear();
        }
        list_room.addAll(rooms);
        roomAdapter.notifyDataSetChanged();
        if (list_room.size() > 0) {
            tvFangjianName.setText(list_room.get(0).name);
            roomId=list_room.get(0).id;
            mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\"" + roomId+ "\"}");
        }
    }

    private void parseData(String payload) {
        RoomDeviceBean roomDeviceBean = new Gson().fromJson(payload, RoomDeviceBean.class);
        List<RoomDeviceBean.Devices> devices = roomDeviceBean.devices;
        if (mList.size() > 0) {
            mList.clear();
        }
        mList.addAll(devices);
        if (mList_status.size() > 0) {
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

        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.VERTICAL);
        rvDeviceRoom.setLayoutManager(manager1);
        adapter = new RoomDeviceAdapter(mList, mList_status);
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
        unregisterReceiver(homeReceiver);
    }
}

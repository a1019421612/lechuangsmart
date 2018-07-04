package com.hbdiye.lechuangsmart.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hbdiye.lechuangsmart.Global.CWebSocketHandler;
import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.adapter.RoomDeviceAdapter;
import com.hbdiye.lechuangsmart.adapter.RoomDeviceByIDAdapter;
import com.hbdiye.lechuangsmart.bean.RoomBean;
import com.hbdiye.lechuangsmart.bean.RoomDeviceBean;
import com.hbdiye.lechuangsmart.google.zxing.activity.CaptureActivity;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hbdiye.lechuangsmart.views.SceneDialog;

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

public class RoomActivity extends BaseActivity {

    @BindView(R.id.tv_room_name)
    TextView tvRoomName;
    @BindView(R.id.rv_room_device)
    RecyclerView rvRoomDevice;
    @BindView(R.id.ll_add_roomdevice)
    LinearLayout llAddRoomdevice;

    private String TAG = RoomActivity.class.getSimpleName();
    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;
    private String roomId="";

    private RoomDeviceByIDAdapter adapter;
    private List<RoomDeviceBean.Devices> mList = new ArrayList<>();
    private List<Boolean> mList_status = new ArrayList<>();

    private boolean editStatus = false;//编辑状态标志，默认false

    private SceneDialog sceneDialog;
    private int flag=-1;

    private String erCode="";

    private boolean flag_code=false;

    @Override
    protected void initData() {
        mobilephone = (String) SPUtils.get(this, "mobilephone", "");
        password = (String) SPUtils.get(this, "password", "");
        roomId = getIntent().getStringExtra("roomId");
        mConnection = new WebSocketConnection();
        socketConnect();
    }

    @Override
    protected String getTitleName() {
        return "房间";
    }

    @Override
    protected void initView() {
        ivBaseEdit.setVisibility(View.VISIBLE);
        ivBaseEdit.setImageResource(R.drawable.bianji2);

        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.VERTICAL);
        rvRoomDevice.setLayoutManager(manager1);
        adapter = new RoomDeviceByIDAdapter(mList, mList_status);
        rvRoomDevice.setAdapter(adapter);
        handlerClick();
    }

    private void handlerClick() {
        ivBaseEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editStatus) {
                    ivBaseEdit.setImageResource(R.drawable.bianji2);
                    adapter.sceneStatusChange(editStatus);
                    editStatus = false;
                } else {
                    ivBaseEdit.setImageResource(R.drawable.duigou);
                    adapter.sceneStatusChange(editStatus);
                    editStatus = true;
                }
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                flag = position;
                switch (view.getId()) {
                    case R.id.ll_scene_item_del:
//                        mConnection.sendTextMessage("{\"pn\":\"RDTP\",\"roomID\":\""+mList.get(position).id+"\"}");
                        break;
                    case R.id.ll_scene_item_edt:
                        sceneDialog = new SceneDialog(RoomActivity.this, R.style.MyDialogStyle, dailogClicer, "修改设备名称");
                        sceneDialog.setCanceledOnTouchOutside(true);
                        sceneDialog.show();
                        break;
//                    case R.id.ll_scene_device:
//                        if (!editStatus) {
//                            startActivity(new Intent(FamilyManagerActivity.this, RoomActivity.class).putExtra("roomId", mList.get(position).id));
//                        }
//                        break;
                }
            }
        });
    }

    private View.OnClickListener dailogClicer=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.app_cancle_tv:
                    sceneDialog.dismiss();
                    break;
                case R.id.app_sure_tv:
                    String sceneName = sceneDialog.getSceneName().trim();
                    if (TextUtils.isEmpty(sceneName)) {
                        SmartToast.show("设备名称不能为空");
                    } else {
                        String devicemac = mList.get(flag).mac;
                        mConnection.sendTextMessage("{\"pn\":\"DUTP\",\"deviceMAC\":\""+devicemac+"\",\"newName\": \""+sceneName+"\"}");
                    }
                    break;
            }
        }
    };

    @Override
    protected int getLayoutID() {
        return R.layout.activity_room;
    }

    @OnClick(R.id.ll_add_roomdevice)
    public void onViewClicked() {
        startActivityForResult(new Intent(this, CaptureActivity.class),123);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == 4) {
            flag_code=true;
            erCode = data.getStringExtra("erCode");

//            textview.setText(s);
//            SmartToast.show(erCode);
        }
    }
    class MyWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e(TAG, "open");
            mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\""+roomId+"\"}");
            if (flag_code){
//                mConnection.sendTextMessage("{\"pn\":\"UJFTP\",\"familyID\":\""+ erCode +"\"} ");
                mConnection.sendTextMessage("{{\"pn\":\"SDBTP\",\"roomID\":\""+roomId+"\",\"serialnumber\":\""+erCode+"\"}}");
            }
        }

        @Override
        public void onTextMessage(String payload) {

            Log.e(TAG, "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
            }
            if (payload.contains("{\"pn\":\"PRTP\"}")) {
                for (Activity activity : MyApp.mActivitys) {
                    String packageName = activity.getLocalClassName();
                    Log.e("LLL",packageName);
                }
                MyApp.finishAllActivity();
                Intent intent = new Intent(RoomActivity.this, LoginActivity.class);
                startActivity(intent);
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
            if (payload.contains("\"pn\":\"DUTP\"")){
                //修改设备名称 DUTP
                try {
                    JSONObject jsonObject=new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status){
                        sceneDialog.dismiss();
                        mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\""+roomId+"\"}");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (payload.contains("\"pn\":\"SDBTP\"")){
                //扫描加入家庭
                flag_code=false;
                try {
                    JSONObject jsonObject=new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status){
                        SmartToast.show("成功加入");
                        mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\""+roomId+"\"}");
                    }else {
                        SmartToast.show("加入失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                SmartToast.showLong(payload);
            }
            if (payload.contains("\"pn\":\"ATP\"")){
//                    mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\"" + list_room.get(flagRoomPosition).id + "\"}");
                try {
                    JSONObject jsonObject=new JSONObject(payload);
                    String deviceID = jsonObject.getString("deviceID");
                    String proAttID = jsonObject.getString("proAttID");
                    int value =Integer.parseInt(jsonObject.getString("value"));
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).id.equals(deviceID)){
                            RoomDeviceBean.Devices devices = mList.get(i);
                            List<RoomDeviceBean.Devices.DeviceAttributes> deviceAttributes = devices.deviceAttributes;
                            for (int j = 0; j < deviceAttributes.size(); j++) {
                                if (deviceAttributes.get(j).proAttID.equals(proAttID)){
//                                    mList.get(i).deviceAttributes.get(j).value;
                                    deviceAttributes.get(j).value=value;
                                    mList.set(i,devices);
                                    adapter.notifyItemChanged(i);
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onClose(int code, String reason) {
            Log.e(TAG, "onClose");
        }
    }

    private void parseData(String payload) {
        RoomDeviceBean roomDeviceBean = new Gson().fromJson(payload, RoomDeviceBean.class);
        tvRoomName.setText(roomDeviceBean.room.name);
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

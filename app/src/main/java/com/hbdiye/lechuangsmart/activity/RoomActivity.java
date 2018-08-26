package com.hbdiye.lechuangsmart.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.hbdiye.lechuangsmart.SingleWebSocketConnection;
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
    private HomeReceiver homeReceiver;
    private String roomId = "";

    private RoomDeviceByIDAdapter adapter;
    private List<RoomDeviceBean.Devices> mList = new ArrayList<>();
    private List<Boolean> mList_status = new ArrayList<>();

    private boolean editStatus = false;//编辑状态标志，默认false

    private SceneDialog sceneDialog;
    private int flag = -1;

    private String erCode = "";

    private boolean flag_code = false;

    String[] array_place;
    int intoRoomPositon=0;
    private List<RoomBean.Rooms> rooms=new ArrayList<>();

    private int device_position=-1;

    @Override
    protected void initData() {
        roomId = getIntent().getStringExtra("roomId");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("DGLTP");
        intentFilter.addAction("SDOSTP");
        intentFilter.addAction("DUTP");
        intentFilter.addAction("SDBTP");
        intentFilter.addAction("ATP");
        intentFilter.addAction("DDUTP");
        intentFilter.addAction("RGLTP");
        intentFilter.addAction("DDUTP");
        homeReceiver = new HomeReceiver();
        registerReceiver(homeReceiver, intentFilter);
        mConnection = SingleWebSocketConnection.getInstance();
        mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\"" + roomId + "\"}");
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


        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

//                SPUtils.put(RoomActivity.this,"RoomRgltp",true);
                device_position=position;
                mConnection.sendTextMessage("{\"pn\":\"RGLTP\"}");

                return false;
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!editStatus) {
                    if (mList.get(position).product.modelPath.equals("pro_dispatcher")) {
                        startActivity(new Intent(RoomActivity.this, YaoKongListActivity.class)
                                .putExtra("deviceID", mList.get(position).id)
                                .putExtra("deviceName", mList.get(position).name)
                                .putExtra("mac", mList.get(position).mac));
                    }
                }
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                flag = position;
                RoomDeviceBean.Devices devices = mList.get(position);
                switch (view.getId()) {
                    case R.id.ll_scene_item_del:
                        String mess="{\"pn\":\"DDUTP\",\"deviMAC\":\""+mList.get(position).mac+"\",\"method\":\"Q\"}";
                        mConnection.sendTextMessage("{\"pn\":\"DDUTP\",\"deviMAC\":\""+mList.get(position).mac+"\",\"method\":\"Q\"}");
                        break;
                    case R.id.ll_scene_item_edt:
                        sceneDialog = new SceneDialog(RoomActivity.this, R.style.MyDialogStyle, dailogClicer, "修改设备名称");
                        sceneDialog.setCanceledOnTouchOutside(true);
                        sceneDialog.show();
                        break;
                    case R.id.iv_room_left:
                        if (!editStatus) {
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
                        }
                        break;
                    case R.id.iv_room_middle:
                        if (!editStatus) {
                            int value_middle = devices.deviceAttributes.get(1).value;
                            if (value_middle == 0) {
                                String onId = devices.deviceAttributes.get(1).actions.get(0).id;//开id
                                mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\"" + mList.get(position).id + "\",\"proActID\":\"" + onId + "\",\"param\":\"\"}");
                            } else {
                                String offId = devices.deviceAttributes.get(1).actions.get(1).id;//关id
                                mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\"" + mList.get(position).id + "\",\"proActID\":\"" + offId + "\",\"param\":\"\"}");
                            }
                        }
                        break;
                    case R.id.iv_room_right:
                        if (!editStatus) {
                            int value_right = devices.deviceAttributes.get(2).value;
                            if (value_right == 0) {
                                String onId = devices.deviceAttributes.get(2).actions.get(0).id;//开id
                                mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\"" + mList.get(position).id + "\",\"proActID\":\"" + onId + "\",\"param\":\"\"}");
                            } else {
                                String offId = devices.deviceAttributes.get(2).actions.get(1).id;//关id
                                mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\"" + mList.get(position).id + "\",\"proActID\":\"" + offId + "\",\"param\":\"\"}");
                            }
                        }
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

    private View.OnClickListener dailogClicer = new View.OnClickListener() {
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
                        mConnection.sendTextMessage("{\"pn\":\"DUTP\",\"deviceMAC\":\"" + devicemac + "\",\"newName\": \"" + sceneName + "\"}");
                    }
                    break;
            }
        }
    };
    private View.OnClickListener dialogDeviceCode = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.app_cancle_tv:
                    sceneDialog.dismiss();
                    break;
                case R.id.app_sure_tv:
                    String sceneName = sceneDialog.getSceneName().trim();
                    if (TextUtils.isEmpty(sceneName)) {
                        SmartToast.show("设备标识不能为空");
                    } else {
                        mConnection.sendTextMessage("{\"pn\":\"SDBTP\",\"roomID\":\"" + roomId + "\",\"serialnumber\":\"" + sceneName + "\"}");
                    }
                    break;
            }
        }
    };

    @Override
    protected int getLayoutID() {
        return R.layout.activity_room;
    }

    String[] array_type = {"扫码添加", "手动添加"};
    int type_position;

    @OnClick(R.id.ll_add_roomdevice)
    public void onViewClicked() {
        type_position = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择添加方式");
        builder.setSingleChoiceItems(array_type, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                type_position = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (type_position == 0) {
                    //扫码添加
                    startActivityForResult(new Intent(RoomActivity.this, CaptureActivity.class), 123);
                } else if (type_position == 1) {
                    //手动添加
                    sceneDialog = new SceneDialog(RoomActivity.this, R.style.MyDialogStyle, dialogDeviceCode, "请输入设备标识");
                    sceneDialog.setCanceledOnTouchOutside(true);
                    sceneDialog.show();
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == 4) {
            flag_code = true;
            erCode = data.getStringExtra("erCode");

//            textview.setText(s);
//            SmartToast.show(erCode);
        }
    }

    class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String payload = intent.getStringExtra("message");
            if (action.equals("DGLTP")) {
                parseData(payload);
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
            if (action.equals("DUTP")) {
                //修改设备名称 DUTP
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        sceneDialog.dismiss();
                        mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\"" + roomId + "\"}");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (action.equals("SDBTP")) {
                //扫描加入家庭
                flag_code = false;
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    String stCode = jsonObject.getString("stCode");
                    if (stCode.equals("200")){
                        if (sceneDialog!=null){
                            sceneDialog.dismiss();
                        }
                        SmartToast.show("成功加入");
                        mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\"" + roomId + "\"}");
                    }else if (stCode.equals("401")){
                        SmartToast.show("绑定异常");
                    }else if (stCode.equals("435")){
                        SmartToast.show("设备已绑定");
                    }else if (stCode.equals("6001")){
                        SmartToast.show("设备不存在");
                    }else if (stCode.equals("9999")){
                        SmartToast.show("找不到在线网管");
                    }
                    else {
                        SmartToast.show("加入失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                SmartToast.showLong(payload);
            }
            if (action.equals("ATP")) {
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
            if (action.contains("RGLTP")) {
                roomListData(payload);
            }
            if (action.contains("DDUTP")){
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    String stCode = jsonObject.getString("stCode");
                    if (stCode.equals("200")){
                        SmartToast.show("删除成功");
                        mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\"" + roomId + "\"}");
                    }else if (stCode.equals("404")){
                        SmartToast.show("设备不存在");
                    }else if (stCode.equals("531")){
                        SmartToast.show("房间不存在");
                    }else if (stCode.equals("801")){
                        SmartToast.show("非法数据");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    class MyWebSocketHandler extends WebSocketHandler {
//        @Override
//        public void onOpen() {
//            Log.e(TAG, "open");
//            mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\"" + roomId + "\"}");
//            if (flag_code) {
////                mConnection.sendTextMessage("{\"pn\":\"UJFTP\",\"familyID\":\""+ erCode +"\"} ");
//                mConnection.sendTextMessage("{\"pn\":\"SDBTP\",\"roomID\":\"" + roomId + "\",\"serialnumber\":\"" + erCode + "\"}");
//            }
//        }
//
//        @Override
//        public void onTextMessage(String payload) {
//
//            Log.e(TAG, "onTextMessage" + payload);
//            if (payload.contains("{\"pn\":\"HRQP\"}")) {
//                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
//            }
//            if (payload.contains("{\"pn\":\"PRTP\"}")) {
//                for (Activity activity : MyApp.mActivitys) {
//                    String packageName = activity.getLocalClassName();
//                    Log.e("LLL", packageName);
//                }
//                MyApp.finishAllActivity();
//                Intent intent = new Intent(RoomActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//            if (payload.contains("\"pn\":\"DGLTP\"")) {
//                parseData(payload);
//            }
//            if (payload.contains("\"pn\":\"DDUTP\"")){
//                //删除设备
//                try {
//                    JSONObject jsonObject=new JSONObject(payload);
//                    String stCode = jsonObject.getString("stCode");
//                    if (stCode.equals("200")){
//                        SmartToast.show("删除成功");
//                        mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\"" + roomId + "\"}");
//                    }else if (stCode.equals("304")){
//                        SmartToast.show("网关未在线");
//                    }else if (stCode.equals("402")){
//                        SmartToast.show("设备删除失败");
//                    }else if (stCode.equals("404")){
//                        SmartToast.show("设备不存在");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (payload.contains("\"pn\":\"SDOSTP\"")) {
//                try {
//                    JSONObject jsonObject = new JSONObject(payload);
//                    boolean status = jsonObject.getBoolean("status");
//                    String sdMAC = jsonObject.getString("sdMAC");
//                    //子设备在线
//                    for (int i = 0; i < mList.size(); i++) {
//                        if (mList.get(i).mac.equals(sdMAC)) {
//                            mList_status.set(i, status);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                adapter.notifyDataSetChanged();
//            }
//            if (payload.contains("\"pn\":\"DUTP\"")) {
//                //修改设备名称 DUTP
//                try {
//                    JSONObject jsonObject = new JSONObject(payload);
//                    boolean status = jsonObject.getBoolean("status");
//                    if (status) {
//                        sceneDialog.dismiss();
//                        mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\"" + roomId + "\"}");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (payload.contains("\"pn\":\"SDBTP\"")) {
//                //扫描加入家庭
//                flag_code = false;
//                try {
//                    JSONObject jsonObject = new JSONObject(payload);
//                    boolean status = jsonObject.getBoolean("status");
//                    if (status) {
//                        SmartToast.show("成功加入");
//                        mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\"" + roomId + "\"}");
//                    } else {
//                        SmartToast.show("加入失败");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
////                SmartToast.showLong(payload);
//            }
//            if (payload.contains("\"pn\":\"ATP\"")) {
////                    mConnection.sendTextMessage("{\"pn\":\"DGLTP\",\"classify\":\"room\",\"id\":\"" + list_room.get(flagRoomPosition).id + "\"}");
//                try {
//                    JSONObject jsonObject = new JSONObject(payload);
//                    String deviceID = jsonObject.getString("deviceID");
//                    String proAttID = jsonObject.getString("proAttID");
//                    int value = Integer.parseInt(jsonObject.getString("value"));
//                    for (int i = 0; i < mList.size(); i++) {
//                        if (mList.get(i).id.equals(deviceID)) {
//                            RoomDeviceBean.Devices devices = mList.get(i);
//                            List<RoomDeviceBean.Devices.DeviceAttributes> deviceAttributes = devices.deviceAttributes;
//                            for (int j = 0; j < deviceAttributes.size(); j++) {
//                                if (deviceAttributes.get(j).proAttID.equals(proAttID)) {
////                                    mList.get(i).deviceAttributes.get(j).value;
//                                    deviceAttributes.get(j).value = value;
//                                    mList.set(i, devices);
//                                    adapter.notifyItemChanged(i);
//                                }
//                            }
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (payload.contains("\"pn\":\"RGLTP\"")) {
//                roomListData(payload);
//            }
//
//        }
//
//        @Override
//        public void onClose(int code, String reason) {
//            Log.e(TAG, "onClose");
//        }
//    }

    private void roomListData(String payload) {
        rooms.clear();
        Log.e("ssss",payload);
        RoomBean roomBean = new Gson().fromJson(payload, RoomBean.class);
        rooms = roomBean.rooms;
        if (rooms.size()>0){

            array_place=new String[rooms.size()];

            for (int i = 0; i < rooms.size(); i++) {
                array_place[i]=rooms.get(i).name;
            }

            AlertDialog.Builder builder=new AlertDialog.Builder(RoomActivity.this);
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
                    mConnection.sendTextMessage("{\"pn\":\"DDUTP\",\"deviMAC\":\""+mList.get(device_position).mac+"\",\"method\":\"M\",\"deviName\": \"\",\"roomID\": \""+rooms.get(intoRoomPositon).id+"\",\"orderNo\":\"\"}");
                }
            });
            builder.setNegativeButton("取消",null);
            builder.show();
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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
    }
}

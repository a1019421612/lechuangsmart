package com.hbdiye.lechuangsmart.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.adapter.RoomsManagerAdapter;
import com.hbdiye.lechuangsmart.bean.RoomBean;
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

public class FamilyManagerActivity extends BaseActivity {
    @BindView(R.id.rv_rooms_manager)
    RecyclerView rvRoomsManager;
    @BindView(R.id.tv_addroom)
    LinearLayout tvAddroom;
    private String TAG = FamilyManagerActivity.class.getSimpleName();
    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;

    private RoomsManagerAdapter adapter;
    private List<RoomBean.Rooms> mList = new ArrayList<>();

    private boolean editStatus = false;//编辑状态标志，默认false

    private SceneDialog sceneDialog;
    private int flag = -1;

    @Override
    protected void initData() {
        mobilephone = (String) SPUtils.get(this, "mobilephone", "");
        password = (String) SPUtils.get(this, "password", "");
        mConnection = new WebSocketConnection();
        socketConnect();
    }

    @Override
    protected String getTitleName() {
        return "房间管理";
    }

    @Override
    protected void initView() {
        ivBaseEdit.setVisibility(View.VISIBLE);
        ivBaseEdit.setImageResource(R.drawable.bianji2);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRoomsManager.setLayoutManager(manager);
        adapter = new RoomsManagerAdapter(mList);
        rvRoomsManager.setAdapter(adapter);

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
                        mConnection.sendTextMessage("{\"pn\":\"RDTP\",\"roomID\":\""+mList.get(position).id+"\"}");
                        break;
                    case R.id.ll_scene_item_edt:
                        sceneDialog = new SceneDialog(FamilyManagerActivity.this, R.style.MyDialogStyle, dailogClicer, "修改房间名称");
                        sceneDialog.setCanceledOnTouchOutside(true);
                        sceneDialog.show();
                        break;
                    case R.id.ll_scene_device:
                        if (!editStatus) {
                            startActivity(new Intent(FamilyManagerActivity.this, RoomActivity.class).putExtra("roomId", mList.get(position).id));
                        }
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
                        SmartToast.show("房间名称不能为空");
                    } else {
                        mConnection.sendTextMessage("{\"pn\":\"RUTP\",\"roomID\":\"" + mList.get(flag).id + "\",\"updateType\":\"rn\",\"roomName\":\"" + sceneName + "\"}");
                    }
                    break;
            }
        }
    };

    @Override
    protected int getLayoutID() {
        return R.layout.activity_family_manager;
    }

    @OnClick(R.id.tv_addroom)
    public void onViewClicked() {

        sceneDialog = new SceneDialog(FamilyManagerActivity.this, R.style.MyDialogStyle, addroomClicer, "添加房间名称");
        sceneDialog.setCanceledOnTouchOutside(true);
        sceneDialog.show();
    }

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
    private List<String> mList_a= new ArrayList<>();
    class MyWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e(TAG, "open");
            mConnection.sendTextMessage("{\"pn\":\"RGLTP\"}");
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
                    mList_a.add(packageName);
                }
                if (mList_a.get(mList_a.size()-1).equals("FamilyManagerActivity")){
                    MyApp.finishAllActivity();
                    Intent intent = new Intent(FamilyManagerActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
            if (payload.contains("\"pn\":\"RGLTP\"")) {
                parseData(payload);
            }
            if (payload.contains("\"pn\":\"RUTP\"")) {
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
            if (payload.contains("\"pn\":\"RATP\"")) {
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
            if (payload.contains("\"pn\":\"RDTP\"")){
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

        @Override
        public void onClose(int code, String reason) {
            Log.e(TAG, "onClose");
        }
    }

    private void parseData(String payload) {
        RoomBean roomBean = new Gson().fromJson(payload, RoomBean.class);
        List<RoomBean.Rooms> rooms = roomBean.rooms;
        if (mList.size() > 0) {
            mList.clear();
        }
        mList.addAll(rooms);
        adapter.notifyDataSetChanged();
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

package com.hbdiye.lechuangsmart.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hbdiye.lechuangsmart.MainActivity;
import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.SingleWebSocketConnection;
import com.hbdiye.lechuangsmart.activity.LoginActivity;
import com.hbdiye.lechuangsmart.activity.SceneSettingActivity;
import com.hbdiye.lechuangsmart.adapter.SceneAdapter;
import com.hbdiye.lechuangsmart.bean.SceneBean;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hbdiye.lechuangsmart.views.SceneDialog;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class SceneFragment extends Fragment implements View.OnClickListener {
    private String TAG = SceneFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ImageView iv_status;
    private LinearLayout ll_add_scene;
    SceneAdapter adapter;

    private WebSocketConnection mConnection;
    private HomeReceiver homeReceiver;
    private String mobilephone;
    private String password;

    private List<SceneBean.Scenes> mList = new ArrayList<>();

    private boolean editStatus = false;//编辑状态标志，默认false
    private int flag = -1;//position
    private String sceneName = "";//场景名称
    private boolean isAddScene = true;//添加还是修改场景

    private SceneDialog sceneDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scene, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_scene);
        iv_status = view.findViewById(R.id.iv_scene_edit);
        ll_add_scene = view.findViewById(R.id.ll_add_scene);

//        adapter.expandAll();
        mConnection = SingleWebSocketConnection.getInstance();
        mConnection.sendTextMessage("{\"pn\":\"SLTP\"}");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SLTP");
        intentFilter.addAction("SSTP");
        intentFilter.addAction("SUTP");
        intentFilter.addAction("SDTP");
        intentFilter.addAction("SATP");
        homeReceiver = new HomeReceiver();
        getActivity().registerReceiver(homeReceiver, intentFilter);
//        mobilephone = (String) SPUtils.get(getActivity(), "mobilephone", "");
//        password = (String) SPUtils.get(getActivity(), "password", "");

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        adapter = new SceneAdapter(mList);
        mRecyclerView.setAdapter(adapter);

//        socketConnect();

        handleClick();
        return view;
    }

    private void handleClick() {
        iv_status.setOnClickListener(this);
        ll_add_scene.setOnClickListener(this);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                flag = position;
                switch (view.getId()) {
                    case R.id.ll_scene_item_del:
                        mConnection.sendTextMessage("{\"pn\":\"SDTP\",\"sceneID\":\"" + mList.get(position).id + "\"}");
                        break;
                    case R.id.ll_scene_item_edt:
                        isAddScene = false;
                        sceneDialog = new SceneDialog(getActivity(), R.style.MyDialogStyle, dailogClicer, "修改场景名称");
                        sceneDialog.setCanceledOnTouchOutside(true);
                        sceneDialog.show();
                        break;
                    case R.id.ll_scene_item:
                        if (editStatus) {
                            startActivity(new Intent(getActivity(), SceneSettingActivity.class).putExtra("sceneID", mList.get(position).id));
                        } else {
                            String sceneId = mList.get(position).id;
                            sceneName = mList.get(position).name;
                            mConnection.sendTextMessage("{\"pn\":\"SSTP\",\"sceneID\":\"" + sceneId + "\"}");
                        }
                        break;
                }
            }
        });
    }

//    private void socketConnect() {
//        try {
//            mConnection.connect("ws://39.104.119.0:18888/mobilephone=" + mobilephone + "&password=" + password, new MySceneWebSocketHandler());
//
//        } catch (WebSocketException e) {
//            e.printStackTrace();
//            SmartToast.show("网络连接错误");
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_scene_edit:
                if (editStatus) {
                    iv_status.setImageResource(R.drawable.bianji2);
                    adapter.sceneStatusChange(editStatus);
                    editStatus = false;
                } else {
                    iv_status.setImageResource(R.drawable.duigou);
                    adapter.sceneStatusChange(editStatus);
                    editStatus = true;
                }
                break;
            case R.id.ll_add_scene:
                isAddScene = true;
                sceneDialog = new SceneDialog(getActivity(), R.style.MyDialogStyle, dailogClicer, "添加场景名称");
                sceneDialog.setCanceledOnTouchOutside(true);
                sceneDialog.show();
//                startActivity(new Intent(getActivity(),SceneSettingActivity.class));
                break;
        }
    }

    class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String message = intent.getStringExtra("message");
            if (action.equals("SLTP")) {
                Log.e("bbb", message);
                parseData(message);
            }
            if (action.equals("SSTP")) {
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        SmartToast.show("场景：" + sceneName + "已启用！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (action.equals("SUTP")) {
                //修改场景名称
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        if (sceneDialog != null) {
                            sceneDialog.dismiss();
                        }
                        SmartToast.show("修改成功");
                        mConnection.sendTextMessage("{\"pn\":\"SLTP\"}");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (action.equals("SDTP")) {
                //删除场景
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    boolean status = jsonObject.getBoolean("status");
                    SmartToast.show("删除成功");
                    mConnection.sendTextMessage("{\"pn\":\"SLTP\"}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (action.equals("SATP")) {
                //添加场景
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        sceneDialog.dismiss();
                        SmartToast.show("添加场景成功");
                        mConnection.sendTextMessage("{\"pn\":\"SLTP\"}");
                        JSONObject scene = jsonObject.getJSONObject("scene");
                        String id = scene.getString("id");
                        startActivity(new Intent(getActivity(), SceneSettingActivity.class).putExtra("sceneID", id));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<String> mList_a = new ArrayList<>();

    class MySceneWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e(TAG, "open");
            mConnection.sendTextMessage("{\"pn\":\"SLTP\"}");
        }

        @Override
        public void onTextMessage(String payload) {
            Log.e(TAG, "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
            }
            if (payload.contains("\"pn\":\"SLTP\"")) {
                parseData(payload);
            }
            if (payload.contains("\"pn\":\"SSTP\"")) {
                //场景开启
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        SmartToast.show("场景：" + sceneName + "已启用！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (payload.contains("\"pn\":\"SUTP\"")) {
                //修改场景名称
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        sceneDialog.dismiss();
                        SmartToast.show("修改成功");
                        mConnection.sendTextMessage("{\"pn\":\"SLTP\"}");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (payload.contains("\"pn\":\"SDTP\"")) {
                //删除场景
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    SmartToast.show("删除成功");
                    mConnection.sendTextMessage("{\"pn\":\"SLTP\"}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (payload.contains("\"pn\":\"SATP\"")) {
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        sceneDialog.dismiss();
                        SmartToast.show("添加场景成功");
                        mConnection.sendTextMessage("{\"pn\":\"SLTP\"}");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (payload.contains("{\"pn\":\"PRTP\"}")) {
                for (Activity activity : MyApp.mActivitys) {
                    String packageName = activity.getLocalClassName();
                    Log.e("LLL", packageName);
                    mList_a.add(packageName);
                }
                if (mList_a.contains("MainActivity") && MyApp.mActivitys.size() == 1) {
                    Log.e("LLL", "只有MainActivity");
                    MyApp.finishAllActivity();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else if (mList_a.contains("MainActivity") && mList_a.contains("BetaActivity") && MyApp.mActivitys.size() == 2) {
                    MyApp.finishAllActivity();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("LLL", "多个Activity");
                }
            }
        }

        @Override
        public void onClose(int code, String reason) {
            Log.e(TAG, "onClose");
//            socketConnect();

        }
    }

    private void parseData(String payload) {
        try {
            SceneBean sceneBean = new Gson().fromJson(payload, SceneBean.class);
            if (mList.size() > 0) {
                mList.clear();
            }
            List<SceneBean.Scenes> scenes = sceneBean.scenes;
            mList.addAll(scenes);
            adapter.notifyDataSetChanged();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();

        }
    }

    //    @Override
//    public void onStop() {
//        super.onStop();
//        Log.e("TAG", "onstop");
//        mConnection.disconnect();
//    }
//
    @Override
    public void onResume() {
        super.onResume();
        mConnection.sendTextMessage("{\"pn\":\"SLTP\"}");
    }
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        if (hidden) {
//            // 隐藏
//            Log.e(TAG, "scen" + "隐藏");
//            mConnection.disconnect();
//            boolean connected = mConnection.isConnected();
//            Log.e(TAG, "是否断开连接" + connected);
//        } else {
//            // 可视
//            Log.e(TAG, "scen" + "显示");
//            if (mConnection!=null){
//                socketConnect();
//            }
//        }
//    }

    public View.OnClickListener dailogClicer = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.app_cancle_tv:
                    sceneDialog.dismiss();
                    break;
                case R.id.app_sure_tv:
                    if (isAddScene) {
                        //添加场景
                        String sceneName = sceneDialog.getSceneName();
                        if (TextUtils.isEmpty(sceneName)) {
                            SmartToast.show("场景名称不能为空");
                        } else {
                            mConnection.sendTextMessage("{\"pn\":\"SATP\",\"icon\":\"changjing1\",\"name\":\"" + sceneName + "\"}");
                        }
                    } else {
                        //修改场景
                        String sceneName = sceneDialog.getSceneName();
                        if (TextUtils.isEmpty(sceneName)) {
                            SmartToast.show("场景名称不能为空");
                        } else {
                            mConnection.sendTextMessage("{\"pn\":\"SUTP\",\"sceneID\":\"" + mList.get(flag).id + "\",\"name\":\"" + sceneName + "\"}");
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(homeReceiver);
    }
}

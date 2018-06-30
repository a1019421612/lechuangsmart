package com.hbdiye.lechuangsmart.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hbdiye.lechuangsmart.MainActivity;
import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.activity.DeviceTriggeredActivity;
import com.hbdiye.lechuangsmart.activity.LinkageSettingActivity;
import com.hbdiye.lechuangsmart.activity.LoginActivity;
import com.hbdiye.lechuangsmart.activity.TimeTriggeredActivity;
import com.hbdiye.lechuangsmart.adapter.LinkageAdapter;
import com.hbdiye.lechuangsmart.bean.LinkageBean;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hbdiye.lechuangsmart.views.SceneDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class LinkageFragment extends Fragment {
    private String TAG = LinkageFragment.class.getSimpleName();
    @BindView(R.id.iv_linkage_edit)
    ImageView ivLinkageEdit;
    @BindView(R.id.rv_linkage)
    RecyclerView rvLinkage;
    @BindView(R.id.ll_add_linkage)
    LinearLayout llAddLinkage;
    private Unbinder unbinder;

    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;

    private boolean editStatus = false;//编辑状态标志，默认false
    private int flag = -1;//position

    private List<LinkageBean.Linkages> mList = new ArrayList<>();
    private LinkageAdapter adapter;
    private SceneDialog sceneDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_linkage, container, false);
        unbinder = ButterKnife.bind(this, view);

        mConnection = new WebSocketConnection();
        mobilephone = (String) SPUtils.get(getActivity(), "mobilephone", "");
        password = (String) SPUtils.get(getActivity(), "password", "");

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvLinkage.setLayoutManager(manager);
        adapter = new LinkageAdapter(mList);
        rvLinkage.setAdapter(adapter);

        socketConnect();

        handleClick();
        return view;
    }

    private void handleClick() {
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                flag = position;
                switch (view.getId()) {
                    case R.id.ll_linkage_item:
                        if (editStatus) {
                            String timingID = mList.get(position).timingID + "";
                            startActivity(new Intent(getActivity(), LinkageSettingActivity.class).putExtra("linkageID", mList.get(position).id).putExtra("timingId", timingID));
                        }
                        break;
                    case R.id.ll_linkage_item_del:
                        mConnection.sendTextMessage("{\"pn\":\"LDTP\",\"linkageID\":\"" + mList.get(position).id + "\"}");
                        break;
                    case R.id.ll_linkage_item_edt:
                        sceneDialog = new SceneDialog(getActivity(), R.style.MyDialogStyle, dailogClicer, "修改联动名称");
                        sceneDialog.setCanceledOnTouchOutside(true);
                        sceneDialog.show();
                        break;
//                    case R.id.checkbox_switch:
//                        if (mList.get(position).active == 0) {
//                            mConnection.sendTextMessage("{\"pn\":\"LUTP\",\"linkageID\":\"" + mList.get(position).id + "\",\"active\":" + 1 + "}");
//                        } else {
//                            mConnection.sendTextMessage("{\"pn\":\"LUTP\",\"linkageID\":\"" + mList.get(position).id + "\",\"active\":" + 0 + "}");
//                        }
//                        break;
                    case R.id.iv_switch:
                        if (mList.get(position).active == 0) {
                            mConnection.sendTextMessage("{\"pn\":\"LUTP\",\"linkageID\":\"" + mList.get(position).id + "\",\"active\":" + 1 + "}");
                        } else {
                            mConnection.sendTextMessage("{\"pn\":\"LUTP\",\"linkageID\":\"" + mList.get(position).id + "\",\"active\":" + 0 + "}");
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
                    //修改场景
                    String sceneName = sceneDialog.getSceneName();
                    if (TextUtils.isEmpty(sceneName)) {
                        SmartToast.show("联动名称不能为空");
                    } else {
                        mConnection.sendTextMessage("{\"pn\":\"LUTP\",\"linkageID\":\"" + mList.get(flag).id + "\",\"name\":\"" + sceneName + "\"}");
                    }
                    break;
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_linkage_edit, R.id.ll_add_linkage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_linkage_edit:
                if (editStatus) {
                    ivLinkageEdit.setImageResource(R.drawable.bianji2);
                    adapter.sceneStatusChange(editStatus);
                    editStatus = false;
                } else {
                    ivLinkageEdit.setImageResource(R.drawable.duigou);
                    adapter.sceneStatusChange(editStatus);
                    editStatus = true;
                }
                break;
            case R.id.ll_add_linkage:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("请选择添加何种类型联动");
                builder.setNegativeButton("设备联动", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(), DeviceTriggeredActivity.class));
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("取消", null);
                builder.setNeutralButton("时间联动", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mConnection.sendTextMessage("{\"pn\":\"LATP\",\"timing\":\"00:00\",\"timingType\":2,\"cronExpression\":\"仅一次\",\"name\":\"新联动\"}");
//                        startActivity(new Intent(getActivity(), TimeTriggeredActivity.class));
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
        }
    }

    private void socketConnect() {
        try {
            mConnection.connect("ws://39.104.105.10:18888/mobilephone=" + mobilephone + "&password=" + password, new MyLinkageWebSocketHandler());

        } catch (WebSocketException e) {
            e.printStackTrace();
            SmartToast.show("网络连接错误");
        }
    }
    private List<String> mList_a= new ArrayList<>();
    class MyLinkageWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e(TAG, "open");
            mConnection.sendTextMessage("{\"pn\":\"LLTP\"}");
        }

        @Override
        public void onTextMessage(String payload) {
            Log.e(TAG, "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
            }
            if (payload.contains("\"pn\":\"LLTP\"")) {
                parseData(payload);
            }

            if (payload.contains("\"pn\":\"LUTP\"")) {
//                修改联动名称
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (sceneDialog != null) {
                        sceneDialog.dismiss();
                    }
                    mConnection.sendTextMessage("{\"pn\":\"LLTP\"}");
//                    SmartToast.show("修改成功");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (payload.contains("\"pn\":\"LDTP\"")) {
                //删除联动
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    mConnection.sendTextMessage("{\"pn\":\"LLTP\"}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (payload.contains("\"pn\":\"LATP\"")) {
                //LATP
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        mConnection.sendTextMessage("{\"pn\":\"LLTP\"}");
                        JSONObject linkage = jsonObject.getJSONObject("linkage");
                        String linkageID = linkage.getString("id");
                        String timingID = linkage.getString("timingID");
                        startActivity(new Intent(getActivity(), LinkageSettingActivity.class).putExtra("linkageID", linkageID).putExtra("timingId", timingID));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (payload.contains("{\"pn\":\"PRTP\"}")) {
                for (Activity activity : MyApp.mActivitys) {
                    String packageName = activity.getLocalClassName();
                    Log.e("LLL",packageName);
                    mList_a.add(packageName);
                }
                if (mList_a.contains("MainActivity")&&MyApp.mActivitys.size()==1){
                    Log.e("LLL","只有MainActivity");
                    MyApp.finishAllActivity();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else if (mList_a.contains("MainActivity")&&mList_a.contains("BetaActivity")&&MyApp.mActivitys.size()==2){
                    MyApp.finishAllActivity();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    Log.e("LLL","多个Activity");
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
        LinkageBean linkageBean = null;
        try {
            linkageBean = new Gson().fromJson(payload, LinkageBean.class);
            if (mList.size() > 0) {
                mList.clear();
            }

            List<LinkageBean.Linkages> linkages = linkageBean.linkages;

            mList.addAll(linkages);
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
        Log.e(TAG, "onResume");
        boolean editLinkageName = (boolean) SPUtils.get(getActivity(), "editLinkageName", false);
        if (editLinkageName){
            mConnection.sendTextMessage("{\"pn\":\"LLTP\"}");
            SPUtils.remove(getActivity(),"editLinkageName");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            // 隐藏
            Log.e(TAG, "linkage" + "隐藏");
            mConnection.disconnect();
            boolean connected = mConnection.isConnected();
            Log.e(TAG, "是否断开连接" + connected);
        } else {
            // 可视
            Log.e(TAG, "linkage" + "显示");
            socketConnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mConnection.disconnect();
    }
}

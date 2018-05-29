package com.hbdiye.lechuangsmart.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hbdiye.lechuangsmart.Global.ContentConfig;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.adapter.SceneSettingAdapter;
import com.hbdiye.lechuangsmart.adapter.SceneSettingDeviceAdapter;
import com.hbdiye.lechuangsmart.bean.SceneDeviceBean;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hbdiye.lechuangsmart.views.SceneDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

import static com.hbdiye.lechuangsmart.util.DateUtil.getTime;

public class SceneSettingActivity extends AppCompatActivity {
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
    @BindView(R.id.tv_scene_name)
    TextView tvSceneName;
    @BindView(R.id.iv_scene_edit)
    ImageView ivSceneEdit;
    @BindView(R.id.ll_add_device)
    LinearLayout llAddDevice;
    @BindView(R.id.rv_scene_setting)
    RecyclerView rvSceneSetting;
    @BindView(R.id.rv_scene_device)
    RecyclerView rvSceneDevice;
    private boolean isOpen = false;//默认侧边栏关闭
    private String sceneID = "";

    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;

    private SceneSettingAdapter adapter;
    private List<SceneDeviceBean.SceneTasks> mList = new ArrayList<>();

    private SceneSettingDeviceAdapter mAdapter;
    private List<SceneDeviceBean.Devices> mList_device = new ArrayList<>();

    private TimePickerView timePickerView;
    private OptionsPickerView pickerBuilder;
    private int timeFlag = -1;//position

    private SceneDialog sceneDialog;
    private SceneDeviceBean sceneDeviceBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_setting);
        ButterKnife.bind(this);
        initView();
        initData();
        initCustomTimePicker();
        handleClick();
    }

    private void initCustomTimePicker() {
        pickerBuilder = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                tvBaseTitle.setText(options1+"时"+options2+"分"+options3+"秒");
                int i = options1 * 3600 + options2 * 60 + options3;
//                tvBaseTitle.setText(ContentConfig.secToTime(options1*3600+options2*60+options3));
                mConnection.sendTextMessage("{\"pn\":\"STUTP\",\"stID\":\"" + mList.get(timeFlag).id + "\",\"proActID\":\"" + mList.get(timeFlag).proActID + "\",\"delaytime\":\"" + i + "\"}");
            }
        }).setLabels("时", "分", "秒")
                .isCenterLabel(true)
                .build();
        pickerBuilder.setNPicker(ContentConfig.getTimeHours(), ContentConfig.getTimeMin(), ContentConfig.getTimeSeco());
    }

    private void handleClick() {
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                timeFlag = position;
                switch (view.getId()) {
                    case R.id.tv_scene_setting_time:
                        if (pickerBuilder != null) {
                            pickerBuilder.show();
                        }
                        break;
                    case R.id.iv_scene_setting_del:
                        AlertDialog.Builder builder = new AlertDialog.Builder(SceneSettingActivity.this);
                        builder.setMessage("确认删除？");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mConnection.sendTextMessage("{\"pn\":\"STDTP\",\"stID\":\"" + mList.get(position).id + "\"}");
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.show();
                        break;
                    case R.id.tv_scene_setting_switch:
                        final List<SceneDeviceBean.SceneTasks.ProActs> proActs = mList.get(position).proActs;
//                        String[] proActs_id = new String[proActs.size()];
                        String proActID = mList.get(position).proActID;
                        String[] proActs_name = new String[proActs.size()];
                        int dialogflag=-1;
                        for (int i = 0; i < proActs.size(); i++) {
                            proActs_name[i]=proActs.get(i).name;
                            if (proActID.equals(proActs.get(i).id)){
                                dialogflag=i;
                            }
                        }

                        AlertDialog.Builder proActs_list=new AlertDialog.Builder(SceneSettingActivity.this);
                        proActs_list.setSingleChoiceItems(proActs_name, dialogflag, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SmartToast.show(proActs.get(which).name);
                                dialog.dismiss();
                            }
                        });
                        proActs_list.show();
                        break;
                }
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mConnection.sendTextMessage("{\"pn\":\"STATP\",\"sceneID\":\"" + sceneID + "\",\"deviceID\":\"" + sceneDeviceBean.devices.get(position).id + "\",\"proActID\":\"" + sceneDeviceBean.devices.get(position).product.proacts.get(0).id + "\",\"type\":\"0\",\"delaytime\":\"0\"}");
            }
        });
    }

    private void initData() {
        sceneID = getIntent().getStringExtra("sceneID");
        mobilephone = (String) SPUtils.get(this, "mobilephone", "");
        password = (String) SPUtils.get(this, "password", "");
        mConnection = new WebSocketConnection();
        socketConnection();
    }

    private void initView() {
        tvBaseTitle.setText("场景设置");
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

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        rvSceneSetting.setLayoutManager(manager);
        adapter = new SceneSettingAdapter(mList);
        rvSceneSetting.setAdapter(adapter);

        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.VERTICAL);
        rvSceneDevice.setLayoutManager(manager1);
        mAdapter = new SceneSettingDeviceAdapter(mList_device);
        rvSceneDevice.setAdapter(mAdapter);
    }

    @OnClick({R.id.iv_base_back, R.id.iv_base_right, R.id.iv_scene_edit, R.id.ll_add_device})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_scene_edit:
                sceneDialog = new SceneDialog(this, R.style.MyDialogStyle, dailogClicer, "修改场景名称");
                sceneDialog.setCanceledOnTouchOutside(true);
                sceneDialog.show();
                break;
            case R.id.ll_add_device:
                if (isOpen) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
                break;
            case R.id.iv_base_back:
                if (isOpen) {
                    drawerLayout.closeDrawers();
                } else {
                    finish();
                }
                break;
        }
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
                        SmartToast.show("场景名称不能为空");
                    } else {
                        mConnection.sendTextMessage("{\"pn\":\"SUTP\",\"sceneID\":\"" + sceneID + "\",\"icon\":\"changjing1.png\",\"name\":\"" + sceneName + "\"}");
                    }
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (!isOpen) {
            super.onBackPressed();
            return;
        } else {
            drawerLayout.closeDrawers();
        }
    }

    class MyWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e("TAG", "open");
            mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
        }

        @Override
        public void onTextMessage(String payload) {
            Log.e("TAG", "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
            }
            if (payload.contains("\"pn\":\"STLTP\"")) {
                parseData(payload);
            }
            if (payload.contains("\"pn\":\"SUTP\"")) {
                //修改场景名称
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        sceneDialog.dismiss();
                        SmartToast.show("修改成功");
                        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (payload.contains("\"pn\":\"STATP\"")) {
                //添加设备
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
                        drawerLayout.closeDrawers();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (payload.contains("\"pn\":\"STUTP\"")) {
                //设置延时STUTP
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (payload.contains("\"pn\":\"STDTP\"")) {
                //删除设备STDTP
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onClose(int code, String reason) {
            Log.e("TAG", "onClose");
        }
    }

    private void parseData(String payload) {
        try {
            sceneDeviceBean = new Gson().fromJson(payload, SceneDeviceBean.class);
            tvSceneName.setText(sceneDeviceBean.scene.name);
            List<SceneDeviceBean.SceneTasks> sceneTasks = sceneDeviceBean.sceneTasks;
            List<SceneDeviceBean.Devices> devices = sceneDeviceBean.devices;
            if (mList.size() > 0) {
                mList.clear();
            }
            if (mList_device.size() > 0) {
                mList_device.clear();
            }
            mList.addAll(sceneTasks);
            mList_device.addAll(devices);
            adapter.notifyDataSetChanged();
            mAdapter.notifyDataSetChanged();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("TAG", "onstop");
        mConnection.disconnect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("TAG", "onrestart");
        if (mConnection != null) {
            socketConnection();
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
    protected void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "onstop");
        mConnection.disconnect();
    }

}

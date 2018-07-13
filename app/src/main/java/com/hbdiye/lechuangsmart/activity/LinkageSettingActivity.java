package com.hbdiye.lechuangsmart.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hbdiye.lechuangsmart.Global.ContentConfig;
import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.SingleWebSocketConnection;
import com.hbdiye.lechuangsmart.adapter.LinkageDeviceMenuAdapter;
import com.hbdiye.lechuangsmart.adapter.LinkageSettingAdapter;
import com.hbdiye.lechuangsmart.adapter.LinkageSettingDeviceAdapter;
import com.hbdiye.lechuangsmart.adapter.LinkageYaoKongQiListAdapter;
import com.hbdiye.lechuangsmart.bean.LinkageDeviceBean;
import com.hbdiye.lechuangsmart.bean.LinkageSettingBean;
import com.hbdiye.lechuangsmart.bean.YaoKongListBean;
import com.hbdiye.lechuangsmart.util.Logger;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hbdiye.lechuangsmart.views.SceneDialog;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.hzy.tvmao.ir.Device;
import com.kookong.app.data.AppConst;
import com.kookong.app.data.IrData;
import com.kookong.app.data.IrDataList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

import static com.hzy.tvmao.ir.Device.AC;
import static com.hzy.tvmao.ir.Device.AIR_CLEANER;
import static com.hzy.tvmao.ir.Device.BOX;
import static com.hzy.tvmao.ir.Device.DVD;
import static com.hzy.tvmao.ir.Device.FAN;
import static com.hzy.tvmao.ir.Device.PA;
import static com.hzy.tvmao.ir.Device.PRO;
import static com.hzy.tvmao.ir.Device.SLR;
import static com.hzy.tvmao.ir.Device.STB;
import static com.hzy.tvmao.ir.Device.TV;

public class LinkageSettingActivity extends AppCompatActivity {
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
    @BindView(R.id.tv_linkage_name)
    TextView tvLinkageName;
    @BindView(R.id.iv_linkage_edit)
    ImageView ivLinkageEdit;
    @BindView(R.id.rv_linkage_setting)
    RecyclerView rvLinkageSetting;
    @BindView(R.id.ll_add_device)
    LinearLayout llAddDevice;
    @BindView(R.id.rv_linkage_device)
    RecyclerView rvLinkageDevice;
    @BindView(R.id.rv_hongwai_device)
    RecyclerView rvHongwaiDevice;
    @BindView(R.id.iv_device_back)
    ImageView ivDeviceBack;
    @BindView(R.id.rv_device_menu)
    RecyclerView rvDeviceMenu;
    private boolean isOpen = false;//默认侧边栏关闭

    private WebSocketConnection mConnection;
    private HomeReceiver homeReceiver;
//    private String mobilephone;
//    private String password;

    private String linkageID = "";

    private List<LinkageDeviceBean.Devices> mList_device = new ArrayList<>();
    private LinkageSettingDeviceAdapter mAdapter;

    private List<LinkageSettingBean.Lts> mList = new ArrayList<>();
    private LinkageSettingAdapter adapter;
    //遥控列表
    private List<YaoKongListBean.Irremotes> mList_yk = new ArrayList<>();
    private LinkageYaoKongQiListAdapter adapter_yk;

    //遥控按钮
    private ArrayList<IrData.IrKey> mList_menu=new ArrayList<>();
    private LinkageDeviceMenuAdapter adapter_menu;

    //头布局 时间联动
    private TextView tv_time_mode, tv_time_timing;
    private LinearLayout ll_dingshi;
    //头布局 设备联动
    private TextView tv_device_name, tv_device_cfgn, tv_device_cftj;
    private LinearLayout ll_device;

    private SceneDialog sceneDialog;

    private OptionsPickerView pickerBuilder;
    private int timeFlag = -1;//position

    private int dialogflag;//开关列表标志
    private LinkageSettingBean.Linkage linkage;
    private String timingId = "";
    private ImageView iv_header;

    private int level=0;
    private LinkageDeviceBean.Devices devices;
    private YaoKongListBean.Irremotes irremotes;
    private String rcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkage_setting);
        ButterKnife.bind(this);
        timingId = getIntent().getStringExtra("timingId");
        initView();
        initCustomTimePicker();
        initData();
        handleClick();
    }

    private void handleClick() {
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                timeFlag = position;
                switch (view.getId()) {
                    case R.id.tv_scene_setting_time:
                        //延时
                        if (pickerBuilder != null) {
                            pickerBuilder.show();
                        }
                        break;
                    case R.id.iv_scene_setting_del:
                        mConnection.sendTextMessage("{\"pn\":\"LTDTP\",\"ltID\":\"" + mList.get(position).id + "\"}");
                        //删除
                        break;
                    case R.id.tv_scene_setting_switch:
                        //开关
                        dialogflag = -1;
                        List<LinkageSettingBean.Lts.ProActs> proActs = mList.get(position).proActs;
                        String proActID = mList.get(position).proActID;
                        String[] proActs_name = new String[proActs.size()];
                        for (int i = 0; i < proActs.size(); i++) {
                            proActs_name[i] = proActs.get(i).name;
                            if (proActID.equals(proActs.get(i).id)) {
                                dialogflag = i;
                            }
                        }
                        AlertDialog.Builder proActs_list = new AlertDialog.Builder(LinkageSettingActivity.this);
                        proActs_list.setSingleChoiceItems(proActs_name, dialogflag, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                SmartToast.show(proActs.get(which).name);
                                if (which != dialogflag) {
                                    mConnection.sendTextMessage("{\"pn\":\"LTUTP\",\"ltID\":\"" + mList.get(position).id + "\",\"proActID\":\"" + mList.get(position).proActs.get(which).id + "\",\"delaytime\":\"" + mList.get(position).delaytime + "\"}");
                                }
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
                devices = mList_device.get(position);
                if (mList_device.get(position).product.modelPath.equals("pro_dispatcher")) {
                    level=1;
                    mList_yk.clear();
                    adapter_yk.notifyDataSetChanged();
                    rvLinkageDevice.setVisibility(View.GONE);
                    ivDeviceBack.setVisibility(View.VISIBLE);
                    rvHongwaiDevice.setVisibility(View.VISIBLE);
                    mConnection.sendTextMessage("{\"pn\":\"IRLTP\",\"deviceID\":\"" + mList_device.get(position).id + "\"}");
                } else {
                    mConnection.sendTextMessage("{\"pn\":\"LTATP\",\"linkageID\":\"" + linkageID + "\",\"deviceID\":\"" + mList_device.get(position).id + "\",\"proActID\":\"" + mList_device.get(position).product.proacts.get(0).id + "\",\"type\":\"0\",\"delaytime\":\"0\"}");
                }
            }
        });
        //遥控列表
        adapter_yk.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                level=2;
                mList_menu.clear();
                adapter_menu.notifyDataSetChanged();
                ivDeviceBack.setVisibility(View.VISIBLE);
                rvLinkageDevice.setVisibility(View.GONE);
                rvHongwaiDevice.setVisibility(View.GONE);
                rvDeviceMenu.setVisibility(View.VISIBLE);
                irremotes = mList_yk.get(position);
                String rtype = mList_yk.get(position).rtype;
                String rid = mList_yk.get(position).rid;
                if (rtype.equals("TV")) {
                    getIRDataById(rid,TV);
                }else if (rtype.equals("BOX")){
                    getIRDataById(rid,BOX);
                }else if (rtype.equals("STB")){
                    getIRDataById(rid,STB);
                }else if (rtype.equals("DVD")){
                    getIRDataById(rid,DVD);
                }else if (rtype.equals("AC")){
                    getNoStateIRDataById(rid,AC);
                }else if (rtype.equals("PRO")){
                    getIRDataById(rid,PRO);
                }else if (rtype.equals("PA")){
                    getIRDataById(rid,PA);
                }else if (rtype.equals("FAN")){
                    getIRDataById(rid,FAN);
                }else if (rtype.equals("SLR")){
                    getIRDataById(rid,SLR);
                }else if (rtype.equals("AIR_CLEANER")){
                    getIRDataById(rid,AIR_CLEANER);
                }
            }
        });
        adapter_menu.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String data="{\"pn\":\"LTATP\",\"linkageID\":\""+linkageID+"\",\"deviceID\":\""+devices.id+"\",\"proActID\":\""+devices.product.proacts.get(0).id+"\",\"param\":\""+irremotes.id+"\",\"type\":\"1\",\"delaytime\":null,\"rcode\":\""+rcode+"\",\"fpulse\":\""+mList_menu.get(position).pulse.replace(" ", "").replace(",", "")+"\",\"irremoteid\":\""+irremotes.id+"\",\"proname\":\""+mList_menu.get(position).fname+"\"}";
                Log.e("TTT",data);
                mConnection.sendTextMessage(data);
//                mConnection.sendTextMessage("{\"pn\":\"LTATP\",\"linkageID\":\""+linkageID+"\",\"deviceID\":\""+devices.id+"\",\"proActID\":\""+devices.product.proacts.get(0).id+"\",\"param\":\""+irremotes.id+"\",\"type\":\"1\",\"delaytime\":null,\"rcode\":\""+mList_menu.get(0).exts.get(99999)+"\",\"fpulse\":\""+mList_menu.get(position).pulse+"\",\"irremoteid\":\""+irremotes.id+"\",\"proname\":\"%@\"}");
                drawerLayout.closeDrawers();
            }
        });
    }

    /**
     * 电视
     */
    private void getIRDataById(String rid,int type) {
        KookongSDK.getIRDataById(rid, type, true, new IRequestResult<IrDataList>() {

            @Override
            public void onSuccess(String msg, IrDataList result) {
                List<IrData> irDatas = result.getIrDataList();
                rcode = irDatas.get(0).exts.get(99999);
                ArrayList<IrData.IrKey> keys = irDatas.get(0).keys;

                if (mList_menu.size() > 0) {
                    mList_menu.clear();
                }
                mList_menu.addAll(keys);
                adapter_menu.notifyDataSetChanged();
            }

            @Override
            public void onFail(Integer errorCode, String msg) {
                //按红外设备授权的客户，才会用到这两个值
                if (errorCode == AppConst.CUSTOMER_DEVICE_REMOTE_NUM_LIMIT) {//同一个设备下载遥控器超过了50套限制
                    msg = "下载的遥控器超过了套数限制";
                } else if (errorCode == AppConst.CUSTOMER_DEVICE_NUM_LIMIT) {//设备总数超过了授权的额度
                    msg = "设备总数超过了授权的额度";
                }
                SmartToast.show(msg);

            }
        });
    }

    /**
     * 空调
     */
    public void getNoStateIRDataById(String rid,int type){
        KookongSDK.getNoStateIRDataById(rid + "", type, true, new IRequestResult<IrDataList>() {

            @Override
            public void onSuccess(String msg, IrDataList result) {
                List<IrData> irDatas = result.getIrDataList();
                for (int i = 0; i < irDatas.size(); i++) {
                    IrData irData = irDatas.get(i);
                    Logger.d("空调：" + irData.rid);
                    //空调支持的模式、温度、风速
                    HashMap<Integer, String> exts = irData.exts;
                    rcode = exts.get(99999);
                    //遥控器参数
                    //空调的组合按键
                    ArrayList<IrData.IrKey> keyList = irData.keys;
                    if (mList_menu.size() > 0) {
                        mList_menu.clear();
                    }

                    String keySize = irData.rid + "的组合按键个数：" + (keyList == null ? "0" : keyList.size()) + "\n";
                    Logger.d(keySize);
                    if (keyList != null) {
                        IrData.IrKey irKey = keyList.get(0);
                        Logger.d("按键参数：" + irKey.fkey + "=" + irKey.pulse);
                        IrData.IrKey irKey1 = keyList.get(1);
                        Logger.d("按键参数：" + irKey1.fkey + "=" + irKey1.pulse);
                        IrData.IrKey irKey2 = keyList.get(keyList.size() - 1);
                        Logger.d("按键参数：" + irKey2.fkey + "=" + irKey2.pulse);
                        String power_on = irKey.pulse.replace(" ", "").replace(",", "");
                        String power_off = irKey2.pulse.replace(" ", "").replace(",", "");

                        String default_fpulse = irKey1.pulse.replace(" ", "").replace(",", "");
//                        String data = "{\"pn\":\"IRTP\", \"sdMAC\":\"" + mac + "\", \"rcode\":\"" + rcode + "\",\"fpulse\":\"" + replace + "\"}}";
//                        mConnection.sendTextMessage(data);
                        IrData.IrKey irdata_irkey=new IrData.IrKey();
                        irdata_irkey.fname="开";
                        irdata_irkey.pulse=power_on;
                        IrData.IrKey irdata_irkey1=new IrData.IrKey();
                        irdata_irkey1.fname="关";
                        irdata_irkey1.pulse=power_off;
                        mList_menu.add(irdata_irkey);
                        mList_menu.add(irdata_irkey1);
                    }

                    adapter_menu.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(Integer errorCode, String msg) {
                //按红外设备授权的客户，才会用到这两个值
                if (errorCode == AppConst.CUSTOMER_DEVICE_REMOTE_NUM_LIMIT) {//同一个设备下载遥控器超过了50套限制
                    msg = "下载的遥控器超过了套数限制";
                } else if (errorCode == AppConst.CUSTOMER_DEVICE_NUM_LIMIT) {//设备总数超过了授权的额度
                    msg = "设备总数超过了授权的额度";
                }
                SmartToast.show(msg);
            }

        });
    }
    private void initCustomTimePicker() {
        pickerBuilder = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                tvBaseTitle.setText(options1+"时"+options2+"分"+options3+"秒");
                int i = options1 * 3600 + options2 * 60 + options3;
//                tvBaseTitle.setText(ContentConfig.secToTime(options1*3600+options2*60+options3));
                mConnection.sendTextMessage("{\"pn\":\"LTUTP\",\"ltID\":\"" + mList.get(timeFlag).id + "\",\"proActID\":\"" + mList.get(timeFlag).proActID + "\",\"delaytime\":\"" + i + "\"}");
            }
        }).setLabels("时", "分", "秒")
                .isCenterLabel(true)
                .build();
        pickerBuilder.setNPicker(ContentConfig.getTimeHours(), ContentConfig.getTimeMin(), ContentConfig.getTimeSeco());
    }

    private void initData() {
        linkageID = getIntent().getStringExtra("linkageID");

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("LCTP");
        intentFilter.addAction("LDLTP");
        intentFilter.addAction("IRLTP");
        intentFilter.addAction("LUTP");
        intentFilter.addAction("LTDTP");
        intentFilter.addAction("LTUTP");
        intentFilter.addAction("LTATP");
        homeReceiver = new HomeReceiver();
        registerReceiver(homeReceiver,intentFilter);
        mConnection = SingleWebSocketConnection.getInstance();
        mConnection.sendTextMessage("{\"pn\":\"LCTP\",\"linkageID\":\"" + linkageID + "\"}");
        mConnection.sendTextMessage("{\"pn\":\"LDLTP\",\"type\":1}");
//        socketConnection();
    }

    private void initView() {
        tvBaseTitle.setText("联动设置");
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
        rvLinkageDevice.setLayoutManager(manager);
        mAdapter = new LinkageSettingDeviceAdapter(mList_device);
        rvLinkageDevice.setAdapter(mAdapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvLinkageSetting.setLayoutManager(layoutManager);
        adapter = new LinkageSettingAdapter(mList);
        rvLinkageSetting.setAdapter(adapter);

        LinearLayoutManager manager_yk = new LinearLayoutManager(this);
        manager_yk.setOrientation(LinearLayoutManager.VERTICAL);
        rvHongwaiDevice.setLayoutManager(manager_yk);
        adapter_yk = new LinkageYaoKongQiListAdapter(mList_yk);
        rvHongwaiDevice.setAdapter(adapter_yk);


        LinearLayoutManager manager_menu=new LinearLayoutManager(this);
        manager_menu.setOrientation(LinearLayoutManager.VERTICAL);
        rvDeviceMenu.setLayoutManager(manager_menu);
        adapter_menu=new LinkageDeviceMenuAdapter(mList_menu);
        rvDeviceMenu.setAdapter(adapter_menu);

        if (timingId.equals("null")) {
            //设备联动
            addDeviceHeader();
        } else {
            //时间联动
            addTimeHeader();
        }
    }

    private void addDeviceHeader() {
        View view = getLayoutInflater().inflate(R.layout.device_linkage_head, (ViewGroup) rvLinkageSetting.getParent(), false);
        adapter.addHeaderView(view);
        tv_device_name = view.findViewById(R.id.tv_device_linkage_name);
        tv_device_cfgn = view.findViewById(R.id.tv_chufagn);
        tv_device_cftj = view.findViewById(R.id.tv_chufatj);
        ll_device = view.findViewById(R.id.ll_device);
        iv_header = view.findViewById(R.id.iv_header);
        ll_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LinkageSettingActivity.this, DeviceTriggeredActivity.class).putExtra("LinkageData", linkage).putExtra("linkageID", linkageID));
            }
        });
    }

    private void addTimeHeader() {
        View view = getLayoutInflater().inflate(R.layout.time_linkage_head, (ViewGroup) rvLinkageSetting.getParent(), false);
        adapter.addHeaderView(view);
        tv_time_mode = view.findViewById(R.id.tv_linkage_time_mode);
        tv_time_timing = view.findViewById(R.id.tv_linkage_setting_time);
        ll_dingshi = view.findViewById(R.id.ll_dingshi);
        ll_dingshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LinkageSettingActivity.this, TimeTriggeredActivity.class).putExtra("LinkageData", linkage));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
    }

    @OnClick({R.id.iv_base_back, R.id.iv_linkage_edit, R.id.ll_add_device,R.id.iv_device_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_linkage_edit:
                sceneDialog = new SceneDialog(this, R.style.MyDialogStyle, dailogClicer, "修改联动名称");
                sceneDialog.setCanceledOnTouchOutside(true);
                sceneDialog.show();
                break;
            case R.id.ll_add_device:
                if (isOpen) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                    level=0;
                    ivDeviceBack.setVisibility(View.INVISIBLE);
                    rvLinkageDevice.setVisibility(View.VISIBLE);
                    rvHongwaiDevice.setVisibility(View.GONE);
                    rvDeviceMenu.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_base_back:
                if (isOpen) {
                    drawerLayout.closeDrawers();
                } else {
                    finish();
                }
                break;
            case R.id.iv_device_back:
                if (level==2){
                    rvDeviceMenu.setVisibility(View.GONE);
                    rvHongwaiDevice.setVisibility(View.VISIBLE);
                    level=1;
                }else if (level==1){
                    rvHongwaiDevice.setVisibility(View.GONE);
                    rvLinkageDevice.setVisibility(View.VISIBLE);
                    ivDeviceBack.setVisibility(View.INVISIBLE);
                    level=0;
                }
                break;
        }
    }

    private View.OnClickListener dailogClicer = new View.OnClickListener() {
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
                        mConnection.sendTextMessage("{\"pn\":\"LUTP\",\"linkageID\":\"" + linkageID + "\",\"name\":\"" + sceneName + "\"}");
                    }
                    break;
            }
        }
    };
    class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String payload = intent.getStringExtra("message");
            if (action.equals("LLTP")){
                parseData(payload);
            }if (action.equals("LCTP")) {
                parseData(payload);
            }
            if (action.equals("LDLTP")) {
                //设备列表
                parseDeviceData(payload);
            }
            if (action.equals("IRLTP")) {
                parseDataHW(payload);
            }
            if (action.equals("LUTP")) {
//                修改联动名称
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        if (sceneDialog != null) {
                            sceneDialog.dismiss();
                        }
                    }
//                    SmartToast.show("修改成功");
                    SPUtils.put(LinkageSettingActivity.this, "editLinkageName", true);
                    mConnection.sendTextMessage("{\"pn\":\"LCTP\",\"linkageID\":\"" + linkageID + "\"}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (action.equals("LTDTP")) {
//                删除联动设备 LTDTP
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        SmartToast.show("修改成功");
                        mConnection.sendTextMessage("{\"pn\":\"LCTP\",\"linkageID\":\"" + linkageID + "\"}");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (action.equals("LTUTP")) {
//                延时时间修改 LTUTP
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        SmartToast.show("修改成功");
                        mConnection.sendTextMessage("{\"pn\":\"LCTP\",\"linkageID\":\"" + linkageID + "\"}");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (action.equals("LTATP")) {
//                联动添加设备 LTATP
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        mConnection.sendTextMessage("{\"pn\":\"LCTP\",\"linkageID\":\"" + linkageID + "\"}");
                        drawerLayout.closeDrawers();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private List<String> mList_a = new ArrayList<>();

    class MyWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e("TAG", "open");
            mConnection.sendTextMessage("{\"pn\":\"LCTP\",\"linkageID\":\"" + linkageID + "\"}");
            mConnection.sendTextMessage("{\"pn\":\"LDLTP\",\"type\":1}");
        }

        @Override
        public void onTextMessage(String payload) {
            Log.e("TAG", "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");

            }
            if (payload.contains("{\"pn\":\"PRTP\"}")) {
                for (Activity activity : MyApp.mActivitys) {
                    String packageName = activity.getLocalClassName();
                    Log.e("LLL", packageName);
                    mList_a.add(packageName);
                }
                if (mList_a.get(mList_a.size() - 1).equals("LinkageSettingActivity")) {
                    MyApp.finishAllActivity();
                    Intent intent = new Intent(LinkageSettingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
            if (payload.contains("\"pn\":\"LCTP\"")) {
                parseData(payload);
            }
            if (payload.contains("\"pn\":\"LDLTP\"")) {
                //设备列表
                parseDeviceData(payload);
            }
            if (payload.contains("\"pn\":\"IRLTP\"")) {
                parseDataHW(payload);
            }
            if (payload.contains("\"pn\":\"LUTP\"")) {
//                修改联动名称
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        if (sceneDialog != null) {
                            sceneDialog.dismiss();
                        }
                    }
//                    SmartToast.show("修改成功");
                    SPUtils.put(LinkageSettingActivity.this, "editLinkageName", true);
                    mConnection.sendTextMessage("{\"pn\":\"LCTP\",\"linkageID\":\"" + linkageID + "\"}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (payload.contains("\"pn\":\"LTDTP\"")) {
//                删除联动设备 LTDTP
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        SmartToast.show("修改成功");
                        mConnection.sendTextMessage("{\"pn\":\"LCTP\",\"linkageID\":\"" + linkageID + "\"}");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (payload.contains("\"pn\":\"LTUTP\"")) {
//                延时时间修改 LTUTP
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        SmartToast.show("修改成功");
                        mConnection.sendTextMessage("{\"pn\":\"LCTP\",\"linkageID\":\"" + linkageID + "\"}");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (payload.contains("\"pn\":\"LTATP\"")) {
//                联动添加设备 LTATP
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        mConnection.sendTextMessage("{\"pn\":\"LCTP\",\"linkageID\":\"" + linkageID + "\"}");
                        drawerLayout.closeDrawers();
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

    private void parseDataHW(String payload) {
        YaoKongListBean yaoKongListBean = new Gson().fromJson(payload, YaoKongListBean.class);
        List<YaoKongListBean.Irremotes> irremotes = yaoKongListBean.irremotes;
        if (mList_yk.size() > 0) {
            mList_yk.clear();
        }
        mList_yk.addAll(irremotes);
        adapter_yk.notifyDataSetChanged();
    }

    private void parseDeviceData(String payload) {
        try {
            LinkageDeviceBean linkageDeviceBean = new Gson().fromJson(payload, LinkageDeviceBean.class);
            List<LinkageDeviceBean.Devices> devices = linkageDeviceBean.devices;
            if (mList_device.size() > 0) {
                mList_device.clear();
            }
            mList_device.addAll(devices);
            mAdapter.notifyDataSetChanged();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void parseData(String payload) {

        try {
            LinkageSettingBean linkageSettingBean = new Gson().fromJson(payload, LinkageSettingBean.class);
            linkage = linkageSettingBean.linkage;
            String name = linkageSettingBean.linkage.name;
            tvLinkageName.setText(name);
            if (timingId.equals("null")) {
                //设备联动
                String name1 = linkageSettingBean.linkage.device.name;
                String name2 = linkageSettingBean.linkage.proAtt.name;
                String modelPath = linkageSettingBean.linkage.device.product.modelPath;
                tv_device_name.setText(name1);
                Glide.with(this).load(ContentConfig.drawableByIcon(linkageSettingBean.linkage.device.product.icon)).into(iv_header);
                tv_device_cfgn.setText(name2);
                int value = linkageSettingBean.linkage.value;
                if (modelPath.equals("pro_switch")) {
                    if (value == 0) {
                        tv_device_cftj.setText("关闭");
                    } else {
                        tv_device_cftj.setText("开启");
                    }
                } else {
                    int value1 = linkageSettingBean.linkage.value;
                    int type = linkageSettingBean.linkage.type;
                    if (type == -1) {
                        tv_device_cftj.setText("(>)" + value1);
                    } else if (type == 0) {
                        tv_device_cftj.setText("(=)" + value1);
                    } else if (type == 1) {
                        tv_device_cftj.setText("(<)" + value1);
                    }
                }
            } else {
                //时间联动
                String cronExpression = linkageSettingBean.linkage.timingRecord.cronExpression;
                String timing = linkageSettingBean.linkage.timingRecord.timing;
                if (TextUtils.isEmpty(cronExpression)) {
                    tv_time_mode.setText("仅一次");
                } else {
                    tv_time_mode.setText(cronExpression);
                }
                tv_time_timing.setText(timing);
            }

            List<LinkageSettingBean.Lts> lts = linkageSettingBean.lts;
            if (mList.size() > 0) {
                mList.clear();
            }
            mList.addAll(lts);
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

//    private void socketConnection() {
//        try {
//            mConnection.connect("ws://39.104.119.0:18888/mobilephone=" + mobilephone + "&password=" + password, new MyWebSocketHandler());
//
//        } catch (WebSocketException e) {
//            e.printStackTrace();
//            SmartToast.show("网络连接错误");
//        }
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.e("TAG", "onstop");
//        mConnection.disconnect();
//    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.e("TAG", "onrestart");
//        mConnection.sendTextMessage("{\"pn\":\"LCTP\",\"linkageID\":\"" + linkageID + "\"}");
//        mConnection.sendTextMessage("{\"pn\":\"LDLTP\",\"type\":1}");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.e("TAG", "onstop");
//        SPUtils.put(this, "linkageRef", true);
//        mConnection.disconnect();
//    }
}

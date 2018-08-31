package com.hbdiye.lechuangsmart.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hbdiye.lechuangsmart.Global.ContentConfig;
import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.SingleWebSocketConnection;
import com.hbdiye.lechuangsmart.adapter.ImageAdapter;
import com.hbdiye.lechuangsmart.adapter.LinkageDeviceMenuAdapter;
import com.hbdiye.lechuangsmart.adapter.LinkageYaoKongQiListAdapter;
import com.hbdiye.lechuangsmart.adapter.SceneSettingAdapter;
import com.hbdiye.lechuangsmart.adapter.SceneSettingDeviceAdapter;
import com.hbdiye.lechuangsmart.bean.ImageBean;
import com.hbdiye.lechuangsmart.bean.SceneDeviceBean;
import com.hbdiye.lechuangsmart.bean.YaoKongListBean;
import com.hbdiye.lechuangsmart.util.Logger;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hbdiye.lechuangsmart.views.GetGatewayPopwindow;
import com.hbdiye.lechuangsmart.views.GetScenePopwindow;
import com.hbdiye.lechuangsmart.views.SceneDialog;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.kookong.app.data.AppConst;
import com.kookong.app.data.IrData;
import com.kookong.app.data.IrDataList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.tavendo.autobahn.WebSocketConnection;
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
    @BindView(R.id.iv_scene_setting_icon)
    ImageView ivSceneSettingIcon;
    @BindView(R.id.iv_device_back)
    ImageView ivDeviceBack;
    @BindView(R.id.rv_hongwai_device)
    RecyclerView rvHongwaiDevice;
    @BindView(R.id.rv_device_menu)
    RecyclerView rvDeviceMenu;
    @BindView(R.id.ll_parent)
    LinearLayout llParent;
    private boolean isOpen = false;//默认侧边栏关闭
    private String sceneID = "";

    private WebSocketConnection mConnection;
    private HomeReceiver homeReceiver;
    private String mobilephone;
    private String password;

    private SceneSettingAdapter adapter;
    private List<SceneDeviceBean.SceneTasks> mList = new ArrayList<>();

    private SceneSettingDeviceAdapter mAdapter;
    private List<SceneDeviceBean.Devices> mList_device = new ArrayList<>();

    //遥控列表
    private List<YaoKongListBean.Irremotes> mList_yk = new ArrayList<>();
    private LinkageYaoKongQiListAdapter adapter_yk;

    //遥控按钮
    private ArrayList<IrData.IrKey> mList_menu = new ArrayList<>();
    private LinkageDeviceMenuAdapter adapter_menu;

    private TimePickerView timePickerView;
    private OptionsPickerView pickerBuilder;
    private int timeFlag = -1;//position

    private SceneDialog sceneDialog;
    private SceneDeviceBean sceneDeviceBean;

    private int dialogflag;//开关列表标志

    //pupupwindows
    private PopupWindow popupWindow;
    private RecyclerView rv_image_list;

    private List<ImageBean> mList_image = new ArrayList<>();

    private int level = 0;
    private SceneDeviceBean.Devices devices;
    private YaoKongListBean.Irremotes irremotes;
    private String rcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_setting);
        ButterKnife.bind(this);
        initView();
        initData();

        handleClick();
    }

    private void initCustomTimePicker(int position) {
        String timing = ContentConfig.secToTime(mList.get(position).delaytime);
        String[] split = timing.split(":");
        int position0 = ContentConfig.getHourPosition(split[0]);
        int position1 = ContentConfig.getMandSPosition(split[1]);
        int position2 = ContentConfig.getMandSPosition(split[2]);
        Log.e("fff", position0 + " " + position1 + "   " + position2 + "   " + timing);
        pickerBuilder = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                tvBaseTitle.setText(options1+"时"+options2+"分"+options3+"秒");
                int i = options1 * 3600 + options2 * 60 + options3;
//                tvBaseTitle.setText(ContentConfig.secToTime(options1*3600+options2*60+options3));
                mConnection.sendTextMessage("{\"pn\":\"NSTUTP\",\"stID\":\"" + mList.get(timeFlag).id + "\",\"proActID\":\"" + mList.get(timeFlag).proActID + "\",\"delaytime\":\"" + i + "\"}");
            }
        }).setLabels("时", "分", "秒")
                .isCenterLabel(true)
                .setSelectOptions(position0, position1, position2)
                .build();
        pickerBuilder.setNPicker(ContentConfig.getTimeHours(), ContentConfig.getTimeMin(), ContentConfig.getTimeSeco());
    }

    private void handleClick() {
//        =============================8.26注释 修改开关============================================
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                timeFlag = position;
                switch (view.getId()) {
                    case R.id.tv_scene_setting_time:
                        initCustomTimePicker(position);
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
                                mConnection.sendTextMessage("{\"pn\":\"NSTDTP\",\"stID\":\"" + mList.get(position).id + "\"}");
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.show();
                        break;
//                    case R.id.tv_scene_setting_switch:
//                        dialogflag = -1;
//                        final List<SceneDeviceBean.SceneTasks.ProActs> proActs = mList.get(position).proActs;
////                        String[] proActs_id = new String[proActs.size()];
//                        String proActID = mList.get(position).proActID;
//                        String[] proActs_name = new String[proActs.size()];
//
//                        for (int i = 0; i < proActs.size(); i++) {
//                            proActs_name[i] = proActs.get(i).name;
//                            if (proActID.equals(proActs.get(i).id)) {
//                                dialogflag = i;
//                            }
//                        }
//
//                        AlertDialog.Builder proActs_list = new AlertDialog.Builder(SceneSettingActivity.this);
//                        proActs_list.setSingleChoiceItems(proActs_name, dialogflag, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
////                                SmartToast.show(proActs.get(which).name);
////                                if (which != dialogflag) {
////                                    mConnection.sendTextMessage("{\"pn\":\"STUTP\",\"stID\":\"" + mList.get(position).id + "\",\"proActID\":\"" + mList.get(position).proActs.get(which).id + "\",\"delaytime\":\"" + mList.get(position).delaytime + "\"}");
////                                }
//                                taskDeviceActionDialog(mList.get(position).id, mList.get(position).proActs.get(which).id);
//                                dialog.dismiss();
//                            }
//                        });
//                        proActs_list.show();
//                        break;
                    default:
                        break;
                }
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                devices = mList_device.get(position);
                if (mList_device.get(position).product.modelPath.equals("pro_dispatcher")) {
                    level = 1;
                    mList_yk.clear();
                    adapter_yk.notifyDataSetChanged();
                    rvSceneDevice.setVisibility(View.GONE);
                    ivDeviceBack.setVisibility(View.VISIBLE);
                    rvHongwaiDevice.setVisibility(View.VISIBLE);
                    mConnection.sendTextMessage("{\"pn\":\"IRLTP\",\"deviceID\":\"" + mList_device.get(position).id + "\"}");
                } else {
//                    mConnection.sendTextMessage("{\"pn\":\"STATP\",\"sceneID\":\"" + sceneID + "\",\"deviceID\":\"" + sceneDeviceBean.devices.get(position).id + "\",\"proActID\":\"" + sceneDeviceBean.devices.get(position).product.proacts.get(0).id + "\",\"type\":\"0\",\"delaytime\":\"0\"}");
                    final List<SceneDeviceBean.Devices.Product.Proacts> proActs = mList_device.get(position).product.proacts;
//                        String[] proActs_id = new String[proActs.size()];
//                    String proActID = mList_device.get(position).product.proacts;
                    String[] proActs_name = new String[proActs.size()];
                    final String[] proActs_id = new String[proActs.size()];
                    for (int i = 0; i < proActs.size(); i++) {
                        proActs_name[i] = proActs.get(i).name;
                        proActs_id[i] = proActs.get(i).id;
                    }
                    final AlertDialog.Builder builder = new AlertDialog.Builder(SceneSettingActivity.this);
                    builder.setSingleChoiceItems(proActs_name, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mConnection.sendTextMessage("{\"pn\":\"CTP\",\"deviceID\":\"" + mList_device.get(position).id + "\",\"proActID\":\"" + proActs_id[which] + "\",\"param\":\"\"}");
                            dialog.dismiss();
                            deviceActionDialog(mList_device.get(position).id, proActs_id[which]);
                        }
                    });
                    builder.show();

                }
            }
        });
        //遥控列表
        adapter_yk.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                level = 2;
                mList_menu.clear();
                adapter_menu.notifyDataSetChanged();
                ivDeviceBack.setVisibility(View.VISIBLE);
                rvSceneDevice.setVisibility(View.GONE);
                rvHongwaiDevice.setVisibility(View.GONE);
                rvDeviceMenu.setVisibility(View.VISIBLE);
                irremotes = mList_yk.get(position);
                String rtype = mList_yk.get(position).rtype;
                String rid = mList_yk.get(position).rid;
                if (rtype.equals("TV")) {
                    getIRDataById(rid, TV);
                } else if (rtype.equals("BOX")) {
                    getIRDataById(rid, BOX);
                } else if (rtype.equals("STB")) {
                    getIRDataById(rid, STB);
                } else if (rtype.equals("DVD")) {
                    getIRDataById(rid, DVD);
                } else if (rtype.equals("AC")) {
                    getNoStateIRDataById(rid, AC);
                } else if (rtype.equals("PRO")) {
                    getIRDataById(rid, PRO);
                } else if (rtype.equals("PA")) {
                    getIRDataById(rid, PA);
                } else if (rtype.equals("FAN")) {
                    getIRDataById(rid, FAN);
                } else if (rtype.equals("SLR")) {
                    getIRDataById(rid, SLR);
                } else if (rtype.equals("AIR_CLEANER")) {
                    getIRDataById(rid, AIR_CLEANER);
                }
            }
        });
        adapter_menu.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String data = "{\"pn\":\"STATP\",\"sceneID\":\"" + sceneID + "\",\"deviceID\":\"" + devices.id + "\",\"proActID\":\"" + devices.product.proacts.get(0).id + "\",\"param\":\"" + irremotes.id + "\",\"type\":\"1\",\"delaytime\":null,\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + mList_menu.get(position).pulse.replace(" ", "").replace(",", "") + "\",\"irremoteid\":\"" + irremotes.id + "\",\"proname\":\"" + mList_menu.get(position).fname + "\"}";
                Log.e("TTT", data);
                mConnection.sendTextMessage(data);
//                mConnection.sendTextMessage("{\"pn\":\"LTATP\",\"linkageID\":\""+linkageID+"\",\"deviceID\":\""+devices.id+"\",\"proActID\":\""+devices.product.proacts.get(0).id+"\",\"param\":\""+irremotes.id+"\",\"type\":\"1\",\"delaytime\":null,\"rcode\":\""+mList_menu.get(0).exts.get(99999)+"\",\"fpulse\":\""+mList_menu.get(position).pulse+"\",\"irremoteid\":\""+irremotes.id+"\",\"proname\":\"%@\"}");
                drawerLayout.closeDrawers();
            }
        });
    }

    private void taskDeviceActionDialog(final String stId, final String proactId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("该设备是否响应成功？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mConnection.sendTextMessage("{\"pn\":\"NSTUTP\",\"stID\":\"" + stId + "\",\"proActID\":\"" + proactId + "\",\"param\":\"\"}");
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    private void deviceActionDialog(final String deviceID, final String proActID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("该设备是否响应成功？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mConnection.sendTextMessage("{\"pn\":\"NSTATP\",\"sceneID\":\"" + sceneID + "\",\"deviceID\":\"" + deviceID + "\",\"proActID\":\"" + proActID + "\",\"param\":\"\",\"type\":\"0\"}");
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    /**
     * 电视
     */
    private void getIRDataById(String rid, int type) {
        KookongSDK.getIRDataById(rid, type, true, new IRequestResult<IrDataList>() {

            @Override
            public void onSuccess(String msg, IrDataList result) {
                List<IrData> irDatas = result.getIrDataList();
                ArrayList<IrData.IrKey> keys = irDatas.get(0).keys;
                rcode = irDatas.get(0).exts.get(99999);
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
    public void getNoStateIRDataById(String rid, int type) {
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
                        IrData.IrKey irdata_irkey = new IrData.IrKey();
                        irdata_irkey.fname = "开";
                        irdata_irkey.pulse = power_on;
                        IrData.IrKey irdata_irkey1 = new IrData.IrKey();
                        irdata_irkey1.fname = "关";
                        irdata_irkey1.pulse = power_off;
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

    private void initData() {
        sceneID = getIntent().getStringExtra("sceneID");
//        mobilephone = (String) SPUtils.get(this, "mobilephone", "");
//        password = (String) SPUtils.get(this, "password", "");
        mConnection = SingleWebSocketConnection.getInstance();
        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("STLTP");
        intentFilter.addAction("IRLTP");
        intentFilter.addAction("SUTP");
        intentFilter.addAction("NSTATP");
        intentFilter.addAction("NSTUTP");
        intentFilter.addAction("NSTDTP");
        homeReceiver = new HomeReceiver();
        registerReceiver(homeReceiver, intentFilter);
//        socketConnection();

        initImageData();
    }

    private void initImageData() {
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.changjing1);
            setIconName("changjing1");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.huijia);
            setIconName("changjing2");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.youxi);
            setIconName("changjing3");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.ipod);
            setIconName("changjing4");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.zixingche);
            setIconName("changjing5");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.chuang);
            setIconName("changjing6");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.jita);
            setIconName("changjing7");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.dianhua);
            setIconName("changjing8");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.dangao);
            setIconName("changjing9");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.guangdie);
            setIconName("changjing10");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.erji);
            setIconName("changjing11");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.suo);
            setIconName("changjing12");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.kaisuo);
            setIconName("changjing13");
        }});
        mList_image.add(new ImageBean() {{
            setDrawableId(R.mipmap.mojing);
            setIconName("changjing14");
        }});
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

        LinearLayoutManager manager_yk = new LinearLayoutManager(this);
        manager_yk.setOrientation(LinearLayoutManager.VERTICAL);
        rvHongwaiDevice.setLayoutManager(manager_yk);
        adapter_yk = new LinkageYaoKongQiListAdapter(mList_yk);
        rvHongwaiDevice.setAdapter(adapter_yk);


        LinearLayoutManager manager_menu = new LinearLayoutManager(this);
        manager_menu.setOrientation(LinearLayoutManager.VERTICAL);
        rvDeviceMenu.setLayoutManager(manager_menu);
        adapter_menu = new LinkageDeviceMenuAdapter(mList_menu);
        rvDeviceMenu.setAdapter(adapter_menu);
    }

    private GetScenePopwindow getPhotoPopwindow;

    @OnClick({R.id.iv_base_back, R.id.iv_base_right, R.id.iv_scene_edit, R.id.ll_add_device, R.id.iv_scene_setting_icon, R.id.iv_device_back})
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
                    level = 0;
                    ivDeviceBack.setVisibility(View.INVISIBLE);
                    rvSceneDevice.setVisibility(View.VISIBLE);
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
            case R.id.iv_scene_setting_icon:
                showPopWindow(getView());
                break;
            case R.id.iv_device_back:
                if (level == 2) {
                    rvDeviceMenu.setVisibility(View.GONE);
                    rvHongwaiDevice.setVisibility(View.VISIBLE);
                    level = 1;
                } else if (level == 1) {
                    rvHongwaiDevice.setVisibility(View.GONE);
                    rvSceneDevice.setVisibility(View.VISIBLE);
                    ivDeviceBack.setVisibility(View.INVISIBLE);
                    level = 0;
                }
                break;
            case R.id.iv_base_right:
                String groupNo = sceneDeviceBean.scene.groupNo;
                String sceneNo = sceneDeviceBean.scene.sceneNo;
                String info = "信息：场景号：" + sceneNo + ",分组号：" + groupNo;
                getPhotoPopwindow = new GetScenePopwindow(SceneSettingActivity.this, photoclicer, info);
                getPhotoPopwindow.showPopupWindowBottom(llParent);
                break;
        }
    }

    public View.OnClickListener photoclicer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_popupwindows_Photo:
                    //信息
                    break;
                case R.id.item_popupwindows_stop:
                    //情景面板
                    startActivity(new Intent(SceneSettingActivity.this, ScenePanelActivity.class)
                            .putExtra("sceneID", sceneID));
                    if (getPhotoPopwindow != null) {
                        getPhotoPopwindow.dismiss();
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

    private void showPopWindow(View view) {
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        ColorDrawable cd = new ColorDrawable(0x00ffffff);// 背景颜色全透明
        popupWindow.setBackgroundDrawable(cd);
        int[] location = new int[2];
        ivSceneSettingIcon.getLocationOnScreen(location);
        backgroundAlpha(0.5f);// 设置背景半透明
        popupWindow.showAsDropDown(ivSceneSettingIcon);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                popupWindow = null;// 当点击屏幕时，使popupWindow消失
                backgroundAlpha(1.0f);// 当点击屏幕时，使半透明效果取消
            }
        });
    }

    // 设置popupWindow背景半透明
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;// 0.0-1.0
        getWindow().setAttributes(lp);
    }

    private View getView() {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_image_list, null);
        rv_image_list = view.findViewById(R.id.rv_image_list);
        rv_image_list.setLayoutManager(new GridLayoutManager(this, 9));
        ImageAdapter imageAdapter = new ImageAdapter(mList_image);
        rv_image_list.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String s = tvSceneName.getText().toString();
                mConnection.sendTextMessage("{\"pn\":\"SUTP\",\"sceneID\":\"" + sceneID + "\",\"icon\":\"" + mList_image.get(position).getIconName() + "\",\"name\":\"" + s + "\"}");
                popupWindow.dismiss();
            }
        });
        return view;
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
                        mConnection.sendTextMessage("{\"pn\":\"SUTP\",\"sceneID\":\"" + sceneID + "\",\"name\":\"" + sceneName + "\"}");
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

    class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String payload = intent.getStringExtra("message");
            if (action.equals("STLTP")) {
                Log.e("bbb", payload);
                parseData(payload);
            }
            if (action.equals("IRLTP")) {
                parseDataHW(payload);
            }
            if (action.equals("SUTP")) {
                //修改场景名称
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    String ecode = jsonObject.getString("ecode");
                    if (ecode.equals("200")) {
                        if (sceneDialog != null) {
                            sceneDialog.dismiss();
                        }
                        SmartToast.show("修改成功");
//                        SPUtils.put(SceneSettingActivity.this, "editSceneName", true);
                        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
                    } else if (ecode.equals("401")) {
                        SmartToast.show("数据异常");
                    } else if (ecode.equals("484")) {
                        SmartToast.show("场景设置冲突");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (action.equals("NSTATP")) {
                //添加设备
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    String ecode = jsonObject.getString("ecode");
                    if (ecode.equals("200")) {
                        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
                        drawerLayout.closeDrawers();
                    } else if (ecode.equals("304")) {
                        SmartToast.show("网关不在线");
                    } else if (ecode.equals("404")) {
                        SmartToast.show("设备不存在");
                    } else if (ecode.equals("421")) {
                        SmartToast.show("设备动作不存在");
                    } else if (ecode.equals("481")) {
                        SmartToast.show("组号设置失败");
                    } else if (ecode.equals("482")) {
                        SmartToast.show("场景号设置失败");
                    } else if (ecode.equals("483")) {
                        SmartToast.show("场景不存在");
                    } else if (ecode.equals("484")) {
                        SmartToast.show("场景设置冲突");
                    } else {
                        SmartToast.show("添加设备失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (action.equals("NSTUTP")) {
                //设置延时STUTP
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    String ecode = jsonObject.getString("ecode");
                    if (ecode.equals("200")) {
                        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
                    } else if (ecode.equals("304")) {
                        SmartToast.show("网关不在线");
                    } else if (ecode.equals("404")) {
                        SmartToast.show("设备不存在");
                    } else if (ecode.equals("421")) {
                        SmartToast.show("设备动作不存在");
                    } else if (ecode.equals("481")) {
                        SmartToast.show("组号设置失败");
                    } else if (ecode.equals("482")) {
                        SmartToast.show("场景号设置失败");
                    } else if (ecode.equals("483")) {
                        SmartToast.show("场景不存在");
                    } else if (ecode.equals("486")) {
                        SmartToast.show("场景任务不存在");
                    } else {
                        SmartToast.show("修改失败");
                    }
//                    if (status) {
//                        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (action.equals("NSTDTP")) {
                //删除设备STDTP
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    String ecode = jsonObject.getString("ecode");
                    if (ecode.equals("200")) {
                        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
                    } else if (ecode.equals("304")) {
                        SmartToast.show("网关不在线");
                    } else if (ecode.equals("404")) {
                        SmartToast.show("设备不存在");
                    } else if (ecode.equals("421")) {
                        SmartToast.show("设备动作不存在");
                    } else if (ecode.equals("481")) {
                        SmartToast.show("组号设置失败");
                    } else if (ecode.equals("482")) {
                        SmartToast.show("场景号设置失败");
                    } else if (ecode.equals("483")) {
                        SmartToast.show("场景不存在");
                    } else if (ecode.equals("486")) {
                        SmartToast.show("场景任务不存在");
                    } else {
                        SmartToast.show("修改失败");
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
//            Log.e("TAG", "open");
//            mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
//        }
//
//        @Override
//        public void onTextMessage(String payload) {
//            Log.e("TAG", "onTextMessage" + payload);
//            if (payload.contains("{\"pn\":\"HRQP\"}")) {
//                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
//            }
//            if (payload.contains("{\"pn\":\"PRTP\"}")) {
//                MyApp.finishAllActivity();
//                Intent intent = new Intent(SceneSettingActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//            if (payload.contains("\"pn\":\"STLTP\"")) {
//                parseData(payload);
//            }
//            if (payload.contains("\"pn\":\"IRLTP\"")) {
//                parseDataHW(payload);
//            }
//            if (payload.contains("\"pn\":\"SUTP\"")) {
//                //修改场景名称
//                try {
//                    JSONObject jsonObject = new JSONObject(payload);
//                    boolean status = jsonObject.getBoolean("status");
//                    if (status) {
//                        if (sceneDialog != null) {
//                            sceneDialog.dismiss();
//                        }
//                        SmartToast.show("修改成功");
//                        SPUtils.put(SceneSettingActivity.this, "editSceneName", true);
//                        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (payload.contains("\"pn\":\"STATP\"")) {
//                //添加设备
//                try {
//                    JSONObject jsonObject = new JSONObject(payload);
//                    boolean status = jsonObject.getBoolean("status");
//                    String stCode = jsonObject.getString("stCode");
//                    if (stCode.equals("200")){
//                        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
//                        drawerLayout.closeDrawers();
//                    }else if (stCode.equals("304")){
//                        SmartToast.show("网关不在线");
//                    }else if (stCode.equals("404")){
//                        SmartToast.show("设备不存在");
//                    }else if (stCode.equals("421")){
//                        SmartToast.show("设备动作不存在");
//                    }else if (stCode.equals("481")){
//                        SmartToast.show("组号设置失败");
//                    }else if (stCode.equals("482")){
//                        SmartToast.show("场景号设置失败");
//                    }else if (stCode.equals("483")){
//                        SmartToast.show("场景不存在");
//                    }else {
//                        SmartToast.show("添加设备失败");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (payload.contains("\"pn\":\"STUTP\"")) {
//                //设置延时STUTP
//                try {
//                    JSONObject jsonObject = new JSONObject(payload);
//                    boolean status = jsonObject.getBoolean("status");
//                    if (status) {
//                        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (payload.contains("\"pn\":\"STDTP\"")) {
//                //删除设备STDTP
//                try {
//                    JSONObject jsonObject = new JSONObject(payload);
//                    boolean status = jsonObject.getBoolean("status");
//                    if (status) {
//                        mConnection.sendTextMessage("{\"pn\":\"STLTP\",\"sceneID\":\"" + sceneID + "\"}");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        @Override
//        public void onClose(int code, String reason) {
//            Log.e("TAG", "onClose");
//        }
//    }

    private void parseDataHW(String payload) {
        YaoKongListBean yaoKongListBean = new Gson().fromJson(payload, YaoKongListBean.class);
        List<YaoKongListBean.Irremotes> irremotes = yaoKongListBean.irremotes;
        if (mList_yk.size() > 0) {
            mList_yk.clear();
        }
        mList_yk.addAll(irremotes);
        adapter_yk.notifyDataSetChanged();
    }

    private void parseData(String payload) {
        try {
            sceneDeviceBean = new Gson().fromJson(payload, SceneDeviceBean.class);
            tvSceneName.setText(sceneDeviceBean.scene.name);
            Glide.with(this).load(ContentConfig.sceneIcon(sceneDeviceBean.scene.icon)).into(ivSceneSettingIcon);
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
            Iterator<SceneDeviceBean.Devices> it = mList_device.iterator();
            while (it.hasNext()) {
                SceneDeviceBean.Devices x = it.next();
                if (x.product.modelPath.equals("pro_dispatcher")) {
                    it.remove();
                }
            }

            adapter.notifyDataSetChanged();
            mAdapter.notifyDataSetChanged();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
    }
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.e("TAG", "onstop");
//        mConnection.disconnect();
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.e("TAG", "onrestart");
//        if (mConnection != null) {
//            socketConnection();
//        }
//    }
//
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
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.e("TAG", "onstop");
//        mConnection.disconnect();
//    }

}

package com.hbdiye.lechuangsmart.activity;

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
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.adapter.YaoKongQiListAdapter;
import com.hbdiye.lechuangsmart.bean.YaoKongListBean;
import com.hbdiye.lechuangsmart.util.Logger;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hbdiye.lechuangsmart.views.PicYaoKongPopwindow;
import com.hbdiye.lechuangsmart.views.SceneDialog;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;

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

/**
 * 遥控设备列表
 */
public class YaoKongListActivity extends BaseActivity {

    @BindView(R.id.tv_yaokong_name)
    TextView tvYaokongName;
    @BindView(R.id.rv_yaokong)
    RecyclerView rvYaokong;
    @BindView(R.id.ll_add_device)
    LinearLayout llAddDevice;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;

    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;
    private String deviceID = "";
    private String deviceName = "";

    private YaoKongQiListAdapter adapter;
    private List<YaoKongListBean.Irremotes> mList = new ArrayList<>();

    private boolean editStatus = false;//编辑状态标志，默认false
    private int flag = -1;

    private SceneDialog sceneDialog;

    private PicYaoKongPopwindow popwindow;

    private String province;
    private String city;
    private String district;
    private String mac;

    @Override
    protected void initData() {
        deviceID = getIntent().getStringExtra("deviceID");
        deviceName = getIntent().getStringExtra("deviceName");
        mac = getIntent().getStringExtra("mac");
        mobilephone = (String) SPUtils.get(this, "mobilephone", "");
        password = (String) SPUtils.get(this, "password", "");
        mConnection = new WebSocketConnection();
        socketConnect();

        tvYaokongName.setText(deviceName);

        province = (String) SPUtils.get(this, "province", "");
        city = (String) SPUtils.get(this, "city", "");
        district = (String) SPUtils.get(this, "district", "");
        getAreaId();

    }

    private void getAreaId() {
        if (!TextUtils.isEmpty(province) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(district)) {
            KookongSDK.getAreaId(province, city, district, new IRequestResult<Integer>() {

                @Override
                public void onSuccess(String msg, Integer result) {
                    Logger.d("AreaId is : " + result);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("areaId", result);

                        //
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(Integer errorCode, String msg) {
                    SmartToast.show(msg);
                }
            });
        }
    }

    @Override
    protected String getTitleName() {
        return "遥控器列表";
    }

    @Override
    protected void initView() {
        ivBaseEdit.setVisibility(View.VISIBLE);
        ivBaseEdit.setImageResource(R.drawable.bianji2);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvYaokong.setLayoutManager(manager);
        adapter = new YaoKongQiListAdapter(mList);
        rvYaokong.setAdapter(adapter);

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
                        mConnection.sendTextMessage("{\"pn\":\"IRDTP\",\"id\":\"" + mList.get(position).id + "\"}");
                        break;
                    case R.id.ll_scene_item_edt:
                        sceneDialog = new SceneDialog(YaoKongListActivity.this, R.style.MyDialogStyle, dailogClicer, "修改设备名称");
                        sceneDialog.setCanceledOnTouchOutside(true);
                        sceneDialog.show();
                        break;
                    case R.id.ll_scene_device:
                        if (!editStatus) {
                            String rtype = mList.get(position).rtype;
                            String rid = mList.get(position).rid;
                            if (rtype.equals("TV")) {
                                //电视
                                startActivity(new Intent(YaoKongListActivity.this, DianShiActivity.class)
                                        .putExtra("rid", rid)
                                        .putExtra("type", TV)
                                        .putExtra("mac",mac ));
                            } else if (rtype.equals("BOX")) {
                                //盒子
                                startActivity(new Intent(YaoKongListActivity.this, SmartBoxActivity.class)
                                        .putExtra("rid", rid)
                                        .putExtra("type", BOX)
                                        .putExtra("mac",mac ));
                            } else if (rtype.equals("STB")) {
                                //机顶盒
                                startActivity(new Intent(YaoKongListActivity.this, JDHActivity.class)
                                        .putExtra("rid", rid)
                                        .putExtra("type", STB)
                                        .putExtra("mac",mac ));
                            } else if (rtype.equals("DVD")) {
                                //DVD
                                startActivity(new Intent(YaoKongListActivity.this, DVDActivity.class)
                                        .putExtra("rid", rid)
                                        .putExtra("type", DVD)
                                        .putExtra("mac",mac ));
                            } else if (rtype.equals("AC")) {
                                //空调
                                startActivity(new Intent(YaoKongListActivity.this, KongTiaoActivity.class)
                                        .putExtra("rid", rid)
                                        .putExtra("type", AC)
                                        .putExtra("mac",mac ));
                            } else if (rtype.equals("PRO")) {
                                //投影仪
                                startActivity(new Intent(YaoKongListActivity.this, TYYActivity.class)
                                        .putExtra("rid", rid)
                                        .putExtra("type", PRO)
                                        .putExtra("mac",mac ));
                            } else if (rtype.equals("PA")) {
                                //功放
                                startActivity(new Intent(YaoKongListActivity.this, GFActivity.class)
                                        .putExtra("rid", rid)
                                        .putExtra("type", PA)
                                        .putExtra("mac",mac ));
                            } else if (rtype.equals("FAN")) {
                                //风扇
                                startActivity(new Intent(YaoKongListActivity.this, FSActivity.class)
                                        .putExtra("rid", rid)
                                        .putExtra("type", FAN)
                                        .putExtra("mac",mac ));
                            } else if (rtype.equals("SLR")) {
                                //单反
                                startActivity(new Intent(YaoKongListActivity.this, DFActivity.class)
                                        .putExtra("rid", rid)
                                        .putExtra("type", SLR)
                                        .putExtra("mac",mac ));
                            } else if (rtype.equals("Light")) {
                                //开关灯泡
                                SmartToast.show("Light");
                            } else if (rtype.equals("AIR_CLEANER")) {
                                //空气净化器
                                startActivity(new Intent(YaoKongListActivity.this, KQjhqActivity.class)
                                        .putExtra("rid", rid)
                                        .putExtra("type", AIR_CLEANER)
                                        .putExtra("mac",mac ));
                            } else if (rtype.equals("WATER_HEATER")) {
                                //热水器
                                SmartToast.show("WATER_HEATER");
                            }
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
                        SmartToast.show("设备名称不能为空");
                    } else {
                        mConnection.sendTextMessage("{\"pn\":\"IRUTP\",\"id\":\"" + mList.get(flag).id + "\",\"name\":\"" + sceneName + "\"}");
                    }
                    break;
            }
        }
    };

    @Override
    protected int getLayoutID() {
        return R.layout.activity_yao_kong_list;
    }

    @OnClick(R.id.ll_add_device)
    public void onViewClicked() {
        popwindow = new PicYaoKongPopwindow(this, popupClicker);
        popwindow.showPopupWindowBottom(llRoot);
    }

    /**
     * 获取某设备的品牌列表
     * public int STB = 1; //机顶盒
     * public int TV  = 2; //电视
     * public int BOX = 3; //网络盒子
     * public int DVD = 4; //DVD
     * public int AC  = 5; //空调
     * public int PRO = 6; //投影仪
     * public int PA  = 7; //功放
     * public int FAN = 8; //风扇
     * public int SLR = 9; //单反相机
     * public int Light = 10; //开关灯泡
     * public int AIR_CLEANER = 11;// 空气净化器
     * public int WATER_HEATER = 12;// 热水器
     */
    public View.OnClickListener popupClicker = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_ds:
                    startActivity(new Intent(YaoKongListActivity.this, HWDeviceListActivity.class)
                            .putExtra("type", 2)
                            .putExtra("mac", mac)
                            .putExtra("deviceID", deviceID));
                    break;
                case R.id.tv_jdh:
                    startActivity(new Intent(YaoKongListActivity.this, HWDeviceListActivity.class)
                            .putExtra("type", 1)
                            .putExtra("mac", mac)
                            .putExtra("deviceID",deviceID));
                    break;
                case R.id.tv_kt:
                    startActivity(new Intent(YaoKongListActivity.this, HWDeviceListActivity.class)
                            .putExtra("type", 5)
                            .putExtra("mac", mac).putExtra("mac", mac)
                            .putExtra("deviceID",deviceID));
                    break;
                case R.id.tv_fs:
                    startActivity(new Intent(YaoKongListActivity.this, HWDeviceListActivity.class)
                            .putExtra("type", 8).putExtra("mac", mac)
                            .putExtra("deviceID",deviceID));
                    break;
                case R.id.tv_znhz:
                    startActivity(new Intent(YaoKongListActivity.this, HWDeviceListActivity.class)
                            .putExtra("type", 3).putExtra("mac", mac)
                            .putExtra("deviceID",deviceID));
                    break;
                case R.id.tv_gf:
                    startActivity(new Intent(YaoKongListActivity.this, HWDeviceListActivity.class)
                            .putExtra("type", 7).putExtra("mac", mac)
                            .putExtra("deviceID",deviceID));
                    break;
                case R.id.tv_dvd:
                    startActivity(new Intent(YaoKongListActivity.this, HWDeviceListActivity.class)
                            .putExtra("type", 4).putExtra("mac", mac)
                            .putExtra("deviceID",deviceID));
                    break;
                case R.id.tv_tyy:
                    startActivity(new Intent(YaoKongListActivity.this, HWDeviceListActivity.class)
                            .putExtra("type", 6).putExtra("mac", mac)
                            .putExtra("deviceID",deviceID));
                    break;
                case R.id.tv_xj:
                    startActivity(new Intent(YaoKongListActivity.this, HWDeviceListActivity.class)
                            .putExtra("type", 9).putExtra("mac", mac)
                            .putExtra("deviceID",deviceID));
                    break;
                case R.id.tv_kqjhq:
                    startActivity(new Intent(YaoKongListActivity.this, HWDeviceListActivity.class)
                            .putExtra("type", 11).putExtra("mac", mac)
                            .putExtra("deviceID",deviceID));
                    break;
                case R.id.tv_rsq:
                    startActivity(new Intent(YaoKongListActivity.this, HWDeviceListActivity.class)
                            .putExtra("type", 12).putExtra("mac", mac)
                            .putExtra("deviceID",deviceID));
                    break;
            }
        }
    };

    class MyWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e("TAG", "open");
            mConnection.sendTextMessage("{\"pn\":\"IRLTP\",\"deviceID\":\"" + deviceID + "\"}");
        }

        @Override
        public void onTextMessage(String payload) {

            Log.e("TAG", "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
            }
            if (payload.contains("\"pn\":\"DOSTP\"")) {

            }
            if (payload.contains("\"pn\":\"IRLTP\"")) {
                parseData(payload);
            }
            if (payload.contains("\"pn\":\"IRUTP\"")) {
//                修改设备名称 IRUTP
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        sceneDialog.dismiss();
                        SmartToast.show("修改成功");
                        mConnection.sendTextMessage("{\"pn\":\"IRLTP\",\"deviceID\":\"" + deviceID + "\"}");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (payload.contains("\"pn\":\"IRDTP\"")) {
                //删除 IRDTP
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        mConnection.sendTextMessage("{\"pn\":\"IRLTP\",\"deviceID\":\"" + deviceID + "\"}");
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
        YaoKongListBean yaoKongListBean = new Gson().fromJson(payload, YaoKongListBean.class);
        List<YaoKongListBean.Irremotes> irremotes = yaoKongListBean.irremotes;
        if (mList.size() > 0) {
            mList.clear();
        }
        mList.addAll(irremotes);
        adapter.notifyDataSetChanged();
//        YaoKongBean anFangBean = new Gson().fromJson(payload, YaoKongBean.class);
//        if (mList.size() > 0) {
//            mList.clear();
//        }
//        List<YaoKongBean.Devices> devices = anFangBean.devices;
//        mList.addAll(devices);
//        for (int i = 0; i < devices.size(); i++) {
//            mList_status.add(false);
//        }
//        mConnection.sendTextMessage("{\"pn\":\"SDOSTP\"}");
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

package com.hbdiye.lechuangsmart.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.util.Logger;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hbdiye.lechuangsmart.util.TipsUtil;
import com.hbdiye.lechuangsmart.views.SceneDialog;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.hzy.tvmao.ir.Device;
import com.kookong.app.data.AppConst;
import com.kookong.app.data.IrData;
import com.kookong.app.data.IrDataList;
import com.kookong.app.data.RemoteList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class PicKTYaoKongActivity extends BaseActivity {

    @BindView(R.id.tv_power)
    TextView tvPower;
    @BindView(R.id.tv_yaokong_test)
    TextView tvYaokongTest;
    private int type;
    private int brandId;
    private int remoteids_size;
    private List<Integer> remoteids;

    private int flag=0;//点击测试按钮标志

    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;
    private String mac;
    private String deviceId;
    private Integer rid;

    private SceneDialog sceneDialog;

    @Override
    protected void initData() {
        mobilephone = (String) SPUtils.get(this, "mobilephone", "");
        password = (String) SPUtils.get(this, "password", "");
        mConnection = new WebSocketConnection();
        socketConnect();
        type = getIntent().getIntExtra("type", -1);
        brandId = getIntent().getIntExtra("brandId", -1);
        mac = getIntent().getStringExtra("mac");
        deviceId=getIntent().getStringExtra("deviceID");
        getAllRemoteIds(type);
    }

    @Override
    protected String getTitleName() {
        return "选择遥控";
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_pic_ktyao_kong;
    }

    @OnClick(R.id.tv_power)
    public void onViewClicked() {
        if (remoteids!=null){
            if (flag==remoteids_size){

                return;
            }
            rid = remoteids.get(flag);
            getIRDataById(remoteids.get(flag));
            flag++;
            tvYaokongTest.setText("测试按键（"+flag+"/"+ remoteids_size +"）");

        }
    }

    private void getIRDataById(Integer rid) {
        //获取获取rid = 4162的 红外码, 批量获取红外码的方式是逗号隔开
        KookongSDK.getNoStateIRDataById(rid+"", Device.AC, true, new IRequestResult<IrDataList>() {

            @Override
            public void onSuccess(String msg, IrDataList result) {
                List<IrData> irDatas = result.getIrDataList();
                for (int i = 0; i < irDatas.size(); i++) {
                    IrData irData = irDatas.get(i);
                    Logger.d("空调：" + irData.rid);
                    //空调支持的模式、温度、风速
                    HashMap<Integer, String> exts = irData.exts;
                    //遥控器参数
                    String remoteParam = exts.get(99999);
                    Logger.d("遥控器参数99999：" + remoteParam);
                    try {
                        JSONArray ja = new JSONArray(exts.get(0));
                        //遍历模式，模式顺序：制冷、制热、自动、送风、除湿

                        //制冷模式
                        acMode(ja.getJSONObject(0), "制冷模式");
                        //制热模式
                        acMode(ja.getJSONObject(1), "制热模式");
                        //自动模式
                        acMode(ja.getJSONObject(2), "自动模式");
                        //送风模式
                        acMode(ja.getJSONObject(3), "送风模式");
                        //除湿模式
                        acMode(ja.getJSONObject(4), "除湿模式");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //空调的组合按键
                    ArrayList<IrData.IrKey> keyList = irData.keys;
                    String keySize = irData.rid + "的组合按键个数：" + (keyList == null ? "0" : keyList.size()) + "\n";
                    Logger.d(keySize);
                    if (keyList != null) {
                        IrData.IrKey irKey = keyList.get(0);
                        Logger.d("按键参数：" + irKey.fkey + "=" + irKey.pulse);
                        IrData.IrKey irKey1 = keyList.get(1);
                        Logger.d("按键参数：" + irKey1.fkey + "=" + irKey1.pulse);
                        IrData.IrKey irKey2 = keyList.get(keyList.size() - 1);
                        Logger.d("按键参数：" + irKey2.fkey + "=" + irKey2.pulse);
                        String replace = irKey.pulse.replace(" ", "").replace(",", "");
                        String data="{\"pn\":\"IRTP\", \"sdMAC\":\""+mac+"\", \"rcode\":\""+remoteParam+"\",\"fpulse\":\""+replace+"\"}}";
                        mConnection.sendTextMessage(data);
                        AlertDialog.Builder builder=new AlertDialog.Builder(PicKTYaoKongActivity.this);
                        builder.setMessage("设备有响应吗？");
                        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sceneDialog = new SceneDialog(PicKTYaoKongActivity.this, R.style.MyDialogStyle, dailogClicer, "修改设备名称");
                                sceneDialog.show();
                            }
                        });
                        builder.setNegativeButton("否",null);
                        builder.show();
                    }
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
                SmartToast.show( msg);
            }

        });
    }

    private void getAllRemoteIds(int type) {
        //指定brand下的电视机所有的红外码的id spid和areaid都传0
//                //获取指定设备类型(机顶盒是spId和areaid,所以这里不是机顶盒的获取方
//                //式，机顶盒的红外码都是按区域划分和品牌关系不大),指定品牌下的红外码
        KookongSDK.getAllRemoteIds(type, brandId, 0, 0, new IRequestResult<RemoteList>() {

            @Override
            public void onSuccess(String msg, RemoteList result) {
                remoteids = result.rids;
                remoteids_size = remoteids.size();
                tvYaokongTest.setText("测试按键（0/"+ remoteids_size +"）");
                String res = Arrays.toString(remoteids.toArray());
                Logger.d("tv remoteids: " + res);
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
    class MyWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e("TAG", "open");
//            mConnection.sendTextMessage("{\"pn\":\"DGLTP\", \"classify\":\"protype\", \"id\":\"PROTYPE02\"}");
        }

        @Override
        public void onTextMessage(String payload) {

            Log.e("TAG", "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
            }
            if (payload.contains("{\"pn\":\"PRTP\"}")) {
                MyApp.finishAllActivity();
                Intent intent = new Intent(PicKTYaoKongActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            if (payload.contains("\"pn\":\"IRTP\"")){

                SmartToast.show(payload);
                try {
                    JSONObject jsonObject=new JSONObject(payload);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (payload.contains("\"pn\":\"IRATP\"")){
                //添加红外遥控 IRATP
                try {
                    JSONObject jsonObject=new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status){
                        sceneDialog.dismiss();
                        startActivity(new Intent(PicKTYaoKongActivity.this,YaoKongListActivity.class));
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
                        String rtype = TipsUtil.RtypeNameByValue(type);
                        String sss="{\"pn\":\"IRATP\",\"deviceID\":\""+deviceId+"\",\"name\":\""+sceneName+"\",\"rid\":\""+rid+"\",\"rtype\":\""+rtype+"\"}";
                        mConnection.sendTextMessage(sss);
                    }
                    break;
            }
        }
    };
    private void parseData(String payload) {
//        AnFangBean anFangBean = new Gson().fromJson(payload, AnFangBean.class);
//        if (mList.size()>0){
//            mList.clear();
//        }
//        List<AnFangBean.Devices> devices = anFangBean.devices;
//        mList.addAll(devices);
//        for (int i = 0; i < devices.size(); i++) {
//            mList_status.add(false);
//        }
////        adapter.notifyDataSetChanged();
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
    private void acMode(JSONObject jo, String mode) throws JSONException {
        String speed = jo.optString("speed");

        String temperature = jo.optString("temperature");
        Map<String,String> mod_s_t=new HashMap<>();
        mod_s_t.put("speed",speed);
        mod_s_t.put("temperature",temperature);
        if (TextUtils.isEmpty(speed) && TextUtils.isEmpty(temperature)) {
            Logger.d("不具备" + mode);
        } else {
            Logger.d(mode + "支持的可调风速：" + speed + "，支持的可调温度：" + temperature);
        }

    }
}

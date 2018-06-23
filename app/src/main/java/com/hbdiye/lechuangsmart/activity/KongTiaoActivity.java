package com.hbdiye.lechuangsmart.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.util.Logger;
import com.hbdiye.lechuangsmart.util.SPUtils;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class KongTiaoActivity extends BaseActivity {

    @BindView(R.id.tv_kt_power)
    TextView tvKtPower;
    @BindView(R.id.tv_kt_mode)
    TextView tvKtMode;
    @BindView(R.id.tv_kt_speed)
    TextView tvKtSpeed;
    @BindView(R.id.tv_kt_director)
    TextView tvKtDirector;
    @BindView(R.id.tv_kt_saof)
    TextView tvKtSaof;
    @BindView(R.id.tv_kt_tem_down)
    TextView tvKtTemDown;
    @BindView(R.id.tv_kt_tem_up)
    TextView tvKtTemUp;
    @BindView(R.id.tv_kt_cur_mode)
    TextView tvKtCurMode;
    @BindView(R.id.tv_kt_cur_speed)
    TextView tvKtCurSpeed;
    @BindView(R.id.tv_kt_cur_temp)
    TextView tvKtCurTemp;
    private String rid = "";
    private int type;

    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;
    private String mac;
    private String rcode;
    private String power_off;
    private String power_on;
    private ArrayList<IrData.IrKey> keyList;
    private String[] speed_array;
    private String[] temp_array;
    private String[] mode_array={"制冷模式","制热模式","自动模式","送风模式","除湿模式"};
    private String default_fpulse;

    private String mode;
    private String speed;
    private String temp;
    private String fkey;

    @Override
    protected void initData() {
        mobilephone = (String) SPUtils.get(this, "mobilephone", "");
        password = (String) SPUtils.get(this, "password", "");
        mConnection = new WebSocketConnection();
        socketConnect();
        type = getIntent().getIntExtra("type", -1);
        rid = getIntent().getStringExtra("rid");
        mac = getIntent().getStringExtra("mac");
        if (!TextUtils.isEmpty(rid) && type != -1) {
            getIRDataById();
        }
    }

    @Override
    protected String getTitleName() {
        return "空调";
    }

    @Override
    protected void initView() {
        ivBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_kong_tiao;
    }

    private void getIRDataById() {
        //获取获取rid = 4162的 红外码, 批量获取红外码的方式是逗号隔开
        KookongSDK.getNoStateIRDataById(rid + "", Device.AC, true, new IRequestResult<IrDataList>() {

            @Override
            public void onSuccess(String msg, IrDataList result) {
                List<IrData> irDatas = result.getIrDataList();
                for (int i = 0; i < irDatas.size(); i++) {
                    IrData irData = irDatas.get(i);
                    Logger.d("空调：" + irData.rid);
                    //空调支持的模式、温度、风速
                    HashMap<Integer, String> exts = irData.exts;
                    //遥控器参数
                    rcode = exts.get(99999);
                    Logger.d("遥控器参数99999：" + rcode);
                    try {
                        JSONArray ja = new JSONArray(exts.get(0));
                        //遍历模式，模式顺序：制冷、制热、自动、送风、除湿

                        //制冷模式
                        acMode(ja.getJSONObject(0), "制冷模式");
                        //制热模式
//                        acMode(ja.getJSONObject(1), "制热模式");
//                        //自动模式
//                        acMode(ja.getJSONObject(2), "自动模式");
//                        //送风模式
//                        acMode(ja.getJSONObject(3), "送风模式");
//                        //除湿模式
//                        acMode(ja.getJSONObject(4), "除湿模式");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //空调的组合按键
                    keyList = irData.keys;
                    String keySize = irData.rid + "的组合按键个数：" + (keyList == null ? "0" : keyList.size()) + "\n";
                    Logger.d(keySize);
                    if (keyList != null) {
                        IrData.IrKey irKey = keyList.get(0);
                        Logger.d("按键参数：" + irKey.fkey + "=" + irKey.pulse);
                        IrData.IrKey irKey1 = keyList.get(1);
                        fkey = irKey1.fkey;
                        Logger.d("按键参数：" + irKey1.fkey + "=" + irKey1.pulse);
                        IrData.IrKey irKey2 = keyList.get(keyList.size() - 1);
                        Logger.d("按键参数：" + irKey2.fkey + "=" + irKey2.pulse);
                        power_on = irKey.pulse.replace(" ", "").replace(",", "");
                        power_off = irKey2.pulse.replace(" ", "").replace(",", "");
                        default_fpulse = irKey1.pulse.replace(" ", "").replace(",", "");
//                        String data = "{\"pn\":\"IRTP\", \"sdMAC\":\"" + mac + "\", \"rcode\":\"" + rcode + "\",\"fpulse\":\"" + replace + "\"}}";
//                        mConnection.sendTextMessage(data);
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
                SmartToast.show(msg);
            }

        });
    }

    private void acMode(JSONObject jo, String mode) throws JSONException {
        String speed = jo.optString("speed");

        String temperature = jo.optString("temperature");
        Map<String, String> mod_s_t = new HashMap<>();
        mod_s_t.put("speed", speed);
        mod_s_t.put("temperature", temperature);
        speed_array = speed.split(",");
        temp_array = temperature.split(",");
        if (TextUtils.isEmpty(speed) && TextUtils.isEmpty(temperature)) {
            Logger.d("不具备" + mode);
        } else {
            Logger.d(mode + "支持的可调风速：" + speed + "，支持的可调温度：" + temperature);
        }

    }

    @OnClick({R.id.tv_kt_power, R.id.tv_kt_mode, R.id.tv_kt_speed, R.id.tv_kt_director, R.id.tv_kt_saof, R.id.tv_kt_tem_down, R.id.tv_kt_tem_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_kt_power:
                //电源
                String data = "{\"pn\":\"IRTP\", \"sdMAC\":\"" + mac + "\", \"rcode\":\"" + rcode + "\",\"fpulse\":\"" + power_on + "\"}}";
                mConnection.sendTextMessage(data);
                mConnection.sendTextMessage("{\"pn\":\"IRTP\", \"sdMAC\":\"" + mac + "\", \"rcode\":\"" + rcode + "\",\"fpulse\":\"" + default_fpulse + "\"}}");
                getMode(fkey);
                tvKtCurMode.setText(mode_array[Integer.parseInt(mode)]);
                tvKtCurSpeed.setText(speed);
                tvKtCurTemp.setText(temp+"℃");
                break;
            case R.id.tv_kt_mode:
                int i = Integer.parseInt(mode);
                i=i+1;
                i=i%5;
                tvKtCurMode.setText(mode_array[i]);
                mode=i+"";
                //模式
                makeFkey(mode,temp,speed);
                getFpulse();
                mConnection.sendTextMessage("{\"pn\":\"IRTP\", \"sdMAC\":\"" + mac + "\", \"rcode\":\"" + rcode + "\",\"fpulse\":\"" + default_fpulse + "\"}}");
                break;
            case R.id.tv_kt_speed:
                int max = Integer.parseInt(speed_array[speed_array.length - 1]);
                int cur_speed = Integer.parseInt(speed);
                cur_speed=cur_speed+1;
                speed=cur_speed%speed_array.length+"";
                tvKtCurSpeed.setText(speed);
                makeFkey(mode,temp,speed);
                getFpulse();
                mConnection.sendTextMessage("{\"pn\":\"IRTP\", \"sdMAC\":\"" + mac + "\", \"rcode\":\"" + rcode + "\",\"fpulse\":\"" + default_fpulse + "\"}}");
//                风速
                break;
            case R.id.tv_kt_director:
//                风向
                break;
            case R.id.tv_kt_saof:
//                扫风
                break;
            case R.id.tv_kt_tem_down:
//                温度-
                int cur_temp = Integer.parseInt(temp);
                cur_temp=cur_temp-1;
                int min_temp=Integer.parseInt(temp_array[0]) ;
                int max_temp=Integer.parseInt(temp_array[temp_array.length-1]) ;
                if (cur_temp>=min_temp&&cur_temp<=max_temp){
                    temp=cur_temp+"";
                    tvKtCurTemp.setText(temp+"℃");
                    makeFkey(mode,temp,speed);
                    getFpulse();
                    mConnection.sendTextMessage("{\"pn\":\"IRTP\", \"sdMAC\":\"" + mac + "\", \"rcode\":\"" + rcode + "\",\"fpulse\":\"" + default_fpulse + "\"}}");
                }else {
                    temp=cur_temp+1+"";
                }
                break;
            case R.id.tv_kt_tem_up:
//                温度+
                int cur_temp_up = Integer.parseInt(temp);
                cur_temp_up=cur_temp_up+1;
                int min_temp_up=Integer.parseInt(temp_array[0]) ;
                int max_temp_up=Integer.parseInt(temp_array[temp_array.length-1]) ;
                if (cur_temp_up>=min_temp_up&&cur_temp_up<=max_temp_up){
                    temp=cur_temp_up+"";
                    tvKtCurTemp.setText(temp+"℃");
                    makeFkey(mode,temp,speed);
                    getFpulse();
                    mConnection.sendTextMessage("{\"pn\":\"IRTP\", \"sdMAC\":\"" + mac + "\", \"rcode\":\"" + rcode + "\",\"fpulse\":\"" + default_fpulse + "\"}}");
                }else {
                    temp=cur_temp_up-1+"";
                }
                break;
        }
    }

    private void getFpulse() {
        if (keyList!=null){
            for (int i = 0; i < keyList.size(); i++) {
                if (fkey.equals(keyList.get(i).fkey)){
                    default_fpulse=keyList.get(i).pulse;
                }
            }
        }
    }

    /**
     * 拼接key
     * @param mode
     * @param temp
     * @param speed
     */
    private void makeFkey(String mode, String temp, String speed) {
        fkey="M"+mode+"_T"+temp+"_S"+speed;
    }

    /**
     *
     * @param fkey
     */
    private void getMode(String fkey) {
        String[] split = fkey.split("_");
        mode = split[0].substring(1);
        temp=split[1].substring(1);
        speed=split[2].substring(1);
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
            if (payload.contains("\"pn\":\"IRTP\"")) {

            }
        }

        @Override
        public void onClose(int code, String reason) {
            Log.e("TAG", "onClose");
        }
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

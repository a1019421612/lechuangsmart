package com.hbdiye.lechuangsmart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hzy.tvmao.KookongSDK;
import com.hzy.tvmao.interf.IRequestResult;
import com.kookong.app.data.AppConst;
import com.kookong.app.data.IrData;
import com.kookong.app.data.IrDataList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class FSActivity extends BaseActivity {
    @BindView(R.id.tv_fs_power)
    TextView tvFsPower;
    @BindView(R.id.tv_fs_time)
    TextView tvFsTime;
    @BindView(R.id.tv_fs_speed)
    TextView tvFsSpeed;
    @BindView(R.id.tv_fs_type)
    TextView tvFsType;
    @BindView(R.id.tv_fs_baifeng)
    TextView tvFsBaifeng;
    @BindView(R.id.tv_fs_novoice)
    TextView tvFsNovoice;
    private String TAG = FSActivity.class.getSimpleName();

    private String rid = "";
    private int type;
    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;
    private String mac;
    private String power;
    private String time;
    private String novoice;
    private String baifeng;
    private String speed;
    private String windtype;
    private String rcode;

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

    private void getIRDataById() {
        KookongSDK.getIRDataById(rid, type, true, new IRequestResult<IrDataList>() {

            @Override
            public void onSuccess(String msg, IrDataList result) {
                List<IrData> irDatas = result.getIrDataList();
                rcode = irDatas.get(0).exts.get(99999);
                ArrayList<IrData.IrKey> keys = irDatas.get(0).keys;
                for (int i = 0; i < keys.size(); i++) {
                    if (irDatas.get(0).keys.get(i).fid == 1) {
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        power = pulse.replace(" ", "").replace(",", "");
                    }else if (irDatas.get(0).keys.get(i).fid == 23){
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        time = pulse.replace(" ", "").replace(",", "");
                    }else if (irDatas.get(0).keys.get(i).fid == 106){
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        novoice = pulse.replace(" ", "").replace(",", "");
                    }else if (irDatas.get(0).keys.get(i).fid == 9362){
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        baifeng = pulse.replace(" ", "").replace(",", "");
                    }else if (irDatas.get(0).keys.get(i).fid == 9367){
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        speed = pulse.replace(" ", "").replace(",", "");
                    }else if (irDatas.get(0).keys.get(i).fid == 9372){
                        String pulse = irDatas.get(0).keys.get(i).pulse;
                        windtype = pulse.replace(" ", "").replace(",", "");
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

    @Override
    protected String getTitleName() {
        return "风扇";
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
        return R.layout.activity_fs;
    }

    @OnClick({R.id.tv_fs_power, R.id.tv_fs_time, R.id.tv_fs_speed, R.id.tv_fs_type, R.id.tv_fs_baifeng, R.id.tv_fs_novoice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_fs_power:
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + power + "\"}");
                break;
            case R.id.tv_fs_time:
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + time + "\"}");
                break;
            case R.id.tv_fs_speed:
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + speed + "\"}");
                break;
            case R.id.tv_fs_type:
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + windtype + "\"}");
                break;
            case R.id.tv_fs_baifeng:
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + baifeng + "\"}");
                break;
            case R.id.tv_fs_novoice:
                mConnection.sendTextMessage("{\"pn\":\"IRTP\",\"sdMAC\":\"" + mac + "\",\"rcode\":\"" + rcode + "\",\"fpulse\":\"" + novoice + "\"}");
                break;
        }
    }
    class MyWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e(TAG, "open");
//            mConnection.sendTextMessage("{\"pn\":\"DGLTP\", \"classify\":\"protype\", \"id\":\"PROTYPE02\"}");
        }

        @Override
        public void onTextMessage(String payload) {

            Log.e(TAG, "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
            }
            if (payload.contains("\"pn\":\"IRTP\"")) {

            }
            if (payload.contains("{\"pn\":\"PRTP\"}")) {
                MyApp.finishAllActivity();
                Intent intent = new Intent(FSActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public void onClose(int code, String reason) {
            Log.e(TAG, "onClose");
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
            mConnection.connect("ws://39.104.119.0:18888/mobilephone=" + mobilephone + "&password=" + password, new MyWebSocketHandler());

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

package com.hbdiye.lechuangsmart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.R;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.exception.BaseException;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.EZOpenSDKListener;
import com.videogo.util.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AutoWifiNetConfigActivityActivity extends BaseActivity {

    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.etSSID)
    EditText etSSID;
    private String mSerialNoStr="";
    private String mSerialVeryCodeStr="";
    private String ssid;
    private String password;
    private String TAG="AutoWifiNetConfigActivityActivity";
    @Override
    protected void initData() {
        mSerialNoStr = getIntent().getStringExtra("SerialNoStr");
        mSerialVeryCodeStr = getIntent().getStringExtra("SerialVeryCodeStr");
    }

    @Override
    protected String getTitleName() {
        return "网络连接";
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_auto_wifi_net_config_activity;
    }

    @OnClick(R.id.btnNext)
    public void onViewClicked() {
        ssid = etSSID.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(ssid)){
            SmartToast.show("SSID不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)){
            SmartToast.show("密码不能为空");
            return;
        }
        configWifi();
    }

    /**
     * /**
     * 开始WiFi配置
     *
     * @param context      应用 activity context
     * @param deviceSerial 配置设备序列号
     * @param ssid         连接WiFi SSID
     * @param password     连接  WiFi 密码
     * @param mode         配网的方式，EZWiFiConfigMode中列举的模式进行任意组合(EZWiFiConfigMode.EZWiFiConfigSmart:普通配网；EZWiFiConfigMode.EZWiFiConfigWave：声波配网),例  如:EZWiFiConfigMode.EZWiFiConfigSmart|EZWiFiConfigMode.EZWiFiConfigWave
     * @param back         配置回调
     * @since 4.8.3
     */

    private void configWifi() {

// * 停止Wifi配置

//        EZOpenSDK.getInstance().stopConfigWiFi();

// * 配网回调
        EZOpenSDKListener.EZStartConfigWifiCallback mEZStartConfigWifiCallback =
                new EZOpenSDKListener.EZStartConfigWifiCallback() {
                    @Override
                    public void onStartConfigWifiCallback(String deviceSerial, final EZConstants.EZWifiConfigStatus status) {
                        AutoWifiNetConfigActivityActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (status == EZConstants.EZWifiConfigStatus.DEVICE_WIFI_CONNECTED) {
                                    //设备wifi连接成功
                                    LogUtil.debugLog(TAG, "设备wifi连接成功");

                                } else if (status == EZConstants.EZWifiConfigStatus.DEVICE_PLATFORM_REGISTED) {
                                    EZOpenSDK.getInstance().stopConfigWiFi();
                                    //设备注册到平台成功，可以调用添加设备接口添加设备
                                    LogUtil.debugLog(TAG, "设备wifi连接成功");
                                    addDevice();
//                                    startActivity(new Intent(ConfigWifiActivity.this, TestActivity.class));
                                }
                            }
                        });
                    }
                };
//        EZOpenSDK.getInstance().startConfigWifi(this,"801588939", "jinzhicengwang", "wangdaizhongxin888",
//                EZConstants.EZWiFiConfigMode.EZWiFiConfigSmart,mEZStartConfigWifiCallback);
        EZOpenSDK.getInstance().startConfigWifi(this, mSerialNoStr, ssid, password,
                EZConstants.EZWiFiConfigMode.EZWiFiConfigSmart, mEZStartConfigWifiCallback);
    }
    public void addDevice(){
        //添加设备
        new Thread() {
            public void run() {

                try {
                    boolean result = EZOpenSDK.getInstance().addDevice(mSerialNoStr, mSerialVeryCodeStr);
                    LogUtil.debugLog(TAG, "添加成功");
                    /***********如有需要开发者需要自己保存此验证码***********/
//                    if (!TextUtils.isEmpty(mVerifyCode)) {
//                        //保存密码
//                        EzvizApplication.getOpenSDK().setValidateCode(mVerifyCode, mSerialNoStr);
//                    }
                    startActivity(new Intent(AutoWifiNetConfigActivityActivity.this, CameraListActivity.class));
                    // 添加成功过后
//                    sendMessage(MSG_ADD_CAMERA_SUCCESS);
                } catch (BaseException e) {
                    ErrorInfo errorInfo = (ErrorInfo) e.getObject();
                    LogUtil.debugLog(TAG, errorInfo.toString());

//                    sendMessage(MSG_ADD_CAMERA_FAIL, errorInfo.errorCode);
                    LogUtil.errorLog(TAG, "add camera fail");
                }

            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                EZOpenSDK.getInstance().stopConfigWiFi();
                LogUtil.debugLog(TAG, "stopBonjourOnThread .cost time = "
                        + (System.currentTimeMillis() - startTime) + "ms");
            }
        }).start();
    }
}

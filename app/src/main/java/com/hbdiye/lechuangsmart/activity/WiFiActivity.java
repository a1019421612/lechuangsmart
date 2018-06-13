package com.hbdiye.lechuangsmart.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.zxing.WriterException;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.google.zxing.encoding.EncodingHandler;
import com.hbdiye.lechuangsmart.util.DensityUtils;
import com.hbdiye.lechuangsmart.util.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WiFiActivity extends BaseActivity {

    @BindView(R.id.iv_wifi)
    ImageView ivWifi;
    @BindView(R.id.et_wifi_name)
    EditText etWifiName;
    @BindView(R.id.et_wifi_psw)
    EditText etWifiPsw;
    @BindView(R.id.tv_wifi_qrcode)
    TextView tvWifiQrcode;
    private String mobilephone;
    @Override
    protected void initData() {
        //  {"wifi_password":"","wifi_ssid":"","mobile_userid":""}
        mobilephone = (String) SPUtils.get(this, "mobilephone", "");
    }

    @Override
    protected String getTitleName() {
        return "小乐WiFi";
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_wi_fi;
    }

    @OnClick(R.id.tv_wifi_qrcode)
    public void onViewClicked() {
        String wifiName = etWifiName.getText().toString().trim();
        String wifiPsw = etWifiPsw.getText().toString().trim();
        if (TextUtils.isEmpty(wifiName)){
            SmartToast.show("wifi名称不能为空");
        }else if (TextUtils.isEmpty(wifiPsw)){
            SmartToast.show("wifi密码不能为空");
        }else {
            try {
                String data="{\"wifi_password\":\""+wifiPsw+"\",\"wifi_ssid\":\""+wifiName+"\",\"mobile_userid\":\""+mobilephone+"\"}";
                Bitmap qrCode = EncodingHandler.createQRCode(data, DensityUtils.dp2px(this, 150));
                Glide.with(WiFiActivity.this).load(qrCode).into(ivWifi);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }
}

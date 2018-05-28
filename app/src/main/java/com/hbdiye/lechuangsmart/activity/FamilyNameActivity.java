package com.hbdiye.lechuangsmart.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.WriterException;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.FamilyNameBean;
import com.hbdiye.lechuangsmart.google.zxing.encoding.EncodingHandler;
import com.hbdiye.lechuangsmart.util.DensityUtils;
import com.hbdiye.lechuangsmart.util.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class FamilyNameActivity extends BaseActivity {

    @BindView(R.id.tv_family_member)
    TextView tvFamilyMember;
    @BindView(R.id.tv_qrcode)
    TextView tvQrcode;
    @BindView(R.id.tv_family_name)
    TextView tvFamilyName;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_family_phone)
    TextView tvFamilyPhone;
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;
    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;

    @Override
    protected void initData() {
        mobilephone = (String) SPUtils.get(this, "mobilephone", "");
        password = (String) SPUtils.get(this, "password", "");
        mConnection = new WebSocketConnection();
        try {
            mConnection.connect("ws://39.104.105.10:18888/mobilephone=" + mobilephone + "&password=" + password, new MyWebSocketHandler());

        } catch (WebSocketException e) {
            e.printStackTrace();
            SmartToast.show("网络连接错误");
        }
    }

    @Override
    protected String getTitleName() {
        return "家庭名称";
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
        return R.layout.activity_family_name;
    }

    @OnClick({R.id.tv_family_member, R.id.tv_qrcode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_family_member:
                startActivity(new Intent(this, FamilyMemberActivity.class));
                break;
            case R.id.tv_qrcode:
                try {
                    Bitmap qrCode = EncodingHandler.createQRCode("123", DensityUtils.dp2px(this,150));
                    Glide.with(FamilyNameActivity.this).load(qrCode).into(ivQrcode);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
//                startActivity(new Intent(this, CaptureActivity.class));
                break;
        }
    }

    class MyWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e("TAG", "open");
            mConnection.sendTextMessage("{\"pn\":\"UITP\"}");
        }

        @Override
        public void onTextMessage(String payload) {
            Log.e("TAG", "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
            }
            if (payload.contains("\"pn\":\"UITP\"")) {
                parseData(payload);
            }
        }

        @Override
        public void onClose(int code, String reason) {
            Log.e("TAG", "onClose");
        }
    }

    private void parseData(String payload) {

        try {
            FamilyNameBean familyNameBean = new Gson().fromJson(payload, FamilyNameBean.class);
            String name = familyNameBean.user.name;
            String mobilephone = familyNameBean.user.mobilephone;
            String familyname = familyNameBean.user.family.name;
            tvFamilyName.setText(familyname);
            tvFamilyPhone.setText(mobilephone);
            tvName.setText(name);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            try {
                mConnection.connect("ws://39.104.105.10:18888/mobilephone=" + mobilephone + "&password=" + password, new MyWebSocketHandler());

            } catch (WebSocketException e) {
                e.printStackTrace();
                SmartToast.show("网络连接错误");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "onstop");
        mConnection.disconnect();
    }
}

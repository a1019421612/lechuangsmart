package com.hbdiye.lechuangsmart.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.WriterException;
import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.FamilyNameBean;
import com.hbdiye.lechuangsmart.google.zxing.activity.CaptureActivity;
import com.hbdiye.lechuangsmart.google.zxing.encoding.EncodingHandler;
import com.hbdiye.lechuangsmart.util.DensityUtils;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hbdiye.lechuangsmart.views.SceneDialog;

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
    @BindView(R.id.ll_family_name)
    LinearLayout llFamilyName;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    private WebSocketConnection mConnection;
    private String mobilephone;
    private String password;

    private String TAG = FamilyNameActivity.class.getSimpleName();
    private String erCode = "";

    private boolean flag = false;

    private SceneDialog sceneDialog;

    @Override
    protected void initData() {
        mobilephone = (String) SPUtils.get(this, "mobilephone", "");
        password = (String) SPUtils.get(this, "password", "");
        mConnection = new WebSocketConnection();
        try {
            mConnection.connect("ws://39.104.105.10:18888/mobilephone=" + mobilephone + "&password=" + password, new FamilyNameWebSocketHandler());

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

    @OnClick({R.id.tv_family_member, R.id.tv_qrcode,R.id.ll_family_name, R.id.ll_name, R.id.ll_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_family_member:
                startActivity(new Intent(this, FamilyMemberActivity.class));
                break;
            case R.id.tv_qrcode:

                startActivityForResult(new Intent(this, CaptureActivity.class), 111);
                break;
            case R.id.ll_family_name:
                sceneDialog = new SceneDialog(this, R.style.MyDialogStyle, familyNameClicer,"修改家庭名称");
                sceneDialog.setCanceledOnTouchOutside(true);
                sceneDialog.show();
                break;
            case R.id.ll_name:
                sceneDialog = new SceneDialog(this, R.style.MyDialogStyle, nameClicer,"修改姓名");
                sceneDialog.setCanceledOnTouchOutside(true);
                sceneDialog.show();
                break;
//            case R.id.ll_phone:
//                sceneDialog = new SceneDialog(this, R.style.MyDialogStyle, phoneClicer,"修改电话");
//                sceneDialog.setCanceledOnTouchOutside(true);
//                sceneDialog.show();
//                break;
        }
    }
//    ("{\"pn\":\"UUITP\",\"fieldType\":\"pwd\",\"value\":\""+enterpsw+"\"}")
    public View.OnClickListener familyNameClicer = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.app_cancle_tv:
                    sceneDialog.dismiss();
                    break;
                case R.id.app_sure_tv:
                    String sceneName = sceneDialog.getSceneName();
                    if (TextUtils.isEmpty(sceneName)){
                        SmartToast.show("家庭名称不能为空");
                    }else {
                        mConnection.sendTextMessage("{\"pn\":\"UUITP\",\"fieldType\":\"fn\",\"value\":\""+sceneName+"\"}");
                    }
                    break;
            }
        }
    };
    public View.OnClickListener nameClicer = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.app_cancle_tv:
                    sceneDialog.dismiss();
                    break;
                case R.id.app_sure_tv:
                    String sceneName = sceneDialog.getSceneName();
                    if (TextUtils.isEmpty(sceneName)){
                        SmartToast.show("姓名不能为空");
                    }else {
                        mConnection.sendTextMessage("{\"pn\":\"UUITP\",\"fieldType\":\"un\",\"value\":\""+sceneName+"\"}");
                    }
                    break;
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == 4) {
            flag = true;
            erCode = data.getStringExtra("erCode");

//            textview.setText(s);
//            SmartToast.show(erCode);
        }
    }


    private List<String> mList_a= new ArrayList<>();
    class FamilyNameWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e(TAG, "open");
            mConnection.sendTextMessage("{\"pn\":\"UITP\"}");
            if (flag) {
                mConnection.sendTextMessage("{\"pn\":\"UJFTP\",\"familyID\":\"" + erCode + "\"} ");
            }
        }

        @Override
        public void onTextMessage(String payload) {
            Log.e(TAG, "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
            }
            if (payload.contains("{\"pn\":\"PRTP\"}")) {
                for (Activity activity : MyApp.mActivitys) {
                    String packageName = activity.getLocalClassName();
                    Log.e("LLL",packageName);
                    mList_a.add(packageName);
                }
                if (mList_a.get(mList_a.size()-1).equals("FamilyNameActivity")){
                    MyApp.finishAllActivity();
                    Intent intent = new Intent(FamilyNameActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
            if (payload.contains("\"pn\":\"UITP\"")) {
                parseData(payload);
            }
            if (payload.contains("\"pn\":\"UUITP\"")){
                try {
                    JSONObject jsonObject=new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status){
                        SmartToast.show("修改成功");
                        if (sceneDialog!=null){
                            sceneDialog.dismiss();
                        }
                        mConnection.sendTextMessage("{\"pn\":\"UITP\"}");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (payload.contains("\"pn\":\"UJFTP\"")) {
                //扫描加入家庭
                flag = false;
                try {
                    JSONObject jsonObject = new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        SmartToast.show("成功加入家庭");
                    } else {
                        SmartToast.show("加入家庭失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                SmartToast.showLong(payload);
            }
        }

        @Override
        public void onClose(int code, String reason) {
            Log.e(TAG, "onClose");
        }
    }

    private void parseData(String payload) {

        try {
            FamilyNameBean familyNameBean = new Gson().fromJson(payload, FamilyNameBean.class);
            String name = familyNameBean.user.name;
            String mobilephone = familyNameBean.user.mobilephone;
            String familyname = familyNameBean.user.family.name;
            String familyID = familyNameBean.user.familyID;
            try {
                Bitmap qrCode = EncodingHandler.createQRCode(familyID, DensityUtils.dp2px(this, 150));
                Glide.with(FamilyNameActivity.this).load(qrCode).into(ivQrcode);
            } catch (WriterException e) {
                e.printStackTrace();
            }
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

//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.e(TAG, "onstop");
//        mConnection.disconnect();
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.e(TAG, "onrestart");
//        if (mConnection != null) {
//            try {
//                mConnection.connect("ws://39.104.105.10:18888/mobilephone=" + mobilephone + "&password=" + password, new FamilyNameWebSocketHandler());
//
//            } catch (WebSocketException e) {
//                e.printStackTrace();
//                SmartToast.show("网络连接错误");
//            }
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onstop");
        mConnection.disconnect();
    }
}

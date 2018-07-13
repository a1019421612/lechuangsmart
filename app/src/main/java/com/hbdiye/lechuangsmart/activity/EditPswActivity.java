package com.hbdiye.lechuangsmart.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.MyApp;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.SingleWebSocketConnection;
import com.hbdiye.lechuangsmart.util.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class EditPswActivity extends BaseActivity {

    @BindView(R.id.et_old_psw)
    EditText etOldPsw;
    @BindView(R.id.et_new_psw)
    EditText etNewPsw;
    @BindView(R.id.et_enter_psw)
    EditText etEnterPsw;
    @BindView(R.id.tv_edit_submit)
    TextView tvEditSubmit;
    private String TAG = EditPswActivity.class.getSimpleName();
    private WebSocketConnection mConnection;
    private HomeReceiver homeReceiver;
    private String mobilephone;
    private String password;
    @Override
    protected void initData() {
        mobilephone = (String) SPUtils.get(this, "mobilephone", "");
        password = (String) SPUtils.get(this, "password", "");
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("UUITP");
        homeReceiver = new HomeReceiver();
        registerReceiver(homeReceiver,intentFilter);
        mConnection = SingleWebSocketConnection.getInstance();
    }

    @Override
    protected String getTitleName() {
        return "修改密码";
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_edit_psw;
    }


    @OnClick(R.id.tv_edit_submit)
    public void onViewClicked() {
        String oldpsw = etOldPsw.getText().toString().trim();
        String newpsw = etNewPsw.getText().toString().trim();
        String enterpsw = etEnterPsw.getText().toString().trim();
        if (TextUtils.isEmpty(oldpsw)){
            SmartToast.show("旧密码不能为空");
            return;
        }if (TextUtils.isEmpty(newpsw)){
            SmartToast.show("新密码不能为空");
            return;
        }if (TextUtils.isEmpty(enterpsw)){
            SmartToast.show("确认密码不能为空");
            return;
        }
        if (!newpsw.equals(enterpsw)){
            SmartToast.show("两次密码输入不一致");
            return;
        }
        if (password.equals(oldpsw)){
            mConnection.sendTextMessage("{\"pn\":\"UUITP\",\"fieldType\":\"pwd\",\"value\":\""+enterpsw+"\"}");
        }else {
            SmartToast.show("旧密码错误，请重新输入");
        }
    }
    class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String payload = intent.getStringExtra("message");
            if (action.equals("UUITP")) {
//                修改密码UUITP
                try {
                    JSONObject jsonObject=new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status){
                        SmartToast.show("修改成功");
                        MyApp.finishAllActivity();
                        startActivity(new Intent(EditPswActivity.this,LoginActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class MyWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
            Log.e(TAG, "open");

        }

        @Override
        public void onTextMessage(String payload) {

            Log.e(TAG, "onTextMessage" + payload);
            if (payload.contains("{\"pn\":\"HRQP\"}")) {
                mConnection.sendTextMessage("{\"pn\":\"HRSP\"}");
            }
            if (payload.contains("{\"pn\":\"PRTP\"}")) {
                MyApp.finishAllActivity();
                Intent intent = new Intent(EditPswActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            if (payload.contains("\"pn\":\"UUITP\"")){
//                修改密码UUITP
                try {
                    JSONObject jsonObject=new JSONObject(payload);
                    boolean status = jsonObject.getBoolean("status");
                    if (status){
                        SmartToast.show("修改成功");
                        MyApp.finishAllActivity();
                        startActivity(new Intent(EditPswActivity.this,LoginActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onClose(int code, String reason) {
            Log.e(TAG, "onClose");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeReceiver);
    }
}

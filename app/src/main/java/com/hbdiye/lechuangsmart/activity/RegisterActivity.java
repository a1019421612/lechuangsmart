package com.hbdiye.lechuangsmart.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.Global.InterfaceManager;
import com.hbdiye.lechuangsmart.MainActivity;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.util.SPUtils;
import com.hbdiye.lechuangsmart.utils.RegexUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.phoneEditText)
    EditText phoneEditText;
    @BindView(R.id.phoneTextInputLayout)
    TextInputLayout phoneTextInputLayout;
    @BindView(R.id.codeEditText)
    EditText codeEditText;
    @BindView(R.id.codeTextInputLayout)
    TextInputLayout codeTextInputLayout;
    @BindView(R.id.sendCodeButton)
    Button sendCodeButton;
    @BindView(R.id.saveButton)
    TextView saveButton;
    @BindView(R.id.iv_register_back)
    ImageView ivRegisterBack;
    @BindView(R.id.protocolTextView)
    TextView protocolTextView;
    @BindView(R.id.agreeCheckBox)
    CheckBox agreeCheckBox;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.passwordTextInputLayout)
    TextInputLayout passwordTextInputLayout;

    private String mPhone;
    private String mCode;
    private String mPassword;
    private String mData;
    private TimeCount timeCount;

    @Override
    protected void initData() {

    }

    @Override
    protected String getTitleName() {
        return "登录";
    }

    @Override
    protected void initView() {
        titleBarLL.setVisibility(View.GONE);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_register;
    }


    @OnClick({R.id.sendCodeButton, R.id.saveButton, R.id.iv_register_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sendCodeButton:
                if (checkPhone()) {
//                    Toast.makeText(this, "验证码", Toast.LENGTH_SHORT).show();
                    timeCount=new TimeCount(60000,1000);
                    timeCount.start();
                    getVailCode(mPhone);
                }
                break;
            case R.id.saveButton:
                if (check()) {
                   registerUser(mPhone,mPassword,mCode);
                }
                break;
            case R.id.iv_register_back:
                finish();
                break;
        }
    }
    private void registerUser(String mPhone, String mPassword, String mCode) {
        OkHttpUtils.post()
                .url(InterfaceManager.getInstance().getURL(InterfaceManager.REGISTER))
                .addParams("mobilephone",mPhone)
                .addParams("password",mPassword)
                .addParams("confirmPassword",mPassword)
                .addParams("code",mCode)
                .addParams("name",mPhone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        SmartToast.show("注册失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("sss",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int result = jsonObject.getInt("errcode");
                            if (result==0){
                                SmartToast.show("注册成功");
                                finish();
                            }else {
                                String data = jsonObject.getString("errmsg");
                                SmartToast.show(data);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getVailCode(String mPhone) {
        OkHttpUtils
                .post()
                .url(InterfaceManager.getInstance().getURL(InterfaceManager.GETVAILCODE))
                .addParams("mobilephone",mPhone)
                .addParams("type","1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        SmartToast.show("网络连接错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG",response);
                    }
                });
    }

    class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            try {
                sendCodeButton.setEnabled(false);
                sendCodeButton.setBackgroundColor(Color.TRANSPARENT);
                sendCodeButton.setText(millisUntilFinished / 1000 + "秒");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFinish() {
            try {
                sendCodeButton.setEnabled(true);
                sendCodeButton.setText("");
                sendCodeButton.setBackgroundResource(R.mipmap.ic_code);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private boolean check() {
        codeTextInputLayout.setErrorEnabled(false);

        if (!checkPhone()) return false;

        mCode = codeEditText.getText().toString();
        if (TextUtils.isEmpty(mCode)) {
            codeTextInputLayout.setError("请输入验证码");
            return false;
        }
        passwordTextInputLayout.setErrorEnabled(false);
        mPassword = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(mPassword)) {
            passwordTextInputLayout.setError("请输入密码");
            return false;
        }
        return true;
    }

    private boolean checkPhone() {
        phoneTextInputLayout.setErrorEnabled(false);

        mPhone = phoneEditText.getText().toString();
        if (TextUtils.isEmpty(mPhone)) {
            phoneTextInputLayout.setError("请输入手机号");
            return false;
        } else if (!RegexUtils.isMobile(mPhone)) {
            phoneTextInputLayout.setError("请输入有效的手机号");
            return false;
        }
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCount!=null){
            timeCount.cancel();
        }
    }
}

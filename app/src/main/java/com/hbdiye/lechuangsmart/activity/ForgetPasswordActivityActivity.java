package com.hbdiye.lechuangsmart.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.Global.InterfaceManager;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.utils.RegexUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class ForgetPasswordActivityActivity extends BaseActivity {

    @BindView(R.id.iv_forgetpsw_back)
    ImageView ivForgetpswBack;
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
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.passwordTextInputLayout)
    TextInputLayout passwordTextInputLayout;
    @BindView(R.id.saveButton)
    TextView saveButton;

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
        return null;
    }

    @Override
    protected void initView() {
        titleBarLL.setVisibility(View.GONE);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_forget_password_activity;
    }

    @OnClick({R.id.iv_forgetpsw_back, R.id.sendCodeButton, R.id.saveButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_forgetpsw_back:
                finish();
                break;
            case R.id.sendCodeButton:
                if (checkPhone()){
                    timeCount = new TimeCount(60000,1000);
                    timeCount.start();
                    getVailCode(mPhone);
                }
                break;
            case R.id.saveButton:
                if (check()){
                    forgetPsw(mPhone,mCode,mPassword);
                }
                break;
        }
    }

    private void forgetPsw(String phone, String code, String psw) {
        OkHttpUtils
                .post()
                .url(InterfaceManager.getInstance().getURL(InterfaceManager.FORGETPSW))
                .addParams("mobilephone",phone)
                .addParams("code",code)
                .addParams("password",psw)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        SmartToast.show("修改失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("SSS",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int result = jsonObject.getInt("errcode");
                            if (result==0){
                                SmartToast.show("修改成功");

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

    private boolean check() {
        codeTextInputLayout.setErrorEnabled(false);

        if (!checkPhone()) return false;

        mCode = codeEditText.getText().toString();
        if (TextUtils.isEmpty(mCode)) {
            codeTextInputLayout.setError("请输入验证码");
            return false;
        }
        passwordTextInputLayout.setErrorEnabled(false);
        mPassword=passwordEditText.getText().toString();
        if (TextUtils.isEmpty(mPassword)){
            passwordTextInputLayout.setError("请输入密码");
            return false;
        }
        return true;
    }
    private void getVailCode(String mPhone) {
        OkHttpUtils
                .post()
                .url(InterfaceManager.getInstance().getURL(InterfaceManager.GETVAILCODE))
                .addParams("mobilephone",mPhone)
                .addParams("type","2")
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

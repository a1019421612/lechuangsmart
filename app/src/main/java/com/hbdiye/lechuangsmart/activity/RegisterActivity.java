package com.hbdiye.lechuangsmart.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.utils.RegexUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
                    Toast.makeText(this, "验证码", Toast.LENGTH_SHORT).show();
                    timeCount=new TimeCount(10000,1000);
                    timeCount.start();

                }
                break;
            case R.id.saveButton:
                if (check()) {
                    Toast.makeText(this, "注册", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_register_back:
                finish();
                break;
        }
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
            sendCodeButton.setEnabled(false);
            sendCodeButton.setBackgroundColor(Color.TRANSPARENT);
            sendCodeButton.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            sendCodeButton.setEnabled(true);
            sendCodeButton.setText("");
            sendCodeButton.setBackgroundResource(R.mipmap.ic_code);
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

}

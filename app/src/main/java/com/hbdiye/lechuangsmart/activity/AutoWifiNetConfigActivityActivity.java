package com.hbdiye.lechuangsmart.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.hbdiye.lechuangsmart.R;

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

    @Override
    protected void initData() {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnNext)
    public void onViewClicked() {
    }
}

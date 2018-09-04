package com.hbdiye.lechuangsmart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.hbdiye.lechuangsmart.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AutoWifiPrepareStepOneActivity extends BaseActivity {


    @BindView(R.id.imageBg)
    ImageView imageBg;
    @BindView(R.id.btnNext)
    Button btnNext;

    @Override
    protected void initData() {

    }

    @Override
    protected String getTitleName() {
        return getResources().getString(R.string.auto_wifi_step_one_title);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_auto_wifi_prepare_step_one;
    }


    @OnClick(R.id.btnNext)
    public void onViewClicked() {
        startActivity(new Intent(this,AutoWifiNetConfigActivityActivity.class));
    }
}

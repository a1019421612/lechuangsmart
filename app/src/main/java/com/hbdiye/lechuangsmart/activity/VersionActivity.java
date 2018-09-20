package com.hbdiye.lechuangsmart.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.fragment.SettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VersionActivity extends BaseActivity {

    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected void initData() {
        String versionName = new SettingFragment().getVersionName(this);
        tvVersion.setText("V"+versionName);
    }

    @Override
    protected String getTitleName() {
        return "版本信息";
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_version;
    }

}

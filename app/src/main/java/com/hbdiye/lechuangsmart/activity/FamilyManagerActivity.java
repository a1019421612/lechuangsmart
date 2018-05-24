package com.hbdiye.lechuangsmart.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hbdiye.lechuangsmart.R;

public class FamilyManagerActivity extends BaseActivity {

    @Override
    protected void initData() {

    }

    @Override
    protected String getTitleName() {
        return "房间管理";
    }

    @Override
    protected void initView() {
        ivBaseEdit.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_family_manager;
    }
}

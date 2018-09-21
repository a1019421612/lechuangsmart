package com.hbdiye.lechuangsmart.activity;

import android.os.Bundle;

import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.views.CircleLoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XuetangActivity extends BaseActivity {

    @BindView(R.id.circleLoadingView)
    CircleLoadingView circleLoadingView;

    @Override
    protected void initData() {
        circleLoadingView.setProgress(75);
    }

    @Override
    protected String getTitleName() {
        return "血糖";
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_xuetang;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

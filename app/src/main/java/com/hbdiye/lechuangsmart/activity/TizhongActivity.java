package com.hbdiye.lechuangsmart.activity;

import android.os.Bundle;
import android.view.View;

import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.views.DashboardView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TizhongActivity extends BaseActivity {

    @BindView(R.id.dashboard_view)
    DashboardView dashboardView;

    @Override
    protected void initData() {
        int i = new Random().nextInt(150);
        dashboardView.setCreditValueWithAnim(currentKG(i));
        dashboardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = new Random().nextInt(150);
                dashboardView.setCreditValueWithAnim(currentKG(i));
            }
        });
    }
    private int currentKG(int i) {
        return i+350;
    }
    @Override
    protected String getTitleName() {
        return "体重";
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_tizhong;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

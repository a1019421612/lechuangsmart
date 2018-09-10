package com.hbdiye.lechuangsmart.activity;

import android.os.Bundle;

import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.views.CircleProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TiZhiActivity extends BaseActivity {

    @BindView(R.id.progress_bar)
    CircleProgressBar progressBar;
    @BindView(R.id.progress_bar1)
    CircleProgressBar progressBar1;

    @Override
    protected void initData() {

    }

    @Override
    protected String getTitleName() {
        return "体质检测";
    }

    @Override
    protected void initView() {
        progressBar.setAnimProgress(65);
        progressBar1.setAnimProgress(46);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_ti_zhi;
    }

}

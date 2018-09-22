package com.hbdiye.lechuangsmart.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.hbdiye.lechuangsmart.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XueyaActivity extends BaseActivity {

    @BindView(R.id.iv_xueya)
    ImageView ivXueya;

    @Override
    protected void initData() {
        AnimationDrawable animationDrawable = (AnimationDrawable) ivXueya.getDrawable();
        animationDrawable.start();
    }

    @Override
    protected String getTitleName() {
        return "血压";
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_xueya;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

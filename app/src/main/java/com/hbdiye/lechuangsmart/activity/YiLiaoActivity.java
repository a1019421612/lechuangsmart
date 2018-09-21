package com.hbdiye.lechuangsmart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.hbdiye.lechuangsmart.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YiLiaoActivity extends BaseActivity {

    @BindView(R.id.ll_yiliao_tizhi)
    LinearLayout llYiliaoTizhi;
    @BindView(R.id.ll_yiliao_tiwen)
    LinearLayout llYiliaoTiwen;
    @BindView(R.id.ll_yiliao_tizhong)
    LinearLayout llYiliaoTizhong;
    @BindView(R.id.ll_yiliao_xuetang)
    LinearLayout llYiliaoXuetang;
    @BindView(R.id.ll_yiliao_xueya)
    LinearLayout llYiliaoXueya;

    @Override
    protected void initData() {

    }

    @Override
    protected String getTitleName() {
        return "身体检测";
    }

    @Override
    protected void initView() {
        ivBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_yi_liao;
    }

    @OnClick({R.id.ll_yiliao_tizhi, R.id.ll_yiliao_tiwen, R.id.ll_yiliao_tizhong, R.id.ll_yiliao_xuetang, R.id.ll_yiliao_xueya})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_yiliao_tizhi:
                startActivity(new Intent(this,TiZhiActivity.class));
                break;
            case R.id.ll_yiliao_tiwen:
                startActivity(new Intent(this,TiwenActivity.class));
                break;
            case R.id.ll_yiliao_tizhong:
                startActivity(new Intent(this,TizhongActivity.class));
                break;
            case R.id.ll_yiliao_xuetang:
                startActivity(new Intent(this,XuetangActivity.class));
                break;
            case R.id.ll_yiliao_xueya:
                startActivity(new Intent(this,XueyaActivity.class));
                break;
        }
    }
}

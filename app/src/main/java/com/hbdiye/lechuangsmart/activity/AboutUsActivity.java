package com.hbdiye.lechuangsmart.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {

    private boolean isOpen=false;//默认侧边栏关闭
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void initData() {

    }

    @Override
    protected String getTitleName() {
        return "关于我们";
    }

    @Override
    protected void initView() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.setFocusableInTouchMode(false);
        ivBaseEdit.setVisibility(View.VISIBLE);
        ivBaseBack.setOnClickListener(this);
        ivBaseEdit.setOnClickListener(this);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                isOpen=true;
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                isOpen=false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_about_us;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_base_back:
                if (isOpen){
                    drawerLayout.closeDrawers();
                }else {
                    finish();
                }
                break;
            case R.id.iv_base_right:
                if (isOpen){
                    drawerLayout.closeDrawers();
                }else {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
                break;
        }
    }
    @Override
    public void onBackPressed() {
        if (!isOpen) {
            super.onBackPressed();
            return;
        }else {
            drawerLayout.closeDrawers();
        }
    }

}

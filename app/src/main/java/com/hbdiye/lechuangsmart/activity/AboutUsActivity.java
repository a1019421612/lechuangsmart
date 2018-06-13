package com.hbdiye.lechuangsmart.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUsActivity extends BaseActivity{

    @Override
    protected void initData() {

    }

    @Override
    protected String getTitleName() {
        return "关于我们";
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_about_us;
    }
}

//public class AboutUsActivity extends AppCompatActivity {
//    @BindView(R.id.iv_base_back)
//    ImageView ivBaseBack;
//    @BindView(R.id.tv_base_title)
//    TextView tvBaseTitle;
//    @BindView(R.id.iv_base_right)
//    ImageView ivBaseRight;
//    @BindView(R.id.toolbar_ll)
//    LinearLayout toolbarLl;
//    @BindView(R.id.tv_drawer)
//    TextView tvDrawer;
//    @BindView(R.id.drawer_layout)
//    DrawerLayout drawerLayout;
//    private boolean isOpen = false;//默认侧边栏关闭
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_about_us);
//        ButterKnife.bind(this);
//        initView();
//    }
//
//    private void initView() {
//        tvBaseTitle.setText("关于我们");
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        drawerLayout.setFocusableInTouchMode(false);
//        ivBaseRight.setVisibility(View.VISIBLE);
//
//        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
//            @Override
//            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
//
//            }
//
//            @Override
//            public void onDrawerOpened(@NonNull View drawerView) {
//                isOpen=true;
//            }
//
//            @Override
//            public void onDrawerClosed(@NonNull View drawerView) {
//                isOpen=false;
//            }
//
//            @Override
//            public void onDrawerStateChanged(int newState) {
//
//            }
//        });
//    }
//
//
//    @OnClick({R.id.iv_base_back, R.id.tv_base_title, R.id.iv_base_right})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.iv_base_back:
//                if (isOpen){
//                    drawerLayout.closeDrawers();
//                }else {
//                    finish();
//                }
//                break;
//            case R.id.tv_base_title:
//                break;
//            case R.id.iv_base_right:
//                if (isOpen){
//                    drawerLayout.closeDrawers();
//                }else {
//                    drawerLayout.openDrawer(GravityCompat.END);
//                }
//                break;
//        }
//    }
//    @Override
//    public void onBackPressed() {
//        if (!isOpen) {
//            super.onBackPressed();
//            return;
//        }else {
//            drawerLayout.closeDrawers();
//        }
//    }
////    private boolean isOpen=false;//默认侧边栏关闭
////    @BindView(R.id.drawer_layout)
////    DrawerLayout drawerLayout;
////
////    @Override
////    protected void initData() {
////
////    }
////
////    @Override
////    protected String getTitleName() {
////        return "关于我们";
////    }
////
////    @Override
////    protected void initView() {
////        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
////        drawerLayout.setFocusableInTouchMode(false);
////        ivBaseEdit.setVisibility(View.VISIBLE);
////        ivBaseBack.setOnClickListener(this);
////        ivBaseEdit.setOnClickListener(this);
////        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
////            @Override
////            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
////
////            }
////
////            @Override
////            public void onDrawerOpened(@NonNull View drawerView) {
////                isOpen=true;
////            }
////
////            @Override
////            public void onDrawerClosed(@NonNull View drawerView) {
////                isOpen=false;
////            }
////
////            @Override
////            public void onDrawerStateChanged(int newState) {
////
////            }
////        });
////    }
////
////    @Override
////    protected int getLayoutID() {
////        return R.layout.activity_about_us;
////    }
////
////    @Override
////    public void onClick(View view) {
////        switch (view.getId()) {
////            case R.id.iv_base_back:
////                if (isOpen){
////                    drawerLayout.closeDrawers();
////                }else {
////                    finish();
////                }
////                break;
////            case R.id.iv_base_right:
////                if (isOpen){
////                    drawerLayout.closeDrawers();
////                }else {
////                    drawerLayout.openDrawer(GravityCompat.END);
////                }
////                break;
////        }
////    }
//
//}

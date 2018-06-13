package com.hbdiye.lechuangsmart.activity;

import android.os.Bundle;
import android.view.View;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.util.ImageUtil;
import com.hbdiye.lechuangsmart.views.RoundMenuView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 智能盒子遥控
 */
public class SmartBoxActivity extends BaseActivity {

    @BindView(R.id.roundMenuView)
    RoundMenuView roundMenuView;

    @Override
    protected void initData() {

    }

    @Override
    protected String getTitleName() {
        return "智能盒子";
    }

    @Override
    protected void initView() {
        RoundMenuView.RoundMenu roundMenu=new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor( R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor( R.color.gray_9999);
        roundMenu.icon= ImageUtil.drawable2Bitmap(SmartBoxActivity.this,R.mipmap.right);
        roundMenu.onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartToast.show("点击了1");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor( R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor( R.color.gray_9999);
        roundMenu.icon=ImageUtil.drawable2Bitmap(SmartBoxActivity.this,R.mipmap.right);
        roundMenu.onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartToast.show("点击了2");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor( R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor( R.color.gray_9999);
        roundMenu.icon=ImageUtil.drawable2Bitmap(SmartBoxActivity.this,R.mipmap.right);
        roundMenu.onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartToast.show("点击了3");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor(R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor(R.color.gray_9999);
        roundMenu.icon=ImageUtil.drawable2Bitmap(SmartBoxActivity.this,R.mipmap.right);
        roundMenu.onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartToast.show("点击了4");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenuView.setCoreMenu(getResources().getColor( R.color.gray_f2f2),
                getResources().getColor( R.color.gray_9999), getResources().getColor( R.color.gray_9999)
                , 1, 0.43, ImageUtil.drawable2Bitmap(SmartBoxActivity.this,R.drawable.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SmartToast.show("点击了中心圆圈");
                    }
                });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_smart_box;
    }
}

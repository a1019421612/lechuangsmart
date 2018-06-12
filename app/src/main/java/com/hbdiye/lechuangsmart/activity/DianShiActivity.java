package com.hbdiye.lechuangsmart.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.view.View;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.views.RoundMenuView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DianShiActivity extends BaseActivity {


    @BindView(R.id.roundMenuView)
    RoundMenuView roundMenuView;

    @Override
    protected void initData() {

    }

    @Override
    protected String getTitleName() {
        return "电视";
    }

    @Override
    protected void initView() {
        RoundMenuView.RoundMenu roundMenu=new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = getResources().getColor( R.color.gray_9999);
        roundMenu.strokeColor = getResources().getColor( R.color.gray_9999);
        roundMenu.icon=drawable2Bitmap(R.mipmap.right);
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
        roundMenu.icon=drawable2Bitmap(R.mipmap.right);
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
        roundMenu.icon=drawable2Bitmap(R.mipmap.right);
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
        roundMenu.icon=drawable2Bitmap(R.mipmap.right);
        roundMenu.onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               SmartToast.show("点击了4");
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenuView.setCoreMenu(getResources().getColor( R.color.gray_f2f2),
                getResources().getColor( R.color.gray_9999), getResources().getColor( R.color.gray_9999)
                , 1, 0.43,drawable2Bitmap(R.drawable.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SmartToast.show("点击了中心圆圈");
                    }
                });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_dian_shi;
    }
    Bitmap drawable2Bitmap(int drawable) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawable);
        return bitmap;
    }
}

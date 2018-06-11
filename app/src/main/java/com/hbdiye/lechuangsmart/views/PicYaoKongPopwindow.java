package com.hbdiye.lechuangsmart.views;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbdiye.lechuangsmart.R;


/**
 * 拍照的悬浮窗口
 * @author Administrator
 *
 */
public class PicYaoKongPopwindow extends PopupWindow {
	private Context context;
	private LayoutInflater inflater;
	private View view;
	private OnClickListener clickListener;
   public PicYaoKongPopwindow(Context context, OnClickListener clickListener){
	   this.context = context;
	   this.clickListener = clickListener;
	   inflater = LayoutInflater.from(context);
	   view = inflater.inflate(R.layout.pic_yaokong_popupwindows, null);
	   initView();
	   this.setWidth(LayoutParams.MATCH_PARENT);
	   this.setHeight(LayoutParams.WRAP_CONTENT);
	   this.setBackgroundDrawable(new BitmapDrawable());
	 //设置SelectPicPopupWindow弹出窗体动画效果
       this.setAnimationStyle(R.style.AnimBottom);
	   this.setFocusable(true);
	   this.setOutsideTouchable(true);
	   this.setContentView(view);
   }
   public void initView(){
	   TextView tv_ds=view.findViewById(R.id.tv_ds);
	   TextView tv_jdh=view.findViewById(R.id.tv_jdh);
	   TextView tv_kt=view.findViewById(R.id.tv_kt);
	   TextView tv_fs=view.findViewById(R.id.tv_fs);
	   TextView tv_znhz=view.findViewById(R.id.tv_znhz);
	   TextView tv_gf=view.findViewById(R.id.tv_gf);
	   TextView tv_dvd=view.findViewById(R.id.tv_dvd);
	   TextView tv_tyy=view.findViewById(R.id.tv_tyy);
	   TextView tv_xj=view.findViewById(R.id.tv_xj);
	   TextView tv_kqjhq=view.findViewById(R.id.tv_kqjhq);
	   TextView tv_rsq=view.findViewById(R.id.tv_rsq);

	   tv_ds.setOnClickListener(clickListener);
	   tv_jdh.setOnClickListener(clickListener);
	   tv_kt.setOnClickListener(clickListener);
	   tv_fs.setOnClickListener(clickListener);
	   tv_znhz.setOnClickListener(clickListener);
	   tv_gf.setOnClickListener(clickListener);
	   tv_dvd.setOnClickListener(clickListener);
	   tv_tyy.setOnClickListener(clickListener);
	   tv_xj.setOnClickListener(clickListener);
	   tv_kqjhq.setOnClickListener(clickListener);
	   tv_rsq.setOnClickListener(clickListener);
   }
   /**
    * 从底部展示
    * @param parent
    */
   public void showPopupWindowBottom(View parent) {
       if (!this.isShowing()) {  
       	 this.showAtLocation(
       			 parent,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
       } else {  
           this.dismiss();  
       }  
   }
	
}

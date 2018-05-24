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

import com.hbdiye.lechuangsmart.R;


/**
 * 拍照的悬浮窗口
 * @author Administrator
 *
 */
public class GetPhotoPopwindow extends PopupWindow {
	private Context context;
	private LayoutInflater inflater;
	private View view;
	private OnClickListener clickListener;
   public GetPhotoPopwindow(Context context, OnClickListener clickListener){
	   this.context = context;
	   this.clickListener = clickListener;
	   inflater = LayoutInflater.from(context);
	   view = inflater.inflate(R.layout.photo_popupwindows, null);
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
	   RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
	    bt1.setOnClickListener(clickListener);
	    bt2.setOnClickListener(clickListener);
	    bt3.setOnClickListener(clickListener);
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

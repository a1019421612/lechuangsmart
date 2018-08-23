package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.Global.ContentConfig;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.KaiGuanBean;

import java.util.List;

public class KaiGuanAdapter extends BaseQuickAdapter<KaiGuanBean.Devices,BaseViewHolder>{
    private List<Boolean> mList;
    public KaiGuanAdapter( @Nullable List<KaiGuanBean.Devices> data,List<Boolean> mList) {
        super(R.layout.kaiguan_item, data);
        this.mList=mList;
    }

    @Override
    protected void convert(BaseViewHolder helper, KaiGuanBean.Devices item) {
        Glide.with(mContext).load(ContentConfig.drawableByIcon(item.product.icon)).into((ImageView) helper.getView(R.id.iv_device_icon));
        helper.setText(R.id.tv_device_name,item.name);
        int adapterPosition = helper.getAdapterPosition();
        if (mList.get(adapterPosition)){
            helper.setGone(R.id.tv_device_status,false);
            //0关1开
            if (item.product.proatts.size()==1){//单路开关
//                helper.setGone(R.id.checkbox_right,false);
//                helper.setGone(R.id.checkbox_middle,false);
//                helper.setGone(R.id.checkbox_left,true);

                helper.setGone(R.id.iv_kg_right,false);
                helper.setGone(R.id.iv_kg_middle,false);
                helper.setGone(R.id.iv_kg_left,true);
                if (item.deviceAttributes.get(0).value==0){
//                    helper.setChecked(R.id.checkbox_left,false);

                    Glide.with(mContext).load(R.drawable.off).into((ImageView) helper.getView(R.id.iv_kg_left));
                }else {
//                    helper.setChecked(R.id.checkbox_left,true);

                    Glide.with(mContext).load(R.drawable.on).into((ImageView) helper.getView(R.id.iv_kg_left));
                }
            }else if (item.product.proatts.size()==2){//双路开关
//                helper.setGone(R.id.checkbox_left,true);
//                helper.setGone(R.id.checkbox_middle,true);
                helper.setGone(R.id.iv_kg_right,false);
                helper.setGone(R.id.iv_kg_left,true);
                helper.setGone(R.id.iv_kg_middle,true);
                if (item.deviceAttributes.get(0).value==0){
//                    helper.setChecked(R.id.checkbox_left,false);

                    Glide.with(mContext).load(R.drawable.off).into((ImageView) helper.getView(R.id.iv_kg_left));
                }else {
//                    helper.setChecked(R.id.checkbox_left,true);

                    Glide.with(mContext).load(R.drawable.on).into((ImageView) helper.getView(R.id.iv_kg_left));
                }
                if (item.deviceAttributes.get(1).value==0){
//                    helper.setChecked(R.id.checkbox_middle,false);

                    Glide.with(mContext).load(R.drawable.off).into((ImageView) helper.getView(R.id.iv_kg_middle));
                }else {
//                    helper.setChecked(R.id.checkbox_middle,true);

                    Glide.with(mContext).load(R.drawable.on).into((ImageView) helper.getView(R.id.iv_kg_middle));
                }
            }else if (item.product.proatts.size()==3){//三路开关
//                helper.setGone(R.id.checkbox_left,true);
//                helper.setGone(R.id.checkbox_middle,true);
//                helper.setGone(R.id.checkbox_right,true);

                helper.setGone(R.id.iv_kg_left,true);
                helper.setGone(R.id.iv_kg_middle,true);
                helper.setGone(R.id.iv_kg_right,true);
                if (item.deviceAttributes.get(0).value==0){
//                    helper.setChecked(R.id.checkbox_left,false);

                    Glide.with(mContext).load(R.drawable.off).into((ImageView) helper.getView(R.id.iv_kg_left));
                }else {
//                    helper.setChecked(R.id.checkbox_left,true);

                    Glide.with(mContext).load(R.drawable.on).into((ImageView) helper.getView(R.id.iv_kg_left));
                }
                if (item.deviceAttributes.get(1).value==0){
//                    helper.setChecked(R.id.checkbox_middle,false);

                    Glide.with(mContext).load(R.drawable.off).into((ImageView) helper.getView(R.id.iv_kg_middle));
                }else {
//                    helper.setChecked(R.id.checkbox_middle,true);

                    Glide.with(mContext).load(R.drawable.on).into((ImageView) helper.getView(R.id.iv_kg_middle));
                }
                if (item.deviceAttributes.get(2).value==0){
//                    helper.setChecked(R.id.checkbox_right,false);

                    Glide.with(mContext).load(R.drawable.off).into((ImageView) helper.getView(R.id.iv_kg_right));
                }else {
//                    helper.setChecked(R.id.checkbox_right,true);

                    Glide.with(mContext).load(R.drawable.on).into((ImageView) helper.getView(R.id.iv_kg_right));
                }
            }
        }else {
            helper.setGone(R.id.tv_device_status,true);
//            helper.setGone(R.id.checkbox_left,false);
//            helper.setGone(R.id.checkbox_middle,false);
//            helper.setGone(R.id.checkbox_right,false);
            helper.setGone(R.id.iv_kg_left,false);
            helper.setGone(R.id.iv_kg_middle,false);
            helper.setGone(R.id.iv_kg_right,false);
        }
//        helper.addOnClickListener(R.id.checkbox_left);
//        helper.addOnClickListener(R.id.checkbox_middle);
//        helper.addOnClickListener(R.id.checkbox_right);
        helper.addOnClickListener(R.id.iv_kg_left);
        helper.addOnClickListener(R.id.iv_kg_middle);
        helper.addOnClickListener(R.id.iv_kg_right);
    }
}

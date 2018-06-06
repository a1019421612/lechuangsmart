package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.ChuangLianBean;

import java.util.List;

public class ChuangLianAdapter extends BaseQuickAdapter<ChuangLianBean.Devices,BaseViewHolder>{
    private List<Boolean> mList;
    public ChuangLianAdapter( @Nullable List<ChuangLianBean.Devices> data,List<Boolean> mList) {
        super(R.layout.chuanglian_item, data);
        this.mList=mList;
    }

    @Override
    protected void convert(BaseViewHolder helper, ChuangLianBean.Devices item) {
        int adapterPosition = helper.getAdapterPosition();
        helper.setText(R.id.tv_device_name,item.name);
        if (mList.get(adapterPosition)){
            helper.setGone(R.id.tv_device_status,false);
            //0关1开
            if (item.product.proatts.size()==1){//单路开关
                helper.setGone(R.id.checkbox_right,false);
                helper.setGone(R.id.checkbox_left,true);
                if (item.deviceAttributes.get(0).value==0){
                    helper.setChecked(R.id.checkbox_left,false);
                }else {
                    helper.setChecked(R.id.checkbox_left,true);
                }
            }else if (item.product.proatts.size()==2){//双路开关
                helper.setGone(R.id.checkbox_left,true);
                helper.setGone(R.id.checkbox_right,true);
                if (item.deviceAttributes.get(0).value==0){
                    helper.setChecked(R.id.checkbox_left,false);
                }else {
                    helper.setChecked(R.id.checkbox_left,true);
                }
                if (item.deviceAttributes.get(1).value==0){
                    helper.setChecked(R.id.checkbox_right,false);
                }else {
                    helper.setChecked(R.id.checkbox_right,true);
                }
            }
        }else {
            helper.setGone(R.id.tv_device_status,true);
            helper.setGone(R.id.checkbox_left,false);
            helper.setGone(R.id.checkbox_right,false);
        }

        helper.addOnClickListener(R.id.checkbox_left);
        helper.addOnClickListener(R.id.checkbox_right);
    }
}

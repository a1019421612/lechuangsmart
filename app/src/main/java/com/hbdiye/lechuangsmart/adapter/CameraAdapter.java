package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.R;
import com.videogo.openapi.bean.EZDeviceInfo;

import java.util.List;

public class CameraAdapter extends BaseQuickAdapter<EZDeviceInfo,BaseViewHolder> {
    public CameraAdapter(@Nullable List<EZDeviceInfo> data) {
        super(R.layout.device_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EZDeviceInfo item) {
        helper.setText(R.id.tv_name,item.getDeviceName());
        int status = item.getStatus();
        if (status==1){
            //在线
            helper.setTextColor(R.id.tv_status,mContext.getResources().getColor(R.color.state_normal_text));
            helper.setText(R.id.tv_status,"设备在线");
        }else {
            //不在线
            helper.setTextColor(R.id.tv_status,mContext.getResources().getColor(R.color.common_hint_text));
            helper.setText(R.id.tv_status,"设备离线");
        }
    }
}

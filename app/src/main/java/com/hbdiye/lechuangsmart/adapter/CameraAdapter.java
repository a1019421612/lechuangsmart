package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
        if (item!=null){
            if (item.getStatus()==2){
                helper.setGone(R.id.item_offline,true);
                helper.setGone(R.id.offline_bg,true);
            }else {
                helper.setGone(R.id.item_offline,false);
                helper.setGone(R.id.offline_bg,false);
            }
            helper.setText(R.id.camera_name_tv,item.getDeviceName());
            helper.setVisible(R.id.item_icon,true);
            String imageUrl = item.getDeviceCover();
            if(!TextUtils.isEmpty(imageUrl)) {
                Glide.with(mContext).load(imageUrl).into((ImageView)helper.getView(R.id.item_icon));
            }
        }
        helper.addOnClickListener(R.id.tab_remoteplayback_btn);
        helper.addOnClickListener(R.id.tab_alarmlist_btn);
        helper.addOnClickListener(R.id.tab_setdevice_btn);
    }
}

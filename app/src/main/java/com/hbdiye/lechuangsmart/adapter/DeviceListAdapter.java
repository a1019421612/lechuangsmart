package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.Global.ContentConfig;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.RemoteDeviceBean;
import com.hbdiye.lechuangsmart.bean.YaoKongBean;

import java.util.List;

public class DeviceListAdapter extends BaseQuickAdapter<RemoteDeviceBean.Data,BaseViewHolder>{
    public DeviceListAdapter(@Nullable List<RemoteDeviceBean.Data> data) {
        super(R.layout.device_hw_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteDeviceBean.Data item) {
        helper.setText(R.id.tv_device_name,item.device_name);
    }
}

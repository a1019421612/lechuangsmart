package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.R;
import com.kookong.app.data.BrandList;

import java.util.List;

public class HWDeviceAdapter extends BaseQuickAdapter<BrandList.Brand,BaseViewHolder>{
    public HWDeviceAdapter( @Nullable List<BrandList.Brand> data) {
        super(R.layout.scene_device_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BrandList.Brand item) {
        helper.setGone(R.id.iv_device_icon_item,false);
        helper.setText(R.id.tv_scene_device_name,item.cname);
    }
}

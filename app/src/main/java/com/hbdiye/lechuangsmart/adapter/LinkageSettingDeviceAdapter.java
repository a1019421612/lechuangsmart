package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.Global.ContentConfig;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.LinkageDeviceBean;
import com.hbdiye.lechuangsmart.bean.SceneDeviceBean;

import java.util.List;

public class LinkageSettingDeviceAdapter extends BaseQuickAdapter<LinkageDeviceBean.Devices,BaseViewHolder>{
    public LinkageSettingDeviceAdapter(@Nullable List<LinkageDeviceBean.Devices> data) {
        super(R.layout.scene_device_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LinkageDeviceBean.Devices item) {
        helper.setText(R.id.tv_scene_device_name,item.name);
        Glide.with(mContext).load(ContentConfig.drawableByIcon(item.product.icon)).into((ImageView) helper.getView(R.id.iv_device_icon_item));
    }
}

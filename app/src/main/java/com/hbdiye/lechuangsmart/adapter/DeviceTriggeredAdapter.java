package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.DeviceTriggerBean;
import com.hbdiye.lechuangsmart.bean.LinkageDeviceBean;

import java.util.List;

public class DeviceTriggeredAdapter extends BaseQuickAdapter<DeviceTriggerBean.Devices,BaseViewHolder>{
    public DeviceTriggeredAdapter(@Nullable List<DeviceTriggerBean.Devices> data) {
        super(R.layout.scene_device_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceTriggerBean.Devices item) {
        helper.setText(R.id.tv_scene_device_name,item.name);
    }
}

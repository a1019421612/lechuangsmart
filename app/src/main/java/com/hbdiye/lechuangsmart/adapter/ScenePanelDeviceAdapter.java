package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.ScenePanelDeviceBean;

import java.util.List;

public class ScenePanelDeviceAdapter extends BaseQuickAdapter<ScenePanelDeviceBean.Devices,BaseViewHolder> {
    public ScenePanelDeviceAdapter(@Nullable List<ScenePanelDeviceBean.Devices> data) {
        super(R.layout.scene_panel_device_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScenePanelDeviceBean.Devices item) {
        helper.setText(R.id.tv_scene_device_name,item.name);
    }
}

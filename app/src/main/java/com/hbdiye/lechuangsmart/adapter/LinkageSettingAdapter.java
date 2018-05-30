package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.Global.ContentConfig;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.LinkageSettingBean;
import com.hbdiye.lechuangsmart.bean.SceneDeviceBean;

import java.util.List;

public class LinkageSettingAdapter extends BaseQuickAdapter<LinkageSettingBean.Lts,BaseViewHolder>{
    public LinkageSettingAdapter(@Nullable List<LinkageSettingBean.Lts> data) {
        super(R.layout.scene_setting_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LinkageSettingBean.Lts item) {
        helper.setText(R.id.tv_name,"联动设备");
        helper.setText(R.id.tv_scene_setting_name,item.device.name);
        helper.setText(R.id.tv_scene_setting_switch,item.proAct.name);
        helper.setText(R.id.tv_scene_setting_time, ContentConfig.secToTime(item.delaytime));
        helper.addOnClickListener(R.id.iv_scene_setting_del);
        helper.addOnClickListener(R.id.tv_scene_setting_time);
        helper.addOnClickListener(R.id.tv_scene_setting_switch);
    }
}

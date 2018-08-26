package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.RoomBean;
import com.hbdiye.lechuangsmart.bean.ScenePanelBean;

import java.util.List;

public class ScenePanelAdapter extends BaseQuickAdapter<ScenePanelBean.Devices,BaseViewHolder>{
    private boolean isShow=true;
    public ScenePanelAdapter(@Nullable List<ScenePanelBean.Devices> data) {
        super(R.layout.scene_panel_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScenePanelBean.Devices item) {
        if (isShow){
            helper.setGone(R.id.ll_scene_item_del,false);
            helper.setGone(R.id.ll_scene_item_edt,false);
        }else {
            helper.setGone(R.id.ll_scene_item_del,true);
            helper.setGone(R.id.ll_scene_item_edt,true);
        }
        helper.setText(R.id.tv_scene_device_name,item.name);

        helper.addOnClickListener(R.id.ll_scene_device);
        helper.addOnClickListener(R.id.ll_scene_item_del);
        helper.addOnClickListener(R.id.ll_scene_item_edt);
    }
    public void sceneStatusChange(boolean status){
        isShow=status;
        notifyDataSetChanged();
    }
}

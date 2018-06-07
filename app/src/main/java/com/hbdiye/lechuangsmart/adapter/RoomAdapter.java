package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.RoomBean;

import java.util.List;

public class RoomAdapter extends BaseQuickAdapter<RoomBean.Rooms,BaseViewHolder>{
    public RoomAdapter(@Nullable List<RoomBean.Rooms> data) {
        super(R.layout.scene_device_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RoomBean.Rooms item) {
        helper.setText(R.id.tv_scene_device_name,item.name);
    }
}

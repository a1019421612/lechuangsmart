package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.YaoKongListBean;

import java.util.List;

public class LinkageYaoKongQiListAdapter extends BaseQuickAdapter<YaoKongListBean.Irremotes,BaseViewHolder>{
    public LinkageYaoKongQiListAdapter(@Nullable List<YaoKongListBean.Irremotes> data) {
        super(R.layout.scene_device_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, YaoKongListBean.Irremotes item) {
        helper.setText(R.id.tv_scene_device_name,item.name);

    }
}

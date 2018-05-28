package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.AnFangBean;

import java.util.List;

public class AnFangAdapter extends BaseQuickAdapter<AnFangBean.Devices,BaseViewHolder>{
    public AnFangAdapter(@Nullable List<AnFangBean.Devices> data) {
        super(R.layout.anfang_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AnFangBean.Devices item) {
        helper.setText(R.id.tv_device_name,item.name);
    }
}

package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.BrandListBean;
import com.hbdiye.lechuangsmart.bean.ModeListBean;

import java.util.List;

public class ModeListAdapter extends BaseQuickAdapter<ModeListBean.Data,BaseViewHolder>{
    public ModeListAdapter(@Nullable List<ModeListBean.Data> data) {
        super(R.layout.device_hw_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ModeListBean.Data item) {
        helper.setText(R.id.tv_device_name,item.bn);
    }
}

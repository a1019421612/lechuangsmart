package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.BrandListBean;
import com.hbdiye.lechuangsmart.bean.RemoteDeviceBean;

import java.util.List;

public class BrandListAdapter extends BaseQuickAdapter<BrandListBean.Data,BaseViewHolder>{
    public BrandListAdapter(@Nullable List<BrandListBean.Data> data) {
        super(R.layout.device_hw_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BrandListBean.Data item) {
        helper.setText(R.id.tv_device_name,item.bn);
    }
}

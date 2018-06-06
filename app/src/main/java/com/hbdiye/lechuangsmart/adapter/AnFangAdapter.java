package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.Global.ContentConfig;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.AnFangBean;

import java.util.List;

public class AnFangAdapter extends BaseQuickAdapter<AnFangBean.Devices,BaseViewHolder>{
    private List<Boolean> mList;
    public AnFangAdapter(@Nullable List<AnFangBean.Devices> data,List<Boolean> mList) {
        super(R.layout.anfang_item, data);
        this.mList=mList;
    }

    @Override
    protected void convert(BaseViewHolder helper, AnFangBean.Devices item) {
        int adapterPosition = helper.getAdapterPosition();
        Glide.with(mContext).load(ContentConfig.drawableByIcon(item.product.icon)).into((ImageView) helper.getView(R.id.iv_device_icon));
        helper.setText(R.id.tv_device_name,item.name);
        Log.e("aaa",mList.get(adapterPosition)+"");
        if (mList.get(adapterPosition)){
            helper.setGone(R.id.tv_device_status,false);
        }else {
            helper.setGone(R.id.tv_device_status,true);
        }
    }
}

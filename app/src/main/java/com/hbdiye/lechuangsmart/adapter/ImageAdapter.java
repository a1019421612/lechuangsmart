package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.ImageBean;

import java.util.List;

public class ImageAdapter extends BaseQuickAdapter<ImageBean,BaseViewHolder>{
    public ImageAdapter( @Nullable List<ImageBean> data) {
        super(R.layout.image_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageBean item) {
        Glide.with(mContext).load(item.getDrawableId()).into((ImageView) helper.getView(R.id.iv_image_item));
    }
}

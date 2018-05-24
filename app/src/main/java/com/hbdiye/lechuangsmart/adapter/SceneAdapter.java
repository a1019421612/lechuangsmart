package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.bean.TestBean;

import java.util.List;

public class SceneAdapter extends BaseQuickAdapter<TestBean,BaseViewHolder>{


    public SceneAdapter(int layoutResId, @Nullable List<TestBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TestBean item) {

    }
}

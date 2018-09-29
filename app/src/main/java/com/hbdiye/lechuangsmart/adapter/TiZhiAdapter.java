package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.TiZhiBean;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TiZhiAdapter extends BaseQuickAdapter<TiZhiBean.Data.Lists,BaseViewHolder>{
    public TiZhiAdapter(@Nullable List<TiZhiBean.Data.Lists> data) {
        super(R.layout.tizhi_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TiZhiBean.Data.Lists item) {
        String datetime = item.datetime;
        Pattern p = Pattern.compile("\\s");
        Matcher m = p.matcher(datetime);
        datetime = m.replaceAll("\n");
        helper.setText(R.id.tv_date,datetime);
    }
}

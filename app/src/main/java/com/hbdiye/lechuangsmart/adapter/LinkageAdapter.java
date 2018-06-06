package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.LinkageBean;

import java.util.List;

public class LinkageAdapter extends BaseQuickAdapter<LinkageBean.Linkages,BaseViewHolder>{
    private boolean isShow=true;
    public LinkageAdapter(@Nullable List<LinkageBean.Linkages> data) {
        super(R.layout.linkage_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LinkageBean.Linkages item) {
        helper.setText(R.id.tv_linkage_name,item.name);
        if (isShow){
            helper.setGone(R.id.ll_linkage_item_del,false);
            helper.setGone(R.id.ll_linkage_item_edt,false);
            helper.setGone(R.id.checkbox_switch,true);
            helper.setChecked(R.id.checkbox_switch,false);
            if (item.active==1){
                helper.setChecked(R.id.checkbox_switch,true);
            }
        }else {
            helper.setGone(R.id.ll_linkage_item_del,true);
            helper.setGone(R.id.ll_linkage_item_edt,true);
            helper.setGone(R.id.checkbox_switch,false);
        }
        helper.addOnClickListener(R.id.ll_linkage_item);
        helper.addOnClickListener(R.id.ll_linkage_item_del);
        helper.addOnClickListener(R.id.ll_linkage_item_edt);
        helper.addOnClickListener(R.id.checkbox_switch);
    }
    public void sceneStatusChange(boolean status){
        isShow=status;
        notifyDataSetChanged();
    }
}

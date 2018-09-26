package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.InfraredBean;

import java.util.ArrayList;
import java.util.List;

public class RemoteDeviceListAdapter extends BaseQuickAdapter<InfraredBean.Remote_list.Ir_list,BaseViewHolder>{

    private boolean isShow=true;
    public RemoteDeviceListAdapter(@Nullable ArrayList<InfraredBean.Remote_list.Ir_list> data) {
        super(R.layout.infrared_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InfraredBean.Remote_list.Ir_list item) {
        if (isShow){
            helper.setGone(R.id.ll_scene_item_del,false);
            helper.setGone(R.id.ll_scene_item_edt,false);
        }else {
            helper.setGone(R.id.ll_scene_item_del,true);
            helper.setGone(R.id.ll_scene_item_edt,true);
        }
//        Glide.with(mContext).load(ContentConfig.sceneIcon(item.icon)).into((ImageView) helper.getView(R.id.iv_scene_icon));
        helper.setText(R.id.tv_scene_name,item.name);
        helper.addOnClickListener(R.id.ll_scene_item_del);
        helper.addOnClickListener(R.id.ll_scene_item_edt);
    }
    public void sceneStatusChange(boolean status){
        isShow=status;
        notifyDataSetChanged();
    }
}

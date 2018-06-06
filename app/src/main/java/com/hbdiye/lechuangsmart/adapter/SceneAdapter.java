package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.Global.ContentConfig;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.SceneBean;
import com.hbdiye.lechuangsmart.bean.TestBean;

import java.util.List;

public class SceneAdapter extends BaseQuickAdapter<SceneBean.Scenes,BaseViewHolder>{

    private boolean isShow=true;
    public SceneAdapter(@Nullable List<SceneBean.Scenes> data) {
        super(R.layout.scene_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SceneBean.Scenes item) {
        if (isShow){
            helper.setGone(R.id.ll_scene_item_del,false);
            helper.setGone(R.id.ll_scene_item_edt,false);
        }else {
            helper.setGone(R.id.ll_scene_item_del,true);
            helper.setGone(R.id.ll_scene_item_edt,true);
        }
        Glide.with(mContext).load(ContentConfig.sceneIcon(item.icon)).into((ImageView) helper.getView(R.id.iv_scene_icon));
        helper.setText(R.id.tv_scene_name,item.name);
        helper.addOnClickListener(R.id.ll_scene_item);
        helper.addOnClickListener(R.id.ll_scene_item_del);
        helper.addOnClickListener(R.id.ll_scene_item_edt);
    }
    public void sceneStatusChange(boolean status){
        isShow=status;
        notifyDataSetChanged();
    }
}

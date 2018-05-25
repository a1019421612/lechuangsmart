package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.FamilyNameBean;

import java.util.List;

public class FamilyMemberAdapter extends BaseQuickAdapter<FamilyNameBean.FamilyUsers,BaseViewHolder>{
    public FamilyMemberAdapter(@Nullable List<FamilyNameBean.FamilyUsers> data) {
        super(R.layout.familymember_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FamilyNameBean.FamilyUsers item) {
        helper.setText(R.id.tv_member_name,item.name);
        helper.setText(R.id.tv_member_phone,item.mobilephone);
    }
}

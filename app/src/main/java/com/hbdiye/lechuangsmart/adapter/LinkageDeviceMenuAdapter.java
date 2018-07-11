package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.R;
import com.kookong.app.data.IrData;

import java.util.List;

public class LinkageDeviceMenuAdapter extends BaseQuickAdapter<IrData.IrKey,BaseViewHolder>{
    public LinkageDeviceMenuAdapter(@Nullable List<IrData.IrKey> data) {
        super(R.layout.device_menu_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IrData.IrKey item) {
        helper.setText(R.id.tv_device_menu,item.fname);
    }
}

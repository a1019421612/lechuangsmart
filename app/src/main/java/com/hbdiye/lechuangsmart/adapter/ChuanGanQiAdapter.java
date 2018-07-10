package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.Global.ContentConfig;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.ChuanGanQiBean;

import java.util.List;

public class ChuanGanQiAdapter extends BaseQuickAdapter<ChuanGanQiBean.Devices,BaseViewHolder>{
    private List<Boolean> mList;
    public ChuanGanQiAdapter( @Nullable List<ChuanGanQiBean.Devices> data,List<Boolean> mList) {
        super(R.layout.chuanganqi_item, data);
        this.mList=mList;
    }

    @Override
    protected void convert(BaseViewHolder helper, ChuanGanQiBean.Devices item) {
        Glide.with(mContext).load(ContentConfig.drawableByIcon(item.product.icon)).into((ImageView) helper.getView(R.id.iv_device_icon));
        int layoutPosition = helper.getAdapterPosition();
        helper.setText(R.id.tv_device_name,item.name);
        if (mList.get(layoutPosition)){
            //在线
            helper.setGone(R.id.tv_device_status,false);
            if (item.product.modelPath.equals("pro_sensor_realtime")){
                helper.setGone(R.id.ll_device_attr,true);
                List<ChuanGanQiBean.Devices.DeviceAttributes> deviceAttributes = item.deviceAttributes;
                if (deviceAttributes!=null){
                        for (int i = 0; i < deviceAttributes.size(); i++) {
                            String proAttID = deviceAttributes.get(i).proAttID;
                            if (proAttID.equals("12")){
                                //温度
                                int value = deviceAttributes.get(i).value;
//                                int wd = Math.round(value / 100);
                                String wd = String.format("%.1f", (float)value / 100);
                                helper.setText(R.id.tv_wd,wd+"℃");
                            }
                            if (proAttID.equals("11")){
                                //湿度
                                int value = deviceAttributes.get(i).value;
//                                int round = Math.round(value / 100);
                                String sd = String.format("%.1f", (float)value / 100);
                                helper.setText(R.id.tv_sd,sd+"%");
                            }
                            if (proAttID.equals("13")){
                                //pm2.5
                                int value = deviceAttributes.get(i).value;
                                helper.setText(R.id.tv_pm,value+"PPM");
                            }
                        }
                }
            }
        }else {
            //离线
            helper.setGone(R.id.tv_device_status,true);
            helper.setGone(R.id.ll_device_attr,false);
        }
    }
}

package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.Global.ContentConfig;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.ChuanGanQiBean;
import com.hbdiye.lechuangsmart.bean.RoomDeviceBean;

import java.util.ArrayList;
import java.util.List;

public class RoomDeviceAdapter extends BaseQuickAdapter<RoomDeviceBean.Devices,BaseViewHolder>{

    private List<Boolean> mList;
    private List<Integer> checkboxUserIdList ;
    public RoomDeviceAdapter(@Nullable List<RoomDeviceBean.Devices> data,List<Boolean> mList) {
        super(R.layout.room_device_item, data);
        this.mList=mList;
        checkboxUserIdList=new ArrayList<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, RoomDeviceBean.Devices item) {
        int adapterPosition = helper.getAdapterPosition();

        Glide.with(mContext).load(ContentConfig.drawableByIcon(item.product.icon)).into((ImageView) helper.getView(R.id.iv_device_icon));
        String modelPath = item.product.modelPath;
        helper.setText(R.id.tv_device_name,item.name+"--"+modelPath);
        if (mList.get(adapterPosition)){
            //设备在线
            helper.setGone(R.id.tv_device_status,false);
            //检测器
            if (modelPath.equals("pro_sensor_realtime")){
                helper.setGone(R.id.ll_device_attr,true);

                List<RoomDeviceBean.Devices.DeviceAttributes> deviceAttributes = item.deviceAttributes;
                if (deviceAttributes!=null){
                    for (int i = 0; i < deviceAttributes.size(); i++) {
                        String proAttID = deviceAttributes.get(i).proAttID;
                        if (proAttID.equals("11")){
                            //温度
                            int value = deviceAttributes.get(i).value;
                            String wd = String.format("%.1f", (float)value / 100);
                            helper.setText(R.id.tv_wd,wd+"℃");
                        }
                        if (proAttID.equals("12")){
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
            }else if (modelPath.equals("pro_switch")){
                checkboxUserIdList.add(adapterPosition);
                helper.setTag(R.id.ll_root,adapterPosition);
                int tag = (int) helper.getView(R.id.ll_root).getTag();
                if (checkboxUserIdList.contains(tag)){

                    //开关
                    //0关1开
                    if (item.product.proatts.size()==1){//单路开关
                        helper.setGone(R.id.ll_checkbox,false);

                        helper.setGone(R.id.ll_checkbox,true);
                        helper.setGone(R.id.checkbox_right,false);
                        helper.setGone(R.id.checkbox_middle,false);
                        helper.setGone(R.id.checkbox_left,true);
                        if (item.deviceAttributes.get(0).value==0){
                            helper.setChecked(R.id.checkbox_left,false);
                        }else {
                            helper.setChecked(R.id.checkbox_left,true);
                        }
                    }else if (item.product.proatts.size()==2){//双路开关

                        helper.setGone(R.id.ll_checkbox,false);

                        helper.setGone(R.id.ll_checkbox,true);
                        helper.setGone(R.id.checkbox_left,true);
                        helper.setGone(R.id.checkbox_right,false);
                        helper.setGone(R.id.checkbox_middle,true);
                        if (item.deviceAttributes.get(0).value==0){
                            helper.setChecked(R.id.checkbox_left,false);
                        }else {
                            helper.setChecked(R.id.checkbox_left,true);
                        }
                        if (item.deviceAttributes.get(1).value==0){
                            helper.setChecked(R.id.checkbox_middle,false);
                        }else {
                            helper.setChecked(R.id.checkbox_middle,true);
                        }
                    }else if (item.product.proatts.size()==3){//三路开关

                        helper.setGone(R.id.ll_checkbox,false);

                        helper.setGone(R.id.ll_checkbox,true);
                        helper.setGone(R.id.checkbox_left,true);
                        helper.setGone(R.id.checkbox_right,true);
                        helper.setGone(R.id.checkbox_middle,true);
                        if (item.deviceAttributes.get(0).value==0){
                            helper.setChecked(R.id.checkbox_left,false);
                        }else {
                            helper.setChecked(R.id.checkbox_left,true);
                        }
                        if (item.deviceAttributes.get(1).value==0){
                            helper.setChecked(R.id.checkbox_middle,false);
                        }else {
                            helper.setChecked(R.id.checkbox_middle,true);
                        }
                        if (item.deviceAttributes.get(2).value==0){
                            helper.setChecked(R.id.checkbox_right,false);
                        }else {
                            helper.setChecked(R.id.checkbox_right,true);
                        }
                    }
                }
            }else {
                helper.setGone(R.id.ll_checkbox,false);
                helper.setGone(R.id.checkbox_left,false);
                helper.setGone(R.id.checkbox_middle,false);
                helper.setGone(R.id.checkbox_right,false);
                helper.setGone(R.id.ll_device_attr,false);
            }

        }else {
            //设备离线
            helper.setGone(R.id.tv_device_status,true);
            helper.setGone(R.id.ll_checkbox,false);
            helper.setGone(R.id.checkbox_right,false);
            helper.setGone(R.id.checkbox_middle,false);
            helper.setGone(R.id.checkbox_left,false);
        }
    }
}

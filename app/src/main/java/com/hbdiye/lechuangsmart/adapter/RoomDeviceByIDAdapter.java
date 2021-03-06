package com.hbdiye.lechuangsmart.adapter;

import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hbdiye.lechuangsmart.Global.ContentConfig;
import com.hbdiye.lechuangsmart.R;
import com.hbdiye.lechuangsmart.bean.RoomDeviceBean;

import java.util.ArrayList;
import java.util.List;

public class RoomDeviceByIDAdapter extends BaseQuickAdapter<RoomDeviceBean.Devices,BaseViewHolder>{
    private boolean isShow=true;
    private List<Boolean> mList;
    private List<Integer> checkboxUserIdList ;
    public RoomDeviceByIDAdapter(@Nullable List<RoomDeviceBean.Devices> data, List<Boolean> mList) {
        super(R.layout.room_device_byid_item, data);
        this.mList=mList;
        checkboxUserIdList=new ArrayList<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, RoomDeviceBean.Devices item) {
        int adapterPosition = helper.getAdapterPosition();

        Glide.with(mContext).load(ContentConfig.drawableByIcon(item.product.icon)).into((ImageView) helper.getView(R.id.iv_device_icon));
        String modelPath = item.product.modelPath;
        helper.setText(R.id.tv_device_name,item.name);
        if (mList.get(adapterPosition)){
            //设备在线
            helper.setGone(R.id.tv_device_status,false);
            //检测器
            if (modelPath.equals("pro_sensor_realtime")){
                helper.setGone(R.id.ll_device_attr,true);
                helper.setGone(R.id.ll_checkbox,false);
                List<RoomDeviceBean.Devices.DeviceAttributes> deviceAttributes = item.deviceAttributes;
                List<RoomDeviceBean.Devices.Product.Proatts> proatts = item.product.proatts;
                if (proatts!=null){
                    for (int i = 0; i < proatts.size(); i++) {
                        String attributeID = proatts.get(i).attributeID;
                        String id = proatts.get(i).id;
                        if (attributeID.equals("ATT0011")){
                            //温度
                            for (int j = 0; j < deviceAttributes.size(); j++) {
                                String proAttID = deviceAttributes.get(j).proAttID;
                                if (proAttID.equals(id)){
                                    int value = deviceAttributes.get(j).value;
                                    String wd = String.format("%.1f", (float)value / 100);
                                    helper.setText(R.id.tv_wd,wd+"℃");
                                }
                            }
                        }else if (attributeID.equals("ATT0010")){
                            //湿度
                            for (int j = 0; j < deviceAttributes.size(); j++) {
                                String proAttID = deviceAttributes.get(j).proAttID;
                                if (proAttID.equals(id)){
                                    int value = deviceAttributes.get(j).value;
//                                int round = Math.round(value / 100);
                                    String sd = String.format("%.1f", (float)value / 100);
                                    helper.setText(R.id.tv_sd,sd+"%");
                                }
                            }

                        }else if (attributeID.equals("ATT00121")){
                            //pm2.5
                            for (int j = 0; j < deviceAttributes.size(); j++) {
                                String proAttID = deviceAttributes.get(j).proAttID;
                                if (proAttID.equals(id)){
                                    int value = deviceAttributes.get(j).value;
                                    helper.setText(R.id.tv_pm,value+"PPM");
                                }
                            }

                        }
                    }
                }
//                if (deviceAttributes!=null){
//                    for (int i = 0; i < deviceAttributes.size(); i++) {
//                        String proAttID = deviceAttributes.get(i).proAttID;
//                        if (proAttID.equals("ATT0011")){
//                            //温度
//                            int value = deviceAttributes.get(i).value;
//                            String wd = String.format("%.1f", (float)value / 100);
//                            helper.setText(R.id.tv_wd,wd+"℃");
//                        }
//                        if (proAttID.equals("ATT0010")){
//                            //湿度
//                            int value = deviceAttributes.get(i).value;
////                                int round = Math.round(value / 100);
//                            String sd = String.format("%.1f", (float)value / 100);
//                            helper.setText(R.id.tv_sd,sd+"%");
//                        }
//                        if (proAttID.equals("ATT00121")){
//                            //pm2.5
//                            int value = deviceAttributes.get(i).value;
//                            helper.setText(R.id.tv_pm,value+"PPM");
//                        }
//                    }
//                }
            }else if (modelPath.equals("pro_switch")){
                helper.setGone(R.id.ll_device_attr,false);
                checkboxUserIdList.add(adapterPosition);
                helper.setTag(R.id.ll_root,adapterPosition);
                int tag = (int) helper.getView(R.id.ll_root).getTag();
                if (checkboxUserIdList.contains(tag)){

                    //开关
                    //0关1开
                    if (item.product.proatts.size()==1){//单路开关
                        helper.setGone(R.id.ll_checkbox,false);

                        helper.setGone(R.id.ll_checkbox,true);
//                        helper.setGone(R.id.checkbox_right,false);
//                        helper.setGone(R.id.checkbox_middle,false);
//                        helper.setGone(R.id.checkbox_left,true);

                        helper.setGone(R.id.iv_room_left,true);
                        helper.setGone(R.id.iv_room_middle,false);
                        helper.setGone(R.id.iv_room_right,false);

                        if (item.deviceAttributes.get(0).value==0){
//                            helper.setChecked(R.id.checkbox_left,false);

                            Glide.with(mContext).load(R.drawable.off).into((ImageView) helper.getView(R.id.iv_room_left));

                        }else {
//                            helper.setChecked(R.id.checkbox_left,true);

                            Glide.with(mContext).load(R.drawable.on).into((ImageView) helper.getView(R.id.iv_room_left));

                        }
                    }else if (item.product.proatts.size()==2){//双路开关

                        helper.setGone(R.id.ll_checkbox,false);

                        helper.setGone(R.id.ll_checkbox,true);
//                        helper.setGone(R.id.checkbox_left,true);
//                        helper.setGone(R.id.checkbox_right,false);
//                        helper.setGone(R.id.checkbox_middle,true);

                        helper.setGone(R.id.iv_room_left,true);
                        helper.setGone(R.id.iv_room_right,false);
                        helper.setGone(R.id.iv_room_middle,true);

                        if (item.deviceAttributes.get(0).value==0){
//                            helper.setChecked(R.id.checkbox_left,false);

                            Glide.with(mContext).load(R.drawable.off).into((ImageView) helper.getView(R.id.iv_room_left));

                        }else {
//                            helper.setChecked(R.id.checkbox_left,true);

                            Glide.with(mContext).load(R.drawable.on).into((ImageView) helper.getView(R.id.iv_room_left));

                        }
                        if (item.deviceAttributes.get(1).value==0){
//                            helper.setChecked(R.id.checkbox_middle,false);

                            Glide.with(mContext).load(R.drawable.off).into((ImageView) helper.getView(R.id.iv_room_middle));

                        }else {
//                            helper.setChecked(R.id.checkbox_middle,true);

                            Glide.with(mContext).load(R.drawable.on).into((ImageView) helper.getView(R.id.iv_room_middle));

                        }
                    }else if (item.product.proatts.size()==3){//三路开关

                        helper.setGone(R.id.ll_checkbox,false);

                        helper.setGone(R.id.ll_checkbox,true);
//                        helper.setGone(R.id.checkbox_left,true);
//                        helper.setGone(R.id.checkbox_right,true);
//                        helper.setGone(R.id.checkbox_middle,true);

                        helper.setGone(R.id.iv_room_left,true);
                        helper.setGone(R.id.iv_room_right,true);
                        helper.setGone(R.id.iv_room_middle,true);

                        if (item.deviceAttributes.get(0).value==0){
//                            helper.setChecked(R.id.checkbox_left,false);

                            Glide.with(mContext).load(R.drawable.off).into((ImageView) helper.getView(R.id.iv_room_left));

                        }else {
//                            helper.setChecked(R.id.checkbox_left,true);

                            Glide.with(mContext).load(R.drawable.on).into((ImageView) helper.getView(R.id.iv_room_left));

                        }
                        if (item.deviceAttributes.get(1).value==0){
//                            helper.setChecked(R.id.checkbox_middle,false);

                            Glide.with(mContext).load(R.drawable.off).into((ImageView) helper.getView(R.id.iv_room_middle));

                        }else {
//                            helper.setChecked(R.id.checkbox_middle,true);

                            Glide.with(mContext).load(R.drawable.on).into((ImageView) helper.getView(R.id.iv_room_middle));

                        }
                        if (item.deviceAttributes.get(2).value==0){
//                            helper.setChecked(R.id.checkbox_right,false);

                            Glide.with(mContext).load(R.drawable.off).into((ImageView) helper.getView(R.id.iv_room_right));

                        }else {
//                            helper.setChecked(R.id.checkbox_right,true);

                            Glide.with(mContext).load(R.drawable.on).into((ImageView) helper.getView(R.id.iv_room_right));

                        }
                    }
                }
            }else {
                helper.setGone(R.id.ll_checkbox,false);
//                helper.setGone(R.id.checkbox_left,false);
//                helper.setGone(R.id.checkbox_middle,false);
//                helper.setGone(R.id.checkbox_right,false);

                helper.setGone(R.id.iv_room_left,false);
                helper.setGone(R.id.iv_room_middle,false);
                helper.setGone(R.id.iv_room_right,false);

                helper.setGone(R.id.ll_device_attr,false);
            }

        }else {
            //设备离线
            helper.setGone(R.id.tv_device_status,true);
            helper.setGone(R.id.ll_checkbox,false);
//            helper.setGone(R.id.checkbox_right,false);
//            helper.setGone(R.id.checkbox_middle,false);
//            helper.setGone(R.id.checkbox_left,false);
            helper.setGone(R.id.ll_device_attr,false);
            helper.setGone(R.id.iv_room_right,false);
            helper.setGone(R.id.iv_room_middle,false);
            helper.setGone(R.id.iv_room_left,false);
        }
        if (isShow){
            helper.setGone(R.id.ll_scene_item_del,false);
            helper.setGone(R.id.ll_scene_item_edt,false);
        }else {
            helper.setGone(R.id.ll_scene_item_del,true);
            helper.setGone(R.id.ll_scene_item_edt,true);

            helper.setGone(R.id.tv_device_status,false);
            helper.setGone(R.id.ll_checkbox,false);
//            helper.setGone(R.id.checkbox_left,false);
//            helper.setGone(R.id.checkbox_middle,false);
//            helper.setGone(R.id.checkbox_right,false);

            helper.setGone(R.id.iv_room_left,false);
            helper.setGone(R.id.iv_room_middle,false);
            helper.setGone(R.id.iv_room_right,false);

            helper.setGone(R.id.ll_device_attr,false);
        }
//        helper.addOnClickListener(R.id.checkbox_left);
//        helper.addOnClickListener(R.id.checkbox_middle);
//        helper.addOnClickListener(R.id.checkbox_right);

        helper.addOnClickListener(R.id.iv_room_left);
        helper.addOnClickListener(R.id.iv_room_middle);
        helper.addOnClickListener(R.id.iv_room_right);

        helper.addOnClickListener(R.id.ll_scene_device);
        helper.addOnClickListener(R.id.ll_scene_item_del);
        helper.addOnClickListener(R.id.ll_scene_item_edt);
    }
    public void sceneStatusChange(boolean status){
        isShow=status;
        notifyDataSetChanged();
    }
}

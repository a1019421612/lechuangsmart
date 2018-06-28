package com.hbdiye.lechuangsmart.util;

import android.content.Context;
import android.widget.Toast;

public class TipsUtil
{
    
    public static void toast(Context context, String msg) {
        toast(context, msg, Toast.LENGTH_SHORT);
    }
    
    public static void toast(Context context, String msg, int time) {
        Toast sToast = Toast.makeText(context, msg, time);
        sToast.show();
    }
    /**
     * 获取某设备的品牌列表
     * public int STB = 1; //机顶盒
     * public int TV  = 2; //电视
     * public int BOX = 3; //网络盒子
     * public int DVD = 4; //DVD
     * public int AC  = 5; //空调
     * public int PRO = 6; //投影仪
     * public int PA  = 7; //功放
     * public int FAN = 8; //风扇
     * public int SLR = 9; //单反相机
     * public int Light = 10; //开关灯泡
     * public int AIR_CLEANER = 11;// 空气净化器
     * public int WATER_HEATER = 12;// 热水器
     */
    public static String RtypeNameByValue(int value){
        String name="";
        if (value==1){
            name="STB";
        }else if (value==2){
            name="TV";
        }else if (value==3){
            name="BOX";
        }else if (value==4){
            name="DVD";
        }else if (value==5){
            name="AC";
        }else if (value==6){
            name="PRO";
        }else if (value==7){
            name="PA";
        }else if (value==8){
            name="FAN";
        }else if (value==9){
            name="SLR";
        }else if (value==10){
            name="Light";
        }else if (value==11){
            name="AIR_CLEANER";
        }else if (value==12){
            name="WATER_HEATER";
        }
        return name;
    }
}

package com.hbdiye.lechuangsmart.Global;

import com.hbdiye.lechuangsmart.R;

import java.util.ArrayList;

public class ContentConfig {
    public static String[] hour_array={"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};
    public static String[] min_array={"00","01","02","03","04","05","06","07","08","09","10",
                                            "11","12","13","14","15","16","17","18","19","20",
                                            "21","22","23","24","25","26","27","28","29","30",
                                            "31","32","33","34","35","36","37","38","39","40",
                                            "41","42","43","44","45","46","47","48","49","50",
                                            "51","52","53","54","55","56","57","58","59","60"};
    public static int getHourPosition(String time){
        for (int i = 0; i < hour_array.length; i++) {
            if (time.equals(hour_array[i])){
                return i;
            }
        }
        return -1;
    }
    public static int getMandSPosition(String time){
        for (int i = 0; i < min_array.length; i++) {
            if (time.equals(min_array[i])){
                return i;
            }
        }
        return -1;
    }
    /**
     * 时间Hour
     * @return
     */
    public static ArrayList<Integer> getTimeHours(){
        ArrayList<Integer> datas=new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            datas.add(i);
        }
        return datas;
    }
    /**
     * 分
     */
    public static ArrayList<Integer> getTimeMin(){
        ArrayList<Integer> datas=new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            datas.add(i);
        }
        return datas;
    }
    /**
     * 秒
     */
    public static ArrayList<Integer> getTimeSeco(){
        ArrayList<Integer> datas=new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            datas.add(i);
        }
        return datas;
    }
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:"+unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }
    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
    public static int drawableByIcon(String iconname){
        if (iconname.equals("shebeia")){
            return R.mipmap.shebei1;
        }else if (iconname.equals("shebeib")){
            return R.mipmap.shebei2;
        }else if (iconname.equals("shebeic")){
            return R.mipmap.shebei3;
        }else if (iconname.equals("shebeid")){
            return R.mipmap.shebei4;
        }else if (iconname.equals("shebeie")){
            return R.mipmap.shebei5;
        }else if (iconname.equals("shebeif")){
            return R.mipmap.shebei6;
        }else if (iconname.equals("shebeig")){
            return R.mipmap.shebei7;
        }else if (iconname.equals("shebeih")){
            return R.mipmap.shebei8;
        }else if (iconname.equals("shebeii")){
            return R.mipmap.shebei9;
        }else if (iconname.equals("shebeij")){
            return R.mipmap.shebei10;
        }else if (iconname.equals("shebeik")){
            return R.mipmap.shebei11;
        }else if (iconname.equals("shebeil")){
            return R.mipmap.shebei12;
        }else {
            return R.mipmap.shebei0;
        }
    }
    public static int sceneIcon(String iconname){
        if (iconname.equals("changjing1")){
            return R.mipmap.changjing1;
        }else if (iconname.equals("changjing2")){
            return R.mipmap.huijia;
        }else if (iconname.equals("changjing3")){
            return R.mipmap.youxi;
        }else if (iconname.equals("changjing4")){
            return R.mipmap.ipod;
        }else if (iconname.equals("changjing5")){
            return R.mipmap.zixingche;
        }else if (iconname.equals("changjing6")){
            return R.mipmap.chuang;
        }else if (iconname.equals("changjing7")){
            return R.mipmap.jita;
        }else if (iconname.equals("changjing8")){
            return R.mipmap.dianhua;
        }else if (iconname.equals("changjing9")){
            return R.mipmap.dangao;
        }else if (iconname.equals("changjing10")){
            return R.mipmap.guangdie;
        }else if (iconname.equals("changjing11")){
            return R.mipmap.erji;
        }else if (iconname.equals("changjing12")){
            return R.mipmap.suo;
        }else if (iconname.equals("changjing13")){
            return R.mipmap.kaisuo;
        }else if (iconname.equals("changjing14")){
            return R.mipmap.mojing;
        }else {
            return R.mipmap.changjing1;
        }
    }
}

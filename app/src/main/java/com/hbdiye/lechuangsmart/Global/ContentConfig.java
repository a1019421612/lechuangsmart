package com.hbdiye.lechuangsmart.Global;

import java.util.ArrayList;

public class ContentConfig {
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
}

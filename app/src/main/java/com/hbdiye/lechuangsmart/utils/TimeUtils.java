package com.hbdiye.lechuangsmart.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by weiruibin on 2017/7/14.
 */

public class TimeUtils {
    /**
     * 获取当前日期 时间 星期
     * @return
     */
    public static String getNowTime(){
        long time= System.currentTimeMillis();
        Date date=new Date(time);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm EEEE");
        return format.format(date);
    }
    public static String getNowTimeNoWeek(){
        long time= System.currentTimeMillis();
        Date date=new Date(time);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
    public static String getTodayTime(){
        long time= System.currentTimeMillis();
        Date date=new Date(time);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    //获取年月日日期格式
    public static String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    //获取年月日期格式
    public static String getYEAR_MONTHTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }

    public static String getNowMonth(){
        String nowTIme= getNowTimeNoWeek();
        String month =nowTIme.substring(5,7);
        return month;
    }
    /**
     * 时间戳转为时间
     * @param str
     * @return
     */
    public static String sjcToTime(String str){
        if(str!=null) {
            Date date = new Date(Long.valueOf(str));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return format.format(date);
        }
        return "";
    }
    /**
     * 时间戳转为时间
     * @param str
     * @return
     */
    public static String sjcToTimehaveweek(String str){
        if(str!=null) {
            Date date = new Date(Long.valueOf(str));
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm EEEE");
            return format.format(date);
        }
        return "";
    }
    /**
     * 比较两个日期
     * @param s1
     * @param s2
     * @throws Exception
     */
    public static boolean DateCompare(String s1, String s2){
        try {
            //设定时间的模板
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //得到指定模范的时间
            Date d1 = sdf.parse(s1);

            Date d2 = sdf.parse(s2);
            long time = ((d1.getTime() - d2.getTime())/(24*3600*1000));
            Log.e("sss",time+"");
            //比较
            if( time>=7) {
                System.out.println("大于7天");
                return true;
            }else{
                System.out.println("小于7天");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 比较两个日期
     * @param s1
     * @param s2
     * @throws Exception
     */
    public static boolean DateComparestart(String s1, String s2){
        try {
            //设定时间的模板
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //得到指定模范的时间
            Date d1 = sdf.parse(s1);

            Date d2 = sdf.parse(s2);
            long time = ((d1.getTime() - d2.getTime())/(24*3600*1000));
            //比较
            if( d1.getTime()<=d2.getTime()) {
                System.out.println("大于三天");
                return true;
            }else{
                System.out.println("小于三天");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 比较两个日期是否大于7000s
     * @param s1 新时间
     * @param s2 旧时间
     * @throws Exception
     */
    public static boolean DateCompare7000s(String s1, String s2){
        try {
            //设定时间的模板
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //得到指定模范的时间
            Date d1 = sdf.parse(s1);

            Date d2 = sdf.parse(s2);
            long time = (d1.getTime() - d2.getTime())/1000;
            //比较
            if(time>7000) {
                System.out.println("大7000秒");
                return true;
            }else{
                System.out.println("小于三天");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 时间格式
     */
    public static final String DATE_FORMAT_NORMAL = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式（年月日）
     */
    public static final String DATE_FORMAT_YYMMDD = "yyyy-MM-dd";

    @SuppressLint("SimpleDateFormat")
//	 public static void main(String[] args) throws ParseException {
////	 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////	 Date date = format.parse("2015-08-19 12:35:35");
////	 System.out.println(getTime("2015-08-19 14:35:35","HH:mm"));
////		 getMonthTime("2016-03-20 15:53:28", DATE_FORMAT_NORMAL,"yyyy-MM");
//		 getShopListHeader("2014-12-20 15:53:28", DATE_FORMAT_NORMAL);
//	 }

    /**
     * 格式化时间到店面列表项标题需要的样式</br>
     * 本年本月显示“本月”；本来其他月份只显示月份；非本年日期全部显示年份+月份
     *
     * @param dateTime 待计算时间
     * @param format 时间格式
     * @return
     * @throws ParseException
     */
    public static String getShopListHeader(String dateTime, String format) throws ParseException {
        String headerText="";
        //判断是否是本年的时间
        if(isThisYear(dateTime, format)){
            //是今年的时间
            if(isThisMonth(dateTime, format)){
                //是今年本月的时间：显示“本月”
                headerText="本月";
            }else{
                //是今年的其他月份:只显示月份
                headerText = getTime(dateTime, format, "M月");
            }
        }else{
            //非今年的时间：显示年+月
            headerText = getTime(dateTime, format, "yyyy年M月");
        }
        System.out.println("headerText="+headerText);
        return headerText;
    }

    /**
     * 判断是否是本年的时间
     * @param dateTime
     * @param format
     * @return
     * @throws ParseException
     */
    @SuppressLint("SimpleDateFormat")
    @SuppressWarnings("deprecation")
    public static boolean isThisYear(String dateTime, String format) throws ParseException {
        Calendar cal = Calendar.getInstance();
        // 使用格林尼治时域，防止不同时间戳间的时差
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.setTimeInMillis(System.currentTimeMillis());
        int currentYear=cal.get(Calendar.YEAR);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = dateFormat.parse(dateTime);
        return date.getYear()==(currentYear-1900);
    }

    /**
     * 判断是否是本年本月的时间
     * @param dateTime
     * @param format
     * @return
     * @throws ParseException
     */
    @SuppressLint("SimpleDateFormat")
    @SuppressWarnings("deprecation")
    public static boolean isThisMonth(String dateTime, String format) throws ParseException {
        if(!isThisYear(dateTime, format)){
            //不是本年的时间，显然不是本年本月的时间
            return false;
        }
        Calendar cal = Calendar.getInstance();
        // 使用格林尼治时域，防止不同时间戳间的时差
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.setTimeInMillis(System.currentTimeMillis());
        int currentMonth=cal.get(Calendar.MONTH);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = dateFormat.parse(dateTime);
        return date.getMonth()==currentMonth;
    }

    /**
     * 获取精确到月份的时间
     *
     * @param dateTime
     *            待计算时间
     * @param formatFrom
     *            原时间格式
     * @param formatTo 目标时间格式
     * @return
     * @throws ParseException
     */
    @SuppressLint("SimpleDateFormat")
    public static final long getMonthTime(String dateTime, String formatFrom, String formatTo)
            throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatFrom);
        Date date = dateFormat.parse(dateTime);
        dateFormat = new SimpleDateFormat(formatTo);
        String afterDateTime = dateFormat.format(date);
        long time = dateFormat.parse(afterDateTime).getTime();
        return time;
    }

    /**
     * 格式化时间
     * @param dateTime
     * @param formatFrom
     * @param formatTo
     * @return
     * @throws ParseException
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTime(String dateTime, String formatFrom,
                                 String formatTo) throws ParseException {
        if (isBlank(dateTime) || isBlank(formatFrom)
                || isBlank(formatTo)) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatFrom);
        Date date = dateFormat.parse(dateTime);
        dateFormat = new SimpleDateFormat(formatTo);
        return dateFormat.format(date);
    }

    /**
     * 字符串空判断
     * <pre>is null or its length is 0 or it is made by space</pre>
     *
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return
     *         true, else return false.
     */
    public static boolean isBlank(String str)
    {
        return (str == null || str.trim().length() == 0);
    }

}

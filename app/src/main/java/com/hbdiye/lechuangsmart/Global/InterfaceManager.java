package com.hbdiye.lechuangsmart.Global;

import java.util.HashMap;

public class InterfaceManager {
    public static final String LOGIN = "LOGIN";//登录
    public static final String REGISTER="REGISTER";//注册
    public static final String GETVAILCODE="GETVAILCODE";//获取验证码
    public static final String FORGETPSW="FORGETPSW";//忘记密码
    public static final String HWREGISTER="HWREGISTER";//红外注册
    public static final String HWGETREMOTE="HWGETREMOTE";//获取红外列表
    public static final String ADDREMOTE="ADDREMOTE";//添加红外
    public static final String EDITREMOTE="EDITREMOTE";//编辑遥控中心
    public static final String DELREMOTE="DELREMOTE";//删除遥控中心
    public static final String GETDEVICELIST="GETDEVICELIST";//支持的红外设备列表
    public static final String GETBRANDLIST="GETBRANDLIST";//获取品牌列表
    public static final String GETMODELIST="GETMODELIST";//获取型号列表
    public static final String CHECKIRDEVICE="CHECKIRDEVICE";//匹配一个指定的红外设备
    public static final String ADDIRDEVICE="ADDIRDEVICE";//增加红外设备
    public static final String EDITIRDEVICE="EDITIRDEVICE";//编辑红外设备名称
    public static final String DELIRDEVICE="DELIRDEVICE";//删除红外设备
    public static final String SENDCODE="SENDCODE";//控制红外设备

    public static final String HOSTURL="";
    public static final String BASEURL="http://www.thingtill.com/Home/ThreeIr/";
    public static final String HWREGISTER_URL=BASEURL+"reg";

    public static final String APPID="123456";
    public static final String APPKEY="3576be75bca3495e";

    private static HashMap<String, String> urlManager = new HashMap<String, String>();
    private static InterfaceManager manager;
    public static InterfaceManager getInstance(){
        if (manager==null){
            manager=new InterfaceManager();
            urlManager.put(InterfaceManager.LOGIN,"http://39.104.119.0/SmartHome-java-user/user/login");
            urlManager.put(InterfaceManager.REGISTER,"http://39.104.119.0:80/SmartHome-java-user/user/register");
            urlManager.put(InterfaceManager.GETVAILCODE,"http://39.104.119.0:80/SmartHome-java-user/user/getValidateCode");
            urlManager.put(InterfaceManager.FORGETPSW,"http://39.104.119.0:80/SmartHome-java-user/user/resetPassword");
            urlManager.put(InterfaceManager.HWREGISTER,HWREGISTER_URL);
            urlManager.put(InterfaceManager.HWGETREMOTE,BASEURL+"get_remote");
            urlManager.put(InterfaceManager.ADDREMOTE,BASEURL+"add_remote");
            urlManager.put(InterfaceManager.EDITREMOTE,BASEURL+"edit_remote");
            urlManager.put(InterfaceManager.DELREMOTE,BASEURL+"del_remote");
            urlManager.put(InterfaceManager.GETDEVICELIST,BASEURL+"get_device_list");
            urlManager.put(InterfaceManager.GETBRANDLIST,BASEURL+"get_brand_list");
            urlManager.put(InterfaceManager.GETMODELIST,BASEURL+"get_mode_list");
            urlManager.put(InterfaceManager.CHECKIRDEVICE,BASEURL+"check_ir_device");
            urlManager.put(InterfaceManager.ADDIRDEVICE,BASEURL+"add_ir_device");
            urlManager.put(InterfaceManager.EDITIRDEVICE,BASEURL+"edit_ir_device");
            urlManager.put(InterfaceManager.DELIRDEVICE,BASEURL+"del_ir_device");
            urlManager.put(InterfaceManager.SENDCODE,BASEURL+"send_code");
        }
        return manager;
    }
    /**
     * 获取路径
     * @param name
     * @return
     */
    public String getURL(String name) {
        return urlManager.get(name);
    }
}

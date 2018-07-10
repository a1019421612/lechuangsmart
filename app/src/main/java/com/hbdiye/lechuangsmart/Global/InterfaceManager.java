package com.hbdiye.lechuangsmart.Global;

import java.util.HashMap;

public class InterfaceManager {
    public static final String LOGIN = "LOGIN";//登录
    public static final String REGISTER="REGISTER";//注册
    public static final String GETVAILCODE="GETVAILCODE";//获取验证码
    public static final String FORGETPSW="FORGETPSW";//忘记密码
    public static final String HOSTURL="";
    private static HashMap<String, String> urlManager = new HashMap<String, String>();
    private static InterfaceManager manager;
    public static InterfaceManager getInstance(){
        if (manager==null){
            manager=new InterfaceManager();
            urlManager.put(InterfaceManager.LOGIN,"http://39.104.119.0:8888/SmarthomeHTTPServer/Login.php");
            urlManager.put(InterfaceManager.REGISTER,"http://39.104.119.0:8088/SmartHome-java-user/user/register");
            urlManager.put(InterfaceManager.GETVAILCODE,"http://39.104.119.0:8088/SmartHome-java-user/user/getValidateCode");
            urlManager.put(InterfaceManager.FORGETPSW,"http://39.104.119.0:8088/SmartHome-java-user/user/resetPassword");
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

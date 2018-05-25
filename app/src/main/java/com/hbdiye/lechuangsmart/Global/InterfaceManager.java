package com.hbdiye.lechuangsmart.Global;

import java.util.HashMap;

public class InterfaceManager {
    public static final String LOGIN = "LOGIN";//登录
    public static final String GETVAILCODE="GETVAILCODE";//获取验证码
    private static HashMap<String, String> urlManager = new HashMap<String, String>();
    private static InterfaceManager manager;
    public static InterfaceManager getInstance(){
        if (manager==null){
            manager=new InterfaceManager();
            urlManager.put(InterfaceManager.LOGIN,"http://39.104.105.10:8888/SmarthomeHTTPServer/Login.php");
            urlManager.put(InterfaceManager.GETVAILCODE,"http://39.104.105.10:8888/SmarthomeHTTPServer/GetVailCode.php");
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

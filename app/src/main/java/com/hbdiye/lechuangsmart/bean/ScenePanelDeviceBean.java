package com.hbdiye.lechuangsmart.bean;

import java.io.Serializable;
import java.util.List;

public class ScenePanelDeviceBean implements Serializable {

    public String method  ;
    public String deviID  ;
    public String sceneID  ;
    public String pn  ;
    public String stCode  ;
    public String sdrID  ;public 
    List<Devices> devices;
    public class Devices implements Serializable  {

        public String parent  ;
        public String product  ;
        public String serialnumber  ;
        public String productID  ;
        public String onlineStatus  ;
        public int index  ;
        public String version  ;
        public String childs  ;
        public String mac  ;
        public String roomID  ;
        public String room  ;
        public String parentID  ;
        public String familyID  ;
        public String name  ;
        public String id  ;
        public String family  ;
        public String deviceAttributes  ;
    }
}
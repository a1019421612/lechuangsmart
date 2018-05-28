package com.hbdiye.lechuangsmart.bean;

import java.lang.reflect.Field;
import java.io.Serializable;
import java.util.List;

public class AnFangBean implements Serializable {

    public String classify;
    public String rooms;
    public String id;
    public String pn;
    public String room;
    public List<Devices> devices;

    public class Devices implements Serializable {

        public String parent;
        public String serialnumber;
        public String productID;
        public String onlineStatus;
        public int index;
        public String version;
        public String childs;
        public String mac;
        public String roomID;
        public String parentID;
        public String familyID;
        public String name;
        public String id;
        public String family;
    }
}
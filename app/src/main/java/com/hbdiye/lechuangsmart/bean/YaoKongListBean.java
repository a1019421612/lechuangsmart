package com.hbdiye.lechuangsmart.bean;

import java.lang.reflect.Field;
import java.io.Serializable;
import java.util.List;

public class YaoKongListBean implements Serializable {

    public String deviceID;
    public String pn;
    public List<Irremotes> irremotes;

    public class Irremotes implements Serializable {

        public String rtype;
        public String name;
        public String id;
        public String rid;
        public String deviceID;
        public Object device;
    }
}
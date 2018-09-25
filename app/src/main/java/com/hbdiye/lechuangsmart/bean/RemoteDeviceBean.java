package com.hbdiye.lechuangsmart.bean;

import java.lang.reflect.Field;
import java.io.Serializable;
import java.util.List;

public class RemoteDeviceBean implements Serializable {

    public Boolean success;
    public String info;
    public List<Data> data;

    public class Data implements Serializable {

        public String device_name;
        public int id;
    }
}
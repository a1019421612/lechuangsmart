package com.hbdiye.lechuangsmart.bean;

import java.lang.reflect.Field;
import java.io.Serializable;
import java.util.List;

public class ModeListBean implements Serializable {

    public Boolean success;
    public String info;
    public List<Data> data;

    public class Data implements Serializable {

        public String id;
        public String bn;
        public int key_squency;
    }
}
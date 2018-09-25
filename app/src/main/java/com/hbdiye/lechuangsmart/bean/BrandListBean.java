package com.hbdiye.lechuangsmart.bean;

import java.lang.reflect.Field;
import java.io.Serializable;
import java.util.List;

public class BrandListBean implements Serializable {

    public Boolean success;
    public String info;
    public List<Data> data;

    public class Data implements Serializable {

        public int id;
        public String bn;
    }
}
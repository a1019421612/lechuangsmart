package com.hbdiye.lechuangsmart.bean;

import java.io.Serializable;


import java.lang.reflect.Field;
import java.io.Serializable;
import java.util.List;
    public class IrDataBeans implements Serializable  {
        public List<IrDataList> irDataList;
        public class IrDataList implements Serializable  {

            public int fre  ;
            public String extJSON  ;
            public int rid  ;
            public int type  ;public List<Keys> keys;
            public class Keys implements Serializable  {

                public int fid  ;
                public String fname  ;
                public String scode  ;
                public String fkey  ;
                public int format  ;
                public String pulse  ;
                public String dcode  ;
                public  Exts exts;
                public class Exts implements Serializable  {
                }
            }
        }
}

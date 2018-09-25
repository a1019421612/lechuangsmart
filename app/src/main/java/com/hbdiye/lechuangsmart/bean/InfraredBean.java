package com.hbdiye.lechuangsmart.bean;

import java.io.Serializable;
import java.util.List;

public class InfraredBean implements Serializable {

    public Boolean success;
    public String info;
    public List<Remote_list> remote_list;

    public class Remote_list implements Serializable {

        public String name;
        public String id;
        public String uuid;
        public String version;
        public String mac;
        public List<Ir_list> ir_list;

        public class Ir_list implements Serializable {

            public String modelid;
            public String remote_id;
            public String name;
            public String id;
            public String type;
            public String version;
            public String key_squency;
        }
    }
}
package com.hbdiye.lechuangsmart.bean;

import java.lang.reflect.Field;
import java.io.Serializable;
import java.util.List;

public class DeviceTriggerBean implements Serializable {

    public int type;
    public String pn;
    public List<Devices> devices;

    public class Devices implements Serializable {

        public Object parent;
        public String serialnumber;
        public String productID;
        public Object onlineStatus;
        public int index;
        public String version;
        public Object childs;
        public String mac;
        public String roomID;
        public Object room;
        public String parentID;
        public String familyID;
        public String name;
        public String id;
        public Object family;
        public Product product;

        public class Product implements Serializable {

            public Object proacts;
            public Object proatts;
            public String name;
            public String icon;
            public String modelPath;
            public int index;
            public Object productTypes;
            public String id;
            public Object childs;
        }

        public List<DeviceAttributes> deviceAttributes;

        public class DeviceAttributes implements Serializable {

            public String proAttID;
            public String id;
            public String deviceID;
            public Object actions;
            public int value;
            public ProAtt proAtt;

            public class ProAtt implements Serializable {

                public String attributeID;
                public Object product;
                public String productID;
                public int port;
                public String name;
                public int index;
                public String id;
                public Attribute attribute;

                public class Attribute implements Serializable {

                    public int clusterNo;
                    public String name;
                    public int index;
                    public int attributeNo;
                    public String id;
                }
            }
        }
    }
}
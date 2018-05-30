package com.hbdiye.lechuangsmart.bean;

import java.lang.reflect.Field;
import java.io.Serializable;
import java.util.List;

public class LinkageSettingBean implements Serializable {

    public String linkageID;
    public String pn;
    public List<Lts> lts;

    public class Lts implements Serializable {

        public String linkageID;
        public int delaytime;
        public int index;
        public Object rcode;
        public Object linkage;
        public int type;
        public String deviceID;
        public String param;
        public String proActID;
        public String id;
        public Object fpulse;
        public List<ProActs> proActs;

        public class ProActs implements Serializable {

            public Object product;
            public String productID;
            public int port;
            public String name;
            public String actionID;
            public int index;
            public String id;
            public Action action;

            public class Action implements Serializable {

                public int clusterNo;
                public int commandNo;
                public String name;
                public int index;
                public String id;
                public int directionNo;

            }

        }

        public Device device;

        public class Device implements Serializable {

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
            public Object deviceAttributes;
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

        }

        public ProAct proAct;

        public class ProAct implements Serializable {

            public Object product;
            public String productID;
            public int port;
            public String name;
            public String actionID;
            public int index;
            public String id;
            public Action action;

            public class Action implements Serializable {

                public int clusterNo;
                public int commandNo;
                public String name;
                public int index;
                public String id;
                public int directionNo;

            }

        }

    }

    public Linkage linkage;

    public class Linkage implements Serializable {

        public int active;
        public Object linkageTasks;
        public Object proAtt;
        public Object type;
        public Object deviceID;
        public String familyID;
        public Object proAttID;
        public String timingID;
        public String name;
        public String id;
        public Object family;
        public Object device;
        public Object value;
        public TimingRecord timingRecord;

        public class TimingRecord implements Serializable {

            public String cronExpression;
            public String familyID;
            public Object timingInfo;
            public Object delayedSecond;
            public String timing;
            public int active;
            public String id;
            public int type;

        }

    }

}
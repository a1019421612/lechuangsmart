package com.hbdiye.lechuangsmart.bean;

import java.io.Serializable;
import java.util.List;

public class LinkageBean implements Serializable {

    public String pn;
    public List<Linkages> linkages;

    public class Linkages implements Serializable {

        public int active;
//        public String timingRecord;
//        public String linkageTasks;
        public int type;
        public String deviceID;
        public String familyID;
        public String proAttID;
        public String timingID;
        public String name;
        public String id;
        public String family;
        public int value;
        public ProAtt proAtt;

        public class ProAtt implements Serializable {

            public String attributeID;
            public String product;
            public String productID;
            public int port;
            public String name;
            public int index;
            public String id;
            public String attribute;

        }

        public Device device;

        public class Device implements Serializable {

            public String parent;
            public String product;
            public String serialnumber;
            public String productID;
            public String onlineStatus;
            public int index;
            public String version;
            public String childs;
            public String mac;
            public String roomID;
            public String room;
            public String parentID;
            public String familyID;
            public String name;
            public String id;
            public String family;
            public String deviceAttributes;
        }
    }
}

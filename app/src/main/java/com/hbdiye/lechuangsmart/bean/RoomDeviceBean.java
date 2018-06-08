package com.hbdiye.lechuangsmart.bean;

import java.lang.reflect.Field;
import java.io.Serializable;
import java.util.List;

public class RoomDeviceBean implements Serializable {

    public String classify;
    public Object rooms;
    public String id;
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
        public Object parentID;
        public String familyID;
        public String name;
        public String id;
        public Object family;
        public Product product;

        public class Product implements Serializable {

            public String name;
            public String icon;
            public String modelPath;
            public int index;
            public String id;
            public Object childs;
            public List<Proacts> proacts;

            public class Proacts {
            }

            public List<Proatts> proatts;

            public class Proatts {

            }

            public List<ProductTypes> productTypes;

            public class ProductTypes implements Serializable {

                public String name;
                public String id;
            }
        }

        public Room room;

        public class Room implements Serializable {

            public String familyID;
            public Object devices;
            public String name;
            public int index;
            public String id;
            public Object family;

        }

        public List<DeviceAttributes> deviceAttributes;

        public class DeviceAttributes {
            public String proAttID;
            public String id;
            public Object proAtt;
            public String deviceID;
            public int value;
            public List<Actions> actions;

            public class Actions {
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
    }

    public Room room;

    public class Room implements Serializable {

        public String familyID;
        public String name;
        public int index;
        public String id;
        public List<Devices> devices;

        public class Devices implements Serializable {

            public Object parent;
            public Object product;
            public String serialnumber;
            public String productID;
            public Object onlineStatus;
            public int index;
            public String version;
            public Object childs;
            public String mac;
            public String roomID;
            public Object room;
            public Object parentID;
            public String familyID;
            public String name;
            public String id;
            public Object family;
            public Object deviceAttributes;

        }

        public Family family;

        public class Family implements Serializable {

            public Long createDatetime;
            public String name;
            public String id;

        }

    }
}

package com.hbdiye.lechuangsmart.bean;

import java.lang.reflect.Field;
import java.io.Serializable;
import java.util.List;

public class SceneDeviceBean implements Serializable {

    public String sceneID;
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
        public Object deviceAttributes;
        public Product product;

        public class Product implements Serializable {

            public Object proatts;
            public String name;
            public String icon;
            public String modelPath;
            public int index;
            public Object productTypes;
            public String id;
            public Object childs;
            public List<Proacts> proacts;

            public class Proacts implements Serializable {

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

    public List<SceneTasks> sceneTasks;

    public class SceneTasks implements Serializable {

        public int delaytime;
        public int index;
        public Object rcode;
        public int type;
        public String deviceID;
        public Object scene;
        public String param;
        public String proActID;
        public String sceneID;
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
            public Parent parent;

            public class Parent implements Serializable {

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

    public Scene scene;

    public class Scene implements Serializable {

        public String familyID;
        public String name;
        public String icon;
        public int index;
        public String id;
        public Object family;
        public Object sceneTasks;
    }
}
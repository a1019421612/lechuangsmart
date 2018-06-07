package com.hbdiye.lechuangsmart.bean;

import java.lang.reflect.Field;
import java.io.Serializable;
import java.util.List;

public class RoomBean implements Serializable {

    public String pn;
    public List<Rooms> rooms;

    public class Rooms implements Serializable {

        public String familyID;
        public Object devices;
        public String name;
        public int index;
        public String id;
        public Object family;

    }

}
package com.hbdiye.lechuangsmart.bean;

import java.io.Serializable;
import java.util.List;
public class SceneBean implements Serializable  {

    public String pn  ;public List<Scenes> scenes;
    public class Scenes implements Serializable {

        public String familyID  ;
        public String name  ;
        public String icon  ;
        public int index  ;
        public String id  ;
        public String family  ;
        public String sceneTasks  ;
    }

}

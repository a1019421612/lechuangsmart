package com.hbdiye.lechuangsmart.bean;

import java.io.Serializable;
import java.lang.reflect.Field;
        import java.io.Serializable;
        import java.util.List;
public class FamilyNameBean implements Serializable  {

    public String pn  ;
    public List<FamilyUsers> familyUsers;
    public class FamilyUsers implements Serializable  {

        public Long createDatetime  ;
        public String familyID  ;
        public String password  ;
        public String mobilephone  ;
        public String name  ;
        public String id  ;
        public String family  ;
    }

    public  User user;
    public class User implements Serializable  {

        public Long createDatetime  ;
        public String familyID  ;
        public String password  ;
        public String mobilephone  ;
        public String name  ;
        public String id  ;
        public  Family family;
        public class Family implements Serializable {

            public Long createDatetime  ;
            public String name  ;
            public String id  ;

        }

    }

}

package com.hbdiye.lechuangsmart.bean;

import java.lang.reflect.Field;
import java.io.Serializable;
import java.util.List;

public class TiZhiBean implements Serializable  {

    public String phone  ;
    public int pageSize  ;
    public int page  ;
    public String type  ;
    public String pn  ;
    public String ecode  ;
    public  Data data;
    public class Data implements Serializable  {

        public int amount  ;public
        List<Lists> list;
        public class Lists implements Serializable  {

            public String datetime  ;
            public String phone  ;
            public String id  ;
            public int type  ;
            public  JsonStr jsonStr;
            public class JsonStr implements Serializable  {

                public String usernumber  ;
                public String phone  ;
                public String dtype  ;
                public int type  ;
                public String did  ;
                public  Datas data;
                public class Datas implements Serializable  {

                    public String physicalID  ;
                    public String impedance  ;
                    public String bone  ;
                    public Double moisture  ;
                    public String result  ;
                    public String medicId  ;
                    public Double adiposerate  ;
                    public Double muscle  ;
                    public String thermal  ;
                    public String metabolism  ;
                    public String time  ;
                    public String basalMetabolism  ;
                    public int visceralfat  ;
                    public String bmi  ;
                    public String memberId  ;
                    public  Suggestion suggestion;
                    public class Suggestion implements Serializable  {

                        public String doctor  ;
                        public String common  ;
                        public String sport  ;
                        public String foot  ;

                    }
                }
            }
        }
    }
}
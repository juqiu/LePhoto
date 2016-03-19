package com.little.happypic.business.pojo;

import java.util.ArrayList;

/**
 * Created by little on 16/3/8.
 */

/**
 * Pic class Tags & ids
 */
public class PicClassTags {

    public boolean status;

    public ArrayList<PicClass>  tngou;

    /**
     *
     */
    public static class PicClass{
        public int id;
        public String name;
        public String title;
        public String keywords;
        public String description;
        public int seq;//排序 从0。。。。10开始
    }
}

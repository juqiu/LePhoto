package com.little.happypic.business.pojo;

import java.util.ArrayList;

/**
 * Created by little on 16/3/12.
 */
public class GalleryList {

    public boolean status;
    public int total;

    public ArrayList<Gallery> tngou;

    public static class  Gallery {
        public int id;
        public int  galleryclass ;   //          图片分类
        public String title     ;    //          标题
        public String img     ;      //          图库封面
        public int count     ;       //          访问数
        public int rcount     ;      //          回复数
        public int fcount     ;      //          收藏数
        public int size     ;        //          图片多少张
    }
}

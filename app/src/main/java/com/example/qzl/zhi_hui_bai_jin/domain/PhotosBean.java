package com.example.qzl.zhi_hui_bai_jin.domain;

import java.util.ArrayList;

/**
 * 组图对象
 * Created by Qzl on 2016-08-04.
 */
public class PhotosBean {
    public PhotosData data;
    public class PhotosData{
        public ArrayList<PhotoNews> news;
    }

    public class PhotoNews{
        public int id;
        public String listimage;
        public String title;
    }
}

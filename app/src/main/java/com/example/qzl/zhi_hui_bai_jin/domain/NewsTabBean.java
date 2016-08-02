package com.example.qzl.zhi_hui_bai_jin.domain;

import java.util.ArrayList;

/**
 * 页签详情数据对象
 * Created by Qzl on 2016-08-02.
 */
public class NewsTabBean {
    public NewsTab data;
    public class NewsTab{
        public String more;
        public ArrayList<NewsData> news;
        public ArrayList <TopNews> topnews;
    }
    //新闻列表对象
    public class NewsData{
        public int id;
        public String listimage;//新闻所对应的图片
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }

    //头条新闻对象
    public class TopNews{
        public int id;
        public String topimage;//新闻所对应的图片
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }
}

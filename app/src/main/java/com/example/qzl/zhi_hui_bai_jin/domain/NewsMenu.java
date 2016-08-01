package com.example.qzl.zhi_hui_bai_jin.domain;

import java.util.ArrayList;

/**
 * 分类信息的对象分装
 * 使用Gson解析时，Gson使用技巧
 * 1 逢{}创建对象，逢[]创建集合（Arraylist）
 * 2 所有字段名称要和Gson返回字段高度一致
 * Created by Qzl on 2016-08-01.
 */
public class NewsMenu {
    public int retcode;
    public ArrayList<Integer> extend;

    public ArrayList <NewsMenuData> data;
    //侧边栏菜单对象
    public class NewsMenuData {
        public int id;
        public String title;
        public int type;

        public ArrayList <NewsTabData> children;

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "title='" + title + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    //页签对象
    public class NewsTabData {
        public int id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NewsMenu{" +
                "data=" + data +
                '}';
    }
}

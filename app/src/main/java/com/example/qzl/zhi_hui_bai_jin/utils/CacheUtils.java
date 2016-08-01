package com.example.qzl.zhi_hui_bai_jin.utils;

import android.content.Context;

/**
 * 网络缓存的工具类
 * Created by Qzl on 2016-08-01.
 */
public class CacheUtils {
    /**
     * 以url为key，以Json为value保存在本地
     * @param url
     * @param json
     */
    public static void setCache(String url, String json, Context context){
        //也可以用文件缓存：以MD5（url）为文件名，以Json为文件内容
        PrefUtils.setString(context,url,json);
    }

    /**
     * 获取缓存
     * @param url
     * @param context
     * @return
     */
    public static String getCache(String url, Context context){
        //文件缓存：查找有没有一个叫MD5（url）的文件，有的话，说明有缓存
        return PrefUtils.getString(context,url,null);
    }
}

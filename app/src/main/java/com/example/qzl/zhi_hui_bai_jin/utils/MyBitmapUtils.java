package com.example.qzl.zhi_hui_bai_jin.utils;

import android.widget.ImageView;

/**
 * 自定义三级缓存图片加载工具
 * Created by Qzl on 2016-08-04.
 */
public class MyBitmapUtils {

    private NetCacheUtils mNetCacheUtils;//网络缓存工具类
    private MyBitmapUtils(){
        mNetCacheUtils = new NetCacheUtils();
    }
    public void display(ImageView imageView, String url) {
        // 优先从内存中加载图片
        mNetCacheUtils.getBitmapFormNet(imageView,url);
    }
}

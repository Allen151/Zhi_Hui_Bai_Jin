package com.example.qzl.zhi_hui_bai_jin.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.qzl.zhi_hui_bai_jin.R;

/**
 * 自定义三级缓存图片加载工具
 * Created by Qzl on 2016-08-04.
 */
public class MyBitmapUtils {

    private NetCacheUtils mNetCacheUtils;//网络缓存工具类
    private LocalCacheUtils mLocalCacheUtils;//本地缓存工具类
    private MemoryCacheUtils mMemoryCacheUtils;//内存缓存工具类
    public MyBitmapUtils(Activity activity){
        mLocalCacheUtils = new LocalCacheUtils();//创建本地缓存对象
        mNetCacheUtils = new NetCacheUtils(activity,mLocalCacheUtils,mMemoryCacheUtils);
        mMemoryCacheUtils = new MemoryCacheUtils();
    }
    public void display(ImageView imageView, String url) {
        //设置默认图片
        imageView.setImageResource(R.drawable.pic_item_list_default);
        // 优先从内存中加载图片,速度最快，不浪费流量
        Bitmap bitmap = mMemoryCacheUtils.getMemoryCache(url);
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
            System.out.println("从内存加载");
            return;
        }
        //其次从本地（sdcard）加载图片，速度快，不浪费流量
        bitmap = mLocalCacheUtils.getLocalCache(url);
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
            System.out.println("从本地加载了");
            //写内存缓存
            mMemoryCacheUtils.setMemoryCache(url,bitmap);
            return;
        }
        //最后从网络加载
        mNetCacheUtils.getBitmapFormNet(imageView,url);
    }
}

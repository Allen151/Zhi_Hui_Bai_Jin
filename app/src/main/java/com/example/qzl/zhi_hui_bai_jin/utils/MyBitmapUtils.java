package com.example.qzl.zhi_hui_bai_jin.utils;

import android.app.Activity;
import android.widget.ImageView;

import com.example.qzl.zhi_hui_bai_jin.R;

/**
 * 自定义三级缓存图片加载工具
 * Created by Qzl on 2016-08-04.
 */
public class MyBitmapUtils {

    private NetCacheUtils mNetCacheUtils;//网络缓存工具类
    public MyBitmapUtils(Activity activity){
        mNetCacheUtils = new NetCacheUtils(activity);
    }
    public void display(ImageView imageView, String url) {
        //设置默认图片
        imageView.setImageResource(R.drawable.pic_item_list_default);
        // 优先从内存中加载图片
        mNetCacheUtils.getBitmapFormNet(imageView,url);
    }
}

package com.example.qzl.zhi_hui_bai_jin.utils;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * 内存缓存
 * Created by Qzl on 2016-08-04.
 */
public class MemoryCacheUtils {
    //private HashMap<String,Bitmap> mMemoryCache = new HashMap<>();
    //SoftReference<Bitmap> :软引用
    private HashMap<String,SoftReference<Bitmap>> mMemoryCache = new HashMap<>();
    /**
     * 写内存缓存
     */
    public void setMemoryCache(String url,Bitmap bitmap){
//        mMemoryCache.put(url,bitmap);
        SoftReference<Bitmap> soft = new SoftReference<Bitmap>(bitmap);//使用软引用将bitmap包装起来
        mMemoryCache.put(url,soft);
    }

    /**
     * 读内存缓存
     */
    public Bitmap getMemoryCache(String url){
        //return mMemoryCache.get(url);
        SoftReference<Bitmap> softReference = mMemoryCache.get(url);
        if (softReference != null){
            Bitmap bitmap = softReference.get();
            return bitmap;
        }
        return null;
    }
}

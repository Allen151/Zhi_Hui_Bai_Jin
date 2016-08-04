package com.example.qzl.zhi_hui_bai_jin.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 本地缓存
 * Created by Qzl on 2016-08-04.
 */
public class LocalCacheUtils {
    private static final String LOCAL_CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/zhbj_cache";
    /**
     * 写本地缓存
     */
    public void setLocalCache(String url, Bitmap bitmap){
        File dir = new File(LOCAL_CACHE_PATH);
        if (!dir.exists() || !dir.isDirectory() ){
            dir.mkdirs();//创建文件夹
        }
        try {
            String fileName = MD5Encoder.encode(url);
            File cacheFile = new File(dir,fileName);
            //将当前图片压缩到本地 参一 ：图片格式，参二：压缩比例0-100，参三：输出流
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(cacheFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读本地缓存
     */
    public Bitmap getLocalCache(String url){
        try {
            File cacheFile = new File(LOCAL_CACHE_PATH,MD5Encoder.encode(url));
            if (cacheFile.exists()){
                //如果文件存在
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(cacheFile));
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

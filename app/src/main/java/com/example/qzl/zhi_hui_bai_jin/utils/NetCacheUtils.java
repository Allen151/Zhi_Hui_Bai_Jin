package com.example.qzl.zhi_hui_bai_jin.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * 网络缓存
 * Created by Qzl on 2016-08-04.
 */
public class NetCacheUtils {
    private String TAG = "NetCacheUtils";
    public void getBitmapFormNet(ImageView imageView, String url) {
        //AsyncTask 异步封装的工具，可以实现异步请求及主界面更新（线程池+handler）
        new BitmapTask().execute(imageView,url);//启动AsyncTask
    }

    /**
     * 三个泛型意义：
     * 第一个泛型:代表的是doInBackground里的参数类型
     * 第二个泛型：代表的是onProgressUpdate里的参数类型
     * 第三个泛型：代表的是doInBackground的返回类型及onPostExecute里的参数类型
     */
    class BitmapTask extends AsyncTask<Object,Integer,Bitmap>{
        // 1 预加载，运行在主线程
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: 预加载");
        }
        //2 在后台运行，正在加载，运行在子线程（核心方法），可以直接异步请求
        @Override
        protected Bitmap doInBackground(Object... params) {
            Log.d(TAG, "doInBackground: 正在加载");
            ImageView imageView = (ImageView) params[0];
            String url = (String) params[1];
            //开始下载图片

            return null;
        }

        // 3 进度更新，运行在主线程
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "onProgressUpdate: 进度更新");
        }

        // 4 加载结束之后，运行在主线程（核心方法），可以直接更新ui
        @Override
        protected void onPostExecute(Bitmap aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPostExecute: 加载结束");
        }
    }
}

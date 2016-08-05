package com.example.qzl.zhi_hui_bai_jin.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络缓存
 * Created by Qzl on 2016-08-04.
 */
public class NetCacheUtils {
    private String TAG = "NetCacheUtils";
    private ImageView mImageView;
    private String mUrl;

    private Activity activity;
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;
    public NetCacheUtils(Activity activity, LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        this.activity = activity;
        mLocalCacheUtils = localCacheUtils;
        mMemoryCacheUtils = memoryCacheUtils;
    }

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
            mImageView = (ImageView) params[0];
            mUrl = (String) params[1];
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mImageView.setTag(mUrl);//打标记，将当前imageView和URL绑定到了一起
                }
            });
            //开始下载图片
            Bitmap bitmap = download(mUrl);

            //publishProgress(valus);//调运此方法实现进度更新（会回调onProgressUpdate）
            return bitmap;
        }

        // 3 进度更新，运行在主线程
        @Override
        protected void onProgressUpdate(Integer... values) {
            //更新进度条
            super.onProgressUpdate(values);
            Log.d(TAG, "onProgressUpdate: 进度更新");
        }

        // 4 加载结束之后，运行在主线程（核心方法），可以直接更新ui
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute: 加载结束");
            if (result != null){
                //给imageView设置图片
                //由于listView的重用机制导致imageView对象可能被多个item共用，从而可能将错误的图片设置给了imageView对象
                //所以，需要在此处校验，判断是否是正确的图片
                String url = (String) mImageView.getTag();
                if (mUrl.equals(url)) {//判断图片绑定的url是否是当前bitmap的url，如果是，说明图片正确
                    mImageView.setImageBitmap(result);
                    //写本地缓存
                    mLocalCacheUtils.setLocalCache(mUrl,result);
                    //写内存缓存
                    mMemoryCacheUtils.setMemoryCache(mUrl,result);
                }
            }
        }
    }

    //下载图片
    private Bitmap download(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);//链接超时
            conn.setReadTimeout(5000);//读取超时时间
            conn.connect();
            int requestCode = conn.getResponseCode();
            if (requestCode == 200){
                InputStream inputStream = conn.getInputStream();
                //根据输入流生成bitmap对象
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (conn != null){
                conn.disconnect();
            }
        }
        return null;
    }
}

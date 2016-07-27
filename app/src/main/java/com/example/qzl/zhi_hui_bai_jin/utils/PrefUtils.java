package com.example.qzl.zhi_hui_bai_jin.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *对SharedPreferences进行封装
 * Created by Qzl on 2016-07-27.
 */
public class PrefUtils {
    private static SharedPreferences msp;

    /**
     * 设置保存的值
     * @param context
     * @param key
     * @param value ： 设置值
     * @return
     */
    public static void setBoolean(Context context,String key,boolean value){
        msp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        msp.edit().putBoolean(key,value).commit();
    }
    public static void setString(Context context,String key,String string){
        msp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        msp.edit().putString(key,string).commit();
    }
    public static void setInt(Context context,String key,int i){
        msp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        msp.edit().putInt(key,i).commit();
    }
    /**
     * 获取保存的值
     * @param context
     * @param key
     * @param defValue ： 默认值
     * @return
     */
    public static boolean getBoolean(Context context,String key,boolean defValue){
        msp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return msp.getBoolean(key,defValue);
    }
    public static String getString(Context context,String key,String difString){
        msp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return msp.getString(key,difString);
    }
    public static int getInt(Context context,String key,int difi){
        msp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return msp.getInt(key,difi);
    }
}

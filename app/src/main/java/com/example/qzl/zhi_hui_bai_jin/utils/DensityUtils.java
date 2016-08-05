package com.example.qzl.zhi_hui_bai_jin.utils;

import android.content.Context;

/**
 * 密度换算工具类
 * Created by Qzl on 2016-08-05.
 */
public class DensityUtils {

    //将dp转成px
    public static int dip2px(float dip, Context ctx){
        float density = ctx.getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);//四舍五入
        return px;
    }
    //将px转成dp
    public static float px2dp(int px,Context ctx){
        float density = ctx.getResources().getDisplayMetrics().density;
        float dp = px / density;
        return dp;
    }
}

package com.example.qzl.zhi_hui_bai_jin.base;

import android.app.Activity;
import android.view.View;

/**
 * 菜单详情页的基类
 * Created by Qzl on 2016-08-01.
 */
public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    public View mRootView;//菜单详情也根布局

    public BaseMenuDetailPager(Activity activity) {
        mActivity = activity;
        mRootView = initView();
    }

    //初始化布局，必须子类实现
    public abstract View initView();

    //初始化数据
    public void initData(){

    }

}

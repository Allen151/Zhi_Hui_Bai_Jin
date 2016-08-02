package com.example.qzl.zhi_hui_bai_jin.implayment.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.qzl.zhi_hui_bai_jin.base.BaseMenuDetailPager;
import com.example.qzl.zhi_hui_bai_jin.domain.NewsMenu;

/**
 * Created by Qzl on 2016-08-02.
 */
public class TabDetailPager extends BaseMenuDetailPager{
    private NewsMenu.NewsTabData mTabData;//单个页签的网络数据
    private TextView mView;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
    }

    @Override
    public View initView() {
        mView = new TextView(mActivity);
        //view.setText(mTabData.title);//此处空指针
        mView.setTextColor(Color.RED);
        mView.setTextSize(22);
        mView.setGravity(Gravity.CENTER);
        return mView;
    }

    @Override
    public void initData() {
        mView.setText(mTabData.title);
    }
}

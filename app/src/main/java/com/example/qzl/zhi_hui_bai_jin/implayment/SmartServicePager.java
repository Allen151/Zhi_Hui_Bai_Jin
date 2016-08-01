package com.example.qzl.zhi_hui_bai_jin.implayment;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.qzl.zhi_hui_bai_jin.base.BasePager;

/**
 * 智慧服务页面
 * Created by Qzl on 2016-07-28.
 */
public class SmartServicePager extends BasePager {
    public SmartServicePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        Log.d("tag","智慧服务初始化了...");
        //要给帧布局填充布局对象
        TextView view = new TextView(mActivity);
        view.setText("智慧服务");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        mFl_base_pager_content.addView(view);
        //修改页面标题
        mTv_base_pager_title.setText("生活");
        //显示菜单按钮
        mBtn_base_pager_menu.setVisibility(View.VISIBLE);
    }
}

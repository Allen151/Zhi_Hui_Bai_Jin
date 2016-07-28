package com.example.qzl.zhi_hui_bai_jin.implayment;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.example.qzl.zhi_hui_bai_jin.base.BasePager;

/**
 * 设置页面
 * Created by Qzl on 2016-07-28.
 */
public class SettingPager extends BasePager {
    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        Log.d("tag","设置页初始化了...");
        //要给帧布局填充布局对象
        TextView view = new TextView(mActivity);
        view.setText("设置");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        mFl_base_pager_content.addView(view);
        //修改页面标题
        mTv_base_pager_title.setText("设置");
    }
}

package com.example.qzl.zhi_hui_bai_jin.implayment;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.example.qzl.zhi_hui_bai_jin.base.BasePager;

/**
 * 首页
 * Created by Qzl on 2016-07-28.
 */
public class HomePager extends BasePager {
    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        Log.d("tag","首页初始化了...");
        //要给帧布局填充布局对象
        TextView view = new TextView(mActivity);
        view.setText("首页");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        mFl_base_pager_content.addView(view);
    }
}

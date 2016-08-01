package com.example.qzl.zhi_hui_bai_jin.implayment;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.qzl.zhi_hui_bai_jin.base.BasePager;

/**
 * 政务页面
 * Created by Qzl on 2016-07-28.
 */
public class GovAffairsPager extends BasePager {
    public GovAffairsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        Log.d("tag","政务页初始化了...");
        //要给帧布局填充布局对象
        TextView view = new TextView(mActivity);
        view.setText("政务");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        mFl_base_pager_content.addView(view);
        //修改页面标题
        mTv_base_pager_title.setText("人口管理");
        //显示菜单按钮
        mBtn_base_pager_menu.setVisibility(View.VISIBLE);
    }
}

package com.example.qzl.zhi_hui_bai_jin.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.qzl.zhi_hui_bai_jin.MainActivity;
import com.example.qzl.zhi_hui_bai_jin.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 五个标签页的基类
 * Created by Qzl on 2016-07-28.
 */
public class BasePager {
    public View mRootView;//当前页面的布局对象
    public Activity mActivity;
    public TextView mTv_base_pager_title;
    public ImageButton mBtn_base_pager_menu;
    public FrameLayout mFl_base_pager_content;//空的帧布局布局对象，要动态添加布局

    public BasePager(Activity activity){
        this.mActivity = activity;
        mRootView = initView();
    }
    /**
     * 初始化布局
     * @return
     */
    public View initView(){
        View view = View.inflate(mActivity, R.layout.base_pager,null);
        mTv_base_pager_title = (TextView) view.findViewById(R.id.tv_base_pager_title);
        mBtn_base_pager_menu = (ImageButton) view.findViewById(R.id.btn_base_pager_menu);
        mFl_base_pager_content = (FrameLayout) view.findViewById(R.id.fl_base_pager_content);
        mBtn_base_pager_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        return view;
    }

    /**
     * 打开或者关闭侧边栏
     */
    private void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();//如果当前状态是开，调运后就是关，反之亦然
    }
    /**
     * 初始化数据
     */
    public void initData(){

    }
}

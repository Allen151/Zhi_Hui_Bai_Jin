package com.example.qzl.zhi_hui_bai_jin;

import android.os.Bundle;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 主页面
 */
public class MainActivity extends SlidingFragmentActivity {

    private SlidingMenu mSlidingMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//取标题
        setContentView(R.layout.activity_main);
        //设置左侧边栏
        setBehindContentView(R.layout.menu_left);
        //获取SlideMenu
        mSlidingMenu = getSlidingMenu();
        //设置触摸方式为全屏
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mSlidingMenu.setBehindOffset(400);//屏幕预留200像素宽度
    }
}

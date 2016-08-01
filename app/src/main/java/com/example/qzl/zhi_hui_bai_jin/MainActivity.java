package com.example.qzl.zhi_hui_bai_jin;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.example.qzl.zhi_hui_bai_jin.fragment.ContentFragment;
import com.example.qzl.zhi_hui_bai_jin.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 主页面
 */
public class MainActivity extends SlidingFragmentActivity {

    private static final String TAG_LEFT_FRAGMENT_MENU = "TAG_LEFT_FRAGMENT_MENU";
    private static final String TAG_FRAGMENT_CONTENT = "TAG_FRAGMENT_CONTENT";
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

        initFragment();
    }

    /**
     * 初始化fragment
     */
    private void initFragment(){
        //1 拿到fragment管理器
        FragmentManager fm = getSupportFragmentManager();
        // 2 开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        //3 用fragment替换帧布局  参数一：要替换的帧布局id，参数二：要替换的fragment，参数三：标记
        transaction.replace(R.id.fl_main,new ContentFragment(),TAG_FRAGMENT_CONTENT);
        transaction.replace(R.id.fl_mian_left_menu,new LeftMenuFragment(),TAG_LEFT_FRAGMENT_MENU);
        // 4 提交事务
        transaction.commit();

//        Fragment fragment = fm.findFragmentByTag(TAG_LEFT_FRAGMENT_MENU);//根据标记找到对应的fragment
    }

    //获取侧边栏fragment对象
    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_FRAGMENT_MENU);
        return fragment;
    }
}

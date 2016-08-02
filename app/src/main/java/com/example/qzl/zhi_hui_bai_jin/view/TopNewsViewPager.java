package com.example.qzl.zhi_hui_bai_jin.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Qzl on 2016-08-02.
 */
public class TopNewsViewPager extends ViewPager {

    private int mStartX,mStartY;

    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 1 上下滑动，需要拦截
     * 2 向右滑动并且当前是第一个页面，需要拦截
     * 3 向左滑动并且当前是最后一个页面，需要拦截
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //请求所有父控件以及祖宗控件都不要拦截
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //记录x和y的坐标
                mStartX = (int) ev.getX();
                mStartY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();
                int dx = endX - mStartX;
                int dy = endY - mStartY;
                if (Math.abs(dy) < Math.abs(dx)){
                    //当前的页面
                    int currentItem = getCurrentItem();
                    //左右滑动
                    if (dx > 0){
                        //向右滑
                        if (currentItem == 0){
                            //第一个页面：需要拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }else {
                        //向左滑
                        int count = getAdapter().getCount();//item的总数
                        if (currentItem == count - 1){
                            //最后一个页面：需要拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }else {
                    //上下滑动,需要拦截
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}

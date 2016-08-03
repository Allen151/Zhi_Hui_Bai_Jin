package com.example.qzl.zhi_hui_bai_jin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.example.qzl.zhi_hui_bai_jin.R;

/**
 * 下拉刷新的listView
 * Created by Qzl on 2016-08-03.
 */
public class PullToRefreshListView extends ListView {

    private View mHeaderView;
    private int mHeaderViewHeight;
    private int mStartY = -1;

    public PullToRefreshListView(Context context) {
        super(context);
        initHeaderView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh, null);
        this.addHeaderView(mHeaderView);
        //隐藏头布局
        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartY = (int) ev.getY();//起点的y坐标
                break;
            case MotionEvent.ACTION_MOVE:
                if (mStartY == -1) {
                    //当用户按住头条新闻的viewPager进行下拉时，
                    // actionDown事件会被viewPager消费掉，导致stareY没有复制，此处需要重新获取一下
                    mStartY = (int) ev.getY();
                }
                int endY = (int) ev.getY();//终点的坐标
                int dy = endY - mStartY;//偏移量
                int firstVisiblePosition = getFirstVisiblePosition();//当前显示的第一个item的位置
                //必须下拉并且当前显示的是第一个item
                if (dy > 0 && firstVisiblePosition == 0){
                    int padding = dy - mHeaderViewHeight;//计算当前下拉控件的padding值
                    mHeaderView.setPadding(0,padding,0,0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(ev);
    }
}

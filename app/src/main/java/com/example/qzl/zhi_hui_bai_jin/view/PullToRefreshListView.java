package com.example.qzl.zhi_hui_bai_jin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.qzl.zhi_hui_bai_jin.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 下拉刷新的listView
 * Created by Qzl on 2016-08-03.
 */
public class PullToRefreshListView extends ListView {

    private static final int STATE_PULL_TO_REFRESH = 1;
    private static final int STATE_RELEASE_TO_REFRESH = 2;
    private static final int STATE_REFRESHING = 3;

    private int mCurrentState = STATE_PULL_TO_REFRESH;//当前状态

    private View mHeaderView;
    private int mHeaderViewHeight;
    private int mStartY = -1;
    private TextView mTvTitle;
    private TextView mTvTime;
    private ImageView mIvArrow;
    private RotateAnimation mAnimUp;
    private RotateAnimation mAnimDown;
    private ProgressBar mPbProgress;


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
        mTvTitle = (TextView) mHeaderView.findViewById(R.id.tv_pull_to_reresh_title);
        mTvTime = (TextView) mHeaderView.findViewById(R.id.tv_pull_to_reresh_time);
        mIvArrow = (ImageView) mHeaderView.findViewById(R.id.iv_pull_to_refresh_arrow);
        mPbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_pull_to_refresh_loading);
        //隐藏头布局
        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
        //箭头的动画
        initAnim();
        setCurrentTime();
    }

    /**
     * 设置刷新时间
     */
    private void setCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());
        mTvTime.setText(time);
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
                if (mCurrentState == STATE_REFRESHING) {
                    //如果是正在刷新的状态，直接跳出
                    break;
                }
                int endY = (int) ev.getY();//终点的坐标
                int dy = endY - mStartY;//偏移量
                int firstVisiblePosition = getFirstVisiblePosition();//当前显示的第一个item的位置
                //必须下拉并且当前显示的是第一个item
                if (dy > 0 && firstVisiblePosition == 0) {
                    int padding = dy - mHeaderViewHeight;//计算当前下拉控件的padding值
                    mHeaderView.setPadding(0, padding, 0, 0);
                    if (padding > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
                        //改为松开刷新
                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {
                        //改为下拉刷新
                        mCurrentState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mStartY = -1;
                if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
                    mCurrentState = STATE_REFRESHING;
                    refreshState();
                    //完整展示头布局
                    mHeaderView.setPadding(0, 0, 0, 0);
                    //4 进行回调
                    if (mListener != null) {
                        mListener.onRefresh();
                    }
                } else if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
                    //隐藏头布局
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 初始化箭头动画
     */
    private void initAnim() {
        mAnimUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mAnimUp.setDuration(200);
        mAnimUp.setFillAfter(true);//保持状态
        mAnimDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mAnimDown.setDuration(200);
        mAnimDown.setFillAfter(true);//保持状态
    }

    /**
     * 根据当前状态刷新界面
     */
    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_TO_REFRESH://下拉刷新
                mTvTitle.setText("下拉刷新");
                mPbProgress.setVisibility(View.INVISIBLE);
                mIvArrow.setVisibility(View.VISIBLE);
                mIvArrow.startAnimation(mAnimDown);
                break;
            case STATE_RELEASE_TO_REFRESH://松开刷新
                mTvTitle.setText("松开刷新");
                mPbProgress.setVisibility(View.INVISIBLE);
                mIvArrow.setVisibility(View.VISIBLE);
                mIvArrow.startAnimation(mAnimUp);
                break;
            case STATE_REFRESHING://正在刷新
                mTvTitle.setText("正在刷新...");
                //清除箭头动画，否则无法隐藏
                mIvArrow.clearAnimation();
                mPbProgress.setVisibility(View.VISIBLE);//显示进度条
                mIvArrow.setVisibility(View.INVISIBLE);//隐藏箭头
                break;
        }
    }

    /**
     * 刷新结束，收起控件
     */
    public void onRefreshComplete(boolean success) {

        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
        mCurrentState = STATE_RELEASE_TO_REFRESH;
        mTvTitle.setText("下拉刷新");
        mPbProgress.setVisibility(INVISIBLE);
        mIvArrow.setVisibility(VISIBLE);
        if (success) {//只有刷新之后才更新时间
            setCurrentTime();
        }
    }

    //3 定义成员变量，接收监听对象
    private OnRefreshListener mListener;

    /**
     * 2、暴露接口，设置监听
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * 1、下拉刷新的回调接口
     */
    public interface OnRefreshListener {
        public void onRefresh();
    }
}

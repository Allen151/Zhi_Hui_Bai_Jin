package com.example.qzl.zhi_hui_bai_jin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
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
public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener{

    private static final int STATE_PULL_TO_REFRESH = 1;//下拉刷新
    private static final int STATE_RELEASE_TO_REFRESH = 2;//松开刷新
    private static final int STATE_REFRESHING = 3;//正在刷新

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
    private View mFooterView;
    private int mFooterViewHeight;

    private boolean isLoadMore;//标记是否正在加载更多


    public PullToRefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh_header, null);
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
     * 初始化脚布局
     */
    private void initFooterView(){
        mFooterView = View.inflate(getContext(), R.layout.pull_to_refresh_foot,null);
        this.addFooterView(mFooterView);

        mFooterView.measure(0,0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0,-mFooterViewHeight,0,0);
        this.setOnScrollListener(this);//滑动监听

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
                int dy = endY - mStartY;//移动偏移量
                int firstVisiblePosition = getFirstVisiblePosition();//当前显示的第一个item的位置
                //必须下拉并且当前显示的是第一个item
                if (dy > 0 && firstVisiblePosition == 0) {
                    int padding = dy - mHeaderViewHeight;//计算当前下拉控件的padding值
                    if (padding > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
                        //改为松开刷新
                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {
                        //改为下拉刷新
                        mCurrentState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    }
                    mHeaderView.setPadding(0, padding, 0, 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mStartY = -1;
                if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
                    mCurrentState = STATE_REFRESHING;
                    //完整展示头布局
                    mHeaderView.setPadding(0, 0, 0, 0);
                    refreshState();
                } else if (mCurrentState == STATE_PULL_TO_REFRESH) {
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
                //4 进行回调
                if (mListener != null) {
                    mListener.onRefresh();
                }
                break;
        }
    }

    /**
     * 刷新结束，收起控件
     */
    public void onRefreshComplete(boolean success) {
        if (!isLoadMore) {
            mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
            mCurrentState = STATE_RELEASE_TO_REFRESH;
            mTvTitle.setText("下拉刷新");
            mPbProgress.setVisibility(INVISIBLE);
            mIvArrow.setVisibility(VISIBLE);
            if (success) {//只有刷新之后才更新时间
                setCurrentTime();
            }
        }else {
            //加载更多
            mFooterView.setPadding(0,-mFooterViewHeight,0,0);//隐藏加载更多布局
            isLoadMore = false;
        }
        mCurrentState = STATE_PULL_TO_REFRESH;
    }

    //3 定义成员变量，接收监听对象
    private OnRefreshListener mListener;

    /**
     * 2、暴露接口，设置监听
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    //滑动状态发生变化
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE){//空闲状态
            int lastVisiblePosition = getLastVisiblePosition();//最后一个空闲状态
            if (lastVisiblePosition == getCount() - 1 && !isLoadMore){//当前显示的是左后一个item并且没有加载更多
                //到底了
                Log.d("TAG", "加载更多。。。");
                isLoadMore = true;
                mFooterView.setPadding(0,0,0,0);//显示加载更多的布局
                setSelection(getCount() - 1);//将listView的位置显示在最后一个item上，从而加载更多会直接展示出来，无需手动滑动
                //通知主界面加载下一页数据
                if (mListener != null){
                    mListener.onLoadMore();
                }
            }
        }
    }
    //滑动过程回调
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * 1、下拉刷新的回调接口
     */
    public interface OnRefreshListener {
        public void onRefresh();
        //下拉加载更多
        public void onLoadMore();
    }
}

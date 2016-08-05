package com.example.qzl.zhi_hui_bai_jin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.qzl.zhi_hui_bai_jin.utils.DensityUtils;
import com.example.qzl.zhi_hui_bai_jin.utils.PrefUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 新手引导页面
 */
public class GuideActivity extends Activity {

    private ViewPager mvp_guid;

    private List<ImageView> mImageViewList;//imageview集合
    //引导页图片id数组
    private int[] mImageId = new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
    private LinearLayout mll_guide_container;
    private ImageView mIv_guide_redpoint;//小红点
    private int mMPointDistance;//小红点移动距离
    private Button mBtn_guide_start;//开始体验控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//取标题
        setContentView(R.layout.activity_guide);
        mvp_guid = (ViewPager) findViewById(R.id.vp_guid);
        mll_guide_container = (LinearLayout) findViewById(R.id.ll_guide_container);//初始化线性布局
        mIv_guide_redpoint = (ImageView) findViewById(R.id.iv_guide_redpoint);
        mBtn_guide_start = (Button) findViewById(R.id.btn_guide_start);

        InitData();
        mvp_guid.setAdapter(new GuideAdapter());
        //页面滑动的监听
        mvp_guid.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //当页面滑动过程中的回调
            // position ： 当前位置  positionOffset ： 移动偏移量（百分比）  positionOffsetPixels ： 具体移动了多少个像素
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //更新小红点的距离
                int leftMargin = (int) (mMPointDistance * positionOffset) + position * mMPointDistance;//计算小红点当前的左边距
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mIv_guide_redpoint.getLayoutParams();
                layoutParams.leftMargin = leftMargin;//修改左边距
                //重新设置布局参数
                mIv_guide_redpoint.setLayoutParams(layoutParams);
            }
            //页面被选中的时候
            @Override
            public void onPageSelected(int position) {
                if (position == mImageViewList.size() - 1){
                    //最后一个页面显示开始体验的按钮
                    mBtn_guide_start.setVisibility(View.VISIBLE);
                }else {
                    mBtn_guide_start.setVisibility(View.INVISIBLE);
                }
            }
            //页面状态改变的时候
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //计算两个圆点的距离
        // 移动距离 = 第二个left值 - 第一个left值
        //绘制流程 ：measure->layout(确定位置)->draw（activity的onCreate执行完之后才会执行）
        //mMPointDistance = mll_guide_container.getChildAt(1).getLeft() - mll_guide_container.getChildAt(0).getLeft();
        //Log.d("tag","小红点移动的距离:"+mMPointDistance);

        //监听layout方法结束的事件，位置确定好之后再去获取圆点间距
        //获取视图树/观察视图树的执行流程
        mIv_guide_redpoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //移出监听，避免重复回调
                //mIv_guide_redpoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);//可兼容较低的版本
                mIv_guide_redpoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //layout方法执行结束的回调
                mMPointDistance = mll_guide_container.getChildAt(1).getLeft() - mll_guide_container.getChildAt(0).getLeft();
//                Log.d("tag","小红点移动的距离:"+mMPointDistance);
            }
        });

        mBtn_guide_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更新sp,已经不是第一次进入了
                PrefUtils.setBoolean(getApplicationContext(),"is_first_enter",false);
                //跳到主页面
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }

    /**
     * 初始化数据（ImageView）
     */
    private void InitData(){
        mImageViewList = new ArrayList<>();
        for (int i = 0; i < mImageId.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mImageId[i]);//通过设置背景让宽高填充布局
//            imageView.setImageResource(mImageId[i]); //这个会根据自己的宽高填充背景
            mImageViewList.add(imageView);

            //初始化小圆点
            ImageView mPoint = new ImageView(getApplicationContext());
            mPoint.setImageResource(R.drawable.shape_point_gray);//设置图片shape形状
            //初始化布局参数，宽高包裹内容，父控件是谁，就是谁的布局参数
            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i > 0){
                //从第二个点开始设置左边距
                mParams.leftMargin = DensityUtils.dip2px(10,getApplicationContext());
            }
            mPoint.setLayoutParams(mParams);//设置布局参数
            mll_guide_container.addView(mPoint);//给容器添加圆点
        }
    }
    class GuideAdapter extends PagerAdapter{
        private ImageView mView;

        //返回item的个数
        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        //判断是不是view
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        //初始化item的布局
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            mView = mImageViewList.get(position);
            container.addView(mView);//将图片添加到布局中
            return mView;
        }
        //销毁item布局
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

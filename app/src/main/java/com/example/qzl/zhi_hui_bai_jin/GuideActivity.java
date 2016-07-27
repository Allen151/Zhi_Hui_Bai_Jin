package com.example.qzl.zhi_hui_bai_jin;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//取标题
        setContentView(R.layout.activity_guide);
        mvp_guid = (ViewPager) findViewById(R.id.vp_guid);
        InitData();
        mvp_guid.setAdapter(new GuideAdapter());
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
        }
    }
    class GuideAdapter extends PagerAdapter{
        private ImageView mView;

        //返回item的个数
        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        //初始化item的布局
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            mView = mImageViewList.get(position);
            container.addView(mView);
            return mView;
        }
        //销毁item布局
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

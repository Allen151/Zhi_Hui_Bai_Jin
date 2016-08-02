package com.example.qzl.zhi_hui_bai_jin.implayment.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.qzl.zhi_hui_bai_jin.R;
import com.example.qzl.zhi_hui_bai_jin.base.BaseMenuDetailPager;
import com.example.qzl.zhi_hui_bai_jin.domain.NewsMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * 菜单详情页-新闻
 * ViewPagerIndictor：使用流程
 * 1 引入库
 * 2 解决support-v4冲突（让两个版本一致）
 * 3 从例子程序中考贝布局文件
 * 4 从例子程序中考贝相关代码（指示器和ViewPager绑定，重写getPageTitle返回标题）
 * 5 在清单文件中增加样式
 * 6 背景修改为白色
 * 7 修改样式（背景样式，文字样式）
 * Created by Qzl on 2016-08-01.
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager{

    @ViewInject(R.id.vp_news_menu_detail)
    private ViewPager mViewPager;
    @ViewInject(R.id.indicator)
    private TabPageIndicator mIndicator;

    private ArrayList<NewsMenu.NewsTabData> mTabData;//页签网络数据
    private ArrayList<TabDetailPager> mPagers;

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsMenu.NewsTabData> children) {
        super(activity);
        mTabData = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_news_menu_detail,null);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void initData() {
        //初始化页签
        mPagers = new ArrayList<>();
        for (int i = 0; i < mTabData.size(); i++) {
            TabDetailPager pager = new TabDetailPager(mActivity,mTabData.get(i));
            mPagers.add(pager);
        }
        mViewPager.setAdapter(new NewsMenuDeatilAdapter());
        //将指示器和viewPager绑定在一起
        // 注意 ： 必须在viewpager设置完数据之后去绑定
        mIndicator.setViewPager(mViewPager);
    }

    class NewsMenuDeatilAdapter extends PagerAdapter{
        //指定指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            NewsMenu.NewsTabData data = mTabData.get(position);
            return data.title;
        }

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mPagers.get(position);
            View view = pager.mRootView;
            container.addView(view);
            pager.initData();
            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

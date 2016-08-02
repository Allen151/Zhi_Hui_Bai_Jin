package com.example.qzl.zhi_hui_bai_jin.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.qzl.zhi_hui_bai_jin.MainActivity;
import com.example.qzl.zhi_hui_bai_jin.R;
import com.example.qzl.zhi_hui_bai_jin.base.BasePager;
import com.example.qzl.zhi_hui_bai_jin.implayment.GovAffairsPager;
import com.example.qzl.zhi_hui_bai_jin.implayment.HomePager;
import com.example.qzl.zhi_hui_bai_jin.implayment.NewsCenterPager;
import com.example.qzl.zhi_hui_bai_jin.implayment.SettingPager;
import com.example.qzl.zhi_hui_bai_jin.implayment.SmartServicePager;
import com.example.qzl.zhi_hui_bai_jin.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qzl on 2016-07-27.
 */
public class ContentFragment extends BaseFragment {


    private List<BasePager> mPagers;//五个标签页的集合
    private NoScrollViewPager mNsvp_fcontent_content;
    private RadioGroup mRg_fcontent_group;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content,null);
        mNsvp_fcontent_content = (NoScrollViewPager) view.findViewById(R.id.nsvp_fcontent_content);
        mRg_fcontent_group = (RadioGroup) view.findViewById(R.id.rg_fcontent_group);
        return view;
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        mPagers = new ArrayList<>();
        //填充数据
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new GovAffairsPager(mActivity));
        mPagers.add(new SettingPager(mActivity));
        mNsvp_fcontent_content.setAdapter(new ContentAdapter());

        //设置RadioGroup的监听事件，低栏标签切换监听
        mRg_fcontent_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_contentfragment_home:
                        //首页被选中
                        mNsvp_fcontent_content.setCurrentItem(0,false);
                        break;
                    case R.id.rb_contentfragment_news:
                        //新闻中心
                        mNsvp_fcontent_content.setCurrentItem(1,false);//参二表示是否具有滑动动画
                        break;
                    case R.id.rb_contentfragment_smart:
                        //智慧服务
                        mNsvp_fcontent_content.setCurrentItem(2,false);
                        break;
                    case R.id.rb_contentfragment_gov:
                        //政务
                        mNsvp_fcontent_content.setCurrentItem(3,false);
                        break;
                    case R.id.rb_contentfragment_setting:
                        //设置
                        mNsvp_fcontent_content.setCurrentItem(4,false);
                        break;
                }
            }
        });

        mNsvp_fcontent_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滑动的时候
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //页面被选中
            @Override
            public void onPageSelected(int position) {
                BasePager pager = mPagers.get(position);
                pager.initData();
                if (position == 0 || position == mPagers.size() -1){
                    //首页和设置页要禁用菜单栏
                    setSlidingMenuEnable(false);
                }else {
                    //其他页面要开启侧边栏
                    setSlidingMenuEnable(true);
                }
            }
            //状态改变
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //手动加载第一页数据
        mPagers.get(0).initData();
        //首页禁用
        setSlidingMenuEnable(false);
    }

    /**
     * 开启或禁用菜单栏
     * @param enable
     */
    private void setSlidingMenuEnable(boolean enable) {
        //获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if (enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            //设置不可触摸
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPagers.size();
        }

        //判断是不是view
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPagers.get(position);
            //获取当前页面对象的布局
            View view = pager.mRootView;
//            pager.initData();//初始化数据，viewPager会默认加载下一个界面，为了节省流量和性能，不要在此处调运初始化数据的方法
            container.addView(view);
            return view;
        }

        //销毁view
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mNsvp_fcontent_content.removeView((View) object);
        }
    }
    public NewsCenterPager getNewsCenterPager(){
        NewsCenterPager pager = (NewsCenterPager) mPagers.get(1);
        return pager;
    }
}

package com.example.qzl.zhi_hui_bai_jin.fragment;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.qzl.zhi_hui_bai_jin.R;
import com.example.qzl.zhi_hui_bai_jin.base.BasePager;
import com.example.qzl.zhi_hui_bai_jin.implayment.GovAffairsPager;
import com.example.qzl.zhi_hui_bai_jin.implayment.HomePager;
import com.example.qzl.zhi_hui_bai_jin.implayment.NewsCenterPager;
import com.example.qzl.zhi_hui_bai_jin.implayment.SettingPager;
import com.example.qzl.zhi_hui_bai_jin.implayment.SmartServicePager;
import com.example.qzl.zhi_hui_bai_jin.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qzl on 2016-07-27.
 */
public class ContentFragment extends BaseFragment {


    private List<BasePager> mPagers;//五个标签页的集合
    private NoScrollViewPager mNsvp_fcontent_content;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content,null);
        mNsvp_fcontent_content = (NoScrollViewPager) view.findViewById(R.id.nsvp_fcontent_content);

        return view;
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<>();
        //填充数据
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new GovAffairsPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new SettingPager(mActivity));
        mNsvp_fcontent_content.setAdapter(new ContentAdapter());
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
            pager.initData();
            container.addView(view);
            return view;
        }

        //销毁view
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mNsvp_fcontent_content.removeView((View) object);
        }
    }
}

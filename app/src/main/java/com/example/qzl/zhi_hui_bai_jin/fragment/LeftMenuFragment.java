package com.example.qzl.zhi_hui_bai_jin.fragment;

import android.view.View;

import com.example.qzl.zhi_hui_bai_jin.R;

/**
 * 侧边栏
 * Created by Qzl on 2016-07-27.
 */
public class LeftMenuFragment extends BaseFragment {
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu,null);
        return view;
    }

    @Override
    public View initData() {
        return null;
    }
}

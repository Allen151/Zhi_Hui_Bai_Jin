package com.example.qzl.zhi_hui_bai_jin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
    //这个activity就是mainActivity
    public FragmentActivity mActivity;

    /**
     * fragment创建
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取当前fragment所依赖的activity
        mActivity = getActivity();
    }

    /**
     * 初始化fragment布局
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = initView();
        return view;
    }

    /**
     * fragment所依赖的activity的onCreate方法执行结束
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化数据
        initData();
    }

    /**
     * 初始化布局，必须由子类实现
     * @return
     */
    public abstract View initView();
    /**
     * 初始数据，必须由子类实现
     * @return
     */
    public abstract void initData();
}

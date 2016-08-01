package com.example.qzl.zhi_hui_bai_jin.implayment;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.qzl.zhi_hui_bai_jin.MainActivity;
import com.example.qzl.zhi_hui_bai_jin.base.BaseMenuDetailPager;
import com.example.qzl.zhi_hui_bai_jin.base.BasePager;
import com.example.qzl.zhi_hui_bai_jin.domain.NewsMenu;
import com.example.qzl.zhi_hui_bai_jin.fragment.LeftMenuFragment;
import com.example.qzl.zhi_hui_bai_jin.global.GlobalConstants;
import com.example.qzl.zhi_hui_bai_jin.implayment.menu.InteractMenuDetailPager;
import com.example.qzl.zhi_hui_bai_jin.implayment.menu.NewsMenuDetailPager;
import com.example.qzl.zhi_hui_bai_jin.implayment.menu.NewsTopicDetailPager;
import com.example.qzl.zhi_hui_bai_jin.implayment.menu.PhotosMenuDetailPager;
import com.example.qzl.zhi_hui_bai_jin.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * 新闻中心的界面
 * Created by Qzl on 2016-07-28.
 */
public class NewsCenterPager extends BasePager {
    private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;//菜单详情也集合
    private NewsMenu mNewsData;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        Log.d("tag","新闻中心初始化了...");
        //要给帧布局填充布局对象
//        TextView view = new TextView(mActivity);
//        view.setText("新闻中心");
//        view.setTextColor(Color.RED);
//        view.setTextSize(22);
//        view.setGravity(Gravity.CENTER);
//        mFl_base_pager_content.addView(view);
        //修改页面标题
        mTv_base_pager_title.setText("新闻中心");
        //显示菜单按钮
        mBtn_base_pager_menu.setVisibility(View.VISIBLE);

        //先判断有没有缓存，如果有的话，先读取缓存
        String cache = CacheUtils.getCache(GlobalConstants.CATEGORY_URL,mActivity);
        if (!TextUtils.isEmpty(cache)){
            Log.d("tag","发现缓存了。。。");
            processData(cache);
        }
        //请求服务器获取数据
        //开源框架 xUtils
        getDataFromServer();
    }

    /**
     * 从服务器获取数据
     * 需要权限：访问网络，写sd卡
     */
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.CATEGORY_URL, new RequestCallBack<String>() {
            private String mResult;

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //请求成功
                mResult = responseInfo.result;//获取服务器的返回结果
                Log.d("tag","服务器返回结果 mResult = "+mResult);
                // Gson 解析
                processData(mResult);
                //写缓存
                CacheUtils.setCache(GlobalConstants.CATEGORY_URL,mResult,mActivity);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                //请求失败
                e.printStackTrace();
                Toast.makeText(mActivity,s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 解析数据
     */
    private void processData(String json) {
        // Gson：Google的Json
        Gson gson = new Gson();
        mNewsData = gson.fromJson(json, NewsMenu.class);
//        System.out.println("解析结果："+data);
        //获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();

        //给侧边栏设置数据
        leftMenuFragment.setMenuData(mNewsData.data);

        //初始化四个菜单详情页
        mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new NewsTopicDetailPager(mActivity));
        mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));

        //将新闻菜单详情页设置为默认页
        setCurrentDetailPager(0);
    }

    /**
     * 设置菜单详情页
     * @param position
     */
    public void setCurrentDetailPager(int position){
        //重新给frameLayout添加内容
        BaseMenuDetailPager pager = mMenuDetailPagers.get(position);//获取当前应该显示的界面
        View view = pager.mRootView;//当前页面的布局
        //清除之前旧的布局
        mFl_base_pager_content.removeAllViews();
        mFl_base_pager_content.addView(view);//给帧布局添加布局

        //初始化页面数据
        pager.initData();

        //更新标题
        mTv_base_pager_title.setText(mNewsData.data.get(position).title);
    }
}

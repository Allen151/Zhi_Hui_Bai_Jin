package com.example.qzl.zhi_hui_bai_jin.implayment;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qzl.zhi_hui_bai_jin.base.BasePager;
import com.example.qzl.zhi_hui_bai_jin.domain.NewsMenu;
import com.example.qzl.zhi_hui_bai_jin.global.GlobalConstants;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 新闻中心的界面
 * Created by Qzl on 2016-07-28.
 */
public class NewsCenterPager extends BasePager {
    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        Log.d("tag","新闻中心初始化了...");
        //要给帧布局填充布局对象
        TextView view = new TextView(mActivity);
        view.setText("新闻中心");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        mFl_base_pager_content.addView(view);
        //修改页面标题
        mTv_base_pager_title.setText("新闻中心");
        //显示菜单按钮
        mBtn_base_pager_menu.setVisibility(View.VISIBLE);

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
        NewsMenu data = gson.fromJson(json, NewsMenu.class);
        System.out.println("解析结果："+data);
    }
}

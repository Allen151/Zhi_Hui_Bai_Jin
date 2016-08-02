package com.example.qzl.zhi_hui_bai_jin.implayment.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qzl.zhi_hui_bai_jin.R;
import com.example.qzl.zhi_hui_bai_jin.base.BaseMenuDetailPager;
import com.example.qzl.zhi_hui_bai_jin.domain.NewsMenu;
import com.example.qzl.zhi_hui_bai_jin.domain.NewsTabBean;
import com.example.qzl.zhi_hui_bai_jin.global.GlobalConstants;
import com.example.qzl.zhi_hui_bai_jin.utils.CacheUtils;
import com.example.qzl.zhi_hui_bai_jin.view.TopNewsViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by Qzl on 2016-08-02.
 */
public class TabDetailPager extends BaseMenuDetailPager{
    private NewsMenu.NewsTabData mTabData;//单个页签的网络数据
//    private TextView mView;
    @ViewInject(R.id.vp_tab_detail_top_news)
    private TopNewsViewPager mViewPager;

    @ViewInject(R.id.tv_tab_detail_title)
    private TextView tvTitle;

    private final String mUrl;

    private ArrayList<NewsTabBean.TopNews> mTopNews;
    private BitmapUtils mBitmapUtils;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = GlobalConstants.SERVER_URL + mTabData.url;
    }

    @Override
    public View initView() {
//        mView = new TextView(mActivity);
//        //view.setText(mTabData.title);//此处空指针
//        mView.setTextColor(Color.RED);
//        mView.setTextSize(22);
//        mView.setGravity(Gravity.CENTER);
        View view = View.inflate(mActivity, R.layout.pager_tab_detail,null);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void initData() {
//        mView.setText(mTabData.title);
        String cache = CacheUtils.getCache(mUrl,mActivity);
        if (cache != null){
            processData(cache);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result);
                //设置缓存
                CacheUtils.setCache(mUrl,result,mActivity);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //请求失败
                error.printStackTrace();
                Toast.makeText(mActivity,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //解析数据
    private void processData(String result) {
        Gson gson = new Gson();
        NewsTabBean newsTabBean = gson.fromJson(result, NewsTabBean.class);
        //头条新闻填充数据
        mTopNews = newsTabBean.data.topnews;
        if (mTopNews != null){
            mViewPager.setAdapter(new TopNewsAdapter());
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    //更新头条新闻的标题
                    NewsTabBean.TopNews topNews = mTopNews.get(position);
                    tvTitle.setText(topNews.title);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            //更新第一个头条新闻标题
            tvTitle.setText(mTopNews.get(0).title);
        }
    }

    //头条新闻数据适配器
    class TopNewsAdapter extends PagerAdapter{
        public TopNewsAdapter(){
            mBitmapUtils = new BitmapUtils(mActivity);
            //设置加载中的默认图片
            mBitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
        }

        @Override
        public int getCount() {
            return mTopNews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(mActivity);
//            view.setImageResource(R.drawable.topnews_item_default);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            String imageUrl = mTopNews.get(position).topimage;//图片的下载链接
            //下载图片，将图片设置给imageView，避免内存溢出，缓存
            //BitmapUtils->xUtils
            mBitmapUtils.display(view,imageUrl);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

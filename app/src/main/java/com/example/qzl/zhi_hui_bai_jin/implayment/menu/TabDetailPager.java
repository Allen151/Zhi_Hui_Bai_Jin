package com.example.qzl.zhi_hui_bai_jin.implayment.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qzl.zhi_hui_bai_jin.NewsDetailActivity;
import com.example.qzl.zhi_hui_bai_jin.R;
import com.example.qzl.zhi_hui_bai_jin.base.BaseMenuDetailPager;
import com.example.qzl.zhi_hui_bai_jin.domain.NewsMenu;
import com.example.qzl.zhi_hui_bai_jin.domain.NewsTabBean;
import com.example.qzl.zhi_hui_bai_jin.global.GlobalConstants;
import com.example.qzl.zhi_hui_bai_jin.utils.CacheUtils;
import com.example.qzl.zhi_hui_bai_jin.utils.PrefUtils;
import com.example.qzl.zhi_hui_bai_jin.view.PullToRefreshListView;
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
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by Qzl on 2016-08-02.
 */
public class TabDetailPager extends BaseMenuDetailPager {
    private NewsMenu.NewsTabData mTabData;//单个页签的网络数据
    //    private TextView mView;
    @ViewInject(R.id.vp_tab_detail_top_news)
    private TopNewsViewPager mViewPager;

    @ViewInject(R.id.tv_tab_detail_title)
    private TextView tvTitle;

    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator;

    @ViewInject(R.id.lv_tab_detail_list)
    private PullToRefreshListView mLvList;

    private final String mUrl;

    private ArrayList<NewsTabBean.TopNews> mTopNews;
    private BitmapUtils mBitmapUtils;
    private ArrayList<NewsTabBean.NewsData> mNewsList;
    private NewsAdapter mNewsAdapter;
    private String mMoreUrl;

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
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        ViewUtils.inject(this, view);

        //给listView添加头布局
        View mHeaderView = View.inflate(mActivity, R.layout.list_item_header, null);
        ViewUtils.inject(this, mHeaderView);//此处必须将头布局也注入
        mLvList.addHeaderView(mHeaderView);
        //5 前端界面设置回调
        mLvList.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                getDataFromServer();
            }
            //加载下一页数据
            @Override
            public void onLoadMore() {
                //判断是否有下一页数据
                if (mMoreUrl != null){
                    //有下一页数据
                    getMoreDataFromServer();
                }else {
                    //没有下一页数据
                    Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    //没有数据时，也要收起控件
                    mLvList.onRefreshComplete(true);
                }
            }
        });
        //设置listView的点击事件
        mLvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int headerViewsCount = mLvList.getHeaderViewsCount();//获取头布局的数量
                position = position - headerViewsCount;//需要减去头布局的占位
                Log.d("TAG", "第 "+position+"个被点击了");

                NewsTabBean.NewsData news = mNewsList.get(position);
                // read_ids:1101,1102,1105,1203
                String readIds = PrefUtils.getString(mActivity,"read_ids","");
                if (!readIds.contains(news.id+"")) {//只有不包含当前id才追加，避免重复追加同一数据
                    readIds = readIds + news.id + ",";
                    PrefUtils.setString(mActivity,"read_ids",readIds);
                }
                //要将被点击的item的文字颜色改为灰色，局部刷新，view对象就是当前被点击的对象
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_item_news_title);
                tvTitle.setTextColor(Color.GRAY);
//                mNewsAdapter.notifyDataSetChanged();//全局刷新，性能比较低

                //跳到新闻详情页面
                mActivity.startActivity(new Intent(mActivity, NewsDetailActivity.class));
            }
        });
        return view;
    }

    /**
     * 加载下一页数据
     */
    private void getMoreDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result,true);
                //收起下拉刷新控件
                mLvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //请求失败
                error.printStackTrace();
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                //收起下拉刷新控件
                mLvList.onRefreshComplete(false);
            }
        });
    }

    @Override
    public void initData() {
//        mView.setText(mTabData.title);
        String cache = CacheUtils.getCache(mUrl, mActivity);
        if (cache != null) {
            processData(cache,false);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result,false);
                //设置缓存
                CacheUtils.setCache(mUrl, result, mActivity);
                //收起下拉刷新控件
                mLvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //请求失败
                error.printStackTrace();
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                //收起下拉刷新控件
                mLvList.onRefreshComplete(false);
            }
        });
    }

    //解析数据
    private void processData(String result,boolean isMore) {
        Gson gson = new Gson();
        NewsTabBean newsTabBean = gson.fromJson(result, NewsTabBean.class);
        String moreUrl = newsTabBean.data.more;
        if (!TextUtils.isEmpty(moreUrl)){
            //下一页数据连接
            mMoreUrl = GlobalConstants.SERVER_URL + moreUrl;
        }else {
            mMoreUrl = null;
        }
        if (!isMore) {
            //头条新闻填充数据
            mTopNews = newsTabBean.data.topnews;
            if (mTopNews != null) {
                mViewPager.setAdapter(new TopNewsAdapter());
                mIndicator.setViewPager(mViewPager);
                mIndicator.setSnap(true);//快照方式展示
                //事件要设置给Indicator
                mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                mIndicator.onPageSelected(0);//默认让第一个选中（解决页面销毁后重新初始化时，Indicator任然保留上个位置的bug）
            }
            //列表新闻
            mNewsList = newsTabBean.data.news;
            if (mNewsList != null) {
                mNewsAdapter = new NewsAdapter();
                mLvList.setAdapter(mNewsAdapter);
            }
        }else {
            //加载更多数据
            ArrayList<NewsTabBean.NewsData> moreNews = newsTabBean.data.news;
            //将数据追加到原来的集合中
            mNewsList.addAll(moreNews);
            //刷新listView
            mNewsAdapter.notifyDataSetChanged();
        }
    }

    //头条新闻数据适配器
    class TopNewsAdapter extends PagerAdapter {
        public TopNewsAdapter() {
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
            mBitmapUtils.display(view, imageUrl);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class NewsAdapter extends BaseAdapter {

        private BitmapUtils mBitmapUtils;

        public NewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public NewsTabBean.NewsData getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_news, null);
                holder = new ViewHolder();
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_item_news_icon);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_item_news_title);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_item_news_data);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            NewsTabBean.NewsData news = getItem(position);
            //设置标题
            holder.tvTitle.setText(news.title);
            //设置时间
            holder.tvDate.setText(news.pubdate);
            //已读新闻的回显操作
            String readIds = PrefUtils.getString(mActivity,"read_ids","");
            if (readIds.contains(news.id+"")){
                holder.tvTitle.setTextColor(Color.GRAY);
            }else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }
            //设置图片
            mBitmapUtils.display(holder.ivIcon, news.listimage);
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView ivIcon;
        public TextView tvTitle, tvDate;
    }
}

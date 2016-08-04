package com.example.qzl.zhi_hui_bai_jin.implayment.menu;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qzl.zhi_hui_bai_jin.R;
import com.example.qzl.zhi_hui_bai_jin.base.BaseMenuDetailPager;
import com.example.qzl.zhi_hui_bai_jin.domain.PhotosBean;
import com.example.qzl.zhi_hui_bai_jin.global.GlobalConstants;
import com.example.qzl.zhi_hui_bai_jin.utils.CacheUtils;
import com.example.qzl.zhi_hui_bai_jin.utils.MyBitmapUtils;
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
 * 菜单详情页-组图
 * Created by Qzl on 2016-08-01.
 */
public class PhotosMenuDetailPager extends BaseMenuDetailPager implements View.OnClickListener{
    @ViewInject(R.id.lv_ppmd_photo)
    private ListView mLvPhoto;
    @ViewInject(R.id.gv_ppmd_photo)
    private GridView mGvPhoto;
    private ArrayList<PhotosBean.PhotoNews> mNewsList;

    private ImageButton mBtnPhoto;
    public PhotosMenuDetailPager(Activity activity, ImageButton btnPhoto) {
        super(activity);
        btnPhoto.setOnClickListener(this);//组图按钮切换点击事件
        mBtnPhoto = btnPhoto;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail,null);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(GlobalConstants.PHOTOS_URL,mActivity);
        if (!TextUtils.isEmpty(cache)){
            processData(cache);
        }
        getDataFromServer();
    }

    /**
     * 加载网络数据
     */
    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, GlobalConstants.PHOTOS_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                //解析数据
                processData(result);
                CacheUtils.setCache(GlobalConstants.PHOTOS_URL,result,mActivity);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //请求失败
                error.printStackTrace();
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processData(String result) {
        Gson gson = new Gson();
        PhotosBean photosBean = gson.fromJson(result, PhotosBean.class);

        mNewsList = photosBean.data.news;

        mLvPhoto.setAdapter(new PhotoAdapter());
        mGvPhoto.setAdapter(new PhotoAdapter());//gridView的布局结构和listView的完全一致，所以可以共用一个adapter
    }

    private boolean isListView = true;//标记当前是否是listView展示
    @Override
    public void onClick(View view) {
        if (isListView){
            //切成gridView
            mLvPhoto.setVisibility(View.GONE);
            mGvPhoto.setVisibility(View.VISIBLE);
            mBtnPhoto.setImageResource(R.drawable.icon_pic_list_type);
            isListView = false;
        }else {
            //切成listView
            mLvPhoto.setVisibility(View.VISIBLE);
            mGvPhoto.setVisibility(View.GONE);
            mBtnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
            isListView = true;
        }
    }

    class PhotoAdapter extends BaseAdapter{

//        private BitmapUtils mBitmapUtils;
        private MyBitmapUtils mBitmapUtils;

        public PhotoAdapter(){
            mBitmapUtils = new MyBitmapUtils();
//            mBitmapUtils = new BitmapUtils(mActivity);
//            mBitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
        }
        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public PhotosBean.PhotoNews getItem(int i) {
            return mNewsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null){
                view = View.inflate(mActivity,R.layout.list_item_photos,null);
                holder = new ViewHolder();
                holder.mIvPic = (ImageView) view.findViewById(R.id.iv_lip_pic);
                holder.mLvTitle = (TextView) view.findViewById(R.id.tv_lip_title);
                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }
            PhotosBean.PhotoNews item = getItem(i);
            holder.mLvTitle.setText(item.title);
            mBitmapUtils.display(holder.mIvPic,item.listimage);
            return view;
        }
    }

    static class ViewHolder{
        private ImageView mIvPic;
        private TextView mLvTitle;
    }
}

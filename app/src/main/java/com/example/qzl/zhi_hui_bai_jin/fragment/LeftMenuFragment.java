package com.example.qzl.zhi_hui_bai_jin.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.qzl.zhi_hui_bai_jin.R;
import com.example.qzl.zhi_hui_bai_jin.domain.NewsMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 侧边栏
 * Created by Qzl on 2016-07-27.
 */
public class LeftMenuFragment extends BaseFragment {
    @ViewInject(R.id.lv_fragmentleft_list)
    private View mLv_fragmentleft_list;

    private ArrayList<NewsMenu.NewsMenuData> mNewsMenuDatas;//侧边栏网络数据对象
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu,null);
        //mLv_fragmentleft_list = view.findViewById(R.id.lv_fragmentleft_list);

        ViewUtils.inject(this,view);//注入view和事件

        return view;
    }

    @Override
    public void initData() {
    }

    /**
     * 给侧边栏设置数据
     * @param data
     */
    public void setMenuData(ArrayList<NewsMenu.NewsMenuData> data){
        //更新页面
        mNewsMenuDatas = data;
    }
    class LeftMenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mNewsMenuDatas.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int i) {
            return mNewsMenuDatas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            
            return null;
        }
    }
}

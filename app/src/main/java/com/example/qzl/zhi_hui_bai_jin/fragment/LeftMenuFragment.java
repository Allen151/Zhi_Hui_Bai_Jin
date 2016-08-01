package com.example.qzl.zhi_hui_bai_jin.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qzl.zhi_hui_bai_jin.MainActivity;
import com.example.qzl.zhi_hui_bai_jin.R;
import com.example.qzl.zhi_hui_bai_jin.domain.NewsMenu;
import com.example.qzl.zhi_hui_bai_jin.implayment.NewsCenterPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 侧边栏
 * Created by Qzl on 2016-07-27.
 */
public class LeftMenuFragment extends BaseFragment {
    @ViewInject(R.id.lv_fragmentleft_list)
    private ListView mLv_fragmentleft_list;

    private ArrayList<NewsMenu.NewsMenuData> mNewsMenuDatas;//侧边栏网络数据对象

    private int mCurrentPos;//当前被选中的item的位置
    private LeftMenuAdapter mMAdapter;

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
        mCurrentPos = 0;//当前选中的位置归零
        //更新页面
        mNewsMenuDatas = data;
        mMAdapter = new LeftMenuAdapter();
        mLv_fragmentleft_list.setAdapter(mMAdapter);
        mLv_fragmentleft_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentPos = i;//更新当前被选中的位置
                mMAdapter.notifyDataSetChanged();//刷新listView
                //收起侧边栏
                toggle();
                //侧边栏收起之后，要改变新闻中心的FrameLayout中的内容
                setCurrentDetailPager(i);
            }
        });
    }

    //设置当前的菜单详情页
    private void setCurrentDetailPager(int i) {
        //获取新闻中心的对象
        MainActivity mainUI = (MainActivity) mActivity;
        //获取contentFragment
        ContentFragment fragment = mainUI.getContentFragment();
        //获取新闻中心
        NewsCenterPager newsCenterPager = fragment.getNewsCenterPager();
        //修改新闻中心的frameLayout
        newsCenterPager.setCurrentDetailPager(i);
    }

    /**
     * 打开或者关闭侧边栏
     */
    private void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();//如果当前状态是开，调运后就是关，反之亦然
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
            View view1 = View.inflate(mActivity,R.layout.list_item_leftmenu,null);
            TextView tv_letmenu_menu = (TextView) view1.findViewById(R.id.tv_leftmenu_menu);
            NewsMenu.NewsMenuData item = getItem(i);
            tv_letmenu_menu.setText(item.title);
            if (i == mCurrentPos){
                //被选中
                tv_letmenu_menu.setEnabled(true);//文字变为红色
            }else {
                //没有选中
                tv_letmenu_menu.setEnabled(false);//文字变为白色
            }
            return view1;
        }
    }
}

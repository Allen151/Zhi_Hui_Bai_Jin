package com.example.qzl.zhi_hui_bai_jin;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 新闻详情页
 */
public class NewsDetailActivity extends Activity {

    @ViewInject(R.id.ll_tb_control)
    private LinearLayout mLlControl;
    @ViewInject(R.id.btn_base_pager_back)
    private ImageButton mBtnBack;
    @ViewInject(R.id.btn_base_pager_back)
    private ImageButton mBtnTextSize;
    @ViewInject(R.id.btn_base_pager_share)
    private ImageButton mBtnShare;
    @ViewInject(R.id.btn_base_pager_menu)
    private ImageButton mBtnMenu;
    @ViewInject(R.id.wv_and_news_detail)
    private WebView mWebView;
    @ViewInject(R.id.pb_and_loading)
    private ProgressBar mPbLoading;

    private String TAG = "NewsDetailActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);
        ViewUtils.inject(this);

        mLlControl.setVisibility(View.VISIBLE);
        mBtnBack.setVisibility(View.VISIBLE);
        mBtnMenu.setVisibility(View.GONE);

        mWebView.loadUrl("http://www.itheima.com");

        WebSettings settings = mWebView.getSettings();
        settings.setBuiltInZoomControls(true);//设置显示缩放按钮
        settings.setUseWideViewPort(true);//支持双击缩放
        settings.setJavaScriptEnabled(true);//支持js功能

        mWebView.setWebViewClient(new WebViewClient(){
            //开始加载网页
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(TAG, "开始加载网页了。。。");
                mPbLoading.setVisibility(View.VISIBLE);
            }

            //网页加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "网页加载结束了");
                mPbLoading.setVisibility(View.INVISIBLE);
            }

            //所有链接跳转会走此方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "跳转链接："+url);
                view.loadUrl(url);//在跳转页面时，强制在当前webView中加载
                return true;
            }
        });
        //mWebView.goBack();//跳到上个页面
        //mWebView.goForward();//跳到上个页面
        mWebView.setWebChromeClient(new WebChromeClient(){
            //进度发生变化
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //进度发生变化
                Log.d(TAG, "进度："+newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //网页标题
                Log.d(TAG, "网页标题:"+title);
            }
        });
    }
}

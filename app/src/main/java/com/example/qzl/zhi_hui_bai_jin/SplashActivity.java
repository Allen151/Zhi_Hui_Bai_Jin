package com.example.qzl.zhi_hui_bai_jin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.qzl.zhi_hui_bai_jin.utils.PrefUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 闪屏页
 */
public class SplashActivity extends Activity {

    private RelativeLayout mrl_root;
    private boolean mIs_first_enter;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mrl_root = (RelativeLayout) findViewById(R.id.rl_root);
        //闪屏页开始动画
        startAnim();


    }

    /**
     * 闪屏页开启动画
     */
    private void startAnim() {
        // 1 旋转动画
        // Animation.RELATIVE_TO_SELF : 基于自身
        RotateAnimation animRotate = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animRotate.setDuration(1000);//播放时间
        animRotate.setFillAfter(true);//保持动画结束的状态

        //2 缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);

        //3 渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);

        //4 动画集合
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(animRotate);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        //5 启动动画
        mrl_root.startAnimation(animationSet);
        //设置动画监听器
        animationSet.setAnimationListener(new Animation.AnimationListener() {

            //动画开始的时候调运
            @Override
            public void onAnimationStart(Animation animation) {

            }
            //动画结束的时候调运
            @Override
            public void onAnimationEnd(Animation animation) {
                //需要跳转界面
                //如果是第一次进入，跳新手引导，否则，跳主界面
                mIs_first_enter = PrefUtils.getBoolean(SplashActivity.this,"is_first_enter",true);
                if (mIs_first_enter){
                    //新手引导
                    mIntent = new Intent(getApplicationContext(),GuideActivity.class);
                }else {
                    //主界面
                    mIntent = new Intent(getApplicationContext(),MainActivity.class);
                }
                startActivity(mIntent);
                //结束当前页面
                finish();
            }
            //动画重复的时候调运
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

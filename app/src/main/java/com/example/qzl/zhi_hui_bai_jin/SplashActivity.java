package com.example.qzl.zhi_hui_bai_jin;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

/**
 * 闪屏页
 */
public class SplashActivity extends Activity {

    private RelativeLayout mrl_root;

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
    }
}

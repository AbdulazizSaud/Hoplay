package com.hoplay.kay.hoplay.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.hoplay.kay.hoplay.App.App;
import com.hoplay.kay.hoplay.Cores.AuthenticationCore.LoginCore;
import com.hoplay.kay.hoplay.R;
import com.hoplay.kay.hoplay.Services.CallbackHandlerCondition;
import com.hoplay.kay.hoplay.Services.HandlerCondition;

public class SplashScreen extends AppCompatActivity {

    String versionName="";
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_splash_screen);

        logo = (ImageView) findViewById(R.id.start_logo_imageview);




         // SCALING ANIMATION !!!!!!!!!
//        final ScaleAnimation growAnim = new ScaleAnimation(1.0f, 1.15f, 1.0f, 1.15f);
//        final ScaleAnimation shrinkAnim = new ScaleAnimation(1.15f, 1.0f, 1.15f, 1.0f);
//
//        growAnim.setDuration(500);
//        shrinkAnim.setDuration(500);
//
//        logo.setAnimation(growAnim);
//        growAnim.start();
//
//        growAnim.setAnimationListener(new Animation.AnimationListener()
//        {
//            @Override
//            public void onAnimationStart(Animation animation){}
//
//            @Override
//            public void onAnimationRepeat(Animation animation){}
//
//            @Override
//            public void onAnimationEnd(Animation animation)
//            {
//                logo.setAnimation(shrinkAnim);
//                shrinkAnim.start();
//            }
//        });
//        shrinkAnim.setAnimationListener(new Animation.AnimationListener()
//        {
//            @Override
//            public void onAnimationStart(Animation animation){}
//
//            @Override
//            public void onAnimationRepeat(Animation animation){}
//
//            @Override
//            public void onAnimationEnd(Animation animation)
//            {
//                logo.setAnimation(growAnim);
//                growAnim.start();
//            }
//        });





        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pInfo.versionName;
            int verCode = pInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(4000);
                    Intent intent;
                    // Check if the user have the latest version
                    if (!versionName.equals(App.versionName)){
                        intent = new Intent(getApplicationContext(), VersionUpdate.class);
                    }
                    else {
                        intent = new Intent(getApplicationContext(), LoginCore.class);
                    }
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();

    }




    // SCALING ANIMATION
//    public void scaleView(View v, float startScale, float endScale) {
//        Animation anim = new ScaleAnimation(
//                1f, 1f, // Start and end values for the X axis scaling
//                startScale, endScale, // Start and end values for the Y axis scaling
//                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
//                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
//        anim.setFillAfter(true); // Needed to keep the result of the animation
//        anim.setDuration(1000);
//        v.startAnimation(anim);
//    }



    // ALPHA ANIMATION
//    private void setAlphaAnimation(View v) {
//        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(v, "alpha", 1f, .3f);
//        fadeOut.setDuration(1000);
//        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(v, "alpha", .3f, 1f);
//        fadeIn.setDuration(1000);
//
//        final AnimatorSet mAnimationSet = new AnimatorSet();
//
//        mAnimationSet.play(fadeIn).after(fadeOut);
//
//        mAnimationSet.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                mAnimationSet.start();
//            }
//        });
//        mAnimationSet.start();
//    }
}

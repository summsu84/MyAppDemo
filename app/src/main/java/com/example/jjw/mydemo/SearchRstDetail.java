package com.example.jjw.mydemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

public class SearchRstDetail extends AppCompatActivity {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private ViewFlipper mViewFlipper;
    private Animation.AnimationListener mAnimationListener;
    private Context mContext;

   // private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());     //더 좋은게 나왔으니 사용하지 말라는 것..
   private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_rst_detail);

        mContext = this;
        //ViewFlipper 참고
        mViewFlipper = (ViewFlipper)this.findViewById(R.id.viewFlipper);
        mViewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });

/*
        Animation showIn = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        mViewFlipper.setAnimation(showIn);

        mViewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
*/

//animation listener
        mAnimationListener = new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                //animation started event
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                //TODO animation stopped event
            }
        };



    }


    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    System.out.println("-----------------GestureDector!1-------------");
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right));
                    // controlling animation
                    mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
                    mViewFlipper.showNext();
                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    System.out.println("-----------------GestureDector!2-------------");
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right));
                    // controlling animation
                    mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
                    mViewFlipper.showPrevious();
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}

package com.example.mentalhealf;


import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
public class UiAnimations {
        public static void fadeInAnimation(View view) {
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            fadeIn.setDuration(500);
            fadeIn.start();
        }

        public static void slideUpAnimation(View view) {
            ObjectAnimator slideUp = ObjectAnimator.ofFloat(view, "translationY", view.getHeight(), 0);
            slideUp.setDuration(500);
            slideUp.start();
        }

        public static void slideRightAnimation(View view) {
            ObjectAnimator slideRight = ObjectAnimator.ofFloat(view, "translationX", 0, 300);
            slideRight.setDuration(500);
            slideRight.start();
        }
    public static void increaseTextSize(View view, int duration) {
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 2.2f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 2.2f);
        scaleUpX.setDuration(duration);
        scaleUpY.setDuration(duration);

        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.playTogether(scaleUpX, scaleUpY);
        scaleUp.start();
    }

    public static void decreaseTextSize(View view, int duration) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 2.2f, 1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 2.2f, 1f);
        scaleDownX.setDuration(duration);
        scaleDownY.setDuration(duration);

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.playTogether(scaleDownX, scaleDownY);
        scaleDown.start();
    }

}

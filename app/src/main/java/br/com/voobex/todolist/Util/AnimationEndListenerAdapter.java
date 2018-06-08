package br.com.voobex.todolist.Util;

import android.animation.Animator;
import android.support.annotation.NonNull;

/**
 * Created by SnowFlake on 09.02.2016.
 */
public class AnimationEndListenerAdapter implements Animator.AnimatorListener {
    private br.com.voobex.todolist.Util.AnimationEndListener mAdapted;

    public AnimationEndListenerAdapter(@NonNull br.com.voobex.todolist.Util.AnimationEndListener adapted) {
        this.mAdapted = adapted;
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mAdapted.onAnimationEnd(animation);
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}

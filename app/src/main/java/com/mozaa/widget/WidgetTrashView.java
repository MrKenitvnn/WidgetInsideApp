package com.mozaa.widget;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import com.mozaa.demobubbleinsideapp.R;

/**
 * Created by HN on 11/04/2017.
 */

public class WidgetTrashView extends WidgetBaseLayout{
    private static final String TAG = "WidgetTrashView";

    private boolean magnetismApplied = false;
    private boolean attachedToWindow = false;

    public WidgetTrashView(Context context) {
        super(context);
    }

    public WidgetTrashView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WidgetTrashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        attachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        attachedToWindow = false;
    }

    @Override
    public void setVisibility(int visibility) {
        if (attachedToWindow) {
            if (visibility != getVisibility()) {
                if (visibility == VISIBLE) {
                    playAnimation(R.anim.widget_trash_shown_animator);
                    Log.e(TAG, "trash x= " + getX() + ", y= " + getY());

                } else {
                    Log.e(TAG, "trash x= " + getX() + ", y= " + getY());
                    playAnimation(R.anim.widget_trash_hide_animator);
                }
            }
        }
        super.setVisibility(visibility);
    }

    void applyMagnetism() {
        if (!magnetismApplied) {
            magnetismApplied = true;
            playAnimation(R.anim.widget_trash_shown_magnetism_animator);
        }
    }

    void releaseMagnetism() {
        if (magnetismApplied) {
            magnetismApplied = false;
            playAnimation(R.anim.widget_trash_hide_magnetism_animator);
        }
    }

    private void playAnimation(int animationResourceId) {
        if (!isInEditMode()) {
            AnimatorSet animator = (AnimatorSet) AnimatorInflater
                    .loadAnimator(getContext(), animationResourceId);
            animator.setTarget(getChildAt(0));
            animator.start();
        }
    }

}

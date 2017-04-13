package com.mozaa.widget;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import mozaa.demobubbleinsideapp.R;

/**
 * Created by HN on 10/04/2017.
 */

public class WidgetView extends WidgetBaseLayout {

    private final String TAG = WidgetView.class.getName();
    private static final int TOUCH_TIME_THRESHOLD = 100;
    private final float ALPHA_DIM = 0.7F;
    private final int ALPHA_GRAVITY = 50;
    private final int DURATION_DIM = 400;

    private float initialTouchX;
    private float initialTouchY;
    private int initialX;
    private int initialY;
    private long lastTouchDown;
    private MoveAnimator animator;
    private int width;
    private boolean shouldStickToWall = true;
    private OnWidgetClickListener onWidgetClickListener;


    public WidgetView(Context context) {
        super(context);
        animator = new MoveAnimator();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        setClickable(true);
    }

    public WidgetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        animator = new MoveAnimator();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        setClickable(true);
    }

    public WidgetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        animator = new MoveAnimator();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        setClickable(true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        playAnimation();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = getViewParams().x;
                    initialY = getViewParams().y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    playAnimationClickDown();
                    lastTouchDown = System.currentTimeMillis();
                    updateSize();
                    animator.stop();
                    dimWidgetButton(ALPHA_DIM, 1, DURATION_DIM);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int x = initialX + (int)(event.getRawX() - initialTouchX);
                    int y = initialY + (int)(event.getRawY() - initialTouchY);
                    getViewParams().x = x;
                    getViewParams().y = y;
                    getWindowManager().updateViewLayout(this, getViewParams());
                    if (getLayoutCoordinator() != null) {
                        getLayoutCoordinator().notifyBubblePositionChanged(this, x, y);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    goToWall();
                    if (getLayoutCoordinator() != null) {
                        getLayoutCoordinator().notifyBubbleRelease(this);
                        playAnimationClickUp();
                    }
                    if (System.currentTimeMillis() - lastTouchDown < TOUCH_TIME_THRESHOLD) {
                        if (onWidgetClickListener != null) {
                            onWidgetClickListener.onWidgetClickListener(this);
                        }
                        Log.w(TAG,"on widget click on action up");
                    }
                    dimWidgetButton(1, ALPHA_DIM, DURATION_DIM);
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    public void setOnWidgetClickListener (OnWidgetClickListener listener) {
        this.onWidgetClickListener = listener;
    }

    private void playAnimation() {
        if (!isInEditMode()) {
            AnimatorSet animator = (AnimatorSet) AnimatorInflater
                    .loadAnimator(getContext(), R.anim.widget_shown_animator);
            animator.setTarget(this);
            animator.start();
        }
    }

    private void playAnimationClickDown() {
        if (!isInEditMode()) {
            AnimatorSet animator = (AnimatorSet) AnimatorInflater
                    .loadAnimator(getContext(), R.anim.widget_down_click_animator);
            animator.setTarget(this);
            animator.start();
        }
    }

    private void playAnimationClickUp() {
        if (!isInEditMode()) {
            AnimatorSet animator = (AnimatorSet) AnimatorInflater
                    .loadAnimator(getContext(), R.anim.widget_up_click_animator);
            animator.setTarget(this);
            animator.start();
        }
    }

    private void updateSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = (size.x - this.getWidth());
    }

    public void goToWall() {
        if(shouldStickToWall){
            int middle = width / 2;
            float nearestXWall = getViewParams().x >= middle ? width : 0;
            animator.start(nearestXWall, getViewParams().y);
        }
    }

    private void move(float deltaX, float deltaY) {
        getViewParams().x += deltaX;
        getViewParams().y += deltaY;
        windowManager.updateViewLayout(this, getViewParams());
    }

    private final void dimWidgetButton(float alphaBegin, float alphaEnd, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[] { alphaBegin, alphaEnd });
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                setAlpha(Float.parseFloat(animation.getAnimatedValue().toString()));
            }
        });
        valueAnimator.start();
    }

    private class MoveAnimator implements Runnable {
        private Handler handler = new Handler(Looper.getMainLooper());
        private float destinationX;
        private float destinationY;
        private long startingTime;

        private void start(float x, float y) {
            this.destinationX = x;
            this.destinationY = y;
            startingTime = System.currentTimeMillis();
            handler.post(this);
        }

        @Override
        public void run() {
            if (getRootView() != null && getRootView().getParent() != null) {
                float progress = Math.min(1, (System.currentTimeMillis() - startingTime) / 400f);
                float deltaX = (destinationX -  getViewParams().x) * progress;
                float deltaY = (destinationY -  getViewParams().y) * progress;
                move(deltaX, deltaY);
                if (progress < 1) {
                    handler.post(this);
                }
            }
        }

        private void stop() {
            handler.removeCallbacks(this);
        }
    }

}

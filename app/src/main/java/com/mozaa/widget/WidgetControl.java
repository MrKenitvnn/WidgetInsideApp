package com.mozaa.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by HN on 10/04/2017.
 */

public class WidgetControl {

    private static WidgetControl INSTANCE;
    private Activity mContext;
    private WindowManager mWindowManager;
    private WidgetView widgetView;
    private int widgetTrashResourceId;
    private WidgetTrashView widgetTrashView;
    private WidgetLayoutCoordinator widgetLayoutCoordinator;
    private boolean isWidgetShowing = false;


    private WidgetControl () {}

    private WidgetControl (Activity activity) {
        mContext = activity;
    }

    public static WidgetControl getInstance (Activity activity) {
        if (null == INSTANCE) {
            INSTANCE = new WidgetControl(activity);
        }
        return INSTANCE;
    }

    public void setOnWidgetClickListener (OnWidgetClickListener listener) {
        widgetView.setOnWidgetClickListener(listener);
    }

    public void setOnWidgetRemoveListener (OnWidgetRemoveListener listener) {
        widgetLayoutCoordinator.setOnWidgetRemoveListener(listener);
    }

    public void setWidgetView (int resourceId) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        widgetView = (WidgetView) inflater.inflate(resourceId, null);
    }

    public void setWidgetTrashView (int resourceId) {
        widgetTrashResourceId = resourceId;
        addTrash();
        initializeLayoutCoordinator();
    }

    public Activity getContext () {
        return mContext;
    }

    public void showWidget () {
        if (widgetView.getWindowToken() != null && isWidgetShowing) {
            return;
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;

        WindowManager.LayoutParams params = buildLayoutParamsForBubble(0,screenHeight/2);
        widgetView.setViewParams(params);
        widgetView.setLayoutCoordinator(widgetLayoutCoordinator);
        getWindowManager().addView(this.widgetView, params);

        isWidgetShowing = true;
    }

    public void removeWidget () {
        if (isWidgetShowing && widgetView.getWindowToken() != null) {
            getWindowManager().removeView(widgetView);
            isWidgetShowing = false;
        }
    }

    private WindowManager getWindowManager() {
        if (null == mWindowManager) {
            synchronized (this) {
                if (this.mWindowManager == null) {
                    this.mWindowManager = ((WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE));
                }
            }
        }
        return mWindowManager;
    }


    private WindowManager.LayoutParams buildLayoutParamsForBubble(int x, int y) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = x;
        params.y = y;
        return params;
    }


    private WindowManager.LayoutParams buildLayoutParamsForTrash() {

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

        return params;
    }

    void addTrash () {
        widgetTrashView = new WidgetTrashView(mContext);
        widgetTrashView.setWindowManager(getWindowManager());
        widgetTrashView.setViewParams(buildLayoutParamsForTrash());
        widgetTrashView.setVisibility(View.GONE);
        LayoutInflater.from(mContext).inflate(widgetTrashResourceId, widgetTrashView, true);
        getWindowManager().addView(this.widgetTrashView, buildLayoutParamsForTrash());

    }

    void initializeLayoutCoordinator () {
        widgetLayoutCoordinator = new WidgetLayoutCoordinator.Builder(this)
                .setWindowManager(getWindowManager())
                .setTrashView(widgetTrashView)
                .build();
    }

}

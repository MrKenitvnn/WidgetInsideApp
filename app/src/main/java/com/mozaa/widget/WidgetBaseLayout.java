package com.mozaa.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.FrameLayout;


/**
 * Created by HN on 11/04/2017.
 */

public class WidgetBaseLayout extends FrameLayout{

    protected WindowManager windowManager;
    protected WindowManager.LayoutParams params;
    private WidgetLayoutCoordinator layoutCoordinator;

    void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    WindowManager getWindowManager() {
        return this.windowManager;
    }

    void setViewParams(WindowManager.LayoutParams params) {
        this.params = params;
    }

    WindowManager.LayoutParams getViewParams() {
        return this.params;
    }

    void setLayoutCoordinator(WidgetLayoutCoordinator layoutCoordinator) {
        this.layoutCoordinator = layoutCoordinator;
    }

    WidgetLayoutCoordinator getLayoutCoordinator() {
        return layoutCoordinator;
    }



    public WidgetBaseLayout(Context context) {
        super(context);
    }

    public WidgetBaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WidgetBaseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}

package com.mozaa.widget;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by HN on 11/04/2017.
 */

public class WidgetLayoutCoordinator {

    private static final String TAG = "WidgetLayoutCoordinator";

    private static WidgetLayoutCoordinator INSTANCE;
    private WidgetTrashView trashView;
    private WindowManager windowManager;
    private WidgetControl widgetControl;
    private OnWidgetRemoveListener onWidgetRemoveListener;

    private WidgetLayoutCoordinator() { }

    private static WidgetLayoutCoordinator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WidgetLayoutCoordinator();
        }
        return INSTANCE;
    }

    public void setOnWidgetRemoveListener (OnWidgetRemoveListener listener) {
        this.onWidgetRemoveListener = listener;
    }

    public void notifyBubblePositionChanged(WidgetView bubble, int x, int y) {
        if (trashView != null) {
            trashView.setVisibility(View.VISIBLE);
            if (checkIfBubbleIsOverTrash(bubble)) {
                trashView.applyMagnetism();
                applyTrashMagnetismToBubble(bubble);
            } else {
                trashView.releaseMagnetism();
            }
        }
    }

    private void applyTrashMagnetismToBubble(WidgetView bubble) {

        View trashContentView = getTrashContent();
        int trashCenterX = (trashContentView.getLeft() + (trashContentView.getMeasuredWidth() / 2));
        int x = (trashCenterX - (bubble.getMeasuredWidth() / 2));

        int[] loc = new int[2];
        trashView.getLocationOnScreen(loc);
        int trashCenterY = loc[1] + trashView.getHeight()/2;
        int y = (trashCenterY  - bubble.getMeasuredHeight()/2) - getStatusBarHeight();

        boolean isFullScreen = (widgetControl.getContext().getWindow().getAttributes().flags
                & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
        if (isFullScreen) {
            y = (trashCenterY  - bubble.getMeasuredHeight()/2);
        }

        bubble.getViewParams().x = x;
        bubble.getViewParams().y = y;
        windowManager.updateViewLayout(bubble, bubble.getViewParams());
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = widgetControl.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = widgetControl.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private boolean checkIfBubbleIsOverTrash(WidgetView bubble) {
        boolean result = false;
        if (trashView.getVisibility() == View.VISIBLE) {
            View trashContentView = getTrashContent();
            int trashWidth = trashContentView.getMeasuredWidth();
            int trashLeft = (trashContentView.getLeft() - (trashWidth / 2));
            int trashRight = (trashContentView.getLeft() + trashWidth + (trashWidth / 2));
            int bubbleWidth = bubble.getMeasuredWidth();
            int bubbleHeight = bubble.getMeasuredHeight();
            int bubbleLeft = bubble.getViewParams().x;
            int bubbleRight = bubbleLeft + bubbleWidth;
            int bubbleTop = bubble.getViewParams().y;
            int bubbleBottom = bubbleTop + bubbleHeight;

            int[] loc = new int[2];
            trashContentView.getLocationOnScreen(loc);
            int trashTop = loc[1] - getStatusBarHeight() - bubble.getMeasuredHeight()/2;

            boolean isFullScreen = (widgetControl.getContext().getWindow().getAttributes().flags
                    & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
            if (isFullScreen) {
                trashTop = loc[1] - bubble.getMeasuredHeight()/2;
            }
            Log.e(TAG, " trashTop: " + trashTop + ", bubble Top: " + bubbleTop);

            if (bubbleLeft >= trashLeft && bubbleRight <= trashRight) {
                if (bubbleTop >= trashTop) {
                    result = true;
                }
            }
        }
        return result;
    }


    public void notifyBubbleRelease(WidgetView bubble) {
        if (trashView != null) {
            if (checkIfBubbleIsOverTrash(bubble)) {
                widgetControl.removeWidget();
                if (onWidgetRemoveListener != null) {
                    onWidgetRemoveListener.onWidgetRemoveListener();
                }
            }
            trashView.setVisibility(View.GONE);
        }
    }

    public static class Builder {
        private WidgetLayoutCoordinator layoutCoordinator;

        public Builder(WidgetControl service) {
            layoutCoordinator = getInstance();
            layoutCoordinator.widgetControl = service;
        }

        public WidgetLayoutCoordinator.Builder setTrashView(WidgetTrashView trashView) {
            layoutCoordinator.trashView = trashView;
            return this;
        }

        public WidgetLayoutCoordinator.Builder setWindowManager(WindowManager windowManager) {
            layoutCoordinator.windowManager = windowManager;
            return this;
        }

        public WidgetLayoutCoordinator build() {
            return layoutCoordinator;
        }
    }

    private View getTrashContent() {
        return trashView.getChildAt(0);
    }

}

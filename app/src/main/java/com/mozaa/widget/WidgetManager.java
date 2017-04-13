package com.mozaa.widget;

import android.app.Activity;

/**
 * Created by HN on 11/04/2017.
 */

public class WidgetManager {

    private static WidgetManager INSTANCE;
    private Activity mActivity;
    private int mWidgetLayoutResourceId;
    private int mTrashLayoutResourceId;
    private WidgetControl widgetControl;

    private WidgetManager(Activity activity) {
        this.mActivity = activity;
    }

    private static WidgetManager getInstance(Activity activity) {
        if (INSTANCE == null) {
            INSTANCE = new WidgetManager(activity);
        }
        return INSTANCE;
    }

    public static class Builder {
        private WidgetManager widgetManager;

        public Builder(Activity activity) {
            this.widgetManager = WidgetManager.getInstance(activity);
        }

        public WidgetManager.Builder setWidgetLayout (int widgetLayout) {
            widgetManager.mWidgetLayoutResourceId = widgetLayout;
            return this;
        }

        public WidgetManager.Builder setTrashLayout (int trashLayoutResourceId) {
            widgetManager.mTrashLayoutResourceId = trashLayoutResourceId;
            return this;
        }

        public WidgetManager build() {
            return widgetManager;
        }
    }

    public void initialize () {
        widgetControl = WidgetControl.getInstance(mActivity);
        widgetControl.setWidgetView(mWidgetLayoutResourceId);
        widgetControl.setWidgetTrashView(mTrashLayoutResourceId);
    }

    public void setOnWidgetClickListener (OnWidgetClickListener listener) {
        widgetControl.setOnWidgetClickListener(listener);
    }

    public void setOnWidgetRemoveListener (OnWidgetRemoveListener listener) {
        widgetControl.setOnWidgetRemoveListener(listener);
    }

    public void showWidget () {
        widgetControl.showWidget();
    }

    public void hideWidget () {
        widgetControl.removeWidget();
    }


}

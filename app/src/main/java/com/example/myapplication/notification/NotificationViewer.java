package com.example.myapplication.notification;

import android.app.Activity;
import android.support.v4.view.ViewCompat;
import android.view.ViewGroup;

import com.example.myapplication.R;

import java.lang.ref.WeakReference;

public class NotificationViewer {


    private WeakReference<Activity> activityWeakReference;
    private NotificationView notificationView;

    public NotificationView show() {

        Activity activity = activityWeakReference.get();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ViewGroup viewGroup = activityDecorView();
                    if (viewGroup != null) {
                        viewGroup.addView(notificationView);
                    }
                }
            });
        }
        return notificationView;
    }


    private ViewGroup activityDecorView() {
        ViewGroup decorView = null;
        Activity activity = activityWeakReference.get();
        if (activity != null) {
            decorView = (ViewGroup) activity.getWindow().getDecorView();
        }

        return decorView;
    }

    private void setActivity(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }


    public static NotificationViewer create(Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("activity 不能为空");
        }

        NotificationViewer notificationViewer = new NotificationViewer();
        notificationViewer.clearCurrent(activity);
        notificationViewer.setActivity(activity);
        notificationViewer.notificationView = new NotificationView(activity);
        return notificationViewer;
    }


    public void clearCurrent(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        for (int i = 0; i < decorView.getChildCount(); i++) {
            NotificationView childView;
            if (decorView.getChildAt(i) instanceof NotificationView) {
                childView = (NotificationView) decorView.getChildAt(i);
            } else {
                childView = null;
            }

            if (childView != null && childView.getWindowToken() != null) {
                ViewCompat.animate(childView).alpha(0f).withEndAction(getRemoveViewRunnable(childView));
            }
        }
    }

    public void hide() {
        Activity activity = activityWeakReference.get();
        if (activity != null) {
            clearCurrent(activity);
        }
    }

    public boolean isShowing() {
        boolean isShowing = false;
        Activity activity = activityWeakReference.get();
        if (activity != null) {
            isShowing = activity.findViewById(R.id.llAlertBackground) != null;
        }
        return isShowing;
    }


    private Runnable getRemoveViewRunnable(final NotificationView childView) {
        return new Runnable() {
            @Override
            public void run() {
                ((ViewGroup) childView.getParent()).removeView(childView);
            }
        };
    }


    public NotificationViewer setTitle(String title) {
        if (notificationView != null) {
            notificationView.setTitle(title);
        }
        return this;
    }

    public NotificationViewer setText(String text) {
        if (notificationView != null) {
            notificationView.setText(text);
        }
        return this;
    }

    public NotificationViewer setOnNotifyClickListener(OnNotifyClickListener onNotifyClickListener) {
        if (notificationView != null) {
            notificationView.setOnNotifyClickListener(onNotifyClickListener);
        }
        return this;
    }

    public NotificationViewer setDuration(long milliseconds) {
        if (notificationView != null) {
            notificationView.setDuration(milliseconds);
        }
        return this;
    }


}

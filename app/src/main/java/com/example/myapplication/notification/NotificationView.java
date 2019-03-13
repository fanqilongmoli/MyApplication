package com.example.myapplication.notification;

import android.content.Context;
import android.os.IInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.myapplication.R;

public class NotificationView extends FrameLayout implements Animation.AnimationListener, View.OnClickListener {

    private OnHideAlertListener onShowListener;
    private OnShowAlertListener onHideListener;
    /**
     * 回收时间
     */
    private static int CLEAN_UP_DELAY_MILLIS = 100;
    /**
     * 显示时间
     */
    private static int DISPLAY_TIME_IN_SECONDS = 3000;

    private Animation enterAnimation;
    private Animation exitAnimation;
    private Context context;
    private Runnable runningAnimation;
    private boolean isDismissable = true;
    private boolean vibrationEnabled = true;

    private LinearLayout llAlertBackground;

    public NotificationView(@NonNull Context context) {
        super(context, null);
    }

    public NotificationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public NotificationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        initView();
    }

    private void initView() {


        enterAnimation = AnimationUtils.loadAnimation(context, R.anim.alerter_slide_in_from_top);
        exitAnimation = AnimationUtils.loadAnimation(context, R.anim.alerter_slide_out_to_top);

        LayoutInflater.from(context).inflate(R.layout.layout_notification_view, this);
        llAlertBackground = findViewById(R.id.llAlertBackground);

        setHapticFeedbackEnabled(true);
        ViewCompat.setTranslationZ(this, Integer.MAX_VALUE);

        llAlertBackground.setOnClickListener(this);

    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        enterAnimation.setAnimationListener(this);
        setAnimation(enterAnimation);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        enterAnimation.setAnimationListener(null);
    }


    @Override
    public void setOnClickListener(@Nullable View.OnClickListener l) {
        llAlertBackground.setOnClickListener(l);
    }

    //---------------
    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
    //----------------------

    @Override
    public void onClick(View v) {
        if (isDismissable) {
            hide();
        }
    }

    /**
     * 关闭当前的 提示框
     */
    private void hide() {
        exitAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                llAlertBackground.setOnClickListener(null);
                llAlertBackground.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                removeFromParent();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(enterAnimation);
    }

    /**
     * 清除当前的 提示框
     */
    private void removeFromParent() {
        clearAnimation();
        setVisibility(View.GONE);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getParent() != null) {

                }
            }
        }, CLEAN_UP_DELAY_MILLIS);
    }
}

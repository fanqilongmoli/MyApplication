package com.example.myapplication.notification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.IInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;

public class NotificationView extends FrameLayout implements Animation.AnimationListener, View.OnClickListener {

    public static final String TAG = NotificationView.class.getSimpleName();

    /**
     * 回收时间
     */
    private static int CLEAN_UP_DELAY_MILLIS = 100;
    /**
     * 显示时间
     */
    private static int DISPLAY_TIME_IN_SECONDS = 3000;


    private OnNotifyClickListener onNotifyClickListener;

    private boolean marginSet;

    private Animation enterAnimation;
    private Animation exitAnimation;
    private Context context;
    private Runnable runningAnimation;
    private boolean vibrationEnabled = true;
    private long duration = DISPLAY_TIME_IN_SECONDS;
    private LinearLayout llAlertBackground;
    private TextView tv_title;
    private TextView tv_text;


    public void setOnNotifyClickListener(OnNotifyClickListener onNotifyClickListener) {
        this.onNotifyClickListener = onNotifyClickListener;
    }

    public NotificationView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public NotificationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NotificationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;

        enterAnimation = AnimationUtils.loadAnimation(context, R.anim.alerter_slide_in_from_top);
        exitAnimation = AnimationUtils.loadAnimation(context, R.anim.alerter_slide_out_to_top);

        LayoutInflater.from(context).inflate(R.layout.layout_notification_view, this);
        llAlertBackground = findViewById(R.id.llAlertBackground);
        tv_title = ((TextView) findViewById(R.id.tv_title));
        tv_text = ((TextView) findViewById(R.id.tv_text));

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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!marginSet) {
            marginSet = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                DisplayCutout displayCutout = ((Activity) context).getWindow().getDecorView().getRootWindowInsets().getDisplayCutout();
                int notchHeight = 0;
                if (displayCutout != null) {
                    notchHeight = displayCutout.getSafeInsetTop();
                }
                llAlertBackground.setPadding(getPaddingLeft(), getPaddingTop() + notchHeight / 2, getPaddingRight(), getPaddingBottom());
            }

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        enterAnimation.setAnimationListener(null);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.performClick();
        return super.onTouchEvent(event);

    }

    @Override
    public void setOnClickListener(@Nullable View.OnClickListener l) {
        llAlertBackground.setOnClickListener(l);

    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(VISIBLE);
        }
    }

    //---------------
    @Override
    public void onAnimationStart(Animation animation) {
        if (!isInEditMode()) {
            setVisibility(VISIBLE);
            if (vibrationEnabled) {
                performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        startHideAnimation();
    }


    @Override
    public void onAnimationRepeat(Animation animation) {

    }
    //----------------------

    private void startHideAnimation() {

        runningAnimation = new Runnable() {
            @Override
            public void run() {
                hide();
            }
        };
        postDelayed(runningAnimation, duration);
    }


    @Override
    public void onClick(View v) {
        hide();
        if (onNotifyClickListener != null) {
            onNotifyClickListener.onClick();
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
        startAnimation(exitAnimation);
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
                    try {
                        ((ViewGroup) getParent()).removeView(NotificationView.this);
                    } catch (Exception e) {
                        Log.e(TAG, "Cannot remove from parent layout");
                    }
                }
            }
        }, CLEAN_UP_DELAY_MILLIS);
    }

    /**
     * 设置title
     *
     * @param title
     */
    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
            tv_title.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置内容
     *
     * @param text
     */
    public void setText(String text) {
        if (!TextUtils.isEmpty(text)) {
            tv_text.setText(text);
            tv_text.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置显示时间
     *
     * @param duration
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }


}

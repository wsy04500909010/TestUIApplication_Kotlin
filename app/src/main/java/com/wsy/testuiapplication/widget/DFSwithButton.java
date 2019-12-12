package com.wsy.testuiapplication.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.wsy.testuiapplication.DFRoundTextView;
import com.wsy.testuiapplication.R;
import com.wsy.testuiapplication.util.DensityUtil;

public class DFSwithButton extends LinearLayout {
    private GradientDrawable mBtnDrawable = new GradientDrawable();
    private ValueAnimator va;
    private int mPosition = 0;
    private DFRoundTextView tv1;
    private int mColor;
    private float mInnerPadding;
    private float mTextSize;
    private String mTextLeft;
    private String mTextRight;
    private boolean enable = true;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    private float mTextWidth;

    public DFSwithButton(Context context) {
        super(context);
        setWillNotDraw(false);
        initView();
    }

    public DFSwithButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        obtainAttributes(context, attrs);
        initView();
    }

    public DFSwithButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        obtainAttributes(context, attrs);
        initView();
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DFSwithButton);
        mColor = ta.getColor(R.styleable.DFSwithButton_df_switch_button_color, getResources()
                .getColor(R.color.colorPrimary));
        mInnerPadding = ta.getDimension(R.styleable.DFSwithButton_df_switch_button_innerPadding,
                20);
        mTextSize = ta.getDimension(R.styleable.DFSwithButton_df_switch_button_textSize, 14);
        mTextLeft = ta.getString(R.styleable.DFSwithButton_df_switch_button_textLeft);
        mTextRight = ta.getString(R.styleable.DFSwithButton_df_switch_button_textRight);
        enable = ta.getBoolean(R.styleable.DFSwithButton_df_switch_button_enable, true);
        mTextWidth = ta.getDimension(R.styleable.DFSwithButton_df_switch_button_textwidth, 52.5f);
        ta.recycle();
    }

    private void initView() {
        tv1 = new DFRoundTextView(getContext());
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextColor(Color.WHITE);
        tv1.setDrawColor(mColor);
        tv1.setInnerPadding(DensityUtil.dip2px(getContext(), 3));
        tv1.setTextSize(DensityUtil.px2dip(getContext(),mTextSize));
        tv1.setText(mTextLeft);
        tv1.setFocusable(false);
        LayoutParams lp_tab = new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout
                .LayoutParams.MATCH_PARENT);
        lp_tab.width = (int) mTextWidth;
//        lp_tab.width = DensityUtil.dip2px(getContext(), 52.5f);
//        tv1.post(new Runnable() {
//            @Override
//            public void run() {
//                LinearLayout.LayoutParams params = (LayoutParams) tv1.getLayoutParams();
//                if (getMeasuredWidth() > 0) {
//                    params.width = getMeasuredWidth() * 3 / 4;
//                }
//                tv1.setLayoutParams(params);
//            }
//        });
        addView(tv1, lp_tab);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBtnDrawable.setColor(Color.WHITE);
        mBtnDrawable.setCornerRadius(getHeight() / 2);
        mBtnDrawable.setStroke(2, mColor);
        mBtnDrawable.setBounds(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingLeft(),
                getHeight() - getPaddingTop());
        mBtnDrawable.draw(canvas);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && enable) {
            startAnim();
        }
        return true;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * 设置将要切换的上一个状态
     */
    public void setPositionFrom(int positionTo) {
        this.mPosition = positionTo;
        startAnim();
    }

    public void setColor(int color) {
        this.mColor = color;
        tv1.setDrawColor(color);
        invalidate();
    }


    private void startAnim() {
        LayoutParams lp_tab = ((LayoutParams) tv1.getLayoutParams());
        if (mPosition == LEFT) {
            va = ValueAnimator.ofFloat(0f, 1f);
            mPosition = RIGHT;
            if (onSwitchListener != null) {
                onSwitchListener.onSwitch(RIGHT);
            }
            if (lp_tab.leftMargin > 0) {
                return;
            }
        } else {
            va = ValueAnimator.ofFloat(1f, 0f);
            mPosition = LEFT;
            if (onSwitchListener != null) {
                onSwitchListener.onSwitch(LEFT);
            }
            if (lp_tab.leftMargin == 0) {
                return;
            }
        }
        va.setDuration(200);
//        va.setInterpolator(new AccelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();

                LayoutParams lp_tab = ((LayoutParams) tv1.getLayoutParams());
                lp_tab.leftMargin = (int) (value * getWidth() / 4);
                tv1.setLayoutParams(lp_tab);
                if (value > 0.5) {
                    tv1.setText(mTextRight);
                } else {
                    tv1.setText(mTextLeft);
                }
            }
        });
        va.start();
    }

    public interface OnSwitchListener {
        void onSwitch(int position);
    }

    private OnSwitchListener onSwitchListener;

    public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
        this.onSwitchListener = onSwitchListener;
    }
}

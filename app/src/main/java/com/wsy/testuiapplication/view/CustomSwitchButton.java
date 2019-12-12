package com.wsy.testuiapplication.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CustomSwitchButton extends android.support.v7.widget.AppCompatCheckBox {
    private static final String TAG = "SwitchButton";
    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = DEFAULT_WIDTH / 8 * 5;

    private Paint mPaint;
    private RectF mRectF;

    /**
     * 开关指示器按钮圆心 X 坐标的偏移量
     */
    private float mButtonCenterXOffset;
    /**
     * 颜色渐变系数
     */
    private float mColorGradientFactor = 1;
    /**
     * 状态切换时的动画时长
     */
    private long mAnimateDuration = 2000L;
    /**
     * 开关未选中状态,即关闭状态时的背景颜色
     */
    private int mBackgroundColorUnchecked = 0xFFCCCCCC;
    /**
     * 开关选中状态,即打开状态时的背景颜色
     */
    private int mBackgroundColorChecked = 0xFF6495ED;
    /**
     * 开关指示器按钮的颜色
     */
    private int mButtonColor = 0xFFFFFFFF;


    public CustomSwitchButton(Context context) {
        this(context, null);
    }

    public CustomSwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setButtonDrawable(null);
        setBackgroundResource(0);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mRectF = new RectF();


        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                invalidate();

                startAnimate();
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = (getPaddingLeft() + DEFAULT_WIDTH + getPaddingRight());
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (getPaddingTop() + DEFAULT_HEIGHT + getPaddingBottom());
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth((float) getMeasuredWidth() / 40);
//        if (isChecked()) {
//            mPaint.setColor(0xFF6495ED);
//        } else {
//            mPaint.setColor(0xFFCCCCCC);
//        }
        // 根据是否选中的状态设置画笔颜色
        if (isChecked()) {
            // 选中状态时,背景颜色由未选中状态的背景颜色逐渐过渡到选中状态的背景颜色
            mPaint.setColor(getCurrentColor(mColorGradientFactor, mBackgroundColorUnchecked, mBackgroundColorChecked));
        } else {
            // 未选中状态时,背景颜色由选中状态的背景颜色逐渐过渡到未选中状态的背景颜色
            mPaint.setColor(getCurrentColor(mColorGradientFactor, mBackgroundColorChecked, mBackgroundColorUnchecked));
        }
        mRectF.set(mPaint.getStrokeWidth()
                , mPaint.getStrokeWidth()
                , getMeasuredWidth() - mPaint.getStrokeWidth()
                , getMeasuredHeight() - mPaint.getStrokeWidth());
        canvas.drawRoundRect(mRectF, getMeasuredHeight(), getMeasuredHeight(), mPaint);

        mPaint.setColor(0xFFFFFFFF);
        float radius = (getMeasuredHeight() - mPaint.getStrokeWidth() * 4) / 2;
        float x;
        float y;
        if (isChecked()) {
//            x = getMeasuredWidth() - radius - mPaint.getStrokeWidth() - mPaint.getStrokeWidth();
            x = getMeasuredWidth() - radius - mPaint.getStrokeWidth() - mPaint.getStrokeWidth() - mButtonCenterXOffset;
        } else {
//            x = mPaint.getStrokeWidth() + radius + mPaint.getStrokeWidth();
            x = mPaint.getStrokeWidth() + radius + mPaint.getStrokeWidth() + mButtonCenterXOffset;
        }
        y = (float) getMeasuredHeight() / 2;
        canvas.drawCircle(x, y, radius, mPaint);

    }


    /**
     * Author: QinHao
     * Email:qinhao@jeejio.com
     * Date:2019/6/3 9:45
     * Description:开始开关按钮切换状态和背景颜色过渡的动画
     */
    private void startAnimate() {
        // 计算开关指示器的半径
        float radius = (getMeasuredHeight() - mPaint.getStrokeWidth() * 4) / 2;
        // 计算开关指示器的 X 坐标的总偏移量
        float centerXOffset = getMeasuredWidth() - mPaint.getStrokeWidth() - mPaint.getStrokeWidth() - radius
                - (mPaint.getStrokeWidth() + mPaint.getStrokeWidth() + radius);

        AnimatorSet animatorSet = new AnimatorSet();
        // 偏移量逐渐变化到 0
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "buttonCenterXOffset", centerXOffset, 0);
        objectAnimator.setDuration(mAnimateDuration);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });

//        // 背景颜色过渡系数逐渐变化到 1
//        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(this, "colorGradientFactor", 0, 1);
//        objectAnimator2.setDuration(mAnimateDuration);

        // 同时开始修改开关指示器 X 坐标偏移量的动画和修改背景颜色过渡系数的动画
        animatorSet.play(objectAnimator);
//        animatorSet.play(objectAnimator).with(objectAnimator2);
        animatorSet.start();
    }

    /**
     * Author: QinHao
     * Email:qinhao@jeejio.com
     * Date:2019/6/3 9:04
     * Description:获取一个过渡期中当前颜色,fraction 为过渡系数,取值范围 0f-1f,值越接近 1,颜色就越接近 endColor
     *
     * @param fraction   当前渐变系数
     * @param startColor 过渡开始颜色
     * @param endColor   过渡结束颜色
     * @return 当前颜色
     */
    private int getCurrentColor(float fraction, int startColor, int endColor) {
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaStart = Color.alpha(startColor);

        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        int alphaEnd = Color.alpha(endColor);

        int redDifference = redEnd - redStart;
        int blueDifference = blueEnd - blueStart;
        int greenDifference = greenEnd - greenStart;
        int alphaDifference = alphaEnd - alphaStart;

        int redCurrent = (int) (redStart + fraction * redDifference);
        int blueCurrent = (int) (blueStart + fraction * blueDifference);
        int greenCurrent = (int) (greenStart + fraction * greenDifference);
        int alphaCurrent = (int) (alphaStart + fraction * alphaDifference);

        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
    }

    public void setButtonCenterXOffset(float buttonCenterXOffset) {
        mButtonCenterXOffset = buttonCenterXOffset;
    }

    public void setColorGradientFactor(float colorGradientFactor) {
        mColorGradientFactor = colorGradientFactor;
    }

    public void setAnimateDuration(long animateDuration) {
        mAnimateDuration = animateDuration;
    }

    public void setBackgroundColorUnchecked(int backgroundColorUnchecked) {
        mBackgroundColorUnchecked = backgroundColorUnchecked;
    }

    public void setBackgroundColorChecked(int backgroundColorChecked) {
        mBackgroundColorChecked = backgroundColorChecked;
    }

    public void setButtonColor(int buttonColor) {
        mButtonColor = buttonColor;
    }

}

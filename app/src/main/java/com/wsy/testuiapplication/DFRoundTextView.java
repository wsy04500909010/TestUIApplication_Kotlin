package com.wsy.testuiapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

public class DFRoundTextView extends android.support.v7.widget.AppCompatTextView {
    private GradientDrawable mRectDrawable = new GradientDrawable();
    private int innerPadding = 0;
    private int mColor;// 边框颜色
    private int mContentColor;//内容颜色
    private float conner = 0;
    private int stock = 0;

    public DFRoundTextView(Context context) {
        super(context);

    }

    public DFRoundTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        obtainAttributes(context, attrs);

    }

    public DFRoundTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttributes(context, attrs);

    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DFRoundTextView);
        mColor = ta.getColor(R.styleable.DFRoundTextView_df_round_text_color,
                getResources().getColor(R.color.colorPrimary));
        mContentColor = ta.getColor(R.styleable.DFRoundTextView_df_round_text_content_color,
                getResources().getColor(R.color.white));
        conner = ta.getDimension(R.styleable.DFRoundTextView_df_round_text_conner, 20);
        innerPadding = (int) ta.getDimension(R.styleable.DFRoundTextView_df_round_text_innerPadding, 0);
        stock = (int) ta.getDimension(R.styleable.DFRoundTextView_df_round_text_stock, 0);
    }

    public void setDrawColor(int color) {
        this.mColor = color;
        invalidate();
    }

    public void setInnerPadding(int padding) {
        this.innerPadding = padding;
    }

    public void setConner(int conner) {
        this.conner = conner;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mRectDrawable.setCornerRadius(conner == 0 ? getHeight() / 2 : conner);
        if (stock > 0) {
            mRectDrawable.setStroke(stock, mColor);
            mRectDrawable.setColor(mContentColor);
        } else {
            mRectDrawable.setColor(mColor);
        }
        mRectDrawable.setBounds(getPaddingLeft() + innerPadding, getPaddingTop() + innerPadding,
                getWidth() - getPaddingLeft() - innerPadding, getHeight() - getPaddingTop() - innerPadding);
        mRectDrawable.draw(canvas);
        super.onDraw(canvas);
    }
}

package com.numberview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by wujun on 2017/7/26.
 * 自定义进度条
 * @author madreain
 * @desc xml、java双设置，圆角带进度值显示的进度条
 */

public class NumberView extends View {

    //进度条的背景
    Paint backgroundPaint;
    int backgroudColor;
    float defaultStrokeWidth;
    //进度条
    Paint mPaint;
    int mColor;
    //进度值
    Paint textPaint;
    int textColor;
    float textsize;

    float padding;

    float textheight;
    float textwidth;
    int mwidth;
    int mheight;
    private int mMaxProgress;
    private int mCurrentProgress = 0;
    //Progress 1对应的宽度
    private float onewidth;

    public NumberView(Context context) {
        super(context);
        initPaint();
    }

    public NumberView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initTypedArray(context, attrs);
        initPaint();
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberView);
        backgroudColor = typedArray.getColor(R.styleable.NumberView_backgroudColor, Color.BLUE);
        defaultStrokeWidth = typedArray.getFloat(R.styleable.NumberView_defaultStrokeWidth, 16);
        mColor = typedArray.getColor(R.styleable.NumberView_mColor, Color.RED);
        textColor = typedArray.getColor(R.styleable.NumberView_textColor, Color.GREEN);
        textsize = typedArray.getColor(R.styleable.NumberView_textsize, 60);
        padding = typedArray.getFloat(R.styleable.NumberView_padding, 40);
        mMaxProgress=typedArray.getInteger(R.styleable.NumberView_mMaxProgress,100);
    }

    public NumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 初始化Paint
     */
    private void initPaint() {
        //背景设置
        backgroundPaint = new Paint();
        backgroundPaint.setStrokeWidth(defaultStrokeWidth);
        backgroundPaint.setColor(backgroudColor);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);

        //进度条设置
        mPaint = new Paint();
        mPaint.setStrokeWidth(defaultStrokeWidth);
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        //文字进度值设置
        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textsize);
        textPaint.setAntiAlias(true);
        textPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mwidth = w;
        mheight = h;
        //计算一个进度对于的宽度
        onewidth = (mwidth - padding * 2) / mMaxProgress;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //背景线条
        canvas.drawLine(padding, mheight / 2, mwidth - padding, mheight / 2, backgroundPaint);

        //进度条
        if (mCurrentProgress > 0 && mCurrentProgress < mMaxProgress) {
            //结束位置需要加一个padding
            canvas.drawLine(padding, mheight / 2, mCurrentProgress * onewidth + padding, mheight / 2, mPaint);
        } else if (mCurrentProgress == mMaxProgress) {
            //最大进度时是宽度减去一个padding
            canvas.drawLine(padding, mheight / 2, mwidth - padding, mheight / 2, mPaint);
        } else if (mCurrentProgress == 0) {
            canvas.drawLine(padding, mheight / 2, padding, mheight / 2, mPaint);
//            canvas.drawLine(padding, mheight / 2, padding+1, mheight / 2, mPaint);
        }


        //获取文字的宽度及其高度
        Rect rect = new Rect();
        String numtext = mCurrentProgress + "/%";
        textPaint.getTextBounds(numtext, 0, numtext.length(), rect);
        textheight = rect.height();
        textwidth = rect.width();

        //开始位置，设置从x=padding+10开始，距离y轴有个空格，样式好看一些
        if (mCurrentProgress == 0) {
            canvas.drawText(numtext, padding+10, mheight / 2 + textheight / 2, textPaint);
            //达到最大进度条时，为了让进度值能看得见，去算字的宽度，将结束的位置减去这个宽度
        } else {
            //当当前进度对应的宽度+padding+文字的宽度>=宽度-padding  设置
            if (mCurrentProgress * onewidth + padding + textwidth >= mwidth - padding) {
                canvas.drawText(numtext, mwidth - padding - textwidth, mheight / 2 + textheight / 2, textPaint);
            } else {
                canvas.drawText(numtext, mCurrentProgress * onewidth + padding, mheight / 2 + textheight / 2, textPaint);
            }
        }

    }

    /**
     * 设置当前的进度，然后再次调用ondraw()方法，刷新UI
     *
     * @param mCurrentProgress
     */
    public void setCurrentProgress(int mCurrentProgress) {
        this.mCurrentProgress = mCurrentProgress;
        invalidate();
    }

    /**
     * 设置进度条的最大进度，默认时100
     *
     * @param mMaxProgress
     */
    public void setMaxProgress(int mMaxProgress) {
        this.mMaxProgress = mMaxProgress;
    }


    /**
     *
     * @param backgroudColor
     */
    public void setBackgroudColor(int backgroudColor) {
        this.backgroudColor = backgroudColor;
    }

    /**
     *
     * @param defaultStrokeWidth
     */
    public void setDefaultStrokeWidth(float defaultStrokeWidth) {
        this.defaultStrokeWidth = defaultStrokeWidth;
    }

    /**
     *
     * @param mColor
     */
    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    /**
     *
     * @param textColor
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    /***
     *
     * @param textsize
     */
    public void setTextsize(float textsize) {
        this.textsize = textsize;
    }
}

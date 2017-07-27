# NumberView
自定义view————进度条

为了能向高级工程师进阶，还在默默的学习自定义view，今天给大家送上自定义view————进度条，注意哦！！！是带进度值的进度条条

![效果图](/images/numberview.gif)

首先分析效果图，由三部分组成，一个底部的全进度，一个进度变化的进度，一个进度值，接下来就去画

### 各种初始化

要画东西先把paint准备好

```

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

```

初始化好了，需要计算一下一个进度对应的宽度，这个宽度是在当进度值发生变化是去动态计算时要用的

```
@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mwidth = w;
        mheight = h;
        //计算一个进度对于的宽度
        onewidth = (mwidth - padding * 2) / mMaxProgress;
    }
```

### 画进度条

首先先把底部的背景进度条画出来

```
//背景线条
        canvas.drawLine(padding, mheight / 2, mwidth - padding, mheight / 2, backgroundPaint);

```

然后再画进度条，首先分析一下，进度条开始位置、结束位置、开始位置到结束位置

开始位置两种做法：

1）开始位置是否进度条占那么一点点位置

2）开始位置从零开始


结束位置：最大进度时是宽度减去一个padding（这里为了美观，使用来左右两边设置一样的padding）

开始位置到结束位置：当前进度*每一个进度对于的宽度+padding

接下来就要上菜了

```
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

```


最后就是画进度值的显示，首先分析一下，进度值开始位置、临界位置、开始位置到临界位置  （画文字的方法drawText()）

开始位置：开始位置，设置从x=padding+10开始，距离y轴有个空格，样式好看一些

临界位置：当前进度对应的宽度+padding+文字的宽度>=宽度-padding 这是一个临界值，这决定的文字显示的好看，其实就是文字是否会被遮挡

开始位置到临界位置：当前进度对应的宽度+padding

```
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

```

### 进度条画好了，进度变化时调用进度条重绘

实际代码应用中，当进度值变化时，需要重新调用ondraw()

```
  /**
     * 设置当前的进度，然后再次调用ondraw()方法，刷新UI
     *
     * @param mCurrentProgress
     */
    public void setCurrentProgress(int mCurrentProgress) {
        this.mCurrentProgress = mCurrentProgress;
        invalidate();
    }
    
```

### 设置相关属性动态改变

attrs.xml
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="NumberView">
        <attr name="backgroudColor" format="color"/>
        <attr name="defaultStrokeWidth" format="float"/>
        <attr name="mColor" format="color"/>
        <attr name="textColor" format="color"/>
        <attr name="textsize" format="float"/>
        <attr name="padding" format="float"/>
        <attr name="mMaxProgress" format="integer"/>
    </declare-styleable>
</resources>

```


java
```

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

```

到此结束，自定义view————进度条已经写完了

```
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


```

### 代码中使用

```
mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (i <=100) {
                    numberview.setCurrentProgress(i);
                    i += 1;
                    mHandler.sendEmptyMessageDelayed(0, 100);
                }

            }
        };

        numberview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.sendEmptyMessageDelayed(0, 10);
            }
        });
```

[个人博客](https://madreain.github.io/)

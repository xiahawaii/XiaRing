package com.xia.xiaring.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xia.xiaring.R;

/**
 * author xiaweiyao
 * create 16/6/16
 * mail 362613381@qq.com
 */
public class XiaRing extends View {

    private static int RING_INVALID_COLOR_DEFAULT = Color.parseColor("#c8c8c8");    // 底部圆环颜色
    private static int RING_VALID_COLOR_DEFAULT = Color.parseColor("#5dac88");      // 进度圆弧颜色
    private static int TITLE_COLOR_DEFAULT = Color.parseColor("#5dac88");           // 进度文字颜色
    private static int RING_WIDTH_DEFAULT = 10;                                     // 圆环圆弧厚度
    private static int TITLE_SIZE_DEFAULT = 36;                                     // 进度文字大小

    private int mRingInvalidColor;      // 圆环无效百分比颜色；
    private int mRingValidColor;        // 圆环有效百分比颜色；
    private int mTitleColor;            // 百分比文字颜色；
    private float mRingWidth;           // 圆环宽度；
    private float mTitleSize;           // 百分比文字字体大小；

    private Paint mPaint;                   // 画笔对象；
    private boolean isProgress;             // 动画情况；true = 正在执行动画，false = 不在执行动画；
    private float targetProgress = 0;       // 目标的百分比，范围：0 ~ 360；
    private float currentProgress = 0;      // 当前的百分比，范围：0 ~ 360；
    private float stepProgress = 3;         // 百分比改变的步长；步长是逐渐加大；

    public XiaRing(Context context) {
        this(context, null);
    }

    public XiaRing(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XiaRing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.initCustomValue(context, attrs, defStyleAttr, 0);
        mPaint = new Paint();
    }

    // 获取自定义style参数
    private void initCustomValue(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        // 获取控件的属性
        int[] myStyleModel = R.styleable.XiaRing;
        TypedArray mStyleParams = context.getTheme().obtainStyledAttributes(attrs, myStyleModel, defStyleAttr, defStyleRes);
        for (int i = 0; i < mStyleParams.getIndexCount(); i++) {
            int mStyleParam = mStyleParams.getIndex(i);
            switch (mStyleParam) {
                case R.styleable.XiaRing_ringInvalidColor:
                    mRingInvalidColor = mStyleParams.getColor(mStyleParam, RING_INVALID_COLOR_DEFAULT);
                    break;
                case R.styleable.XiaRing_ringValidColor:
                    mRingValidColor = mStyleParams.getColor(mStyleParam, RING_VALID_COLOR_DEFAULT);
                    break;
                case R.styleable.XiaRing_titleColor:
                    mTitleColor = mStyleParams.getColor(mStyleParam, TITLE_COLOR_DEFAULT);
                    break;
                case R.styleable.XiaRing_ringWidth:
                    mRingWidth = mStyleParams.getDimensionPixelSize(mStyleParam, RING_WIDTH_DEFAULT);
                    break;
                case R.styleable.XiaRing_titleSize:
                    mTitleSize = mStyleParams.getDimensionPixelSize(mStyleParam, TITLE_SIZE_DEFAULT);
                    break;
            }
        }
        mStyleParams.recycle();

        // 控件属性若未定义，使用默认值
        if (mRingInvalidColor == 0) {
            mRingInvalidColor = RING_INVALID_COLOR_DEFAULT;
        }
        if (mRingValidColor == 0) {
            mRingValidColor = RING_VALID_COLOR_DEFAULT;
        }
        if (mTitleColor == 0) {
            mTitleColor = TITLE_COLOR_DEFAULT;
        }
        if (mRingWidth == 0) {
            mRingWidth = RING_WIDTH_DEFAULT;
        }
        if (mTitleSize == 0) {
            mTitleSize = TITLE_SIZE_DEFAULT;
        }

        Log.i(getClass().getName(), "=========================================");
        Log.i(getClass().getName(), "mRingInvalidColor:" + mRingInvalidColor);
        Log.i(getClass().getName(), "mRingValidColor:" + mRingValidColor);
        Log.i(getClass().getName(), "mTitleColor:" + mTitleColor);
        Log.i(getClass().getName(), "mRingWidth:" + mRingWidth);
        Log.i(getClass().getName(), "mTitleSize:" + mTitleSize);
        Log.i(getClass().getName(), "=========================================");
    }

    // 控件信息更新线程
    private boolean startUpdateThread() {

        // 若已经开启，无须重新开启
        if (isProgress)
            return false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                isProgress = true;
                while (isProgress && currentProgress != targetProgress) {

                    // 每执行1步，步长加0.2
                    stepProgress = stepProgress + 0.2f;

                    // 改变显示的百分比值 - 减少情况
                    if (currentProgress > targetProgress) {
                        currentProgress = currentProgress - stepProgress;
                        if (currentProgress < targetProgress)
                            currentProgress = targetProgress;
                    }

                    // 改变显示的百分比值 - 增加情况
                    if (currentProgress < targetProgress) {
                        currentProgress = currentProgress + stepProgress;
                        if (currentProgress > targetProgress)
                            currentProgress = targetProgress;
                    }

                    postInvalidate();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                stepProgress = 3;
                isProgress = false;
            }
        }).start();

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 计算控件的X-Y轴中点，使用较短边为圆的半径
        int centreX = getWidth() / 2;
        int centreY = getHeight() / 2;
        int radius;
        if (centreX > centreY) {
            radius = centreY;
        } else {
            radius = centreX;
        }

        // 设置圆环、圆弧的基本属性
        mPaint.setStrokeWidth(mRingWidth);          // 设置圆环宽度
        mPaint.setAntiAlias(true);                  // 消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);        // 设置空心

        // 画底部圆环
        mPaint.setColor(mRingInvalidColor);
        canvas.drawCircle(centreX, centreY, radius - mRingWidth, mPaint);

        // 画进度圆弧
        mPaint.setColor(mRingValidColor);
        RectF oval = new RectF(
                centreX - radius + mRingWidth,
                centreY - radius + mRingWidth,
                centreX + radius - mRingWidth,
                centreY + radius - mRingWidth);
        canvas.drawArc(oval, -90, currentProgress, false, mPaint);

        // 画文字信息
        mPaint.setColor(mTitleColor);
        mPaint.setStrokeWidth(4);
        mPaint.setTextSize(mTitleSize);
        Rect mBound = new Rect();
        String mTitleText = ((int)(currentProgress / 3.6)) + "%";
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
        canvas.drawText(mTitleText, centreX - mBound.width() / 2, centreY + mBound.height() / 2, mPaint);
    }

    // 重新开始
    public void resetProgress(int fromProgress, int toProgress) {

        if (toProgress > 100)
            toProgress = 100;
        if (toProgress < 0)
            toProgress = 0;

        this.currentProgress = fromProgress;
        this.targetProgress = (int) (toProgress * 3.6);
        this.stepProgress = 3;
        startUpdateThread();
    }
}

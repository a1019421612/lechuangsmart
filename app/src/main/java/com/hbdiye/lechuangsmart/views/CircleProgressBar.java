package com.hbdiye.lechuangsmart.views;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.hbdiye.lechuangsmart.R;

public class CircleProgressBar extends View {
    private Context mContext;
    private Paint mPaint;
    private double mProgress = 0;
    private static double MAX_PROGRESS = 100;
    /**
     * 弧度
     */
    private double mAngle;
    /**
     * 中间的文字
     */
    private String mText;
    /**
     * 描述文字
     */
    private String mDescript;

    public String getmDescript() {
        return mDescript;
    }

    public void setmDescript(String mDescript) {
        this.mDescript = mDescript;
    }

    /**
     * 外圆颜色
     */
    private int outRoundColor;
    /**
     * 内圆的颜色
     */
    private int inRoundColor;
    /**
     * 线的宽度
     */
    private int roundWidth;
    private int style;
    /**
     * 字体颜色
     */
    private int textColor;
    /**
     * 字体大小
     */
    private float textSize;
    /**
     * 字体是否加粗
     */
    private boolean isBold;

    /**
     * 进度条颜色
     */
    private int progressBarColor;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    @TargetApi(21)
    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init(attrs);
    }

    /**
     * 解析自定义属性
     *
     * @param attrs
     */
    public void init(AttributeSet attrs) {
        mPaint = new Paint();
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        outRoundColor = typedArray.getColor(R.styleable.CircleProgressBar_outCircleColor, getResources().getColor(R.color.transparent));
        inRoundColor = typedArray.getColor(R.styleable.CircleProgressBar_inCircleColor, getResources().getColor(R.color.colorPrimaryDark));
        progressBarColor = typedArray.getColor(R.styleable.CircleProgressBar_progressColor, getResources().getColor(R.color.colorAccent));
        isBold = typedArray.getBoolean(R.styleable.CircleProgressBar_textBold, false);
        textColor = typedArray.getColor(R.styleable.CircleProgressBar_barTextColor, Color.BLACK);
        roundWidth = typedArray.getDimensionPixelOffset(R.styleable.CircleProgressBar_lineWidth, 20);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /**
         * 画外圆
         */
        super.onDraw(canvas);
        int center = getWidth() / 2;               //圆心
        int radius = (center - roundWidth / 2);    //半径
        mPaint.setColor(outRoundColor);            //外圆颜色
        mPaint.setStrokeWidth(roundWidth);         //线的宽度
        mPaint.setStyle(Paint.Style.STROKE);       //空心圆
        mPaint.setAntiAlias(true);                //消除锯齿
        canvas.drawCircle(center, center, radius, mPaint);
        //内圆
        mPaint.setColor(inRoundColor);
        radius = radius - roundWidth;
        canvas.drawCircle(center, center, radius, mPaint);

        //画进度是一个弧线
        mPaint.setColor(progressBarColor);
        RectF rectF = new RectF(center - radius, center - radius, center + radius, center + radius);//圆弧范围的外接矩形
        canvas.drawArc(rectF, -90, (float) mAngle, false, mPaint);
        canvas.save();  //平移画布之前保存之前画的

        //画进度终点的小球,旋转画布的方式实现
        mPaint.setStyle(Paint.Style.FILL);
        //将画布坐标原点移动至圆心
        canvas.translate(center, center);
        //旋转和进度相同的角度，因为进度是从-90度开始的所以-90度
        canvas.rotate((float) (mAngle - 90));
        //同理从圆心出发直接将原点平移至要画小球的位置
        canvas.translate(radius, 0);
        canvas.drawCircle(0, 0, roundWidth, mPaint);
        //画完之后恢复画布坐标
        canvas.restore();

        //画文字将坐标平移至圆心
//        canvas.translate(center, center);
//        mPaint.setStrokeWidth(0);
//        mPaint.setColor(textColor);
//        if (isBold) {
//            //字体加粗
//            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
//        }
//        if (TextUtils.isEmpty(mText)) {
//            mText = mProgress + "%";
//        }
//        //动态设置文字长为圆半径，计算字体大小
//        float textLength = mText.length();
//        textSize = radius / textLength;
//        mPaint.setTextSize(60);
//        //将文字画到中间
//        float textWidth = mPaint.measureText(mText);
//        canvas.drawText(mText, -textWidth / 2, (textSize / 2)-20, mPaint);
////        canvas.drawText(mText, -textWidth / 2, -textSize, mPaint);


//        String mText_b=mDescript;
//        float textLength_b = mText_b.length();
//        textSize = radius / 3;
//        mPaint.setTextSize(30);
//        //将文字画到中间
//        float textWidth_b = mPaint.measureText(mText_b);
//        canvas.drawText(mText_b, -textWidth_b / 2, textSize, mPaint);
    }


    public double getmProgress() {
        return mProgress;
    }

    /**
     * 设置进度
     *
     */
    public void setmProgress(int p) {
        if (p > MAX_PROGRESS) {
            mProgress = MAX_PROGRESS;
            mAngle = 360;
        } else {
            mProgress = p;
            mAngle = 360 * p / (int) MAX_PROGRESS;
        }
    }


    public String getmText() {
        return mText;
    }

    /**
     * 设置文本
     *
     * @param mText
     */
    public void setmText(String mText) {
        this.mText = mText;
    }

    /**
     * 设置带动画的进度
     * @param p
     */
    public void setAnimProgress(double p) {
        if (p > MAX_PROGRESS) {
            mProgress = MAX_PROGRESS;
        } else {
            mProgress = p;
        }
        //设置属性动画
        ValueAnimator valueAnimator = new ValueAnimator().ofInt(0, (int) p);
        //动画从快到慢
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(3000);
        //监听值的变化
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentV = (Integer) animation.getAnimatedValue();
                mAngle = 360 * currentV / (int) MAX_PROGRESS;
//                Object animatedValue = animation.getAnimatedValue();
//                mText = currentV + "%";
                invalidate();
            }
        });
        valueAnimator.start();
    }
}
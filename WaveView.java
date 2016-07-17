package cn.chenyuantao.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Tao on 2016/7/17.
 */
public class WaveView extends View {

    private int mViewWidth;
    private int mViewHeight;
    private int mWaveHeight;
    private int mFirstProgress;
    private int mSecondProgress;
    private Paint mPaint;
    private Path mPath;
    private int mColor;
    private Boolean canRun;
    private Point[] mStartPoints;
    private Point[] mControlPoints;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                while (canRun) {
                    postInvalidate();
                    mFirstProgress++;
                    mSecondProgress++;
                    if (mFirstProgress == 25) {
                        mSecondProgress = 0;
                    } else if (mFirstProgress == 100) {
                        mFirstProgress = 0;
                    }
                    Thread.sleep(75);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        canRun = true;
        mFirstProgress = 0;
        mSecondProgress = -25;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mColor = Color.rgb(63, 81, 181);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPath = new Path();
        Thread thread = new Thread(mRunnable);
        thread.start();
    }

    private void initMeasure() {
        mWaveHeight = mViewWidth / 30;
        mStartPoints = new Point[4];
        mControlPoints = new Point[4];
        for (int i = 0; i < 4; i++) {
            Point startPoint = new Point();
            Point controlPoint = new Point();
            startPoint.x = -3 * mViewWidth + mViewWidth * i;
            startPoint.y = mViewHeight - mWaveHeight;
            controlPoint.x = -5 * mViewWidth / 2 + mViewWidth * i;
            if (i % 2 == 0) {
                controlPoint.y = startPoint.y + mWaveHeight;
            } else {
                controlPoint.y = startPoint.y - mWaveHeight;
            }
            mStartPoints[i] = startPoint;
            mControlPoints[i] = controlPoint;
            ;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        initMeasure();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 新建图层
        canvas.saveLayer(-mViewWidth, 0, mViewWidth, mViewHeight, null, Canvas.ALL_SAVE_FLAG);
        int increment = 2 * mViewWidth * mFirstProgress / 100;
        mPath.reset();
        for (int i = 0; i < 4; i++) {
            mPath.moveTo(mStartPoints[i].x + increment, mStartPoints[i].y);
            mPath.quadTo(mControlPoints[i].x + increment, mControlPoints[i].y, mStartPoints[i].x + mViewWidth + increment, mStartPoints[i].y);
        }
        canvas.drawPath(mPath, mPaint);
        //设置混合模式 （交叉部分不绘制）
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        canvas.drawRect(-mViewWidth, 0, mViewWidth, mViewHeight - mWaveHeight, mPaint);
        // 还原混合模式
        mPaint.setXfermode(null);
        // 还原图层
        canvas.restore();

        // 新建图层绘制虚波浪线
        canvas.saveLayerAlpha(-mViewWidth, 0, mViewWidth, mViewHeight, 60, Canvas.ALL_SAVE_FLAG);
        increment = 2 * mViewWidth * mSecondProgress / 100;
        mPath.reset();
        for (int i = 0; i < 4; i++) {
            mPath.moveTo(mStartPoints[i].x + increment, mStartPoints[i].y);
            mPath.quadTo(mControlPoints[i].x + increment, mControlPoints[i].y, mStartPoints[i].x + mViewWidth + increment, mStartPoints[i].y);
        }
        canvas.drawPath(mPath, mPaint);
        //设置混合模式 （交叉部分不绘制）
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        canvas.drawRect(-mViewWidth, 0, mViewWidth, mViewHeight - mWaveHeight, mPaint);
        // 还原混合模式
        mPaint.setXfermode(null);
        // 还原图层
        canvas.restore();
    }

    private class Point {
        public int x;
        public int y;
    }

    public void setColor(int color) {
        this.mColor = color;
        mPaint.setColor(mColor);
    }

    public int getColor() {
        return this.mColor;
    }

    public void start() {
        canRun = true;
    }

    public void stop() {
        canRun = false;
    }

}

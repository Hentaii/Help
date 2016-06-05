package com.help.widge;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Toast;

import com.help.R;
import com.help.util.Util;

/**
 * Created by gan on 2016/6/3.
 */
public class CircleButtonWithProgerss extends View {
    private RectF mColorWheelRectangle = new RectF();   //圆圈的矩形范围
    private Paint mDefaultWheelPaint;   // 绘制底部灰色圆圈的画笔
    private Paint mColorWheelPaint; // 绘制蓝色扇形的画笔
    private Paint textPaint;    //中间文字的画笔
    private float mColorWheelRadius;    //圆圈普通状态下的半径
    private float circleStrokeWidth;    // 圆圈的线条粗细

    private float pressExtraStrokeWidth;    //按下状态下增加的圆圈线条增加的粗细

    private String mText;   //中间文字内容
    private float mSweepAnglePer;   // 为了达到蓝色扇形增加效果而添加的变量，他和mSweepAngle其实代表一个意思
    private float mSweepAngle;      // 扇形弧度
    private int mTextSize;   //文字颜色
    private boolean onTouch = false;
    private float time;
    private OnChoseListener choseListener;

    BarAnimation anim;//动画效果


    public CircleButtonWithProgerss(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleButtonWithProgerss(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircleButtonWithProgerss(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public void init(AttributeSet attrs, int defStyle) {
        circleStrokeWidth = Util.dip2px(getContext(), 30);
        pressExtraStrokeWidth = Util.dip2px(getContext(), 2);
        mTextSize = Util.dip2px(getContext(), 15);
        //设置填充颜色的画笔
        mColorWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorWheelPaint.setColor(getResources().getColor(R.color.colorWhite_ffffff));
        mColorWheelPaint.setStyle(Paint.Style.STROKE);//描边
        mColorWheelPaint.setStrokeWidth(circleStrokeWidth);
        //设置蓝色圆圈的画笔
        mDefaultWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultWheelPaint.setColor(getResources().getColor(R.color.colorPrimary_Blue_4EA2F8));
        mDefaultWheelPaint.setStyle(Paint.Style.STROKE);//描边
        mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth);
        //设置中间文字的画笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setColor(getResources().getColor(R.color.colorBlack_000000));
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);//填充内部和描边
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(mTextSize);

        mText = "紧急呼救";
        mSweepAngle = 0;

        anim = new BarAnimation();
        anim.setDuration(2000);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
//      未按下状态的按钮的宽度
        mColorWheelRadius = min - circleStrokeWidth - pressExtraStrokeWidth;
        mColorWheelRectangle.set(circleStrokeWidth + pressExtraStrokeWidth, circleStrokeWidth +
                pressExtraStrokeWidth, mColorWheelRadius, mColorWheelRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画出黑色区域
        canvas.drawArc(mColorWheelRectangle, -90, 360, false, mDefaultWheelPaint);
        //画出绿色区域
        canvas.drawArc(mColorWheelRectangle, -90, mSweepAnglePer, false, mColorWheelPaint);
        Rect bounds = new Rect();
        //获取文字的范围
        textPaint.getTextBounds(mText, 0, mText.length(), bounds);
        canvas.drawText(mText, mColorWheelRectangle.centerX() - textPaint.measureText(mText) / 2,
                mColorWheelRectangle.centerY() + bounds.height() / 2, textPaint);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouch = true;
                startCustomAnimation();
                break;
            case MotionEvent.ACTION_UP:
                if (!onTouch){
                    return true;
                }
                onTouch = false;
                clearAnimation();
                rotateyAnimRun(this);
                postInvalidate();
                break;
        }
        return true;
    }

    public void startCustomAnimation() {
        this.startAnimation(anim);
    }

    public void setText(String text) {
        mText = text;
        this.startAnimation(anim);
    }

    public void setSweepAngle(float sweepAngle) {
        mSweepAngle = sweepAngle;

    }

    private class BarAnimation extends Animation {
        public BarAnimation() {
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            if (interpolatedTime < 1.0f) {
                mSweepAnglePer = interpolatedTime * mSweepAngle;
                time = interpolatedTime;
            }
            if (interpolatedTime == 1.0f) {
                onTouch = false;
                mSweepAnglePer = mSweepAngle;
                if (choseListener != null) {
                    choseListener.onChose();
                } else {
                    Toast.makeText(getContext(), "over", Toast.LENGTH_SHORT).show();
                }
            }
            postInvalidate();
        }
    }

    public void rotateyAnimRun(final View view) {
        ObjectAnimator anim = ObjectAnimator//
                .ofFloat(view, "null", time, 0.0f)
                .setDuration(500);//
        anim.start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSweepAnglePer = (float) animation.getAnimatedValue() * mSweepAngle;
                postInvalidate();
            }
        });
    }

    public void setOnChoseListener(OnChoseListener listener) {
        this.choseListener = listener;
    }

    public interface OnChoseListener {
        void onChose();
    }
}

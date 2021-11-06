package com.waltonbd.smartscale.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.waltonbd.smartscale.R;


public class RulerView extends View {

    private int mMinVelocity;
    private Scroller mScroller; //Scroller is a tool class specially used to deal with scrolling effects. Use mScroller to record/calculate the position of View scrolling, and then rewrite View's computeScroll() to complete the actual scrolling.
    private VelocityTracker mVelocityTracker; //Mainly used to track the rate of touch screen events (flinging events and other gestures gesture events).
    private int mWidth;
    private int mHeight;

    private float mSelectorValue = 50.0f; // The default value when not selected The value after sliding indicates the current middle pointer is pointing
    private float mMaxValue = 200; // maximum value
    private float mMinValue = 100.0f; //The smallest value
    private float mPerValue = 0.1f; //The smallest unit such as 1: indicates that every two scale differences are 1. 0.1: indicates that every two scale differences are 0.1
    // In the demo, the height mPerValue is 1 and the weight mPerValue is 0.1

    private float mLineSpaceWidth = 5; // distance between 2 lines of ruler scale
    private float mLineWidth = 4; // width of ruler scale

    private float mLineMaxHeight = 100; // Ruler scale is divided into 3 different heights. mLineMaxHeight represents the longest root (that is, the height at a multiple of 10)
    private float mLineMidHeight = 30; // mLineMidHeight represents the middle height (that is, 5 15 25 isochronous height)
    private float mLineMinHeight = 17; // mLineMinHeight represents the shortest height (that is, 1 2 3 4 isochronous height)

    private float mTextMarginTop = 10;    //o
    private float mTextSize = 30; //Digital textsize below the ruler scale

    private boolean mAlphaEnable = false; // Does the ruler need to be transparent on the far left side (transparency effect is better)

    private float mTextHeight = 40; //The height of the number below the ruler scale

    private Paint mTextPaint; // The number below the ruler scale (that is, the value that appears every 10th) paint
    private Paint mLinePaint; // ruler scale paint

    private int mTotalLine; //How many scales there are
    private int mMaxOffset; //How long is the total scale
    private float mOffset; // By default, the position of mSelectorValue is at the position of the total scale of the ruler
    private int mLastX, mMove;
    private OnValueChangeListener mListener; // Value callback after sliding

    private int mLineColor = Color.GRAY; //The color of the scale
    private int mTextColor = Color.BLACK; //Text color


    public RulerView(Context context) {
        super(context);
        init(context, null);
    }

    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        mScroller = new Scroller(context);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RulerView);

            mAlphaEnable = typedArray.getBoolean(R.styleable.RulerView_alphaEnable, mAlphaEnable);

            mLineSpaceWidth = typedArray.getDimension(R.styleable.RulerView_lineSpaceWidth, mLineSpaceWidth);
            mLineWidth = typedArray.getDimension(R.styleable.RulerView_lineWidth, mLineWidth);
            mLineMaxHeight = typedArray.getDimension(R.styleable.RulerView_lineMaxHeight, mLineMaxHeight);
            mLineMidHeight = typedArray.getDimension(R.styleable.RulerView_lineMidHeight, mLineMidHeight);
            mLineMinHeight = typedArray.getDimension(R.styleable.RulerView_lineMinHeight, mLineMinHeight);
            mLineColor = typedArray.getColor(R.styleable.RulerView_lineColor, mLineColor);

            mTextSize = typedArray.getDimension(R.styleable.RulerView_textSize, mTextSize);
            mTextColor = typedArray.getColor(R.styleable.RulerView_textColor, mTextColor);
            mTextMarginTop = typedArray.getDimension(R.styleable.RulerView_textMarginTop, mTextMarginTop);

            mSelectorValue = typedArray.getFloat(R.styleable.RulerView_selectorValue, 0.0f);
            mMinValue = typedArray.getFloat(R.styleable.RulerView_minValue, 0.0f);
            mMaxValue = typedArray.getFloat(R.styleable.RulerView_maxValue, 100.0f);
            mPerValue = typedArray.getFloat(R.styleable.RulerView_perValue, 0.1f);

            mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();

            mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.setColor(mTextColor);
            mTextHeight = getFontHeight(mTextPaint);

            mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLinePaint.setStrokeWidth(mLineWidth);
            mLinePaint.setColor(mLineColor);

            setValue(mSelectorValue, mMinValue, mMaxValue, mPerValue);
            typedArray.recycle();
        } else {
            throw new RuntimeException("AttributeSet is null!");
        }
    }

    public static int myfloat(float paramFloat) {
        return (int) (0.5F + paramFloat * 1.0f);
    }

    private float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

    /**
     * @param selectorValue When not selected, the default value is the value that the current middle pointer is pointing after sliding
     * @param minValue      maximum value
     * @param maxValue      The smallest value
     * @param per           The smallest unit such as 1: indicates that the difference between each two scales is 1. 0.1: indicates that the difference between each two scales is 0.1 In the demo, height mPerValue is 1 weight mPerValue is 0.1
     */
    public void setValue(float selectorValue, float minValue, float maxValue, float per) {
        this.mSelectorValue = selectorValue;
        this.mMaxValue = maxValue;
        this.mMinValue = minValue;
        this.mPerValue = (int) (per * 10.0f);
        //this.mPerValue = per;
        this.mTotalLine = ((int) ((mMaxValue * 10 - mMinValue * 10) / mPerValue)) + 1;


        mMaxOffset = (int) (-(mTotalLine - 1) * mLineSpaceWidth);
        mOffset = (mMinValue - mSelectorValue) / mPerValue * mLineSpaceWidth * 10;
        invalidate();
        setVisibility(VISIBLE);
    }

    public void setOnValueChangeListener(OnValueChangeListener listener) {
        mListener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            mWidth = w;
            mHeight = h;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float left, height;
        String value;
        int alpha = 0;
        float scale;
        int srcPointX = mWidth / 2;
        for (int i = 0; i < mTotalLine; i++) {
            left = srcPointX + mOffset + i * mLineSpaceWidth;

            if (left < 0 || left > mWidth) {
                continue; // First draw the default value in the middle, half of the left and right views. The extra part is not drawn temporarily (that is, the default value is in the middle, and the left and right tick marks are drawn)
            }

            /*Text*/
            if (i % 10 == 0) {
                value = String.valueOf((int) (mMinValue + i * mPerValue / 10));
                if (mAlphaEnable) {
                    mTextPaint.setAlpha(alpha);
                }
                canvas.drawText(value, left - mTextPaint.measureText(value) / 2,
                        mTextHeight, mTextPaint); // When it is an integer, draw the value
            }

            /*Line*/
            if (i % 10 == 0) {
                height = mLineMinHeight;
            } else if (i % 5 == 0) {
                height = mLineMidHeight;
            } else {
                height = mLineMaxHeight;
            }
            if (mAlphaEnable) {
                scale = 1 - Math.abs(left - srcPointX) / srcPointX;
                alpha = (int) (255 * scale * scale);

                mLinePaint.setAlpha(alpha);
            }
            //Log.e("left", String.valueOf(left));
            canvas.drawLine(left, mLineMaxHeight + mTextMarginTop + mTextHeight, left, height, mLinePaint);

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int xPosition = (int) event.getX();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);
                mLastX = xPosition;
                mMove = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                mMove = (mLastX - xPosition);
                changeMoveAndValue();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                countMoveEnd();
                countVelocityTracker();
                return false;
            default:
                break;
        }

        mLastX = xPosition;
        return true;
    }

    private void countVelocityTracker() {
        mVelocityTracker.computeCurrentVelocity(1000); //The unit of initialization rate
        float xVelocity = mVelocityTracker.getXVelocity(); //current speed
        if (Math.abs(xVelocity) > mMinVelocity) {
            mScroller.fling(0, 0, (int) xVelocity, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        }
    }


    /**
     * After sliding, if the pointer is between 2 scales, change mOffset so that the pointer is just on the scale.
     */
    private void countMoveEnd() {

        mOffset -= mMove;
        if (mOffset <= mMaxOffset) {
            mOffset = mMaxOffset;
        } else if (mOffset >= 0) {
            mOffset = 0;
        }

        mLastX = 0;
        mMove = 0;

        mSelectorValue = mMinValue + Math.round(Math.abs(mOffset) * 1.0f / mLineSpaceWidth) * mPerValue / 10.0f;
        mOffset = (mMinValue - mSelectorValue) * 10.0f / mPerValue * mLineSpaceWidth;

        notifyValueChange();
        postInvalidate();
    }


    /**
     * Operation after sliding
     */
    private void changeMoveAndValue() {
        mOffset -= mMove;

        if (mOffset <= mMaxOffset) {
            mOffset = mMaxOffset;
            mMove = 0;
            mScroller.forceFinished(true);
        } else if (mOffset >= 0) {
            mOffset = 0;
            mMove = 0;
            mScroller.forceFinished(true);
        }
        mSelectorValue = mMinValue + Math.round(Math.abs(mOffset) * 1.0f / mLineSpaceWidth) * mPerValue / 10.0f;

        notifyValueChange();
        postInvalidate();
    }

    private void notifyValueChange() {
        if (null != mListener) {
            mListener.onValueChange(mSelectorValue);
        }
    }


    /**
     * Callback after sliding
     */
    public interface OnValueChangeListener {
        void onValueChange(float value);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {//mScroller.computeScrollOffset() returns true to indicate that the slide has not ended
            if (mScroller.getCurrX() == mScroller.getFinalX()) {
                countMoveEnd();
            } else {
                int xPosition = mScroller.getCurrX();
                mMove = (mLastX - xPosition);
                changeMoveAndValue();
                mLastX = xPosition;
            }
        }
    }
}


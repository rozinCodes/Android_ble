package com.waltonbd.smartscale.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.res.ResourcesCompat;

import com.waltonbd.smartscale.R;

/**
 * Author: Chayan on 2021-23-02.
 * Description: EditText with clear button
 */
public class EditTextWithClear extends AppCompatEditText {

    @DrawableRes
    private static final int DEFAULT_CLEAR_ICON_RES_ID = R.drawable.edittext_clear_logo;

    private Drawable mClearDrawable;

    public EditTextWithClear(Context context) {
        super(context);
        init();
    }

    public EditTextWithClear(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextWithClear(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Initialize Drawable member variable.
        mClearDrawable = ResourcesCompat.getDrawable(getResources(), DEFAULT_CLEAR_ICON_RES_ID, null);
    }


    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
        setClearIconVisible(hasFocus() && text.length() > 0);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        setClearIconVisible(focused && length() > 0);
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*if (event.getAction() == MotionEvent.ACTION_UP) {
            Drawable drawable = drawables[DRAWABLE_RIGHT];
            if (drawable != null && event.getX() <= (getWidth() - getPaddingRight())
                    && event.getX() >= (getWidth() - getPaddingRight() - drawable.getBounds().width())) {
                setText("");
            }
        }*/
        if (isClearIconTouched(event)) {
            setText(null);
            event.setAction(MotionEvent.ACTION_CANCEL);
            return false;
        }
        return super.onTouchEvent(event);
    }

    private boolean isClearIconTouched(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            final int touchPointX = (int) event.getX();

            final int widthOfView = getWidth();
            final int compoundPadding =
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                            ? getCompoundPaddingEnd()
                            : getCompoundPaddingRight();
            return touchPointX >= widthOfView - compoundPadding;
        }
        return false;
    }

    private void setClearIconVisible(boolean visible) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Drawable[] drawables = getCompoundDrawablesRelative();
            // show/hide icon on the right
            setCompoundDrawablesRelativeWithIntrinsicBounds(
                    drawables[0], drawables[1], visible ? mClearDrawable : null, drawables[3]);
        } else {
            Drawable[] drawables = getCompoundDrawables();
            // show/hide icon on the right
            setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], visible ? mClearDrawable : null, drawables[3]);
        }
    }
}


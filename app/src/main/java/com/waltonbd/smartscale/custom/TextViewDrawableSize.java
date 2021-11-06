package com.waltonbd.smartscale.custom;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * https://stackoverflow.com/a/31916731/5280371
 */
public class TextViewDrawableSize extends AppCompatTextView {

    private static final int mDrawableWidth = 55;
    private static final int mDrawableHeight = 55;

    public TextViewDrawableSize(@NonNull Context context) {
        super(context);
        init();
    }

    public TextViewDrawableSize(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewDrawableSize(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (mDrawableWidth > 0 || mDrawableHeight > 0) {
            initCompoundDrawableSize();
        }
    }

    private void initCompoundDrawableSize() {
        Drawable[] drawables = getCompoundDrawables();

        Drawable mLeftDrawable = drawables[0];
        if (mLeftDrawable == null) return;

        Rect realBounds = mLeftDrawable.getBounds();
        float scaleFactor = realBounds.height() / (float) realBounds.width();

        float drawableWidth = realBounds.width();
        float drawableHeight = realBounds.height();

        if (mDrawableWidth > 0) {
            // save scale factor of image
            if (drawableWidth > mDrawableWidth) {
                drawableWidth = mDrawableWidth;
                drawableHeight = drawableWidth * scaleFactor;
            }
        }
        if (mDrawableHeight > 0) {
            // save scale factor of image
            if (drawableHeight > mDrawableHeight) {
                drawableHeight = mDrawableHeight;
                drawableWidth = drawableHeight / scaleFactor;
            }
        }

        realBounds.right = realBounds.left + Math.round(drawableWidth);
        realBounds.bottom = realBounds.top + Math.round(drawableHeight);
        mLeftDrawable.setBounds(realBounds);

        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }
}
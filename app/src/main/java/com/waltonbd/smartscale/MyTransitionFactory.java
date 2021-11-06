package com.waltonbd.smartscale;

import android.graphics.drawable.Drawable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.transition.DrawableCrossFadeTransition;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.TransitionFactory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MyTransitionFactory implements TransitionFactory<Drawable> {
    private DrawableCrossFadeTransition resourceTransition = new DrawableCrossFadeTransition(300, true);

    @NotNull
    public Transition<Drawable> build(@Nullable DataSource dataSource, boolean isFirstResource) {
        return resourceTransition;
    }
}

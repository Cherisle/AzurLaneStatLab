package com.cherisle.azurlanestatlab;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ShipCardAnimation extends Animation {
    private int startHeight;
    private int deltaHeight; // distance between start and end height
    private View view;
    private ShipsAdapter.ShipViewHolder viewholder;

    /**
     * constructor, do not forget to use the setParams(int, int) method before
     * starting the animation
     * @param view_to_animate
     * @param view_holder
     */
    public ShipCardAnimation (View view_to_animate, ShipsAdapter.ShipViewHolder view_holder) {
        this.view = view_to_animate;
        this.viewholder = view_holder;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        view.getLayoutParams().height = (int) (startHeight + deltaHeight * interpolatedTime);
        view.requestLayout();
    }

    /**
     * set the starting and ending height for the resize animation
     * starting height is usually the views current height, the end height is the height
     * we want to reach after the animation is completed
     * @param start height in pixels
     * @param end height in pixels
     */
    public void setParams(int start, int end) {

        this.startHeight = start;
        deltaHeight = end - startHeight;
    }

    /**
     * set the duration for the hideshowanimation
     */
    @Override
    public void setDuration(long durationMillis) {
        super.setDuration(durationMillis);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}

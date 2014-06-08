package com.example.doan_didong;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ScalableImageView extends ImageView {
    public boolean isMeasured = true; 

    public ScalableImageView(Context context) {
        super(context);
    }

    public ScalableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScalableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDetachedFromWindow () 
    {
        setImageDrawable(null);
        setBackgroundDrawable(null);
        setImageBitmap(null);
        System.gc();
    }
}

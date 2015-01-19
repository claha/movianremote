package com.claha.showtimeremote;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class ShowtimeToggleButton extends ShowtimeButton {

    private Drawable src;
    private Drawable src2;

    public ShowtimeToggleButton(Context context) {
        super(context);
    }

    public ShowtimeToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public ShowtimeToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ShowtimeToggleButton, 0, 0);
        try {
            src2 = a.getDrawable(R.styleable.ShowtimeToggleButton_src2);
            src = getDrawable();
        } finally {
            a.recycle();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (getDrawable() == src) {
            setImageDrawable(src2);
        } else {
            setImageDrawable(src);
        }
    }
}

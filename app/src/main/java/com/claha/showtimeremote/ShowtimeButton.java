package com.claha.showtimeremote;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class ShowtimeButton extends ImageButton implements View.OnClickListener, View.OnTouchListener {

    private String action;
    private boolean onPress = false;

    private final ShowtimeHTTP showtimeHTTP = new ShowtimeHTTP();

    private final Handler handler = new Handler();
    private final Runnable handlerRunnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 100);
            ShowtimeButton.this.onClick(ShowtimeButton.this);
        }
    };

    public ShowtimeButton(Context context) {
        super(context);
        init();
    }

    public ShowtimeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    public ShowtimeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ShowtimeButton, 0, 0);
        try {
            action = a.getString(R.styleable.ShowtimeButton_action);
            onPress = a.getBoolean(R.styleable.ShowtimeButton_onPress, false);
        } finally {
            a.recycle();
        }
    }

    private void init() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        showtimeHTTP.setIpAddress(sharedPreferences.getString("ipAddress", null));
        showtimeHTTP.setPort(sharedPreferences.getString("port", null));

        if (onPress) {
            setOnTouchListener(this);
        } else {
            setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Log.d("ShowtimeButton", showtimeHTTP.getIpAddress() + ":" + showtimeHTTP.getPort() + " - " + action);
        showtimeHTTP.sendAction(action);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(v);
                return true;
            case MotionEvent.ACTION_UP:
                onTouchUp();
                return true;
            case MotionEvent.ACTION_CANCEL:
                onTouchCancel();
                return true;
            default:
                return false;
        }
    }

    private void onTouchDown(View v) {
        handler.removeCallbacks(handlerRunnable);
        handler.postDelayed(handlerRunnable, 400);
        this.onClick(v);
    }

    private void onTouchUp() {
        handler.removeCallbacks(handlerRunnable);
    }

    private void onTouchCancel() {
        onTouchUp();
    }
}

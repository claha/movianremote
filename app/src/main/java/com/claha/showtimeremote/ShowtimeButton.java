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

public class ShowtimeButton extends ImageButton implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {

    private final static String TAG = "ShowtimeButton";
    private final static String IP_ADDRESS = "ipAddress";
    private final static String PORT = "port";

    private String action;
    private String actionLong = null;
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
        Log.d(TAG, "initAttrs");
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ShowtimeButton, 0, 0);
        try {
            action = a.getString(R.styleable.ShowtimeButton_action);
            actionLong = a.getString(R.styleable.ShowtimeButton_actionLong);
            onPress = a.getBoolean(R.styleable.ShowtimeButton_onPress, false);
        } finally {
            a.recycle();
        }
    }

    private void init() {
        Log.d(TAG, "init");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        showtimeHTTP.setIpAddress(sharedPreferences.getString(IP_ADDRESS, null));
        showtimeHTTP.setPort(sharedPreferences.getString(PORT, null));

        if (onPress) {
            setOnTouchListener(this);
        } else {
            setOnClickListener(this);
            if (actionLong != null) {
                setOnLongClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, showtimeHTTP.getIpAddress() + ":" + showtimeHTTP.getPort() + " - " + action);
        showtimeHTTP.sendAction(action);
    }

    @Override
    public boolean onLongClick(View v) {
        Log.d(TAG, showtimeHTTP.getIpAddress() + ":" + showtimeHTTP.getPort() + " - " + actionLong);
        showtimeHTTP.sendAction(actionLong);
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown();
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

    private void onTouchDown() {
        handler.removeCallbacks(handlerRunnable);
        handler.postDelayed(handlerRunnable, 400);
        this.onClick(this);
        this.setPressed(true);
    }

    private void onTouchUp() {
        handler.removeCallbacks(handlerRunnable);
        this.setPressed(false);
    }

    private void onTouchCancel() {
        onTouchUp();
    }

}

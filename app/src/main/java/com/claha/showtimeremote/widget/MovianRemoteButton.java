package com.claha.showtimeremote.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.claha.showtimeremote.R;
import com.claha.showtimeremote.core.MovianRemoteHTTP;

public class MovianRemoteButton extends ImageButton implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {

    private final Handler handler = new Handler();
    private final Runnable handlerRunnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 100);
            MovianRemoteButton.this.onClick(MovianRemoteButton.this);
        }
    };
    private String action;
    private String actionLong = null;
    private boolean onPress = false;
    private MovianRemoteHTTP http;

    public MovianRemoteButton(Context context) {
        super(context);
        if (this.isInEditMode()) return;
        init();
    }

    public MovianRemoteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (this.isInEditMode()) return;
        initAttrs(attrs);
        init();
    }

    public MovianRemoteButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (this.isInEditMode()) return;
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.MovianRemoteButton, 0, 0);
        try {
            action = a.getString(R.styleable.MovianRemoteButton_action);
            actionLong = a.getString(R.styleable.MovianRemoteButton_actionLong);
            onPress = a.getBoolean(R.styleable.MovianRemoteButton_onPress, false);
        } finally {
            a.recycle();
        }
    }

    private void init() {
        http = new MovianRemoteHTTP(getContext());

        if (onPress) {
            setOnTouchListener(this);
        } else {
            setOnClickListener(this);
            if (actionLong != null) {
                setOnLongClickListener(this);
            }
        }
    }

    public void setOnClickAction(String action) {
        this.action = action;
    }

    public void setOnLongClickAction(String actionLong) {
        this.actionLong = actionLong;
        setOnLongClickListener(this);
        disableOnPress();
    }

    /*public void enableOnPress() {
        onPress = true;
        setOnTouchListener(this);
        setOnLongClickListener(null);
    }*/

    private void disableOnPress() {
        onPress = false;
        setOnTouchListener(null);
    }

    @Override
    public void onClick(View v) {
        if (action != null) {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            http.sendAction(action);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (actionLong != null) {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            http.sendAction(actionLong);
            return true;
        }
        return false;
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
        onClick(this);
        setPressed(true);
    }

    private void onTouchUp() {
        handler.removeCallbacks(handlerRunnable);
        setPressed(false);
    }

    private void onTouchCancel() {
        onTouchUp();
    }

}

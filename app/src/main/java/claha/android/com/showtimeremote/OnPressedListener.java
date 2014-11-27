package claha.android.com.showtimeremote;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class OnPressedListener implements OnTouchListener {

    private Handler handler = new Handler();

    private final int initialInterval = 400;
    private final int normalInterval = 100;
    private final OnClickListener clickListener;

    private Runnable handlerRunnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, normalInterval);
            clickListener.onClick(downView);
        }
    };

    private View downView;

    public OnPressedListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return actionDown(view);
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                return actionCancel();
        }

        return false;
    }

    private boolean actionDown(View view) {
        handler.removeCallbacks(handlerRunnable);
        handler.postDelayed(handlerRunnable, initialInterval);
        downView = view;
        clickListener.onClick(view);
        downView.setPressed(true);
        return true;
    }

    private boolean actionCancel() {
        handler.removeCallbacks(handlerRunnable);
        downView.setPressed(false);
        downView = null;
        return true;
    }

}
package com.claha.showtimeremote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public abstract class ButtonFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "ButtonFragment";

    private ShowtimeHTTP showtimeHTTP;
    protected ArrayList<ShowtimeButtonOld> buttons;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        showtimeHTTP = new ShowtimeHTTP();

        showtimeHTTP.setIpAddress(getIPAddress());
        showtimeHTTP.setPort(getPort());

        buttons = new ArrayList<>();
        setupButtons();

        // Set on click listener for all buttons
        for (final ShowtimeButtonOld showtimeButtonOld : buttons) {
            try {
                Button button = (Button) view.findViewById(showtimeButtonOld.getId());
                button.setText(showtimeButtonOld.getName());
                button.setOnClickListener(this);
            } catch (ClassCastException e) {
                ImageButton button = (ImageButton) view.findViewById(showtimeButtonOld.getId());

                if (!showtimeButtonOld.getOnLongClickAction().equals("")) {

                    button.setOnTouchListener(new OnPressedListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String action = showtimeButtonOld.getOnClickAction();
                            showtimeHTTP.sendAction(action);
                        }
                    }));
                } else {
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String action = showtimeButtonOld.getOnClickAction();
                            showtimeHTTP.sendAction(action);
                        }
                    });
                }
            }
        }

        return view;
    }

    protected abstract void setupButtons();

    @Override
    public void onClick(View view) {
        debug("onClick");
        int id = view.getId();

        int index = buttons.indexOf(new ShowtimeButtonOld(id));
        if (index != -1) {
            String action = buttons.get(index).getOnClickAction();
            showtimeHTTP.sendAction(action);
            toast(action);
        }
    }

    /*@Override
    public boolean onLongClick(View view) {
        debug("onLongClick");
        int id = view.getId();
        int index = buttons.indexOf(new ShowtimeButton(id));
        if (index != -1) {
            String action = buttons.get(index).getOnLongClickAction();
            showtimeHTTP.sendAction(action);
            toast(action);
            return true;
        }
        return false;
    }*/

    private void toast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

}

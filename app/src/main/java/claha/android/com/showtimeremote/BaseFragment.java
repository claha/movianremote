package claha.android.com.showtimeremote;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public abstract class BaseFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "BaseFragment";

    private ShowtimeHTTP showtimeHTTP;
    protected ArrayList<ShowtimeButton> buttons;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);

        showtimeHTTP = new ShowtimeHTTP();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String ipAddress = sharedPrefs.getString("ipAddress", "NULL");
        String port = sharedPrefs.getString("port", "NULL");

        showtimeHTTP.setIpAddress(ipAddress);
        showtimeHTTP.setPort(port);

        buttons = new ArrayList<ShowtimeButton>();
        setupButtons();

        // Set on click listener for all buttons
        for (ShowtimeButton showtimeButton : buttons) {
            Button button = (Button) view.findViewById(showtimeButton.getId());
            button.setText(showtimeButton.getName());
            button.setOnClickListener(this);
            button.setOnLongClickListener(this);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        debug("onClick");
        int id = view.getId();

        int index = buttons.indexOf(new ShowtimeButton(id));
        if (index != -1) {
            String action = buttons.get(index).getOnClickAction();
            showtimeHTTP.sendAction(action);
            toast(action);
        }
    }

    @Override
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
    }

    private void toast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    protected abstract int getLayoutResource();

    protected abstract void setupButtons();

    protected void debug(String text) {
        Log.d(TAG, text);
    }

}


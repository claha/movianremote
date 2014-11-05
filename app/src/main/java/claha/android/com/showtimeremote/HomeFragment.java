package claha.android.com.showtimeremote;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String ipAddress = sharedPrefs.getString("ipAddress", "NULL");
        String port = sharedPrefs.getString("port", "NULL");

        TextView info = (TextView)view.findViewById(R.id.info);
        info.setText(ipAddress + ":" + port);

        return view;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    protected void setupButtons() {

    }
}

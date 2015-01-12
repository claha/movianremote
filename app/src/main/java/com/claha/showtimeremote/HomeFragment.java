package com.claha.showtimeremote;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HomeFragment extends BaseFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView info = (TextView) view.findViewById(R.id.info);
        info.setText(getIPAddress() + ":" + getPort());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

}

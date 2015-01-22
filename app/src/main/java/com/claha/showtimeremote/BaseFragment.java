package com.claha.showtimeremote;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public abstract class BaseFragment extends Fragment {

    private ShowtimeSettings showtimeSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        showtimeSettings = new ShowtimeSettings(getActivity());
        return inflater.inflate(getLayoutResource(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        final List<String> profiles = showtimeSettings.loadProfiles().getPrettyStringList();

        if (profiles.isEmpty()) {
            profiles.add(showtimeSettings.getIPAddress());
        }

        ProfileAdapter adapter = new ProfileAdapter(viewPager, profiles);
        viewPager.setAdapter(adapter);

        int index = showtimeSettings.loadProfiles().indexOf(showtimeSettings.getCurrentProfile()) + 1;
        viewPager.setCurrentItem(index);

    }

    protected abstract int getLayoutResource();

    private class ProfileAdapter extends CircularPagerAdapter<String> {

        public ProfileAdapter(ViewPager viewPager, List<String> data) {
            super(viewPager, data);
        }

        @Override
        protected View instantiateView(Context context, String item) {
            TextView view = new TextView(context);
            view.setText(item);
            view.setGravity(Gravity.CENTER);
            view.setTextColor(0xFFFFFFFF);
            view.setTypeface(Typeface.DEFAULT_BOLD);
            return view;
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            position = getOriginalPosition(position);
            showtimeSettings.setCurrentProfile(showtimeSettings.loadProfiles().get(position));
        }
    }
}
package com.claha.showtimeremote;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
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

        final List<String> profiles = new ArrayList<>();
        ShowtimeSettings.Profiles ps = showtimeSettings.loadProfiles();
        for (ShowtimeSettings.Profile p : ps) {
            profiles.add(p.getName().toUpperCase() + " (" + p.getIPAddress() + ")");
        }

        if (profiles.size() > 1) {
            profiles.add(0, ps.get(ps.size() - 1).getName().toUpperCase() + " (" + ps.get(ps.size() - 1).getIPAddress() + ")");
            profiles.add(ps.get(0).getName().toUpperCase() + " (" + ps.get(0).getIPAddress() + ")");
        }

        if (profiles.isEmpty()) {
            profiles.add(showtimeSettings.getIPAddress());
        }

        ProfileAdapter adapter = new ProfileAdapter(profiles);
        viewPager.setAdapter(adapter);

        int index = showtimeSettings.loadProfiles().indexOf(showtimeSettings.getCurrentProfile());
        index = index < 0 ? 0 : index + 1;

        viewPager.setCurrentItem(index);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int currentPage = 0;

            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int pageCount = profiles.size();
                    if (currentPage == 0) {
                        viewPager.setCurrentItem(pageCount - 2, false);
                    } else if (currentPage == pageCount - 1) {
                        viewPager.setCurrentItem(1, false);
                    }
                    showtimeSettings.setCurrentProfile(showtimeSettings.loadProfiles().get(currentPage - 1));
                }
            }
        });
    }

    protected abstract int getLayoutResource();

    public class ProfileAdapter extends PagerAdapter {

        final List<String> profiles;

        public ProfileAdapter(List<String> profiles) {
            this.profiles = profiles;
        }

        @Override
        public int getCount() {
            return profiles.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            View view = inflater.inflate(R.layout.bottombar, container, false);
            TextView textView = (TextView) view.findViewById(R.id.profile);
            textView.setText(profiles.get(position));

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object view) {
            container.removeView((View) view);
        }
    }
}
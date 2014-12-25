package com.claha.showtimeremote;

import java.util.ArrayList;

public class NavigationFragment extends ButtonFragment {

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_navigation;
    }

    @Override
    protected void setupButtons() {
        buttons = new ArrayList<>();
        buttons.add(new ShowtimeButtonOld(R.id.buttonOk, "OK", ShowtimeHTTP.ACTION_ACTIVATE));
        buttons.add(new ShowtimeButtonOld(R.id.buttonUp, "Up", ShowtimeHTTP.ACTION_UP, ShowtimeHTTP.ACTION_UP));
        buttons.add(new ShowtimeButtonOld(R.id.buttonDown, "Down", ShowtimeHTTP.ACTION_DOWN, ShowtimeHTTP.ACTION_DOWN));
        buttons.add(new ShowtimeButtonOld(R.id.buttonLeft, "Left", ShowtimeHTTP.ACTION_LEFT, ShowtimeHTTP.ACTION_LEFT));
        buttons.add(new ShowtimeButtonOld(R.id.buttonRight, "Right", ShowtimeHTTP.ACTION_RIGHT, ShowtimeHTTP.ACTION_RIGHT));
        buttons.add(new ShowtimeButtonOld(R.id.buttonBack, "Back", ShowtimeHTTP.ACTION_NAV_BACK));
        buttons.add(new ShowtimeButtonOld(R.id.buttonMenu, "Menu", ShowtimeHTTP.ACTION_MENU));
        buttons.add(new ShowtimeButtonOld(R.id.buttonHome, "Home", ShowtimeHTTP.ACTION_HOME));
    }


}

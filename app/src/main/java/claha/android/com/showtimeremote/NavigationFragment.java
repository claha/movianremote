package claha.android.com.showtimeremote;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class NavigationFragment extends BaseFragment {

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_navigation;
    }

    @Override
    protected void setupButtons() {
        buttons = new ArrayList<ShowtimeButton>();
        buttons.add(new ShowtimeButton(R.id.buttonOk, "OK", ShowtimeHTTP.ACTION_ACTIVATE));
        buttons.add(new ShowtimeButton(R.id.buttonTop, "Top", ShowtimeHTTP.ACTION_TOP));
        buttons.add(new ShowtimeButton(R.id.buttonPageUp, "Page Up", ShowtimeHTTP.ACTION_PAGE_UP));
        buttons.add(new ShowtimeButton(R.id.buttonUp, "Up", ShowtimeHTTP.ACTION_UP));
        buttons.add(new ShowtimeButton(R.id.buttonBottom, "Bottom", ShowtimeHTTP.ACTION_BOTTOM));
        buttons.add(new ShowtimeButton(R.id.buttonPageDown, "Page Down", ShowtimeHTTP.ACTION_PAGE_DOWN));
        buttons.add(new ShowtimeButton(R.id.buttonDown, "Down", ShowtimeHTTP.ACTION_DOWN));
        buttons.add(new ShowtimeButton(R.id.buttonLeft, "Left", ShowtimeHTTP.ACTION_LEFT));
        buttons.add(new ShowtimeButton(R.id.buttonRight, "Right", ShowtimeHTTP.ACTION_RIGHT));
        buttons.add(new ShowtimeButton(R.id.buttonBack, "Back", ShowtimeHTTP.ACTION_NAV_BACK));
        buttons.add(new ShowtimeButton(R.id.buttonMenu, "Menu", ShowtimeHTTP.ACTION_MENU, ShowtimeHTTP.ACTION_ITEMMENU));
        buttons.add(new ShowtimeButton(R.id.buttonHome, "Home", ShowtimeHTTP.ACTION_HOME));
    }


}

package claha.android.com.showtimeremote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowtimeRemote extends ActionBarActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "ShowtimeRemote";

    private static final int SHOWTIMESETTINGS = 0;

    private ShowtimeHTTP showtimeHTTP;
    private TextView info;

    private ArrayList<ShowtimeButton> buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtime_remote);

        // Info
        info = (TextView) findViewById(R.id.info);

        // Showtime HTTP
        showtimeHTTP = new ShowtimeHTTP();

        // Settings
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        loadSettings();

        // Buttons
        buttons = new ArrayList<ShowtimeButton>();
        buttons.add(new ShowtimeButton(R.id.buttonLeft, "LEFT", ShowtimeHTTP.ACTION_LEFT, ShowtimeHTTP.ACTION_SEEK_BACKWARD));
        buttons.add(new ShowtimeButton(R.id.buttonRight, "RGHT", ShowtimeHTTP.ACTION_RIGHT, ShowtimeHTTP.ACTION_SEEK_FORWARD));
        buttons.add(new ShowtimeButton(R.id.buttonUp, "UP", ShowtimeHTTP.ACTION_UP, ShowtimeHTTP.ACTION_PAGE_UP));
        buttons.add(new ShowtimeButton(R.id.buttonDown, "DOWN", ShowtimeHTTP.ACTION_DOWN, ShowtimeHTTP.ACTION_PAGE_DOWN));
        buttons.add(new ShowtimeButton(R.id.buttonOK, "OK", ShowtimeHTTP.ACTION_ACTIVATE));

        buttons.add(new ShowtimeButton(R.id.buttonHome, "HOME", ShowtimeHTTP.ACTION_HOME));
        buttons.add(new ShowtimeButton(R.id.buttonMenu, "MENU", ShowtimeHTTP.ACTION_MENU, ShowtimeHTTP.ACTION_ITEMMENU));

        buttons.add(new ShowtimeButton(R.id.buttonBack, "BACK", ShowtimeHTTP.ACTION_NAV_BACK));
        buttons.add(new ShowtimeButton(R.id.buttonForward, "FWD", ShowtimeHTTP.ACTION_NAV_FWD));

        // Set on click listener for all buttons
        for (ShowtimeButton showtimeButton : buttons) {
            Button button = (Button) findViewById(showtimeButton.getId());
            button.setText(showtimeButton.getName());
            button.setOnClickListener(this);
            button.setOnLongClickListener(this);
        }

        Button buttonAction = (Button) findViewById(R.id.buttonAction);
        buttonAction.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.showtime_remote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, ShowtimeSettings.class);
            startActivityForResult(i, SHOWTIMESETTINGS);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SHOWTIMESETTINGS:
                loadSettings();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void loadSettings() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String ipAddress = sharedPrefs.getString("ipAddress", "NULL");
        String port = sharedPrefs.getString("port", "NULL");
        info.setText(ipAddress + ":" + port);

        showtimeHTTP.setIpAddress(ipAddress);
        showtimeHTTP.setPort(port);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        // Use this when all buttons are set up correctly
        int index = buttons.indexOf(new ShowtimeButton(id));
        if (index != -1) {
            String action = buttons.get(index).getOnClickAction();
            showtimeHTTP.sendAction(action);
            toast(action);
        }


        // Find out what each action really does
        final String[] actions = getResources().getStringArray(R.array.actions);
        if (id == R.id.buttonAction) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose action").setItems(R.array.actions, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String action = actions[which];
                    showtimeHTTP.sendAction(action);
                    toast(action);
                }
            });
            builder.create().show();
        }


    }

    @Override
    public boolean onLongClick(View view) {
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
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}

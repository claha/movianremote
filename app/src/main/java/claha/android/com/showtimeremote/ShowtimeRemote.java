package claha.android.com.showtimeremote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class ShowtimeRemote extends Activity implements View.OnClickListener {

    private static final int SHOWTIMESETTINGS = 0;

    private ShowtimeHTTP showtimeHTTP;
    private TextView info;

    private Map<Integer, String> buttons;

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
        buttons = new HashMap<Integer, String>();
        buttons.put(R.id.buttonAction, ""); // Assign specific action

        // Set on click listener for all buttons
        for (int id : buttons.keySet()) {
            Button button = (Button) findViewById(id);
            button.setOnClickListener(this);
        }

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

        /*
        // Use this when all buttons are set up correctly
        if (buttons.containsKey(id)) {
            String action = buttons.get(id);
            showtimeHTTP.sendAction(action);
        }
        */

        // Find out what each action really does
        final String[] actions = getResources().getStringArray(R.array.actions);
        if (buttons.containsKey(id)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose action").setItems(R.array.actions, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String action = actions[which];
                    showtimeHTTP.sendAction(action);
                    Toast.makeText(getApplicationContext(), action, Toast.LENGTH_SHORT).show();
                }
            });
            builder.create().show();
        }


    }
}

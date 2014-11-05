package claha.android.com.showtimeremote;

import java.util.ArrayList;

public class MediaFragment extends BaseFragment {

    @Override
    protected void setupButtons() {
        buttons = new ArrayList<ShowtimeButton>();
        buttons.add(new ShowtimeButton(R.id.buttonVolumeDown, "Vol-", ShowtimeHTTP.ACTION_VOLUME_DOWN));
        buttons.add(new ShowtimeButton(R.id.buttonVolumeMute, "Mute", ShowtimeHTTP.ACTION_VOLUME_MUTE_TOGGLE));
        buttons.add(new ShowtimeButton(R.id.buttonVolumeUp, "Vol+", ShowtimeHTTP.ACTION_VOLUME_UP));
        buttons.add(new ShowtimeButton(R.id.buttonSeekBackward, "Seek-", ShowtimeHTTP.ACTION_SEEK_BACKWARD));
        buttons.add(new ShowtimeButton(R.id.buttonSeekForward, "Seek+", ShowtimeHTTP.ACTION_SEEK_FORWARD));
        buttons.add(new ShowtimeButton(R.id.buttonSkipBackward, "Skip-", ShowtimeHTTP.ACTION_SKIP_BACKWARD));
        buttons.add(new ShowtimeButton(R.id.buttonSkipForward, "Skip+", ShowtimeHTTP.ACTION_SKIP_FORWARD));
        buttons.add(new ShowtimeButton(R.id.buttonStop, "Stop", ShowtimeHTTP.ACTION_STOP));
        buttons.add(new ShowtimeButton(R.id.buttonPlay, "Play", ShowtimeHTTP.ACTION_PLAY));
        buttons.add(new ShowtimeButton(R.id.buttonPause, "Pause", ShowtimeHTTP.ACTION_PAUSE));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_video;
    }
}

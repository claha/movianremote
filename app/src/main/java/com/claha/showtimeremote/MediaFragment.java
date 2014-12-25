package com.claha.showtimeremote;

public class MediaFragment extends ButtonFragment {

    @Override
    protected void setupButtons() {
        buttons.add(new ShowtimeButtonOld(R.id.buttonVolumeDown, "Vol-", ShowtimeHTTP.ACTION_VOLUME_DOWN));
        buttons.add(new ShowtimeButtonOld(R.id.buttonVolumeMute, "Mute", ShowtimeHTTP.ACTION_VOLUME_MUTE_TOGGLE));
        buttons.add(new ShowtimeButtonOld(R.id.buttonVolumeUp, "Vol+", ShowtimeHTTP.ACTION_VOLUME_UP));
        buttons.add(new ShowtimeButtonOld(R.id.buttonSeekBackward, "Seek-", ShowtimeHTTP.ACTION_SEEK_BACKWARD));
        buttons.add(new ShowtimeButtonOld(R.id.buttonSeekForward, "Seek+", ShowtimeHTTP.ACTION_SEEK_FORWARD));
        buttons.add(new ShowtimeButtonOld(R.id.buttonSkipBackward, "Skip-", ShowtimeHTTP.ACTION_SKIP_BACKWARD));
        buttons.add(new ShowtimeButtonOld(R.id.buttonSkipForward, "Skip+", ShowtimeHTTP.ACTION_SKIP_FORWARD));
        buttons.add(new ShowtimeButtonOld(R.id.buttonStop, "Stop", ShowtimeHTTP.ACTION_STOP));
        buttons.add(new ShowtimeButtonOld(R.id.buttonPlay, "Play", ShowtimeHTTP.ACTION_PLAY));
        buttons.add(new ShowtimeButtonOld(R.id.buttonPause, "Pause", ShowtimeHTTP.ACTION_PAUSE));

        buttons.add(new ShowtimeButtonOld(R.id.buttonAudio, "Audio", ShowtimeHTTP.ACTION_CYCLE_AUDIO));
        buttons.add(new ShowtimeButtonOld(R.id.buttonSubtitle, "Subs", ShowtimeHTTP.ACTION_CYCLE_SUBTITLE));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_media;
    }

}

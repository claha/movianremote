package com.claha.showtimeremote.preference;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.List;

public class IPAddressPickerPreference extends DialogPreference {
    private final String DEFAULT_IP_ADDRESS = "192.168.0.0";
    private String ipAddress;
    private List<NumberPicker> numberPickers;

    public IPAddressPickerPreference(Context context) {
        super(context, null);
    }


    @Override
    protected void onSetInitialValue(final boolean restoreValue, final Object defaultValue) {
        ipAddress = restoreValue ? getPersistedString(DEFAULT_IP_ADDRESS) : (String) defaultValue;
    }

    @Override
    protected Object onGetDefaultValue(final TypedArray a, final int index) {
        return a.getString(index);
    }

    @Override
    protected void onPrepareDialogBuilder(final Builder builder) {
        super.onPrepareDialogBuilder(builder);

        final LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        linearLayout.setGravity(Gravity.CENTER);

        numberPickers = new ArrayList<>();

        if (ipAddress == null) {
            ipAddress = DEFAULT_IP_ADDRESS;
        }

        for (int i = 0; i < 4; i++) {

            NumberPicker numberPicker = new NumberPicker(this.getContext());
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(255);
            numberPicker.setValue(Integer.parseInt(ipAddress.split("\\.")[i]));
            numberPicker.setWrapSelectorWheel(true);
            numberPicker.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            numberPickers.add(numberPicker);

            linearLayout.addView(numberPicker);
        }

        builder.setView(linearLayout);
    }

    @Override
    protected void onDialogClosed(final boolean positiveResult) {
        if (positiveResult) {
            ipAddress = numberPickers.get(0).getValue() + "." + numberPickers.get(1).getValue() + "." + numberPickers.get(2).getValue() + "." + numberPickers.get(3).getValue();
            if (callChangeListener(ipAddress)) {
                persistString(ipAddress);
            }
        }
    }

    public void setIPAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}

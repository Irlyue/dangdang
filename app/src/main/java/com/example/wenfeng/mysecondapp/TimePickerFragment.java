package com.example.wenfeng.mysecondapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    private Button mView;

    public void setView(Button view){
        mView = view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int h, m;
        String[] times = mView.getText().toString().split(":");
        h = Integer.parseInt(times[0]);
        m = Integer.parseInt(times[1]);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, h, m,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Button left = getActivity().findViewById(R.id.left_text_view);
        Button right = getActivity().findViewById(R.id.right_text_view);

        if(mView == left) {
            left.setText(String.format("%02d:%02d:00", hourOfDay, minute));
            right.setText(String.format("%02d:%02d:00", hourOfDay, minute));
        }else if(mView == right){
            right.setText(String.format("%02d:%02d:00", hourOfDay, minute));
        }
    }
}

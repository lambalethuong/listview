package com.example.sgvn89.listviewapp;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sgvn89 on 2018/02/23.
 * add tab
 */

public class AddActivity extends Fragment {

    TextView name;
    TextView class_name;
    Button addBtn;
    Button vibrateBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_tab, container, false);
        name = rootView.findViewById(R.id.name);
        class_name = rootView.findViewById(R.id.class_name);
        addBtn = rootView.findViewById(R.id.button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Student newStudent = new Student(name.getText().toString(), class_name.getText().toString());
                MainActivity.listScreen.addItem(newStudent);
            }
        });
        final Vibrator vibrate = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        final long[] pattern = {0, 100, 1000, 500, 1000, 1000, 1000};
        vibrateBtn = rootView.findViewById(R.id.button2);
        vibrateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vibrate != null && vibrate.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrate.vibrate(VibrationEffect.createWaveform(pattern, -1));
                    }else{
                        vibrate.vibrate(pattern, -1);
                    }
                }else{
                    Toast.makeText(getContext(), "Device does not support vibration", Toast.LENGTH_LONG).show();
                }

//                if (Build.VERSION.SDK_INT >= 26) {
//                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
//                } else {
//                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
//                }
            }
        });
        return rootView;
    }

}

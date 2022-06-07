package com.example.meee;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class Stats extends AppCompatActivity {

    NumberPicker yearPicker, monthPicker;
    private static final int MAX_YEAR = 2030;
    private static final int MIN_YEAR = 2010;

    Button btn;
    TextView tv;
    View datedialog;

    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        btn = findViewById(R.id.button);
        tv = findViewById(R.id.textView);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datedialog = View.inflate(Stats.this, R.layout.datedialog, null);
                yearPicker = datedialog.findViewById(R.id.yearPicker);
                monthPicker = datedialog.findViewById(R.id.monthPicker);
                monthPicker.setMinValue(1);
                monthPicker.setMaxValue(12);
                yearPicker.setMinValue(MIN_YEAR);
                yearPicker.setMaxValue(MAX_YEAR);

                new AlertDialog.Builder(Stats.this)
                        .setTitle("날짜 선택")
                        .setIcon(R.drawable.calendar_month)
                        .setView(datedialog)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                date = String.valueOf(yearPicker.getValue()) +". "
                                        + String.valueOf(monthPicker.getValue());
                                tv.setText(date);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
            }
        });
    }
}
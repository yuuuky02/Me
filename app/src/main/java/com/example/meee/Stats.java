package com.example.meee;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class Stats extends AppCompatActivity {
    MemoDBHelper memoHelper;
    Cursor cursor;
    String result;
    String[] dbemotion = {"매우 좋음", "중간", "매우 나쁨"};

    NumberPicker yearPicker, monthPicker;
    private static final int MAX_YEAR = 2024;
    private static final int MIN_YEAR = 2020;

    Button btn1_st;
    TextView tv1_st, tv17_st, tv19_st;
    TextView[] tvst = new TextView[4];
    TextView[] tvst_result = new TextView[4];
    TextView[] tvst_emo = new TextView[3];
    View datedialog;
    String Mmonth;

    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        memoHelper = new MemoDBHelper(this);
        btn1_st = findViewById(R.id.btn1_st);
        tv1_st = findViewById(R.id.tv1_st);
        tvst[0] = findViewById(R.id.tv3_st);
        tvst[1] = findViewById(R.id.tv5_st);
        tvst[2] = findViewById(R.id.tv7_st);
        tvst[3] = findViewById(R.id.tv9_st);
        tvst_result[0] = findViewById(R.id.tv4_st);
        tvst_result[1] = findViewById(R.id.tv6_st);
        tvst_result[2] = findViewById(R.id.tv8_st);
        tvst_result[3] = findViewById(R.id.tv10_st);
        tvst_emo[0] = findViewById(R.id.tv12_st);
        tvst_emo[1] = findViewById(R.id.tv13_st);
        tvst_emo[2] = findViewById(R.id.tv14_st);
        tv17_st = findViewById(R.id.tv17_st);
        tv19_st = findViewById(R.id.tv19_st);

        btn1_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datedialog = View.inflate(Stats.this, R.layout.datedialog, null);
                yearPicker = datedialog.findViewById(R.id.yearPicker_date);
                monthPicker = datedialog.findViewById(R.id.monthPicker_date);
                monthPicker.setMinValue(1);
                monthPicker.setMaxValue(12);
                yearPicker.setMinValue(MIN_YEAR);
                yearPicker.setMaxValue(MAX_YEAR);

                SQLiteDatabase db;
                db = memoHelper.getReadableDatabase();

                new AlertDialog.Builder(Stats.this)
                        .setTitle("날짜 선택")
                        .setIcon(R.drawable.calendar_month)
                        .setView(datedialog)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (monthPicker.getValue() > 0 && monthPicker.getValue() < 10){
                                    Mmonth = "0" + monthPicker.getValue();
                                }else{
                                    Mmonth = String.valueOf(monthPicker.getValue());
                                }
                                date = String.valueOf(yearPicker.getValue())+"년"+Mmonth+"월";
                                tv1_st.setText(date);

                                // 메모 기록 횟수
                                for (int i1=0; i1<tvst.length; i1++){
                                    cursor = db.rawQuery("SELECT count(id) FROM memo WHERE substr(date,1,8)=? and category=?",
                                            new String[]{tv1_st.getText().toString(), tvst[i1].getText().toString()});
                                    if (cursor.moveToNext()) {
                                        result = cursor.getString(0);
                                    }
                                    tvst_result[i1].setText(result+" 회");
                                }

                                // 감정 기록 횟수
                                for (int i2=0; i2<dbemotion.length; i2++){
                                    cursor = db.rawQuery("SELECT count(id) FROM memo WHERE substr(date,1,8)=? and emotion=?",
                                            new String[]{tv1_st.getText().toString(), dbemotion[i2]});
                                    if (cursor.moveToNext()) {
                                        result = cursor.getString(0);
                                    }
                                    tvst_emo[i2].setText(result+" 회");
                                }

                                // 운동_운동시간 월별 합계
                                cursor = db.rawQuery("SELECT sum(etime) FROM exercise WHERE substr(date,1,8)=?",
                                        new String[]{tv1_st.getText().toString()});
                                if (cursor.moveToNext()) {
                                    result = cursor.getString(0);
                                }
                                tv17_st.setText(result+" 시간");

                                // 운동_달린거리 월별 합계
                                cursor = db.rawQuery("SELECT sum(edistance) FROM exercise WHERE substr(date,1,8)=?",
                                        new String[]{tv1_st.getText().toString()});
                                if (cursor.moveToNext()) {
                                    result = cursor.getString(0);
                                }
                                tv19_st.setText(result+" km");


                                memoHelper.close();
                                Toast.makeText(getApplicationContext(), "날짜를 선택했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
            }
        });
    }
}
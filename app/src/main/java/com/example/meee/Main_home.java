package com.example.meee;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Main_home extends AppCompatActivity {

    MemoDBHelper memoHelper;
    String selectDate;
    ListView listView;
    Button btn1_list, btn2_list;

    TextView tv1;
    CalendarView cv;
    RadioGroup rdg;
    RadioButton rbtn1, rbtn2, rbtn3, rbtn4;
    View dialogview;
    String fileName;
    int selectYear, selectMonth, selectDay;
    String Mmonth;

    Menu menu;
    MenuItem stats, tripdata;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        memoHelper = new MemoDBHelper(this);

//        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},MODE_PRIVATE);
//        final String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//        final File dir = new File(sdPath + "Pictures");
//        dir.mkdir();

        tv1 = findViewById(R.id.tv_calendar);
        cv = findViewById(R.id.calendarView);
        rdg = findViewById(R.id.radiogroup);
        rbtn1 = findViewById(R.id.rbtn_daily);
        rbtn2 = findViewById(R.id.rbtn_exercise);
        rbtn3 = findViewById(R.id.rbtn_goal);
        rbtn4 = findViewById(R.id.rbtn_travel);
        stats = findViewById(R.id.stats);
        tripdata = findViewById(R.id.tripdata);
        listView = (ListView) findViewById(R.id.listView1_main);
        btn1_list = findViewById(R.id.btn1_list);
        btn2_list = findViewById(R.id.btn2_list);

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                selectYear = year;
                selectMonth = month + 1;
                selectDay = dayOfMonth;
                if (selectMonth > 0 && selectMonth < 10){
                    Mmonth = "0" + selectMonth;
                }else{
                    Mmonth = String.valueOf(selectMonth);
                }
                selectDate = selectYear+"년"+Mmonth+"월"+selectDay+"일";

                // 해당 날짜게 기록된게 없으면 '카테고리' 뜨기,
                SQLiteDatabase db = memoHelper.getReadableDatabase();
                Cursor cursor1 = db.rawQuery("SELECT count(id) FROM memo WHERE date=?", new String[]{selectDate});
                String result = null;
                while(cursor1.moveToNext()){
                    result = cursor1.getString(0);
                }
                if (Integer.valueOf(result) == 0) {
                    onCategory();
                }else{ // 있으면 '리스트뷰' 뜨기
                    new android.app.AlertDialog.Builder(Main_home.this)
                            .setPositiveButton("작성하기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onCategory();
                                }
                            })
                            .setNeutralButton("기록보기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    displayList();
                                }
                            })
                            .show();
                }

//                fileName = Integer.toString(selectYear) + "년"
//                        + Integer.toString(selectMonth + 1) + "월"
//                        + Integer.toString(selectDay) + "일";
//                String Path =sdPath+"/Pictures/"+fileName;
//                File files = new File(Path);
//
//                //파일 있으면 리스트뷰로 띄우기 각각 다른 경로에서 가져와야함 픽쳐스 폴더에 아예 다 저장해버리는 것도 되긴함 파일 내용은 달라야함
//                if (files.exists()) {
//                    try {
//                        FileInputStream infile =new FileInputStream(Path);
//                        byte[] txt = new byte[infile.available()];
//                        infile.read(txt);
//                        String str = new String(txt);
//                    } catch (IOException e){
//                        e.printStackTrace();;
//                    }
//                    //파일 없는 경우 라디오버튼으로 카테고리 선택 후 해당 카테고리로 화면이동
//                }else {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.stats) {
            Intent homeIntent = new Intent(this, Stats.class);
            startActivity(homeIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    // '리스트뷰'
    @RequiresApi(api = Build.VERSION_CODES.O)
    void displayList() {
        MemoDBHelper memoHelper = new MemoDBHelper(this);
        SQLiteDatabase db = memoHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM memo WHERE date=?", new String[]{selectDate});
        ListViewAdapter adapter = new ListViewAdapter();
        while(cursor.moveToNext()) {
            adapter.addItemToList(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getBlob(5),
                    cursor.getBlob(6), cursor.getString(7));
        }
        listView.setAdapter(adapter);
    }

    // '카테고리' 대화창
    void onCategory() {
        dialogview=(View)View.inflate(Main_home.this, R.layout.dialog, null);
        rdg = dialogview.findViewById(R.id.radiogroup);
        rbtn1 = dialogview.findViewById(R.id.rbtn_daily);
        rdg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_daily:
                        intent = new Intent(getApplicationContext(), MemoDaily.class);
                        intent.putExtra("select1", Integer.toString(selectYear));
                        intent.putExtra("select2", Mmonth);
                        intent.putExtra("select3", Integer.toString(selectDay));
                        break;

                    case R.id.rbtn_exercise:
                        intent = new Intent(getApplicationContext(), MemoExercise.class);
                        intent.putExtra("select4", Integer.toString(selectYear));
                        intent.putExtra("select5", Mmonth);
                        intent.putExtra("select6", Integer.toString(selectDay));
                        break;

                    case R.id.rbtn_goal:
                        intent = new Intent(getApplicationContext(), MemoStudy.class);
                        intent.putExtra("select7", Integer.toString(selectYear));
                        intent.putExtra("select8", Mmonth);
                        intent.putExtra("select9", Integer.toString(selectDay));
                        break;

                    case R.id.rbtn_travel:
                        intent = new Intent(getApplicationContext(), MemoTrip.class);
                        intent.putExtra("select10", Integer.toString(selectYear));
                        intent.putExtra("select11", Mmonth);
                        intent.putExtra("select12", Integer.toString(selectDay));
                        break;
                }
                return;
            }
        });
        AlertDialog.Builder dlg = new AlertDialog.Builder(Main_home.this);
        dlg.setTitle("Select Categoty");
        dlg.setView(dialogview);


        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(intent);
            }
        });

        dlg.setNegativeButton("취소",null);
        dlg.show();
    }
}
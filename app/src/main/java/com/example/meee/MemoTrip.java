package com.example.meee;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MemoTrip extends AppCompatActivity {

    MemoDBHelper memoHelper;
    SQLiteDatabase sqlDB;
    String emotion;
    byte[] byteArrayCamera, byteArrayAlbum;

    private static final int REQ_CODE_SELECT_CAMERA = 100;
    private static final int REQ_CODE_SELECT_IMAGE = 200;
    private static final int GPS_ENABLE_REQUEST_CODE = 300;

    Bitmap bitmap, imageBitmap;
    Button btn1_t, btn2_t, btn3_t, btn4_t, btn5_t, album_btn1, album_btn2, album_btn3, album_btn4;
    RadioGroup rg_t;
    RadioButton rb1_t, rb2_t, rb3_t;
    EditText et1_t;
    TextView tv1_t, tv2_t, tv3_t;
    ImageView iv1_t, iv2_t, album_iv1;

    View albumdialog;
    Canvas canvas;
    Paint paint;
    Path path;

    Intent intent;
    String select10, select11, select12;

    String mode = "none";
    float upx = 0, upy = 0;
    static float width = 15F;
    static int color = Color.RED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_trip);
        ActivityCompat.requestPermissions(this, new String[]
                {android.Manifest.permission.CAMERA,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION}, MODE_PRIVATE);

        btn1_t = findViewById(R.id.btn1_t);
        btn2_t = findViewById(R.id.btn2_t);
        btn3_t = findViewById(R.id.btn3_t);
        btn4_t = findViewById(R.id.btn4_t);
        btn5_t = findViewById(R.id.btn5_t);
        album_btn1 = findViewById(R.id.album_btn1);
        album_btn2 = findViewById(R.id.album_btn2);
        album_btn3 = findViewById(R.id.album_btn3);
        album_btn4 = findViewById(R.id.album_btn4);
        rg_t = findViewById(R.id.rg_t);
        rb1_t = findViewById(R.id.rb1_t);
        rb2_t = findViewById(R.id.rb2_t);
        rb3_t = findViewById(R.id.rb3_t);
        et1_t = findViewById(R.id.et1_t);
        tv1_t = findViewById(R.id.tv1_t);
        tv2_t = findViewById(R.id.tv2_t);
        tv3_t = findViewById(R.id.tv3_t);
        iv1_t = findViewById(R.id.iv1_t);
        iv2_t = findViewById(R.id.iv2_t);
        album_iv1 = findViewById(R.id.album_iv1);

        // close
        btn1_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main_home.class);
                startActivity(intent);
            }
        });

        memoHelper = new MemoDBHelper(this);
        // 저장하기
        btn2_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues row;
                sqlDB = memoHelper.getWritableDatabase();
                row = new ContentValues();
                row.put("date", tv2_t.getText().toString());     // 날짜
                row.put("category", tv1_t.getText().toString()); // 카테고리
                row.put("content", et1_t.getText().toString());  // 메모내용
                row.put("camera", byteArrayCamera);             // 카메라 사진
                row.put("album", byteArrayAlbum);               // 앨범 사진
                row.put("address", tv3_t.getText().toString()); // 주소
                row.put("emotion", emotion);                    // 감정
                sqlDB.insert("memo", null, row);
                memoHelper.close();
                Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 앨범 아이콘 선택 시 대화창
        btn4_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumdialog = View.inflate(MemoTrip.this, R.layout.albumdialog, null);
                album_iv1 = albumdialog.findViewById(R.id.album_iv1);
                album_btn1 = albumdialog.findViewById(R.id.album_btn1);
                new AlertDialog.Builder(MemoTrip.this)
                        .setTitle("사진 선택")
                        .setIcon(R.drawable.photo)
                        .setView(albumdialog)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                iv1_t.setImageBitmap(imageBitmap); // 카메라 사진
                                iv2_t.setImageBitmap(bitmap); // 앨범 사진

                                ByteArrayOutputStream albumStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, albumStream);
                                byteArrayAlbum = albumStream.toByteArray();

                                Toast.makeText(getApplicationContext(), "사진을 선택했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "사진 선택을 취소했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        // 지도 보기
        btn5_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.meee.Map.class);
                startActivityForResult(intent, GPS_ENABLE_REQUEST_CODE);
            }
        });

        // "감정" 버튼 클릭 시 색상 변경
        rg_t.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1_t: // 좋음
                        rb1_t.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#002EFF")));
                        rb2_t.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        rb3_t.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        emotion = rb1_t.getText().toString();
                        break;
                    case R.id.rb2_t: // 중간
                        rb1_t.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        rb2_t.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#002EFF")));
                        rb3_t.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        emotion = rb2_t.getText().toString();
                        break;
                    case R.id.rb3_t: // 나쁨
                        rb1_t.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        rb2_t.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        rb3_t.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#002EFF")));
                        emotion = rb3_t.getText().toString();
                        break;
                }
                return;
            }
        });

        //캘린더날짜
        intent=getIntent();
        select10 = intent.getStringExtra("select10");
        select11 = intent.getStringExtra("select11");
        select12 = intent.getStringExtra("select12");
        tv2_t.setText(select10+"년"+select11+"월"+select12+"일");
    }

    // 카메라 열기
    public void onCamera(View v){
        switch (v.getId()){
            case R.id.btn3_t:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQ_CODE_SELECT_CAMERA);
                break;
        }
        return;
    }

    // 앨범 열기
    public void onOpenAlbum(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
    }

    // 앨범에서 선택 후 호출, 그림
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode) {
                case REQ_CODE_SELECT_CAMERA: // 카메라 선택
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    iv1_t.setImageBitmap(imageBitmap);

                    ByteArrayOutputStream cameraStream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, cameraStream);
                    byteArrayCamera = cameraStream.toByteArray();
                    break;
                case REQ_CODE_SELECT_IMAGE:  // 앨범 선택
                    try{
                        Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, album_iv1.getWidth(), album_iv1.getHeight(), false);

                        bitmap = bitmap2.copy(Bitmap.Config.ARGB_8888,true);

                        canvas = new Canvas(bitmap);
                        album_iv1.setImageBitmap(bitmap);

                        album_iv1.setOnTouchListener(new View.OnTouchListener() { // albumdialog의 iv4 에 그림그리기
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                float x = (float) motionEvent.getX();
                                float y = (float) motionEvent.getY();
                                int action = motionEvent.getAction();
                                switch (action){
                                    case MotionEvent.ACTION_DOWN:
                                        path.reset();
                                        path.moveTo(x, y);
                                        break;
                                    case MotionEvent.ACTION_MOVE:
                                        upx = motionEvent.getX();
                                        upy = motionEvent.getY();

                                        path.lineTo(x, y);
                                        canvas.drawPath(path, paint);
                                        album_iv1.invalidate();
                                        break;
                                    case MotionEvent.ACTION_UP:
                                        break;
                                    default:
                                        return false;
                                }
                                album_iv1.invalidate();
                                return true;
                            }
                        });
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    break;
                case GPS_ENABLE_REQUEST_CODE:
                    if (resultCode == RESULT_OK) {
                        tv3_t.setText(data.getStringExtra("address"));
                    }
                    break;
            }
        }else {
            return;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        initJob();
    }

    // 그림 그리기 기본 상태
    public void initJob(){
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15F);
        paint.setAntiAlias(true);
        path = new Path();
    }

    // Paint 변경
    public void onChangePaint(View v) {
        switch (v.getId()) {
            case R.id.album_btn2: // 파랑
                color = Color.BLUE;
                break;
            case R.id.album_btn3: // 가늘게
                width -= 5F;
                break;
            case R.id.album_btn4: // 원래대로
                color = Color.RED;
                width = 15F;
                break;
        }
        album_iv1.invalidate();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width);
        return;
    }
}
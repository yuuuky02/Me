package com.example.meee;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Memo_daily extends AppCompatActivity {

    private static final int REQ_CODE_SELECT_CAMERA = 100;
    private static final int REQ_CODE_SELECT_IMAGE = 200;
    private static final int GPS_ENABLE_REQUEST_CODE = 300;

    Bitmap bitmap, imageBitmap;
    Button btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10;
    RadioGroup rg;
    RadioButton rb, rb2, rb3;
    EditText et1;
    TextView tv25, maptv;
    ImageView iv2, iv3, iv4;

    View albumdialog;
    Canvas canvas;
    Paint paint;
    Path path;

    String mode = "none";
    float upx = 0, upy = 0;
    static float width = 15F;
    static int color = Color.RED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_daily);
        ActivityCompat.requestPermissions(this, new String[]
                {android.Manifest.permission.CAMERA,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION}, MODE_PRIVATE);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn5 = findViewById(R.id.button5);
        btn6 = findViewById(R.id.button6);
        btn7 = findViewById(R.id.button7);
        btn8 = findViewById(R.id.button8);
        btn9 = findViewById(R.id.button9);
        btn10 = findViewById(R.id.button10);
        rg = findViewById(R.id.radioGroup);
        rb = findViewById(R.id.radioButton);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);
        et1 = findViewById(R.id.editText1);
        tv25 = findViewById(R.id.textView25);
        iv2 = findViewById(R.id.imageView2);
        iv3 = findViewById(R.id.imageView3);
        iv4 = findViewById(R.id.imageView4);
        maptv = findViewById(R.id.textView13_d);

        // close
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // 저장하기
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // 앨범 아이콘 선택 시 대화창
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumdialog = View.inflate(Memo_daily.this, R.layout.albumdialog, null);
                iv4 = albumdialog.findViewById(R.id.imageView4);
                btn7 = albumdialog.findViewById(R.id.button7);
                new AlertDialog.Builder(Memo_daily.this)
                        .setTitle("사진 선택")
                        .setIcon(R.drawable.photo)
                        .setView(albumdialog)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                iv2.setImageBitmap(imageBitmap); // 카메라 사진
                                iv3.setImageBitmap(bitmap); // 앨범 사진
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
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), com.example.meee.Map.class);
                startActivityForResult(intent, GPS_ENABLE_REQUEST_CODE);
            }
        });

        // "감정" 버튼 클릭 시 색상 변경
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton: // 좋음
                        rb.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#002EFF")));
                        rb2.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        rb3.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        break;
                    case R.id.radioButton2: // 중간
                        rb.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        rb2.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#002EFF")));
                        rb3.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        break;
                    case R.id.radioButton3: // 나쁨
                        rb.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        rb2.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        rb3.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#002EFF")));
                        break;
                }
                return;
            }
        });
    }

    // 카메라 열기
    public void onCamera(View v){
        switch (v.getId()){
            case R.id.button4:
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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_SELECT_CAMERA:
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    iv2.setImageBitmap(imageBitmap);
                    break;
                case REQ_CODE_SELECT_IMAGE:
                    try{
                        Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, iv4.getWidth(), iv4.getHeight(), false);

                        bitmap = bitmap2.copy(Bitmap.Config.ARGB_8888,true);

                        canvas = new Canvas(bitmap);
                        iv4.setImageBitmap(bitmap);

                        iv4.setOnTouchListener(new View.OnTouchListener() { // albumdialog의 iv4 에 그림그리기
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
                                        iv4.invalidate();
                                        break;
                                    case MotionEvent.ACTION_UP:
                                        break;
                                    default:
                                        return false;
                                }
                                iv4.invalidate();
                                return true;
                            }
                        });
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    break;
                case GPS_ENABLE_REQUEST_CODE:
                    if (resultCode == RESULT_OK) {
                        maptv.setText(data.getStringExtra("address"));
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
            case R.id.button8: // 파랑
                color = Color.BLUE;
                break;
            case R.id.button9: // 가늘게
                width -= 5F;
                break;
            case R.id.button10: // 원래대로
                color = Color.RED;
                width = 15F;
                break;
        }
        iv4.invalidate();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width);
        return;
    }
}
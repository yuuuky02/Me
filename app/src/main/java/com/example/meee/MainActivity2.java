package com.example.meee;

import static com.example.meee.R.drawable.neutral;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MainActivity2 extends AppCompatActivity {

    private static final int REQ_CODE_SELECT_CAMERA = 100;
    private static final int REQ_CODE_SELECT_IMAGE = 200;

    Bitmap bitmap, imageBitmap;
    Button btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10;
    RadioGroup rg;
    RadioButton rb, rb2, rb3;
    EditText et1;
    TextView tv25;
    ImageView iv2, iv3, iv4, iv5;

    View albumdialog, showdialog;
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
        setContentView(R.layout.activity_main2);
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
        iv5 = findViewById(R.id.imageView5);

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
                albumdialog = View.inflate(MainActivity2.this, R.layout.albumdialog, null);
                iv3 = albumdialog.findViewById(R.id.imageView3);
                btn7 = albumdialog.findViewById(R.id.button7);
                new AlertDialog.Builder(MainActivity2.this)
                        .setTitle("사진 선택")
                        .setIcon(R.drawable.photo)
                        .setView(albumdialog)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(),"사진을 선택했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
            }
        });

        // 지도 보기
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
                startActivity(intent);
            }
        });

        // 사진 보기
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialog = View.inflate(MainActivity2.this, R.layout.showdialog, null);
                iv4 = showdialog.findViewById(R.id.imageView4);
                iv5 = showdialog.findViewById(R.id.imageView5);
                iv4.setImageBitmap(imageBitmap);
                iv5.setImageBitmap(bitmap);
                new AlertDialog.Builder(MainActivity2.this)
                        .setTitle("사진 보기")
                        .setIcon(R.drawable.photo)
                        .setView(showdialog)
                        .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(), "사진 보기를 했습니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
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
            if(requestCode == REQ_CODE_SELECT_CAMERA){ // 카메라 선택
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                iv2.setImageBitmap(imageBitmap);
            }
            else if (requestCode == REQ_CODE_SELECT_IMAGE){ // 앨범 선택
                try{
                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, iv3.getWidth(), iv3.getHeight(), false);

                    bitmap = bitmap2.copy(Bitmap.Config.ARGB_8888,true);

                    canvas = new Canvas(bitmap);
                    iv3.setImageBitmap(bitmap);
                    iv2.setImageBitmap(bitmap);
                    iv3.setOnTouchListener(new View.OnTouchListener() { // iv3 에 그림그리기
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            float x = motionEvent.getX();
                            float y = motionEvent.getY();
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
                                    iv3.invalidate();
                                    break;
                            }
                            return true;
                        }
                    });
                }catch(IOException e){
                    e.printStackTrace();
                }
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
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width);
        return;
    }
}
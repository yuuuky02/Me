package com.example.meee;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity2 extends AppCompatActivity {

    private static final int REQ_CODE_SELECT_IMAGE = 100;

    Bitmap bitmap;
    Button btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12;
    EditText et1;
    TextView tv25;
    ImageView iv2, iv3, iv4;

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
        btn11 = findViewById(R.id.button11);
        btn12 = findViewById(R.id.button12);
        et1 = findViewById(R.id.editText1);
        tv25 = findViewById(R.id.textView25);
        iv2 = findViewById(R.id.imageView2);
        iv3 = findViewById(R.id.imageView3);
        iv4 = findViewById(R.id.imageView4);

        // 앨범
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumdialog = View.inflate(MainActivity2.this, R.layout.albumdialog, null);
                iv3 = albumdialog.findViewById(R.id.imageView3);
                btn10 = albumdialog.findViewById(R.id.button10);
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

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 사진 보기
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialog = View.inflate(MainActivity2.this, R.layout.showdialog, null);
                iv4 = showdialog.findViewById(R.id.imageView4);
                iv4.setImageBitmap(bitmap);
                new AlertDialog.Builder(MainActivity2.this)
                        .setTitle("사진 보기")
                        .setIcon(R.drawable.photo)
                        .setView(showdialog)
                        .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(),"사진 보기를 했습니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
    }

    public void initJob(){
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15F);

        path = new Path();
    }

    // 앨범 열기
    public void onOpenAlbum(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        initJob();
    }

    // 앨범에서 선택 후 호출
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode == REQ_CODE_SELECT_IMAGE){
                try{
                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, iv3.getWidth(), iv3.getHeight(), false);

                    bitmap = bitmap2.copy(Bitmap.Config.ARGB_8888,true);

                    canvas = new Canvas(bitmap);
                    iv3.setImageBitmap(bitmap);
                    iv2.setImageBitmap(bitmap);
                    iv3.setOnTouchListener(new View.OnTouchListener() {
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

    // 색상 변경 : 파랑, 굵기 변경 : 가늘게
    public void onChangePaint(View v) {
        switch (v.getId()) {
            case R.id.button11:
                color = Color.BLUE;
                break;
            case R.id.button12:
                width -= 5F;
                break;
            case R.id.button13:
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
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

public class MemoExercise extends AppCompatActivity {

    MemoDBHelper memoHelper;
    SQLiteDatabase sqlDB;
    String emotion;
    byte[] byteArrayCamera, byteArrayAlbum;

    private static final int REQ_CODE_SELECT_CAMERA = 100;
    private static final int REQ_CODE_SELECT_IMAGE = 200;
    private static final int GPS_ENABLE_REQUEST_CODE = 300;

    Bitmap bitmap, imageBitmap;
    Button btn1_e, btn2_e, btn3_e, btn4_e, btn5_e, album_btn1, album_btn2, album_btn3, album_btn4;
    RadioGroup rg_e;
    RadioButton rb1_e, rb2_e, rb3_e;
    EditText et1_e, et2_e, et3_e;
    TextView tv1_e, tv2_e, tv3_e;
    ImageView iv1_e, iv2_e, album_iv1;

    View albumdialog;
    Canvas canvas;
    Paint paint;
    Path path;

    Intent intent;
    String select4, select5, select6;

    String mode = "none";
    float upx = 0, upy = 0;
    static float width = 15F;
    static int color = Color.RED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_exercise);
        ActivityCompat.requestPermissions(this, new String[]
                {android.Manifest.permission.CAMERA,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION}, MODE_PRIVATE);

        btn1_e = findViewById(R.id.btn1_e);
        btn2_e = findViewById(R.id.btn2_e);
        btn3_e = findViewById(R.id.btn3_e);
        btn4_e = findViewById(R.id.btn4_e);
        btn5_e = findViewById(R.id.btn5_e);
        album_btn1 = findViewById(R.id.album_btn1);
        album_btn2 = findViewById(R.id.album_btn2);
        album_btn3 = findViewById(R.id.album_btn3);
        album_btn4 = findViewById(R.id.album_btn4);
        rg_e = findViewById(R.id.rg_e);
        rb1_e = findViewById(R.id.rb1_e);
        rb2_e = findViewById(R.id.rb2_e);
        rb3_e = findViewById(R.id.rb3_e);
        et1_e = findViewById(R.id.et1_e);
        et2_e = findViewById(R.id.et2_e);
        et3_e = findViewById(R.id.et3_e);
        tv1_e = findViewById(R.id.tv1_e);
        tv2_e = findViewById(R.id.tv2_e);
        tv3_e = findViewById(R.id.tv3_e);
        iv1_e = findViewById(R.id.iv1_e);
        iv2_e = findViewById(R.id.iv2_e);
        album_iv1 = findViewById(R.id.album_iv1);

        // close
        btn1_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main_home.class);
                startActivity(intent);
            }
        });

        memoHelper = new MemoDBHelper(this);
        // ????????????
        btn2_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues row, exerciserow;
                sqlDB = memoHelper.getWritableDatabase();
                row = new ContentValues();
                exerciserow = new ContentValues();
                row.put("date", tv2_e.getText().toString()); // ??????
                row.put("category", tv1_e.getText().toString()); // ????????????
                row.put("content", et1_e.getText().toString()); // ????????????
                row.put("camera", byteArrayCamera); // ????????? ??????
                row.put("album", byteArrayAlbum); // ?????? ??????
                row.put("address", tv3_e.getText().toString()); // ??????
                row.put("emotion", emotion); // ??????
                exerciserow.put("date", tv2_e.getText().toString());     // ??????
                exerciserow.put("etime", Integer.valueOf(et2_e.getText().toString())); // ????????????
                exerciserow.put("edistance", Integer.valueOf(et3_e.getText().toString())); // ????????????
                sqlDB.insert("memo", null, row);
                sqlDB.insert("exercise", null, exerciserow);
                memoHelper.close();
                Toast.makeText(getApplicationContext(), "?????????????????????.", Toast.LENGTH_SHORT).show();
            }
        });

        // ?????? ????????? ?????? ??? ?????????
        btn4_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumdialog = View.inflate(MemoExercise.this, R.layout.albumdialog, null);
                album_iv1 = albumdialog.findViewById(R.id.album_iv1);
                album_btn1 = albumdialog.findViewById(R.id.album_btn1);
                new AlertDialog.Builder(MemoExercise.this)
                        .setTitle("?????? ??????")
                        .setIcon(R.drawable.photo)
                        .setView(albumdialog)
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                iv1_e.setImageBitmap(imageBitmap); // ????????? ??????
                                iv2_e.setImageBitmap(bitmap); // ?????? ??????

                                ByteArrayOutputStream albumStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, albumStream);
                                byteArrayAlbum = albumStream.toByteArray();

                                Toast.makeText(getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        // ?????? ??????
        btn5_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.meee.Map.class);
                startActivityForResult(intent, GPS_ENABLE_REQUEST_CODE);
            }
        });

        // "??????" ?????? ?????? ??? ?????? ??????
        rg_e.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1_e: // ??????
                        rb1_e.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#002EFF")));
                        rb2_e.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        rb3_e.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        emotion = rb1_e.getText().toString();
                        break;
                    case R.id.rb2_e: // ??????
                        rb1_e.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        rb2_e.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#002EFF")));
                        rb3_e.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        emotion = rb2_e.getText().toString();
                        break;
                    case R.id.rb3_e: // ??????
                        rb1_e.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        rb2_e.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        rb3_e.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#002EFF")));
                        emotion = rb3_e.getText().toString();
                        break;
                }
                return;
            }
        });

        //???????????????
        intent=getIntent();
        select4 = intent.getStringExtra("select4");
        select5 = intent.getStringExtra("select5");
        select6 = intent.getStringExtra("select6");
        tv2_e.setText(select4+"???"+select5+"???"+select6+"???");
    }

    // ????????? ??????
    public void onCamera(View v){
        switch (v.getId()){
            case R.id.btn3_e:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQ_CODE_SELECT_CAMERA);
                break;
        }
        return;
    }

    // ?????? ??????
    public void onOpenAlbum(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
    }

    // ???????????? ?????? ??? ??????, ??????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode) {
                case REQ_CODE_SELECT_CAMERA: // ????????? ??????
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    iv1_e.setImageBitmap(imageBitmap);

                    ByteArrayOutputStream cameraStream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, cameraStream);
                    byteArrayCamera = cameraStream.toByteArray();
                    break;
                case REQ_CODE_SELECT_IMAGE:  // ?????? ??????
                    try{
                        Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, album_iv1.getWidth(), album_iv1.getHeight(), false);

                        bitmap = bitmap2.copy(Bitmap.Config.ARGB_8888,true);

                        canvas = new Canvas(bitmap);
                        album_iv1.setImageBitmap(bitmap);

                        album_iv1.setOnTouchListener(new View.OnTouchListener() { // albumdialog??? iv4 ??? ???????????????
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
                        tv3_e.setText(data.getStringExtra("address"));
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

    // ?????? ????????? ?????? ??????
    public void initJob(){
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15F);
        paint.setAntiAlias(true);
        path = new Path();
    }

    // Paint ??????
    public void onChangePaint(View v) {
        switch (v.getId()) {
            case R.id.album_btn2: // ??????
                color = Color.BLUE;
                break;
            case R.id.album_btn3: // ?????????
                width -= 5F;
                break;
            case R.id.album_btn4: // ????????????
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
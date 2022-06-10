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

    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION};

    private static final int REQ_CODE_SELECT_CAMERA = 100;
    private static final int REQ_CODE_SELECT_IMAGE = 200;
    private static final int REQ_CODE_MEMO = 300;

    Bitmap bitmap, imageBitmap;
    Button btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10;
    RadioGroup rg;
    RadioButton rb, rb2, rb3;
    EditText et1;
    TextView tv25, maptv;
    ImageView iv2, iv3, iv4, iv5;

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
                {android.Manifest.permission.CAMERA}, MODE_PRIVATE);
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
        maptv = findViewById(R.id.textView13_d);

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            checkRunTimePermission();
        }

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
                startActivityForResult(intent, REQ_CODE_MEMO);

//                gpsTracker = new GpsTracker(Memo_daily.this);
//
//                double latitude = gpsTracker.getLatitude();
//                double longitude = gpsTracker.getLongitude();
//
//                String address = getCurrentAddress(latitude, longitude);
//                maptv.setText(address);
//
//                Toast.makeText(Memo_daily.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
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
                case REQ_CODE_MEMO:
                    break;
                case GPS_ENABLE_REQUEST_CODE:
                    if (checkLocationServicesStatus()) {
                        if (checkLocationServicesStatus()) {

                            Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                            checkRunTimePermission();
                            return;
                        }
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


    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                //위치 값을 가져올 수 있음
                ;
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(Memo_daily.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                } else {

                    Toast.makeText(Memo_daily.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(Memo_daily.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(Memo_daily.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(Memo_daily.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(Memo_daily.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(Memo_daily.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(Memo_daily.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Memo_daily.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
package com.example.meee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Trip_Tour extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap gMap;
    MapFragment mapFragment;
    GroundOverlayOptions videoMark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_tour);
        setTitle("버스노선");
        mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.tour_map);
        mapFragment.getMapAsync(this);

        String serviceUrl;
        String serviceKey;
        String strSrch;
        String strUrl;
        serviceUrl = "http://apis.data.go.kr/1360000/TourStnInfoService";
        serviceKey = "JLViv85HvCs%2Bpo2L1eVU3PqyNV%2BRKBZaM14VO9qYy6nd2hAigcZ8RXuALUEb0b6YN4v3K9j1GCgXQbnj%2FdkZCA%3D%3D";
        String COURSE_ID = "100100223";
        strUrl = serviceUrl+"?ServiceKey="+serviceKey+"&COURSE_ID="+COURSE_ID;

        new DownloadWebpageTask().execute(strUrl);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.568256,126.897240),15));
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                videoMark = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(
                        R.drawable.camera)).position(latLng,100f,100f);
                gMap.addGroundOverlay(videoMark);
            }
        });
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return (String)downloadUrl((String)urls[0]);
            } catch (IOException e) {
                return "다운로드 실패";
            }
        }

        protected void onPostExecute(String result) {
            displayBuspos(result);
        }

        private String downloadUrl(String myurl) throws IOException {

            HttpURLConnection conn = null;
            BufferedReader br = null;
            String page = "";
            try {
                URL url = new URL(myurl);
                conn = (HttpURLConnection) url.openConnection();

                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                String line = null;

                while ((line = br.readLine()) != null) {
                    page += line;
                }
                return page;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }
            return page;
        }

        private void displayBuspos(String result) {
            String plainNo = "";
            String gpsX    = "";
            String gpsY    = "";
            boolean bSet_plainNo = false;
            boolean bSet_gpsX    = false;
            boolean bSet_gpsY    = false;

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new StringReader(result));
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if(eventType == XmlPullParser.START_DOCUMENT) {
                        ;
                    } else if(eventType == XmlPullParser.START_TAG) {
                        String tag_name = xpp.getName();
                        if (tag_name.equals("plainNo"))
                            bSet_plainNo = true;
                        if (tag_name.equals("gpsX"))
                            bSet_gpsX = true;
                        if (tag_name.equals("gpsY"))
                            bSet_gpsY = true;
                    } else if(eventType == XmlPullParser.TEXT) {
                        if (bSet_gpsX) {
                            gpsX = xpp.getText();
                            bSet_gpsX = false;
                        }
                        if (bSet_gpsY) {
                            gpsY = xpp.getText();
                            bSet_gpsY = false;
                        }
                        if (bSet_plainNo) {
                            plainNo = xpp.getText();
                            bSet_plainNo = false;

                            displayMap(gpsX, gpsY, plainNo);
                        }

                    } else if(eventType == XmlPullParser.END_TAG) {
                        ;
                    }
                    eventType = xpp.next();
                }

            } catch (Exception e) {
                ;
            }
        }

        private void displayMap(String gpsX, String gpsY, String plainNo) {
            double latitude= Double.parseDouble(gpsY);
            double longitude  = Double.parseDouble(gpsX);
            final LatLng LOC = new LatLng(latitude, longitude);

            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LOC, 15));
            Marker mk = gMap.addMarker(new MarkerOptions()
                    .position(LOC)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus))
                    .title(plainNo));
            mk.showInfoWindow();
        }
    }
}


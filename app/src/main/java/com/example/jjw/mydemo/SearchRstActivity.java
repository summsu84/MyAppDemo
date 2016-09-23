package com.example.jjw.mydemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jjw.mydemo.lib.DirectionFinder;
import com.example.jjw.mydemo.lib.DirectionFinderListener;
import com.example.jjw.mydemo.lib.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SearchRstActivity  extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {


    private static final Object DIV_VALUE =  10000000 ;
    /* Google Map */
    private GoogleMap mMap;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    /* Src and Dest */
    private String start;
    private String dest;

    private Button mSearchRstPlan;

    ListView listView;
    ArrayList<PlaceInfo> items;     //전체 Place 정보
    ArrayList<PlaceInfo> mShowItems;        //반경에 들어오는 보여지는 Pace.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_rst);
        System.out.println("----------------OnCreate-------------");

        // Initializing
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Burndle
        Bundle initBurndle = getIntent().getExtras();
        //아래 부분 체크 필요 getExtras
        if(initBurndle != null)
        {
            this.start = initBurndle.getString("origin");
            this.dest = initBurndle.getString("destination");
        }


        listView = (ListView)this.findViewById(R.id.SearchRstListView);
        items = (ArrayList<PlaceInfo>)getIntent().getSerializableExtra("placeList");
        mShowItems = new ArrayList<PlaceInfo>();

        //CustomAdapter Class
/*        final CustomAdapter adapter = new CustomAdapter(this, 0, items);
        listView.setAdapter(adapter);*/

        //Button Event
        Button mSearchRstPlan = (Button) findViewById(R.id.btnSearchRstPlan);
        mSearchRstPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<PlaceInfo> checkedPlace = new ArrayList<PlaceInfo>();


                for(int i = 0 ; i < items.size() ; i++)
                {
                    PlaceInfo info = items.get(i);
                    if(info.isChecked() == true)
                    {
                        checkedPlace.add(info);
                    }
                }
                if(checkedPlace.size() > 0) {
                    Intent intent2 = new Intent(getApplicationContext(), PlanStsActivity.class);
                    intent2.putExtra("checkedPlaceList", checkedPlace);
                    startActivity(intent2);
                }

            }
        });


        //Test
        //시청
        double cLat = 37.566679;
        double cLon = 126.978430;
        double tLat = 37.578193;
        double tLon = 126.982338;

        test(cLat, cLon,tLat, tLon);
    }


    private void sendRequest() {
        String origin = this.start;
        String destination = this.dest;
        System.out.println("----------------SendRequest-------------origin : " + origin + ", dest : " + destination);
        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    /**
     * 구글로부터 받아온 루트 정보를 처리한다..
     * @param routes
     */
    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            //((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
           // ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }

        //20160922 - JJW 해당 경로 지점을 체크한다.
        for (Route route : routes) {
            System.out.println("------Route start location : " + route.startLocation + ", end location : " + route.endLocation);
            LatLng startLocation = route.startLocation;
            LatLng endtLocation = route.endLocation;
           // checkCoveredPlace(startLocation);
            checkCoveredPlace(endtLocation);
        }

        final CustomAdapter adapter = new CustomAdapter(this, 0, mShowItems);
        listView.setAdapter(adapter);

        for(PlaceInfo info : mShowItems)
        {
            // Creating MarkerOptions
            MarkerOptions options = new MarkerOptions();
            // Setting the position of the marker
            LatLng latlng = new LatLng(Double.parseDouble(info.getLat()), Double.parseDouble(info.getLon()));
            options.position(latlng);
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            mMap.addMarker(options);
        }

    }

    public void checkCoveredPlace(LatLng cLatLon)
    {
        //기준 좌표
        double cLat = cLatLon.latitude;
        double cLon = cLatLon.longitude;

        for(PlaceInfo info : items)
        {
            double dist = test(cLat, cLon, Double.parseDouble(info.getLat()), Double.parseDouble(info.getLon()));
            //1.5 km 이내인지 검색한다..
            if(dist <= 1)
            {
                //반경에 들어온것으로 체크한다.
                mShowItems.add(info);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("----------------OnMapReady-------------");
        mMap = googleMap;
        LatLng hcmus = new LatLng(37.566679, 126.978430);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("TEST")
                .position(hcmus)));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        sendRequest();
    }

    //TEST

    public int distanceCmp( int lat1, // 기준 위도,
                     int lon1, // 기준 경도,
                     int lat2,  // 대상 위도,
                     int lon2,  // 대상 경도
                     int nCmpLat, // 위도간 거리
                     int nCmpLon // 경도간 거리
    )
    {
/*
위도,경도에 대한 절대값 계산
*/
        double lon = lon1 > lon2  ? (lon1 - lon2)/(double)DIV_VALUE   : (lon2-lon1)/(double)DIV_VALUE;
        double lat =  lat1 > lat2  ? (lat1 - lat2)/(double)DIV_VALUE   : (lat2-lat1)/(double)DIV_VALUE;
 
/*
경도에 대한 도분초및 거리 계산
*/
        int rad = (int)lon;
        int min = (int) ((lon-rad)*60);
        double sec = ((lon-rad)*60 - min)*60;
        int lon_dist, lat_dist;
        lon_dist = (int)((rad * 88.8) + (min*1.48) + (sec*0.025)) * 1000; // m단위
 
/*
위도에 대한 도분초및 거리 계산
*/
        rad = (int)lat;
        min = (int) ((lat-rad)*60);
        sec = ((lat-rad)*60 - min)*60;
        lat_dist = (int) ((rad * 111) + (min*1.85) + (sec*0.031)) * 1000; // m단위

        if( nCmpLat == 0 ){ // 원 형태의 구역반경
            // 직선거리만을 조건으로 한다.
            int realDist = (int) Math.sqrt((lon_dist*lon_dist)+(lat_dist*lat_dist));
            if( nCmpLon >= realDist ){
                return 1;
            }
        }else if( nCmpLat >= lat_dist && nCmpLon >= lon_dist ){ // 사각 형태의 구역반경
// 종/횡측 거리안에 들어오는지 확인한다.
            return 1;
        }

        return 0;
    }

    public double test(double cLat, double cLon, double tLat, double tLon)
    {
        double dist = (6371*Math.acos(Math.cos(Math.toRadians(cLat))* Math.cos(Math.toRadians(tLat))* Math.cos(Math.toRadians(tLon)

                - Math.toRadians(cLon))+ Math.sin(Math.toRadians(cLat)) * Math.sin(Math.toRadians(tLat))));

        System.out.println("-------------distance : " + dist);      //1km 이면 1을 리턴..
        return dist;
    }

    private class CustomAdapter extends ArrayAdapter<PlaceInfo> implements View.OnClickListener
    {
        private ArrayList<PlaceInfo> items;
        private Context mContext;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<PlaceInfo> objects) {
            super(context, textViewResourceId, objects);

            this.items = objects;
            this.mContext = context;
        }

        //우선 하드코딩으로 하기..
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View v = convertView;

            if(v == null)
            {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.custom_lit_item, null);
            }
            v.setTag(position);
            v.setOnClickListener(this);
            //Image Instacne
            ImageView imageView = (ImageView)v.findViewById(R.id.imageView);

            //리스트뷰의 아이템에 이미지를 변경한다.

            PlaceInfo info = items.get(position);

            String packName = getPackageName(); // 패키지명
            String resName = info.getImageSrc();
            int resID = getResources().getIdentifier(resName, "drawable", packName);


            imageView.setImageResource(resID);
            imageView.setMaxHeight(80);
            imageView.setMaxWidth(80);
            TextView textView = (TextView)v.findViewById(R.id.textView);
            textView.setText(info.getName());
            CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkTest);
            checkBox.setTag(position);
            checkBox.setOnClickListener(buttonClickListener);

            return v;
        }

        //CheckBox클릭시 이벤트 설정
        private View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                switch (v.getId()) {

                    // 이미지 클릭
                    case R.id.checkTest:
                        CheckBox tmp = (CheckBox)v.findViewById(R.id.checkTest);
                        System.out.println("------------Checkbox clicekd..status : " + tmp.isChecked() + ", position : " +position);
                        items.get(position).setChecked(tmp.isChecked());
                        break;
                }
            }
        };

        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();

            PlaceInfo info = items.get(position);
            if(v.getId() == R.id.checkTest)
            {
                CheckBox tmp = (CheckBox)v.findViewById(R.id.checkTest);
                System.out.println("Checkbox is clicked.. position : " + position + ", checked : " + tmp.isChecked());
                info.setChecked(tmp.isChecked());
            }else {
                //Bundle 생성
                Bundle extras = new Bundle();
                extras.putString("city", info.getName());
                extras.putString("desc", info.getDescription());
                extras.putString("imgSrc", info.getImageSrc());


                // 인텐트를 생성한다.
                // 컨텍스트로 현재 액티비티를, 생성할 액티비티로 ItemClickExampleNextActivity 를 지정한다.
                Intent intent = new Intent(mContext, SearchDetActivity.class);

                // 위에서 만든 Bundle을 인텐트에 넣는다.
                intent.putExtras(extras);

                // 액티비티를 생성한다.
                mContext.startActivity(intent);
            }
        }
    }
}

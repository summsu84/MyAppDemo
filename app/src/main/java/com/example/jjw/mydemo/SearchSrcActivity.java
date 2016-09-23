package com.example.jjw.mydemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class SearchSrcActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {
    private GoogleMap mMap;
    private Button btnFindPath;
    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    ArrayList<LatLng> markerPoints;
    ArrayList<PlaceInfo> mPlaceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("----------onCreate----------");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_src);
        // Initializing
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mPlaceList = (ArrayList<PlaceInfo>)getIntent().getSerializableExtra("placeList");
        markerPoints = new ArrayList<LatLng>();

        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        etOrigin = (EditText) findViewById(R.id.etOrigin);
        etDestination = (EditText) findViewById(R.id.etDestination);

        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }
    private void sendRequest() {
        String origin = etOrigin.getText().toString();
        String destination = etDestination.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        /* 위에 항목을체크하고, 나머지는 Search Rst Acitivity 에서 처리한다 */

        Bundle extras = new Bundle();
        extras.putString("origin", origin);
        extras.putString("destination", destination);
        Intent intent = new Intent(this, SearchRstActivity.class);

        // 위에서 만든 Bundle을 인텐트에 넣는다.
        intent.putExtras(extras);
        intent.putExtra("placeList",mPlaceList);

        // 액티비티를 생성한다.
        this.startActivity(intent);
        /* 주석처리
        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng hcmus = new LatLng(37.566679, 126.978430);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("TEST")
                .position(hcmus)));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        // Setting onclick event listener for the map -Test
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                System.out.println("----------onMapClick...point :" + point);
                // Already two locations
                if (markerPoints.size() > 1) {
                    markerPoints.clear();
                    mMap.clear();
                    etOrigin.setText("");
                    etDestination.setText("");
                }

                // Adding new item to the ArrayList
                markerPoints.add(point);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(point);

                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.
                 */
                if (markerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    String strOrigin = markerPoints.get(0).latitude + "," + markerPoints.get(0).longitude;
                    etOrigin.setText(strOrigin);
                } else if (markerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    String strDest = markerPoints.get(1).latitude + "," + markerPoints.get(1).longitude;
                    etDestination.setText(strDest);
                }


                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (markerPoints.size() >= 2) {
                    LatLng origin = markerPoints.get(0);
                    LatLng dest = markerPoints.get(1);
                    // Getting URL to the Google Directions API
                    //String url = getDirectionsUrl(origin, dest);

                    //System.out.println("Url : " + url);
                    //DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                   // downloadTask.execute(url);
                }

            }
        });

    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
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

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

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
    }

}

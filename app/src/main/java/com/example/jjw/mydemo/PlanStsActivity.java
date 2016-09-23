package com.example.jjw.mydemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class PlanStsActivity extends AppCompatActivity{

    ListView mPlanStstListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_sts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPlanStstListView = (ListView)this.findViewById(R.id.PlanStsListView);
        ArrayList<PlaceInfo> tmp = (ArrayList<PlaceInfo>)getIntent().getSerializableExtra("checkedPlaceList");
        //CustomAdapter Class
        CheckedPlaceListAdapter adapter = new CheckedPlaceListAdapter(this, 0, tmp);
        mPlanStstListView.setAdapter(adapter);

    }


    private class CheckedPlaceListAdapter extends ArrayAdapter<PlaceInfo> implements View.OnClickListener
    {
        private ArrayList<PlaceInfo> items;
        private Context mContext;

        public CheckedPlaceListAdapter(Context context, int textViewResourceId, ArrayList<PlaceInfo> objects) {
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
            textView.setText(info.getIntro());
            CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkTest);

            return v;
        }

        @Override
        public void onClick(View v) {

        }
    }

}

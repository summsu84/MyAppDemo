package com.example.jjw.mydemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int MAIN_CONTENT_NUM = 5;

    private GoogleApiClient client;
    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    Typeface typeRe;

    ArrayList<PlaceInfo> mPlaceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        mPlaceList = new ArrayList<PlaceInfo>();
        Intent initIntent = getIntent();
        //아래 부분 체크 필요 getExtras
        if(initIntent != null)
        {
            try {
                boolean isLogin = initIntent.getExtras().getBoolean("login");
                TextView tmp = (TextView) findViewById(R.id.btnMainIntLogin);
                tmp.setText("위치등록");
            }catch(NullPointerException e){
                e.printStackTrace();
            }
        }

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        String[] test = {"Chicken & Beer", "Pa-jeon&Mak-Geol-Li", "Whole Chicken with noodles", "Flour based food", "Pork belly","Bulgogi", "Naeng Myeon", "Dak-galbi", "Bibimbap", "Pat bing su" };

        //리스트 뷰 및 리스트 뷰 아답터 생성  ==> 추후 디비에서 받아올수 있도록 수정하기..
        mListView = (ListView) findViewById(R.id.mList);
        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);

        for(int i=0; i<MAIN_CONTENT_NUM; i++) {
            mAdapter.addItem(test[i]);
        }
        

        setListViewHeightBasedOnChildren(mListView);
        initPlaceInfo();
    }

    //Place xml 파일을 읽어 들인다.
    void initPlaceInfo()
    {
        System.out.println("InitPlaceInfo");
        try{
            XmlPullParser parser = getResources().getXml(R.xml.place_list);

            System.out.println("Xml Parser Start..");
            while(parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if(parser.getEventType() == XmlPullParser.START_TAG && "place".equals(parser.getName())) {

                    parser.nextTag();
                    parser.require(XmlPullParser.START_TAG, null, "name");
                    String name = parser.nextText();
                    parser.require(XmlPullParser.END_TAG, null, "name");

                    parser.nextTag();
                    parser.require(XmlPullParser.START_TAG, null, "intro");
                    String intro = parser.nextText();
                    parser.require(XmlPullParser.END_TAG, null, "intro");

                    parser.nextTag();
                    parser.require(XmlPullParser.START_TAG, null, "description");
                    String description = parser.nextText();
                    parser.require(XmlPullParser.END_TAG, null, "description");

                    parser.nextTag();
                    parser.require(XmlPullParser.START_TAG, null, "lat");
                    String lat = parser.nextText();
                    parser.require(XmlPullParser.END_TAG, null, "lat");

                    parser.nextTag();
                    parser.require(XmlPullParser.START_TAG, null, "lon");
                    String lon = parser.nextText();
                    parser.require(XmlPullParser.END_TAG, null, "lon");

                    parser.nextTag();
                    parser.require(XmlPullParser.START_TAG, null, "rate");
                    String rate = parser.nextText();
                    parser.require(XmlPullParser.END_TAG, null, "rate");

                    parser.nextTag();
                    parser.require(XmlPullParser.START_TAG, null, "imgSrc");
                    String imgSrc = parser.nextText();
                    parser.require(XmlPullParser.END_TAG, null, "imgSrc");


                    mPlaceList.add(new PlaceInfo(name, intro, description, lat, lon, rate, false, imgSrc));
                }
                parser.next();
            }
        }catch(Throwable t) {

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.jjw.mydemo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.jjw.mydemo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    //화면 클릭 이벤트
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnMainIntLogin:
                TextView tmp = (TextView) findViewById(R.id.btnMainIntLogin);
                if("로그인".equals(tmp.getText())) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    //intent.putExtra("now", now_weatherr);
                    startActivity(intent);
                    //overridePendingTransition(R.anim.fade, R.anim.hold);
                }else
                {
                    System.out.println("위치등록하기...");
                    //위치 등록으로 넘어가기
                }
                break;
            case R.id.btnMainIntRegister:

                Intent intent2 = new Intent(getApplicationContext(), SearchSrcActivity.class);
                intent2.putExtra("placeList", mPlaceList);
                startActivity(intent2);
                //overridePendingTransition(R.anim.fade, R.anim.hold);
                break;
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    //메인 화면
    private class ViewHolder{
        public TextView text;
        public RelativeLayout back;

    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ListData> mListData = new ArrayList<ListData>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_item, null);

                holder.text = (TextView) convertView.findViewById(R.id.text);
                holder.back = (RelativeLayout) convertView.findViewById(R.id.layout);
                holder.text.setTypeface(typeRe);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListData mData = mListData.get(position);
            switch (position) {
                case 0:
                    holder.back.setBackgroundResource(R.drawable.test1);
                    break;
                case 1:
                    holder.back.setBackgroundResource(R.drawable.test2);
                    break;
                case 2:
                    holder.back.setBackgroundResource(R.drawable.test3);
                    break;
                case 3:
                    holder.back.setBackgroundResource(R.drawable.test4);
                    break;
                case 4:
                    holder.back.setBackgroundResource(R.drawable.test5);
                    break;
                case 5:
                    holder.back.setBackgroundResource(R.drawable.test6);
                    break;
                case 6:
                    holder.back.setBackgroundResource(R.drawable.test7);
                    break;
                case 7:
                    holder.back.setBackgroundResource(R.drawable.test8);
                    break;
                case 8:
                    holder.back.setBackgroundResource(R.drawable.test9);
                    break;
                case 9:
                    holder.back.setBackgroundResource(R.drawable.test10);
                    break;
                default:
                    //holder.back.setBackgroundResource(R.color.black);
                    break;
            }
//            if(mData.back != null){
//                //holder.back.setVisibility(View.VISIBLE);
//
//                holder.back.setBackgroundResource(mData.back);
//            }else{
//                holder.back.setVisibility(View.GONE);
//            }
            holder.text.setText(mData.title);

            return convertView;
        }

        public void addItem(String text) {
            ListData addInfo = null;
            addInfo = new ListData();
            addInfo.title = text;
            //addInfo.back = back;

            mListData.add(addInfo);
        }

        public void remove(int position) {
            mListData.remove(position);
            dataChange();
        }

        public void dataChange() {
            mAdapter.notifyDataSetChanged();
        }
    }
}

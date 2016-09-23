package com.example.jjw.mydemo;
/*
지도 상세보기 데모
 */
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomListActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);

        listView = (ListView)this.findViewById(R.id.listView2);


        ArrayList<DataInfo> items = genData();
        //CustomAdapter Class
        CustomAdapter adapter = new CustomAdapter(this, 0, items);
        listView.setAdapter(adapter);

        //ImageView
        ImageView iv3 = (ImageView)this.findViewById(R.id.imageView3);
        ImageView iv4 = (ImageView)this.findViewById(R.id.imageView4);
        ImageView iv5 = (ImageView)this.findViewById(R.id.imageView5);

        iv3.setAdjustViewBounds(true);
        iv3.setMaxWidth(300);
        iv3.setMaxHeight(300);

        iv4.setAdjustViewBounds(true);
        iv4.setMaxWidth(300);
        iv4.setMaxHeight(300);

        iv5.setAdjustViewBounds(true);
        iv5.setMaxWidth(300);
        iv5.setMaxHeight(300);


        //iv3.setImageDrawable(getResources().getDrawable(R.drawable.test1));  사용안됨
        iv3.setImageResource(R.drawable.test1);
        iv4.setImageResource(R.drawable.test2);
        iv5.setImageResource(R.drawable.test3);

    }

    public ArrayList<DataInfo> genData()
    {
        ArrayList<DataInfo> items = new ArrayList<>();
        items.add(new DataInfo("Seoul", "captial", false));
        items.add(new DataInfo("Busan", "2ndCity", false));

        return items;
    }

    private class DataInfo
    {
        public String city;
        public String desc;
        public boolean isChecked;
        public DataInfo(String _city, String _desc, boolean _ischecked)
        {
            city = _city;
            desc = _desc;
            isChecked = _ischecked;
        }
    }

    private class CustomAdapter extends ArrayAdapter<DataInfo>

    {
        private ArrayList<DataInfo> items;


        public CustomAdapter(Context context, int textViewResourceId, ArrayList<DataInfo> objects) {
            super(context, textViewResourceId, objects);

            this.items = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            View v = convertView;
            if(v == null)
            {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.custom_lit_item, null);
            }

            //Image Instacne
            ImageView imageView = (ImageView)v.findViewById(R.id.imageView);

            //리스트뷰의 아이템에 이미지를 변경한다.
            if("Seoul".equals(items.get(position).city)){
                System.out.println("Seoul : " +R.drawable.seoul );
                imageView.setImageResource(R.drawable.seoul);
            }else if("Busan".equals(items.get(position).city))
            {
                imageView.setImageResource(R.drawable.busan);
            }

            TextView textView = (TextView)v.findViewById(R.id.textView);
            textView.setText(items.get(position).desc);

            final String text = items.get(position).city;

            CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkTest);

            return v;
        }

    }


}
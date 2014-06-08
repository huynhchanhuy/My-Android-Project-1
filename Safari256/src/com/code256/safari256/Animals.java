package com.code256.safari256;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Animals extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animals);
        setListAdapter(new MyAdapter(this, android.R.layout.simple_list_item_1,R.id.textView1, getResources().getStringArray(R.array.tourist_sites)));
    }
    private class MyAdapter extends ArrayAdapter<String>{

        public MyAdapter(Context context, int resource, int textViewResourceId,
                         String[] strings) {
            super(context, resource, textViewResourceId, strings);
            // TODO Auto-generated constructor stub
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row =inflater.inflate(R.layout.app_custom_list, parent, false);
            String[] items =getResources().getStringArray(R.array.tourist_sites);
            ImageView iv = (ImageView) row.findViewById(R.id.imageView1);
            TextView tv =(TextView) row.findViewById(R.id.textView1);
            tv.setText(items[position]);
            if(items[position].equals("Kidepo")){

                iv.setImageResource(R.drawable.kidepo7);
                iv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent flann = new Intent(Animals.this,KidepoActivity.class);
                        startActivity(flann);
                    }
                });
            }

            else if(items[position].equals("Source of the Nile")){
                iv.setImageResource(R.drawable.nile9);
                iv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent flan = new Intent(Animals.this,Nile.class);
                        startActivity(flan);
                    }
                });
            }
            else if(items[position].equals("Bwindi")){
                iv.setImageResource(R.drawable.kidepo);
                iv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent fla = new Intent(Animals.this,BwindiActivity.class);
                        startActivity(fla);
                    }
                });
            }
            else if(items[position].equals("Mabira")){
                iv.setImageResource(R.drawable.kidepo2);
                iv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent fl = new Intent(Animals.this,MabiraActivity.class);
                        startActivity(fl);
                    }
                });
            }
            else if(items[position].equals("Queen Elizabeth National Park")){
                iv.setImageResource(R.drawable.nile);
                iv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent flanny = new Intent(Animals.this,InfoActivity.class);
                        startActivity(flanny);
                    }
                });
            }
            return row;
        }

    }
}

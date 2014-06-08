package com.example.doan_didong;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.doan_didong.Tab2_ThayDoiThongTinCaNhanActivity.XacNhanThayDoiProgress;

public class Tab1_Home_HuongDan extends FragmentActivity {
    MyPageAdapter pageAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.tab1_home_huongdan);
      
      List<android.support.v4.app.Fragment> fragments = getFragments();
      pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
      ViewPager pager = (ViewPager)findViewById(R.id.tab1_huongdan_viewpager);
      pager.setAdapter(pageAdapter);
//      pager.setPageTransformer(true, new ZoomOutPageTransformer());
    }
  
    //
    //
	class MyPageAdapter extends FragmentPagerAdapter 
	{
		  private List<android.support.v4.app.Fragment> fragments;
	
		  public MyPageAdapter(android.support.v4.app.FragmentManager fm, List<android.support.v4.app.Fragment> fragments2) 
		  {
		    super(fm);
		    this.fragments = fragments2;
		  }
		  @Override 
		  public android.support.v4.app.Fragment getItem(int position) {
		    return this.fragments.get(position);
		  }
	
		  @Override
		  public int getCount() {
		    return this.fragments.size();
		  }
	}
    //
	private List<android.support.v4.app.Fragment> getFragments()
	{
		  List<android.support.v4.app.Fragment> fList = new ArrayList<android.support.v4.app.Fragment>();
		  //fList.add(MyFragment.newInstance("Fragment 1"));
		  fList.add(new Tab1_Home_HuongDan_Fragment1());
		  fList.add(new Tab1_Home_HuongDan_Fragment2());
		  fList.add(new Tab1_Home_HuongDan_Fragment3());
		  fList.add(new Tab1_Home_HuongDan_Fragment4());
		  fList.add(new Tab1_Home_HuongDan_Fragment5());
		  return fList;
	}
	//
	//
	public class ZoomOutPageTransformer implements ViewPager.PageTransformer 
	{
	    private static final float MIN_SCALE = 0.85f;
	    private static final float MIN_ALPHA = 0.5f;

	    public void transformPage(View view, float position) {
	        int pageWidth = view.getWidth();
	        int pageHeight = view.getHeight();

	        if (position < -1) { // [-Infinity,-1)
	            // This page is way off-screen to the left.
	            view.setAlpha(0);

	        } else if (position <= 1) { // [-1,1]
	            // Modify the default slide transition to shrink the page as well
	            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
	            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
	            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
	            if (position < 0) {
	                view.setTranslationX(horzMargin - vertMargin / 2);
	            } else {
	                view.setTranslationX(-horzMargin + vertMargin / 2);
	            }

	            // Scale the page down (between MIN_SCALE and 1)
	            view.setScaleX(scaleFactor);
	            view.setScaleY(scaleFactor);

	            // Fade the page relative to its size.
	            view.setAlpha(MIN_ALPHA +
	                    (scaleFactor - MIN_SCALE) /
	                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

	        } else { // (1,+Infinity]
	            // This page is way off-screen to the right.
	            view.setAlpha(0);
	        }
	    }
	}
	//
 }

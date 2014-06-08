package com.example.doan_didong;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.*;
import android.support.v4.app.*;

import com.actionbarsherlock.app.SherlockFragment;

public class Tab9_YeuThich extends SherlockFragment 
{
	Button t9_tuthongdung, t9_thanhngu, t9_bainghe, t9_video;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		Log.d("---tab 9 onCreateView","----------tab 9 onCreateView------ ");
		View view = inflater.inflate(R.layout.tab9_yeuthich, container, false);
		t9_tuthongdung = (Button)view.findViewById(R.id.tab9_tuthongdung_yeuthich);
		t9_thanhngu = (Button)view.findViewById(R.id.tab9_thanhngu_yeuthich);
		t9_bainghe = (Button)view.findViewById(R.id.tab9_bainghe_yeuthich);
		t9_video = (Button)view.findViewById(R.id.tab9_video_yeuthich);
		t9_tuthongdung.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				Intent i = new Intent(arg0.getContext(), Tab9_TuThongDung_YeuThich.class);
            	i.putExtra(Constant.KEYWORD_USER_ID, String.valueOf(MainActivity.iduser));
            	i.putExtra("loai", "tudien");
            	startActivity(i);
			}
		});
		t9_thanhngu.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				Intent i = new Intent(arg0.getContext(), Tab9_ThanhNgu_YeuThich.class);
            	i.putExtra(Constant.KEYWORD_USER_ID, String.valueOf(MainActivity.iduser));
            	i.putExtra("loai", "thanhngu");
            	startActivity(i);
			}
		});
		t9_bainghe.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				Intent i = new Intent(arg0.getContext(), Tab9_Audio_YeuThich.class);
            	i.putExtra(Constant.KEYWORD_USER_ID, String.valueOf(MainActivity.iduser));
            	i.putExtra("loai", "audio");
            	startActivity(i);
			}
		});
		t9_video.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				Intent i = new Intent(arg0.getContext(), Tab9_Video_YeuThich.class);
            	i.putExtra(Constant.KEYWORD_USER_ID, String.valueOf(MainActivity.iduser));
            	i.putExtra("loai", "video");
            	startActivity(i);
			}
		});
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
		setUserVisibleHint(true);
	}
	@Override
	public void onResume()
	{
		super.onResume();
	    Log.d("tab9 Resuming", "tab9 resume. ");
	}

}

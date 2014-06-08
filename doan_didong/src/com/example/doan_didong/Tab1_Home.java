package com.example.doan_didong;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class Tab1_Home extends SherlockFragment 
{
	Bundle b;
	public int iduser;
	public String username;
	public String hovaten;
	public int active;
	public int usertype;
	TextView tab1_welcome_username;
	Button t1_thongtintacgia,t1_lienhe, t1_danhgia, t1_huongdan, t1_binhluan;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab1.xml
		Log.d("---tab 1 onCreateView","----------tab 1 onCreateView------ ");
		View view = inflater.inflate(R.layout.tab1_home, container, false);
		tab1_welcome_username = (TextView)view.findViewById(R.id.tab1_welcome_username);
		t1_thongtintacgia = (Button)view.findViewById(R.id.tab1_btn_thongtintacgia);
		t1_huongdan = (Button)view.findViewById(R.id.tab1_btn_huongdan);
		t1_binhluan = (Button)view.findViewById(R.id.tab1_btn_binhluan);
		
		b = getActivity().getIntent().getExtras();
		
		iduser 		= b.getInt(Constant.KEYWORD_USER_ID);
		hovaten		= b.getString(Constant.KEYWORD_HOVATEN);
		username 	= b.getString(Constant.KEYWORD_USER_NAME);
		usertype    = b.getInt(Constant.KEYWORD_USER_TYPE);
		active		= b.getInt(Constant.KEYWORD_USER_ACTIVE);
		
		tab1_welcome_username.setText("Chào "+MainActivity.username+" !");
		t1_thongtintacgia.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				Intent i = new Intent(arg0.getContext(), Tab1_Home_ThongTinTacGia.class);
				getActivity().startActivity(i);
			}
		});
		
		t1_binhluan.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				Intent i = new Intent(arg0.getContext(), Tab1_Home_BinhLuan.class);
				getActivity().startActivity(i);
			}
		});
		
		t1_huongdan.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				Intent i = new Intent(arg0.getContext(), Tab1_Home_HuongDan.class);
				getActivity().startActivity(i);
			}
		});
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setUserVisibleHint(true);
	}
	@Override
	public void onResume()
	{
		super.onResume();
	    Log.d("Resuming", "tab1 resume. ");
	}

}

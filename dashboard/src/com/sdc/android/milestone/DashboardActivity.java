package com.sdc.android.milestone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DashboardActivity extends Fragment{

	private TextView tv_title;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		View v = inflater.inflate(R.layout.activity_dashboard, container, false);

		tv_title =(TextView)v.findViewById(R.id.tv_title);
		tv_title.setText("Dashboard");
		
		return v;
	}

	
}

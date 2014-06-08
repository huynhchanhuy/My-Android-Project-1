package com.sdc.android.milestone;

import java.util.ArrayList;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

public class VisitorActivity extends Fragment {
	private ProgressWheel prog_one_visitor, prog_two_visitor;
	private TextView tv_prog_one_visitor, tv_prog_two_visitor, tv_title;
	private TabHost mTabHost;

	private ListView lvKeyword;
	private KeywordAdapter adapterKeyword;
	private ArrayList<Keyword> listKeyword;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View v = inflater.inflate(R.layout.activity_visitor, container, false);

		
		setupTabHost(v);

		setupTab(new TextView(getActivity()), "Sources");
		setupTab(new TextView(getActivity()), "Keywords");

		reLoad(v);
		init(v);
		
		return v;
	}
	
	
	private void init(View v) {
		// TODO Auto-generated method stub
		prog_one_visitor = (ProgressWheel) v.findViewById(R.id.prog_one_visitor);
    	prog_two_visitor = (ProgressWheel) v.findViewById(R.id.prog_two_visitor);
    	
    	tv_prog_one_visitor = (TextView) v.findViewById(R.id.tv_prog_one_visitor);
        tv_prog_two_visitor = (TextView) v.findViewById(R.id.tv_prog_two_visitor);
        tv_title = (TextView) v.findViewById(R.id.tv_title);
        tv_title.setText("Visitors");
         
        prog_one_visitor.setProgress((int)(50*3.6f)); 	// Set progress 50%
        tv_prog_one_visitor.setText(50+"");				// Set value 50 for TextView
        
        prog_two_visitor.setProgress((int)(33*3.6f));	// Same above
        tv_prog_two_visitor.setText(33+"");
	}
	private void setupTabHost(View v) {
		mTabHost = (TabHost) v.findViewById(R.id.tabhost1);
		mTabHost.setup();
	}

	private void setupTab(final View view, final String tag) {
		View tabview = createTabView(mTabHost.getContext(), tag);

		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview)
				.setContent(new TabContentFactory() {
					public View createTabContent(String tag) {
						return view;
					}
				});
		mTabHost.addTab(setContent);

	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

	public void reLoad(View v) {
		lvKeyword = (ListView) v.findViewById(R.id.lvSource1);
		listKeyword = new ArrayList<Keyword>();

		listKeyword.add(addKeyword("punk baby clothes", "1520"));
		listKeyword.add(addKeyword("rock baby clothes", "455"));
		listKeyword.add(addKeyword("rock and roll baby clothes", "223"));
		listKeyword.add(addKeyword("baby clothes rock and roll", "584"));
		listKeyword.add(addKeyword("rock baby clothes", "22"));
		listKeyword.add(addKeyword("rock and roll baby clothes", "58"));
		listKeyword.add(addKeyword("baby clothes rock and roll", "20"));
		listKeyword.add(addKeyword("rock baby clothes", "10"));
		listKeyword.add(addKeyword("rock and roll baby clothes", "6"));
		
		adapterKeyword = new KeywordAdapter(getActivity(), listKeyword);
		lvKeyword.setAdapter(adapterKeyword);
	}

	private Keyword addKeyword(String text, String number) {
		Keyword keyword = new Keyword();
		keyword.setText(text);
		keyword.setNumber(number);
		return keyword;
	}

}

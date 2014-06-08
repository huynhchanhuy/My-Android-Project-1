package com.sdc.android.milestone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;

import com.sdc.android.milestone.R.id;

public class MainActivity extends FragmentActivity {

	public static final int FRAGMENT_DASHBOARD = 0;
	public static final int FRAGMENT_SOURCE = 1;
	public static final int FRAGMENT_VISITOR = 2;

	private SlidingPaneLayout spl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();

		setNewPage(new DashboardActivity(), FRAGMENT_DASHBOARD);
	}

	private void init() {
		spl = (SlidingPaneLayout) findViewById(id.sliding_pane_layout);
	}

	public void setNewPage(Fragment fragment, int pageIndex) {
		if (spl.isOpen()) {
			spl.closePane();
		}
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.layout_frame, fragment, "currentFragment")
				.commit();

	}

	public void onDashBoard(View v) {
		setNewPage(new DashboardActivity(), FRAGMENT_DASHBOARD);
	}

	public void onSources(View v) {
		setNewPage(new SourceActivity(), FRAGMENT_SOURCE);
	}

	public void onVisitors(View v) {
		setNewPage(new VisitorActivity(), FRAGMENT_VISITOR);
	}

	public void onSliding(View v) {
		if (spl.isOpen()) {
			spl.closePane();
		} else {
			spl.openPane();
		}
	}

}

package com.example.practise_04_layout;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class OrientationChangedActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orientation_changed);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.orientation_changed, menu);
		return true;
	}

}

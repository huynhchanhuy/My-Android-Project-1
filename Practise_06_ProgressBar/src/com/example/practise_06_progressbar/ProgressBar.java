package com.example.practise_06_progressbar;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ProgressBar extends Activity {

	private static int progress;
	private ProgressBar progressBar; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_progress_bar);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.progress_bar, menu);
		return true;
	}

}

package com.example.practise_04_layout;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("testevent", "onCreate");
		String info = (String)getLastNonConfigurationInstance();
		Log.d("testevent", "getLasNonConfigurationInstance: "+info);
		Button btn = (Button)findViewById(R.id.btnSave);
		Button btn2 = (Button)findViewById(R.id.btnCancle);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getBaseContext(), FramelayoutActivity.class);
				startActivity(i);
			}
		});
		
		btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getBaseContext(), OrientationChangedActivity.class);
				startActivity(i);
			}
		});
	}

	@Override
	@Deprecated
	public Object onRetainNonConfigurationInstance() {
		// TODO Auto-generated method stub
		Log.d("testevent", "onRetainNonConfigurationInstance: Some text to preserve");
		return "Some text to preserve";
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putString("ID", "Value");
		Log.d("testevent", "onSaveInstanceState: put Value with key of ID");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		String ID = savedInstanceState.getString("ID");
		Log.d("testevent", "onRestoreInstancState: "+ID);
		//super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d("testevent", "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d("testevent", "onPause");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Log.d("testevent", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d("testevent", "onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.d("testevent", "onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.d("testevent", "onStop");
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

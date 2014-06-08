package com.example.bt1;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	String tag= "Event";
	int request_Code = 1;
	//InputMethodManager imm;
	//Example 1
	//CharSequence[] items = {"Google","Apple","Microsoft"};
	//boolean[] itemsChecked = new boolean [items.length];
	
	//Example 2
	private ProgressDialog _progressDialog;
	private int _progress = 0;
	private Handler _progressHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		Log.d(tag,"In the onCreate() event");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_ENTER)
		{
			//0
			//startActivity(new Intent("com.example.bt1.ACTIVITY2"));
			
			//1
			//startActivity(new Intent(this, Activity2.class));
			
			//2
			//Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.codelearn.org"));
			//startActivity(intent);
			
			//3
			//startActivity(new Intent().setType("text/plain").setAction(Intent.ACTION_SEND).putExtra(android.content.Intent.EXTRA_TEXT, "News for you!"));
			
			//4
			// this runs, for example, after a button click
			//Intent intent = new Intent(Intent.ACTION_SEND);
			//intent.setType("text/plain");
			//intent.putExtra(android.content.Intent.EXTRA_TEXT, "News for you!");
			//startActivity(intent); 
			
			//5
			//startActivityForResult(new Intent(this,Activity2.class),request_Code);
			
			//6
			Intent intent = new Intent("com.example.bt1.ACTIVITY2");
			//Intent intent = new Intent(this, Activity2.class);
			Bundle extras = new Bundle();
			extras.putString("Name", "Your name here");
			intent.putExtras(extras);
			startActivityForResult(intent, 1);
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == request_Code)
		{
			if(resultCode == RESULT_OK)
			{
				Toast alert = Toast.makeText(this,data.getData().toString(),Toast.LENGTH_SHORT);
				alert.setGravity(Gravity.CENTER,0,0);
				alert.show();
			}
		}
	}

}

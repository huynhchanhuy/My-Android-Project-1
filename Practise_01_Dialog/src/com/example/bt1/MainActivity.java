package com.example.bt1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	String tag= "Event";
	
	//Example 1
	CharSequence[] items = {"Google","Apple","Microsoft"};
	boolean[] itemsChecked = new boolean [items.length];
	
	//Example 2
	private ProgressDialog _progressDialog;
	private int _progress = 0;
	private Handler _progressHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		Button btn = (Button) findViewById(R.id.btn_dialog);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Example 1
				//showDialog(0);
				
				//Example 2
				showDialog(1);
				_progress = 0;
				_progressDialog.setProgress(0);
				_progressHandler.sendEmptyMessage(0);
			}
		});

		_progressHandler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				//super.handleMessage(msg);
				/*switch (msg.what)
				{
					case 0:
						Log.d("Event", "msg what = 0");
				}*/
				if(_progress > 100)
				{
					_progressDialog.dismiss();
				} else {
					_progress++;
					_progressDialog.incrementProgressBy(1);
					_progressHandler.sendEmptyMessageDelayed(0, 10);
				}
			}
		
		};
		
	}
	
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch(id)
		{
		case 0:
			Dialog dlg = new AlertDialog.Builder(this)
				.setIcon(R.drawable.ic_launcher)
				.setTitle("This is a dialog with some simple text...")
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(getBaseContext(), "OK clicker "+getBaseContext(), Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
					}
				})
				.setMultiChoiceItems(items, itemsChecked, new OnMultiChoiceClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						// TODO Auto-generated method stub
						Toast.makeText(getBaseContext(), items[which]+(isChecked ? " checked! ":
							" unchecked! "), 
							Toast.LENGTH_SHORT).show();
					}
				})
				.create();
			Window window = dlg.getWindow();
			WindowManager.LayoutParams wlp = window.getAttributes();
			wlp.gravity = Gravity.BOTTOM;
			wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
			window.setAttributes(wlp);
			return dlg;

			case 1:
				_progressDialog = new ProgressDialog(this);
				_progressDialog.setIcon(R.drawable.ic_launcher);
				_progressDialog.setTitle("Downloading files...");
				_progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				
				_progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Hide", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(getBaseContext(), "Hide clicked!", Toast.LENGTH_SHORT).show();
					}
				});
				
				_progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(getBaseContext(), "Cancle clicked!", Toast.LENGTH_SHORT).show();
					}
				});
				
				return _progressDialog;
		}
			
		return null;
		//return super.onCreateDialog(id);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(tag, "In the onPause() event");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(tag, "In the onDestroy() event");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d(tag, "In the onRestart() event");
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(tag, "In the onResume() event");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(tag, "In the onStart() event");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(tag, "In the onStart() event");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

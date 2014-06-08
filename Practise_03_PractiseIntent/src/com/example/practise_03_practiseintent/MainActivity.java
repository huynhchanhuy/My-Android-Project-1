package com.example.practise_03_practiseintent;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button b1,b2,b3,b4,b5;
	int request_Code=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Initialize button
		b1 = (Button)findViewById(R.id.btn_webbrowser);
		b2 = (Button)findViewById(R.id.btn_makecalls);
		b3 = (Button)findViewById(R.id.btn_showMap);
		b4 = (Button)findViewById(R.id.btn_chooseContact);
		b5 = (Button)findViewById(R.id.btn_launchMyBrowser);
	
		//Web Browser
		b1.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(android.content.Intent.ACTION_VIEW,
								Uri.parse("http://www.google.com"));
						Log.d("Event1",Uri.parse("http://www.google.com").toString());
						startActivity(i);
					}
				});
		
		//Make call
		b2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Event1",Uri.parse("tel:+841679246288").toString());
				Intent i = new Intent(Intent.ACTION_DIAL, 
						Uri.parse("tel:+841679246288"));
				startActivity(i);
			}
		});
		
		//Show Map
		b3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Event1",Uri.parse("geo:37.827500,-122.481670").toString());
				Intent i = new Intent(Intent.ACTION_VIEW,
						Uri.parse("geo:37.827500,-122.481670"));
				startActivity(i);
			}
		});

		//Choose contact
		b4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_PICK);
				i.setType(ContactsContract.Contacts.CONTENT_TYPE);
				Log.d("Event1",ContactsContract.Contacts.CONTENT_TYPE);
				startActivityForResult(i,request_Code);
			}
		});	
		
		b5.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent i =new Intent("com.example.practise_03_practiseintent.MyBrowserActivity");
				Intent i =new Intent("android.intent.action.VIEW"); //invoke all apps using "android.intent.action.VIEW" 
				//If you specify the above line, so you must add this bellow line to make sure the apps choose this MyBrowse
				//instead of the other
				i.addCategory("net.practiseandroid.apps");
				//Intent i =new Intent(getBaseContext(),MyBrowserActivity.class); //invoke only this ap
				i.setData(Uri.parse("http://www.amazon.com"));
				startActivity(i);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==request_Code)
		{
			if(resultCode==RESULT_OK)
			{
//				Toast alert = Toast.makeText(this, data.getData().toString(), Toast.LENGTH_SHORT);
//				alert.setGravity(Gravity.CENTER, 0, 0);
//				alert.show();
				Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(data.getData().toString()));
				Log.d("Event1",Uri.parse(data.getData().toString()).toString());
				startActivity(i);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}

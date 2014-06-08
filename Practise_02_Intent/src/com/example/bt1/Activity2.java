package com.example.bt1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Activity2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity2);
		
		String defaultName="";
		Bundle extras = getIntent().getExtras();
		try{
		if(extras != null)
			defaultName = extras.get("Name").toString();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		EditText edit = (EditText)findViewById(R.string.editTextID);
		edit.setHint(defaultName);
		
		//get button OK
		Button btn1 = (Button)findViewById(R.string.buttonID);
		
		//event handler for button ok
		btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent data = new Intent();
				
				//get the edittext view
				EditText txt_username = (EditText)findViewById(R.string.editTextID);
				
				//set the data to pass back
				data.setData(Uri.parse(txt_username.getText().toString()));
				setResult(RESULT_OK,data);
				
				//close activity
				finish();
			}
		});
		
	}
	
}

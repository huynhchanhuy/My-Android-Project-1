package com.example.doan_didong;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseUploadActivity extends Activity
{

	String giaovien;
	String iduser;
	Button btn_upload_audio;
	Button btn_upload_video;
	Button btn_upload_study;
	Button btn_upload_test;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_upload);
		Bundle b = getIntent().getExtras();
		giaovien=b.getString("giaovien");
		iduser=b.getString("iduser");
		
		
		btn_upload_audio=(Button)findViewById(R.id.btn_upload_audio);
		btn_upload_video=(Button)findViewById(R.id.btn_upload_video);
		btn_upload_study=(Button)findViewById(R.id.btn_upload_study);
		btn_upload_test=(Button)findViewById(R.id.btn_upload_test);
		btn_upload_audio.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(getApplicationContext(), ChooseAudioInSdcard.class);
				i.putExtra("giaovien", giaovien);
				i.putExtra("iduser", iduser);
				startActivity(i);
				
			}
		});
		btn_upload_video.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(getApplicationContext(), ChooseVideoInSdcard.class);
				i.putExtra("giaovien", giaovien);
				i.putExtra("iduser", iduser);
				startActivity(i);
				
			}
		});
		
		btn_upload_study.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(getApplicationContext(), UploadAStudy.class);
				i.putExtra("giaovien", giaovien);
				i.putExtra("iduser", iduser);
				startActivity(i);
				
			}
		});
		
		btn_upload_test.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(getApplicationContext(), UploadAllTestName.class);
				i.putExtra("giaovien", giaovien);
				i.putExtra("iduser", iduser);
				startActivity(i);
				
			}
		});
	}
     
}

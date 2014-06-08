package com.example.doan_didong;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doan_didong.UploadAVideo.UploadProgress;

public class UploadAStudy extends Activity 
{

	private ProgressDialog pDialog;
	
	String giaovien;
	String iduser;
	
	String title_study;
	String text_study;

	
	EditText ed_title_study;

	EditText ed_text_study;

	
	Button btn_upload_study;
	

	String urlString="http://10.0.2.2/doan_didong/upload_video.php";
	String url_upload_study="http://10.0.2.2/doan_didong/upload_study.php";


    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_a_study);
        
        Bundle b = getIntent().getExtras();
		giaovien=b.getString("giaovien");
		iduser=b.getString("iduser");
		
		ed_title_study=(EditText)findViewById(R.id.ed_upload_title_study);
		ed_text_study=(EditText)findViewById(R.id.ed_upload_text_study);
		
		btn_upload_study=(Button)findViewById(R.id.btn_upload_study);

		
		btn_upload_study.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{			
				if(!"".equals(ed_title_study.getText().toString()) && !"".equals(ed_text_study.getText().toString()))
				{
					
					new UploadProgress().execute();
					
					
				}else
					Toast.makeText(getBaseContext(), "Không được để trống", Toast.LENGTH_SHORT).show();

			}
		});
		 
    }
	
    
    class UploadProgress extends AsyncTask<String, String, String> 
	{
		
		int success;
		String issuccess="";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(UploadAStudy.this);
			pDialog.setMessage("Uploading......");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		protected String doInBackground(String... args) 
		{
			
			    List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("title", ed_title_study.getText().toString()));
				params.add(new BasicNameValuePair("text", ed_text_study.getText().toString()));
			
				String json_string = jsonParser.makeHttpRequest(url_upload_study,"POST", params);			
				Log.d("chuỗi json nhận được", "là json_string========"+json_string);
				if(json_string.length() != 0)
				{
					JSONObject json;
					try {
							json = JSONParser.getJSONObjectFromJString(json_string);
						success = json.getInt(TAG_SUCCESS);
						
						if (success == 1) 
						{
							issuccess="upload success";
						} else 
							{
							issuccess="upload fail";
							}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				return null;
		}

		protected void onPostExecute(String file_url) 
		{
			
			pDialog.dismiss();
			Toast.makeText(getBaseContext(), issuccess,Toast.LENGTH_LONG).show();
			
		}

	}
   
}

package com.example.doan_didong;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doan_didong.UploadAStudy.UploadProgress;

public class AddBinhLuanActivity extends Activity 
{

	private ProgressDialog pDialog;


	EditText ed_binhluan_name;

	EditText ed_binhluan_text;

	
	Button btn_add_binhluan;
	

	String url_add_binhluan="http://10.0.2.2/doan_didong/binh_luan_add.php";
	


    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.binhluan_add);
        

		
        ed_binhluan_name=(EditText)findViewById(R.id.ed_binhluan_name);
        ed_binhluan_text=(EditText)findViewById(R.id.ed_binhluan_text);
		
        btn_add_binhluan=(Button)findViewById(R.id.btn_add_binhluan);

		
        btn_add_binhluan.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{			
				if(!"".equals(ed_binhluan_name.getText().toString()) && !"".equals(ed_binhluan_text.getText().toString()))
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
			pDialog = new ProgressDialog(AddBinhLuanActivity.this);
			pDialog.setMessage("Uploading......");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		protected String doInBackground(String... args) 
		{
			
			    List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("name", ed_binhluan_name.getText().toString()));
				params.add(new BasicNameValuePair("text", ed_binhluan_text.getText().toString()));
			
				String json_string = jsonParser.makeHttpRequest(url_add_binhluan,"POST", params);			
				Log.d("chuỗi json nhận được", "là json_string========"+json_string);
				if(json_string.length() != 0)
				{
					JSONObject json;
					try {
							json = JSONParser.getJSONObjectFromJString(json_string);
							success = json.getInt(TAG_SUCCESS);
						
						if (success == 1) 
						{
							issuccess="thêm thành công";
						} else 
							{
							issuccess="không thêm được";
							}
					} catch (JSONException e) 
					{
						e.printStackTrace();
					}
				}else {};
				return null;
		}

		protected void onPostExecute(String file_url) 
		{
			
			pDialog.dismiss();
			Toast.makeText(getBaseContext(), issuccess,Toast.LENGTH_LONG).show();
			
		}

	}
   
}

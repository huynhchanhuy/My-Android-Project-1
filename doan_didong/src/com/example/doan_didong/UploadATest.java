package com.example.doan_didong;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;



public class UploadATest extends Activity 
{

	// Progress Dialog
	private ProgressDialog pDialog;

	String iduser;
	String nametest;
	
	String dapan="";

	EditText cauhoi;
	EditText a;
	EditText b;
	EditText c;
	EditText d;
	RadioGroup radiodapan;
	RadioButton radio1,radio2,radio3,radio4;
	
	Button btn_upload_test1;
	

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();
	
	private static String url_upload_a_test = "http://10.0.2.2/doan_didong/upload_a_test.php";
//	private static String url_get_all_id_test1 = "http://10.0.2.2/doan_didong/test_get_all_id_test1.php";
//	private static String url_add_diem = "http://10.0.2.2/doan_didong/add_diem_for_user.php";
//	private static String url_link = "http://10.0.2.2/doan_didong/test1/";
	
	// JSON Node names
	private static final String TAG_SUCCESS  = "success";
	


	// products JSONArray
	JSONArray all_test = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_a_test);

		cauhoi=(EditText)findViewById(R.id.ed_cau_hoi);
		a=(EditText)findViewById(R.id.ed_tra_loi_a);
		b=(EditText)findViewById(R.id.ed_tra_loi_b);
		c=(EditText)findViewById(R.id.ed_tra_loi_c);
		d=(EditText)findViewById(R.id.ed_tra_loi_d);
		btn_upload_test1=(Button)findViewById(R.id.btn_upload_test1);
		
		radiodapan=(RadioGroup)findViewById(R.id.radio_tra_loi);
		radio1=(RadioButton)findViewById(R.id.radio_tra_loi_a);
		radio2=(RadioButton)findViewById(R.id.radio_tra_loi_b);
		radio3=(RadioButton)findViewById(R.id.radio_tra_loi_c);
		radio4=(RadioButton)findViewById(R.id.radio_tra_loi_d);

		
		Bundle bundle = getIntent().getExtras();
		iduser = bundle.getString("iduser");
		nametest   = bundle.getString("nametest");
				
		radiodapan.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				
				switch (checkedId) 
				{
				case R.id.radio_tra_loi_a:
					dapan="a";
					break;
				case R.id.radio_tra_loi_b:
					dapan="b";
					break;
				case R.id.radio_tra_loi_c:
					dapan="c";
					break;
				case R.id.radio_tra_loi_d:
					dapan="d";
					break;
				}
				//Toast.makeText(getBaseContext(), dapan, Toast.LENGTH_SHORT).show();
			}
		});
		btn_upload_test1.setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View v) 
			{
				if(   !"".equals(cauhoi.getText().toString()) &&  !"".equals(a.getText().toString())
						&& !"".equals(b.getText().toString()) && !"".equals(c.getText().toString())
						&& !"".equals(d.getText().toString()) && !"".equals(dapan)   )
				{
					new LoadAllTest123().execute();
				}
				else
					Toast.makeText(getBaseContext(), "Không được để trống", Toast.LENGTH_SHORT).show();
			}
		});
	}
	//------------------------------------------------
		class LoadAllTest123 extends AsyncTask<String, String, String> 
		{
			int success;
			String issuccess;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(UploadATest.this);
				pDialog.setMessage("Loading......");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
			}

			protected String doInBackground(String... args) {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("nametest", nametest));
				params.add(new BasicNameValuePair("text", cauhoi.getText().toString()));
				params.add(new BasicNameValuePair("a", "A. "+a.getText().toString()));
				params.add(new BasicNameValuePair("b", "B. "+b.getText().toString()));
				params.add(new BasicNameValuePair("c", "C. "+c.getText().toString()));
				params.add(new BasicNameValuePair("d", "D. "+d.getText().toString()));
				params.add(new BasicNameValuePair("dapan", dapan));
				String json_string = jParser.makeHttpRequest(url_upload_a_test, "POST", params);

				Log.d("chuỗi json nhận được", "là json_string========"+json_string);
				if(json_string.length() != 0)
				{
					JSONObject json;
					try {
							json = JSONParser.getJSONObjectFromJString(json_string);
						// Checking for SUCCESS TAG
						success = json.getInt(TAG_SUCCESS);
	
						if (success == 1) 
						{
							issuccess="upload success";
							
						} else 
							{issuccess="upload fail";}
							
					} catch (JSONException e) {e.printStackTrace();}
				}
				return null;
			}
			protected void onPostExecute(String file_url) 
			{
				pDialog.dismiss();	
				Toast.makeText(getBaseContext(), issuccess, Toast.LENGTH_SHORT).show();
			}

		}
		//-----------------------------------
}
	


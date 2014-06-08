package com.example.doan_didong;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class GetAllTest1 extends Activity implements OnCompletionListener
{

	// Progress Dialog
	private ProgressDialog pDialog;
	private  MediaPlayer mp;
	public int islandau = 0;
	String path;
	
	String iduser;
	String nametest;
	String socauhoi="20";
	String so_thu_tu_cau="1";
	
	String[] array_tra_loi;
	String[] array_id_cau_hoi;
	private String id_cau_hoi;
	
	
	String question_id;
	String question_text;
	String question_a;
	String question_b;
	String question_c;
	String question_d;
	String question_dapan;
	String question_link;
	
	
	String ngay;
	float diemso;
	
	
	Button  btn_causau,
			btn_cautruoc,
			btn_choncau,
			btn_lamlai,
			btn_ketqua,
			btn_play,
			btn_stop,
			btn_chualam;
	TextView cau_hien_tai,
			 noi_dung_cau_hoi,
			 dapan;
	EditText choncau;
	RadioGroup radiogroup;
	
	RadioButton radio1,radio2,radio3,radio4;
	
	
	

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();
	
	private static String url_get_all_test1 = "http://10.0.2.2/doan_didong/test_get_all_test1.php";
	private static String url_get_all_id_test1 = "http://10.0.2.2/doan_didong/test_get_all_id_test1.php";
	private static String url_add_diem = "http://10.0.2.2/doan_didong/add_diem_for_user.php";
	private static String url_link = "http://10.0.2.2/doan_didong/test1/";
	
	// JSON Node names
	private static final String TAG_SUCCESS  = "success";
	


	// products JSONArray
	JSONArray all_test = null;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.get_all_test1);

		
		/*btn_causau=(Button)findViewById(R.id.btn_causau);
		btn_cautruoc=(Button)findViewById(R.id.btn_cautruoc);
		btn_choncau=(Button)findViewById(R.id.btn_choncau1);
		btn_lamlai=(Button)findViewById(R.id.btn_lamlai);
		btn_ketqua=(Button)findViewById(R.id.btn_ketqua);
		btn_play=(Button)findViewById(R.id.btn_play1);
		btn_stop=(Button)findViewById(R.id.btn_stop1);
		btn_chualam=(Button)findViewById(R.id.btn_chualam);
		
		choncau=(EditText)findViewById(R.id.ed_choncau);
		
		cau_hien_tai=(TextView)findViewById(R.id.cau_hien_tai);
		noi_dung_cau_hoi=(TextView)findViewById(R.id.noi_dung_cau_hoi);
		dapan=(TextView)findViewById(R.id.dapan);
		
		radiogroup=(RadioGroup)findViewById(R.id.radioGroup1);
		radio1=(RadioButton)findViewById(R.id.radio1);
		radio2=(RadioButton)findViewById(R.id.radio2);
		radio3=(RadioButton)findViewById(R.id.radio3);
		radio4=(RadioButton)findViewById(R.id.radio4);*/

		
				Bundle b = getIntent().getExtras();
				iduser = b.getString("iduser");
				nametest   = b.getString("nametest");
				socauhoi=b.getString("socauhoi");

		array_id_cau_hoi =new String[Integer.valueOf(socauhoi)+1];
		
		array_tra_loi =new String[Integer.valueOf(socauhoi)+1];
		for(int i=0;i<Integer.valueOf(socauhoi);i++)
		{
			array_tra_loi[i+1]="";
		}
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("nametest", nametest));
		params.add(new BasicNameValuePair("socauhoi", socauhoi));
		String json_string = jParser.makeHttpRequest(url_get_all_id_test1, "POST", params);
		Log.d("chuỗi json nhận được", "là json_string========"+json_string);
		if(json_string.length() != 0)
		{
			JSONObject json;
		try {
				json = JSONParser.getJSONObjectFromJString(json_string);
				int success = json.getInt("success");
				if (success == 1) 
				{
					all_test = json.getJSONArray("products");
	
					// looping through All Products
					for (int i = 0; i < all_test.length(); i++) 
					{
						JSONObject c = all_test.getJSONObject(i);
						
						String id=c.getString("id");
						
						array_id_cau_hoi[i+1]=id;
					}
				} else {}
			} catch (JSONException e) {e.printStackTrace();}
		}else{};
		
		id_cau_hoi=array_id_cau_hoi[1];
		array_tra_loi[1]="";
		new LoadAllTest1().execute();
		mp=new MediaPlayer();
		//Toast.makeText(getBaseContext(), path, Toast.LENGTH_SHORT).show();
		
		btn_play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// check for already playing
				
				switch (islandau) 
				{
				case 0:
					try{
						mp.reset();
						mp.setDataSource(path);
						mp.prepare();
						mp.start();
						islandau=1;
						btn_play.setBackgroundResource(R.drawable.pause_test);
						//Toast.makeText(getBaseContext(), path, Toast.LENGTH_SHORT).show();
					}catch(Exception e)
					{
						e.printStackTrace();
					}
					break;
				case 1:
					if(mp.isPlaying())
					{
						if(mp!=null)
						{
							mp.pause();
							// Changing button image to play button
							btn_play.setBackgroundResource(R.drawable.play_test);
							islandau=2;
						}
					}
					break;
				case 2:
					if(!mp.isPlaying())
					{
						if(mp!=null)
						{
							mp.start();
							
							btn_play.setBackgroundResource(R.drawable.pause_test);
							islandau=1;
						}
					}
					break;
				}	
			}
		});
		btn_stop.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				if(islandau!=0)
				{
					mp.stop();
					islandau=0;
					btn_play.setBackgroundResource(R.drawable.play_test);
				}
				
				
			}
		});
		
		btn_chualam.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				String chualam="Câu chưa làm: ";
				for(int i=0;i<Integer.valueOf(socauhoi);i++)
				{
					if(array_tra_loi[i+1].equals(""))
						chualam=chualam + String.valueOf(i+1) + "   ";
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(GetAllTest1.this);
				builder.setMessage(chualam)
				.setTitle("Câu chưa làm!")
				.setCancelable(false)
				.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
										
						dialog.cancel();
					}
				});
				builder.create().show();
				
			}
		});
		btn_cautruoc.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				setradiocheck();
				if(islandau!=0)
				{
					mp.stop();
					islandau=0;
					btn_play.setBackgroundResource(R.drawable.play_test);
				}
				
				
				if( Integer.valueOf(so_thu_tu_cau) == 1  )
				{
					Toast.makeText(getBaseContext(), "Không có câu trước!", Toast.LENGTH_SHORT).show();
				} 
				else if(Integer.valueOf(so_thu_tu_cau)>1)
				{
					
					so_thu_tu_cau=String.valueOf(Integer.valueOf(so_thu_tu_cau)-1);
					id_cau_hoi=array_id_cau_hoi[Integer.valueOf(so_thu_tu_cau)];
					new LoadAllTest1().execute();
				}
			}
		});
		btn_causau.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				if(islandau!=0)
				{
					mp.stop();
					islandau=0;
					btn_play.setBackgroundResource(R.drawable.play_test);
				}
				setradiocheck();
				if( Integer.valueOf(so_thu_tu_cau) == Integer.valueOf(socauhoi)  )
				{
					Toast.makeText(getBaseContext(), "Hết câu hỏi", Toast.LENGTH_SHORT).show();
					
				} 
				else if( Integer.valueOf(so_thu_tu_cau) < Integer.valueOf(socauhoi))
				{
					
					so_thu_tu_cau=String.valueOf(Integer.valueOf(so_thu_tu_cau)+1);
					id_cau_hoi=array_id_cau_hoi[Integer.valueOf(so_thu_tu_cau)];
					new LoadAllTest1().execute();
				}
			}
		});
		btn_choncau.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				
				String cau=choncau.getText().toString();
				if(!"".equals(cau))
				{
					int temp1=Integer.valueOf(cau);
					if(temp1 > 0 && temp1 <= Integer.valueOf(socauhoi))
					{
						if(islandau!=0)
						{
							mp.stop();
							islandau=0;
							btn_play.setBackgroundResource(R.drawable.play_test);
						}
						so_thu_tu_cau=String.valueOf(temp1);
						id_cau_hoi=array_id_cau_hoi[Integer.valueOf(so_thu_tu_cau)];
						new LoadAllTest1().execute();
					}
					else
						Toast.makeText(getBaseContext(), "Câu bạn chọn không đúng", Toast.LENGTH_SHORT).show();
			
				}
			}
		});
		
		btn_lamlai.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				if(islandau!=0)
				{
					mp.stop();
					islandau=0;
					btn_play.setBackgroundResource(R.drawable.play_test);
				}
				setradiocheck();
				so_thu_tu_cau="1";
				id_cau_hoi=array_id_cau_hoi[1];
				for(int i=0;i<Integer.valueOf(socauhoi);i++)
				{
					array_tra_loi[i+1]="";
				}
				new LoadAllTest1().execute();
			}
		});
		btn_ketqua.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				if(islandau!=0)
				{
					mp.stop();
					islandau=0;
					btn_play.setBackgroundResource(R.drawable.play_test);
				}
				int socaudung=0;
				for(int i=0;i<Integer.valueOf(socauhoi);i++)
				{
					if("1".equals(array_tra_loi[i+1]))
					{
						socaudung=socaudung+1;
					}
				}
				diemso=(float)(    (float)10/Integer.valueOf(socauhoi)   )*socaudung;
				
				java.util.Date d = new java.util.Date();
				
				ngay = String.valueOf(d.getDate())+"/"+String.valueOf(d.getMonth()+1)+"/"+String.valueOf(d.getYear()+1900);
				
				//Toast.makeText(getBaseContext(), ngay, Toast.LENGTH_SHORT).show();
				AlertDialog.Builder builder = new AlertDialog.Builder(GetAllTest1.this);
				builder.setMessage("Số câu đúng:"+socaudung+"/"+socauhoi +"\nĐiểm số: "+diemso+"\nBạn có muốn làm lại!")
				.setTitle("Kết quả")
				.setCancelable(false)
				.setPositiveButton("Có", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
										
						so_thu_tu_cau="1";
						id_cau_hoi=array_id_cau_hoi[Integer.valueOf(so_thu_tu_cau)];
						for(int i=0;i<Integer.valueOf(socauhoi);i++)
						{
							array_tra_loi[i+1]="";
						}
						new LoadAllTest1().execute();
					}
				})
				.setNeutralButton("Save kết quả",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("iduser", iduser));
						params.add(new BasicNameValuePair("nametest", nametest));
						params.add(new BasicNameValuePair("diemso", String.valueOf(diemso)));
						params.add(new BasicNameValuePair("ngay", ngay));
						params.add(new BasicNameValuePair("socauhoi", socauhoi));
						String json_string=jParser.makeHttpRequest(url_add_diem, "POST", params);
						Log.d("chuỗi json nhận được", "là json_string========"+json_string);
						if(json_string.length() != 0)
						{
							JSONObject json;
							try {
								json = JSONParser.getJSONObjectFromJString(json_string);
								int success;
							// Checking for SUCCESS TAG
								success = json.getInt(TAG_SUCCESS);
	
								if (success == 1) 
								{
									Toast.makeText(getBaseContext(), "Save complete", Toast.LENGTH_SHORT).show();
								} else 
									{}
								
							} catch (JSONException e) {e.printStackTrace();}
						}
					}
				})
				.setNegativeButton("Không", new DialogInterface.OnClickListener() 
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				builder.create().show();
				
			}
		});
	}
	
	@Override
	public void onCompletion(MediaPlayer arg0) 
	{
		btn_play.setBackgroundResource(R.drawable.play_test);
		if(islandau!=0)
		{
			mp.stop();
			islandau=0;
			btn_play.setBackgroundResource(R.drawable.play_test);
		}
	}
	
	@Override
	 public void onDestroy()
	{
		super.onDestroy();
		
		if(islandau!=0)
		{
			mp.release();
			islandau=0;
			btn_play.setBackgroundResource(R.drawable.play_test);
		}
	 }
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}	
	@Override
	  public boolean onKeyDown(int keyCode, KeyEvent event) {
	      
	      if (keyCode == KeyEvent.KEYCODE_BACK) 
	      {
	    	  if(islandau!=0)
				{
					mp.release();
					islandau=0;
					btn_play.setBackgroundResource(R.drawable.play_test);
				}
			    finish();
	          return true;
	      }
	      return super.onKeyDown(keyCode, event);
	  }
	public void setradiocheck()
	{
		radio1.setChecked(false);
		radio2.setChecked(false);
		radio3.setChecked(false);
		radio4.setChecked(false);
	}
	//------------------------------------------------
	class LoadAllTest1 extends AsyncTask<String, String, String> 
	{
		int success;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			pDialog = new ProgressDialog(GetAllTest1.this);
//			pDialog.setMessage("Loading......");
//			pDialog.setIndeterminate(false);
//			pDialog.setCancelable(false);
//			pDialog.show();
		}

		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			//params.add(new BasicNameValuePair("iduser", iduser));
			params.add(new BasicNameValuePair("nametest", nametest));
			params.add(new BasicNameValuePair("cauhientai", id_cau_hoi));
			//params.add(new BasicNameValuePair("socauhoi", socauhoi));
			
			
			String json_string = jParser.makeHttpRequest(url_get_all_test1, "POST", params);

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
					JSONArray productObj = json.getJSONArray("product"); // JSON Array
					
					// get first product object from JSON Array
					JSONObject product = productObj.getJSONObject(0);
					
					question_id = product.getString("id");
					question_text = product.getString("text");
					question_a = product.getString("a");
					question_b = product.getString("b");
					question_c = product.getString("c");
					question_d = product.getString("d");
					question_dapan = product.getString("dapan");
					question_link = product.getString("link");
					if("".equals(question_link))
						path="";
					else path=url_link + question_link;
					
					//Toast.makeText(getBaseContext(), songtitle +" "+path, Toast.LENGTH_LONG).show();
					
				} else 
					{}
					
			} catch (JSONException e) {e.printStackTrace();}
			}else {};

			return null;
		}
		protected void onPostExecute(String file_url) 
		{
//			pDialog.dismiss();
			cau_hien_tai.setText("Question: " + so_thu_tu_cau + "/" + socauhoi);
			noi_dung_cau_hoi.setText(question_text);
			radio1.setText(question_a);
			radio2.setText(question_b);
			radio3.setText(question_c);
			radio4.setText(question_d);
			dapan.setText(question_dapan);
			if(!"".equals(path))
			{
				
				btn_play.setClickable(true);
				btn_stop.setClickable(true);
				
				Toast.makeText(getBaseContext(), "Câu hỏi này có audio kèm theo!", Toast.LENGTH_LONG).show();
				//Toast.makeText(getBaseContext(), path, Toast.LENGTH_LONG).show();
			} else
				{
				  btn_play.setClickable(false);
				  btn_stop.setClickable(false);
				}
			
		}

	}
	//-----------------------------------
	
	
}


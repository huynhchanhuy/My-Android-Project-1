package com.example.doan_didong;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class Tab1_Home_BinhLuan extends Activity 
{
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private FunctionCheck instantFunctionCheck;
	ArrayList<BinhLuan> itemlistBINHLUAN = new ArrayList<Tab1_Home_BinhLuan.BinhLuan>();
	private BinhLuanCustomAdapter adapter;
	private GetAllBinhLuan_first task_first;
	private GetAllBinhLuan_refresh task_refresh;
	BinhLuan_Save task_binhluan_save;
	
	
	EditText t1_binhluan_noidung;
	Button t1_binhluan_btn;
	ListView t1_binhluan_listview;
	
	boolean isloading;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab1_home_binhluan);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//set không cho xoay màn hình
		isloading = false;
		instantFunctionCheck = new FunctionCheck(getApplicationContext());
		t1_binhluan_noidung = (EditText)findViewById(R.id.tab1_binhluan_noidung);
		t1_binhluan_listview = (ListView)findViewById(R.id.tab1_binhluan_listview);
		t1_binhluan_btn		 = (Button)findViewById(R.id.tab1_binhluan_btn_gui);
		
		if(instantFunctionCheck.checkInternetConnection())
		{
			task_first = new GetAllBinhLuan_first();
			task_first.execute();
		}
		
		t1_binhluan_btn.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				if("".equals(t1_binhluan_noidung.getText().toString()))
				{
					Toast.makeText(getApplicationContext(), "Vui lòng nhập vào nội dung", Toast.LENGTH_SHORT).show();
				}
				else
				{
					task_binhluan_save = new BinhLuan_Save();
					task_binhluan_save.execute();
				}
				
				
			}
		});
		
	};
	
	
	//
	//
	//
	class BinhLuan_Save extends AsyncTask<String , String, String> 
	{
		int success;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			success=0;
			pDialog = new ProgressDialog(Tab1_Home_BinhLuan.this);
			pDialog.setMessage("Đang xử lý ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... param) {
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("iduser", String.valueOf(MainActivity.iduser) ));
			params.add(new BasicNameValuePair("username", String.valueOf(MainActivity.username) ));
			params.add(new BasicNameValuePair("noidung", t1_binhluan_noidung.getText().toString() ));
			params.add(new BasicNameValuePair("request", "totnghiep_binh_luan_save"));
			
			JSONParser jj = new JSONParser();
			String json_string = jj.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_binh_luan_save.php", "POST", params);
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					
					success = json.getInt(Constant.KEYWORD_SUCCESS); 
				} catch (JSONException e) {e.printStackTrace();}
			} else {success=3;};

			return null;
		}
		protected void onPostExecute(String file_url) 
		{
			pDialog.dismiss();
			if(success==1 )
			{
				task_refresh = new GetAllBinhLuan_refresh();
				task_refresh.execute();
				
			}
			if(success==0 )
			{
				Toast.makeText(getApplicationContext(), "Không thành công. Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			}
			if(success==3)
			{
				Toast.makeText(getApplicationContext(), "Không thành công. Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			}
		}

	}
	//
	//
	class GetAllBinhLuan_first extends AsyncTask<String, String, String> 
	{
		int success111;
		int all_item;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			pDialog = new ProgressDialog(Tab1_Home_BinhLuan.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			success111 = 0;
			all_item = -1;
			isloading=true;
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("request", "totnghiep_binh_luan_get_all"));
			
			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_binh_luan_get_all.php", "POST", params);
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					success111 =		json.getInt(Constant.KEYWORD_SUCCESS);
					all_item = json.getInt("all_item");
					
					if (success111 == 1 && all_item > 0) 
					{
						JSONArray jsonArray = json.getJSONArray("all_binhluan"); 
						for (int i = 0; i < jsonArray.length(); i++) 
						{
							
							JSONObject c = jsonArray.getJSONObject(i);
							String id 		= c.getString("id");
							String username = c.getString("username");
							String ngay 	= c.getString("ngay");
							String noidung = c.getString("noidung");
							
							BinhLuan item = new BinhLuan(id,username, ngay, noidung);
							itemlistBINHLUAN.add(item);
						}
						jsonArray=null;
					} else {}
				} catch (JSONException e) 
				{
					success111=0;
					e.printStackTrace();
				}
			} else {success111=3;}

			return null;
		}
		protected void onPostExecute(String file_url) {
			isloading=false;
			pDialog.dismiss();
			if(success111==0) 
			{
				Log.d("---Success=0 lỗi internet","---Success=0 lỗi internet");
				//Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
				task_first = new GetAllBinhLuan_first();
				task_first.execute();
			} 
			else if(success111==3) 
			{
				Log.d("---Success=3 JSON=rỗng", "---Success=3 JSON=rỗng");
				task_first = new GetAllBinhLuan_first();
				task_first.execute();
				//Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			}
			
			if(all_item==0)
			{
				Toast.makeText(getApplicationContext(), "Chưa có bình luận nào", Toast.LENGTH_SHORT).show();
			}
			else if(success111==1 && all_item > 0)
			{
					adapter = new BinhLuanCustomAdapter(getApplicationContext(), R.layout.tab1_home_binhluan_item, itemlistBINHLUAN );
					t1_binhluan_listview.setAdapter(adapter);
			}
			
		}

	}
	
	class GetAllBinhLuan_refresh extends AsyncTask<String, String, String> 
	{
		int success111;
		int all_item;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			pDialog = new ProgressDialog(Tab1_Home_BinhLuan.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			success111 = 0;
			all_item = -1;
			isloading=true;
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("request", "totnghiep_binh_luan_get_all"));
			
			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_binh_luan_get_all.php", "POST", params);
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					success111 =		json.getInt(Constant.KEYWORD_SUCCESS);
					all_item = json.getInt("all_item");
					
					if (success111 == 1 && all_item > 0) 
					{
						itemlistBINHLUAN.clear();
						JSONArray jsonArray = json.getJSONArray("all_binhluan"); 
						for (int i = 0; i < jsonArray.length(); i++) 
						{
							
							JSONObject c = jsonArray.getJSONObject(i);
							String id 		= c.getString("id");
							String username = c.getString("username");
							String ngay 	= c.getString("ngay");
							String noidung = c.getString("noidung");
							
							BinhLuan item = new BinhLuan(id,username, ngay, noidung);
							itemlistBINHLUAN.add(item);
						}
						jsonArray=null;
					} else {}
				} catch (JSONException e) 
				{
					success111=0;
					e.printStackTrace();
				}
			} else {success111=3;}

			return null;
		}
		protected void onPostExecute(String file_url) {
			isloading=false;
			pDialog.dismiss();
			if(success111==0) 
			{
				Log.d("---Success=0 lỗi internet","---Success=0 lỗi internet");
				//Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
				task_first = new GetAllBinhLuan_first();
				task_first.execute();
			} 
			else if(success111==3) 
			{
				Log.d("---Success=3 JSON=rỗng", "---Success=3 JSON=rỗng");
				task_first = new GetAllBinhLuan_first();
				task_first.execute();
				//Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			}
			
			if(all_item==0)
			{
				Toast.makeText(getApplicationContext(), "Chưa có bình luận nào", Toast.LENGTH_SHORT).show();
			}
			else if(success111==1 && all_item > 0)
			{
				
				adapter = new BinhLuanCustomAdapter(getApplicationContext(), R.layout.tab1_home_binhluan_item, itemlistBINHLUAN );
				t1_binhluan_listview.setAdapter(adapter);
				Toast.makeText(getApplicationContext(), "Thêm bình luận thành công", Toast.LENGTH_SHORT).show();
			}
			
		}

	}
	//
	//
	//Class custom adapter quản lý listitem
	//
	public class BinhLuanCustomAdapter extends ArrayAdapter<BinhLuan> 
	{
		
		Context mContext;  
	    ArrayList<BinhLuan> items=null;
	    int resource;
	    
		public BinhLuanCustomAdapter(Context context, int textViewResourceId, ArrayList<BinhLuan> objects) 
		{
			  super(context, textViewResourceId, objects);
			  this.mContext = context;
			  this.resource = textViewResourceId;
			  this.items = objects; 
		}
		  
		@Override
		public Tab1_Home_BinhLuan.BinhLuan getItem(int position) 
		{
			return super.getItem(position);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{			 
	    	  
			  final ViewHolder holder;   
		      if (convertView == null) 
		      {
		    	  LayoutInflater inflater = getLayoutInflater();
		    	  convertView = inflater.inflate(R.layout.tab1_home_binhluan_item, parent, false); 
		    	  
		    	  holder = new ViewHolder();
		    	  holder.id = 		(TextView) convertView.findViewById(R.id.tab1_home_binhluan_id);
		    	  holder.username = 		(TextView) convertView.findViewById(R.id.tab1_home_binhluan_username);
		    	  holder.ngay = 		(TextView) convertView.findViewById(R.id.tab1_home_binhluan_ngay);
		    	  holder.noidung = 	(TextView) convertView.findViewById(R.id.tab1_home_binhluan_noidung);
		    	  convertView.setTag(holder);
		      } else {
		    	  holder = (ViewHolder) convertView.getTag();
		      }
		      final BinhLuan iContent = items.get(position);
		      
		      holder.id.setText(iContent.getId());
		      holder.username.setText("Thành viên: "+iContent.getUsername());
		      holder.ngay.setText("Ngày: "+iContent.getNgay());
		      holder.noidung.setText("Nội dung: "+iContent.getNoidung());
		      
		      convertView.setOnClickListener(new OnClickListener() 
		      {
					@Override
					public void onClick(View v) 
					{
						
					}
		      });
		      return convertView;
		}
		private class ViewHolder 
		{
			   TextView id;
			   TextView username;
			   TextView ngay;
			   TextView noidung;
		}
	}
	//
	//
	public class BinhLuan 
	{
		private String id; 
	    private String username;
	    private String ngay;
	    private String noidung;
	    
	    public BinhLuan(String id, String username ,String ngay, String noidung)
	    {
	        this.id = id;
	        this.username = username;
	        this.ngay = ngay;
	        this.noidung = noidung;
	    }
	    public String getId() { return id;}
	    public String getUsername() { return username;}
	    public String getNgay() { return ngay;}
	    public String getNoidung() { return noidung;}
	    
	    public void setId(String id) {this.id = id;}
	    public void setUsername(String username) {this.username = username;}
	    public void setNgay(String ngay) {this.ngay = ngay;}
	    public void setNoidung(String noidung) {this.noidung= noidung;}
	}
	//
}

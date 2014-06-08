package com.example.doan_didong;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract.RawContacts.Data;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;

import android.view.ViewGroup;
import android.widget.AbsListView;
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
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;


public class Tab9_Video_YeuThich extends Activity
{
	ArrayList<Tab9_Video_YeuThich.Video> itemlistVIDEO_yeuthich;
	JSONParser jsonParser = new JSONParser();
	String json_string;
	
	private FunctionCheck instantFunctionCheck;
	private ProgressDialog pDialog;
	
	private ListView t9_listview_yeuthich_video;
	private VideoYeuThichCustomAdapter adapter;
	private GetAllVideo_First task_first;
	
	boolean isloading;
	int _ok;
	int _start;
	int success;
	int position;
	String iduser;
	String loai;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab9_video_listview);
		t9_listview_yeuthich_video= (ListView)findViewById(R.id.t9_listview_yeuthich_video);
		isloading = false;
		position  = -1;
		_start=1;
		instantFunctionCheck = new FunctionCheck(getApplicationContext());
		itemlistVIDEO_yeuthich = new ArrayList<Tab9_Video_YeuThich.Video>();
		
		Bundle b = getIntent().getExtras();
		iduser = 		b.getString("iduser");
		loai   = 		b.getString("loai");
		Log.e("----id user, id level, tong_socauhoi","="+iduser+"="+loai);
		
		
		if( itemlistVIDEO_yeuthich.size() == 0 )
		{
			if(instantFunctionCheck.checkInternetConnection())
			{
				task_first = new GetAllVideo_First();
		        task_first.execute();
			} else Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			
	        
		} else 
		{
			adapter = new VideoYeuThichCustomAdapter(getApplicationContext(), R.layout.tab9_video_item, itemlistVIDEO_yeuthich);
			t9_listview_yeuthich_video.setAdapter(adapter);
		}
                
		
		
	}
	
	@Override
	public void onBackPressed() 
	{
		Log.e("---release itemlist Tab9 video","---release itemlist Tab9 video");
		itemlistVIDEO_yeuthich = null;
		super.onBackPressed();
	}

	//
	//
	//
	class GetAllVideo_First extends AsyncTask<String, String, String> 
	{
		int success111;
		int all_item;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			success111 = 0;
			isloading=true;
			
			pDialog = new ProgressDialog(Tab9_Video_YeuThich.this);
			pDialog.setMessage("Vui lòng đợi ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("request", "totnghiep_yeuthich_video"));
			params.add(new BasicNameValuePair("iduser", iduser));
			params.add(new BasicNameValuePair("loai", loai));
			
			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_yeuthich_video.php", "POST", params);
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					
					success111=json.getInt(Constant.KEYWORD_SUCCESS);
					all_item = json.getInt("all_item");
					if(all_item==0)
					{
						success111=4;
					}
					else if (success111 == 1) 
					{
						JSONArray jsonArray = json.getJSONArray("all_video"); 
						for (int i = 0; i < jsonArray.length(); i++) 
						{
							JSONObject c = jsonArray.getJSONObject(i);
							String id1 = c.getString("id");
							String title1 =_start +". " + c.getString("title");
							String mo_ta1 = c.getString("mo_ta");
							String link1 = c.getString("link");
							String ngay_tao1 = c.getString("ngay_tao");
							String luot_view1 = c.getString("luot_view");
							String ngay_luu1 = c.getString("ngay_luu");
							
							Tab9_Video_YeuThich.Video item = new Tab9_Video_YeuThich.Video(id1,title1,mo_ta1,link1, luot_view1,ngay_luu1);
							itemlistVIDEO_yeuthich.add(item);
							_start++;
						}
						
					}
				} catch (JSONException e) 
				{
					success111=0;
					e.printStackTrace();
				}
			} else 
				{
					success111=3;
				}

			return null;
		}
		protected void onPostExecute(String file_url) {
			isloading=false;
			pDialog.dismiss();
			if(success111==4)
			{
				taoDialog_all_item("Chưa có video nào được lưu.");
			}
			else if(success111==1)
			{
					adapter = new VideoYeuThichCustomAdapter(getApplicationContext(), R.layout.tab9_video_item, itemlistVIDEO_yeuthich);
					t9_listview_yeuthich_video.setAdapter(adapter);
					
					Log.e("---Kết quả get_lần đầu ", "Success");
			}
			else if(success111==0) 
			{
					Log.d("---Success=0 lỗi internet","---Success=0 lỗi internet");
					taoDialog("Tải dữ liệu không thành công. Click OK thử lại.");
					//Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			}
			else if(success111==3) 
				{
					Log.d("---Success=3 JSON=rỗng", "---Success=3 JSON=rỗng");
					taoDialog("Tải dữ liệu không thành công. Click OK thử lại.");
					//Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
				}
		}

	}
	//
	public void taoDialog(String message)
	{
		Dialog dialog;
        AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(this);
        builder.setTitle("Lỗi !");
		builder.setIcon(R.drawable.icon_stop);
        builder.setMessage(message)
        .setCancelable(false)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	try{
	        			if(instantFunctionCheck.checkInternetConnection())
	        			{
	        				task_first = new GetAllVideo_First();
	        		        task_first.execute();
	        			} 
	        			else Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
        			} 
            		catch(Exception e) { e.printStackTrace(); }
            }
        });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        dialog = builder.create();
        dialog.show();
	}
	
	public void taoDialog_all_item(String msg)
	{
		Dialog dialog;
        AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(this);
        builder.setTitle("Lỗi !");
		builder.setIcon(R.drawable.icon_stop);
        builder.setMessage(msg)
        .setCancelable(false);
        builder.setNegativeButton("Thoát",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        dialog = builder.create();
        dialog.show();
	}
	
	//
	//Class custom adapter quản lý listitem
	//
	public class VideoYeuThichCustomAdapter extends ArrayAdapter<Tab9_Video_YeuThich.Video> 
	{
		
		Context mContext;  
	    ArrayList<Tab9_Video_YeuThich.Video> items=null;
	    int resource;
	    
		public VideoYeuThichCustomAdapter(Context context, int textViewResourceId,ArrayList<Tab9_Video_YeuThich.Video> objects) 
		{
			  super(context, textViewResourceId, objects);
			  this.mContext = context;
			  this.resource = textViewResourceId;
			  this.items = objects; 
		}
		  
		@Override
		public Tab9_Video_YeuThich.Video getItem(int position) 
		{
			return super.getItem(position);
		}
		
		@Override
		public long getItemId(int position) 
		{
			return position;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) 
		{			 
	    	  
			  final ViewHolder holder;   
		      if (convertView == null) 
		      {
		    	  LayoutInflater inflater = getLayoutInflater();
		    	  convertView = inflater.inflate(R.layout.tab9_video_item, parent, false); 
		    	  
		    	  holder = new ViewHolder();
		    	  holder.id = 		(TextView) convertView.findViewById(R.id.tab9_video_id);
		    	  holder.title = 	(TextView) convertView.findViewById(R.id.tab9_video_title);
		    	  holder.mo_ta = 	(TextView) convertView.findViewById(R.id.tab9_video_mo_ta);
		    	  holder.ngay_luu = (TextView) convertView.findViewById(R.id.tab9_video_ngay_luu);
		    	  holder.luot_view =(TextView) convertView.findViewById(R.id.tab9_video_luotview);
		    	  holder.next = 	(Button) convertView.findViewById(R.id.tab9_video_next_btn);
		    	  holder.watch = 	(Button) convertView.findViewById(R.id.tab9_video_watch);
		    	  holder.delete = 	(Button) convertView.findViewById(R.id.tab9_video_delete);
		    	  convertView.setTag(holder);
		      } else {
		    	  holder = (ViewHolder) convertView.getTag();
		      }
		      final Tab9_Video_YeuThich.Video iContent = items.get(position);
		      
		      holder.id.setText(iContent.getId());
		      holder.title.setText(iContent.getTitle());
		      holder.mo_ta.setText(iContent.getMo_ta());
		      holder.luot_view.setText("Xem "+iContent.getLuot_view()+ " lần");
		      holder.ngay_luu.setText("Ngày lưu: "+iContent.getNgay_luu());
		      
		      convertView.setOnClickListener(new OnClickListener() 
		      {
					@Override
					public void onClick(View v) 
					{
						/*Intent in = new Intent(getSherlockActivity(),Tab7_Video_One.class);
						
						in.putExtra("iduser", MainActivity.iduser);
						in.putExtra("idvideo", iContent.getId());
						startActivity(in);*/	
					}
		      });
		      holder.next.setOnClickListener( new View.OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {		
		    		  Intent in = new Intent(getApplicationContext(),Tab9_Video_Watch_One.class);
						
		    		  in.putExtra("iduser", String.valueOf(MainActivity.iduser));
		    		  in.putExtra("idvideo", iContent.getId());
		    		  startActivity(in);
		    	  }
			  });
		      holder.watch.setOnClickListener( new View.OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {	
		    		  Intent in = new Intent(getApplicationContext(),Tab9_Video_Watch_One.class);
						
		    		  in.putExtra("iduser", String.valueOf(MainActivity.iduser));
		    		  in.putExtra("idvideo", iContent.getId());
		    		  startActivity(in);
		    	  }
			  });
		      holder.delete.setOnClickListener( new OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {
		    		  if(instantFunctionCheck.checkInternetConnection())// no Internet connection- Internet
					  {
		    			  Tab9_Video_YeuThich.this.position = position;
		    			  new Video_Delete_YeuThich().execute(holder.id.getText().toString());
					  } 
					  else Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
		    		  
		    	  }
			  });
		      return convertView;
		      
		}
		private class ViewHolder 
		{
			   TextView id;
			   TextView title;
			   TextView mo_ta;
			   TextView ngay_luu;
			   TextView luot_view;
			   Button next;
			   Button watch;
			   Button delete;
		}
	}
	
	
	//
	//Lưu từ điển yêu thích
	//
	class Video_Delete_YeuThich extends AsyncTask<String , String, String> 
	{
		int success111;
		int da_ton_tai;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			success=0;
			da_ton_tai=0;
			pDialog = new ProgressDialog(Tab9_Video_YeuThich.this);
			pDialog.setMessage("Đang xử lý ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... param) {
			String idloai = param[0];
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("idloai", idloai ));
			params.add(new BasicNameValuePair("iduser", iduser ));
			params.add(new BasicNameValuePair("loai", loai ));
			params.add(new BasicNameValuePair("request", "totnghiep_yeuthich_tudien_delete"));
			Log.e("----idloai, iduser, loai =","---"+idloai+ "="+iduser+ "="+loai);
			
			
			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_yeuthich_tudien_delete.php", "POST", params);
			Log.e("------json STRING","="+json_string);
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					
					da_ton_tai = json.getInt(Constant.KEYWORD_USERNAME_DA_TON_TAI);
					success111 = json.getInt(Constant.KEYWORD_SUCCESS); 
					if (success111 == 1) 
					{
						
					} else 
					{
						success111=0;
						da_ton_tai=0;
					}
				} catch (JSONException e) 
				{
					e.printStackTrace(); 
					success111=0;
					da_ton_tai=0;
				}
			} else {success111=3; da_ton_tai=0; };

			return null;
		}
		protected void onPostExecute(String file_url) 
		{
			pDialog.dismiss();
			if(success111==1 && da_ton_tai == 1)
			{
				Toast.makeText(getApplicationContext(), "Xóa khỏi yêu thích thành công.", Toast.LENGTH_SHORT).show();
				if(position != -1) 
				{
					itemlistVIDEO_yeuthich.remove(position);
					adapter.notifyDataSetChanged();
				}
				position = -1;
			}
			if(success111==0 && da_ton_tai == 0)
			{
				position = -1;
				Toast.makeText(getApplicationContext(), "Không thành công. Kiểm tra internet.", Toast.LENGTH_SHORT).show();
			}
			if(success111==3 && da_ton_tai == 0)
			{
				position = -1;
				Toast.makeText(getApplicationContext(), "Không thành công. Kiểm tra internet.", Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	//
	//
	public class Video 
	{
		private String id; 
	    private String title;
	    private String mo_ta;
	    private String link;
	    private String luot_view;
	    private String ngay_luu;
	    public Video(String id, String title,String mo_ta, String link, String luot_view, String ngay_luu )
	    {
	        this.id = id;
	        this.title = title;
	        this.mo_ta = mo_ta;
	        this.link = link;
	        this.luot_view = luot_view;
	        this.ngay_luu = ngay_luu;
	        
	    }
	    public String getId() { return id;}
	    public String getTitle() { return title;}
	    public String getMo_ta() { return mo_ta;}
	    public String getLink() { return link;}
	    public String getNgay_luu() { return ngay_luu;}
	    public String getLuot_view() { return luot_view;}
	    
	    public void setId(String id) {this.id = id;}
	    public void setTitle(String title) {this.title = title;}
	    public void setMo_ta(String mota) {this.mo_ta = mota;}
	    public void setLink(String link) {this.link = link;}
	    public void setNgay_luu(String ngay_luu) {this.ngay_luu = ngay_luu;}
	    public void setLuot_view(String luot_view) {this.luot_view = luot_view;}
	}
	//
	//
}


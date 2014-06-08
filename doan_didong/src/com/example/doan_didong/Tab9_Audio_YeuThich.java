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


public class Tab9_Audio_YeuThich extends Activity
{
	ArrayList<Tab9_Audio_YeuThich.Audio> itemlistAUDIO_yeuthich;
	JSONParser jsonParser = new JSONParser();
	String json_string;
	
	private FunctionCheck instantFunctionCheck;
	private ProgressDialog pDialog;
	
	private ListView t9_listview_yeuthich_audio;
	private AudioYeuThichCustomAdapter adapter;
	private GetAllAudio_First task_first;
	
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
		setContentView(R.layout.tab9_audio_listview);
		t9_listview_yeuthich_audio= (ListView)findViewById(R.id.t9_listview_yeuthich_audio);
		isloading = false;
		position  = -1;
		_start=1;
		instantFunctionCheck = new FunctionCheck(getApplicationContext());
		itemlistAUDIO_yeuthich = new ArrayList<Tab9_Audio_YeuThich.Audio>();
		
		Bundle b = getIntent().getExtras();
		iduser = 		b.getString("iduser");
		loai   = 		b.getString("loai");
		Log.e("----id user, id level, tong_socauhoi","="+iduser+"="+loai);
		
		
		if( itemlistAUDIO_yeuthich.size() == 0 )
		{
			if(instantFunctionCheck.checkInternetConnection())
			{
				task_first = new GetAllAudio_First();
		        task_first.execute();
			} else Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			
	        
		} else 
		{
			adapter = new AudioYeuThichCustomAdapter(getApplicationContext(), R.layout.tab9_audio_item, itemlistAUDIO_yeuthich);
			t9_listview_yeuthich_audio.setAdapter(adapter);
		}	
	}
	
	@Override
	public void onBackPressed() 
	{
		Log.e("---release itemlist Tab9 Audio","---release itemlist Tab9 Audio");
		itemlistAUDIO_yeuthich = null;
		super.onBackPressed();
	}

	//
	//
	//
	class GetAllAudio_First extends AsyncTask<String, String, String> 
	{
		int success111;
		int all_item;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			success111 = 0;
			isloading=true;
			
			pDialog = new ProgressDialog(Tab9_Audio_YeuThich.this);
			pDialog.setMessage("Vui lòng đợi ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("request", "totnghiep_yeuthich_audio"));
			params.add(new BasicNameValuePair("iduser", iduser));
			params.add(new BasicNameValuePair("loai", loai));
			
			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_yeuthich_audio.php", "POST", params);
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
						JSONArray jsonArray = json.getJSONArray("all_audio"); 
						for (int i = 0; i < jsonArray.length(); i++) 
						{
							JSONObject c = jsonArray.getJSONObject(i);
							String id1 = c.getString("id");
							String title1 =_start + ". " + c.getString("title");
							String text1 = c.getString("text");
							String luot_view1 = c.getString("luot_view");
							String ngay_luu = c.getString("ngay_luu");
							
							Tab9_Audio_YeuThich.Audio item = new Tab9_Audio_YeuThich.Audio(id1,title1,text1, luot_view1,ngay_luu);
							itemlistAUDIO_yeuthich.add(item);
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
				taoDialog_all_item("Chưa có bài nghe nào được lưu.");
			}
			else if(success111==1)
			{
					adapter = new AudioYeuThichCustomAdapter(getApplicationContext(), R.layout.tab9_audio_item, itemlistAUDIO_yeuthich);
					t9_listview_yeuthich_audio.setAdapter(adapter);
					
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
	        				task_first = new GetAllAudio_First();
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
	public class AudioYeuThichCustomAdapter extends ArrayAdapter<Tab9_Audio_YeuThich.Audio> 
	{
		
		Context mContext;  
	    ArrayList<Tab9_Audio_YeuThich.Audio> items=null;
	    int resource;
	    
		public AudioYeuThichCustomAdapter(Context context, int textViewResourceId,ArrayList<Tab9_Audio_YeuThich.Audio> objects) 
		{
			  super(context, textViewResourceId, objects);
			  this.mContext = context;
			  this.resource = textViewResourceId;
			  this.items = objects; 
		}
		  
		@Override
		public Tab9_Audio_YeuThich.Audio getItem(int position) 
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
		    	  convertView = inflater.inflate(R.layout.tab9_audio_item, parent, false); 
		    	  
		    	  holder = new ViewHolder();
		    	  holder.id = 		(TextView) convertView.findViewById(R.id.tab9_audio_id);
		    	  holder.title = 	(TextView) convertView.findViewById(R.id.tab9_audio_title);
		    	  holder.text = 	(TextView) convertView.findViewById(R.id.tab9_audio_text);
		    	  holder.ngay_luu = (TextView) convertView.findViewById(R.id.tab9_audio_ngay_luu);
		    	  holder.luot_view =(TextView) convertView.findViewById(R.id.tab9_audio_luotview);
		    	  holder.next = 	(Button) convertView.findViewById(R.id.tab9_audio_next_btn);
		    	  holder.listen = 	(Button) convertView.findViewById(R.id.tab9_audio_listen);
		    	  holder.view = 	(Button) convertView.findViewById(R.id.tab9_audio_view);
		    	  holder.delete = 	(Button) convertView.findViewById(R.id.tab9_audio_delete);
		    	  
		    	  convertView.setTag(holder);
		      } else {
		    	  holder = (ViewHolder) convertView.getTag();
		      }
		      final Tab9_Audio_YeuThich.Audio iContent = items.get(position);
		      
		      holder.id.setText(iContent.getId());
		      holder.title.setText(iContent.getTieude());
		      holder.text.setText(iContent.getTomtat());
		      holder.luot_view.setText("Xem "+iContent.getLuot_view()+ " lần");
		      holder.ngay_luu.setText("Ngày lưu: "+iContent.getNgay_luu());
		      
		      convertView.setOnClickListener(new OnClickListener() 
		      {
					@Override
					public void onClick(View v) 
					{
//						Intent in = new Intent(getSherlockActivity(),Tab6_Listen_One_Audio.class);
//						
//						in.putExtra("iduser", MainActivity.iduser);
//						in.putExtra("idaudio", iContent.getId());
//						in.putExtra("luot_view", iContent.getLuot_view());
//						startActivity(in);	
					}
		      });
		      holder.next.setOnClickListener( new View.OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {
		    		  	Intent in = new Intent(getApplicationContext(),Tab9_Audio_Listen_One.class);
						
						in.putExtra("iduser", MainActivity.iduser);
						in.putExtra("idaudio", iContent.getId());
						startActivity(in);		
		    	  }
			  });
		      holder.listen.setOnClickListener( new View.OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {
		    		  	Intent in = new Intent(getApplicationContext(),Tab9_Audio_Listen_One.class);
						
						in.putExtra("iduser", MainActivity.iduser);
						in.putExtra("idaudio", iContent.getId());
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
		    			  Tab9_Audio_YeuThich.this.position = position;
		    			  new Audio_Delete_YeuThich().execute(holder.id.getText().toString());
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
			   TextView text;
			   TextView luot_view;
			   TextView ngay_luu;
			   Button next;
			   Button listen;
			   Button delete;
			   Button view;
		}
	}
	
	
	//
	//Lưu từ điển yêu thích
	//
	class Audio_Delete_YeuThich extends AsyncTask<String , String, String> 
	{
		int success111;
		int da_ton_tai;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			success=0;
			da_ton_tai=0;
			pDialog = new ProgressDialog(Tab9_Audio_YeuThich.this);
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
					itemlistAUDIO_yeuthich.remove(position);
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
	public class Audio 
	{
		private String id; 
	    private String tieude;
	    private String tomtat;
	    private String luot_view;
	    private String ngay_luu;
	    public Audio(String id, String title, String text, String luot_view, String ngay_luu )
	    {
	        this.id = id;
	        this.tieude = title;
	        this.tomtat = text;
	        this.luot_view = luot_view;
	        this.ngay_luu = ngay_luu;
	    }
	    public String getId() { return id;}
	    public String getTieude() { return tieude;}
	    public String getTomtat() { return tomtat;}
	    public String getLuot_view() { return luot_view;}
	    public String getNgay_luu() { return ngay_luu;}
	    
	    public void setId(String id) {this.id = id;}
	    public void setTieude(String tieude) {this.tieude = tieude;}
	    public void setTomtat(String tomtat) {this.tomtat = tomtat;}
	    public void setLuot_view(String luot_view) {this.luot_view = luot_view;}
	    public void setNgay_luu(String luot_view) {this.ngay_luu = luot_view;}
	}
	//
	//
}


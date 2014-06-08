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


public class Tab9_ThanhNgu_YeuThich extends Activity
{
	ArrayList<Tab9_ThanhNgu_YeuThich.ThanhNgu> itemlistTHANHNGU_yeuthich;
	JSONParser jsonParser = new JSONParser();
	String json_string;
	
	private FunctionCheck instantFunctionCheck;
	private ProgressDialog pDialog;
	
	private ListView t9_listview_yeuthich_thanhngu;
	private ThanhNguYeuThichCustomAdapter adapter;
	private GetAllThanhNgu_First task_first;
	
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
		setContentView(R.layout.tab9_thanhngu_listview);
		t9_listview_yeuthich_thanhngu= (ListView)findViewById(R.id.t9_listview_yeuthich_thanhngu);
		isloading = false;
		position  = -1;
		_start=1;
		instantFunctionCheck = new FunctionCheck(getApplicationContext());
		itemlistTHANHNGU_yeuthich = new ArrayList<Tab9_ThanhNgu_YeuThich.ThanhNgu>();
		
		Bundle b = getIntent().getExtras();
		iduser = 		b.getString("iduser");
		loai   = 		b.getString("loai");
		Log.e("----id user, id level, tong_socauhoi","="+iduser+"="+loai);
		
		
		if( itemlistTHANHNGU_yeuthich.size() == 0 )
		{
			if(instantFunctionCheck.checkInternetConnection())
			{
				task_first = new GetAllThanhNgu_First();
		        task_first.execute();
			} else Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			
	        
		} else 
		{
			adapter = new ThanhNguYeuThichCustomAdapter(getApplicationContext(), R.layout.tab9_thanhngu_item, itemlistTHANHNGU_yeuthich);
			t9_listview_yeuthich_thanhngu.setAdapter(adapter);
		}
                
		
		
	}
	
	@Override
	public void onBackPressed() 
	{
		Log.e("---release itemlist Tab9 ThanhNgu","---release itemlist Tab9 ThanhNgu");
		itemlistTHANHNGU_yeuthich = null;
		super.onBackPressed();
	}

	//
	//
	//
	class GetAllThanhNgu_First extends AsyncTask<String, String, String> 
	{
		int success111;
		int all_item;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			success111 = 0;
			isloading=true;
			
			pDialog = new ProgressDialog(Tab9_ThanhNgu_YeuThich.this);
			pDialog.setMessage("Vui lòng đợi ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("request", "totnghiep_yeuthich_thanhngu"));
			params.add(new BasicNameValuePair("iduser", iduser));
			params.add(new BasicNameValuePair("loai", loai));
			
			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_yeuthich_thanhngu.php", "POST", params);
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
						JSONArray jsonArray = json.getJSONArray("all_thanhngu"); 
						for (int i = 0; i < jsonArray.length(); i++) 
						{
							JSONObject c = jsonArray.getJSONObject(i);
							String id1 = c.getString("id");
							String thanhngu1 =_start +". " + c.getString("thanhngu");
							String noidung1 = c.getString("noidung");
							String vidu1 = c.getString("vidu");
							String ngay_luu = c.getString("ngay_luu");
							
							Tab9_ThanhNgu_YeuThich.ThanhNgu item = new Tab9_ThanhNgu_YeuThich.ThanhNgu(id1,thanhngu1,noidung1,vidu1, 0,ngay_luu);
							itemlistTHANHNGU_yeuthich.add(item);
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
				taoDialog_all_item();
			}
			else if(success111==1)
			{
					adapter = new ThanhNguYeuThichCustomAdapter(getApplicationContext(), R.layout.tab9_thanhngu_item, itemlistTHANHNGU_yeuthich);
					t9_listview_yeuthich_thanhngu.setAdapter(adapter);
					
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
	        				task_first = new GetAllThanhNgu_First();
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
	
	public void taoDialog_all_item()
	{
		Dialog dialog;
        AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(this);
        builder.setTitle("Lỗi !");
		builder.setIcon(R.drawable.icon_stop);
        builder.setMessage("Chưa có thành ngữ nào được lưu.")
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
	public class ThanhNguYeuThichCustomAdapter extends ArrayAdapter<Tab9_ThanhNgu_YeuThich.ThanhNgu> 
	{
		
		Context mContext;  
	    ArrayList<Tab9_ThanhNgu_YeuThich.ThanhNgu> items=null;
	    int resource;
	    
		public ThanhNguYeuThichCustomAdapter(Context context, int textViewResourceId,ArrayList<Tab9_ThanhNgu_YeuThich.ThanhNgu> objects) 
		{
			  super(context, textViewResourceId, objects);
			  this.mContext = context;
			  this.resource = textViewResourceId;
			  this.items = objects; 
		}
		  
		@Override
		public Tab9_ThanhNgu_YeuThich.ThanhNgu getItem(int position) 
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
		    	  convertView = inflater.inflate(R.layout.tab9_thanhngu_item, parent, false); 
		    	  
		    	  holder = new ViewHolder();
		    	  holder.id = 		(TextView) convertView.findViewById(R.id.tab9_thanhngu_id);
		    	  holder.thanhngu = 		(TextView) convertView.findViewById(R.id.tab9_thanhngu_thanhngu);
		    	  holder.noidung = 	(TextView) convertView.findViewById(R.id.tab9_thanhngu_noidung);
		    	  holder.vidu = 	(TextView) convertView.findViewById(R.id.tab9_thanhngu_vidu);
		    	  holder.ngay_luu = 	(TextView) convertView.findViewById(R.id.tab9_thanhngu_ngay_luu);
		    	  holder.delete = 	(Button) convertView.findViewById(R.id.tab9_thanhngu_delete);
		    	  convertView.setTag(holder);
		      } else {
		    	  holder = (ViewHolder) convertView.getTag();
		      }
		      final Tab9_ThanhNgu_YeuThich.ThanhNgu iContent = items.get(position);
		      
		      if(iContent.getIs_gone()==0) holder.vidu.setVisibility(TextView.GONE);
		      	else holder.vidu.setVisibility(TextView.VISIBLE);
		      holder.id.setText(iContent.getId());
		      holder.thanhngu.setText(iContent.getThanhngu());
		      holder.noidung.setText(iContent.getNoidung());
		      if(iContent.getVidu().length() == 0) holder.vidu.setText("Không có ví dụ.");
		      else holder.vidu.setText(iContent.getVidu());
		      holder.ngay_luu.setText("Ngày lưu: "+iContent.getNgay_luu());
		      
		      convertView.setOnClickListener(new OnClickListener() 
		      {
					@Override
					public void onClick(View v) 
					{
						if(holder.vidu.getVisibility() == TextView.GONE)
						{
								holder.vidu.setVisibility(TextView.VISIBLE);
								iContent.setIs_gone(1);
						}
		    			else
		    			{
		    					holder.vidu.setVisibility(TextView.GONE);
		    					iContent.setIs_gone(0);
		    			}
					}
		      });
		      holder.delete.setOnClickListener( new OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {
		    		  if(instantFunctionCheck.checkInternetConnection())// no Internet connection- Internet
					  {
		    			  Tab9_ThanhNgu_YeuThich.this.position = position;
		    			  new ThanhNgu_Delete_YeuThich().execute(holder.id.getText().toString());
					  } 
					  else Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
		    		  
		    	  }
			  });
		      return convertView;
		}
		private class ViewHolder 
		{
			TextView id;
			TextView thanhngu;
			TextView noidung;
			TextView vidu;
			TextView ngay_luu;
			Button delete;
		}
	}
	
	
	//
	//Lưu từ điển yêu thích
	//
	class ThanhNgu_Delete_YeuThich extends AsyncTask<String , String, String> 
	{
		int success111;
		int da_ton_tai;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			success=0;
			da_ton_tai=0;
			pDialog = new ProgressDialog(Tab9_ThanhNgu_YeuThich.this);
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
					itemlistTHANHNGU_yeuthich.remove(position);
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
	public class ThanhNgu 
	{
		private String id; 
	    private String thanhngu;
	    private String noidung;
	    private String vidu;
	    private String ngay_luu;
	    private int is_gone;
	    
	    public ThanhNgu(String id, String thanhngu, String noidung, String vidu, int is_gone, String ngay_luu )
	    {
	        this.id = id;
	        this.thanhngu = thanhngu;
	        this.noidung = noidung;
	        this.vidu = vidu;
	        this.is_gone=is_gone;
	        this.ngay_luu=ngay_luu;
	    }
	    public String getId() { return id;}
	    public String getThanhngu() { return thanhngu;}
	    public String getNoidung() { return noidung;}
	    public String getVidu() { return vidu;}
	    public String getNgay_luu() { return ngay_luu;}
	    public int getIs_gone()		{return is_gone;}
	 
	    public void setId(String id) {this.id = id;}
	    public void setThanhngu(String tudien) {this.thanhngu = tudien;}
	    public void setNoidung(String tuloai) {this.noidung = tuloai;}
	    public void setVidu(String phienam) {this.vidu = phienam;}
	    public void setNgay_luu(String ngay_luu){this.ngay_luu=ngay_luu;}
	    public void setIs_gone(int isgone){this.is_gone=isgone;}
	}
	//
	//
}


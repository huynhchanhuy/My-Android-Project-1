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

import com.example.doan_didong.MainActivity.TuDien;
import com.example.doan_didong.Tab2_ThayDoiMatKhauActivity.GetInformationProgress;
import com.example.doan_didong.Tab3_TuDien.GetAllTuDien_First;
import com.example.doan_didong.Tab3_TuDien.GetAllTuDien_Second;
import com.example.doan_didong.Tab3_TuDien.TuDienCustomAdapter;

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


public class Tab9_TuThongDung_YeuThich extends Activity
{
	ArrayList<Tab9_TuThongDung_YeuThich.TuDien> itemlistTUDIEN_yeuthich;
	int itemlistTUDIEN_yeuthich_all;
	JSONParser jsonParser = new JSONParser();
	String json_string;
	
	private FunctionCheck instantFunctionCheck;
	private ProgressDialog pDialog;
	
	private ListView t9_yeuthich_tudien_listview;
	private TuDienYeuThichCustomAdapter adapter;
	private GetAllTuDien_First task_first;
	
	private boolean isloading;
	int _ok;
	int success;
	int position;
	String iduser;
	String loai;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab9_tuthongdung_listview);
		t9_yeuthich_tudien_listview= (ListView)findViewById(R.id.t9_yeuthich_tudien);
		isloading = false;
		position  = -1;
		instantFunctionCheck = new FunctionCheck(getApplicationContext());
		itemlistTUDIEN_yeuthich = new ArrayList<Tab9_TuThongDung_YeuThich.TuDien>();
		
		Bundle b = getIntent().getExtras();
		iduser = 		b.getString("iduser");
		loai   = 		b.getString("loai");
		Log.e("----id user, id level, tong_socauhoi","="+iduser+"="+loai);
		
		
		if( itemlistTUDIEN_yeuthich.size() == 0 )
		{
			if(instantFunctionCheck.checkInternetConnection())
			{
				task_first = new GetAllTuDien_First();
		        task_first.execute();
			} else Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			
	        
		} else 
		{
			adapter = new TuDienYeuThichCustomAdapter(getApplicationContext(), R.layout.tab9_tuthongdung_item, itemlistTUDIEN_yeuthich);
			t9_yeuthich_tudien_listview.setAdapter(adapter);
		}
                
		
		
	}
	
	@Override
	public void onBackPressed() 
	{
		itemlistTUDIEN_yeuthich = null;
		Log.e("---release itemlist Tab9 TuDien","---release itemlist Tab9 TuDien");
		super.onBackPressed();
	}

	//
	//
	//
	class GetAllTuDien_First extends AsyncTask<String, String, String> 
	{
		int success111;
		int all_item;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			success111 = 0;
			isloading=true;
			
			pDialog = new ProgressDialog(Tab9_TuThongDung_YeuThich.this);
			pDialog.setMessage("Vui lòng đợi ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("request", "post_tudien_get_all"));
			params.add(new BasicNameValuePair("iduser", iduser));
			params.add(new BasicNameValuePair("loai", loai));
			
			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_yeuthich_tudien.php", "POST", params);
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					
					success111=json.getInt("success");
					all_item = json.getInt("all_item");
					if(all_item==0)
					{
						success111=4;
					}
					else if (success111 == 1) 
					{
						JSONArray jsonArray = json.getJSONArray("all_tudien"); 
						for (int i = 0; i < jsonArray.length(); i++) 
						{
							JSONObject c = jsonArray.getJSONObject(i);
							String id1 = c.getString("id");
							String tu1 = c.getString("tu");
							String loaitu1 = c.getString("loaitu");
							String phienam1 = c.getString("phienam");
							String dichnghia1 = c.getString("dichnghia");
							String nhieunghia1 = c.getString("nhieunghia");
							String link_audio1 = c.getString("link_audio");
							String ngay_luu = c.getString("ngay_luu");
							
							Tab9_TuThongDung_YeuThich.TuDien item = new Tab9_TuThongDung_YeuThich.TuDien(id1,tu1,loaitu1,phienam1,dichnghia1,nhieunghia1,link_audio1, 0,ngay_luu);
							itemlistTUDIEN_yeuthich.add(item);
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
					adapter = new TuDienYeuThichCustomAdapter(getApplicationContext(), R.layout.tab9_tuthongdung_item, itemlistTUDIEN_yeuthich);
					t9_yeuthich_tudien_listview.setAdapter(adapter);
					
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
	        				task_first = new GetAllTuDien_First();
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
	//
	public void taoDialog_all_item()
	{
		Dialog dialog;
        AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(this);
        builder.setTitle("Lỗi !");
		builder.setIcon(R.drawable.icon_stop);
        builder.setMessage("Bạn chưa lưu gì.")
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
	public class TuDienYeuThichCustomAdapter extends ArrayAdapter<Tab9_TuThongDung_YeuThich.TuDien> 
	{
		
		Context mContext;  
	    ArrayList<Tab9_TuThongDung_YeuThich.TuDien> items=null;
	    int resource;
	    
		public TuDienYeuThichCustomAdapter(Context context, int textViewResourceId,ArrayList<Tab9_TuThongDung_YeuThich.TuDien> objects) 
		{
			  super(context, textViewResourceId, objects);
			  this.mContext = context;
			  this.resource = textViewResourceId;
			  this.items = objects; 
		}
		  
		@Override
		public Tab9_TuThongDung_YeuThich.TuDien getItem(int position) 
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
		    	  convertView = inflater.inflate(R.layout.tab9_tuthongdung_item, parent, false); 
		    	  
		    	  holder = new ViewHolder();
		    	  holder.id = 		(TextView) convertView.findViewById(R.id.tab9_tudien_id);
		    	  holder.tu = 		(TextView) convertView.findViewById(R.id.tab9_tudien_tu);
		    	  holder.loaitu = 	(TextView) convertView.findViewById(R.id.tab9_tudien_loaitu);
		    	  holder.phienam = 	(TextView) convertView.findViewById(R.id.tab9_tudien_phienam);
		    	  holder.dichnghia = (TextView) convertView.findViewById(R.id.tab9_tudien_dichnghia);
		    	  holder.nhieunghia = (TextView) convertView.findViewById(R.id.tab9_tudien_nhieunghia);
		    	  holder.link_audio = (TextView) convertView.findViewById(R.id.tab9_tudien_link_audio);
		    	  holder.ngay_luu = (TextView) convertView.findViewById(R.id.tab9_tudien_ngay_luu);
		    	  holder.delete = 	(Button) convertView.findViewById(R.id.tab9_tudien_delete);
		    	  holder.listen = 	(Button) convertView.findViewById(R.id.tab9_tudien_listen);
		    	  convertView.setTag(holder);
		      } else {
		    	  holder = (ViewHolder) convertView.getTag();
		      }
		      final Tab9_TuThongDung_YeuThich.TuDien iContent = items.get(position);
		      
		      if(iContent.getIs_gone()==0) holder.nhieunghia.setVisibility(TextView.GONE);
		      	else holder.nhieunghia.setVisibility(TextView.VISIBLE);
		      holder.id.setText(iContent.getId());
		      holder.tu.setText(iContent.getTu());
		      holder.loaitu.setText(iContent.getLoaitu());
		      holder.phienam.setText(iContent.getPhienam());
		      holder.dichnghia.setText(iContent.getDichnghia());
		      holder.nhieunghia.setText(iContent.getNhieunghia());
		      holder.link_audio.setText(iContent.getLink_audio());
		      holder.ngay_luu.setText("Ngày lưu: "+iContent.getNgay_luu());
		      
		      convertView.setOnClickListener(new OnClickListener() 
		      {
					@Override
					public void onClick(View v) 
					{
						if(holder.nhieunghia.getVisibility() == TextView.GONE)
						{
								holder.nhieunghia.setVisibility(TextView.VISIBLE);
								iContent.setIs_gone(1);
						}
		    			else
		    			{
		    					holder.nhieunghia.setVisibility(TextView.GONE);
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
		    			  Tab9_TuThongDung_YeuThich.this.position = position;
		    			  new TuDien_Delete_YeuThich().execute(holder.id.getText().toString());
						} 
		    		  else 
							Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
		    		  
		    	  }
			  });
		      holder.listen.setOnClickListener( new OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {
		    		  	if(instantFunctionCheck.checkInternetConnection())// no Internet connection- Internet
						{
		    		  		String filename = holder.link_audio.getText().toString();
				    		new ListenTuDien().execute(filename);
						} 
		    		  	else 
							Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show(); 
		    		  
		    	  }
			  });
		      return convertView;
		}
		private class ViewHolder 
		{
			   TextView id;
			   TextView tu;
			   TextView loaitu;
			   TextView phienam;
			   TextView dichnghia;
			   TextView nhieunghia;
			   TextView link_audio;
			   TextView ngay_luu;
			   Button delete;
			   Button listen;
		}
	}
	
	
	//
	//Lưu từ điển yêu thích
	//
	class TuDien_Delete_YeuThich extends AsyncTask<String , String, String> 
	{
		int success111;
		int da_ton_tai;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			success=0;
			da_ton_tai=0;
			pDialog = new ProgressDialog(Tab9_TuThongDung_YeuThich.this);
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
				Toast.makeText(getApplicationContext(), "Xóa khỏi yêu thích.", Toast.LENGTH_SHORT).show();
				if(position != -1) 
				{
					itemlistTUDIEN_yeuthich.remove(position);
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
	//
	//
	class ListenTuDien extends AsyncTask<String , String, String>
	{
		int no_file;
		int thanh_cong;
		MediaPlayer mp;
		String fullPath;
		String filename;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			no_file=0;
			thanh_cong=0;
			mp = new MediaPlayer();
			fullPath = Environment.getExternalStorageDirectory() + "/hocngoaingu/tudien/";
		    
			File folder = new File(Environment.getExternalStorageDirectory() + "/hocngoaingu/tudien");
			if (folder.exists() && folder.isDirectory() )
			{
			    Log.e("---folder đã tồn tại","--folder đã tồn tại"+folder.getPath());
			} else if (!folder.exists() || !folder.isDirectory())
			{
			    if(folder.mkdirs()) 
			    	Log.e("---folder không tồn tại, tạo thành công","---folder không tồn tại, tạo thành công"+folder.getPath());
			    else Log.e("---error permission, folder không tồn tại","---Error permisson, tạo không thành công"+folder.getPath());
			}
		}

		protected String doInBackground(String... param) {
			filename = param[0];
			File filename_fullpath = new File(fullPath + filename);
		    if(filename_fullpath.isFile() && filename_fullpath.exists())
		    {
		    	Log.e("---file tồn tại PLAY","---file tồn tại PLAY "+filename_fullpath.getPath());
				try {
						    
						mp.setDataSource(filename_fullpath.getPath());
			  		    mp.prepare();
			  		    mp.setLooping(false);
			  		    
				} 	catch (FileNotFoundException e) { e.printStackTrace(); }
					catch (IOException e) 			{ e.printStackTrace(); }
		    }
		    else 
		    {
		    	Log.e("---file không tồn tại DOWNLOAD","---file k ton tai, DOWNLOAD "+filename_fullpath.getPath());
		    	no_file=1;
		    }
			return null;
		}
		protected void onPostExecute(String file_url) 
		{
			if(no_file==0)
			{
				try {
					if(mp!=null) 
					{
						mp.start();
						Handler handler = new Handler();
				        handler.postDelayed(new Runnable() 
				        {
				            public void run() {
				            	Log.e("Sau 2s","Sau 2s release MP");
				            	mp.reset(); 
								mp.release();
				            }}, 2000);
						
					}
				} catch (Exception e) {
				       e.printStackTrace();
				}
			} else if(no_file==1)
			{
				if(instantFunctionCheck.checkInternetConnection())
				{
					new DownloadFileAsync().execute(filename, fullPath + filename);
				} 
    		  	else 
					Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	
	class DownloadFileAsync extends AsyncTask<String, String, String> 
	{	  
		String filename;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Tab9_TuThongDung_YeuThich.this);
			pDialog.setMessage("Downloading file...");
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setCancelable(false);
			pDialog.show();
			_ok=0;
		}

		@Override
		protected String doInBackground(String... param) {
			filename = param[0];
			String file_server = Constant.BASE_URL_SERVER+ "tudien/" + param[0];
			String file_sdcard = param[1];
			int count;
		try {
				URL url = new URL(file_server);
				HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
				conexion.setConnectTimeout(10000);
				conexion.setRequestMethod("POST");
				conexion.connect();
				int lenghtOfFile = conexion.getContentLength();
				Log.e("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
				if(conexion.getResponseCode() == HttpURLConnection.HTTP_OK && lenghtOfFile > 1024)
				{
					_ok=1;
					InputStream input = new BufferedInputStream(conexion.getInputStream());
					OutputStream output = new FileOutputStream(file_sdcard);
		
					byte data[] = new byte[1024];
		
					long total = 0;
		
					while ((count = input.read(data)) != -1)
					{
						total += count;
						publishProgress(""+(int)((total*100)/lenghtOfFile));
						output.write(data, 0, count);
					}
		
					output.flush();
					output.close();
					input.close();
					conexion.disconnect();
				} else _ok=0;
				
		} catch (Exception e) { e.printStackTrace(); }
		return null;

		}
		protected void onProgressUpdate(String... progress) 
		{
			 pDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(String unused) {
			pDialog.dismiss();
			if(_ok==1) { new ListenTuDien().execute(filename); _ok=0; }
			else if(_ok==0) Toast.makeText(getApplicationContext(), "Không thành công. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
		}
	}
	//
	//
	public class TuDien 
	{
		private String id; 
	    private String tu;
	    private String loaitu;
	    private String phienam;
	    private String dichnghia;
	    private String nhieunghia;
	    private String link_audio;
	    private String ngay_luu;
	    private int is_gone;
	    
	    public TuDien(String id, String tudien, String phienam, String loaitu, 
	    		String dichnghia, String nghiathem, String audio, int is_gone, String ngay_luu )
	    {
	        this.id = id;
	        this.tu = tudien;
	        this.loaitu = phienam;
	        this.phienam = loaitu;
	        this.dichnghia = dichnghia;
	        this.nhieunghia = nghiathem;
	        this.link_audio = audio;
	        this.ngay_luu = ngay_luu;
	        this.is_gone=is_gone;
	    }
	    
	    public String getId() { return id;}
	    public String getTu() { return tu;}
	    public String getLoaitu() { return loaitu;}
	    public String getPhienam() { return phienam;}
	    
	    public String getDichnghia() { return dichnghia;}
	    public String getNhieunghia() { return nhieunghia;}
	    public String getLink_audio() { return link_audio;}
	    public String getNgay_luu() { return ngay_luu;}
	    public int getIs_gone()		{return is_gone;}
	    
	 
	    public void setId(String id) {this.id = id;}
	    public void setTu(String tudien) {this.tu = tudien;}
	    public void setLoaitu(String tuloai) {this.loaitu = tuloai;}
	    public void setPhienam(String phienam) {this.phienam = phienam;}
	    
	    public void setDichnghia(String dichnghia) {this.dichnghia = dichnghia;}
	    public void setNhieunghia(String nghiathem) {this.nhieunghia = nghiathem;}
	    
	    public void setLink_audio(String audio) {this.link_audio = audio;}
	    public void setNgay_luu(String ngay_luu) {this.ngay_luu = ngay_luu;}
	    public void setIs_gone(int isgone){this.is_gone=isgone;}
	}
	//
	//
}


package com.example.doan_didong;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.doan_didong.Tab6_Listen_One_Audio.Audio_Remove;
import com.example.doan_didong.Tab6_Listen_One_Audio.Audio_Save;
import com.example.doan_didong.Tab6_Listen_One_Audio.Listen_One_AudioProgress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class Tab9_Video_Watch_One extends Activity 
{
	private ProgressDialog pDialog;
	private FunctionCheck instantFunctionCheck;
	JSONParser jsonParser = new JSONParser();
	Bundle b;
	
	public int iduser;
	public String idvideo;
	public String link_video_link;
	
	TextView video_id, video_title, video_link, video_ngay_tao, video_luot_view;
	Button video_btnplay;
	CheckBox video_yeuthich;
	RelativeLayout video_one_information;
	boolean isFirstClick;
	
	
	VideoView video;
	MediaController media;
	Uri uri_video ;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
       super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE); 
       setContentView(R.layout.tab9_video_watch_one);
       //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//set không cho xoay màn hình
       getWindow().setFormat(PixelFormat.TRANSLUCENT);
       
       instantFunctionCheck = new FunctionCheck(getApplicationContext());//tạo đối tượng kiểm tra kết nối internet
       isFirstClick=false;
		
       b = getIntent().getExtras();
       iduser 		= b.getInt("iduser");
       idvideo 	= b.getString("idvideo");
       
       video = (VideoView) findViewById(R.id.tab9_video_one_view);
       video_id = (TextView)findViewById(R.id.tab9_video_one_id);
       video_title = (TextView)findViewById(R.id.tab9_video_one_title);
       video_link  =(TextView)findViewById(R.id.tab9_video_one_link);
       video_ngay_tao =(TextView)findViewById(R.id.tab9_video_one_ngay_tao);
       video_luot_view=(TextView)findViewById(R.id.tab9_video_one_luot_view);
       video_btnplay = (Button)findViewById(R.id.tab9_video_one_btn_play);
       video_yeuthich = (CheckBox)findViewById(R.id.tab9_video_one_yeuthich);
       video_one_information=(RelativeLayout)findViewById(R.id.tab9_video_one_information);
       
       media = new MediaController(this);
       
       
       try{
			if(instantFunctionCheck.checkInternetConnection())
			{
				new Load_Video_One_Progress().execute();
			}
			else Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
		} 
		catch(Exception e) { e.printStackTrace(); }
       video_btnplay.setOnClickListener(new View.OnClickListener() 
       {
		
			@Override
			public void onClick(View v) 
			{
				 try{
						if(instantFunctionCheck.checkInternetConnection() && isFirstClick==false)
						{
							video_btnplay.setVisibility(View.INVISIBLE);
							new PrepareVideoProgress().execute();
						}
						else Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
					} 
					catch(Exception e) { e.printStackTrace(); }
				
			}
       });
       
       video.setOnClickListener(new View.OnClickListener() 
       {
	   		@Override
	   		public void onClick(View v) 
	   		{
	   			video_one_information.setVisibility(View.VISIBLE);
	   			media.show(3000);
	   			Handler handler = new Handler();
		        handler.postDelayed(new Runnable() 
		        {
		            public void run() {
		            	Log.e("Sau 5s","----Sau 5s hide Video Information");
		            	video_one_information.setVisibility(View.GONE);
		            }}, 3000);
	   		}
   		});
       video_yeuthich.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(video_yeuthich.isChecked() == true)
				{
					try{
						new Video_Save().execute();
					}catch(Exception e)
					{
						e.printStackTrace();
					}
					
				} else if(video_yeuthich.isChecked() == false)
				{
					try{
						
						new Video_Remove().execute();
					}catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		});
    }
    
    
    
    //
    //
    //
    //
    
    
    class PrepareVideoProgress extends AsyncTask<String, String, Void> 
    {
    	Integer track = 0;
    	ProgressDialog dialog;
    	
    	@Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Tab9_Video_Watch_One.this);
            dialog.setMessage("Loading, Please Wait...");
            dialog.setCancelable(true);
            dialog.show();
            video.setOnPreparedListener(new OnPreparedListener() {
            	 
                public void onPrepared(MediaPlayer arg0) 
                {
                    video.start();
                    dialog.dismiss();
                    isFirstClick=true;
                    video_btnplay.setVisibility(View.INVISIBLE);
                    video_one_information.setVisibility(View.GONE);
                }
            });
            video.setOnTouchListener(new View.OnTouchListener() 
            {
    			@Override
    			public boolean onTouch(View v, MotionEvent event) 
    			{
    				video_one_information.setVisibility(View.VISIBLE);
    				media.show(3000);
    	   			Handler handler = new Handler();
    		        handler.postDelayed(new Runnable() 
    		        {
    		            public void run() {
    		            	Log.e("Sau 3s","=====Sau 3s hide Video Information");
    		            	video_one_information.setVisibility(View.GONE);
    		            }}, 3000);
    				return true;
    			}
           });
        }
    	@Override
        protected void onProgressUpdate(final String... uri) {
 
            try {
            } catch (IllegalArgumentException e) {
                e.printStackTrace(); isFirstClick=false; video_btnplay.setVisibility(View.INVISIBLE);
            } catch (IllegalStateException e) {
                e.printStackTrace(); isFirstClick=false; video_btnplay.setVisibility(View.INVISIBLE);
            } catch (SecurityException e) {
                e.printStackTrace(); isFirstClick=false; video_btnplay.setVisibility(View.INVISIBLE);
            }
             
 
        }
 
        @Override
        protected Void doInBackground(String... params) {
            try {
                 publishProgress("");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
 
    }
    
    
    //
    //
    //
    //
    class Load_Video_One_Progress extends AsyncTask<String, String, String> 
	{
		int success;
		String id;
		String title;
		String link;
		String ngay_tao;
		String luot_view;
		String yeuthich;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			success = 0;
			pDialog = new ProgressDialog(Tab9_Video_Watch_One.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		protected String doInBackground(String... args) 
		{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("iduser", String.valueOf(iduser)));
			params.add(new BasicNameValuePair("idvideo", idvideo));
			params.add(new BasicNameValuePair("request", "post_video_one"));

			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER + "totnghiep_video_one.php","POST", params);
			
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					success = json.getInt("success");
					
					if (success == 1 )
					{
						id = json.getString("id");
						title = json.getString("title");
						link = json.getString("link");
						ngay_tao = json.getString("ngay_tao");
						luot_view = json.getString("luot_view");
						yeuthich = json.getString("yeuthich");
					} else {}
						
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else { success = 3;  }
			return null;
		}
		protected void onPostExecute(String file_url)
		{
			pDialog.dismiss();
			
			if(success == 1 )
			{
				video_id.setText(id);
				video_title.setText(title);
				video_link.setText(link);
				video_ngay_tao.setText("Ngày đăng: "+ngay_tao);
				video_luot_view.setText("Xem: "+luot_view);
				if(yeuthich.equals("yes") == true) 
					video_yeuthich.setChecked(true);
				else video_yeuthich.setChecked(false);
				link_video_link = Constant.BASE_URL_SERVER + "/video/"+video_link.getText().toString();
				
				media.setAnchorView(video);
                video.setMediaController(media);
                Uri uri1 = Uri.parse(link_video_link);
                video.setVideoURI(uri1);
                video.requestFocus();
			}
			else if(success==0 ) taoDialog("Tải dữ liệu không thành công. Click OK thử lại.");
			else if(success==3) taoDialog("Tải dữ liệu không thành công. Click OK thử lại.");
		} 

	}
    
    //
	//
	//
	//
	class Video_Save extends AsyncTask<String , String, String> 
	{
		int success;
		int da_ton_tai;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			success=0;
			da_ton_tai=0;
			pDialog = new ProgressDialog(Tab9_Video_Watch_One.this);
			pDialog.setMessage("Đang xử lý...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			
		}

		protected String doInBackground(String... param) 
		{
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("idvideo", idvideo ));
			params.add(new BasicNameValuePair("iduser", String.valueOf(iduser) ));
			params.add(new BasicNameValuePair("request", "post_video_save"));
			
			JSONParser jj = new JSONParser();
			String json_string = jj.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_video_save.php", "POST", params);
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					
					da_ton_tai = json.getInt(Constant.KEYWORD_USERNAME_DA_TON_TAI);
					success = json.getInt(Constant.KEYWORD_SUCCESS); 
					if (success == 1) 
					{
						
					} 
					else 
					{ success=0;}
				} catch (JSONException e) {e.printStackTrace();}
			} else {success=3;};

			return null;
		}
		protected void onPostExecute(String file_url) 
		{
			pDialog.dismiss();
			if(success==1 && da_ton_tai == 0)
			{
				Toast.makeText(getApplicationContext(), "Đã thêm vào yêu thích.", Toast.LENGTH_LONG).show();
				video_yeuthich.setChecked(true);
			}
			if(success==0 && da_ton_tai == 0)
			{
				Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_LONG).show();
				video_yeuthich.setChecked(false);
			}
			if(success==3 && da_ton_tai == 0)
			{
				Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_LONG).show();
				video_yeuthich.setChecked(false);
			}
		}

	}
	//
	//
	//
	//
	class Video_Remove extends AsyncTask<String , String, String> 
	{
		int success;
		int da_ton_tai;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			success=0;
			da_ton_tai=0;
			pDialog = new ProgressDialog(Tab9_Video_Watch_One.this);
			pDialog.setMessage("Đang xử lý ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... param) 
		{
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("idvideo", idvideo ));
			params.add(new BasicNameValuePair("iduser", String.valueOf(iduser) ));
			params.add(new BasicNameValuePair("request", "post_video_remove"));
			
			JSONParser jj = new JSONParser();
			String json_string = jj.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_video_remove.php", "POST", params);
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					
					da_ton_tai = json.getInt(Constant.KEYWORD_USERNAME_DA_TON_TAI);
					success = json.getInt(Constant.KEYWORD_SUCCESS); 
					if (success == 1) 
					{
						
					} else {success=0;}
				} catch (JSONException e) {e.printStackTrace();}
			} else {success=3;};

			return null;
		}
		protected void onPostExecute(String file_url) 
		{
			pDialog.dismiss();
			if(success==1 && da_ton_tai == 1)
			{
				Toast.makeText(getApplicationContext(), "Đã xóa khỏi yêu thích.", Toast.LENGTH_SHORT).show();
				video_yeuthich.setChecked(false);
			}
			if(success==0 && da_ton_tai == 0)
			{
				Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
				video_yeuthich.setChecked(true);
			}
			if(success==3 && da_ton_tai == 0)
			{
				Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
				video_yeuthich.setChecked(true);
			}
		}

	}
    
    //
    //
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
        					new Load_Video_One_Progress().execute();
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
                        //finish();
                    }
                });
        dialog = builder.create();
        dialog.show();
	}

}

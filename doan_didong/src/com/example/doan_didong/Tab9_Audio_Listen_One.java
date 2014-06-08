package com.example.doan_didong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Tab9_Audio_Listen_One extends Activity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener
{

	// Progress Dialog
	private ProgressDialog pDialog;
	private FunctionCheck instantFunctionCheck;
	JSONParser jsonParser = new JSONParser();
	
	Bundle b;
	public boolean isFirstClick;
	public int iduser;
	public String idaudio;
	public String link_audio_link;
	
	private TextView audio_id, audio_title, audio_link, audio_ngay_tao, audio_luot_view, audio_text ;
	private TextView audio_songCurrentDurationLabel, audio_totalsongDurationLabel;
	
	private ImageButton  audio_repeat, audio_play, audio_backward, audio_forward;
	private ImageButton audio_btntext;
	private CheckBox audio_yeuthich;
	private SeekBar audio_progressBar;
	private ScrollView Scroll_text_hidden;
	
	
	private  MediaPlayer mp;
	// Handler to update UI timer, progress bar etc,.
	private Handler mHandler = new Handler();
//	private SongsManager songManager;
//	private Utilities utils;
	private int seekForwardTime = 5000; // 5000 milliseconds
	private int seekBackwardTime = 5000; // 5000 milliseconds
//	private int currentSongIndex = 0; 
//	private boolean isShuffle = false;
	private boolean isRepeat = false;
	
	
	
	private static final int DIALOG_CHANGE_REQUIRE = 1; //không được để trống
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab9_audio_listen_one);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//set không cho xoay màn hình
		instantFunctionCheck = new FunctionCheck(getApplicationContext());//tạo đối tượng kiểm tra kết nối internet
		isFirstClick=false;
		
		b = getIntent().getExtras();
		
		iduser 		= b.getInt("iduser");
		idaudio 	= b.getString("idaudio");
		audio_id = (TextView) findViewById(R.id.tab9_one_id);
		audio_title = (TextView) findViewById(R.id.tab9_one_title);
		audio_text  =  (TextView) findViewById(R.id.tab9_one_text);
		audio_link = (TextView) findViewById(R.id.tab9_one_link);
		audio_ngay_tao = (TextView) findViewById(R.id.tab9_one_ngay_tao);
		audio_luot_view = (TextView) findViewById(R.id.tab9_one_luot_view);
		audio_songCurrentDurationLabel = (TextView) findViewById(R.id.tab9_songCurrentDurationLabel);
		audio_totalsongDurationLabel = (TextView) findViewById(R.id.tab9_songTotalDurationLabel);
		audio_yeuthich = (CheckBox) findViewById(R.id.tab9_one_yeuthich);
		
		audio_btntext = (ImageButton) findViewById(R.id.tab9_one_btntext);
		audio_repeat = (ImageButton) findViewById(R.id.tab9_one_btnRepeat);
		audio_play = (ImageButton) findViewById(R.id.tab9_btnPlay);
		audio_backward = (ImageButton) findViewById(R.id.tab9_btnBackward);
		audio_forward = (ImageButton) findViewById(R.id.tab9_btnForward);
		Scroll_text_hidden = (ScrollView) findViewById(R.id.tab9_songThumbnail);
		audio_progressBar = (SeekBar) findViewById(R.id.tab9_songProgressBar); 
		try{
			if(instantFunctionCheck.checkInternetConnection())
			{
				new Listen_One_AudioProgress().execute();
			}
			else Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
		} 
		catch(Exception e) { e.printStackTrace(); }
		
		
		
		
		mp = new MediaPlayer();
		audio_progressBar.setOnSeekBarChangeListener(this); // Important
		mp.setOnCompletionListener(this); 
		
		audio_yeuthich.setOnClickListener(new View.OnClickListener() 
		{
//			boolean check = audio_yeuthich.isChecked();
			@Override
			public void onClick(View v) 
			{
				if(audio_yeuthich.isChecked() == true)
				{
					try{
				
						new Audio_Save().execute();
					}catch(Exception e)
					{
						e.fillInStackTrace();
					}
					
				} else if(audio_yeuthich.isChecked() == false)
				{
					try{
						
						new Audio_Remove().execute();
					}catch(Exception e)
					{
						e.fillInStackTrace();
					}
				}
			}
		});
		audio_play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// check for already playing
				Log.e("click play","click play");
				if(isFirstClick==false)
				{
					Log.e("mp=null","mp=null");
					try{
						
						if(instantFunctionCheck.checkInternetConnection())
						{
							Log.e("mp=prepare","mp=prepare");
							new PrepareProgress().execute();
						}
						else Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
					} catch(Exception e)
					{
						e.printStackTrace();
					}
				} 
				else
				{
					Log.e("mp!=khac null","mp!=khac null");
					if(mp.isPlaying()){
						if(mp!=null){
							mHandler.removeCallbacks(mUpdateTimeTask);
							mp.pause();
							// Changing button image to play button
							audio_play.setImageResource(R.drawable.btn_play);
							updateProgressBar();
						}
					}else{
						// Resume song
						if(mp!=null){
							mHandler.removeCallbacks(mUpdateTimeTask);
							mp.start();
							// Changing button image to pause button
							audio_play.setImageResource(R.drawable.btn_pause);
							updateProgressBar();
						}
					}
				}
				
			}
		});
		audio_forward.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// get current song position				
				int currentPosition = mp.getCurrentPosition();
				// check if seekForward time is lesser than song duration
				if(currentPosition + seekForwardTime <= mp.getDuration()){
					// forward song
					mp.seekTo(currentPosition + seekForwardTime);
				}else{
					// forward to end position
					mp.seekTo(mp.getDuration());
				}
			}
		});
		audio_backward.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// get current song position				
				int currentPosition = mp.getCurrentPosition();
				// check if seekBackward time is greater than 0 sec
				if(currentPosition - seekBackwardTime >= 0){
					// forward song
					mp.seekTo(currentPosition - seekBackwardTime);
				}else{
					// backward to starting position
					mp.seekTo(0);
				}
				
			}
		});
		
		/**
		 * Button Click event for Repeat button
		 * Enables repeat flag to true
		 * */
		audio_repeat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(isRepeat){
					isRepeat = false;
					Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
					audio_repeat.setImageResource(R.drawable.btn_repeat);
				}else{
					// make repeat to true
					isRepeat = true;
					Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
					audio_repeat.setImageResource(R.drawable.btn_repeat_focused);
				}	
			}
		});
		audio_btntext.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				if(Scroll_text_hidden.getVisibility() == ScrollView.GONE)
				{
					Scroll_text_hidden.setVisibility(ScrollView.VISIBLE);
				}
    			else
    			{
    				Scroll_text_hidden.setVisibility(ScrollView.GONE);
    			}
				
			}
		});
		
	}
	public void  playSong()
	{
		// Play song
		try {
        	mp.reset();
			mp.setDataSource(link_audio_link);
			mp.prepare();
			mp.start();
			
        	// Changing Button Image to pause image
			audio_play.setImageResource(R.drawable.btn_pause);
			
			// set Progress bar values
			audio_progressBar.setProgress(0);
			audio_progressBar.setMax(100);
			
			// Updating progress bar
			updateProgressBar();			
		} catch (IllegalArgumentException e) 
		{
			Toast.makeText(getApplicationContext(), "Không thành công.\n Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IllegalStateException e) 
		{
			Toast.makeText(getApplicationContext(), "Không thành công.\n Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) 
		{
			Toast.makeText(getApplicationContext(), "Không thành công.\n Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	
	/**
	 * Update timer on seekbar
	 * */
	public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 300);        
    }	
	
	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() {
		   public void run() {
			   long totalDuration = mp.getDuration();
			   long currentDuration = mp.getCurrentPosition();
			  
			   // Displaying Total Duration time
			   audio_totalsongDurationLabel.setText(""+milliSecondsToTimer(totalDuration));
			   // Displaying time completed playing
			   audio_songCurrentDurationLabel.setText(""+milliSecondsToTimer(currentDuration));
			   
			   // Updating progress bar
			   int progress = (int)(getProgressPercentage(currentDuration, totalDuration));
			   //Log.d("Progress", ""+progress);
			   audio_progressBar.setProgress(progress);
			   
			   // Running this thread after 100 milliseconds
		       mHandler.postDelayed(this, 300);
		   }
		};
		
	/**
	 * 
	 * */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) 
	{
		
	}

	/**
	 * When user starts moving the progress handler
	 * */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) 
	{
			// remove message Handler from updating progress bar
			mHandler.removeCallbacks(mUpdateTimeTask);
    }
	
	/**
	 * When user stops moving the progress hanlder
	 * */
	@Override
    public void onStopTrackingTouch(SeekBar seekBar) 
	{
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mp.getDuration();
		int currentPosition = progressToTimer(seekBar.getProgress(), totalDuration);
		
		// forward or backward to certain seconds
		mp.seekTo(currentPosition);
		
		// update timer progress again
		updateProgressBar();
    }

	/**
	 * On Song Playing completed
	 * if repeat is ON play same song again
	 * if shuffle is ON play random song
	 * */
	@Override
	public void onCompletion(MediaPlayer arg0) {
		
		// check for repeat is ON or OFF
		if(isRepeat){
			// repeat is on play same song again
//			playSong(currentSongIndex);
			mHandler.removeCallbacks(mUpdateTimeTask);
			mp.seekTo(0);
			audio_progressBar.setProgress(0);
			mp.start();
			updateProgressBar();
		} 
//		else if(isShuffle){
//			// shuffle is on - play a random song
//			Random rand = new Random();
//			currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
//			playSong(currentSongIndex);
//		} 
			else{
				mHandler.removeCallbacks(mUpdateTimeTask);
				mp.seekTo(0);
				audio_progressBar.setProgress(0);
				audio_play.setImageResource(R.drawable.btn_play);
				// no repeat or shuffle ON - play next song
	//				if(currentSongIndex < (songsList.size() - 1)){
	//					playSong(currentSongIndex + 1);
	//					currentSongIndex = currentSongIndex + 1;
	//				}else{
	//					// play first song
	//					playSong(0);
	//					currentSongIndex = 0;
	//				}
				
				}
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		mHandler.removeCallbacks(mUpdateTimeTask);
	    mp.release();
	 }
	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
		mHandler.removeCallbacks(mUpdateTimeTask);
		mp.release();
	}
	
	
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
        					new Listen_One_AudioProgress().execute();
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
	
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
        AlertDialog.Builder builder;
        switch(id) {
        case DIALOG_CHANGE_REQUIRE:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Cảnh báo!");
    		builder.setIcon(R.drawable.icon_canh_bao);
            builder.setMessage("Không được để trống!")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {}
            });
            dialog = builder.create();
            break;
       
        default:
            dialog = null;
        }
        return dialog;
	}
	
	
	
	/**
	 * 
	 * */
	class Listen_One_AudioProgress extends AsyncTask<String, String, String> 
	{
		int success;
		String id;
		String title;
		String link;
		String text;
		String hoinhanh;
		String traloi;
		String ngay_tao;
		String luot_view;
		String yeuthich;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			success = 0;
			pDialog = new ProgressDialog(Tab9_Audio_Listen_One.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		protected String doInBackground(String... args) 
		{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("iduser", String.valueOf(iduser)));
			params.add(new BasicNameValuePair("idaudio", idaudio));
			params.add(new BasicNameValuePair("request", "post_audio_view_one"));

			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER + "totnghiep_audio_view_one.php","POST", params);
			
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
						text = json.getString("text");
						hoinhanh = json.getString("hoinhanh");
						traloi = json.getString("traloi");
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
				audio_id.setText(id);
				audio_title.setText(title);
				audio_link.setText(link);
				audio_text.setText(text);
				audio_ngay_tao.setText("Đăng ngày: "+ngay_tao);
				audio_luot_view.setText("Lượt xem: "+luot_view);
				if(yeuthich.equals("yes") == true) 
					audio_yeuthich.setChecked(true);
				else audio_yeuthich.setChecked(false);
				link_audio_link = Constant.BASE_URL_SERVER + "/audio/"+audio_link.getText().toString();
//				Toast.makeText(getApplicationContext(), link_audio_link, Toast.LENGTH_SHORT).show();
			}
			else if(success==0 ) taoDialog("Tải dữ liệu không thành công. Click OK thử lại.");
			else if(success==3) taoDialog("Tải dữ liệu không thành công. Click OK thử lại.");
		}

	}
	
	
	/**
	 * 
	 * */
	class PrepareProgress extends AsyncTask<String, String, String> 
	{
		int success;
		
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			success = 0;
			pDialog = new ProgressDialog(Tab9_Audio_Listen_One.this);
			pDialog.setMessage("Prepare audio...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		protected String doInBackground(String... args) 
		{
			
			
			
				try {
					Log.e("mp set resourece","mp set resourece");
			        	mp.reset();
						mp.setDataSource(link_audio_link);
						mp.prepare();			
					
					
						if (mp!=null)
						{
							success=1;
						} else {}
						
				} catch (IllegalArgumentException e) 
				{
					e.printStackTrace();
					success=0;
				} catch (IllegalStateException e) 
				{
					e.printStackTrace();
					success=0;
				} catch (IOException e) 
				{
					e.printStackTrace();
					success=0;
				}
			return null;
		}
		protected void onPostExecute(String file_url)
		{
			pDialog.dismiss();
			
			if(success == 1 )
			{
				isFirstClick=true;
				mp.start();
				audio_play.setImageResource(R.drawable.btn_pause);
				
				// set Progress bar values
				audio_progressBar.setProgress(0);
				audio_progressBar.setMax(100);
				
				// Updating progress bar
				updateProgressBar();	
			}
			else if(success==0 ) 
				Toast.makeText(getApplicationContext(), "Không thành công.\n Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			
		}

	}
	
	
	
	//
	//
	//
	//
	class Audio_Save extends AsyncTask<String , String, String> 
	{
		int success;
		int da_ton_tai;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			success=0;
			da_ton_tai=0;
			pDialog = new ProgressDialog(Tab9_Audio_Listen_One.this);
			pDialog.setMessage("Đang xử lý...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			
		}

		protected String doInBackground(String... param) 
		{
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("idaudio", idaudio ));
			params.add(new BasicNameValuePair("iduser", String.valueOf(iduser) ));
			params.add(new BasicNameValuePair("request", "post_audio_save"));
			
			JSONParser jj = new JSONParser();
			String json_string = jj.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_audio_save.php", "POST", params);
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
				audio_yeuthich.setChecked(true);
			}
			if(success==0 && da_ton_tai == 0)
			{
				Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_LONG).show();
				audio_yeuthich.setChecked(false);
			}
			if(success==3 && da_ton_tai == 0)
			{
				Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_LONG).show();
				audio_yeuthich.setChecked(false);
			}
		}

	}
	//
	//
	//
	//
	class Audio_Remove extends AsyncTask<String , String, String> 
	{
		int success;
		int da_ton_tai;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			success=0;
			da_ton_tai=0;
			pDialog = new ProgressDialog(Tab9_Audio_Listen_One.this);
			pDialog.setMessage("Đang xử lý ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... param) 
		{
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("idaudio", idaudio ));
			params.add(new BasicNameValuePair("iduser", String.valueOf(iduser) ));
			params.add(new BasicNameValuePair("request", "post_audio_remove"));
			
			JSONParser jj = new JSONParser();
			String json_string = jj.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_audio_remove.php", "POST", params);
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
				audio_yeuthich.setChecked(false);
			}
			if(success==0 && da_ton_tai == 0)
			{
				Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
				audio_yeuthich.setChecked(true);
			}
			if(success==3 && da_ton_tai == 0)
			{
				Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
				audio_yeuthich.setChecked(true);
			}
		}

	}
	
	
	
	
	
	
	
	//
	//
	//
	
	public String milliSecondsToTimer(long milliseconds){
		String finalTimerString = "";
		String secondsString = "";
		
		// Convert total duration into time
		   int hours = (int)( milliseconds / (1000*60*60));
		   int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
		   int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
		   // Add hours if there
		   if(hours > 0){
			   finalTimerString = hours + ":";
		   }
		   
		   // Prepending 0 to seconds if it is one digit
		   if(seconds < 10){ 
			   secondsString = "0" + seconds;
		   }else{
			   secondsString = "" + seconds;}
		   
		   finalTimerString = finalTimerString + minutes + ":" + secondsString;
		return finalTimerString;
	}
	
	/**
	 * Function to get Progress percentage
	 * @param currentDuration
	 * @param totalDuration
	 * */
	public int getProgressPercentage(long currentDuration, long totalDuration){
		Double percentage = (double) 0;
		
		long currentSeconds = (int) (currentDuration / 1000);
		long totalSeconds = (int) (totalDuration / 1000);
		
		// calculating percentage
		percentage =(((double)currentSeconds)/totalSeconds)*100;
		
		// return percentage
		return percentage.intValue();
	}

	/**
	 * Function to change progress to timer
	 * @param progress - 
	 * @param totalDuration
	 * returns current duration in milliseconds
	 * */
	public int progressToTimer(int progress, int totalDuration) {
		int currentDuration = 0;
		totalDuration = (int) (totalDuration / 1000);
		currentDuration = (int) ((((double)progress) / 100) * totalDuration);
		
		// return current duration in milliseconds
		return currentDuration * 1000;
	}
}
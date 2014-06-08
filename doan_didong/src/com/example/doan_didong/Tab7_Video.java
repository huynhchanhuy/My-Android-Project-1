package com.example.doan_didong;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.doan_didong.MainActivity.Video;
import com.example.doan_didong.Tab6_Audio.Audio_Save;

public class Tab7_Video extends SherlockFragment 
{

	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private FunctionCheck instantFunctionCheck;
	
	private ListView tab7_listview;
	private VideoCustomAdapter adapter;
	private GetAllVideo_first task_first;
	private GetAllVideo_second task_second;
    private boolean isloading;
    
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		Log.d("onCreate"," tab7 video onCreate");
		View view = inflater.inflate(R.layout.tab7_listview_video, container, false);
		isloading = false;
		instantFunctionCheck = new FunctionCheck(getSherlockActivity());
		tab7_listview = (ListView) view.findViewById(R.id.tab7_all_video);
		
		if(MainActivity.itemlistVideo.size() == 0 ) //&& MainActivity.video_start == 0)
		{
			if(instantFunctionCheck.checkInternetConnection())
			{
				task_first = new GetAllVideo_first();
		        task_first.execute();
			} else Toast.makeText(getSherlockActivity(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
		} else 
		{
			adapter = new VideoCustomAdapter(getActivity(), R.layout.tab7_item_video, MainActivity.itemlistVideo);
			tab7_listview.setAdapter(adapter);
		}
		
		tab7_listview.setOnScrollListener(new OnScrollListener() 
        {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
			{
				int position = firstVisibleItem + visibleItemCount;
				if( position == totalItemCount && isloading==false 
						&& MainActivity.itemlistVideo.size() < MainActivity.video_all_item
						&& MainActivity.video_end_of_list == 0 )
				{
					task_second = new GetAllVideo_second();
					task_second.execute();
					Toast.makeText(getActivity(), "Loading more...", Toast.LENGTH_SHORT).show();
				}
				if(position == totalItemCount && MainActivity.itemlistVideo.size() == MainActivity.video_all_item
						&& MainActivity.video_end_of_list == 0 )
				{
					MainActivity.video_end_of_list = 1;
				}
			}
		});
		return view;
	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setUserVisibleHint(true);
	}
	
	@Override
	public void onResume() 
	{
		super.onResume();
		Log.d("onResume"," tab7 video onResume");
	}


	@Override
	public void onStart() 
	{
		super.onStart();
		Log.d("onStart"," tab7 video onStart");
	}

	//
	//Task first
	//
	class GetAllVideo_first extends AsyncTask<String, String, String> 
	{
		int success111;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(FunctionFolderAndFile.checkFileExist("version", "video.txt") == false)
			{	
				MainActivity.DownloadDataFromServerProgress a = new MainActivity.DownloadDataFromServerProgress();
				a.execute("video.txt");
			}
			pDialog = new ProgressDialog(getSherlockActivity());
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();	        
			success111 = 0;
			isloading=true;
		}

		protected String doInBackground(String... args) {
			/*List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("start", String.valueOf(MainActivity.video_start)));
			params.add(new BasicNameValuePair("request", "post_video_get_all"));
			
			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_video_get_all.php", "POST", params);*/
			String json_string = FunctionFolderAndFile.readFromFile("version", "video.txt");
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					if(json.getJSONArray("all_video").length() > 0 && json.getInt("success") == 1 
							&& json.getInt("all_item") > 0) 
					{
							success111=1; 
							MainActivity.video_all_item = json.getInt("all_item");
					}
					else success111=0;
					
					if (success111 == 1) 
					{
						JSONArray jsonArray = json.getJSONArray("all_video"); 
						for (int i = 0; i < jsonArray.length(); i++) 
						{
							JSONObject c = jsonArray.getJSONObject(i);
							String id1 = c.getString("id");
							String title1 = MainActivity.video_stt+". " + c.getString("title");
							String mo_ta1 = c.getString("mo_ta");; // lấy mô tả từ database
							String link1 = c.getString("link");
							String ngay_tao1 = c.getString("ngay_tao");
							String luot_view1 = c.getString("luot_view");
							
							MainActivity.Video item = new MainActivity.Video(id1, title1, mo_ta1, link1, ngay_tao1, luot_view1);
							MainActivity.itemlistVideo.add(item);
							MainActivity.video_stt++;
						}
						jsonArray=null;
					} else {}
				} catch (JSONException e) 
				{
					e.printStackTrace();
				}
			} else {success111=3;}

			return null;
		}
		protected void onPostExecute(String file_url) 
		{
			isloading=false;
			pDialog.dismiss();
			Log.e("---biến success111=","---biến success111="+success111);
			if(success111==1)
			{
					adapter = new VideoCustomAdapter(getActivity(), R.layout.tab7_item_video, MainActivity.itemlistVideo);
					tab7_listview.setAdapter(adapter);
					//int aa=MainActivity.video_start;
					//if(aa + 100 < MainActivity.video_all_item) MainActivity.video_start = aa + 100;
			}
			if(success111==0) 
			{
				Log.d("---Success=0 lỗi internet","---Success=0 lỗi internet");
				Toast.makeText(getSherlockActivity(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			}
			if(success111==3) 
			{
				Log.d("---Success=3 JSON=rỗng", "---Success=3 JSON=rỗng");
				Toast.makeText(getSherlockActivity(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	//
	//Task second
	//
	class GetAllVideo_second extends AsyncTask<String, String, String>
	{
		int success222;
		int all_curr_item;
		private JSONArray jsonArray; 
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			all_curr_item=MainActivity.itemlistVideo.size();
			isloading=true;
			success222=0;
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("start", String.valueOf(MainActivity.video_start)));
			params.add(new BasicNameValuePair("request", "post_video_get_all"));
			
			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_video_get_all.php", "POST", params);
			if(json_string.length() != 0)
			{
				JSONObject jsonjson;
				try {
					jsonjson = JSONParser.getJSONObjectFromJString(json_string);
					if(jsonjson.getJSONArray("all_video").length() > 0 && jsonjson.getInt("success") == 1 && jsonjson.getInt("all_item") > 0)
						success222=1; 
					else success222=0;
					if (success222 == 1 && jsonjson.getInt("all_item") > 0 && jsonjson.getJSONArray("all_video").length() > 0) 
					{
						jsonArray = jsonjson.getJSONArray("all_video"); 
						for (int i = 0; i < jsonArray.length(); i++)
						{
							
							JSONObject c = jsonArray.getJSONObject(i);
							String id1 = c.getString("id");
							String title1 =MainActivity.video_stt+". " + c.getString("title");
							String text1 = c.getString("text");
							String ngay_tao1 = c.getString("ngay_tao");
							String luot_view1 = c.getString("luot_view");
							
							MainActivity.Video item = new MainActivity.Video(id1,title1,"", text1, ngay_tao1,luot_view1);
							MainActivity.itemlistVideo.add(item);
							MainActivity.video_stt++;
						}
						jsonArray=null;
						
					} else {}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {success222=3;}

			return null;
		}
		protected void onPostExecute(String file_url) 
		{
			isloading=false;
			Log.e("---biến success222=","---biến success222="+success222);
			if(success222==1)
			{
				if(MainActivity.itemlistAudio.size() > all_curr_item ) 
					{
						adapter.notifyDataSetChanged();
						int aa=MainActivity.video_start;
						if(aa + 100 < MainActivity.video_all_item) MainActivity.video_start = aa + 100;
					}
			}
			if(success222==0) Log.d("---Success=0 lỗi internet","---Success=0 lỗi internet");
			if(success222==3) Log.d("---Success=3 lỗi internet JSON=rỗng", "---Success=3 lỗi internet JSON=rỗng");
		}

	}
	

	//
	//Class custom adapter quản lý listitem
	//
	public class VideoCustomAdapter extends ArrayAdapter<MainActivity.Video> 
	{
		
		Context mContext;  
	    ArrayList<MainActivity.Video> items=null;
	    int resource;
	    
		public VideoCustomAdapter(Context context, int textViewResourceId, ArrayList<MainActivity.Video> objects) 
		{
			  super(context, textViewResourceId, objects);
			  this.mContext = context;
			  this.resource = textViewResourceId;
			  this.items = objects; 
		}
		  
		@Override
		public MainActivity.Video getItem(int position) 
		{
			return super.getItem(position);
		}
		
		@Override
		public long getItemId(int position) 
		{
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{			 
	    	  
			  final ViewHolder holder;   
		      if (convertView == null) 
		      {
		    	  LayoutInflater inflater = getLayoutInflater(getArguments());
		    	  convertView = inflater.inflate(R.layout.tab7_item_video, parent, false); 
		    	  
		    	  holder = new ViewHolder();
		    	  holder.id = 		(TextView) convertView.findViewById(R.id.tab7_video_id);
		    	  holder.title = 	(TextView) convertView.findViewById(R.id.tab7_video_title);
		    	  holder.mo_ta = 	(TextView) convertView.findViewById(R.id.tab7_video_mo_ta);
		    	  holder.ngay_tao = (TextView) convertView.findViewById(R.id.tab7_video_ngaytao);
		    	  holder.luot_view =(TextView) convertView.findViewById(R.id.tab7_video_luotview);
		    	  holder.next = 	(Button) convertView.findViewById(R.id.tab7_video_next_btn);
		    	  holder.watch = 	(Button) convertView.findViewById(R.id.tab7_video_watch);
		    	  holder.save = 	(Button) convertView.findViewById(R.id.tab7_video_save);
		    	  convertView.setTag(holder);
		      } else {
		    	  holder = (ViewHolder) convertView.getTag();
		      }
		      final Video iContent = items.get(position);
		      
		      
		      holder.id.setText(iContent.getId());
		      holder.title.setText(iContent.getTitle());
		      holder.mo_ta.setText(iContent.getMo_ta());
		      holder.ngay_tao.setText("Ngày đăng: "+iContent.getNgay_tao());
		      holder.luot_view.setText("Xem "+iContent.getLuot_view()+ " lần");
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
		    		  Intent in = new Intent(getSherlockActivity(),Tab7_Video_One.class);
						
		    		  in.putExtra("iduser", MainActivity.iduser);
		    		  in.putExtra("idvideo", iContent.getId());
		    		  startActivity(in);
		    	  }
			  });
		      holder.watch.setOnClickListener( new View.OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {	
		    		  Intent in = new Intent(getSherlockActivity(),Tab7_Video_One.class);
						
		    		  in.putExtra("iduser", MainActivity.iduser);
		    		  in.putExtra("idvideo", iContent.getId());
		    		  startActivity(in);
		    	  }
			  });
		      holder.save.setOnClickListener( new OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {
		    		  if(instantFunctionCheck.checkInternetConnection())// no Internet connection- Internet
						{
		    			  new Video_Save().execute(holder.id.getText().toString());
						} 
		    		  else 
							Toast.makeText(getSherlockActivity(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
		    		  
		    	  }
			  });
		      return convertView;
		}
		private class ViewHolder 
		{
			   TextView id;
			   TextView title;
			   TextView mo_ta; //video nói về cảnh đồng quê mùa thu nước Ý, chẳng hạn vậy.
			   TextView ngay_tao;
			   TextView luot_view;
			   Button next;
			   Button watch;
			   Button save;
		}
	}
	//
	//Lưu từ điển yêu thích
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
			pDialog = new ProgressDialog(getSherlockActivity());
			pDialog.setMessage("Đang xử lý ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... param) {
			String idaudio = param[0];
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("idloai", idaudio ));
			params.add(new BasicNameValuePair("loai", "video" ));
			params.add(new BasicNameValuePair("iduser", String.valueOf(MainActivity.iduser) ));
			params.add(new BasicNameValuePair("request", "post_save"));
			
			JSONParser jj = new JSONParser();
			String json_string = jj.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_audio_video_save.php", "POST", params);
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					
					da_ton_tai = json.getInt(Constant.KEYWORD_USERNAME_DA_TON_TAI);
					success = json.getInt(Constant.KEYWORD_SUCCESS); 
					if (success == 1) 
					{
						
					} else {}
				} catch (JSONException e) {e.printStackTrace();}
			} else {success=3;};

			return null;
		}
		protected void onPostExecute(String file_url) 
		{
			pDialog.dismiss();
			if(success==1 && da_ton_tai == 0)
			{
				Toast.makeText(getSherlockActivity(), "Lưu thành công.", Toast.LENGTH_SHORT).show();
			}
			if(success==0 && da_ton_tai == 1)
			{
				Toast.makeText(getSherlockActivity(), "Từ này đã được lưu.", Toast.LENGTH_SHORT).show();
			}
			if(success==0 && da_ton_tai == 0)
			{
				Toast.makeText(getSherlockActivity(), "Không thành công. Kiểm tra internet.", Toast.LENGTH_SHORT).show();
			}
			if(success==3 && da_ton_tai == 0)
			{
				Toast.makeText(getSherlockActivity(), "Không thành công. Kiểm tra internet.", Toast.LENGTH_SHORT).show();
			}
		}

	}
	//

}
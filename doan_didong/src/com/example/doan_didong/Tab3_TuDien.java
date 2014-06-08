package com.example.doan_didong;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract.Directory;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;

public class Tab3_TuDien extends SherlockFragment
{

	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private FunctionCheck instantFunctionCheck;
	int _ok;
//	private MediaPlayer mp;
	private ListView tab3_tudien_listview;
	private TuDienCustomAdapter adapter;
	private GetAllTuDien_Second task;
	private GetAllTuDien_First task_first;
    private boolean isloading;
    
    private EditText tudien_search;
    private Button   tudien_search_cancel;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		isloading = false;
		instantFunctionCheck = new FunctionCheck(getSherlockActivity());
		
		
		Log.d("------tab3 onCreateView---------","size:"+MainActivity.itemlistTuDien.size() + " all_item:"+ MainActivity.tudien_all_item);
		View view = inflater.inflate(R.layout.tab3_listview_tudien, container, false);
		tab3_tudien_listview = (ListView) view.findViewById(R.id.tab3_tudien_listview);
		if(MainActivity.itemlistTuDien.size() == 0 )
		{
			if(instantFunctionCheck.checkInternetConnection())
			{
				task_first = new GetAllTuDien_First();
		        task_first.execute();
			} else Toast.makeText(getSherlockActivity(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			
	        
		} else 
		{
			adapter = new TuDienCustomAdapter(getActivity(), R.layout.tab3_item_tudien, MainActivity.itemlistTuDien);
			tab3_tudien_listview.setAdapter(adapter);
		}
                
        tab3_tudien_listview.setOnScrollListener(new OnScrollListener() 
        {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
			{
				int position = firstVisibleItem + visibleItemCount;
				if( position == totalItemCount && isloading==false && MainActivity.itemlistTuDien.size() < MainActivity.tudien_all_item
						&& MainActivity.tudien_end_of_list == 0 && MainActivity.dang_tim_kiem==0 )
				{
					if(instantFunctionCheck.checkInternetConnection())
					{
						task = new GetAllTuDien_Second();
						task.execute();
						Toast.makeText(getActivity(), "Loading more ...", Toast.LENGTH_SHORT).show();
					} 
					else 
						Toast.makeText(getSherlockActivity(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
				}
				if(position == totalItemCount && MainActivity.itemlistTuDien.size() == MainActivity.tudien_all_item
						&& MainActivity.tudien_end_of_list == 0 && MainActivity.dang_tim_kiem==0)
				{
						MainActivity.tudien_end_of_list = 1;
						Log.d("----Hết itemlistTUDIEN (cuối list)","---itemListTUDIEN.size="+MainActivity.itemlistTuDien.size()
								+" All_item="+MainActivity.tudien_all_item + " biến end_of_list="+MainActivity.tudien_end_of_list);
						
				}
				
			}
		});
        
        setHasOptionsMenu(true);
        
		return view;
	}
	
	@Override
    public void onCreateOptionsMenu(final com.actionbarsherlock.view.Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        
         menu.add("Tìm kiếm")
        .setIcon(android.R.drawable.ic_menu_search)
        .setActionView(R.layout.collapsible_edittext)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		tudien_search=(EditText) menu.getItem(0).getActionView().findViewById(R.id.tudien_search);
		tudien_search.addTextChangedListener(new TextWatcher()
        {
	        public void afterTextChanged(Editable s)
	        {
	               
	        }
	        public void beforeTextChanged(CharSequence s,int start, int count, int after)
	        {
	        }
			@SuppressWarnings("null")
			public void onTextChanged(CharSequence s, int start, int before, int count)
	        {
				MainActivity.dang_tim_kiem=1;
		        int textlength = tudien_search.getText().length();
		        if(textlength==0)
		        {
		        	adapter = new TuDienCustomAdapter(getActivity(), R.layout.tab3_item_tudien, MainActivity.itemlistTuDien);
					tab3_tudien_listview.setAdapter(adapter);
		        } else if(textlength > 0)
		        {
		        	MainActivity.temp_itemlistTuDien.clear();
		        	for (int i = 0; i < MainActivity.itemlistTuDien.size(); i++)
				        {
		        			if (textlength <= MainActivity.itemlistTuDien.get(i).getTu().length())
					        {
						        if(tudien_search.getText().toString().equalsIgnoreCase(
						        (String)MainActivity.itemlistTuDien.get(i).getTu().subSequence(0,textlength) )   )
						        {
						        	MainActivity.temp_itemlistTuDien.add(MainActivity.itemlistTuDien.get(i));
						        }
					        }
				        }
		        	adapter = new TuDienCustomAdapter(getActivity(), R.layout.tab3_item_tudien, MainActivity.temp_itemlistTuDien);
					tab3_tudien_listview.setAdapter(adapter);
		        }
	        }
	        });
			tudien_search_cancel=(Button) menu.getItem(0).getActionView().findViewById(R.id.tudien_search_cancel);
			tudien_search_cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MainActivity.dang_tim_kiem=0;
					adapter = new TuDienCustomAdapter(getActivity(), R.layout.tab3_item_tudien, MainActivity.itemlistTuDien);
					tab3_tudien_listview.setAdapter(adapter);
					menu.getItem(0).collapseActionView();
				}
			});
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		MainActivity.dang_tim_kiem=1;
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setUserVisibleHint(true);
	}
	
	
	class GetAllTuDien_First extends AsyncTask<String, String, String> 
	{
		int success111;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			success111 = 0;
			isloading=true;
			
			pDialog = new ProgressDialog(getSherlockActivity());
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			if(FunctionFolderAndFile.checkFileExist("version", "tudien.txt") == false)
			{	try {
					FunctionFolderAndFile.downloadFileFromServer("tudien.txt", "tudien.txt");
				} catch (MalformedURLException e) 
				{
					e.printStackTrace();
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}

		protected String doInBackground(String... args) {
			/*List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("start", String.valueOf(MainActivity.tudien_start)));
			params.add(new BasicNameValuePair("request", "post_tudien_get_all"));*/
			
//			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_tudien_get_all.php", "POST", params);
			String json_string = FunctionFolderAndFile.readFromFile("version", "tudien.txt");
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					if(json.getJSONArray("all_tudien").length() > 0 && json.getInt(Constant.KEYWORD_SUCCESS) == 1 
							&& json.getInt("all_item") > 0) 
					{
							success111=1; 
							MainActivity.tudien_all_item = json.getInt("all_item");
					}
					else success111=0;
					
					if (success111 == 1) 
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
							
							MainActivity.TuDien item = new MainActivity.TuDien(id1,tu1,loaitu1,phienam1,dichnghia1,nhieunghia1,link_audio1, 0);
							MainActivity.itemlistTuDien.add(item);
						}
						
					} else {}
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
			if(success111==1)
			{
					adapter = new TuDienCustomAdapter(getActivity(), R.layout.tab3_item_tudien, MainActivity.itemlistTuDien);
					tab3_tudien_listview.setAdapter(adapter);
					/*int aa=MainActivity.tudien_start;
					if(aa + 100 < MainActivity.tudien_all_item) MainActivity.tudien_start = aa + 100;*/
					
					Log.e("---Kết quả get_lần đầu "," ---Kết quả get từ điển lần đầu( itemlistTUDIEN="+MainActivity.itemlistTuDien.size()
							+ " all_item="+MainActivity.tudien_all_item + ")"+ " biến end_of_list="+MainActivity.tudien_end_of_list);
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
	
	class GetAllTuDien_Second extends AsyncTask<String, String, String> 
	{
		int success222;
		int all_curr_item;
		private JSONArray jsonArray; 
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			all_curr_item=MainActivity.itemlistTuDien.size();
			isloading=true;
			success222=0;
			Log.d("--- get từ điển lần n "," --- get từ điển lần n( itemlistTUDIEN="+MainActivity.itemlistTuDien.size()
					+ " all_item="+MainActivity.tudien_all_item + ")" + " biến end_of_list="+MainActivity.tudien_end_of_list);
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("start", String.valueOf(MainActivity.tudien_start)));
			params.add(new BasicNameValuePair("request", "post_tudien_get_all"));
			JSONParser jj = new JSONParser();
			String json_string = jj.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_tudien_get_all.php", "POST", params);
			if(json_string.length() != 0)
			{
				JSONObject jsonjson;
				try {
					jsonjson = JSONParser.getJSONObjectFromJString(json_string);
					if(jsonjson.getJSONArray("all_tudien").length() > 0 && jsonjson.getInt("success") == 1 && jsonjson.getInt("all_item") > 0)
						success222=1; 
					else success222=0;
					if (success222 == 1 && jsonjson.getInt("all_item") > 0 && jsonjson.getJSONArray("all_tudien").length() > 0) 
					{
						jsonArray = jsonjson.getJSONArray("all_tudien"); 
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
							
							MainActivity.TuDien item = new MainActivity.TuDien(id1,tu1,loaitu1,phienam1,dichnghia1,nhieunghia1,link_audio1, 0);
							if(jsonArray.length() > 0) MainActivity.itemlistTuDien.add(item);
						}
						jsonArray=null;
						
					} else {}
				} catch (JSONException e) { e.printStackTrace(); }
			} else { success222=3; }

			return null;
		}
		protected void onPostExecute(String file_url) 
		{
			isloading=false;
			Log.e("---biến success222=","---biến success222="+success222);
			if(success222==1)
			{
				if(MainActivity.itemlistTuDien.size() > all_curr_item ) 
					{
						adapter.notifyDataSetChanged();
						int aa=MainActivity.tudien_start;
						if(aa + 100 < MainActivity.tudien_all_item) MainActivity.tudien_start = aa + 100;
						Log.d("--- Kết quả get lần n "," --- Kết quả get lần n( itemlistTUDIEN="+MainActivity.itemlistTuDien.size()
								+ " all_item="+MainActivity.tudien_all_item + ")" + " biến end_of_list="+MainActivity.tudien_end_of_list);
					}
				
			}
			if(success222==0) Log.d("---Success=0 lỗi internet","---Success=0 lỗi internet");
			if(success222==3) Log.d("---Success=3 JSON=rỗng", "---Success=3 JSON=rỗng");
		}

	}
	
	
	
	
	//
	//Class custom adapter quản lý listitem
	//
	public class TuDienCustomAdapter extends ArrayAdapter<MainActivity.TuDien> 
	{
		
		Context mContext;  
	    ArrayList<MainActivity.TuDien> items=null;
	    int resource;
	    
		public TuDienCustomAdapter(Context context, int textViewResourceId,ArrayList<MainActivity.TuDien> objects) 
		{
			  super(context, textViewResourceId, objects);
			  this.mContext = context;
			  this.resource = textViewResourceId;
			  this.items = objects; 
		}
		  
		@Override
		public MainActivity.TuDien getItem(int position) 
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
		    	  convertView = inflater.inflate(R.layout.tab3_item_tudien, parent, false); 
		    	  
		    	  holder = new ViewHolder();
		    	  holder.id = 		(TextView) convertView.findViewById(R.id.tab3_tudien_id);
		    	  holder.tu = 		(TextView) convertView.findViewById(R.id.tab3_tudien_tu);
		    	  holder.loaitu = 	(TextView) convertView.findViewById(R.id.tab3_tudien_loaitu);
		    	  holder.phienam = 	(TextView) convertView.findViewById(R.id.tab3_tudien_phienam);
		    	  holder.dichnghia = (TextView) convertView.findViewById(R.id.tab3_tudien_dichnghia);
		    	  holder.nhieunghia = (TextView) convertView.findViewById(R.id.tab3_tudien_nhieunghia);
		    	  holder.link_audio = (TextView) convertView.findViewById(R.id.tab3_tudien_link_audio);
		    	  holder.save = 	(Button) convertView.findViewById(R.id.tab3_tudien_save);
		    	  holder.listen = 	(Button) convertView.findViewById(R.id.tab3_tudien_listen);
		    	  convertView.setTag(holder);
		      } else {
		    	  holder = (ViewHolder) convertView.getTag();
		      }
		      final MainActivity.TuDien iContent = items.get(position);
		      
		      if(iContent.getIs_gone()==0) holder.nhieunghia.setVisibility(TextView.GONE);
		      	else holder.nhieunghia.setVisibility(TextView.VISIBLE);
		      holder.id.setText(iContent.getId());
		      holder.tu.setText(iContent.getTu());
		      holder.loaitu.setText(iContent.getLoaitu());
		      holder.phienam.setText(iContent.getPhienam());
		      holder.dichnghia.setText(iContent.getDichnghia());
		      holder.nhieunghia.setText(iContent.getNhieunghia());
		      holder.link_audio.setText(iContent.getLink_audio());
		      
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
		      holder.save.setOnClickListener( new OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {
		    		  if(instantFunctionCheck.checkInternetConnection())// no Internet connection- Internet
						{
		    			  new TuDien_Save().execute(holder.id.getText().toString());
						} 
		    		  else 
							Toast.makeText(getSherlockActivity(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
		    		  
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
							Toast.makeText(getSherlockActivity(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show(); 
		    		  
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
			   Button save;
			   Button listen;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	//
	//Lưu từ điển yêu thích
	//
	class TuDien_Save extends AsyncTask<String , String, String> 
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
			String idtudien = param[0];
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("idtudien", idtudien ));
			params.add(new BasicNameValuePair("iduser", String.valueOf(MainActivity.iduser) ));
			params.add(new BasicNameValuePair("request", "post_tudien_save"));
			
			JSONParser jj = new JSONParser();
			String json_string = jj.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_tudien_save.php", "POST", params);
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
					Toast.makeText(getSherlockActivity(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	
	class DownloadFileAsync extends AsyncTask<String, String, String> 
	{	  
		String filename;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getSherlockActivity());
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
			else if(_ok==0) Toast.makeText(getSherlockActivity(), "Không thành công. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
		}
	}
}

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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.doan_didong.MainActivity.NguPhap;

public class Tab5_NguPhap extends SherlockFragment 
{
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	
	private ListView tab5_nguphap_listview;
	private NguPhapCustomAdapter adapter;
	private GetAllNguPhap_second task;
	private GetAllNguPhap_first task_first;
    private boolean isloading;
    
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		Log.d("------------tab 5 onCreateView","-------------tab 5 onCreateView------------- ");
		View view = inflater.inflate(R.layout.tab5_listview_nguphap, container, false);
		isloading = false;
		tab5_nguphap_listview = (ListView) view.findViewById(R.id.tab5_nguphap_listview);
		
		if(MainActivity.itemlistNguPhap.size() == 0 )// && MainActivity.nguphap_start == 0)
		{
			task_first = new GetAllNguPhap_first();
	        task_first.execute();
	        
		} else 
		{
			adapter = new NguPhapCustomAdapter(getActivity(), R.layout.tab5_item_nguphap, MainActivity.itemlistNguPhap);
			tab5_nguphap_listview.setAdapter(adapter);
		}
		
		tab5_nguphap_listview.setOnScrollListener(new OnScrollListener() 
        {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
			{
				int position = firstVisibleItem + visibleItemCount;
				if( position == totalItemCount && isloading==false 
						&& MainActivity.itemlistNguPhap.size() < MainActivity.nguphap_all_item
						&& MainActivity.nguphap_end_of_list == 0 )
				{
					task = new GetAllNguPhap_second();
					task.execute();
					Toast.makeText(getActivity(), "Loading more...", Toast.LENGTH_SHORT).show();
				}
				if(position == totalItemCount && MainActivity.itemlistNguPhap.size() == MainActivity.nguphap_all_item
						&& MainActivity.nguphap_end_of_list == 0 )
				{
					MainActivity.nguphap_end_of_list = 1;
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
	
	//
	//Task first
	//
	class GetAllNguPhap_first extends AsyncTask<String, String, String> 
	{
		int success111;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(FunctionFolderAndFile.checkFileExist("version", "nguphap.txt") == false)
			{	try {
					FunctionFolderAndFile.downloadFileFromServer("nguphap.txt", "nguphap.txt");
				} catch (MalformedURLException e) 
				{
					e.printStackTrace();
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
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
			params.add(new BasicNameValuePair("start", String.valueOf(MainActivity.nguphap_start)));
			params.add(new BasicNameValuePair("request", "post_nguphap_get_all"));*/
			
//			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_nguphap_get_all.php", "POST", params);
			String json_string = FunctionFolderAndFile.readFromFile("version", "nguphap.txt");
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					if(json.getJSONArray("all_nguphap").length() > 0 && json.getInt(Constant.KEYWORD_SUCCESS) == 1 
							&& json.getInt("all_item") > 0) 
					{
							success111=1; 
							MainActivity.nguphap_all_item = json.getInt("all_item");
					}
					else success111=0;
					
					if (success111 == 1) 
					{
						JSONArray jsonArray = json.getJSONArray("all_nguphap"); 
						for (int i = 0; i < jsonArray.length(); i++) 
						{
							JSONObject c = jsonArray.getJSONObject(i);
							String id1 = c.getString("id");
							String tieude1 =MainActivity.nguphap_stt+". " + c.getString("tieude");
							String tomtat1 = c.getString("tomtat");
							String luot_view = c.getString("luot_view");
							String noidung1 = "";
							
							MainActivity.NguPhap item = new MainActivity.NguPhap(id1,tieude1,tomtat1,noidung1, luot_view);
							MainActivity.itemlistNguPhap.add(item);
							MainActivity.nguphap_stt++;
						}
						jsonArray=null;
					} else {}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {success111=3;}

			return null;
		}
		protected void onPostExecute(String file_url) {
			isloading=false;
			pDialog.dismiss();
			Log.e("---biến success111=","---biến success111="+success111);
			if(success111==1)
			{
					adapter = new NguPhapCustomAdapter(getActivity(), R.layout.tab5_item_nguphap, MainActivity.itemlistNguPhap);
					tab5_nguphap_listview.setAdapter(adapter);
					//int aa=MainActivity.nguphap_start;
					//if(aa + 100 < MainActivity.nguphap_all_item) MainActivity.nguphap_start = aa + 100;
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
	class GetAllNguPhap_second extends AsyncTask<String, String, String>
	{
		int success222;
		int all_curr_item;
		private JSONArray jsonArray; 
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			all_curr_item=MainActivity.itemlistNguPhap.size();
			isloading=true;
			success222=0;
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("start", String.valueOf(MainActivity.nguphap_start)));
			params.add(new BasicNameValuePair("request", "post_nguphap_get_all"));
			
			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_nguphap_get_all.php", "POST", params);
			if(json_string.length() != 0)
			{
				JSONObject jsonjson;
				try {
					jsonjson = JSONParser.getJSONObjectFromJString(json_string);
					if(jsonjson.getJSONArray("all_nguphap").length() > 0 && jsonjson.getInt("success") == 1 && jsonjson.getInt("all_item") > 0)
						success222=1; 
					else success222=0;
					if (success222 == 1 && jsonjson.getInt("all_item") > 0 && jsonjson.getJSONArray("all_nguphap").length() > 0) 
					{
						jsonArray = jsonjson.getJSONArray("all_nguphap"); 
						for (int i = 0; i < jsonArray.length(); i++)
						{
							
							JSONObject c = jsonArray.getJSONObject(i);
							String id1 = c.getString("id");
							String tieude1 =MainActivity.nguphap_stt + ". " + c.getString("tieude");
							String tomtat1 = c.getString("tomtat");
							String luot_view = c.getString("luot_view");
							String noidung1 ="";
							
							MainActivity.NguPhap item = new MainActivity.NguPhap(id1,tieude1,tomtat1, noidung1,luot_view);
							MainActivity.itemlistNguPhap.add(item);
							MainActivity.nguphap_stt++;
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
				if(MainActivity.itemlistNguPhap.size() > all_curr_item ) 
					{
						adapter.notifyDataSetChanged();
						int aa=MainActivity.nguphap_start;
						if(aa + 100 < MainActivity.nguphap_all_item) MainActivity.nguphap_start = aa + 100;
					}
			}
			if(success222==0) Log.d("---Success=0 lỗi internet","---Success=0 lỗi internet");
			if(success222==3) Log.d("---Success=3 JSON=rỗng", "---Success=3 JSON=rỗng");
		}

	}
	

	//
	//Class custom adapter quản lý listitem
	//
	public class NguPhapCustomAdapter extends ArrayAdapter<MainActivity.NguPhap> 
	{
		
		Context mContext;  
	    ArrayList<MainActivity.NguPhap> items=null;
	    int resource;
	    
		public NguPhapCustomAdapter(Context context, int textViewResourceId, ArrayList<MainActivity.NguPhap> objects) 
		{
			  super(context, textViewResourceId, objects);
			  this.mContext = context;
			  this.resource = textViewResourceId;
			  this.items = objects; 
		}
		  
		@Override
		public MainActivity.NguPhap getItem(int position) 
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
		    	  convertView = inflater.inflate(R.layout.tab5_item_nguphap, parent, false); 
		    	  
		    	  holder = new ViewHolder();
		    	  holder.id = 		(TextView) convertView.findViewById(R.id.tab5_nguphap_id);
		    	  holder.tieude = 	(TextView) convertView.findViewById(R.id.tab5_nguphap_tieude);
		    	  holder.tomtat = 	(TextView) convertView.findViewById(R.id.tab5_nguphap_tomtat);
		    	  holder.luot_view =(TextView) convertView.findViewById(R.id.tab5_nguphap_luot_view);
		    	  holder.next = 	(Button) convertView.findViewById(R.id.tab5_nguphap_next_btn);
		    	  
		    	  convertView.setTag(holder);
		      } else {
		    	  holder = (ViewHolder) convertView.getTag();
		      }
		      final NguPhap iContent = items.get(position);
		      
		      
		      holder.id.setText(iContent.getId());
		      holder.tieude.setText(iContent.getTieude());
		      holder.tomtat.setText(iContent.getTomtat());
		      holder.luot_view.setText("xem "+iContent.getLuot_view()+ " lần");
		      convertView.setOnClickListener(new OnClickListener() 
		      {
					@Override
					public void onClick(View v) 
					{
						Intent in = new Intent(getSherlockActivity(),Tab5_View_A_NguPhap.class);
						
						in.putExtra("iduser", MainActivity.iduser);
						in.putExtra("idnguphap", iContent.getId());
						in.putExtra("luot_view", iContent.getLuot_view());
						startActivity(in);	
					}
		      });
		      holder.next.setOnClickListener( new View.OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {
		    		  	Intent in = new Intent(getSherlockActivity(),Tab5_View_A_NguPhap.class);
						
						in.putExtra("iduser", MainActivity.iduser);
						in.putExtra("idnguphap", iContent.getId());
						in.putExtra("luot_view", iContent.getLuot_view());
						startActivity(in);		
		    	  }
			  });
		      return convertView;
		}
		private class ViewHolder 
		{
			   TextView id;
			   TextView tieude;
			   TextView tomtat;
			   TextView luot_view;
			   Button next;
		}
	}

}

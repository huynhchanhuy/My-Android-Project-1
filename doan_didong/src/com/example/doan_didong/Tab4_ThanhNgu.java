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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.example.doan_didong.MainActivity.ThanhNgu;
import com.example.doan_didong.Tab3_TuDien.GetAllTuDien_First;

public class Tab4_ThanhNgu extends SherlockFragment 
{
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private FunctionCheck instantFunctionCheck;
	
	
	private ListView tab4_thanhngu_listview;
	private ThanhNguCustomAdapter adapter;
	private GetAllThanhNgu_second task;
	private GetAllThanhNgu_first task_first;
    private boolean isloading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		isloading = false;
		instantFunctionCheck = new FunctionCheck(getSherlockActivity());
		Log.d("------tab4 onCreateView---------","size:"+MainActivity.itemlistThanhNgu.size() + " all_itemTHANHNGU:"+ MainActivity.thanhngu_all_item);
		View view = inflater.inflate(R.layout.tab4_listview_thanhngu, container, false);
		
		tab4_thanhngu_listview = (ListView) view.findViewById(R.id.tab4_thanhngu_listview);
		if(MainActivity.itemlistThanhNgu.size() == 0 )     //&& MainActivity.thanhngu_start == 0)
		{
			if(instantFunctionCheck.checkInternetConnection())
			{
				task_first = new GetAllThanhNgu_first();
		        task_first.execute();
			} else Toast.makeText(getSherlockActivity(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			
	        
		} else 
		{
			adapter = new ThanhNguCustomAdapter(getActivity(), R.layout.tab4_item_thanhngu, MainActivity.itemlistThanhNgu);
			tab4_thanhngu_listview.setAdapter(adapter);
		}
		
		tab4_thanhngu_listview.setOnScrollListener(new OnScrollListener() 
        {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
			{
				int position = firstVisibleItem + visibleItemCount;
				if( position == totalItemCount && isloading==false 
						&& MainActivity.itemlistThanhNgu.size() < MainActivity.thanhngu_all_item
						&& MainActivity.thanhngu_end_of_list == 0 )
				{
					if(instantFunctionCheck.checkInternetConnection())
					{
						task = new GetAllThanhNgu_second();
						task.execute();
						Toast.makeText(getActivity(), "Loading more ...", Toast.LENGTH_SHORT).show();
					} 
					else Toast.makeText(getSherlockActivity(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
					
				}
				if(position == totalItemCount && MainActivity.itemlistThanhNgu.size() == MainActivity.thanhngu_all_item
						&& MainActivity.thanhngu_end_of_list == 0 )
				{
					MainActivity.thanhngu_end_of_list = 1;
				}
			}
		});
		return view;
	}
	///
	//
	//
	class GetAllThanhNgu_first extends AsyncTask<String, String, String> 
	{
		int success111;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(FunctionFolderAndFile.checkFileExist("version", "thanhngu.txt") == false)
			{	try {
					FunctionFolderAndFile.downloadFileFromServer("thanhngu.txt", "thanhngu.txt");
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
			params.add(new BasicNameValuePair("start", String.valueOf(MainActivity.thanhngu_start)));
			params.add(new BasicNameValuePair("request", "post_thanhngu_get_all"));*/
			
//			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_thanhngu_get_all.php", "POST", params);
			String json_string = FunctionFolderAndFile.readFromFile("version", "thanhngu.txt");
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					if(json.getJSONArray("all_thanhngu").length() > 0 && json.getInt(Constant.KEYWORD_SUCCESS) == 1 
							&& json.getInt("all_item") > 0) 
					{
							success111=1; 
							MainActivity.thanhngu_all_item = json.getInt("all_item");
					}
					else success111=0;
					
					if (success111 == 1) 
					{
						JSONArray jsonArray = json.getJSONArray("all_thanhngu"); 
						for (int i = 0; i < jsonArray.length(); i++) 
						{
							
							JSONObject c = jsonArray.getJSONObject(i);
							String id1 = c.getString("id");
							String thanhngu1 =MainActivity.thanhngu_stt+". " + c.getString("thanhngu");
							String noidung1 = c.getString("noidung");
							String vidu1 = c.getString("vidu");
							
							MainActivity.ThanhNgu item = new MainActivity.ThanhNgu(id1,thanhngu1,noidung1,vidu1, 0);
							MainActivity.itemlistThanhNgu.add(item);
							MainActivity.thanhngu_stt++;
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
			if(success111==1)
			{
					adapter = new ThanhNguCustomAdapter(getActivity(), R.layout.tab4_item_thanhngu, MainActivity.itemlistThanhNgu);
					tab4_thanhngu_listview.setAdapter(adapter);
					//int aa=MainActivity.thanhngu_start;
					//if(aa + 100 < MainActivity.thanhngu_all_item) MainActivity.thanhngu_start = aa + 100;
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
	class GetAllThanhNgu_second extends AsyncTask<String, String, String>
	{
		int success222;
		int all_curr_item;
		private JSONArray jsonArray; 
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			all_curr_item=MainActivity.itemlistThanhNgu.size();
			isloading=true;
			success222=0;
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("start", String.valueOf(MainActivity.thanhngu_start)));
			params.add(new BasicNameValuePair("request", "post_thanhngu_get_all"));
			JSONParser jj = new JSONParser();
			String json_string = jj.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_thanhngu_get_all.php", "POST", params);
			if(json_string.length() != 0)
			{
				JSONObject jsonjson;
				try {
					jsonjson = JSONParser.getJSONObjectFromJString(json_string);
					if(jsonjson.getJSONArray("all_thanhngu").length() > 0 && jsonjson.getInt("success") == 1 && jsonjson.getInt("all_item") > 0)
						success222=1; 
					else success222=0;
					if (success222 == 1 && jsonjson.getInt("all_item") > 0 && jsonjson.getJSONArray("all_thanhngu").length() > 0) 
					{
						jsonArray = jsonjson.getJSONArray("all_thanhngu"); 
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject c = jsonArray.getJSONObject(i);
							String id1 = c.getString("id");
							String thanhngu1 =MainActivity.thanhngu_stt + ". " + c.getString("thanhngu");
							String noidung1 = c.getString("noidung");
							String vidu1 = c.getString("vidu");
							
							MainActivity.ThanhNgu item = new MainActivity.ThanhNgu(id1,thanhngu1,noidung1,vidu1, 0);
							MainActivity.itemlistThanhNgu.add(item);
							MainActivity.thanhngu_stt++;
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
				if(MainActivity.itemlistThanhNgu.size() > all_curr_item ) 
					{
						adapter.notifyDataSetChanged();
						int aa=MainActivity.thanhngu_start;
						if(aa + 100 < MainActivity.thanhngu_all_item) MainActivity.thanhngu_start = aa + 100;
					}
			}
			if(success222==0) Log.d("---Success=0 lỗi internet","---Success=0 lỗi internet");
			if(success222==3) Log.d("---Success=3 JSON=rỗng", "---Success=3 JSON=rỗng");
		}

	}
	//
	//
	//
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setUserVisibleHint(true);
	}
	
	
	//
	//Class custom adapter quản lý listitem
	//
	public class ThanhNguCustomAdapter extends ArrayAdapter<MainActivity.ThanhNgu> 
	{
		
		Context mContext;  
	    ArrayList<MainActivity.ThanhNgu> items=null;
	    int resource;
	    
		public ThanhNguCustomAdapter(Context context, int textViewResourceId, ArrayList<MainActivity.ThanhNgu> objects) 
		{
			  super(context, textViewResourceId, objects);
			  this.mContext = context;
			  this.resource = textViewResourceId;
			  this.items = objects; 
		}
		  
		@Override
		public MainActivity.ThanhNgu getItem(int position) 
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
		    	  convertView = inflater.inflate(R.layout.tab4_item_thanhngu, parent, false); 
		    	  
		    	  holder = new ViewHolder();
		    	  holder.id = 		(TextView) convertView.findViewById(R.id.tab4_thanhngu_id);
		    	  holder.thanhngu = 		(TextView) convertView.findViewById(R.id.tab4_thanhngu_thanhngu);
		    	  holder.noidung = 	(TextView) convertView.findViewById(R.id.tab4_thanhngu_noidung);
		    	  holder.vidu = 	(TextView) convertView.findViewById(R.id.tab4_thanhngu_vidu);
		    	  holder.save = 	(Button) convertView.findViewById(R.id.tab4_thanhngu_save);
		    	  convertView.setTag(holder);
		      } else {
		    	  holder = (ViewHolder) convertView.getTag();
		      }
		      final ThanhNgu iContent = items.get(position);
		      
		      if(iContent.getIs_gone()==0) holder.vidu.setVisibility(TextView.GONE);
		      	else holder.vidu.setVisibility(TextView.VISIBLE);
		      holder.id.setText(iContent.getId());
		      holder.thanhngu.setText(iContent.getThanhngu());
		      holder.noidung.setText(iContent.getNoidung());
		      if(iContent.getVidu().length() == 0) holder.vidu.setText("Không có ví dụ.");
		      else holder.vidu.setText(iContent.getVidu());
		      
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
		      holder.save.setOnClickListener( new OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {
		    		  if(instantFunctionCheck.checkInternetConnection())// no Internet connection- Internet
					  {
		    			  new ThanhNgu_Save().execute(holder.id.getText().toString());
					  } 
					  else Toast.makeText(getSherlockActivity(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
		    		  
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
			   Button save;
		}
	}
	//
	//Lưu từ điển yêu thích
	//
	class ThanhNgu_Save extends AsyncTask<String , String, String> 
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
			String idthanhngu = param[0];
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("idthanhngu", idthanhngu ));
			params.add(new BasicNameValuePair("iduser", String.valueOf(MainActivity.iduser) ));
			params.add(new BasicNameValuePair("request", "post_thanhngu_save"));
			
			JSONParser jj = new JSONParser();
			String json_string = jj.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_thanhngu_save.php", "POST", params);
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
				Toast.makeText(getSherlockActivity(), "Thành ngữ này đã được lưu.", Toast.LENGTH_SHORT).show();
			}
			if(success==0 && da_ton_tai == 0)
			{
				Toast.makeText(getSherlockActivity(), "Không thành công. Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			}
			if(success==3 && da_ton_tai == 0)
			{
				Toast.makeText(getSherlockActivity(), "Không thành công. Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			}
		}

	}
}

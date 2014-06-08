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
import com.example.doan_didong.MainActivity.KetQua;

public class Tab10_KetQua extends SherlockFragment 
{
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private FunctionCheck instantFunctionCheck;
	
	private TextView tab10_all_item;
	private ListView tab10_ketqua_listview;
	private KetQuaCustomAdapter adapter;
	private GetAllKetQua_first task_first;
    private boolean isloading;
    int _position;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("---tab 10 onCreateView","----------tab 10 onCreateView------ ");
		View view = inflater.inflate(R.layout.tab10_ketqua, container, false);
		_position=-1;
		isloading = false;
		instantFunctionCheck = new FunctionCheck(getSherlockActivity());
		
		tab10_ketqua_listview = (ListView) view.findViewById(R.id.tab10_listview_ketqua);
		tab10_all_item = (TextView)view.findViewById(R.id.tab10_ketqua_all_item);
		//if(MainActivity.itemlistKetQua.size() == 0 && isloading==false)
		if(isloading==false)
		{
			if(instantFunctionCheck.checkInternetConnection())
			{
				MainActivity.itemlistKetQua.clear();
				task_first = new GetAllKetQua_first();
		        task_first.execute();
			} else Toast.makeText(getSherlockActivity(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			
	        
		} else 
		{
			adapter = new KetQuaCustomAdapter(getSherlockActivity(), R.layout.tab10_ketqua_item, MainActivity.itemlistKetQua);
			tab10_ketqua_listview.setAdapter(adapter);
		}
		
		return view;
	}
	///
		//
		//
		class GetAllKetQua_first extends AsyncTask<String, String, String> 
		{
			int success111;
			int all_item;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				/*pDialog = new ProgressDialog(getSherlockActivity());
				pDialog.setMessage("Loading ...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();*/
				success111 = 0;
				isloading=true;
			}

			protected String doInBackground(String... args) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("iduser", String.valueOf(MainActivity.iduser)));
				params.add(new BasicNameValuePair("request", "totnghiep_ketqua"));
				
				String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_ketqua.php", "POST", params);
				if(json_string.length() != 0)
				{
					JSONObject json;
					try {
						json = JSONParser.getJSONObjectFromJString(json_string);
						success111	= json.getInt(Constant.KEYWORD_SUCCESS);
						all_item 	= json.getInt("all_item");
						if(all_item==0)
						{
							success111=4;
							Log.e("---all_item = 0","---all_item = 0");
						}
						else if (success111 == 1) 
						{
							JSONArray jsonArray = json.getJSONArray("all_ketqua"); 
							for (int i = 0; i < jsonArray.length(); i++) 
							{
								
								JSONObject c = jsonArray.getJSONObject(i);
								String id =    c.getString("id");
								String title = c.getString("title");
								String diemso = c.getString("diemso");
								String ngaythi = c.getString("ngaythi");
								String socauhoi = c.getString("socauhoi");
								String socaudung = c.getString("socaudung");
								MainActivity.KetQua item = new MainActivity.KetQua( id,  title, diemso, ngaythi,socauhoi, socaudung);
								MainActivity.itemlistKetQua.add(item);
							}
							jsonArray=null;
						}
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
				//pDialog.dismiss();
				if(success111==4)
				{
					tab10_all_item.setText("Chưa có kết quả thi nào.");
					tab10_all_item.setVisibility(View.VISIBLE);
				}
				else if(success111==1)
				{
						adapter = new KetQuaCustomAdapter(getActivity(), R.layout.tab10_ketqua_item, MainActivity.itemlistKetQua);
						tab10_ketqua_listview.setAdapter(adapter);
				}
				if(success111==0) 
				{
					Log.d("---Success=0 lỗi internet","---Success=0 lỗi internet");
					task_first = new GetAllKetQua_first();
				    task_first.execute();
				}
				if(success111==3) 
				{
					Log.d("---Success=3 JSON=rỗng", "---Success=3 JSON=rỗng");
					task_first = new GetAllKetQua_first();
				    task_first.execute();
				}
			}

		}
		//
		//Class custom adapter quản lý listitem
		//
		public class KetQuaCustomAdapter extends ArrayAdapter<MainActivity.KetQua> 
		{
			
			Context mContext;  
		    ArrayList<MainActivity.KetQua> items=null;
		    int resource;
		    
			public KetQuaCustomAdapter(Context context, int textViewResourceId, ArrayList<MainActivity.KetQua> objects) 
			{
				  super(context, textViewResourceId, objects);
				  this.mContext = context;
				  this.resource = textViewResourceId;
				  this.items = objects; 
			}
			  
			@Override
			public MainActivity.KetQua getItem(int position) 
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
			    	  LayoutInflater inflater = getLayoutInflater(getArguments());
			    	  convertView = inflater.inflate(R.layout.tab10_ketqua_item, parent, false); 
			    	  
			    	  holder = new ViewHolder();
			    	  holder.id = 		(TextView) convertView.findViewById(R.id.tab10_ketqua_id);
			    	  holder.title = 		(TextView) convertView.findViewById(R.id.tab10_ketqua_title);
			    	  holder.diemso = 	(TextView) convertView.findViewById(R.id.tab10_ketqua_diemso);
			    	  holder.ngaythi = 	(TextView) convertView.findViewById(R.id.tab10_ketqua_ngaythi);
			    	  holder.socaudung = 	(TextView) convertView.findViewById(R.id.tab10_ketqua_socaudung);
			    	  holder.delete = 	(Button) convertView.findViewById(R.id.tab10_ketqua_btn_delete);
			    	  convertView.setTag(holder);
			      } else {
			    	  holder = (ViewHolder) convertView.getTag();
			      }
			      final KetQua iContent = items.get(position);
			      
			      holder.id.setText(iContent.getId());
			      holder.title.setText("Bài Thi: "+iContent.getTitle());
			      holder.diemso.setText("Điểm số: "+iContent.getDiemso());
			      holder.ngaythi.setText("Ngày thi: "+iContent.getNgaythi());
			      holder.socaudung.setText("Số câu đúng: "+iContent.getSocaudung()+"/"+iContent.getSocauhoi());
			      
			      convertView.setOnClickListener(new OnClickListener() 
			      {
						@Override
						public void onClick(View v) 
						{
							
						}
			      });
			      holder.delete.setOnClickListener( new OnClickListener() 
			      {
			     
			    	  @Override
			    	  public void onClick(View v) 
			    	  {
			    		  if(instantFunctionCheck.checkInternetConnection())// no Internet connection- Internet
						  {
			    			  _position=position;
			    			  new KetQua_Delete().execute(holder.id.getText().toString());
						  } 
						  else Toast.makeText(getSherlockActivity(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			    		  
			    	  }
				  });
			      return convertView;
			}
			private class ViewHolder 
			{
				   TextView id;
				   TextView title;
				   TextView diemso;
				   TextView ngaythi;
				   TextView socaudung;
				   Button delete;
			}
		}
		//
		//Lưu từ điển yêu thích
		//
		class KetQua_Delete extends AsyncTask<String , String, String> 
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
				String idketqua = param[0];
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("idketqua", idketqua ));
				params.add(new BasicNameValuePair("iduser", String.valueOf(MainActivity.iduser) ));
				params.add(new BasicNameValuePair("request", "totnghiep_ketqua_delete"));
				
				JSONParser jj = new JSONParser();
				String json_string = jj.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_ketqua_delete.php", "POST", params);
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
					} catch (JSONException e) {success = 0; e.printStackTrace();}
				} else {success=3;};

				return null;
			}
			protected void onPostExecute(String file_url) 
			{
				pDialog.dismiss();
				if(success==1 && da_ton_tai == 1)
				{
					Toast.makeText(getSherlockActivity(), "Xóa thành công.", Toast.LENGTH_SHORT).show();
					if(_position != -1) 
					{
						MainActivity.itemlistKetQua.remove(_position);
						adapter.notifyDataSetChanged();
					}
					_position = -1;
				}
				if(success==0 && da_ton_tai == 0)
				{
					_position = -1;
					Toast.makeText(getSherlockActivity(), "Không thành công. Kiểm tra internet.", Toast.LENGTH_SHORT).show();
				}
				if(success==3 && da_ton_tai == 0)
				{
					_position = -1;
					Toast.makeText(getSherlockActivity(), "Không thành công. Kiểm tra internet.", Toast.LENGTH_SHORT).show();
				}
			}

		}
		//
		//
		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			setUserVisibleHint(true);
		}
		@Override
		public void onResume()
		{
			super.onResume();
		    Log.d("tab 10 Resuming", "tab10 resume. ");
		}

}

package com.example.doan_didong;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.doan_didong.MainActivity.DownloadDataFromServerProgress;
import com.example.doan_didong.MainActivity.TracNghiem;

public class Tab8_ThiTracNghiem extends SherlockFragment 
{
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private FunctionCheck instantFunctionCheck;
	
	private ListView tab8_list_tracnghiem;
	private TracNghiemCustomAdapter adapter;
	private GetAllTracNghiem_first task_first;
    private boolean isloading;
    private DownloadDataFromServerProgress task_downloadfiledata;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		Log.d("onCreate"," tab8 TracNghiem onCreate");
		View view = inflater.inflate(R.layout.tab8_listview_tracnghiem, container, false);
		isloading = false;
		instantFunctionCheck = new FunctionCheck(getSherlockActivity());
		tab8_list_tracnghiem = (ListView) view.findViewById(R.id.tab8_list_tracnghiem_xml);
		
		
		
		if(MainActivity.itemlistTracNghiem.size() == 0 )
		{
			if(instantFunctionCheck.checkInternetConnection())
			{
				if(FunctionFolderAndFile.checkFileExist("version", "level.txt") == false)
				{	
					MainActivity.DownloadDataFromServerProgress a = new MainActivity.DownloadDataFromServerProgress();
					a.execute("level.txt");
					task_first = new GetAllTracNghiem_first();
			        task_first.execute();
				}
				else
				{
					task_first = new GetAllTracNghiem_first();
		        	task_first.execute();
				}
			} else Toast.makeText(getSherlockActivity(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
		} else 
		{
			adapter = new TracNghiemCustomAdapter(getActivity(), R.layout.tab8_item_tracnghiem, MainActivity.itemlistTracNghiem);
			tab8_list_tracnghiem.setAdapter(adapter);
		}
		
		downloadAllLevelTracNghiem();
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
		Log.d("tab8 List TracNghiem onResume"," tab8 List TracNghiem onResume");
	}


	@Override
	public void onStart() 
	{
		super.onStart();
		Log.d("tab8 List TracNghiem onStart"," tab8 List TracNghiem onStart");
	}
	
	//
	//Task first
	//
	class GetAllTracNghiem_first extends AsyncTask<String, String, String> 
	{
		int success;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(FunctionFolderAndFile.checkFileExist("version", "level.txt") == false)
			{	
				//FunctionFolderAndFile.downloadFileFromServer("level.txt", "level.txt");
				MainActivity.DownloadDataFromServerProgress a = new MainActivity.DownloadDataFromServerProgress();
				a.execute("level.txt");
			}
			pDialog = new ProgressDialog(getSherlockActivity());
			pDialog.setMessage("Tải dữ liệu ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();	        
			success = 0;
			isloading=true;
		}

		protected String doInBackground(String... args) 
		{
			String json_string = FunctionFolderAndFile.readFromFile("version", "level.txt");
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					if(json.getJSONArray("all_level").length() > 0 && json.getInt("success") == 1 
							&& json.getInt("count_level") > 0) 
					{
							success=1; 
							MainActivity.tracnghiem_all_item = json.getInt("count_level");
					}
					else success=0;
					
					if (success == 1) 
					{
						JSONArray jsonArray = json.getJSONArray("all_level"); 
						for (int i = 0; i < jsonArray.length(); i++) 
						{
							JSONObject c = jsonArray.getJSONObject(i);
							String id1 = c.getString("id");
							String title1 = c.getString("title");
							String text1 = c.getString("text");
							
							MainActivity.TracNghiem item = new MainActivity.TracNghiem(id1, title1, text1);
							MainActivity.itemlistTracNghiem.add(item);
						}
						jsonArray=null;
					} else {}
				} catch (JSONException e) 
				{
					e.printStackTrace();
				}
			} else {success=3;}

			return null;
		}
		protected void onPostExecute(String file_url) 
		{
			isloading=false;
			pDialog.dismiss();
			Log.e("---biến success111=","---biến success111="+success);
			if(success==1)
			{
					adapter = new TracNghiemCustomAdapter(getActivity(), R.layout.tab8_item_tracnghiem, MainActivity.itemlistTracNghiem);
					tab8_list_tracnghiem.setAdapter(adapter);
			}
			if(success==0) 
			{
				Log.d("---Success=0 lỗi internet","---Success=0 lỗi internet");
				Toast.makeText(getSherlockActivity(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			}
			if(success==3) 
			{
				Log.d("---Success=3 JSON=rỗng", "---Success=3 JSON=rỗng");
				Toast.makeText(getSherlockActivity(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			}
		}

	}
	//
	//
	//
	public void downloadAllLevelTracNghiem()
	{
		Log.e("-----vao function download all file tracnghiem","---------vao function download all file tracnghiem");
		if(FunctionFolderAndFile.createFolderByFoldername("tracnghiem")) //tạo folder tracnghiem trong sdcard
		{
			Log.e("---tao folder trac nghiem success","---------tao folder trac nghiem success");
			if(instantFunctionCheck.checkInternetConnection())
			{
				Log.e("------tien hanh download file tracnghiem","---------tien hanh download file tracnghiem");
				if(FunctionFolderAndFile.checkFileExist("tracnghiem", "level1.txt")==true)
				{
					Log.e("-----level1.txt đã tồn tại","-----level1.txt đã tồn tại");
				}
				else
				{
					task_downloadfiledata = new DownloadDataFromServerProgress();
					task_downloadfiledata.execute("level1.txt");
				}
				
				if(FunctionFolderAndFile.checkFileExist("tracnghiem", "level2.txt")==true)
				{
					Log.e("-----level2.txt đã tồn tại","-----level2.txt đã tồn tại");
				}
				else
				{
					task_downloadfiledata = new DownloadDataFromServerProgress();
					task_downloadfiledata.execute("level2.txt");
				}
				
				if(FunctionFolderAndFile.checkFileExist("tracnghiem", "level3.txt")==true)
				{
					Log.e("-----level3.txt đã tồn tại","-----level3.txt đã tồn tại");
				}
				else
				{
					task_downloadfiledata = new DownloadDataFromServerProgress();
					task_downloadfiledata.execute("level3.txt");
				}
				
				if(FunctionFolderAndFile.checkFileExist("tracnghiem", "level4.txt")==true)
				{
					Log.e("-----level4.txt đã tồn tại","-----level4.txt đã tồn tại");
				}
				else
				{
					task_downloadfiledata = new DownloadDataFromServerProgress();
					task_downloadfiledata.execute("level4.txt");
				}
				
				if(FunctionFolderAndFile.checkFileExist("tracnghiem", "level5.txt")==true)
				{
					Log.e("-----level5.txt đã tồn tại","-----level5.txt đã tồn tại");
				}
				else
				{
					task_downloadfiledata = new DownloadDataFromServerProgress();
					task_downloadfiledata.execute("level5.txt");
				}
				
				if(FunctionFolderAndFile.checkFileExist("tracnghiem", "level6.txt")==true)
				{
					Log.e("-----level6.txt đã tồn tại","-----level6.txt đã tồn tại");
				}
				else
				{
					task_downloadfiledata = new DownloadDataFromServerProgress();
					task_downloadfiledata.execute("level6.txt");
				}
				
				if(FunctionFolderAndFile.checkFileExist("tracnghiem", "level_true_false.txt")==true)
				{
					Log.e("-----level_true_false.txt đã tồn tại","-----level_true_false.txt đã tồn tại");
				}
				else
				{
					task_downloadfiledata = new DownloadDataFromServerProgress();
					task_downloadfiledata.execute("level_true_false.txt");
				}
				
				if(FunctionFolderAndFile.checkFileExist("tracnghiem", "level_fill_blank.txt")==true)
				{
					Log.e("-----level_fill_blank.txt đã tồn tại","-----level_fill_blank.txt đã tồn tại");
				}
				else
				{
					task_downloadfiledata = new DownloadDataFromServerProgress();
					task_downloadfiledata.execute("level_fill_blank.txt");
				}
				
				if(FunctionFolderAndFile.checkFileExist("tracnghiem", "level_sort.txt")==true)
				{
					Log.e("-----level_sort.txt đã tồn tại","-----level_sort.txt đã tồn tại");
				}
				else
				{
					task_downloadfiledata = new DownloadDataFromServerProgress();
					task_downloadfiledata.execute("level_sort.txt");
				}
			} 
			else
			{
				Toast.makeText(getSherlockActivity(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			}
		}
	}
	//
	//Class custom adapter quản lý listitem
	//
	public class TracNghiemCustomAdapter extends ArrayAdapter<MainActivity.TracNghiem> 
	{
		
		Context mContext;  
	    ArrayList<MainActivity.TracNghiem> items=null;
	    int resource;
	    
		public TracNghiemCustomAdapter(Context context, int textViewResourceId, ArrayList<MainActivity.TracNghiem> objects) 
		{
			  super(context, textViewResourceId, objects);
			  this.mContext = context;
			  this.resource = textViewResourceId;
			  this.items = objects; 
		}
		  
		@Override
		public MainActivity.TracNghiem getItem(int position) 
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
		    	  convertView = inflater.inflate(R.layout.tab8_item_tracnghiem, parent, false); 
		    	  
		    	  holder = new ViewHolder();
		    	  holder.id = 		(TextView) convertView.findViewById(R.id.tab8_tracnghiem_id);
		    	  holder.title = 	(TextView) convertView.findViewById(R.id.tab8_tracnghiem_title);
		    	  holder.text = 	(TextView) convertView.findViewById(R.id.tab8_tracnghiem_text);
		    	  
		    	  holder.sl_10cau = 	(Button) convertView.findViewById(R.id.tab8_btn_10cau);
		    	  holder.sl_20cau = 	(Button) convertView.findViewById(R.id.tab8_btn_20cau);
		    	  holder.sl_30cau = 	(Button) convertView.findViewById(R.id.tab8_btn_30cau);
		    	  convertView.setTag(holder);
		      } else {
		    	  holder = (ViewHolder) convertView.getTag();
		      }
		      final TracNghiem iContent = items.get(position);
		      
		      if("7".equals(iContent.getId())  || "8".equals(iContent.getId() ) || "9".equals(iContent.getId() ) )
		      {
		    	  Log.e("level ID =7 OR =8","------------level ID =7 OR =8");
		    	  holder.id.setText(iContent.getId());
			      holder.title.setText(iContent.getTitle());
			      holder.text.setText(iContent.getText()); 
			      holder.sl_10cau.setText("THI");
			      holder.sl_20cau.setVisibility(View.GONE);
			      holder.sl_30cau.setVisibility(View.GONE);
		      }
		      else
		      {
		    	  holder.id.setText(iContent.getId());
			      holder.title.setText(iContent.getTitle());
			      holder.text.setText(iContent.getText());
			      holder.sl_10cau.setText("10 câu");
			      holder.sl_20cau.setVisibility(View.VISIBLE);
			      holder.sl_30cau.setVisibility(View.VISIBLE);
		      }
		      
		      convertView.setOnClickListener(new OnClickListener() 
		      {
					@Override
					public void onClick(View v) 
					{	
					}
		      });
		      holder.sl_10cau.setOnClickListener( new View.OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {		
		    		  if("7".equals(iContent.getId()) )
				      {
		    			  Intent in = new Intent(getSherlockActivity(),Tab8_True_False.class);
						  startActivity(in);
				      }
				      else  if("8".equals(iContent.getId()) )
				      {
				    	  Intent in = new Intent(getSherlockActivity(),Tab8_Fill_Blank.class);
						  startActivity(in);
				      }
				      else  if("9".equals(iContent.getId()) )
				      {
				    	  Intent in = new Intent(getSherlockActivity(), Tab8_Sort.class);
						  startActivity(in);
				      }
				      else
				      {
				    	  Intent in = new Intent(getSherlockActivity(),Tab8_One_TracNghiem.class);
						  
						  in.putExtra("id_user", String.valueOf(MainActivity.iduser));
						  in.putExtra("id_level", iContent.getId().toString());
						  in.putExtra("socauhoi", "10");
						  startActivity(in);
				      }
		    		  	    
		    	  }
			  });
		      holder.sl_20cau.setOnClickListener( new View.OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {		
		    		  if("7".equals(iContent.getId()) )
				      {
				      }
				      else  if("8".equals(iContent.getId()) )
				      {
				      }
				      else  if("9".equals(iContent.getId()) )
				      {
				      }
				      else
				      {
				    	  Intent in = new Intent(getSherlockActivity(),Tab8_One_TracNghiem.class);
						  
						  in.putExtra("id_user", String.valueOf(MainActivity.iduser));
						  in.putExtra("id_level", iContent.getId().toString());
						  in.putExtra("socauhoi", "20");
						  startActivity(in);
				      }
		    	  }
			  });
		      holder.sl_30cau.setOnClickListener( new View.OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {	
		    		  if("7".equals(iContent.getId()) )
				      {
				      }
				      else  if("8".equals(iContent.getId()) )
				      {
				      }
				      else  if("9".equals(iContent.getId()) )
				      {
				      }
				      else
				      {
				    	  Intent in = new Intent(getSherlockActivity(),Tab8_One_TracNghiem.class);
						  
						  in.putExtra("id_user", String.valueOf(MainActivity.iduser));
						  in.putExtra("id_level", iContent.getId().toString());
						  in.putExtra("socauhoi", "30");
						  startActivity(in);
				      }
		    	  }
			  });
		      return convertView;
		}
		private class ViewHolder 
		{
			   TextView id;
			   TextView title;
			   TextView text;
			   Button sl_10cau;
			   Button sl_20cau;
			   Button sl_30cau;
		}
	}
	//
	//Download file level 1->6.txt
	//
	//
	class DownloadDataFromServerProgress extends AsyncTask<String, String, String> 
	{	  
		int download_ok;
		String filename;
		String toast;
		BufferedInputStream in;
	    FileOutputStream fout;
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			download_ok=0;
			in = null;
			fout = null;
		}

		@Override
		protected String doInBackground(String... param) 
		{
			
			filename=param[0];
			String file_server = Constant.BASE_URL_SERVER+ "tracnghiem/" + param[0];
			String file_sdcard = Environment.getExternalStorageDirectory() + "/hocngoaingu/tracnghiem/" + param[0];
			Log.e("----------download file: "+ param[0],"----------download file: "+ file_server);
			int count;
		    try
		    {
		    	    URL url = new URL(file_server);
		            in = new BufferedInputStream(url.openStream());
		            fout = new FileOutputStream(file_sdcard);
		 
		            byte data[] = new byte[1024];
		            Log.e("------Availabe download------",""+String.valueOf(in.available()));
		            if(in.available() > 0)
		            {
		            	download_ok=1;
		            	while ((count = in.read(data, 0, 1024)) != -1)
			            {
			                fout.write(data, 0, count);
			            }
		            }
		    } catch (MalformedURLException e) 
		    {
		    	e.printStackTrace(); download_ok=0;
		    } catch (IOException e) 
		    {
		    	e.printStackTrace(); download_ok=0;
		    }
		    finally
		    {
		    	try {    
		    		if (in != null) in.close();
		    		if (fout != null) fout.close();
		    	}
		    	catch (IOException e) 
		    	{
						e.printStackTrace();
				}
		    }
			return null;
		}

		@Override
		protected void onPostExecute(String unused) 
		{
			if(download_ok==1) { Log.e("-------download file data SUCCESS-------","-------download file data SUCCESS: "+download_ok); }
			else if(download_ok==0) 
			{
				Log.e("-------download file data FAIL-------","-------download file data FAIL-------");
				new DownloadDataFromServerProgress().execute(filename);
			}
		}
	}
	//
	//
	//

}

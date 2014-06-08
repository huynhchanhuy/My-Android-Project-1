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
import com.example.doan_didong.Tab8_ThiTracNghiem.DownloadDataFromServerProgress;

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


public class Tab8_True_False extends Activity
{
	ArrayList<Tab8_True_False.QuestionTrueFalse> itemlist;
	JSONParser jsonParser = new JSONParser();
	
	private FunctionCheck instantFunctionCheck;
	private ProgressDialog pDialog;
	
	private ListView tab8_true_false;
	private QuestionTrueFalseCustomAdapter adapter;
	private GetAllTuDien_First task_first;
	private DownloadDataFromServerProgress task_downloadfiledata;
	int success;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab8_listview_true_false);
		tab8_true_false= (ListView)findViewById(R.id.tab8_listview_true_false);
		instantFunctionCheck = new FunctionCheck(getApplicationContext());
		itemlist = new ArrayList<Tab8_True_False.QuestionTrueFalse>();
		
		if( itemlist.size() == 0 )
		{
			if(instantFunctionCheck.checkInternetConnection())
			{
				task_first = new GetAllTuDien_First();
		        task_first.execute();
			} else Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			
	        
		} /*else 
		{
			adapter = new QuestionTrueFalseCustomAdapter(getApplicationContext(), R.layout.tab8_item_true_false, itemlist);
			tab8_true_false.setAdapter(adapter);
		}*/
		if(instantFunctionCheck.checkInternetConnection())
		{
			Log.e("------tien hanh download file level_question_true_false","---------tien hanh download file level_question_true_false");
			if(FunctionFolderAndFile.checkFileExist("tracnghiem", "level_question_true_false.txt")==true)
			{
			}
			else
			{
				task_downloadfiledata = new DownloadDataFromServerProgress();
				task_downloadfiledata.execute("level_question_true_false.txt");
			}
		}
	}
	
	@Override
	public void onBackPressed() 
	{
		itemlist = null;
		super.onBackPressed();
	}
	//
	//
	//
	class GetAllTuDien_First extends AsyncTask<String, String, String> 
	{
		int success111;
		int all_item;
		String json_string = "";
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			success111 = 0;
			pDialog = new ProgressDialog(Tab8_True_False.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) 
		{
			/*List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("request", "post_tudien_get_all"));
			params.add(new BasicNameValuePair("iduser", iduser));
			params.add(new BasicNameValuePair("loai", loai));
			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_yeuthich_tudien.php", "POST", params);
			*/
			if(FunctionFolderAndFile.checkFileExist("tracnghiem", "level_question_true_false.txt")==true)
			{
				json_string = FunctionFolderAndFile.readFromFile("tracnghiem", "level_true_false.txt");
			}
			else
			{
				json_string="";
			}
			
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					
					success111=json.getInt("success");
					all_item = json.getInt("num_question");
					if (success111 == 1) 
					{
						JSONArray jsonArray = json.getJSONArray("all_question"); 
						for (int i = 0; i < jsonArray.length(); i++) 
						{
							JSONObject c = jsonArray.getJSONObject(i);
							String id1 = c.getString("id");
							String title1 = c.getString("title");
							String doanvan1 = c.getString("doanvan");
							String ngaytao1 = c.getString("ngay_tao");
							String ngaysua1 = c.getString("ngay_sua");
							String numonequestion = c.getString("num_one_question");
							
							Tab8_True_False.QuestionTrueFalse item = new Tab8_True_False.QuestionTrueFalse(id1, title1, doanvan1, ngaytao1, ngaysua1,numonequestion);
							itemlist.add(item);
						}
						
					}
					} catch (JSONException e) 
					{
						success111=0;
						e.printStackTrace();
					}
			} 
			else 
			{
				success111=3;
			}

			return null;
		}
		protected void onPostExecute(String file_url) 
		{
			pDialog.dismiss();
			if(success111==1)
			{
					adapter = new QuestionTrueFalseCustomAdapter(getApplicationContext(), R.layout.tab8_item_true_false, itemlist);
					tab8_true_false.setAdapter(adapter);
					Log.e("---Kết quả get_lần đầu ", "Success");
			}
			else if(success111==0) 
				{
					Log.d("---Success=0 lỗi internet","---Success=0 lỗi internet");
					task_first = new GetAllTuDien_First();
			        task_first.execute();
				}
				
			else if(success111==3) 
				{
					Log.d("---Success=3 JSON=rỗng", "---Success=3 JSON=rỗng");
					task_first = new GetAllTuDien_First();
			        task_first.execute();
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
	//Class custom adapter quản lý listitem
	//
	public class QuestionTrueFalseCustomAdapter extends ArrayAdapter<Tab8_True_False.QuestionTrueFalse> 
	{
		
		Context mContext;  
	    ArrayList<Tab8_True_False.QuestionTrueFalse> items=null;
	    int resource;
	    
		public QuestionTrueFalseCustomAdapter(Context context, int textViewResourceId,ArrayList<Tab8_True_False.QuestionTrueFalse> objects) 
		{
			  super(context, textViewResourceId, objects);
			  this.mContext = context;
			  this.resource = textViewResourceId;
			  this.items = objects; 
		}
		  
		@Override
		public Tab8_True_False.QuestionTrueFalse getItem(int position) 
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
		    	  convertView = inflater.inflate(R.layout.tab8_item_true_false, parent, false); 
		    	  
		    	  holder = new ViewHolder();
		    	  holder.id = 		(TextView) convertView.findViewById(R.id.tab8_true_false_id);
		    	  holder.title = 		(TextView) convertView.findViewById(R.id.tab8_true_false_title);
		    	  holder.doanvan = 	(TextView) convertView.findViewById(R.id.tab8_true_false_doanvan);
		    	  holder.btn_thi = 	(Button) convertView.findViewById(R.id.tab8_true_false_btn_start);
		    	  holder.num_question = (TextView) convertView.findViewById(R.id.tab8_true_false_num);
		    	  convertView.setTag(holder);
		      } else {
		    	  holder = (ViewHolder) convertView.getTag();
		      }
		      final Tab8_True_False.QuestionTrueFalse iContent = items.get(position);
		      
		      holder.id.setText(iContent.getId());
		      holder.title.setText(iContent.getTitle());
		      holder.doanvan.setText(iContent.getDoanvan());
		      holder.num_question.setText("Total question: "+iContent.getNumonequestion());
		      convertView.setOnClickListener(new OnClickListener() 
		      {
					@Override
					public void onClick(View v) 
					{
						
					}
		      });
		      holder.btn_thi.setOnClickListener( new OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {
		    		if(instantFunctionCheck.checkInternetConnection())// no Internet connection- Internet
					{
		    			Intent in = new Intent(getApplicationContext(),Tab8_One_True_False.class);
						in.putExtra("id_user", String.valueOf(MainActivity.iduser));
						in.putExtra("id_level", iContent.getId());
						in.putExtra("socauhoi", iContent.getNumonequestion());
						startActivity(in);
					} 
		    		else
		    		{
		    			Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
		    		}
		    	  }
			  });
		      return convertView;
		}
		private class ViewHolder 
		{
			   TextView id;
			   TextView title;
			   TextView doanvan;
			   TextView num_question;
			   Button btn_thi;
		}
	}
	
	//
	//
	public class QuestionTrueFalse 
	{
		private String id; 
	    private String title;
	    private String doanvan;
	    private String ngay_tao;
	    private String ngay_sua;
	    private String num_one_question;
	    
	    public QuestionTrueFalse(String id, String title, String doanvan, String ngay_tao, String ngay_sua, String num_one_question)
	    {
	        this.id = id;
	        this.title = title;
	        this.doanvan = doanvan;
	        this.ngay_tao = ngay_tao;
	        this.ngay_sua = ngay_sua;
	        this.num_one_question = num_one_question;
	    }
	    
	    public String getId() { return id;}
	    public String getTitle() { return title;}
	    public String getDoanvan() { return doanvan;}
	    public String getNgaytao() { return ngay_tao;}
	    public String getNgaysua() { return ngay_sua;}
	    public String getNumonequestion() { return num_one_question;}
	 
	    public void setId(String id) {this.id = id;}
	    public void setTitle(String tudien) {this.title = tudien;}
	    public void setDoanvan(String tuloai) {this.doanvan = tuloai;}
	    public void setNgaytao(String phienam) {this.ngay_tao = phienam;}
	    public void setNgaysua(String phienam) {this.ngay_sua = phienam;}
	    public void setNumonequestion(String phienam) {this.num_one_question = phienam;}
	}
	//
	//
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
}


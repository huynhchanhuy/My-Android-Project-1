package com.example.doan_didong;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ScoreTestActivity extends ListActivity
{

	// Progress Dialog
	private ProgressDialog pDialog;
	
	
	
	String iduser;
	
	
	String score_name;
	String score_diem;
	String score_ngay;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	MyCustomAdapter adapter;  
    ArrayList<ItemContent> itemlist;  
    ListView lv;

	// url to get all products list
	private static String url_score = "http://10.0.2.2/doan_didong/score_test.php";
	private static String url_delete_score = "http://10.0.2.2/doan_didong/score_test_delete.php";
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ALL_SCORE = "all_score";
	private static final String TAG_NAME = "nametest";
	private static final String TAG_DIEM = "diemso";
	private static final String TAG_NGAY = "ngay";
	


	// products JSONArray
	JSONArray all_video = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save_score);
		
		Bundle b = getIntent().getExtras();
		iduser = b.getString("iduser");
		
		// Hashmap for ListView
		itemlist = new ArrayList<ItemContent>();

		// Loading products in Background Thread
		new LoadAllScore().execute();

		// Get listview
		lv = getListView();
//		lv.setOnItemClickListener(new OnItemClickListener() 
//		{
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				id_video = ((TextView)arg1.findViewById(R.id.id_video)).getText().toString();
//				String link = ((TextView)arg1.findViewById(R.id.video_link)).getText().toString();
//				//Toast.makeText(getBaseContext(),id_video, Toast.LENGTH_LONG).show();
//				
//				
//				List<NameValuePair> params = new ArrayList<NameValuePair>();
//				params.add(new BasicNameValuePair("id", id_video));
//				params.add(new BasicNameValuePair("view", "view"));
//				JSONObject json = jParser.makeHttpRequest(url_update_view_video, "POST", params);
//				
//				
//				
//				Intent in = new Intent(getBaseContext(),WatchAVideoActivity.class);
//				in.putExtra(TAG_ID, id_video);
//				in.putExtra("iduser", iduser);
//				in.putExtra(TAG_LINK, link);
//				startActivity(in);	
//			}
//		});
	}

	class LoadAllScore extends AsyncTask<String, String, String> 
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			pDialog = new ProgressDialog(ScoreTestActivity.this);
//			pDialog.setMessage("Loading.......");
//			pDialog.setIndeterminate(false);
//			pDialog.setCancelable(false);
//			pDialog.show();
		}

		protected String doInBackground(String... args) {
			// Building Parameters
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("iduser", iduser));
			String json_string = jParser.makeHttpRequest(url_score, "POST", params);
			Log.d("chuỗi json nhận được", "là json_string========"+json_string);
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
						json = JSONParser.getJSONObjectFromJString(json_string);
					// Checking for SUCCESS TAG
					int success = json.getInt(TAG_SUCCESS);
	
					if (success == 1) {
						// products found
						// Getting Array of Products
						all_video = json.getJSONArray(TAG_ALL_SCORE);
	
						// looping through All Products
						for (int i = 0; i < all_video.length(); i++) {
							JSONObject c = all_video.getJSONObject(i);
	
							// Storing each json item in variable
							String id1 = c.getString("id");
							String name1 = c.getString(TAG_NAME);
							String diem1 = c.getString(TAG_DIEM);
							String ngay1 = c.getString(TAG_NGAY);
							String socauhoi1 = c.getString("socauhoi");
							// creating new HashMap
							ItemContent item = new ItemContent(id1,name1,diem1,ngay1,socauhoi1);
	
							itemlist.add(item);
						}
					} else {}
						
				} catch (JSONException e) {e.printStackTrace();}
			}
			return null;
		}
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
//			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					
					
					
					ListAdapter adapter = new MyCustomAdapter
							(ScoreTestActivity.this,
							R.layout.save_score_item, itemlist);
					
					setListAdapter(adapter);
				}
			});
			

		}

	}
	
	
	//-----------------------------------------
	public class ItemContent {  
	    private String id;  
	    private String nametest;  
	    private String diemso;
	    private String ngay;
	    private String socauhoi;
	    
	  
	    public ItemContent(String id, String title, String view,String link, String t) 
	    {  
	        this.id = id;  
	        this.nametest = title;  
	        this.diemso=view;
	        this.ngay=link;
	        this.socauhoi=t;
	    }  
	  
	    public String getId() {  
	        return id;  
	    }  
	}  
	
	
	//-----------------------------------------
	public class MyCustomAdapter extends ArrayAdapter<ItemContent> 
	{
		Context mContext;  
	    ArrayList<ItemContent> items;
	    int resource;
	    
	    
		  public MyCustomAdapter(Context context, int textViewResourceId,ArrayList<ItemContent> objects) 
		  {
			  super(context, textViewResourceId, objects);
			  this.mContext = context;
			  resource = textViewResourceId;
			  items = objects; 
		  }
		 
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) 
		  {
		             
		      View v = convertView;
		 
		      if (v == null) {
		       LayoutInflater inflater = getLayoutInflater();
		       v = inflater.inflate(R.layout.save_score_item, parent, false);
		      }
		      ItemContent iContent = items.get(position);
		       
		      final TextView idscore = (TextView) v.findViewById(R.id.score_id );
		      TextView name = (TextView) v.findViewById(R.id.score_nametest );
		      TextView diem = (TextView) v.findViewById(R.id.score_diemso );
		      final TextView ngay = (TextView) v.findViewById(R.id.score_ngaytest );
		      final TextView socauhoi = (TextView) v.findViewById(R.id.score_socauhoi );
		      
		      Button delete = (Button) v.findViewById(R.id.btn_score_xoa);
		      
		      
		      idscore.setText(iContent.id);
		      name.setText("Bài test: "+iContent.nametest);
		      diem.setText("Điểm: "+iContent.diemso);
		      ngay.setText("Ngày: "+iContent.ngay);
		      socauhoi.setText("Số câu: "+iContent.socauhoi);
		      
		      delete.setOnClickListener( new View.OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {
		    		   

		    		  String id = idscore.getText().toString();
		    		  
		    		  
		    		  List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("idscore", id));
						
						
						
						String json_string = jParser.makeHttpRequest(url_delete_score, "POST", params);
						Log.d("chuỗi json nhận được", "là json_string========"+json_string);
						if(json_string.length() != 0)
						{
							JSONObject json;
							try {
									json = JSONParser.getJSONObjectFromJString(json_string);
									int success;
								// Checking for SUCCESS TAG
								success = json.getInt(TAG_SUCCESS);
	
								if (success == 1) 
								{
									Toast.makeText(getBaseContext(), "Delete complete", Toast.LENGTH_SHORT).show();
								} else 
									{}
									
							} catch (JSONException e) {e.printStackTrace();}
						}
						
					    itemlist.clear();
						new LoadAllScore().execute();
						    
		    	  }
		      });
		      return v;
		     
		  }
		 }
}
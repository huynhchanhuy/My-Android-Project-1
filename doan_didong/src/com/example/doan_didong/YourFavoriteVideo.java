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

public class YourFavoriteVideo extends ListActivity
{

	// Progress Dialog
	private ProgressDialog pDialog;
	
	
	
	String iduser;
	String giaovien;
	
	String id_video;
	String title_video;
	String views_video;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	MyCustomAdapter adapter;  
    ArrayList<ItemContent> itemlist;  
    ListView lv;

	// url to get all products list
	private static String url_favorite_video = "http://10.0.2.2/doan_didong/favorite_video.php";
	private static String url_update_view_video = "http://10.0.2.2/doan_didong/update_view_video.php";
	
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ALL_AUDIOS = "all_video";
	private static final String TAG_ID = "id";
	private static final String TAG_TITLE = "title";
	private static final String TAG_LINK = "link";
	private static final String TAG_VIEW = "views";


	// products JSONArray
	JSONArray all_video = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorite_video);
		
		Bundle b = getIntent().getExtras();
		iduser = b.getString("iduser");
		giaovien = b.getString("giaovien");
		// Hashmap for ListView
		itemlist = new ArrayList<ItemContent>();

		// Loading products in Background Thread
		new LoadAllVideo().execute();

		// Get listview
		lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				id_video = ((TextView)arg1.findViewById(R.id.id_video_favorite)).getText().toString();
				String link = ((TextView)arg1.findViewById(R.id.video_link_favorite)).getText().toString();
				//Toast.makeText(getBaseContext(),id_video, Toast.LENGTH_LONG).show();
				
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("id", id_video));
				params.add(new BasicNameValuePair("view", "view"));
				String json_string = jParser.makeHttpRequest(url_update_view_video, "POST", params);
				Log.d("chuỗi json nhận được", "là json_string========"+json_string);
				if(json_string.length() != 0)
				{
					JSONObject json;
					try {
							json = JSONParser.getJSONObjectFromJString(json_string);
					}catch(Exception e){e.printStackTrace();};
					}
				/*Intent in = new Intent(getBaseContext(),WatchAVideoActivity.class);
				in.putExtra(TAG_ID, id_video);
				in.putExtra("iduser", iduser);
				in.putExtra(TAG_LINK, link);
				startActivity(in);	*/
			}
		});
	}

	class LoadAllVideo extends AsyncTask<String, String, String> 
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(YourFavoriteVideo.this);
			pDialog.setMessage("Loading all videos. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("iduser", iduser));
			params.add(new BasicNameValuePair("loai", "video"));
			String json_string = jParser.makeHttpRequest(url_favorite_video, "POST", params);
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
						all_video = json.getJSONArray("all_audio");
	
						// looping through All Products
						for (int i = 0; i < all_video.length(); i++) {
							JSONObject c = all_video.getJSONObject(i);
	
							// Storing each json item in variable
							String id1 = c.getString(TAG_ID);
							String title1 = c.getString(TAG_TITLE);
							String view1 = c.getString(TAG_VIEW);
							String link1=c.getString(TAG_LINK);
							// creating new HashMap
							ItemContent item = new ItemContent(id1,title1,view1,link1);
	
							itemlist.add(item);
						}
					} else 
						{
						}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			return null;
		}
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					
					
					
					ListAdapter adapter = new MyCustomAdapter
							(YourFavoriteVideo.this,
							R.layout.favorite_video_item, itemlist);
					
					setListAdapter(adapter);
				}
			});
			

		}

	}
	
	
	//-----------------------------------------
	public class ItemContent {  
	    private String id;  
	    private String title;  
	    private String view;
	    private String link;
	    
	  
	    public ItemContent(String id, String title, String view,String link) 
	    {  
	        this.id = id;  
	        this.title = title;  
	        this.view=view;
	        this.link=link;
	    }  
	  
	    public String getId() {  
	        return id;  
	    }  
	  
	    public String getTitle() {  
	        return title;  
	    }    
	    public String getView() {  
	        return view;  
	    }
	    public String getLink() {  
	        return link;  
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
		       v = inflater.inflate(R.layout.favorite_video_item, parent, false);
		      }
		      ItemContent iContent = items.get(position);
		       
		      final TextView id_video = (TextView) v.findViewById(R.id.id_video_favorite );
		      TextView title_video = (TextView) v.findViewById(R.id.title_video_favorite );
		      TextView view_video = (TextView) v.findViewById(R.id.view_video_favorite );
		      final TextView link_video = (TextView) v.findViewById(R.id.video_link_favorite );
		      
		      //Button favorite = (Button) v.findViewById(R.id.btn_favorite_video);
		      Button next = (Button) v.findViewById(R.id.btn_arrow_next_favorite);
		      
		      id_video.setText(iContent.id);
		      title_video.setText(iContent.title);
		      view_video.setText(iContent.view);
		      link_video.setText(iContent.link);
		      
		      
		      next.setOnClickListener( new View.OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {
		    		   

		    		  String id = id_video.getText().toString();
		    		  String link = link_video.getText().toString();
		    		  
		    		  List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("id", id));
						params.add(new BasicNameValuePair("view", "view"));
						
						
						String json_string = jParser.makeHttpRequest(url_update_view_video, "POST", params);
						Log.d("chuỗi json nhận được", "là json_string========"+json_string);
						if(json_string.length() != 0)
						{
							JSONObject json;
							try {
									json = JSONParser.getJSONObjectFromJString(json_string);
							}catch(Exception e){e.printStackTrace(); };
			    		  //Toast.makeText(getBaseContext(), id, Toast.LENGTH_LONG).show();
			    		  /*Intent in = new Intent(getBaseContext(),WatchAVideoActivity.class);
							in.putExtra(TAG_ID, id);
							in.putExtra("iduser", iduser);
							in.putExtra("link", link);
							//startActivityForResult(in, 100);
							startActivity(in);	*/    
			    	  }
		    	  }
		      });
		      return v;
		     
		  }
		 }
}

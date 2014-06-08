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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class YourFavoriteAudio extends ListActivity
{

	// Progress Dialog
	private ProgressDialog pDialog;
	
	Button btn_listen_item;
	Button btn_view_item;
	Button btn_favorite_item;
	
	String id_audi;
	String iduser;
	String giaovien;
	
	String title1;
	String text1;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	MyCustomAdapter adapter;  
    ArrayList<ItemContent> itemlist;  
    ListView lv;

	// url to get all products list
	private static String url_all_audios = "http://10.0.2.2/doan_didong/favorite_audio.php";
	//private static String url_add_favorite = "http://10.0.2.2/doan_didong/add_video_favorite.php";
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ALL_AUDIOS = "all_audio";
	private static final String TAG_ID = "id";
	private static final String TAG_TITLE = "title";
//	private static final String TAG_LINK = "link";
//	private static final String TAG_TEXT = "text";

	// products JSONArray
	JSONArray all_audios = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorite_audio);
		
		Bundle b = getIntent().getExtras();
		iduser = b.getString("iduser");
		
		// Hashmap for ListView
		itemlist = new ArrayList<ItemContent>();

		// Loading products in Background Thread
		new LoadAllAudios().execute();

		// Get listview
		lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				id_audi = ((TextView)arg1.findViewById(R.id.id_audio_favorite)).getText().toString();
				
				//Toast.makeText(getBaseContext(),id_audi, Toast.LENGTH_LONG).show();
				
//				Intent in = new Intent(getBaseContext(),ListenAAudioActivity.class);
//				in.putExtra(TAG_ID, id_audi);
//				in.putExtra("iduser", iduser);
//				in.putExtra("loai", "audio");
				//startActivityForResult(in, 100);
//				startActivity(in);	
			}
		});
	}

	class LoadAllAudios extends AsyncTask<String, String, String> 
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			pDialog = new ProgressDialog(YourFavoriteAudio.this);
//			pDialog.setMessage("Loading all audios. Please wait...");
//			pDialog.setIndeterminate(false);
//			pDialog.setCancelable(false);
//			pDialog.show();
		}

		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("iduser", iduser));
			params.add(new BasicNameValuePair("loai", "audio"));
			String json_string = jParser.makeHttpRequest(url_all_audios, "POST", params);
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
							all_audios = json.getJSONArray(TAG_ALL_AUDIOS);
		
							// looping through All Products
							for (int i = 0; i < all_audios.length(); i++) {
								JSONObject c = all_audios.getJSONObject(i);
		
								// Storing each json item in variable
								String id1 = c.getString(TAG_ID);
								String title1 = c.getString(TAG_TITLE);
								
								// creating new HashMap
								ItemContent item = new ItemContent(id1,title1);
		
								// adding each child node to HashMap key => value
		//						item.put(TAG_ID, id1);
		//						item.put(TAG_TITLE,title1);
		
								// adding HashList to ArrayList
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
//			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					
					
					
					ListAdapter adapter = new MyCustomAdapter
							(YourFavoriteAudio.this,
							R.layout.favorite_audio_item, itemlist);
					
					setListAdapter(adapter);
				}
			});
			

		}

	}
	
	
	//-----------------------------------------
	public class ItemContent {  
	    private String id;  
	    private String title;  
	    
	  
	    public ItemContent(String id, String title) 
	    {  
	        this.id = id;  
	        this.title = title;  
	       
	    }  
	  
	    public String getId() {  
	        return id;  
	    }  
	  
	    public String getTitle() {  
	        return title;  
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
		       v = inflater.inflate(R.layout.favorite_audio_item, parent, false);
		      }
		      ItemContent iContent = items.get(position);
		       
		      final TextView id_audio = (TextView) v.findViewById(R.id.id_audio_favorite );
		      TextView title_audio = (TextView) v.findViewById(R.id.title_audio_favorite );
		      Button listen = (Button) v.findViewById(R.id.btn_listen_item_favorite);
		      Button view = (Button) v.findViewById(R.id.btn_view_item_favorite);
		     
		      
		      
		      id_audio.setText(iContent.id);
		      title_audio.setText(iContent.title);
		      
		      
		      
		      listen.setOnClickListener( new View.OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {
		    		  String id1 = id_audio.getText().toString();
//		    		  Intent in = new Intent(getBaseContext(),ListenAAudioActivity.class);
//						in.putExtra(TAG_ID, id1);
//						in.putExtra("iduser", iduser);
//						in.putExtra("giaovien", giaovien);
//						//startActivityForResult(in, 100);
//						startActivity(in);	    
		    	  }
		      });
		      view.setOnClickListener( new View.OnClickListener() 
		      {
		     
		    	  @Override
		    	  public void onClick(View v) 
		    	  {
		    		  /*String idaudio = id_audio.getText().toString();
		    		  Intent in = new Intent(getBaseContext(),ViewAAudioActivity.class);
						in.putExtra("idaudio", idaudio);
						//startActivityForResult(in, 100);
						startActivity(in);*/	    
		    	  }
		      });
  
//		    	  }
//		      });
		      return v;
		     
		  }
		 }
}

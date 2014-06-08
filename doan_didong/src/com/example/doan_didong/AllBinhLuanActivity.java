package com.example.doan_didong;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
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
import android.widget.AdapterView.OnItemClickListener;

public class AllBinhLuanActivity extends ListActivity
{

	// Progress Dialog
	private ProgressDialog pDialog;
	
	Button btn_binh_luan_add;
	
	String iduser;
	String giaovien;
	
	String binhluan_id;
	String binhluan_name;
	String binhluan_text;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	MyCustomAdapter adapter;  
    ArrayList<ItemContent> itemlist;  
    ListView lv;

	// url to get all products list
	private static String url_all_binh_luan = "http://10.0.2.2/doan_didong/binh_luan_get_all.php";
	private static String url_add_binh_luan = "http://10.0.2.2/doan_didong/binh_luan_add.php";
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ALL_STUDY = "all_study";
	private static final String TAG_ID = "id";
	private static final String TAG_TITLE = "name";
	private static final String TAG_TEXT = "text";
	


	// products JSONArray
	JSONArray all_study = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.binhluan);

		itemlist = new ArrayList<ItemContent>();
		btn_binh_luan_add=(Button)findViewById(R.id.btn_add_binh_luan);
		btn_binh_luan_add.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(getApplicationContext(), AddBinhLuanActivity.class);
				startActivity(i);
			}
		});
		
		new LoadAllStudy().execute();

		// Get listview
		lv = getListView();
		
	}

	class LoadAllStudy extends AsyncTask<String, String, String> 
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AllBinhLuanActivity.this);
			pDialog.setMessage("Loading .....");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			String json_string = jParser.makeHttpRequest(url_all_binh_luan, "POST", params);


			Log.d("chuỗi json nhận được", "là json_string========"+json_string);
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						// products found
						// Getting Array of Products
						all_study = json.getJSONArray(TAG_ALL_STUDY);
	
						// looping through All Products
						for (int i = 0; i < all_study.length(); i++) 
						{
							JSONObject c = all_study.getJSONObject(i);
	
							// Storing each json item in variable
							String id1 = c.getString("id");
							String title1 = c.getString("name");
							String view1 = c.getString("text");
							
							// creating new HashMap
							ItemContent item = new ItemContent(id1,title1,view1);
	
							itemlist.add(item);
						}
						} else 
							{
							}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {};
			return null;
		}
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					
					
					
					ListAdapter adapter = new MyCustomAdapter
							(AllBinhLuanActivity.this,
							R.layout.binhluan_item, itemlist);
					
					setListAdapter(adapter);
				}
			});
			

		}

	}
	
	
	//-----------------------------------------
	public class ItemContent {  
	    private String id;  
	    private String name;  
	    private String text;
	    
	    
	  
	    public ItemContent(String id, String title, String text) 
	    {  
	        this.id = id;  
	        this.name = title;  
	        this.text=text;
	        
	    }  
	  
	    public String getId() {  
	        return id;  
	    }  
	  
	    public String getTitle() {  
	        return name;  
	    }    
	    public String getText() {  
	        return text;
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
		       v = inflater.inflate(R.layout.binhluan_item, parent, false);
		      }
		      ItemContent iContent = items.get(position);
		       
		      final TextView id    = (TextView) v.findViewById(R.id.binhluan_id );
		      final TextView name  = (TextView) v.findViewById(R.id.binhluan_name );
		      final TextView text  = (TextView) v.findViewById(R.id.binhluan_text );
		      
		      
		      
		      id.setText(iContent.id);
		      name.setText(iContent.name);
		      text.setText(iContent.text);

		      return v;
		     
		  }
		 }
}
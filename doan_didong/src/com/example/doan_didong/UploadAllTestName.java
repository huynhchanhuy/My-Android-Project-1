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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class UploadAllTestName extends ListActivity
{

	// Progress Dialog
	private ProgressDialog pDialog;
	
	
	String iduser;
	String giaovien;
	
	String test_id;
	String test_name;
	String test_title;
	String test_text;
	

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	MyCustomAdapter        adapter;  
    ArrayList<ItemContent> itemlist;  
    ListView               lv;

	// url to get all products list
	private static String url_all_test =      "http://10.0.2.2/doan_didong/upload_all_name_test.php";
	
	
	// JSON Node names
	private static final String TAG_SUCCESS  = "success";
	private static final String TAG_ALL_TEST = "all_test";
	private static final String TAG_ID       = "id";
	private static final String TAG_NAME     = "name";
	private static final String TAG_TITLE    = "title";
	private static final String TAG_TEXT     = "text";


	// products JSONArray
	JSONArray all_test = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_all_test_name);
		
		Bundle b = getIntent().getExtras();
		iduser = b.getString("iduser");
		giaovien = b.getString("giaovien");
		// Hashmap for ListView
		itemlist = new ArrayList<ItemContent>();

		// Loading products in Background Thread
		new LoadAllTest().execute();

		// Get listview
		lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{
				test_id = ((TextView)arg1.findViewById(R.id.upload_all_test_name_id)).getText().toString();
				test_name = ((TextView)arg1.findViewById(R.id.upload_all_test_name_name)).getText().toString();
				
				//Toast.makeText(getBaseContext(), test_name+"\n"+test_id, Toast.LENGTH_LONG).show();
				
				Intent in = new Intent(getApplicationContext(),UploadATest.class);
				in.putExtra("nametest", test_name);
				in.putExtra("iduser", iduser);
				
				startActivity(in);
				
			}
		});
	}

	class LoadAllTest extends AsyncTask<String, String, String> 
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(UploadAllTestName.this);
			pDialog.setMessage("Loading......");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			String json_string = jParser.makeHttpRequest(url_all_test, "POST", params);
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
						all_test = json.getJSONArray(TAG_ALL_TEST);
	
						// looping through All Products
						for (int i = 0; i < all_test.length(); i++) {
							JSONObject c = all_test.getJSONObject(i);
	
							// Storing each json item in variable
							String id1 = c.getString("id");
							String name1 = c.getString("name");
							String title1 = c.getString("title");
							String text1=c.getString("text");
							// creating new HashMap
							ItemContent item = new ItemContent(id1,name1,title1,text1);
	
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
							(UploadAllTestName.this,
							R.layout.upload_all_test_name_item, itemlist);
					setListAdapter(adapter);
				}
			});
			

		}

	}
	
	
	//-----------------------------------------
	public class ItemContent {  
	    private String id;  
	    private String name;  
	    private String title;
	    private String text;
	    
	  
	    public ItemContent(String id, String title, String view,String link) 
	    {  
	        this.id = id;  
	        this.name = title;  
	        this.title=view;
	        this.text=link;
	    }  
	  
	    public String getId() {  
	        return id;  
	    }  
	  
	    public String getTitle() {  
	        return title;  
	    }    
	    public String getName() {  
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
		       v = inflater.inflate(R.layout.upload_all_test_name_item, parent, false);
		      }
		      ItemContent iContent = items.get(position);
		       
		      final TextView id = (TextView)   v.findViewById(R.id.upload_all_test_name_id );
		      final TextView name = (TextView) v.findViewById(R.id.upload_all_test_name_name );
		      TextView title = (TextView)      v.findViewById(R.id.upload_all_test_name_title );
		      final TextView text = (TextView) v.findViewById(R.id.upload_all_test_name_text );

		      
		      id.setText(iContent.id);
		      name.setText(iContent.name);
		      title.setText(iContent.title);
		      text.setText(iContent.text);

		      return v;
		     
		  }
		 }
}

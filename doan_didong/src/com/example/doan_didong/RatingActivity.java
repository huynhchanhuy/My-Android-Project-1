package com.example.doan_didong;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class RatingActivity extends Activity
{

	private RatingBar ratingbar;
	
	Button btn_danh_gia;
	
//	TextView luotbinhchon[];
	TextView luotbinhchon_1;
	TextView luotbinhchon_2;
	TextView luotbinhchon_3;
	TextView luotbinhchon_4;
	TextView luotbinhchon_5;
	
//	TextView phantram[];
	TextView phantram_1;
	TextView phantram_2;
	TextView phantram_3;
	TextView phantram_4;
	TextView phantram_5;
	
	JSONParser jParser = new JSONParser();
	JSONArray all_study = null;
	String traloi="";
	
	private static String url_danh_gia = "http://10.0.2.2/doan_didong/danh_gia.php";
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rating);
		
		ratingbar=(RatingBar)findViewById(R.id.ratingbar_submit);
	    btn_danh_gia=(Button)findViewById(R.id.btn_submit);
	    luotbinhchon_1=(TextView)findViewById(R.id.luotbinhchon_1);
	    luotbinhchon_2=(TextView)findViewById(R.id.luotbinhchon_2);
	    luotbinhchon_3=(TextView)findViewById(R.id.luotbinhchon_3);
	    luotbinhchon_4=(TextView)findViewById(R.id.luotbinhchon_4);
	    luotbinhchon_5=(TextView)findViewById(R.id.luotbinhchon_5);
	    
	    phantram_1=(TextView)findViewById(R.id.phantram_1);
	    phantram_2=(TextView)findViewById(R.id.phantram_2);
	    phantram_3=(TextView)findViewById(R.id.phantram_3);
	    phantram_4=(TextView)findViewById(R.id.phantram_4);
	    phantram_5=(TextView)findViewById(R.id.phantram_5);
	    
	    
	    ratingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() 
	    {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,boolean fromUser) 
			{
				int ii = 0;
				String tem=String.valueOf(rating);
				if("0.0".equals(tem)) ii=0;
				if("1.0".equals(tem)) ii=1;
				if("2.0".equals(tem)) ii=2;
				if("3.0".equals(tem)) ii=3;
				if("4.0".equals(tem)) ii=4;
				if("5.0".equals(tem)) ii=5;
				
				switch (ii) 
				{
				case 0:
					traloi="";
					break;
				case 1:
					traloi="motsao";
					break;
				case 2:
					traloi="haisao";
					break;
				case 3:
					traloi="basao";
					break;
				case 4:
					traloi="bonsao";
					break;
				case 5:
					traloi="namsao";
					break;
				}
				
				//Toast.makeText(getBaseContext(), traloi, Toast.LENGTH_SHORT).show();
			}
		});
	    btn_danh_gia.setOnClickListener(new View.OnClickListener()
	    {
			
			@Override
			public void onClick(View v) 
			{
				if(!"".equals(traloi))
				{
					List<NameValuePair> params = new ArrayList<NameValuePair>();
	
					params.add(new BasicNameValuePair("traloi",traloi));
	//				params.add(new BasicNameValuePair("iduser", iduser));
	//				params.add(new BasicNameValuePair("idaudio", id_audio));
	//				params.add(new BasicNameValuePair("update", "update"));
					
					String json_string = jParser.makeHttpRequest(url_danh_gia, "POST", params);
					Log.d("chuỗi json nhận được", "là json_string========"+json_string);
					if(json_string.length() != 0)
					{
						JSONObject json;
						try {
								json = JSONParser.getJSONObjectFromJString(json_string);
						}catch(Exception e){e.printStackTrace();};
					}
					Toast.makeText(getBaseContext(), "success", Toast.LENGTH_LONG).show();
				}
				else Toast.makeText(getBaseContext(), "Bạn chưa chọn", Toast.LENGTH_LONG).show();
				
			}
		});
	    
	    
	    
	    
	    
	    
	    
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
		// getting JSON string from URL
		String json_string = jParser.makeHttpRequest(url_danh_gia, "POST", params);
		Log.d("chuỗi json nhận được", "là json_string========"+json_string);
		if(json_string.length() != 0)
		{
			JSONObject json;
			try {
					json = JSONParser.getJSONObjectFromJString(json_string);
				// Checking for SUCCESS TAG
				int success = json.getInt("success");
	
				if (success == 1) {
					// products found
					// Getting Array of Products
					all_study = json.getJSONArray("all_study");
	
					// looping through All Products
					for (int i = 0; i < all_study.length(); i++) 
					{
						JSONObject c = all_study.getJSONObject(i);
	
						
						if(i==0)
						{
							luotbinhchon_1.setText( "  "+  c.getString("soluong") +" lượt   ");
							phantram_1.setText(c.getString("phantram")+"%");
						}
						
						if(i==1)
						{
							luotbinhchon_2.setText( "  "+  c.getString("soluong") +" lượt    ");
							phantram_2.setText(c.getString("phantram")+"%");
						}
						if(i==2)
						{
							luotbinhchon_3.setText( "  "+  c.getString("soluong") +" lượt    ");
							phantram_3.setText(c.getString("phantram")+"%");
						}
						if(i==3)
						{
							luotbinhchon_4.setText( "  "+  c.getString("soluong") +" lượt    ");
							phantram_4.setText(c.getString("phantram")+"%");
						}
						if(i==4)
						{
							luotbinhchon_5.setText( "  "+  c.getString("soluong") +" lượt    ");
							phantram_5.setText(c.getString("phantram")+"%");
						}
	//					String title1 = c.getString(TAG_TITLE);
	//					String view1 = c.getString(TAG_TEXT);
						
						
					}
				} else 
					{
					}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		
	    
	}
}

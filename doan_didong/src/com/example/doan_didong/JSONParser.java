package com.example.doan_didong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

	static InputStream is = null;
    static JSONObject jObj = null;
    public static String json_string = "";
	

	// constructor
	public JSONParser() 
	{}
	public String makeHttpRequest(String url, String method, List<NameValuePair> params) {
		try {
			
			// check for request method
			if(method == "POST")
			{
				HttpPost httpPost = new HttpPost(url);
				httpPost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
				//set timeout cho kết nối
				HttpParams httpParameters = new BasicHttpParams();
				
				int timeoutConnection = 5000;
				HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
				int timeoutSocket = 10000;
				HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
				
				DefaultHttpClient httpClient = new DefaultHttpClient();
				httpClient.setParams(httpParameters);
				
				
				httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				
				if(httpEntity==null)
				{
					json_string="";
					 Log.d("---- get json message -----", "+++++++++++ JSON return :null +++++++++++");
				}
				else{
					json_string = EntityUtils.toString(httpEntity);
					Log.d("---- get json message -----", "++++++++++     JSON return :success ++++++++");
				}
			}
		}
		catch (ConnectTimeoutException e)
	    {
			json_string="";
	        Log.e("+++++++ TIMEOUT_CONNECTION ++++++++ ","Network timeout reached!"); 
	    }catch(SocketTimeoutException ste){
	    	json_string="";	 
	    	Log.e("----Time out ","----timeout for waiting for data"); 
	    }
		catch (UnknownHostException e) {
			json_string="";	
			Log.d("----msg", " ----Khong tim thay host: "+ json_string);
		}
		catch (UnsupportedEncodingException e) {
			json_string="";	
			Log.d("---msg", " ---Can't connect to server");
			
		} catch (MalformedURLException e) {
			json_string="";	
			Log.d("---msg", " ---Can't connect to server");
				
		} catch (IOException e) {
			json_string="";	
			Log.d("----msg", "---- Can't connect to server");
		}
	return json_string;
		
		// try parse the string to a JSON object
	
		/*try {
			if(json.length() != 0)	jObj = new JSONObject(json);
			else jObj = null;
		} catch (JSONException e) 
		{
			Log.e("JSON Parser", "Lỗi parser data sang JSON " + e.toString());
		}

	return jObj;*/
		
			
			
			/*try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(
	                    is, "iso-8859-1"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            is.close();
	            json = sb.toString();
	        } catch (Exception e) {
	            Log.e("Buffer Error", "Error converting result " + e.toString());
	        }

	        // try parse the string to a JSON object
	        try {
	            jObj = new JSONObject(json);
	        } catch (JSONException e) {
	            Log.e("JSON Parser", "lỗi parse json " + e.toString());
	        }

	        // return JSON String
	        return jObj;*/
	
	}
	
	public static JSONObject getJSONObjectFromJString(String json_string) throws JSONException{
		
		JSONObject j;
		
		
		j=new JSONObject(json_string);
		return j;
		
	}
}


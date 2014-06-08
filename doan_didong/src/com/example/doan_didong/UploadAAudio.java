package com.example.doan_didong;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class UploadAAudio extends Activity 
{

	static HttpURLConnection connection ;
	static DataOutputStream outputStream ;	
	static FileInputStream fileInputStream;
	static URL url;
	static InputStream is;
	String issuccess;
	
	String giaovien;
	String iduser;
	
	String title;
	String link;
	String text;
	String question;
	String answer;
	
	EditText ed_title;
	EditText ed_link;
	EditText ed_text;
	EditText ed_question;
	EditText ed_answer;
	
	Button btn_upload;
	
	String url_folder_from ="sdcard/doan_didong/";
	//String url_folder_to="http://10.0.2.2/doan_didong/audio";
	String urlString="http://10.0.2.2/doan_didong/upload_audio.php";
	String url_upload_audio="http://10.0.2.2/doan_didong/upload_audio1.php";
	
	private String Tag = "UPLOADER";
    
    HttpURLConnection conn;
    
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_a_audio);
        
        Bundle b = getIntent().getExtras();
		giaovien=b.getString("giaovien");
		iduser=b.getString("iduser");
		link=b.getString("link");
		
		ed_title=(EditText)findViewById(R.id.ed_title_audio1);
		ed_link=(EditText)findViewById(R.id.ed_link_audio1);
		ed_text=(EditText)findViewById(R.id.ed_text_audio1);
		ed_question=(EditText)findViewById(R.id.ed_question_audio1);
		ed_answer=(EditText)findViewById(R.id.ed_answer_audio1);
		btn_upload=(Button)findViewById(R.id.btn_upload_a_audio);
		
		ed_link.setText(link);
		ed_link.setEnabled(false);
		ed_link.setClickable(false);
		
		btn_upload.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
//				if(!"".equals(ed_title.getText().toString()) && !"".equals(ed_text.getText().toString()) )
//				{
//				Toast.makeText(getBaseContext(), ed_title.getText().toString()+"\n"
//						+ed_text.getText().toString()+"\n"
//						+ed_link.getText().toString(), Toast.LENGTH_SHORT).show();
//				}else Toast.makeText(getBaseContext(), "Không được để trống", Toast.LENGTH_SHORT).show();
				if(!"".equals(ed_title.getText().toString()) && !"".equals(ed_text.getText().toString())
						&& !"".equals(ed_link.getText().toString()))
				{
					issuccess=uploadaudio(url_folder_from+link, urlString);
					if("success".equals(issuccess))
					{
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("title", ed_title.getText().toString()));
					params.add(new BasicNameValuePair("link", link));
					params.add(new BasicNameValuePair("text", ed_text.getText().toString()));
					params.add(new BasicNameValuePair("quickask", ed_question.getText().toString()));
					params.add(new BasicNameValuePair("answer", ed_answer.getText().toString()));

					// getting JSON Object
					// Note that create product url accepts POST method
					String json_string = jsonParser.makeHttpRequest(url_upload_audio,"POST", params);
					Log.d("chuỗi json nhận được", "là json_string========"+json_string);
					if(json_string.length() != 0)
					{
						JSONObject json;
					try {
							json = JSONParser.getJSONObjectFromJString(json_string);
							int success = json.getInt(TAG_SUCCESS);
							
							if (success == 1) 
							{
								
								//Toast.makeText(getBaseContext(), issuccess, Toast.LENGTH_SHORT).show();
								
							}
						} catch (JSONException e) {e.printStackTrace();}
					
					}
					Toast.makeText(getBaseContext(), issuccess, Toast.LENGTH_SHORT).show();
					
					
					}else
						Toast.makeText(getBaseContext(), "Không được để trống", Toast.LENGTH_SHORT).show();

				}
			}
		});
		 
    }
    public static String uploadaudio(String filename, String targetUrl) 
    {
	    String response = "error";
	    connection = null;
	    outputStream = null;
 
	    String pathToOurFile = filename;
	    String urlServer = targetUrl;
	    String lineEnd = "\r\n";
	    String twoHyphens = "--";
	    String boundary = "*****";
 
	    int bytesRead, bytesAvailable, bufferSize;
	    byte[] buffer;
	    int maxBufferSize = 2*1024*1024;
	    try {
	        fileInputStream = new FileInputStream(new File(pathToOurFile));
 
	        url = new URL(urlServer);
	        connection = (HttpURLConnection) url.openConnection();
 
	        // Allow Inputs & Outputs
	        connection.setDoInput(true);
	        connection.setDoOutput(true);
	        connection.setUseCaches(false);
	        connection.setChunkedStreamingMode(1024);
	        // Enable POST method
	        connection.setRequestMethod("POST");
 
	        connection.setRequestProperty("Connection", "Keep-Alive");
	        connection.setRequestProperty("Content-Type",
	                "multipart/form-data; boundary=" + boundary);
 
	        outputStream = new DataOutputStream(connection.getOutputStream());
	        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
 
	        String token = "anyvalue";
	        outputStream.writeBytes("Content-Disposition: form-data; name=\"Token\"" + lineEnd);
	        outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
	        outputStream.writeBytes("Content-Length: " + token.length() + lineEnd);
	        outputStream.writeBytes(lineEnd);
	        outputStream.writeBytes(token + lineEnd);
	        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
 
	        String taskId = "anyvalue";
	        outputStream.writeBytes("Content-Disposition: form-data; name=\"TaskID\"" + lineEnd);
	        outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
	        outputStream.writeBytes("Content-Length: " + taskId.length() + lineEnd);
	        outputStream.writeBytes(lineEnd);
	        outputStream.writeBytes(taskId + lineEnd);
	        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
 
	        String connstr = null;
	        connstr = "Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
	                + pathToOurFile + "\"" + lineEnd;
 
	        outputStream.writeBytes(connstr);
	        outputStream.writeBytes(lineEnd);
 
	      	bytesAvailable = fileInputStream.available();
	        bufferSize = Math.min(bytesAvailable, maxBufferSize);
	        buffer = new byte[bufferSize];
 
	        // Read file
	        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	        System.out.println("Image length " + bytesAvailable + "");
	        try {
	            while (bytesRead > 0) 
	            {
	                try {
	                    outputStream.write(buffer, 0, bufferSize);
	                } catch (OutOfMemoryError e) {
	                    e.printStackTrace();
	                    response = "outofmemoryerror";
	                    return response;
	                }
	                bytesAvailable = fileInputStream.available();
	                bufferSize = Math.min(bytesAvailable, maxBufferSize);
	                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            response = "error";
	            return response;
	        }
	        outputStream.writeBytes(lineEnd);
	        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
 
	        // Responses from the server (code and message)
	        int serverResponseCode = connection.getResponseCode();
	        String serverResponseMessage = connection.getResponseMessage();
	        System.out.println("Server Response Code " + " " + serverResponseCode);
	        System.out.println("Server Response Message "+ serverResponseMessage);
 
	        if (serverResponseCode == 200) {
	            response = "success";
	        }else
	        {
	        	response = "false";
	        }
 
	        fileInputStream.close();
	        outputStream.flush();
 
	        connection.getInputStream();
	        //for android InputStream is = connection.getInputStream();
	        is = connection.getInputStream();
 
			int ch;
			StringBuffer b = new StringBuffer();
			while( ( ch = is.read() ) != -1 )
			{
				b.append( (char)ch );
			}
 
			String responseString = b.toString();
			System.out.println("response string is" + responseString); //Here is the actual output
			
			is.close();
			outputStream.close();
			connection.disconnect();
	        //outputStream = null;
	        
 
	    } catch (Exception ex) {
	        // Exception handling
	        response = "false";
	        System.out.println("Send file Exception" + ex.getMessage() + "");
	        ex.printStackTrace();
	    }
	    return response;
	}
}
     


package com.example.doan_didong;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.doan_didong.GetAllTest1.LoadAllTest1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChooseVideoInSdcard extends Activity {

	String giaovien;
	String iduser;
	String link;
	
	String url_folder_from ="/sdcard/doan_didong/";
	//String url_folder_to="http://10.0.2.2/doan_didong/audio";
	String urlString="http://10.0.2.2/doan_didong/upload_video.php";
	String url_upload_audio="http://10.0.2.2/doan_didong/upload_video1.php";
	
	private String Tag = "UPLOADER";
    
    HttpURLConnection conn;
    
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_video_in_sdcard);
        
        Bundle b = getIntent().getExtras();
		giaovien=b.getString("giaovien");
		iduser=b.getString("iduser");
        link=b.getString("link");
        ListView lv;
        ArrayList<String> FilesInFolder = GetFiles("/sdcard/doan_didong");
        lv = (ListView)findViewById(R.id.listview);

        lv.setAdapter(new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, FilesInFolder));

        lv.setOnItemClickListener(new OnItemClickListener() 
        {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String link=parent.getItemAtPosition(position).toString();
				//Toast.makeText(getBaseContext(),link, Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getApplicationContext(), UploadAVideo.class);
				i.putExtra("giaovien", giaovien);
				i.putExtra("iduser", iduser);
				i.putExtra("link", link);
				startActivity(i);
			}
		});
        
    }
    public ArrayList<String> GetFiles(String DirectoryPath) {
        ArrayList<String> MyFiles = new ArrayList<String>();
        File f = new File(DirectoryPath);

        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0)
            return null;
        else {
            for (int i=0; i<files.length; i++) 
            {
                if(files[i].getName().indexOf(".mp4")!=-1)
            	MyFiles.add(files[i].getName());
            }
        }

        return MyFiles;
    }
    public void uploadaudio()
    {
 	   String exsistingFileName = "/sdcard/doan_didong/"+link;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        
        try {
            // ------------------ CLIENT REQUEST

            Log.e(Tag, "Inside second Method");

            FileInputStream fileInputStream = new FileInputStream(new File(
                    exsistingFileName));

            // open a URL connection to the Servlet

            URL url = new URL(urlString);

            // Open a HTTP connection to the URL

            conn = (HttpURLConnection) url.openConnection();

            // Allow Inputs
            conn.setDoInput(true);

            // Allow Outputs
            conn.setDoOutput(true);

            // Don't use a cached copy.
            conn.setUseCaches(false);

            // Use a post method.
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");

            conn.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: post-data; name=uploadedfile;filename="
                            + exsistingFileName + "" + lineEnd);
            dos.writeBytes(lineEnd);

            Log.e(Tag, "Headers are written");
            
            
            
         // create a buffer of maximum size

            int bytesAvailable = fileInputStream.available();
            int maxBufferSize = 1000;
            // int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bytesAvailable];

            // read file and write it into form...

            int bytesRead = fileInputStream.read(buffer, 0, bytesAvailable);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bytesAvailable);
                bytesAvailable = fileInputStream.available();
                bytesAvailable = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bytesAvailable);
            }
            
            
         // send multipart form data necesssary after file data...

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            Log.e(Tag, "File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {
            Log.e(Tag, "error: " + ex.getMessage(), ex);
        }

        catch (IOException ioe) {
            Log.e(Tag, "error: " + ioe.getMessage(), ioe);
        }

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) 
            {
                Log.e("Dialoge Box", "Message: " + line);
            }
            rd.close();

        } catch (IOException ioex) 
        {
            Log.e("MediaPlayer", "error: " + ioex.getMessage(), ioex);
        }
    }

   
}

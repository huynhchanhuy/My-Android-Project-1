package com.example.doan_didong;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.doan_didong.ReduceImage.ScalingLogic;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Tab2_ThayDoiHinhDaiDien extends Activity 
{
	public static HttpURLConnection connection = null ;
	public static DataOutputStream outputStream = null;	
	public static FileInputStream fileInputStream = null;
	public static URL url = null;
	public static InputStream is=null;
	private int serverResponseCode = 0;
	private ProgressDialog dialog = null;
	
	
	int SELECT_IMAGE = 12345;
	String selectedImagePath="";
	String uploadcomplete="";
	
	// Progress Dialog
	private FunctionCheck instantFunctionCheck;
	Bundle b;
	public int success = 0;

	JSONParser jsonParser = new JSONParser();
	
	TextView tab2_hinhdaidien_filepath;
	Button   tab2_hinhdaidien_choosefile;
	Button   tab2_hinhdaidien_upload;
	
	private static final int DIALOG_CHANGE_REQUIRE = 1; //không được để trống

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab2_thaydoi_hinhdaidien);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//set không cho xoay màn hình
		instantFunctionCheck = new FunctionCheck(Tab2_ThayDoiHinhDaiDien.this);//tạo đối tượng kiểm tra kết nối internet
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		tab2_hinhdaidien_filepath = (TextView) findViewById(R.id.tab2_hinhdaidien_filepath);
		tab2_hinhdaidien_choosefile = (Button) findViewById(R.id.tab2_hinhdaidien_choosefile);
		tab2_hinhdaidien_upload     = (Button) findViewById(R.id.tab2_hinhdaidien_upload);
		
		tab2_hinhdaidien_choosefile.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				if(uploadcomplete.equals("success"))
				{
					Toast.makeText(Tab2_ThayDoiHinhDaiDien.this, "File đã được upload rồi", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
					//startActivity(Intent.createChooser(intent, "Select Picture"));
				}
			}
		});
		
		tab2_hinhdaidien_upload.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				if(uploadcomplete.equals("success"))
				{
					Toast.makeText(Tab2_ThayDoiHinhDaiDien.this, "File đã được upload rồi", Toast.LENGTH_SHORT).show();
				}
				else
				if(instantFunctionCheck.checkInternetConnection())
				{
					if(success==1 && !"".equals(selectedImagePath))
					{
						final String urlServer = "http://studyenglish.zz.mu/doan_didong/totnghiep_upload_hinhdaidien.php";
						dialog = ProgressDialog.show(Tab2_ThayDoiHinhDaiDien.this, "", "Uploading file...", true);
			             new Thread(new Runnable() {
			                 public void run() 
			                 {
			                                      
			                	 uploadcomplete=uploadFile(selectedImagePath, urlServer);
			                                               
			                 }
			               }).start();
					}
					else
					{
						Toast.makeText(Tab2_ThayDoiHinhDaiDien.this, "Chọn file ảnh để upload", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					Toast.makeText(Tab2_ThayDoiHinhDaiDien.this, "Kiểm tra kết nối internet", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	//
	//
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                if(!"".equals(selectedImagePath))
                {
                	tab2_hinhdaidien_filepath.setText(selectedImagePath);
                	success=1;
                }
                else
                {
                	Toast.makeText(Tab2_ThayDoiHinhDaiDien.this, "Có lỗi. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
                Log.v("IMAGE PATH====>>>> ",selectedImagePath);
            }
        }
    }
	//
    public String getPath(Uri uri) 
    {
    	String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String re_path= cursor.getString(column_index);
        return decodeFile(re_path,200,200);
    }
    //
    private String decodeFile(String path,int DESIREDWIDTH, int DESIREDHEIGHT) 
    {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ReduceImage.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) 
            {
                // Part 2: Scale image
                scaledBitmap = ReduceImage.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingLogic.FIT);
                Log.e("====>>>>check width and height"," dài >DESIREDWIDTH và rộng >DESIREDHEIGHT");
            } else {
            	scaledBitmap = ReduceImage.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingLogic.FIT);
            	//unscaledBitmap.recycle();
                Log.e("====>>>>check width and height"," dài <= 50 và rộng <=50 return path");
                //return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/hocngoaingu/TMMFOLDER");
            if (!mFolder.exists()) 
            {
                mFolder.mkdir();
                Log.e("Temp folder"+mFolder.getAbsolutePath(),mFolder.getAbsolutePath().toString());
            }

            String s = MainActivity.username+".jpg";

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Log.e("====>>>>Tạo file image tmp thành công",f.getAbsolutePath().toString());
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) 
            {
                e.printStackTrace();
                return "";
            } catch (Exception e) 
            {
                e.printStackTrace();
                return "";
            }

            scaledBitmap.recycle();
        } catch (Throwable e) 
        {
        	e.printStackTrace();
            return "";
        }

        if (strMyImagePath == null) 
        {
            return path;
        }
        return strMyImagePath;
    }
	
	
	
	
	@Override
	public void onBackPressed() 
	{
		//super.onBackPressed();
		if(success == 1 && !"".equals(selectedImagePath) && uploadcomplete.equals("success"))
		{
			Log.e("====>>>backpress send result","====>>>backpress send result:"+selectedImagePath);
			Intent data = new Intent();
			
			data.putExtra("link_hinhdaidien",selectedImagePath);
           
            setResult(MainActivity.CHANGE_IMAGE, data);
            finish();
		}
		else
		{
			Intent data = new Intent();       
	        setResult(RESULT_CANCELED, data);
	        finish();
		}
		Display display = ((WindowManager) 
		getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_down);
	}
	//
	
	public String uploadFile(String filename, String urlserver) 
	{
        String respone = "error";
        
        String fileName = filename;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024; 
        File sourceFile = new File(fileName); 
         
        if (!sourceFile.isFile()) 
        {
        	dialog.dismiss(); 
        	runOnUiThread(new Runnable() {
                public void run() {
                	Toast.makeText(Tab2_ThayDoiHinhDaiDien.this, "File không tồn tại", Toast.LENGTH_SHORT).show();
                }
            });
        	
             respone = "error";
        }
        else
        {
             try { 
                   // open a URL connection to the Servlet
                 FileInputStream fileInputStream = new FileInputStream(sourceFile);
                 URL url = new URL(urlserver);
                  
                 // Open a HTTP  connection to  the URL
                 conn = (HttpURLConnection) url.openConnection(); 
                 conn.setDoInput(true); // Allow Inputs
                 conn.setDoOutput(true); // Allow Outputs
                 conn.setUseCaches(false); // Don't use a Cached Copy
                 conn.setRequestMethod("POST");
                 conn.setRequestProperty("Connection", "Keep-Alive");
                 conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                 conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                 conn.setRequestProperty("uploadedfile", fileName);
                  
                 dos = new DataOutputStream(conn.getOutputStream());
        
                 dos.writeBytes(twoHyphens + boundary + lineEnd); 
                 dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
                                           + fileName + "\"" + lineEnd);
                  
                 dos.writeBytes(lineEnd);
        
                 // create a buffer of  maximum size
                 bytesAvailable = fileInputStream.available(); 
        
                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
                 buffer = new byte[bufferSize];
        
                 // read file and write it into form...
                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                    
                 while (bytesRead > 0) {
                      
                   dos.write(buffer, 0, bufferSize);
                   bytesAvailable = fileInputStream.available();
                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
                   bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
                    
                  }
        
                 // send multipart form data necesssary after file data...
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
        
                 // Responses from the server (code and message)
                 serverResponseCode = conn.getResponseCode();
                 String serverResponseMessage = conn.getResponseMessage();
                   
                 Log.i("uploadFile", "HTTP Response is : "
                         + serverResponseMessage + ": " + serverResponseCode);
                  
                 if(serverResponseCode == 200)
                 {
                	 dialog.dismiss();
                	 runOnUiThread(new Runnable() {
                         public void run() {
                        	 Toast.makeText(Tab2_ThayDoiHinhDaiDien.this, "Thay đổi ảnh đại diện thành công.", Toast.LENGTH_SHORT).show();
                         }
                     }); 
                	 respone="success";
                 }    
                  
                 //close the streams //
                 fileInputStream.close();
                 dos.flush();
                 dos.close();
                   
            } catch (MalformedURLException ex) 
            {
            	dialog.dismiss();
                ex.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                    	Toast.makeText(Tab2_ThayDoiHinhDaiDien.this, "Upload không thành công", Toast.LENGTH_SHORT).show();
                    }
                });
                respone="error";
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
            } catch (Exception ex)
            {
            	dialog.dismiss(); 
                ex.printStackTrace();
                runOnUiThread(new Runnable() {
                    public void run() {
                    	Toast.makeText(Tab2_ThayDoiHinhDaiDien.this, "Upload không thành công!!!", Toast.LENGTH_SHORT).show();
                    }
                });
                respone="error";
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
            }
            dialog.dismiss(); 
         }
        return respone;
       }
	//
}
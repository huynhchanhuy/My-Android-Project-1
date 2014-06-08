package com.example.doan_didong;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

public class FunctionFolderAndFile 
{
	private Context _context;
    public FunctionFolderAndFile(Context context) {
        this._context = context;
    }
    
    
    
    //hàm check folder có tồn tại trong getExternalStorageDirectory không
    //  nếu không tồn tại thì tạo folder
    //  nếu tồn tại thì thôi
    public static boolean createFolderByFoldername(String foldername)
    {
    	int folder_exist = 0;
		File folder = new File(Environment.getExternalStorageDirectory() + "/hocngoaingu/"+foldername);
		if (folder.exists() && folder.isDirectory() )
		{
		    folder_exist=1;
		} 
		else if (!folder.exists() || !folder.isDirectory())
		{
		    if(folder.mkdirs()) folder_exist=1;
		    else folder_exist=0;
		}
		if(folder_exist==1) return true;
		else return false;
    }
    
    
    
    //hàm check folder có tồn tại trong getExternalStorageDirectory không
    //  nếu không tồn tại thì tạo folder
    //  nếu tồn tại thì thôi
    public static boolean checkFileExist(String foldername, String filename)
    {
    	String fullpath = Environment.getExternalStorageDirectory() + "/hocngoaingu/"+ foldername + "/"+filename;
    	File filename_fullpath = new File(fullpath);
	    if(filename_fullpath.isFile() && filename_fullpath.exists())
	    {
	    	return true;
	    }
	    else 
	    {
	    	return false;
	    }
    }
    
    public static boolean createFile(String foldername, String filename)
    {
    	String fullpath = Environment.getExternalStorageDirectory() + "/hocngoaingu/"+ foldername;
    	File filename_fullpath = new File(fullpath,filename);
	    if(filename_fullpath.isFile() && filename_fullpath.exists())
	    {
	    	return true;
	    }
	    else
		{
		    try {
				if(filename_fullpath.createNewFile()) 
					return true;
				else 
					return false;
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		return false;
    }
    
    
    
   
    
    
    
    //Đọc string từ file
    //tham số foldername, filename
    //
    public static String readFromFile( String foldername, String filename)
    {

        String ret = "";
        String fullpath = Environment.getExternalStorageDirectory() + "/hocngoaingu/"+ foldername + "/"+filename;
        if(checkFileExist(foldername, filename))
        {
        try {
            File f = new File(fullpath);
            FileInputStream fileIS = new FileInputStream(f);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileIS));
            StringBuilder stringBuilder = new StringBuilder();

	            if ( fileIS != null ) 
	            {
	                String receiveString = "";
	                while ( (receiveString = bufferedReader.readLine()) != null ) 
	                {
	                    stringBuilder.append(receiveString);
	                }
	
	                fileIS.close();
	                ret = stringBuilder.toString();
	            }
	            fileIS.close();
        	}
	        catch (FileNotFoundException e) 
	        {
	            Log.e("FileNotFoundException", "File not found: " + e.toString());
	            ret="";
	        } catch (IOException e) 
	        {
	            Log.e("IOException", "Can not read file: " + e.toString());
	            ret="";
	        }
        }
        return ret;
    }
    
    
    
    public static boolean removeFileFromSdcard(String foldername, String filename)
    {
    	String fullpath = Environment.getExternalStorageDirectory() + "/hocngoaingu/"+ foldername + "/"+filename;
    	
    	if(checkFileExist(foldername,filename))
    	{
    		File file = new File(fullpath);
    		return file.delete();
    	}
    	else return false;
    }
    
    
    public static void downloadFileFromServer(String filename_sdcard, String filename_server) throws MalformedURLException, IOException
    {
    	String urlsdcard = Environment.getExternalStorageDirectory() + "/hocngoaingu/version/"+filename_sdcard;
    	String urlserver = Constant.BASE_URL_SERVER + "version/"+filename_server;
    	BufferedInputStream in = null;
        FileOutputStream fout = null;
        try
        {
    	    URL url = new URL(urlserver);
 
            in = new BufferedInputStream(url.openStream());
            fout = new FileOutputStream(urlsdcard);
 
            byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1)
            {
                fout.write(data, 0, count);
            }
        }
        finally
        {
            if (in != null)
                    in.close();
            if (fout != null)
                    fout.close();
        }
    }
    
    
    
    
    
    
    //Ghi json_string vào file
    //tham số: json_string, foldername, filename
    //
    /*public static boolean writeToFile(Context _context, String json_string, String foldername, String filename)
    {  
    	String fullpath = Environment.getExternalStorageDirectory() + "/hocngoaingu/"+ foldername + "/"+filename;
    	try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(_context.openFileOutput(fullpath, Context.MODE_PRIVATE));
            outputStreamWriter.write(json_string);
            outputStreamWriter.close();
            return true;
        }
        catch (IOException e) 
        {
            Log.e("Exception", "File write failed: " + e.toString());
            return false;
        } 
    }*/
}

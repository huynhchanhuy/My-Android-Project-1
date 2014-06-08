package com.example.doan_didong;

import java.io.File;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

public class FunctionCheck 
{
	private Context _context;
    //Hàm dựng khởi tạo đối tượng
    public FunctionCheck(Context context) {
        this._context = context;
    }
	public boolean checkInternetConnection() {// kiem tra ket noi Internet
		ConnectivityManager conMgr = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
		// && conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}
    public boolean checkMobileInternetConn() {
        //Tạo đối tương ConnectivityManager để trả về thông tin mạng
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //Nếu đối tượng khác null
        if (connectivity != null) {
            //Nhận thông tin mạng
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                //Tìm kiếm thiết bị đã kết nối được internet chưa
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean checkWifiInternetConn() {
        //Tạo đối tương ConnectivityManager để trả về thông tin mạng
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //Nếu đối tượng khác null
        if (connectivity != null) {
            //Nhận thông tin mạng
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                //Tìm kiếm thiết bị đã kết nối được internet chưa
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    
    
	
}


package com.example.doan_didong;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class Tab2_ThongTinCaNhan extends SherlockFragment 
{
	private ProgressDialog pDialog;
	Bundle b;
	JSONParser jsonParser = new JSONParser();

	public int success;
	public int da_ton_tai;
	
	TextView txtHovaten;
	TextView txtNgaysinh;
	TextView txtGioitinh;
	TextView txtDiachi;
	TextView txtEmail;
	TextView txtSdt;
	ImageView imgHinhdaidien;
	
	Button btn_thaydoithongtin;
	Button btn_thaydoimatkhau;
	Button btn_thaydoihinhdaidien;
	Button btn_thoat;
	
	//
	// Download image from server
	//
    ImageView view;
    URL url;
    
    //ShareActionProvider mShareActionProvider;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.tab2_thongtincanhan, container, false);
		
		txtHovaten = (TextView)view.findViewById(R.id.txt_hovaten);
		txtNgaysinh = (TextView)view.findViewById(R.id.txt_ngaysinh);
		txtGioitinh = (TextView)view.findViewById(R.id.txt_gioitinh);
		txtDiachi = (TextView)view.findViewById(R.id.txt_diachi);
		txtEmail = (TextView)view.findViewById(R.id.txt_email);
		txtSdt = (TextView)view.findViewById(R.id.txt_sdt);
		imgHinhdaidien = (ImageView)view.findViewById(R.id.img_hinhdaidien);
		
		
		txtHovaten.setText("Họ và tên: "+MainActivity.hovaten);
		txtNgaysinh.setText("Ngày sinh: "+MainActivity.ngaysinh);
		
		txtGioitinh.setText("Giới tính: "+MainActivity.gioitinh);
		txtDiachi.setText("Địa chỉ: "+MainActivity.diachi);
		txtEmail.setText("Email: "+MainActivity.email);
		txtSdt.setText("Số điện thoại: "+MainActivity.sdt);
		if(MainActivity.link_hinhdaidien.length() != 0 && MainActivity.bmImg == null)
		{
			
			try{
				
				new GetInformationUser().execute();
			}
			catch(Exception e){ Log.d("download image from server","Khong thanh cong"); e.printStackTrace();};
		} 
		if(MainActivity.link_hinhdaidien.length() == 0)
		{
				imgHinhdaidien.setImageResource(R.drawable.icon_hinhdaidien);
				Resources resources = getSherlockActivity().getResources();
			    DisplayMetrics metrics = resources.getDisplayMetrics();
			    int px = 100 * (int)(metrics.densityDpi / 160f);
				imgHinhdaidien.setLayoutParams(new LinearLayout.LayoutParams(px, px));	
		}
		if(MainActivity.link_hinhdaidien.length() != 0 && MainActivity.bmImg != null)
		{
			
			imgHinhdaidien.setImageBitmap(MainActivity.bmImg);	
			Resources resources = getSherlockActivity().getResources();
		    DisplayMetrics metrics = resources.getDisplayMetrics();
		    int px = 100 * (int)(metrics.densityDpi / 160f);
			imgHinhdaidien.setLayoutParams(new LinearLayout.LayoutParams(px, px));
		} 
		
		
		
		btn_thaydoithongtin = (Button)view.findViewById(R.id.btn_thaydoi_thongtincanhan);
		btn_thaydoimatkhau = (Button)view.findViewById(R.id.btn_thaydoi_matkhau);
		btn_thaydoihinhdaidien = (Button)view.findViewById(R.id.btn_thaydoi_hinhdaidien);
		btn_thoat = (Button)view.findViewById(R.id.btn_dang_xuat);

		btn_thaydoithongtin.setOnClickListener(new View.OnClickListener() 
		{
            @Override
            public void onClick(View arg0) {
            	
            	Intent i = new Intent(arg0.getContext(), Tab2_ThayDoiThongTinCaNhanActivity.class);
				
            	i.putExtra(Constant.KEYWORD_USER_ID, MainActivity.iduser);
				i.putExtra(Constant.KEYWORD_USER_NAME, MainActivity.username);
				i.putExtra(Constant.KEYWORD_HOVATEN, MainActivity.hovaten);
				i.putExtra(Constant.KEYWORD_NGAYSINH, MainActivity.ngaysinh);
				i.putExtra(Constant.KEYWORD_GIOITINH, MainActivity.gioitinh);
				i.putExtra(Constant.KEYWORD_DIACHI, MainActivity.diachi);
				i.putExtra(Constant.KEYWORD_EMAIL, MainActivity.email);
				i.putExtra(Constant.KEYWORD_SDT, MainActivity.sdt);
				i.putExtra(Constant.KEYWORD_USER_TYPE, MainActivity.usertype);
				i.putExtra(Constant.KEYWORD_USER_ACTIVE, MainActivity.active);
				i.putExtra(Constant.KEYWORD_LINK_HINHDAIDIEN, MainActivity.link_hinhdaidien);
				getActivity().startActivityForResult(i, MainActivity.GET_CODE);
				
            	
				Display display = ((WindowManager) 
				getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
				getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_out_left);
                
            }
        });
		btn_thaydoimatkhau.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            	
            	Intent i = new Intent(arg0.getContext(), Tab2_ThayDoiMatKhauActivity.class);
				
            	i.putExtra(Constant.KEYWORD_USER_ID, MainActivity.iduser);
				i.putExtra(Constant.KEYWORD_USER_NAME, MainActivity.username);
				getActivity().startActivity(i);
				Display display = ((WindowManager) 
				getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
				getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_out_left);
            }
        });
		btn_thaydoihinhdaidien.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) 
            {
            	Intent i = new Intent(arg0.getContext(), Tab2_ThayDoiHinhDaiDien.class);
            	getActivity().startActivityForResult(i, MainActivity.GET_CODE);
            	Display display = ((WindowManager) 
        		getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        		getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_out_left);
            }
        });
		btn_thoat.setOnClickListener(new View.OnClickListener() 
		{
            @Override
            public void onClick(View arg0) {
            	//getActivity().onBackPressed();
            	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            	builder.setMessage("Bạn có chắc muốn đăng xuất ?")
            	       .setCancelable(false)
            	       .setIcon(R.drawable.icon_thongbao)
            	       .setTitle("Đăng Xuất?")
            	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            	           public void onClick(DialogInterface dialog, int id) {
            	        	   Intent i = new Intent(getActivity().getBaseContext(), LoginActivity.class);
            	        	   startActivity(i); 
            	        	   getActivity().finish();
            	        	   MainActivity.bmImg = null;
            	           }
            	       })
            	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
            	           public void onClick(DialogInterface dialog, int id) {
            	                dialog.cancel();
            	           }
            	       });
            	AlertDialog alert = builder.create();
            	alert.show();
            }
        });
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setUserVisibleHint(true);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();	 
	    
	    txtHovaten.setText("Họ và tên: "+MainActivity.hovaten);
		txtNgaysinh.setText("Ngày sinh: "+MainActivity.ngaysinh);
		txtGioitinh.setText("Giới tính: "+MainActivity.gioitinh);
		txtDiachi.setText("Địa chỉ: "+MainActivity.diachi);
		txtEmail.setText("Email: "+MainActivity.email);
		txtSdt.setText("Số điện thoại: "+MainActivity.sdt);
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (requestCode == MainActivity.GET_CODE) 
        {
            if (resultCode == getActivity().RESULT_CANCELED) 
            {
            } 
            else 
            {
                
                if (data != null && resultCode == MainActivity.RESULT_OK) 
                {	
                	txtHovaten.setText("Họ và tên: "+MainActivity.hovaten);
            		txtNgaysinh.setText("Ngày sinh: "+MainActivity.ngaysinh);
            		txtGioitinh.setText("Giới tính: "+MainActivity.gioitinh);
            		txtDiachi.setText("Địa chỉ: "+MainActivity.diachi);
            		txtEmail.setText("Email: "+MainActivity.email);
            		txtSdt.setText("Số điện thoại: "+MainActivity.sdt);
                }
                if (data != null && resultCode == MainActivity.CHANGE_IMAGE) 
                {	
                	Log.e("===>>result change hình dai dien","===>>result change hình dai dien");
                	b = data.getExtras();
            		String link_hinhdaidien_change 	= b.getString("link_hinhdaidien");
            		Bitmap bmp = BitmapFactory.decodeFile(link_hinhdaidien_change);
            		MainActivity.bmImg = bmp;
            		
            		imgHinhdaidien.setImageBitmap(MainActivity.bmImg);	
    				Resources resources = getActivity().getResources();
    			    DisplayMetrics metrics = resources.getDisplayMetrics();
    			    int px = 100 * (int)(metrics.densityDpi / 160f);
    				imgHinhdaidien.setLayoutParams(new LinearLayout.LayoutParams(px, px));
                }
            }
        }
	}
	
	class GetInformationUser extends AsyncTask<String, String, String> 
	{
		String url_hinh_dai_dien="";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getSherlockActivity());
			pDialog.setTitle("Vui lòng đợi!");
			pDialog.setMessage("Đang tải dữ liệu ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			pDialog.setProgress(100);
			Handler handler = new Handler();
	        handler.postDelayed(new Runnable() {
	            public void run() {
	            	pDialog.dismiss();
	            }}, 2000);
			
		}
		protected String doInBackground(String... args) {
			 
			url_hinh_dai_dien = Constant.BASE_URL_HINHDAIDIEN + MainActivity.link_hinhdaidien;
			try {
				url = new URL(url_hinh_dai_dien);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(30000);
	            //conn.setReadTimeout(30000);
	            conn.setInstanceFollowRedirects(true);
	            
                conn.setDoInput(true);   
                conn.connect();     
                InputStream is = conn.getInputStream();
                
                MainActivity.bmImg = BitmapFactory.decodeStream(is); 
                //MainActivity.link_hinhdaidien="";
			} catch (MalformedURLException e) {
				Log.d("------ Url lỗi","------ Url hình dai dien lỗi");
				e.printStackTrace();
			} catch (IOException e) {
				Log.d("-------- InputStream và OutputStream lỗi","--------InputStream và OutputStream lỗi ");
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) 
		{
			if(MainActivity.bmImg != null)
			{
				imgHinhdaidien.setImageBitmap(MainActivity.bmImg);	
				Resources resources = getActivity().getResources();
			    DisplayMetrics metrics = resources.getDisplayMetrics();
			    int px = 100 * (int)(metrics.densityDpi / 160f);
				imgHinhdaidien.setLayoutParams(new LinearLayout.LayoutParams(px, px));
			}
			if(MainActivity.bmImg == null) Log.d("----ko tải dc hình đại diện----","--- ko tải dc hình đại diện");
		}

	}
}

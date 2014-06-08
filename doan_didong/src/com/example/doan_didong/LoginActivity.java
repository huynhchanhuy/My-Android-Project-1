package com.example.doan_didong;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.doan_didong.ReduceImage.ScalingLogic;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity 
{
	
	SlidingUpPanelLayout layout1;
	private ProgressDialog pDialog;
	private FunctionCheck instantFunctionCheck;
	boolean doubleBackToExitPressedOnce = false;

	JSONParser jsonParser = new JSONParser();
	EditText ed_username;
	EditText ed_password;
	
	Button btn_login;
	Button btn_register;
	Button btn_quen_mat_khau;
	Button btn_thoat;
	CheckBox cb_remember;
	
	public int success;
	
	public int iduser;
	public String username;
	public String hovaten;
	public String ngaysinh;
	public String gioitinh;
	public String diachi;
	public String email;
	public String sdt;
	public int active;
	public int usertype;
	public String link_hinhdaidien;
	
	private static final int DIALOG_LOGIN_REQUIRE = 2; //tên đăng nhập và mật khẩu trống
	private static final int DIALOG_NO_INTERNET = 3;  // không có kết nối internet
	private static final int DIALOG_ERROR = 4;		  //lỗi 
	private static final int DIALOG_LOGIN_ERROR = 5;  //lỗi loginprocess
	private static final int DIALOG_INVALID_NAME_PASS = 6;  //lỗi loginprocess
	private static final int DIALOG_USER_LOCK = 7;  //tài khoản bị khóa
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.login);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//set không cho xoay màn hình		
		instantFunctionCheck = new FunctionCheck(getApplicationContext());//tạo đối tượng kiểm tra kết nối internet
		
		
		layout1 = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
		//layout1.setShadowDrawable(getResources().getDrawable(R.drawable.icon_co_loi));
		layout1.setPanelSlideListener(new PanelSlideListener() {
			@Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (slideOffset < 0.2) {
                    if (getActionBar().isShowing()) {
                        getActionBar().hide();
                    }
                } else {
                    if (!getActionBar().isShowing()) {
                        getActionBar().show();
                    }
                }
            }

            @Override
            public void onPanelExpanded(View panel) {}
            @Override
            public void onPanelCollapsed(View panel) {}
        });
		
		
		
		// Edit Text
		ed_username = (EditText) findViewById(R.id.ed_username);
		ed_password = (EditText) findViewById(R.id.ed_password);
		btn_thoat 	= (Button) findViewById(R.id.btn_thoat);
		
		
		btn_thoat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) 
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
				builder.setMessage("Bạn có chắc muốn thoát ứng dụng?")
				       .setCancelable(false)
				       .setIcon(R.drawable.icon_thongbao)
				       .setTitle("Thoát")
				       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                finish();
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
		// Create button
		btn_login = (Button) findViewById(R.id.btnlogin);

		// button click event
		btn_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) 
			{
				
				if(!instantFunctionCheck.checkInternetConnection())// no Internet connection- Internet
				{
					showDialog(DIALOG_NO_INTERNET);
					return;
				} 
				else if (ed_username.length() == 0 || ed_password.length() == 0) {
					showDialog(DIALOG_LOGIN_REQUIRE);
					return;
				} else {
					try {
						new LoginProgress().execute();
					} catch (Exception e) {
						showDialog(DIALOG_ERROR);
					}

				}
			}
		});
		btn_register = (Button)findViewById(R.id.btn_register);
		btn_register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) 
			{
				Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivity(i);
				//finish();
			}
		});
		
		btn_quen_mat_khau = (Button) findViewById(R.id.btn_quen_mat_khau);
		btn_quen_mat_khau.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) 
			{
				Intent i = new Intent(getApplicationContext(), QuenMatKhau.class);
				startActivity(i);
				//finish();
			}
		});
		
		cb_remember = (CheckBox)findViewById(R.id.remember_username);
		cb_remember.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				if(FunctionFolderAndFile.createFolderByFoldername("remember"))
				{
					if(FunctionFolderAndFile.checkFileExist("remember", "remember.txt")==true)
					{
						
					}
					else
					{
						FunctionFolderAndFile.createFile("remember","remember.txt");
					}
					
				}
				if(isChecked==true)
				{
					Log.e("----CHecked","----checked");
					String save = "";
					try{
						byte[] data_user = ed_username.getText().toString().getBytes("UTF-8");
						byte[] data_pass = ed_password.getText().toString().getBytes("UTF-8");
						
						String uu = Base64.encodeToString(data_user, Base64.DEFAULT);
						String pp = Base64.encodeToString(data_pass, Base64.DEFAULT);
						save = save + uu + "," + pp;
						Log.e(save, "------------");
						File file = new File(Environment.getExternalStorageDirectory() + "/hocngoaingu/remember/remember.txt");
				        FileWriter writer = new FileWriter(file);
				        writer.append(save);
				        writer.flush();
				        writer.close();
				        Log.e("Doc tu file remember ","--------"+FunctionFolderAndFile.readFromFile("remember", "remember.txt"));
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					
//					byte[] data = Base64.decode(base64, Base64.DEFAULT);
//					String text = new String(data, "UTF-8");
				}
				else
				{
					Log.e("---Not checked","-----Not checked");
					try{
						File file = new File(Environment.getExternalStorageDirectory() + "/hocngoaingu/remember/remember.txt");
				        FileWriter writer = new FileWriter(file);
				        writer.append("");
				        writer.flush();
				        writer.close();
				        Log.e("Doc tu file remember ","--------"+FunctionFolderAndFile.readFromFile("remember", "remember.txt"));
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		});
		//
		String remember = FunctionFolderAndFile.readFromFile("remember", "remember.txt");
		if("".equals(remember))
		{
			Log.e("----File remember.txt rỗng","----File remember.txt rỗng");
			ed_username.setText("");
			ed_password.setText("");
			cb_remember.setChecked(false);
		}
		else
		{
			Log.e("----remember.txt BASE64","----remember.txt BASE64");
			try {
				String[] uu_pp = remember.split(",");
				byte[] data_uu = Base64.decode(uu_pp[0], Base64.DEFAULT);
				byte[] data_pp = Base64.decode(uu_pp[1], Base64.DEFAULT);
				String uu = new String(data_uu, "UTF-8");
				String pp = new String(data_pp, "UTF-8");
				ed_username.setText(uu);
				ed_password.setText(pp);
				cb_remember.setChecked(true);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			
		}
		//
		
		
	}
	
	//
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
        AlertDialog.Builder builder;
        switch(id) {
        case DIALOG_LOGIN_REQUIRE:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Thông báo!");
    		builder.setIcon(R.drawable.icon_canh_bao);
            builder.setMessage("Vui lòng nhập vào tên đăng nhập và mật khẩu!")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do something here
                }
            });
            dialog = builder.create();
            break;
        case DIALOG_NO_INTERNET:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Internet!");
    		builder.setIcon(R.drawable.icon_canh_bao);
            builder.setMessage("Kiểm tra kết nối internet!")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do something here
                }
            });
            dialog = builder.create();
            break;
        case DIALOG_ERROR:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Cảnh báo!");
    		builder.setIcon(R.drawable.icon_co_loi);
            builder.setMessage("Có lỗi")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do something here
                }
            });
            dialog = builder.create();
            break;
        case DIALOG_LOGIN_ERROR:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Timeout !");
    		builder.setIcon(R.drawable.icon_stop);
            builder.setMessage("Đăng nhập không thành công!\n Vui lòng thử lại.")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do something here
                }
            });
            dialog = builder.create();
            break;
        case DIALOG_INVALID_NAME_PASS:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Lỗi");
    		builder.setIcon(R.drawable.icon_stop);
            builder.setMessage("Tên đăng nhập hoặc mật khẩu không đúng!")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) 
                {
                    //Do something here
                }
            });
            dialog = builder.create();
            break;
        case DIALOG_USER_LOCK:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Tài khoản bị khóa");
    		builder.setIcon(R.drawable.icon_stop);
            builder.setMessage("Tài khoản của bạn đã bị khóa!")
            .setCancelable(false)
            .setPositiveButton("Liên Hệ Admin", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int id) 
                {
                	try {
						   Uri URI = null;
                        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                        //emailIntent.setType("plain/text");
                        emailIntent.setType("message/rfc822");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                                      new String[] { "tranvokhoinguyen@gmail.com" });
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                      "Tiêu đề");
                        if (URI != null) 
                        {
                               emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
                        }
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Nội dung");
                        startActivity(Intent.createChooser(emailIntent,"Sending email..."));

                	} 
                	catch (Throwable t) 
                	{
                        Toast.makeText(getApplicationContext(),
                                      "Gửi thất bại, vui lòng thử lại",Toast.LENGTH_LONG).show();
                	}
                }
            })
            .setNegativeButton("Thoát", new DialogInterface.OnClickListener()
            {
				
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					
				}
			});
            dialog = builder.create();
            break;
        default:
            dialog = null;
        }
        return dialog;
	}
	
	@Override
	public void onBackPressed() 
	{
	        if (doubleBackToExitPressedOnce) {
	            super.onBackPressed();
	            finish();
	            
	        }
	        this.doubleBackToExitPressedOnce = true;
	        Toast.makeText(this, "Vui lòng nhấn lần nữa để thoát!", Toast.LENGTH_SHORT).show();
	        new Handler().postDelayed(new Runnable() {

	            @Override
	            public void run() {
	             doubleBackToExitPressedOnce=false;   

	            }
	        }, 2000);
	 } 
	
	
	/*
	 * 
	 * 
	 */
	class LoginProgress extends AsyncTask<String, String, String> 
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Đang đăng nhập ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		protected String doInBackground(String... args) {
			String post_name = ed_username.getText().toString();
			String post_pass = ed_password.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("post_name", post_name));
			params.add(new BasicNameValuePair("post_pass", post_pass));
			params.add(new BasicNameValuePair("request", "post_login"));

			
			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_login.php", "POST", params);
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					if(json.getInt(Constant.KEYWORD_SUCCESS)==1 && json.getJSONArray(Constant.KEYWORD_JSON_LOGIN_RESPONSE).length()>0)
						success=1;
					else success=0;

					if (success == 1) 
					{
						JSONArray jsonArray = json.getJSONArray(Constant.KEYWORD_JSON_LOGIN_RESPONSE); // JSON Array	
						JSONObject firstJsonElement = jsonArray.getJSONObject(0);
						
						iduser 		= firstJsonElement.getInt(Constant.KEYWORD_USER_ID);
						username 	= firstJsonElement.getString(Constant.KEYWORD_USER_NAME);
						hovaten 	= firstJsonElement.getString(Constant.KEYWORD_HOVATEN);
						ngaysinh 	= firstJsonElement.getString(Constant.KEYWORD_NGAYSINH);
						gioitinh 	= firstJsonElement.getString(Constant.KEYWORD_GIOITINH);
						diachi 		= firstJsonElement.getString(Constant.KEYWORD_DIACHI);
						email 		= firstJsonElement.getString(Constant.KEYWORD_EMAIL);
						sdt 		= firstJsonElement.getString(Constant.KEYWORD_SDT);
						usertype    = firstJsonElement.getInt(Constant.KEYWORD_USER_TYPE);
						active		= firstJsonElement.getInt(Constant.KEYWORD_USER_ACTIVE);
						link_hinhdaidien 	= firstJsonElement.getString(Constant.KEYWORD_LINK_HINHDAIDIEN);
						
						if(active == 0) 
							success = 2;
						else if(active == 1)
							 {
								if(usertype == 0) //user bình thường
								{
								}
								if(usertype == 1) {} //admin
								if(usertype == 2) {} //giao vien 
							 }
					} else 
						{
							success = 0;
						}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else 
				{
					success=3;
				}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			
			if(success == 0)
				showDialog(DIALOG_INVALID_NAME_PASS);
			else if(success == 2)
				showDialog(DIALOG_USER_LOCK);
				else if(success == 3)
					showDialog(DIALOG_LOGIN_ERROR);
			
			if(active == 1 && success == 1)
			{
				
					if(usertype == 0)
					{
						try{
							Intent i = new Intent(LoginActivity.this, com.example.doan_didong.MainActivity.class);
							
							i.putExtra(Constant.KEYWORD_USER_ID, iduser);
							i.putExtra(Constant.KEYWORD_USER_NAME, username);
							i.putExtra(Constant.KEYWORD_HOVATEN, hovaten);
							
							i.putExtra(Constant.KEYWORD_NGAYSINH, ngaysinh);
							i.putExtra(Constant.KEYWORD_GIOITINH, gioitinh);
							i.putExtra(Constant.KEYWORD_DIACHI, diachi);
							i.putExtra(Constant.KEYWORD_EMAIL, email);
							i.putExtra(Constant.KEYWORD_SDT, sdt);
							
							i.putExtra(Constant.KEYWORD_USER_TYPE, usertype);
							i.putExtra(Constant.KEYWORD_USER_ACTIVE, active);
							
							i.putExtra(Constant.KEYWORD_LINK_HINHDAIDIEN, link_hinhdaidien);
							
							startActivity(i);
							finish();
						}catch(Exception e)
						{
							e.printStackTrace();
						}
					}
			}	
		}
		

	}
}

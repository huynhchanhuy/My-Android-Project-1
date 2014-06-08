package com.example.doan_didong;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class QuenMatKhau extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;
	private FunctionCheck instantFunctionCheck;
	private EmailValidator emailValidator;
	private boolean validFormatEmail=false;

	JSONParser jsonParser = new JSONParser();
	EditText ed_name;
	EditText ed_email;
	Spinner sp_cau_hoi_bi_mat;
	EditText ed_cau_tra_loi_bi_mat;
	
	public int iduser;
	public String name;
	public String username;
	public String hovaten;
	public String ngaysinh;
	public String gioitinh;
	public String diachi;
	public String email;
	public String sdt;
	public String link_hinhdaidien;
	public String cau_hoi_bi_mat = "Tên con vật bạn yêu thích nhất?";
	public String cau_tra_loi_bi_mat;
	public String mat_khau_moi;
	
	public int usertype;
	public int active;
	
	public int da_ton_tai_kiem_tra;
	public int success_kiem_tra;
	public int email_khong_dung;
	public int cau_hoi_bi_mat_khong_dung;
	public int cau_tra_loi_bi_mat_khong_dung;
	
	Button btn_quen_mat_khau;
	
	private static final int DIALOG_KHONG_DE_TRONG = 1; //không được để trống
	private static final int DIALOG_EMAIL_ERROR = 2;     // email không đúng định dạng
	
	private static final int DIALOG_NO_INTERNET = 3;  // không có kết nối internet
	private static final int DIALOG_TIME_ERROR = 4;		  //lỗi đăng kí không thành công
	
	private static final int DIALOG_USERNAME_6_KI_TU = 5; //username ít nhất 6 kí tự
	
	private static final int DIALOG_USERNAME_KHONG_TON_TAI = 6;
	private static final int DIALOG_THANH_CONG_LOGIN_TU_DONG = 7;
	
	private static final int DIALOG_EMAIL_KHONG_DUNG = 8;
	private static final int DIALOG_CAUHOI_BIMAT_KHONGDUNG = 9;
	private static final int DIALOG_CAUTRALOI_BIMAT_KHONGDUNG = 10;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quen_mat_khau);
		
		//set không cho xoay màn hình
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				
		//tạo đối tượng kiểm tra kết nối internet
		instantFunctionCheck = new FunctionCheck(getApplicationContext());
				
		// Edit Text
		ed_name = (EditText) findViewById(R.id.ed_username);
		ed_email = (EditText) findViewById(R.id.ed_email);
		sp_cau_hoi_bi_mat = (Spinner) findViewById(R.id.cau_hoi_bi_mat);
		ed_cau_tra_loi_bi_mat = (EditText) findViewById(R.id.ed_cau_tra_loi_bi_mat);
		btn_quen_mat_khau = (Button) findViewById(R.id.btn_quen_mat_khau);
		
		btn_quen_mat_khau.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) 
			{
				int so_luong_loi = 0;
				if (!instantFunctionCheck.checkInternetConnection())// kiểm tra kết nối internet
				{
					so_luong_loi++;
					showDialog(DIALOG_NO_INTERNET);
					return;
				}
				if(ed_name.length()==0 || ed_email.length()==0 || ed_cau_tra_loi_bi_mat.length()==0)
				{
					so_luong_loi++;
					showDialog(DIALOG_KHONG_DE_TRONG);
					return;
				}
				if(ed_name.length() < 6 || ed_name.length() > 20 )
				{
					so_luong_loi++;
					showDialog(DIALOG_USERNAME_6_KI_TU);
					return;
				}
				if(ed_email.length()!=0)
				{
					emailValidator = new EmailValidator();
					validFormatEmail=emailValidator.validate(ed_email.getText().toString());
					if(!validFormatEmail){
						// show message invalid email and return
						so_luong_loi++;
						showDialog(DIALOG_EMAIL_ERROR);
						return;
					}
				}
				if(so_luong_loi == 0) new QuenMatKhau_Process().execute();
				else showDialog(DIALOG_TIME_ERROR);
				//Toast.makeText(getBaseContext(), "Please input all information!",Toast.LENGTH_LONG).show();
				
			}
		});
		 sp_cau_hoi_bi_mat.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		 {
		                                                 
				@Override
				public void onItemSelected(AdapterView<?> arg0,View arg1,int arg2,long arg3) 
				{
					cau_hoi_bi_mat = sp_cau_hoi_bi_mat.getItemAtPosition(arg2).toString();
					//taoDialog(cau_hoi_bi_mat);
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
															// TODO Auto-generated method stub
							}
		 });
	}
	
	public void taoDialog(String message)
	{
		Dialog dialog;
        AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(this);
        builder.setTitle("Thành công!");
		builder.setIcon(R.drawable.icon_success);
        builder.setMessage("Mật khẩu mới: "+ message +"\nChọn OK để đăng nhập tự động.\n Bạn nên đổi lại mật khẩu mới. ")
        .setCancelable(false)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	Intent i = new Intent(getApplicationContext(), MainActivity.class);
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
            }
        });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                });
        dialog = builder.create();
        dialog.show();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
        AlertDialog.Builder builder;
        switch(id) {
        
        case DIALOG_NO_INTERNET:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Cảnh báo!");
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
        case DIALOG_KHONG_DE_TRONG:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Thông báo!");
    		builder.setIcon(R.drawable.icon_canh_bao);
            builder.setMessage("Chưa điền đầy đủ thông tin!")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do something here
                }
            });
            dialog = builder.create();
            break;
        case DIALOG_EMAIL_ERROR:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Cảnh báo!");
    		builder.setIcon(R.drawable.icon_canh_bao);
            builder.setMessage("Email không đúng định dạng example@abc.com!")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do something here
                }
            });
            dialog = builder.create();
            break;
        case DIALOG_TIME_ERROR:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Timeout !");
    		builder.setIcon(R.drawable.icon_co_loi);
            builder.setMessage("Lỗi! Vui lòng thử lại !")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do something here
                }
            });
            dialog = builder.create();
            break;
        case DIALOG_USERNAME_6_KI_TU:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Cảnh báo!");
    		builder.setIcon(R.drawable.icon_canh_bao);
            builder.setMessage("Tên đăng nhập từ 6 đến 20 kí tự!")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do something here
                }
            });
            dialog = builder.create();
            break;
        case DIALOG_USERNAME_KHONG_TON_TAI:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Lỗi!");
    		builder.setIcon(R.drawable.icon_stop);
            builder.setMessage("Tên đăng nhập này không tồn tại!")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do something here
                }
            });
            dialog = builder.create();
            break;
        case DIALOG_EMAIL_KHONG_DUNG:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Lỗi!");
    		builder.setIcon(R.drawable.icon_stop);
            builder.setMessage("Email của tài khoản này không đúng!")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do something here
                }
            });
            dialog = builder.create();
            break;
        case DIALOG_CAUHOI_BIMAT_KHONGDUNG:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Lỗi!");
    		builder.setIcon(R.drawable.icon_stop);
            builder.setMessage("Sai câu hỏi bí mật!")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do something here
                }
            });
            dialog = builder.create();
            break;
        case DIALOG_CAUTRALOI_BIMAT_KHONGDUNG:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Lỗi!");
    		builder.setIcon(R.drawable.icon_stop);
            builder.setMessage("Sai câu trả lời bí mật!")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do something here
                }
            });
            dialog = builder.create();
            break;
        /*case DIALOG_THANH_CONG_LOGIN_TU_DONG:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Thành công!");
    		builder.setIcon(R.drawable.icon_success);
            builder.setMessage("Lấy lại mật khẩu thành công.\n Bạn có muốn đăng nhập tự động? ")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	Intent i = new Intent(getApplicationContext(), MainActivity.class);
					i.putExtra(Constant.KEYWORD_USER_TYPE, usertype );
					i.putExtra(Constant.KEYWORD_USER_ACTIVE, active );
					i.putExtra(Constant.KEYWORD_USER_NAME, name );
					i.putExtra(Constant.KEYWORD_USER_ID, usertype );
					startActivity(i);
					finish();
                }
            });
            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                        }
                    });
            dialog = builder.create();
            break;*/
        default:
            dialog = null;
        }
        return dialog;
	}
	

	class QuenMatKhau_Process extends AsyncTask<String, String, String> 
	{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(QuenMatKhau.this);
			//pDialog.setTitle("Vui lòng đợi!");
			pDialog.setMessage("Đang xử lý ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			name = ed_name.getText().toString();
			email = ed_email.getText().toString();
			//cau_hoi_bi_mat = sp_cau_hoi_bi_mat.getSelectedItem().toString();
			cau_tra_loi_bi_mat = ed_cau_tra_loi_bi_mat.getText().toString();
			

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("cau_hoi_bi_mat", cau_hoi_bi_mat));
			params.add(new BasicNameValuePair("cau_tra_loi_bi_mat", cau_tra_loi_bi_mat));
			params.add(new BasicNameValuePair("request", "post_quen_mat_khau"));

			// getting JSON Object
			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER + "totnghiep_quen_mat_khau.php","POST", params);
			Log.d("chuỗi json nhận được", "là json_string========"+json_string);
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					success_kiem_tra = json.getInt(Constant.KEYWORD_SUCCESS);
					da_ton_tai_kiem_tra = json.getInt(Constant.KEYWORD_USERNAME_DA_TON_TAI);
					
					if (success_kiem_tra == 1 && da_ton_tai_kiem_tra == 1) 
					{
						email_khong_dung = json.getInt(Constant.KEYWORD_EMAIL_KHONGDUNG);
						cau_hoi_bi_mat_khong_dung = json.getInt(Constant.KEYWORD_CAUHOI_BIMAT_KHONGDUNG);
						cau_tra_loi_bi_mat_khong_dung = json.getInt(Constant.KEYWORD_CAUTRALOI_BIMAT_KHONGDUNG);
						if(email_khong_dung == 1)
						{
							email_khong_dung = 2;
						} else if(cau_hoi_bi_mat_khong_dung == 1)
							{
								cau_hoi_bi_mat_khong_dung = 2;
							} else if(cau_tra_loi_bi_mat_khong_dung == 1)
								{
									cau_tra_loi_bi_mat_khong_dung=2;
								} else if(email_khong_dung == 0 && cau_hoi_bi_mat_khong_dung == 0
										&& cau_tra_loi_bi_mat_khong_dung == 0)
									{
						
										iduser 		= json.getInt(Constant.KEYWORD_USER_ID);
										username 	= json.getString(Constant.KEYWORD_USER_NAME);
										hovaten 	= json.getString(Constant.KEYWORD_HOVATEN);
										ngaysinh 	= json.getString(Constant.KEYWORD_NGAYSINH);
										gioitinh 	= json.getString(Constant.KEYWORD_GIOITINH);
										diachi 		= json.getString(Constant.KEYWORD_DIACHI);
										email 		= json.getString(Constant.KEYWORD_EMAIL);
										sdt 		= json.getString(Constant.KEYWORD_SDT);
										usertype    = json.getInt(Constant.KEYWORD_USER_TYPE);
										active		= json.getInt(Constant.KEYWORD_USER_ACTIVE);
										link_hinhdaidien 	= json.getString(Constant.KEYWORD_LINK_HINHDAIDIEN);
										mat_khau_moi = json.getString(Constant.KEYWORD_PASSWORD_NEW);
									}

					} else 
						{
							
						}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else
				{
					success_kiem_tra = 3; //timeout json trả về null
				}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
			if(success_kiem_tra==3) showDialog(DIALOG_TIME_ERROR);//timeout
			else //k có username này
				if(success_kiem_tra==0 && da_ton_tai_kiem_tra == 0) showDialog(DIALOG_USERNAME_KHONG_TON_TAI);
				else 
					if(email_khong_dung==2) showDialog(DIALOG_EMAIL_KHONG_DUNG);
					else 
						if(cau_hoi_bi_mat_khong_dung==2) showDialog(DIALOG_CAUHOI_BIMAT_KHONGDUNG);
						else 
							if(cau_tra_loi_bi_mat_khong_dung==2) showDialog(DIALOG_CAUTRALOI_BIMAT_KHONGDUNG);
							else 
								if(success_kiem_tra ==1 && da_ton_tai_kiem_tra ==1 && email_khong_dung == 0
								&& cau_hoi_bi_mat_khong_dung == 0 && cau_tra_loi_bi_mat_khong_dung == 0)
									taoDialog(mat_khau_moi);
				//Toast.makeText(getBaseContext(), "Error",Toast.LENGTH_LONG).show();
		}

	}
}
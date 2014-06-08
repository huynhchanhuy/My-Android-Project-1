package com.example.doan_didong;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


public class RegisterActivity extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;
	private FunctionCheck instantFunctionCheck;
	private EmailValidator emailValidator;
	private boolean validFormatEmail=false;

	JSONParser jsonParser = new JSONParser();
	EditText ed_name;
	EditText ed_hovaten;
	RadioGroup radGrp;
	EditText ed_pass;
	EditText ed_repeat_pass;
	EditText ed_diachi;
	EditText ed_email;
	EditText ed_sdt;
	Spinner sp_cau_hoi_bi_mat;
	EditText ed_cau_tra_loi_bi_mat;
	
	public int iduser;
	public String username;
	public String hovaten;
	public String ngaysinh= "chuachon";
	public String gioitinh="Nam";
	public String diachi;
	public String pass;
	String repeat_pass;
	String email;
	String sdt;
	public int usertype;
	public int active;
	public String cau_hoi_bi_mat = "Tên con vật bạn yêu thích nhất?";
	public String cau_tra_loi_bi_mat;
	public String link_hinhdaidien;
	
	int da_ton_tai;
	int success;
	
	Button btn_dang_ki;
	Button btn_ngaysinh;
	
	public  String  year,month,day;  
	 // declare  the variables to Show/Set the date and time when Time and  Date Picker Dialog first appears
	private int mYear, mMonth, mDay;
	
	private static final int DIALOG_REGISTER_REQUIRE = 1; //không được để trống
	private static final int DIALOG_PASSWORD_6_KI_TU = 2; //mật khẩu ít nhất 6 kí tự
	private static final int DIALOG_PASSWORD_REPEAT = 3; //mật khẩu lặp lại không đúng
	private static final int DIALOG_EMAIL_ERROR = 4;     // email không đúng định dạng
	
	private static final int DIALOG_NO_INTERNET = 5;  // không có kết nối internet
	private static final int DIALOG_TIME_ERROR = 6;		  //lỗi đăng kí không thành công
	
	private static final int DIALOG_USERNAME_6_KI_TU = 7; //username ít nhất 6 kí tự
	
	private static final int DIALOG_USERNAME_DA_TON_TAI = 8;
	private static final int DIALOG_THANH_CONG_LOGIN_TU_DONG = 9;
	
	private static final int DIALOG_DATE_ID_PICKER = 10;
	private static final int DIALOG_CHON_NGAY_SINH = 11;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		//set không cho xoay màn hình
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				
		//tạo đối tượng kiểm tra kết nối internet
		instantFunctionCheck = new FunctionCheck(getApplicationContext());
		
		final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
				
		// Edit Text
		ed_name = (EditText) findViewById(R.id.ed_username);
		ed_hovaten = (EditText) findViewById(R.id.ed_hovaten);
		ed_diachi  = (EditText) findViewById(R.id.ed_diachi);
		ed_pass = (EditText) findViewById(R.id.ed_password);
		ed_repeat_pass = (EditText) findViewById(R.id.ed_password_repeat);
		ed_email = (EditText) findViewById(R.id.ed_email);
		ed_sdt = (EditText) findViewById(R.id.ed_sdt);
		sp_cau_hoi_bi_mat = (Spinner) findViewById(R.id.cau_hoi_bi_mat);
		ed_cau_tra_loi_bi_mat = (EditText) findViewById(R.id.ed_tra_loi_bi_mat);
		
		radGrp = (RadioGroup) findViewById(R.id.radio_gioitinh);
		int checkedRadioButtonID = radGrp.getCheckedRadioButtonId();
		radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
		      public void onCheckedChanged(RadioGroup arg0, int id) {
		        switch (id) {
		        case R.id.radio_nam:
		          gioitinh = "Nam";
		          break;
		        case R.id.radio_nu:
		          gioitinh = "Nữ";
		          break;
		        default:
		          break;
		        }
		        //Toast.makeText(getApplicationContext(), gioitinh, Toast.LENGTH_SHORT).show();
		    } 
		});
		
		btn_ngaysinh = (Button) findViewById(R.id.btn_ngaysinh);
		btn_ngaysinh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_DATE_ID_PICKER);
				
			}
		});

		btn_dang_ki = (Button) findViewById(R.id.btn_register_submit);
		btn_dang_ki.setOnClickListener(new View.OnClickListener() {

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
				if(ed_name.length()==0 || ed_hovaten.length()==0 || ed_pass.length()==0 || ed_repeat_pass.length()==0 || ed_email.length()==0 || 
						ed_sdt.length()==0 || ed_cau_tra_loi_bi_mat.length()==0 || ed_diachi.length()==0)
				{
					so_luong_loi++;
					showDialog(DIALOG_REGISTER_REQUIRE);
					return;
				}
				if(ed_name.length() < 6 || ed_name.length() > 20 )
				{
					so_luong_loi++;
					showDialog(DIALOG_USERNAME_6_KI_TU);
					return;
				}
				if(ed_pass.length() < 6 || ed_repeat_pass.length() < 6 || ed_pass.length() > 20 || ed_repeat_pass.length() > 20 )
				{
					so_luong_loi++;
					showDialog(DIALOG_PASSWORD_6_KI_TU);
					return;
				}
				if(!ed_pass.getText().toString().equals(ed_repeat_pass.getText().toString()) )
				{
					so_luong_loi++;
					showDialog(DIALOG_PASSWORD_REPEAT);
					return;
				}
				if(ngaysinh.equals("chuachon") )
				{
					so_luong_loi++;
					showDialog(DIALOG_CHON_NGAY_SINH);
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
				if(so_luong_loi == 0) new RegisterAccount().execute();
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
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
		 });
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
        // the callback received when the user "sets" the Date in the DatePickerDialog
                public void onDateSet(DatePicker view, int yearSelected,
                                      int monthOfYear, int dayOfMonth) {
                   year = String.valueOf(yearSelected);
                   if( (monthOfYear+1) <10) month = '0'+ String.valueOf(monthOfYear+1);
                   	else if( monthOfYear == 9) month = String.valueOf(monthOfYear+1); 
                   		else month = String.valueOf(monthOfYear+1);
                   if(dayOfMonth < 10 ) day='0'+ String.valueOf(dayOfMonth);
                   	else day = String.valueOf(dayOfMonth);
                   // Set the Selected Date in Select date Button
                   btn_ngaysinh.setText("Ngày sinh: "+day+"-"+month+"-"+year);
                   ngaysinh=day+"-"+month+"-"+year;
                   //Toast.makeText(getApplicationContext(), ngaysinh, Toast.LENGTH_SHORT).show();
                }
            };
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
        AlertDialog.Builder builder;
        switch(id) {
        
        case DIALOG_CHON_NGAY_SINH:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Cảnh báo!");
    		builder.setIcon(R.drawable.icon_canh_bao);
            builder.setMessage("Chưa chọn ngày sinh!")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do something here
                }
            });
            dialog = builder.create();
            break;
        case DIALOG_DATE_ID_PICKER:
            return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        
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
        case DIALOG_REGISTER_REQUIRE:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Cảnh báo!");
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
        case DIALOG_PASSWORD_6_KI_TU:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Cảnh báo!");
    		builder.setIcon(R.drawable.icon_canh_bao);
            builder.setMessage("Tên đăng nhập và mật khẩu từ 6 đến 20 kí tự!")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do something here
                }
            });
            dialog = builder.create();
            break;
        case DIALOG_PASSWORD_REPEAT:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Cảnh báo!");
    		builder.setIcon(R.drawable.icon_canh_bao);
            builder.setMessage("Mật khẩu lặp lại không đúng!")
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
            builder.setMessage("Đăng kí không thành công. Vui lòng thử lại !")
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
            builder.setMessage("Tên đăng nhập và mật khẩu từ 6 đến 20 kí tự!")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do something here
                }
            });
            dialog = builder.create();
            break;
        case DIALOG_USERNAME_DA_TON_TAI:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Lỗi!");
    		builder.setIcon(R.drawable.icon_stop);
            builder.setMessage("Tên đăng nhập này đã tồn tại!")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do something here
                }
            });
            dialog = builder.create();
            break;
        case DIALOG_THANH_CONG_LOGIN_TU_DONG:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Thành công!");
    		builder.setIcon(R.drawable.icon_success);
            builder.setMessage("Đăng kí tài khoản thành công.\n Bạn có muốn đăng nhập tự động? ")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	Intent i = new Intent(RegisterActivity.this, com.example.doan_didong.MainActivity.class);
					
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
            break;
        default:
            dialog = null;
        }
        return dialog;
	}
	
	/**
	 * Background Async Task to Create new product
	 * */
	class RegisterAccount extends AsyncTask<String, String, String> 
	{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisterActivity.this);
			pDialog.setMessage("Đang tạo tài khoản ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating 
		 * */
		protected String doInBackground(String... args) {
			username = ed_name.getText().toString();
			hovaten = ed_hovaten.getText().toString();
			pass = ed_pass.getText().toString();
			repeat_pass = ed_repeat_pass.getText().toString();
			diachi = ed_diachi.getText().toString();
			email = ed_email.getText().toString();
			sdt = ed_sdt.getText().toString();
			//cau_hoi_bi_mat = sp_cau_hoi_bi_mat.getSelectedItem().toString();
			cau_tra_loi_bi_mat = ed_cau_tra_loi_bi_mat.getText().toString();
			

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("hovaten", hovaten));
			params.add(new BasicNameValuePair("ngaysinh", ngaysinh));
			params.add(new BasicNameValuePair("gioitinh", gioitinh));
			params.add(new BasicNameValuePair("diachi", diachi));
			params.add(new BasicNameValuePair("pass", pass));
			params.add(new BasicNameValuePair("repeat_pass", repeat_pass));
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("sdt", sdt));
			params.add(new BasicNameValuePair("cau_hoi_bi_mat", cau_hoi_bi_mat));
			params.add(new BasicNameValuePair("cau_tra_loi_bi_mat", cau_tra_loi_bi_mat));
			params.add(new BasicNameValuePair("request", "post_register"));

			// getting JSON Object
			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER + "totnghiep_register.php","POST", params);
			Log.d("chuỗi json nhận được", "là json_string========"+json_string);
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					success = json.getInt(Constant.KEYWORD_SUCCESS);
					da_ton_tai = json.getInt(Constant.KEYWORD_USERNAME_DA_TON_TAI);
					
					if (success == 1) 
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
						link_hinhdaidien = json.getString(Constant.KEYWORD_LINK_HINHDAIDIEN);

					} else 
						{
							
						}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else
				{
					success = 3; //timeout json trả về null
				}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
			
			if(success == 1)
			{
		        /*AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
		    	builder.setTitle("Đăng kí thành công!");
		    	builder.setMessage("Đăng kí thành công.\n Bạn có muốn đăng nhập tự động?");
		    	builder.setCancelable(false);
		    	builder.setIcon(R.drawable.icon_success);
		    	builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
		    	    public void onClick(DialogInterface dialog, int which) 
		    	    {			      	
		    	    	//dialog.dismiss();
		    	    	Intent i = new Intent(getApplicationContext(), MainActivity.class);
						i.putExtra(Constant.KEYWORD_USER_TYPE, usertype );
						i.putExtra(Constant.KEYWORD_USER_ACTIVE, active );
						i.putExtra(Constant.KEYWORD_USER_NAME, name );
						i.putExtra(Constant.KEYWORD_USER_ID, usertype );
						startActivity(i);
						finish();
		    	    }
		    	});
		    	builder.setNegativeButton("Không",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.dismiss();
                            }
                        });
                builder.create();*/
				showDialog(DIALOG_THANH_CONG_LOGIN_TU_DONG);
		    	
			}
			
			else if(success==0 && da_ton_tai == 1) showDialog(DIALOG_USERNAME_DA_TON_TAI);
			else if(success==3) showDialog(DIALOG_TIME_ERROR);
				//Toast.makeText(getBaseContext(), "Error",Toast.LENGTH_LONG).show();
		}

	}
}
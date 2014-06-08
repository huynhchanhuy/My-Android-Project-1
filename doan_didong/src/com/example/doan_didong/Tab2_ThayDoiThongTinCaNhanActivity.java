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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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


public class Tab2_ThayDoiThongTinCaNhanActivity extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;
	private FunctionCheck instantFunctionCheck;
	private EmailValidator emailValidator;
	private boolean validFormatEmail=false;
	Bundle b;
	public int success = 0;
	public int da_ton_tai = 0;

	JSONParser jsonParser = new JSONParser();
	EditText ed_thaydoi_hovaten;
	RadioGroup radGrp; RadioButton _nam; RadioButton _nu;
	EditText ed_thaydoi_diachi;
	EditText ed_thaydoi_email;
	EditText ed_thaydoi_sdt;
	
	public int iduser;
	public String username;
	public String hovaten;
	public String ngaysinh;  boolean chon_ngay_sinh = false;
	public String gioitinh;	 public String gioitinh_moi="";
	public String diachi;
	public String email;
	public String sdt;
	public int usertype;
	public int active;
	public String link_hinhdaidien;
	
	public  String  year,month,day;
	private int mYear, mMonth, mDay;
	
	Button btn_thaydoi_thongtincanhan;
	Button btn_thaydoi_ngaysinh;
	
	private static final int DIALOG_CHANGE_REQUIRE = 1; //không được để trống
	
	private static final int DIALOG_EMAIL_FORMAT = 2;     // email không đúng định dạng
	
	private static final int DIALOG_NO_INTERNET = 3;  // không có kết nối internet
	private static final int DIALOG_TIME_OUT = 4;		  //lỗi đăng kí không thành công timeout
	private static final int DIALOG_CHANGE_THANH_CONG = 5;
	
	private static final int DIALOG_DATE_ID_PICKER = 6;
	private static final int DIALOG_ERROR = 7;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab2_thaydoi_thongtincanhan);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//set không cho xoay màn hình
		instantFunctionCheck = new FunctionCheck(getApplicationContext());//tạo đối tượng kiểm tra kết nối internet
		
		b = getIntent().getExtras();
		
		iduser 		= b.getInt(Constant.KEYWORD_USER_ID);
		username 	= b.getString(Constant.KEYWORD_USER_NAME);
		hovaten 	= b.getString(Constant.KEYWORD_HOVATEN);
		ngaysinh 	= b.getString(Constant.KEYWORD_NGAYSINH);
		gioitinh 	= b.getString(Constant.KEYWORD_GIOITINH);
		diachi 		= b.getString(Constant.KEYWORD_DIACHI);
		email 		= b.getString(Constant.KEYWORD_EMAIL);
		sdt 		= b.getString(Constant.KEYWORD_SDT);
		usertype    = b.getInt(Constant.KEYWORD_USER_TYPE);
		active		= b.getInt(Constant.KEYWORD_USER_ACTIVE);
		link_hinhdaidien 	= b.getString(Constant.KEYWORD_LINK_HINHDAIDIEN);
		
		//Toast.makeText(getApplicationContext(), iduser + username + hovaten + ngaysinh+ gioitinh + diachi +sdt, Toast.LENGTH_LONG).show();
		
		final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        
		ed_thaydoi_hovaten = (EditText) findViewById(R.id.ed_thaydoi_hovaten);
			ed_thaydoi_hovaten.setText(hovaten);
		ed_thaydoi_diachi = (EditText) findViewById(R.id.ed_thaydoi_diachi);
			ed_thaydoi_diachi.setText(diachi);
		ed_thaydoi_email = (EditText) findViewById(R.id.ed_thaydoi_email);
			ed_thaydoi_email.setText(email);
		ed_thaydoi_sdt = (EditText) findViewById(R.id.ed_thaydoi_sdt);
			ed_thaydoi_sdt.setText(sdt);
		TextView txtNgaysinh = (TextView) findViewById(R.id.hien_thi_ngay_sinh);
			txtNgaysinh.setText("Ngày sinh: "+ngaysinh);
		
		btn_thaydoi_ngaysinh = (Button) findViewById(R.id.btn_thaydoi_ngaysinh);
		btn_thaydoi_ngaysinh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_DATE_ID_PICKER);
				
			}
		});
		
		radGrp = (RadioGroup) findViewById(R.id.radiogroup_thaydoi_gioitinh);
		_nam = (RadioButton) findViewById(R.id.radio_thaydoi_nam);
		_nu  = (RadioButton) findViewById(R.id.radio_thaydoi_nu);
		if(gioitinh.equals("Nam".toString())) _nam.setChecked(true); else _nu.setChecked(true);
		int checkedRadioButtonID = radGrp.getCheckedRadioButtonId();
		radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
		      public void onCheckedChanged(RadioGroup arg0, int id) {
		        switch (id) {
		        case R.id.radio_thaydoi_nam:
		        	gioitinh_moi = "Nam";
		        	//Toast.makeText(getApplicationContext(), "gioitinh cũ: "+ gioitinh + "\n gioitinh mới: "+gioitinh_moi, Toast.LENGTH_LONG).show();
		        	break;
		        case R.id.radio_thaydoi_nu:
		        	gioitinh_moi = "Nữ";
		        	//Toast.makeText(getApplicationContext(), "gioitinh cũ: "+ gioitinh + "\n gioitinh mới: "+gioitinh_moi, Toast.LENGTH_LONG).show();
		        	break;
		        default:
		          break;
		        }
		    } 
		});


		btn_thaydoi_thongtincanhan = (Button) findViewById(R.id.btn_thaydoi_thongtincanhan);
		btn_thaydoi_thongtincanhan.setOnClickListener(new View.OnClickListener() {

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
				if( ed_thaydoi_hovaten.length()==0 || ed_thaydoi_email.length()==0 || 
						ed_thaydoi_diachi.length()==0 || ed_thaydoi_sdt.length()==0)
				{
					so_luong_loi++;
					showDialog(DIALOG_CHANGE_REQUIRE);
					return;
				}
				if(ed_thaydoi_email.length()!=0)
				{
					emailValidator = new EmailValidator();
					validFormatEmail=emailValidator.validate(ed_thaydoi_email.getText().toString());
					if(!validFormatEmail){
						so_luong_loi++;
						showDialog(DIALOG_EMAIL_FORMAT);
						return;
					}
				}
				if(so_luong_loi == 0) new XacNhanThayDoiProgress().execute();
				else showDialog(DIALOG_ERROR);
				//Toast.makeText(getBaseContext(), "Please input all information!",Toast.LENGTH_LONG).show();
				
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
        builder.setMessage(message + "\n Click OK để quay lại trang thông tin cá nhân.")
        .setCancelable(false)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	
            	Intent data = new Intent();
				
				data.putExtra("iduser",iduser);
				data.putExtra("username",username);
                data.putExtra("hovaten",hovaten);
                data.putExtra("ngaysinh",ngaysinh);
                data.putExtra("gioitinh",gioitinh);
                data.putExtra("diachi",diachi);
                data.putExtra("email",email);
                data.putExtra("sdt",sdt);
                data.putExtra("usertype",usertype);
                data.putExtra("active",active);
                data.putExtra("link_hinhdaidien",link_hinhdaidien);
               
                setResult(RESULT_OK, data);
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
	public void onBackPressed() {
		//super.onBackPressed();
		Log.d("click back","success="+success +" da_ton_tai="+da_ton_tai);
		if(success == 1 && da_ton_tai == 1)
		{
			Intent data = new Intent();
			
			data.putExtra("iduser",iduser);
			data.putExtra("username",username);
            data.putExtra("hovaten",hovaten);
            data.putExtra("ngaysinh",ngaysinh);
            data.putExtra("gioitinh",gioitinh);
            data.putExtra("diachi",diachi);
            data.putExtra("email",email);
            data.putExtra("sdt",sdt);
            data.putExtra("usertype",usertype);
            data.putExtra("active",active);
            data.putExtra("link_hinhdaidien",link_hinhdaidien);
           
            setResult(RESULT_OK, data);
            finish();
		} 
		if(	success == 0  && da_ton_tai == 0 ) 
		{
			Intent data = new Intent();       
	        setResult(RESULT_CANCELED, data);
	        finish();
		}
		if(	success == 3 ) 
		{
			Intent data = new Intent();       
	        setResult(RESULT_CANCELED, data);
	        finish();
		}
		Display display = ((WindowManager) 
		getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_down);
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int yearSelected, int monthOfYear, int dayOfMonth) {
                   year = String.valueOf(yearSelected);
                   if( (monthOfYear+1) <10) month = '0'+ String.valueOf(monthOfYear+1);
                   	else if( monthOfYear == 9) month = String.valueOf(monthOfYear+1); 
                   		else month = String.valueOf(monthOfYear+1);
                   if(dayOfMonth < 10 ) day='0'+ String.valueOf(dayOfMonth);
                   	else day = String.valueOf(dayOfMonth);
                   
                   btn_thaydoi_ngaysinh.setText(day+"-"+month+"-"+year);
                   chon_ngay_sinh = true;
                }
            };
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
        AlertDialog.Builder builder;
        switch(id) {
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
                public void onClick(DialogInterface dialog, int id) {}
            });
            dialog = builder.create();
            break;
        case DIALOG_CHANGE_REQUIRE:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Cảnh báo!");
    		builder.setIcon(R.drawable.icon_canh_bao);
            builder.setMessage("Không được để trống!")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {}
            });
            dialog = builder.create();
            break;
        case DIALOG_EMAIL_FORMAT:
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
        case DIALOG_TIME_OUT:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Timeout !");
    		builder.setIcon(R.drawable.icon_co_loi);
            builder.setMessage("Thay đổi không thành công. Kiểm tra lại internet!")
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
            builder.setTitle("Có lỗi!");
    		builder.setIcon(R.drawable.icon_co_loi);
            builder.setMessage("Thay đổi không thành công. Vui lòng thử lại!")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do something here
                }
            });
            dialog = builder.create();
            break;
        case DIALOG_CHANGE_THANH_CONG:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Thành công !");
    		builder.setIcon(R.drawable.icon_success);
            builder.setMessage("Thay đổi thành công.")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Do something here
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
	class XacNhanThayDoiProgress extends AsyncTask<String, String, String> 
	{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Tab2_ThayDoiThongTinCaNhanActivity.this);
			pDialog.setMessage("Đang xử lý ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		protected String doInBackground(String... args) {
			
			hovaten = ed_thaydoi_hovaten.getText().toString();
			if(chon_ngay_sinh == true) ngaysinh = btn_thaydoi_ngaysinh.getText().toString();
			if(!gioitinh_moi.equals("")) gioitinh=gioitinh_moi;
			diachi = ed_thaydoi_diachi.getText().toString();
			email = ed_thaydoi_email.getText().toString();
			sdt = ed_thaydoi_sdt.getText().toString();
			

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("hovaten", hovaten));
			params.add(new BasicNameValuePair("ngaysinh", ngaysinh));
			params.add(new BasicNameValuePair("gioitinh", gioitinh));
			params.add(new BasicNameValuePair("diachi", diachi));
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("sdt", sdt));
			params.add(new BasicNameValuePair("request", "post_thaydoi_thongtincanhan"));

			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER + "totnghiep_thaydoi_thongtincanhan.php","POST", params);
			
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					success = json.getInt(Constant.KEYWORD_SUCCESS);
					da_ton_tai = json.getInt(Constant.KEYWORD_USERNAME_DA_TON_TAI);
					
					if (success == 1 && da_ton_tai == 1)
					{
					} else {}
						
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
		protected void onPostExecute(String file_url) 
		{
			pDialog.dismiss();
			
			if(success == 1 && da_ton_tai ==1)
			{
		        taoDialog("Thay đổi thông tin thành công.");
			}
			else if(success==0 || da_ton_tai == 0) showDialog(DIALOG_ERROR);
			else if(success==3) showDialog(DIALOG_TIME_OUT);
				//Toast.makeText(getBaseContext(), "Error",Toast.LENGTH_LONG).show();
		}

	}
}
package com.example.doan_didong;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.doan_didong.Tab2_ThayDoiThongTinCaNhanActivity.XacNhanThayDoiProgress;

public class Tab2_ThayDoiMatKhauActivity extends Activity {

	private ProgressDialog pDialog;
	private FunctionCheck instantFunctionCheck;
	Bundle b;
	public int success = 0;
	public int da_ton_tai = 0;
	int _thaydoibimat=0;

	JSONParser jsonParser = new JSONParser();
	TextView tab2_cauhoibimat_cu; 		String tab2_cauhoibimat_cu1="";
	TextView tab2_cautraloibimat_cu; 	String tab2_cautraloibimat_cu1="";
	EditText tab2_matkhau_cu;
	EditText tab2_matkhau_moi;
	EditText tab2_matkhau_moi_repeat;
	Spinner tab2_cauhoi_bimat_moi;
	EditText tab2_cau_traloi_bimat_moi;
	Button tab2_btn_thaydoi_matkhau;
	TextView tab2_text_chon_cau_hoi_bi_mat_moi;
	CheckBox tab2_checkbox_change_bimat;
	
	public int iduser;
	public String username;
	public String password;
	
	public String cau_hoi_bi_mat;
	public String cau_hoi_bi_mat_moi="Tên con vật bạn yêu thích nhất?";
	public String tra_loi_bi_mat;
	
	
//	public  String  year,month,day;
//	private int mYear, mMonth, mDay;
	
	
//	Button btn_thaydoi_ngaysinh;
	
	private static final int DIALOG_CHANGE_REQUIRE = 1; //không được để trống
	
	private static final int DIALOG_EMAIL_FORMAT = 2;     // email không đúng định dạng
	
	private static final int DIALOG_NO_INTERNET = 3;  // không có kết nối internet
	private static final int DIALOG_TIME_OUT = 4;		  //lỗi đăng kí không thành công timeout
	private static final int DIALOG_CHANGE_THANH_CONG = 5;
	
	private static final int DIALOG_DATE_ID_PICKER = 6;
	private static final int DIALOG_ERROR = 7; 
	private static final int DIALOG_PASS_CU_KHONG_DUNG = 8;
	private static final int DIALOG_PASS_MOI_TU_6_20 = 9;
	private static final int DIALOG_PASS_REPEAT_ERROR = 10;
	private static final int DIALOG_2_PASS_BANG_NHAU = 11;
	private static final int DIALOG_CHUA_LOAD_DU_LIEU = 12;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab2_thaydoi_matkhau);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//set không cho xoay màn hình
		instantFunctionCheck = new FunctionCheck(getApplicationContext());//tạo đối tượng kiểm tra kết nối internet
		
		b = getIntent().getExtras();
		
		iduser 		= b.getInt(Constant.KEYWORD_USER_ID);
		username 	= b.getString(Constant.KEYWORD_USER_NAME);
        
		tab2_cauhoibimat_cu = (TextView) findViewById(R.id.tab2_cauhoibimat_cu);
		tab2_cautraloibimat_cu = (TextView) findViewById(R.id.tab2_cautraloibimat_cu);
		tab2_matkhau_cu = (EditText) findViewById(R.id.tab2_matkhau_cu);
		tab2_matkhau_moi = (EditText) findViewById(R.id.tab2_matkhau_moi);
		tab2_matkhau_moi_repeat = (EditText) findViewById(R.id.tab2_matkhau_moi_repeat);
		tab2_cauhoi_bimat_moi = (Spinner) findViewById(R.id.tab2_cauhoi_bimat_moi);
		tab2_cau_traloi_bimat_moi = (EditText) findViewById(R.id.tab2_cau_traloi_bimat_moi);
		tab2_text_chon_cau_hoi_bi_mat_moi = (TextView)findViewById(R.id.tab2_text_chon_cau_hoi_bi_mat_moi);
		tab2_checkbox_change_bimat = (CheckBox)findViewById(R.id.tab2_checkbox_change_bimat);
		tab2_checkbox_change_bimat.setChecked(false);
		tab2_checkbox_change_bimat.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				if(isChecked==true)
				{
					tab2_cauhoi_bimat_moi.setVisibility(View.VISIBLE);
					tab2_cau_traloi_bimat_moi.setVisibility(View.VISIBLE);
					tab2_text_chon_cau_hoi_bi_mat_moi.setVisibility(View.VISIBLE);
					_thaydoibimat=1;
				}
				else if(isChecked==false)
				{
					tab2_cauhoi_bimat_moi.setVisibility(View.GONE);
					tab2_cau_traloi_bimat_moi.setVisibility(View.GONE);
					tab2_text_chon_cau_hoi_bi_mat_moi.setVisibility(View.GONE);
					_thaydoibimat=0;
				}
			}
		});
		try{
			new GetInformationProgress().execute();
		} catch(Exception e) { e.printStackTrace(); }
		
		//tab2_cauhoibimat_cu.setText("Câu hỏi bí mật: "+ cau_hoi_bi_mat); 
		//tab2_cautraloibimat_cu.setText("Câu trả lời bí mật: "+tra_loi_bi_mat);		
		tab2_btn_thaydoi_matkhau = (Button) findViewById(R.id.tab2_btn_thaydoi_matkhau);
		tab2_btn_thaydoi_matkhau.setOnClickListener(new View.OnClickListener() {

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
				if( tab2_matkhau_cu.length()==0 || tab2_matkhau_moi.length()==0 || 
						tab2_matkhau_moi_repeat.length()==0 )
				{
					so_luong_loi++;
					showDialog(DIALOG_CHANGE_REQUIRE);
					return;
				}
				String md51 = md5(md5(tab2_matkhau_cu.getText().toString()));
				//Toast.makeText(getApplicationContext(), md51 +"=\n"+password, Toast.LENGTH_LONG).show();
				if(!md51.equals(password))
				{
					so_luong_loi++;
					showDialog(DIALOG_PASS_CU_KHONG_DUNG);
					return;
				}
				if(tab2_matkhau_moi.length() < 6 || tab2_matkhau_moi.length() >20 ||
						tab2_matkhau_moi_repeat.length() < 6 || tab2_matkhau_moi_repeat.length() >20 )
				{
					so_luong_loi++;
					showDialog(DIALOG_PASS_MOI_TU_6_20);
					return;
				}
				if(!tab2_matkhau_moi.getText().toString().equals(tab2_matkhau_moi_repeat.getText().toString()))
				{
					so_luong_loi++;
					showDialog(DIALOG_PASS_REPEAT_ERROR);
					return;
				}
				if(tab2_matkhau_moi.getText().toString().equals(tab2_matkhau_cu.getText().toString()))
				{
					so_luong_loi++;
					showDialog(DIALOG_2_PASS_BANG_NHAU);
					return;
				}
				if(tab2_cauhoibimat_cu1.toString().equals("") || tab2_cautraloibimat_cu1.toString().equals(""))
				{
					so_luong_loi++;
					taoDialog("Tải dữ liệu không thành công. Click OK thử lại.");
					return;
				}
				if( "".equals(tab2_cau_traloi_bimat_moi.getText().toString()) && _thaydoibimat==1)
				{
					so_luong_loi++;
					showDialog(13);
					return;
				}
				if(so_luong_loi == 0) new XacNhanThayDoiProgress().execute();
				else showDialog(DIALOG_ERROR);
				
			}
		});
		
		tab2_cauhoi_bimat_moi.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		 {
		                                                 
				@Override
				public void onItemSelected(AdapterView<?> arg0,View arg1,int arg2,long arg3) 
				{
					cau_hoi_bi_mat_moi = tab2_cauhoi_bimat_moi.getItemAtPosition(arg2).toString();
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
		 });
	}

	public static String md5(String input)
	{
	    String result = input;
	    if(input != null) {
	        try{
	        	MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
	        
	        md.update(input.getBytes());
	        BigInteger hash = new BigInteger(1, md.digest());
	        result = hash.toString(16);
	        while(result.length() < 32) { //40 for SHA-1
	            result = "0" + result;
	        }
	        }catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	    }
	    return result;
	}
	
	public void taoDialog(String message)
	{
		Dialog dialog;
        AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(this);
        builder.setTitle("Lỗi !");
		builder.setIcon(R.drawable.icon_stop);
        builder.setMessage(message)
        .setCancelable(false)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	try{
	        			if(instantFunctionCheck.checkInternetConnection())
	        			{
	        				new GetInformationProgress().execute();
	        			} else Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
        			} 
            		catch(Exception e) { e.printStackTrace(); }
            }
        });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        dialog = builder.create();
        dialog.show();
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.d("click back","success="+success +" da_ton_tai="+da_ton_tai);
		
		Display display = ((WindowManager) 
		getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_down);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
        AlertDialog.Builder builder;
        switch(id) {
        
        case 13:
        	builder = new AlertDialog.Builder(this);
            builder.setTitle("Cảnh báo!");
    		builder.setIcon(R.drawable.icon_canh_bao);
            builder.setMessage("Câu trả lời bí mật mới không được để trống.")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int id) 
                {}
            });
            dialog = builder.create();
            break;
        case DIALOG_CHUA_LOAD_DU_LIEU:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Lỗi!");
    		builder.setIcon(R.drawable.icon_canh_bao);
            builder.setMessage("Chưa tải dữ liệu xong.")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {}
            });
            dialog = builder.create();
            break;
        case DIALOG_2_PASS_BANG_NHAU:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Lỗi!");
    		builder.setIcon(R.drawable.icon_canh_bao);
            builder.setMessage("Mật khẩu mới phải khác mật khẩu cũ.")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {}
            });
            dialog = builder.create();
            break;
        case DIALOG_PASS_REPEAT_ERROR:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Lỗi!");
    		builder.setIcon(R.drawable.icon_stop);
            builder.setMessage("Mật khẩu lặp lại không đúng.")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {}
            });
            dialog = builder.create();
            break;
        
        case DIALOG_PASS_MOI_TU_6_20:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Lỗi!");
    		builder.setIcon(R.drawable.icon_stop);
            builder.setMessage("Mật khẩu mới từ 6 đến 20 kí tự.")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {}
            });
            dialog = builder.create();
            break;
        
        case DIALOG_PASS_CU_KHONG_DUNG:
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Lỗi!");
    		builder.setIcon(R.drawable.icon_stop);
            builder.setMessage("Mật khẩu cũ không đúng.")
            .setCancelable(false)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {}
            });
            dialog = builder.create();
            break;
            
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
		int success1 = 0;
		int da_ton_tai1 = 0;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Tab2_ThayDoiMatKhauActivity.this);
			pDialog.setMessage("Đang xử lý ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		protected String doInBackground(String... args) 
		{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if(_thaydoibimat==1)
			{
				
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("passwordmoi", tab2_matkhau_moi.getText().toString()));
				params.add(new BasicNameValuePair("cauhoibimatmoi", cau_hoi_bi_mat_moi));
				params.add(new BasicNameValuePair("cautraloibimatmoi", tab2_cau_traloi_bimat_moi.getText().toString()));
				params.add(new BasicNameValuePair("request", "post_thaydoi_matkhau"));
			}
			else if(_thaydoibimat==0)
			{
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("passwordmoi", tab2_matkhau_moi.getText().toString()));
				params.add(new BasicNameValuePair("cauhoibimatmoi", ""));
				params.add(new BasicNameValuePair("cautraloibimatmoi", ""));
				params.add(new BasicNameValuePair("request", "post_thaydoi_matkhau"));
			}
			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER + "totnghiep_thaydoi_matkhau.php","POST", params);
			
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					success1 = json.getInt(Constant.KEYWORD_SUCCESS);
					da_ton_tai1 = json.getInt(Constant.KEYWORD_USERNAME_DA_TON_TAI);
					
					if (success1 == 1 && da_ton_tai1 == 1)
					{
					} else {}
						
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else
				{
					success1 = 3; //timeout json trả về null
				}
			return null;
		}

		protected void onPostExecute(String file_url) 
		{
			pDialog.dismiss();
			
			if(success1 == 1 && da_ton_tai1 == 1)
			{
				showDialog(DIALOG_CHANGE_THANH_CONG);
			}
			else if(success1==0 || da_ton_tai1 == 0) showDialog(DIALOG_ERROR);
			else if(success1==3) showDialog(DIALOG_TIME_OUT);
				//Toast.makeText(getBaseContext(), "Error",Toast.LENGTH_LONG).show();
		}

	}
	
	
	
	
	
	
	
	
	
	
	
	class GetInformationProgress extends AsyncTask<String, String, String> 
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Tab2_ThayDoiMatKhauActivity.this);
			pDialog.setMessage("Đang tải dữ liệu ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		protected String doInBackground(String... args) {
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("request", "post_laythongtincanhan"));

			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER + "totnghiep_laythongtincanhan.php","POST", params);
			
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					success = json.getInt(Constant.KEYWORD_SUCCESS);
					da_ton_tai = json.getInt(Constant.KEYWORD_USERNAME_DA_TON_TAI);
					
					if (success == 1 && da_ton_tai == 1)
					{
						password = json.getString(Constant.KEYWORD_PASSWORD);
						cau_hoi_bi_mat = json.getString(Constant.KEYWORD_CAU_HOI_BI_MAT);
						tra_loi_bi_mat = json.getString(Constant.KEYWORD_CAU_TRA_LOI_BI_MAT);
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

		protected void onPostExecute(String file_url) 
		{
			pDialog.dismiss();
			
			if(success == 1 && da_ton_tai ==1)
			{
				tab2_cauhoibimat_cu.setText("Câu hỏi bí mật: "+ cau_hoi_bi_mat); 
				tab2_cautraloibimat_cu.setText("Câu trả lời bí mật: "+tra_loi_bi_mat);
				tab2_cauhoibimat_cu1=cau_hoi_bi_mat;
				tab2_cautraloibimat_cu1=tra_loi_bi_mat;
				
				tab2_checkbox_change_bimat.setChecked(false);
				tab2_checkbox_change_bimat.setChecked(true);
				tab2_checkbox_change_bimat.setChecked(false);
			}
			if(success==0 || da_ton_tai == 0) taoDialog("Tải dữ liệu không thành công. Click OK thử lại.");
			if(success==3) taoDialog("Tải dữ liệu không thành công. Click OK thử lại.");
				//Toast.makeText(getBaseContext(), "Error",Toast.LENGTH_LONG).show();
		}

	}
}

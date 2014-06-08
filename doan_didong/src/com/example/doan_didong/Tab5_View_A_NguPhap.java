package com.example.doan_didong;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.doan_didong.Tab2_ThayDoiMatKhauActivity.GetInformationProgress;

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
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Tab5_View_A_NguPhap extends Activity 
{

	// Progress Dialog
	private ProgressDialog pDialog;
	private FunctionCheck instantFunctionCheck;
	JSONParser jsonParser = new JSONParser();
	
	Bundle b;
	public int iduser;
	public String idnguphap;
	public String luot_view;
	
	TextView txt_id;
	TextView txt_tieude;
	TextView txt_luot_view;
	TextView txt_noidung;
	TextView txt_click1;
	TextView txt_click2;
	ListView lv_listview;
	TextView txt_listview_more;
	EditText edit_noidung;
	Button btn_add_comment;
	
	private static final int DIALOG_CHANGE_REQUIRE = 1; //không được để trống
	
	private static final int DIALOG_EMAIL_FORMAT = 2;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab5_view_a_nguphap);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//set không cho xoay màn hình
		instantFunctionCheck = new FunctionCheck(getApplicationContext());//tạo đối tượng kiểm tra kết nối internet
		
		b = getIntent().getExtras();
		
		iduser 		= b.getInt("iduser");
		idnguphap 	= b.getString("idnguphap");
		luot_view 	= b.getString("luot_view");
//		Toast.makeText(getBaseContext(), String.valueOf(iduser) + idnguphap + luot_view, Toast.LENGTH_SHORT).show();
		
		txt_id = (TextView) findViewById(R.id.tab5_view_a_id);
		txt_tieude = (TextView) findViewById(R.id.tab5_view_a_tieude);
		txt_luot_view = (TextView) findViewById(R.id.tab5_view_a_luot_view);
		txt_noidung = (TextView) findViewById(R.id.tab5_view_a_noi_dung);
		/*txt_click1 =  (TextView) findViewById(R.id.tab5_view_a_click1);
		txt_click2 =  (TextView) findViewById(R.id.tab5_view_a_click2);
		lv_listview = (ListView) findViewById(R.id.tab5_view_a_listview);
		txt_listview_more =  (TextView) findViewById(R.id.tab5_view_a_listview_more);
		edit_noidung = (EditText)findViewById(R.id.tab5_view_a_add_noidung);
		btn_add_comment = (Button)findViewById(R.id.tab5_view_a_add_btn);*/
		try{
			if(instantFunctionCheck.checkInternetConnection())
			{
				new Load_A_NguPhapProgress().execute();
			}
			else Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
		} 
		catch(Exception e) { e.printStackTrace(); }
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
        					new Load_A_NguPhapProgress().execute();
            			}
        				else Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
        			} 
            		catch(Exception e) { e.printStackTrace(); }
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
		super.onBackPressed();
		/*Log.d("click back","success="+success +" da_ton_tai="+da_ton_tai);
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
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_down);*/
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
        AlertDialog.Builder builder;
        switch(id) {
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
       
        default:
            dialog = null;
        }
        return dialog;
	}
	
	
	
	/**
	 * 
	 * */
	class Load_A_NguPhapProgress extends AsyncTask<String, String, String> 
	{
		int success;
		String id;
		String tieude ;
		String noidung ;
		String luot_view ;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			success = 0;
			pDialog = new ProgressDialog(Tab5_View_A_NguPhap.this);
			pDialog.setMessage("Vui lòng đợi ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		protected String doInBackground(String... args) 
		{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("iduser", String.valueOf(iduser)));
			params.add(new BasicNameValuePair("idnguphap", idnguphap));
			params.add(new BasicNameValuePair("luot_view", luot_view));
			params.add(new BasicNameValuePair("request", "post_nguphap_view_one"));

			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER + "totnghiep_nguphap_view_one.php","POST", params);
			
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					success = json.getInt(Constant.KEYWORD_SUCCESS);
					
					if (success == 1 )
					{
						id = json.getString("id");
						tieude = json.getString("tieude");
						noidung = json.getString("noidung");
						luot_view = json.getString("luot_view");
					} else {}
						
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else { success = 3;  }
			return null;
		}
		protected void onPostExecute(String file_url)
		{
			pDialog.dismiss();
			
			if(success == 1 )
			{
		        txt_id.setText(id);
		        txt_tieude.setText(tieude);
		        txt_luot_view.setText("Lượt xem: "+luot_view + " lần ");
		        txt_noidung.setText(Html.fromHtml(noidung));
			}
			else if(success==0 ) taoDialog("Tải dữ liệu không thành công. Click OK thử lại.");
			else if(success==3) taoDialog("Tải dữ liệu không thành công. Click OK thử lại.");
		}

	}
}
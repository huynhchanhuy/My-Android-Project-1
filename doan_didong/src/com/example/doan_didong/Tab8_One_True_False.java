package com.example.doan_didong;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class Tab8_One_True_False extends Activity
{
	public static ArrayList<OneTracNghiem> itemListLevelQuestion; 	//arr list question 30 câu
	JSONParser jsonParser = new JSONParser();
	String json_string;
	int success;
	private FunctionCheck instantFunctionCheck;
	private ProgressDialog pDialog;
	
	String id_user;					//id của user
	String id_level;				//id level của loại trac nghiem
	String tong_socauhoi;			//tong số câu hỏi tracnghiem chọn   30 câu
	String so_thu_tu_cau;		//số thứ tự câu trong 30cau 5/30
	
	String[] array_cau_tra_loi;		//mảng[30] câu trả lời
	String[] array_a_b_c_d;		
	
	String question_id;	
	String doanvan;
	String question_text;			//
	String ans_a;
	String ans_b;
	String answer;
	
	String	ngay_thi;
	float 	diemso;
	int 	socaudung;
	boolean xemdapan;
	
	
	Button  btn_causau,
			btn_cautruoc,
			btn_choncau,
			btn_lamlai,
			btn_ketqua,
			btn_chualam,
			btn_xem_dap_an;
	TextView cau_hien_tai,		//textview show câu: 5/30
			 noi_dung_cau_hoi,	//textview noidung cau hoi
			 tv_dapan,
			 tv_doanvan;				
	TextView tv_username, tv_levelname;
	EditText choncau;			//edittext go đến câu khác
	
	RadioGroup radiogroup;
	RadioButton radio1,radio2,radio5;
	SaveKetQuaThi task_save_ketqua;
	LoadOneQuestion task_load_onequestion;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab8_one_true_false);
		itemListLevelQuestion 	= new ArrayList<Tab8_One_True_False.OneTracNghiem>();
		instantFunctionCheck = new FunctionCheck(getApplicationContext());//tạo đối tượng kiểm tra kết nối internet
		task_load_onequestion = new LoadOneQuestion();
		so_thu_tu_cau			="0";
		xemdapan 				= false;
		
		Bundle b = getIntent().getExtras();
		id_user = 		b.getString("id_user");
		id_level   = 	b.getString("id_level");
		tong_socauhoi=		b.getString("socauhoi");
		
		btn_causau=(Button)findViewById(R.id.tab8_true_false_btn_causau);
		btn_cautruoc=(Button)findViewById(R.id.tab8_true_false_btn_cautruoc);
		btn_choncau=(Button)findViewById(R.id.tab8_true_false_btn_choncau);
		btn_lamlai=(Button)findViewById(R.id.tab8_true_false_btn_lamlai);
		btn_ketqua=(Button)findViewById(R.id.tab8_true_false_btn_ketqua);
		btn_chualam=(Button)findViewById(R.id.tab8_true_false_btn_chualam);
		btn_xem_dap_an=(Button)findViewById(R.id.tab8_true_false_xem_dap_an);
		
		choncau=(EditText)findViewById(R.id.tab8_true_false_choncau);
		
		cau_hien_tai		=(TextView)findViewById(R.id.tab8_true_false_id_current);
		noi_dung_cau_hoi	=(TextView)findViewById(R.id.tab8_true_false_text);
		tv_doanvan 			=(TextView)findViewById(R.id.tab8_true_false_doanvan);
		tv_dapan			=(TextView)findViewById(R.id.tab8_true_false_dapan);
		tv_username	=(TextView)findViewById(R.id.tab8_true_false_username);
		tv_levelname=(TextView)findViewById(R.id.tab8_true_false_level_name);
		
		radiogroup=(RadioGroup)findViewById(R.id.tab8_true_false_radioGroup);
		radio1=(RadioButton)findViewById(R.id.tab8_true_false_radio1);
		radio2=(RadioButton)findViewById(R.id.tab8_true_false_radio2);
		radio5=(RadioButton)findViewById(R.id.tab8_true_false_radio5);	
		//
		//
		//
		array_cau_tra_loi     =new String[Integer.valueOf(tong_socauhoi)];
		array_a_b_c_d	  =new String[Integer.valueOf(tong_socauhoi)];
		for(int i=0;i<Integer.valueOf(tong_socauhoi); i++)
		{
			array_cau_tra_loi[i]="_";
		}
		//
		for(int i=0;i<Integer.valueOf(tong_socauhoi); i++)
		{
			array_a_b_c_d[i]="_";
		}
		//
		json_string="";
		json_string = FunctionFolderAndFile.readFromFile("tracnghiem", "level_true_false.txt");
		if(json_string.length() != 0)
		{
			JSONObject json;
			try {
				json = JSONParser.getJSONObjectFromJString(json_string);
				if (json.getInt("success") == 1 ) 
				{
					JSONArray jsonArray = json.getJSONArray("all_question"); 
					for (int i = 0; i < jsonArray.length(); i++) 
					{
						JSONObject c = jsonArray.getJSONObject(i);
						if(id_level.equals(c.getString("id")))
						{
							tv_levelname.setText(c.getString("title"));
							tv_doanvan.setText(c.getString("doanvan2"));
							tv_username.setText(MainActivity.username);
							break;
						}
					}
					jsonArray=null;
				} else {}
			} catch (JSONException e) 
			{
				e.printStackTrace();
			}
		} else {}
		//
		//đọc data từ file level1.txt
		//
		json_string="";
		json_string = FunctionFolderAndFile.readFromFile("tracnghiem", "level_question_true_false.txt");
		if(json_string.length() != 0)
		{
			JSONObject json;
			try {
				json = JSONParser.getJSONObjectFromJString(json_string);
				if (json.getInt("success") == 1) 
				{
					JSONArray jsonArray = json.getJSONArray("all_question"); 
					for (int i = 0; i < jsonArray.length(); i++) 
					{
						JSONObject c = jsonArray.getJSONObject(i);
						if(id_level.equals(c.getString("id_level")) )
						{
							String id1 = 	c.getString("id");
							String text1 = 	c.getString("cauhoi");
							String ans_a = 	c.getString("cau_a");
							String ans_b = 	c.getString("cau_b");
							String answer = 	c.getString("dapan");
							OneTracNghiem item = new OneTracNghiem(id1, text1, ans_a, ans_b, answer);
							itemListLevelQuestion.add(item);
						}
						
					}
					jsonArray=null;
				} else {}
			} catch (JSONException e) 
			{
				e.printStackTrace();
			}
		} else {}
		//
		//
		task_load_onequestion.execute();
		setradiocheck();
		//
		//
		//
		btn_chualam.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				String chualam="Câu chưa làm:\n";
				for(int i=0;i<Integer.valueOf(tong_socauhoi);i++)
				{
					if(array_cau_tra_loi[i].equals("_"))
						chualam=chualam + String.valueOf(i+1) + "\n";
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(Tab8_One_True_False.this);
				builder.setMessage(chualam)
				.setTitle("Câu chưa làm!")
				.setCancelable(false)
				.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
										
						dialog.cancel();
					}
				});
				builder.create().show();
				
			}
		});
		btn_cautruoc.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				if( Integer.valueOf(so_thu_tu_cau) <= 0  )
				{
					Toast.makeText(getBaseContext(), "Không có câu trước!", Toast.LENGTH_SHORT).show();
				} 
				else if(Integer.valueOf(so_thu_tu_cau) > 0)
				{
					so_thu_tu_cau=String.valueOf(Integer.valueOf(so_thu_tu_cau)-1);
					new LoadOneQuestion().execute();
					if("_".equals(array_a_b_c_d[Integer.valueOf(so_thu_tu_cau)]) )
					{
						setradiocheck();
					}
					else
					{
						setradiocheck_a_b_c_d( array_a_b_c_d[Integer.valueOf(so_thu_tu_cau)].toString() );
					}
				}
			}
		});
		btn_causau.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				
				if( Integer.valueOf(so_thu_tu_cau) >= (Integer.valueOf(tong_socauhoi)-1)  )
				{
					Toast.makeText(getBaseContext(), "Hết câu hỏi", Toast.LENGTH_SHORT).show();
				} 
				else if( Integer.valueOf(so_thu_tu_cau) < (Integer.valueOf(tong_socauhoi)-1)  )
				{
					so_thu_tu_cau=String.valueOf(Integer.valueOf(so_thu_tu_cau)+1);
					new LoadOneQuestion().execute();
					if("_".equals(array_a_b_c_d[Integer.valueOf(so_thu_tu_cau)]) )
					{
						setradiocheck();
					}
					else
					{
						setradiocheck_a_b_c_d( array_a_b_c_d[Integer.valueOf(so_thu_tu_cau)].toString() );
					}
				}
			}
		});
		btn_choncau.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				
				String cau=choncau.getText().toString();
				if(!"".equals(cau))
				{
					int temp=Integer.valueOf(cau);
					if( 0 <= (temp-1) && (temp-1) < Integer.valueOf(tong_socauhoi))
					{
						so_thu_tu_cau=String.valueOf(temp-1);
						new LoadOneQuestion().execute();
						if("_".equals(array_a_b_c_d[Integer.valueOf(so_thu_tu_cau)]) )
						{
							setradiocheck();
						}
						else
						{
							setradiocheck_a_b_c_d( array_a_b_c_d[Integer.valueOf(so_thu_tu_cau)].toString() );
						}
					}
					else
						Toast.makeText(getBaseContext(), "Câu bạn chọn không đúng", Toast.LENGTH_SHORT).show();
			
				}
			}
		});
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			String traloi_moicau = "";
			String true_false = ""; 
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				
				switch (checkedId) 
				{
					case R.id.tab8_true_false_radio1:
						traloi_moicau="a";
						true_false = "TRUE";
						break;
					case R.id.tab8_true_false_radio2:
						traloi_moicau="b";
						true_false = "FALSE";
						break;
					case R.id.tab8_true_false_radio5:
						traloi_moicau="";
						true_false = "";
						Log.e("---radio5 click Chưa chọn","---radio5 click  Chưa chọn");
						break;
					default:
						traloi_moicau="";
						true_false = "";
						Log.e("---radio5 click Chưa chọn","---radio5 click  Chưa chọn");
						break;
				}
				if(!"".equals( traloi_moicau.toString() ))
				{
					array_a_b_c_d[Integer.valueOf(so_thu_tu_cau)]= traloi_moicau.toString();
					
					if( true_false.equals(answer.toString().toUpperCase()))
					{	
						array_cau_tra_loi[Integer.valueOf(so_thu_tu_cau)]="1";
					}
					else
					{
						array_cau_tra_loi[Integer.valueOf(so_thu_tu_cau)]="0";
					}
					Log.e("----array_a_b_c_d["+so_thu_tu_cau+"]="+array_a_b_c_d[Integer.valueOf(so_thu_tu_cau)],array_a_b_c_d[Integer.valueOf(so_thu_tu_cau)]);
					Log.e("----array_cau_tra_loi["+so_thu_tu_cau+"]=",""+"dapan: "+array_cau_tra_loi[Integer.valueOf(so_thu_tu_cau)]);
				}
				else 
				{
					array_a_b_c_d[Integer.valueOf(so_thu_tu_cau)]="_";
					array_cau_tra_loi[Integer.valueOf(so_thu_tu_cau)]="_";
					Log.e("----array_a_b_c_d["+so_thu_tu_cau+"]="+array_a_b_c_d[Integer.valueOf(so_thu_tu_cau)],array_a_b_c_d[Integer.valueOf(so_thu_tu_cau)]);
					Log.e("----array_cau_tra_loi["+so_thu_tu_cau+"]=",""+"dapan: "+array_cau_tra_loi[Integer.valueOf(so_thu_tu_cau)]);
				}
			}
		});
		btn_lamlai.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(Tab8_One_True_False.this);
				builder.setMessage("Những câu trả lời của bạn sẽ bị mất. Bạn thực sự muốn làm lại ?")
				.setTitle("Bạn có chắc muốn làm lại?")
				.setCancelable(false)
				.setIcon(R.drawable.icon_canh_bao)
				.setNegativeButton("Làm lại", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						xemdapan=false;
						for(int i=0; i<Integer.valueOf(tong_socauhoi);i++)
						{
							array_cau_tra_loi[i]="_";
							array_a_b_c_d[i]="_";
						}
						so_thu_tu_cau="0";
						new LoadOneQuestion().execute();
						setradiocheck();
					}
				})
				.setPositiveButton("Không", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
										
						dialog.cancel();
					}
				});
				builder.create().show();
				
				
			}
		});
		btn_xem_dap_an.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				
					String dapan="ĐÁP ÁN:\n";
					for(int i=0;i<itemListLevelQuestion.size(); i++)
					{
						StringBuilder temp= new StringBuilder();
						if("1".equals(array_cau_tra_loi[i].toString()) )
						{		
							temp.append("Đ");temp.append("ú");temp.append("n");temp.append("g");
							temp.append(" ");temp.append(" ");temp.append(" "); temp.append(" ");
							temp.append(" ");temp.append(" ");temp.append(" "); temp.append(" ");
						}
						else if("0".equals(array_cau_tra_loi[i].toString()) )
						{
							temp.append("S");temp.append("a");temp.append("i"); temp.append(" ");
							temp.append(" ");temp.append(" ");temp.append(" "); temp.append(" ");
							temp.append(" ");temp.append(" ");temp.append(" "); temp.append(" ");
							temp.append(" ");temp.append(" ");temp.append(" ");
						}		
						else
						{
							temp.append("C");temp.append("h");temp.append("ư"); temp.append("a");
							temp.append(" ");temp.append("l");temp.append("à"); temp.append("m");
							temp.append(" ");
						}
						if(i+1 < 10)
						{
							dapan=dapan+ "  " + (i+1) + " "+temp.toString()+"    Đáp án: " + itemListLevelQuestion.get(i).getAnswer().toUpperCase()+"\n";
						}
						else
							dapan=dapan+ ""   + (i+1) + " "+temp.toString()+"    Đáp án: " + itemListLevelQuestion.get(i).getAnswer().toUpperCase()+"\n";
					}
					AlertDialog.Builder builder = new AlertDialog.Builder(Tab8_One_True_False.this);
					builder.setMessage(dapan)
					.setTitle("Đáp Án: "+tv_levelname.getText().toString())
					.setCancelable(false)
					.setNegativeButton("Thoát", new DialogInterface.OnClickListener() 
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							dialog.cancel();
							AlertDialog.Builder builder = new AlertDialog.Builder(Tab8_One_True_False.this);
							builder.setMessage("Bạn có muốn làm lại?")
							.setTitle("Thoát")
							.setCancelable(false)
							.setNegativeButton("Thoát", new DialogInterface.OnClickListener() 
							{
								
								@Override
								public void onClick(DialogInterface dialog, int which) 
								{
									dialog.cancel();
									finish();
								}
							})
							.setPositiveButton("Làm lại",new DialogInterface.OnClickListener() 
							{
								@Override
								public void onClick(DialogInterface dialog, int which) 
								{
									xemdapan=false;
									for(int i=0; i<Integer.valueOf(tong_socauhoi);i++)
									{
										array_cau_tra_loi[i]="_";
										array_a_b_c_d[i]="_";
									}
									so_thu_tu_cau="0";
									
									new LoadOneQuestion().execute();
									setradiocheck();
								}
							});
							builder.create().show();
						}
					})
					.setPositiveButton("Lưu kết quả",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							if(instantFunctionCheck.checkInternetConnection())
							{
								task_save_ketqua = new SaveKetQuaThi();
								task_save_ketqua.execute();
							}
							else
								Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet", Toast.LENGTH_SHORT).show();
						}
					});
					builder.create().show();
				}
			
		});
		btn_ketqua.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				AlertDialog.Builder builder1 = new AlertDialog.Builder(Tab8_One_True_False.this);
				builder1.setMessage("Bạn có chắc muốn nộp bài?")
				.setTitle("Nộp Bài")
				.setCancelable(false)
				.setNegativeButton("Không", new DialogInterface.OnClickListener() 
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.cancel();
					}
				})
				.setPositiveButton("Có",new DialogInterface.OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						socaudung=0;
						int socauchualam=0;
						xemdapan=true;
						for(int i=0;i<Integer.valueOf(tong_socauhoi);i++)
						{
							if("1".equals(array_cau_tra_loi[i]))
							{
								socaudung=socaudung+1;
							}
						}
						for(int i=0;i<Integer.valueOf(tong_socauhoi);i++)
						{
							if("_".equals(array_cau_tra_loi[i]))
							{
								socauchualam=socauchualam+1;
							}
						}
						diemso=(float)(    (float)10/Integer.valueOf(tong_socauhoi)   )*socaudung;
						diemso = (float) (Math.round(diemso*100.0)/100.0);
						java.util.Date d = new java.util.Date();
						
						ngay_thi = String.valueOf(d.getDate())+"/"+String.valueOf(d.getMonth()+1)+"/"+String.valueOf(d.getYear()+1900);
						
						AlertDialog.Builder builder = new AlertDialog.Builder(Tab8_One_True_False.this);
						builder.setMessage("Số câu đúng:"+socaudung+"/"+tong_socauhoi +"\nĐiểm số: "+diemso
								+"\nSố câu chưa làm: "+ socauchualam)
						.setTitle("Kết quả")
						.setCancelable(false)
						.setPositiveButton("Lưu kết quả",new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								if(instantFunctionCheck.checkInternetConnection())
								{
									task_save_ketqua = new SaveKetQuaThi();
									task_save_ketqua.execute();
								}
								else
									Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet", Toast.LENGTH_SHORT).show();
							}
						})
						.setNeutralButton("Xem đáp án",new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								btn_xem_dap_an.performClick();
							}
						})
						.setNegativeButton("Thoát", new DialogInterface.OnClickListener() 
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								dialog.cancel();
								finish();
							}
						});
						builder.create().show();
					}
				});
				builder1.create().show();
				
				
				
			}
		});
	}
	
	@Override
	public void onBackPressed()
	{
		Log.e("----------onbackpress","----------onbackpress");
		AlertDialog.Builder builder = new AlertDialog.Builder(Tab8_One_True_False.this);
		builder.setMessage("Bạn có chắc muốn thoát khi đang làm bài thi?")
		       .setCancelable(false)
		       .setIcon(R.drawable.icon_thongbao)
		       .setTitle("Thoát")
		       .setPositiveButton("Có", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                finish();
		           }
		       })
		       .setNegativeButton("Không", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	//
	public void setradiocheck()
	{	
		Log.e("----array_a_b_c_d Mặc định","----array_a_b_c_d Mặc định");	
			radio1.setChecked(false);
			radio2.setChecked(false);
			radio5.setChecked(true);
	}
	public void setradiocheck_a_b_c_d(String a_b_c_d)
	{	
		int key = 0;
			if( "a".equals(a_b_c_d) ) key=1;
			if( "b".equals(a_b_c_d) ) key=2;
			switch (key) 
			{
			case 1:
				radio1.setChecked(true);
				radio2.setChecked(false);
				radio5.setChecked(false);
				break;
			case 2:
				radio1.setChecked(false);
				radio2.setChecked(true);
				radio5.setChecked(false);
				break;
			}
	}
	
	//
	//
	//
	class LoadOneQuestion extends AsyncTask<String, String, String> 
	{
		int success;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) 
		{
			
			OneTracNghiem item = itemListLevelQuestion.get(Integer.valueOf(so_thu_tu_cau));	
					question_id = item.getId();
					question_text = item.getText();
					ans_a = item.getAns_a();
					ans_b = item.getAns_b();
					answer = item.getAnswer();

			return null;
		}
		protected void onPostExecute(String file_url) 
		{
			cau_hien_tai.setText("Câu: " + (Integer.valueOf(so_thu_tu_cau)+1) + "/" + tong_socauhoi);
			noi_dung_cau_hoi.setText(question_text);
			radio1.setText(ans_a.toUpperCase());
			radio2.setText(ans_b.toUpperCase());
			tv_dapan.setText(answer.toUpperCase());
		}

	}
	//
	//
	//
	class SaveKetQuaThi extends AsyncTask<String , String, String> 
	{
		int success;
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			success=0;
			pDialog = new ProgressDialog(Tab8_One_True_False.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... param) 
		{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id_user", id_user));
			params.add(new BasicNameValuePair("id_level", "7"));
			params.add(new BasicNameValuePair("diemso", String.valueOf(diemso)));
			params.add(new BasicNameValuePair("ngay_thi", ngay_thi));
			params.add(new BasicNameValuePair("socauhoi", tong_socauhoi));
			params.add(new BasicNameValuePair("socaudung", String.valueOf(socaudung)));
			
			JSONParser jj = new JSONParser();
			String json_string = jj.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_tracnghiem_luu_ketqua.php", "POST", params);
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					success = json.getInt(Constant.KEYWORD_SUCCESS); 
					if (success == 1) 
					{
						
					} else {success=0;}
				} catch (JSONException e) {e.printStackTrace();}
			} else {success=3;};

			return null;
		}
		protected void onPostExecute(String file_url) 
		{
			pDialog.dismiss();
			if(success==1 )
			{
				Toast.makeText(getApplicationContext(), "Lưu kết quả thành công", Toast.LENGTH_SHORT).show();
				finish();
			}
			if(success==0 )
			{
				Toast.makeText(getApplicationContext(), "Lưu thất bại, kiểm tra kết nối internet", Toast.LENGTH_SHORT).show();
			}
			if(success==3 )
			{
				Toast.makeText(getApplicationContext(), "Lưu thất bại, kiểm tra kết nối internet", Toast.LENGTH_SHORT).show();
			}
		}

	}
	//
	//Tab8 TracNghiem
	//
	public class OneTracNghiem 
	{
		private String id; 
	    private String cauhoi;
	    private String cau_a;
	    private String cau_b;
	    private String dapan;
	    public OneTracNghiem(String id, String text,String ans_a, String ans_b, String answer)
	    {
	        this.id = id;
	        this.cauhoi = text;
	        this.cau_a = ans_a;
	        this.cau_b = ans_b;
	        this.dapan = answer;
	    }
	    public String getId() { return id;}
	    public String getText() { return cauhoi;}
	    public String getAns_a(){ return cau_a;}
	    public String getAns_b(){ return cau_b;}
	    public String getAnswer(){ return dapan;}
	    
	    public void setId(String id) {this.id = id;}
	    public void setText(String text) {this.cauhoi = text;}
	    public void setAns_a(String ans_a) {this.cau_a = ans_a;}
	    public void setAns_b(String ans_b) {this.cau_b = ans_b;}
	    public void setAnswer(String answer) {this.dapan = answer;}
	    
	}
	//
	//
	//
	
}


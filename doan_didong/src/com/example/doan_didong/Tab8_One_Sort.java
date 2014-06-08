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


public class Tab8_One_Sort extends Activity
{
	JSONParser jsonParser = new JSONParser();
	String json_string;
	int success;
	private FunctionCheck instantFunctionCheck;
	private ProgressDialog pDialog;
	
	String id_user;					//id của user
	String id_level;				//id level của loại trac nghiem
	
	String[] array_cau_tra_loi;		//mảng[30] câu trả lời
	String[] array_dapan;
	String[] array_dapan_2;
	String[] array_diemso;
	
	String tong_socauhoi;
	String	ngay_thi;
	float 	diemso;
	int 	socaudung;
	boolean xemdapan;
	
	
	Button  btn_lamlai,
			btn_ketqua,
			btn_chualam,
			btn_xem_dap_an;
	TextView tv_doanvan;				
	TextView tv_username, tv_levelname;
	
	EditText cau1, cau2, cau3, cau4, cau5, cau6, cau7, cau8, cau9, cau10;
	
	SaveKetQuaThi task_save_ketqua;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab8_one_sort);
		instantFunctionCheck = new FunctionCheck(getApplicationContext());//tạo đối tượng kiểm tra kết nối internet
		
		Bundle b = getIntent().getExtras();
		id_user = 		b.getString("id_user");
		id_level   = 	b.getString("id_level");
		
		json_string="";
		json_string = FunctionFolderAndFile.readFromFile("tracnghiem", "level_sort.txt");
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
							array_dapan_2 = c.getString("dapan").split(",");
							tong_socauhoi = String.valueOf(array_dapan_2.length);
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
		//
		btn_lamlai=(Button)findViewById(R.id.tab8_sort_btn_lamlai);
		btn_ketqua=(Button)findViewById(R.id.tab8_sort_btn_ketqua);
		btn_chualam=(Button)findViewById(R.id.tab8_sort_btn_chualam);
		btn_xem_dap_an=(Button)findViewById(R.id.tab8_sort_btn_xemdapan);
		
		tv_doanvan 	=(TextView)findViewById(R.id.tab8_sort_doanvan);
		tv_username	=(TextView)findViewById(R.id.tab8_sort_username);
		tv_levelname=(TextView)findViewById(R.id.tab8_sort_level_name);
		
		cau1 = (EditText)findViewById(R.id.tab8_sort_1);
		cau2 = (EditText)findViewById(R.id.tab8_sort_2);
		cau3 = (EditText)findViewById(R.id.tab8_sort_3);
		cau4 = (EditText)findViewById(R.id.tab8_sort_4);
		cau5 = (EditText)findViewById(R.id.tab8_sort_5);
		cau6 = (EditText)findViewById(R.id.tab8_sort_6);
		cau7 = (EditText)findViewById(R.id.tab8_sort_7);
		cau8 = (EditText)findViewById(R.id.tab8_sort_8);
		cau9 = (EditText)findViewById(R.id.tab8_sort_9);
		cau10 = (EditText)findViewById(R.id.tab8_sort_10);
		disableEditText(tong_socauhoi);
		//
		//
		//
		array_cau_tra_loi     =new String[Integer.valueOf(tong_socauhoi)];
		array_dapan			= new String[Integer.valueOf(tong_socauhoi)];
		array_diemso     = new String[Integer.valueOf(tong_socauhoi)];
		for(int j=0; j<Integer.valueOf(tong_socauhoi); j++)
		{
			array_cau_tra_loi[j]="_";
			array_dapan[j] = "_";
			array_diemso[j]     ="_";
		}
		//
		json_string="";
		json_string = FunctionFolderAndFile.readFromFile("tracnghiem", "level_sort.txt");
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
							tv_doanvan.setText(c.getString("doanvan"));
							tv_username.setText(MainActivity.username);
							array_dapan = c.getString("dapan").split(",");
							
							for(int ii=0; ii < Integer.valueOf(tong_socauhoi); ii++)
							{
								array_dapan[ii] = array_dapan[ii].replace(" ", "");
								array_dapan[ii] = array_dapan[ii].replace(" ", "");
								array_dapan[ii] = array_dapan[ii].replace(" ", "");
								array_dapan[ii] = array_dapan[ii].replace(" ", "");
								array_dapan[ii] = array_dapan[ii].replace(" ", "");
								Log.e("------"+array_dapan[ii],"+++++++++"+ array_dapan[ii]+"++++++");
							}
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
		//
		//
		btn_chualam.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				checkcauchualam(tong_socauhoi);
				String chualam="Câu chưa làm:\n";
				for(int i=0;i<Integer.valueOf(tong_socauhoi);i++)
				{
					if(array_cau_tra_loi[i].equals("_"))
						chualam=chualam + String.valueOf(i+1) + "\n";
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(Tab8_One_Sort.this);
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
		btn_lamlai.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(Tab8_One_Sort.this);
				builder.setMessage("Những câu trả lời của bạn sẽ bị mất. Bạn thực sự muốn làm lại ?")
				.setTitle("Bạn có chắc muốn làm lại?")
				.setCancelable(false)
				.setIcon(R.drawable.icon_canh_bao)
				.setNegativeButton("Làm lại", new DialogInterface.OnClickListener() 
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						resetallvalue(tong_socauhoi);
						for(int i=0; i<Integer.valueOf(tong_socauhoi);i++)
						{
							array_cau_tra_loi[i]="_";
							array_diemso[i] ="_";
						}
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
			public void onClick(View v) 
			{
					String dapan="\n";
					for(int i=0; i<Integer.valueOf(tong_socauhoi); i++)
					{
						StringBuilder temp= new StringBuilder();
						if("1".equals(array_diemso[i].toString()) )
						{		
							temp.append("Đ");temp.append("ú");temp.append("n");temp.append("g");
							temp.append(" ");temp.append(" ");temp.append(" "); temp.append(" ");
							temp.append(" ");temp.append(" ");temp.append(" "); temp.append(" ");
						}
						else if("0".equals(array_diemso[i].toString()) )
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
						if( (i+1) < 10)
						{
							dapan=dapan+ "  " + (i+1) + " "+temp.toString()+"  Đáp án: " + array_dapan[i].toUpperCase()+"\n";
						}
						else
							dapan=dapan+ ""   + (i+1) + " "+temp.toString()+"  Đáp án: " + array_dapan[i].toUpperCase()+"\n";
					}
					AlertDialog.Builder builder = new AlertDialog.Builder(Tab8_One_Sort.this);
					builder.setMessage(dapan)
					.setTitle("Đáp Án: "+tv_levelname.getText().toString())
					.setCancelable(false)
					.setNegativeButton("Thoát", new DialogInterface.OnClickListener() 
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							dialog.cancel();
							AlertDialog.Builder builder = new AlertDialog.Builder(Tab8_One_Sort.this);
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
									resetallvalue(tong_socauhoi);
									for(int i=0; i<Integer.valueOf(tong_socauhoi);i++)
									{
										array_cau_tra_loi[i]="_";
										array_diemso[i]="_";
									}
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
				AlertDialog.Builder builder1 = new AlertDialog.Builder(Tab8_One_Sort.this);
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
						checkcauchualam(tong_socauhoi);
						checkdiemso(tong_socauhoi);
						socaudung=0;
						int socauchualam=0;
						xemdapan=true;
						for(int i=0;i<Integer.valueOf(tong_socauhoi);i++)
						{
							if("1".equals(array_diemso[i]))
							{
								socaudung=socaudung+1;
							}
						}
						for(int i=0;i<Integer.valueOf(tong_socauhoi);i++)
						{
							if("_".equals(array_diemso[i]))
							{
								socauchualam=socauchualam+1;
							}
						}
						diemso=(float)(    (float)10/Integer.valueOf(tong_socauhoi)   )*socaudung;
						diemso = (float) (Math.round(diemso*100.0)/100.0);
						java.util.Date d = new java.util.Date();
						
						ngay_thi = String.valueOf(d.getDate())+"/"+String.valueOf(d.getMonth()+1)+"/"+String.valueOf(d.getYear()+1900);
						
						AlertDialog.Builder builder = new AlertDialog.Builder(Tab8_One_Sort.this);
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
	//
	public void disableEditText(String num1)
	{
		int num = Integer.valueOf(num1);
		switch (num) 
		{
		case 10:
			break;
		case 9:
			cau10.setVisibility(View.GONE);
			break;
		case 8:
			cau10.setVisibility(View.GONE); cau9.setVisibility(View.GONE);
			break;
		case 7:
			cau10.setVisibility(View.GONE); cau9.setVisibility(View.GONE); cau8.setVisibility(View.GONE);
			break;
		case 6:
			cau10.setVisibility(View.GONE); cau9.setVisibility(View.GONE); cau8.setVisibility(View.GONE);
			cau7.setVisibility(View.GONE);
			break;
		case 5:
			cau10.setVisibility(View.GONE); cau9.setVisibility(View.GONE); cau8.setVisibility(View.GONE);
			cau7.setVisibility(View.GONE); cau6.setVisibility(View.GONE);
			break;
		case 4:
			cau10.setVisibility(View.GONE); cau9.setVisibility(View.GONE); cau8.setVisibility(View.GONE);
			cau7.setVisibility(View.GONE); cau6.setVisibility(View.GONE);  cau5.setVisibility(View.GONE);
			break;
		
		default:
			break;
		}
	}
	public void resetallvalue(String num)
	{
		cau1.setText("");
		cau2.setText("");
		cau3.setText("");
		cau4.setText("");
		cau5.setText("");
		cau6.setText("");
		cau7.setText("");
		cau8.setText("");
		cau9.setText("");
		cau10.setText("");
		for(int i=0 ; i < Integer.valueOf(num); i++ )
		{
			array_cau_tra_loi[i]="_";
			array_diemso[i] = "_";
		}
	}
	//
	public void checkcauchualam(String num1)
	{
		int num = Integer.valueOf(num1);
		switch (num) 
		{
		case 10:
			if("".equals(cau1.getText().toString())) array_cau_tra_loi[0] = "_"; else array_cau_tra_loi[0] = cau1.getText().toString();
			if("".equals(cau2.getText().toString())) array_cau_tra_loi[1] = "_"; else array_cau_tra_loi[1] = cau2.getText().toString();
			if("".equals(cau3.getText().toString())) array_cau_tra_loi[2] = "_"; else array_cau_tra_loi[2] = cau3.getText().toString();
			if("".equals(cau4.getText().toString())) array_cau_tra_loi[3] = "_"; else array_cau_tra_loi[3] = cau4.getText().toString();
			if("".equals(cau5.getText().toString())) array_cau_tra_loi[4] = "_"; else array_cau_tra_loi[4] = cau5.getText().toString();
			if("".equals(cau6.getText().toString())) array_cau_tra_loi[5] = "_"; else array_cau_tra_loi[5] = cau6.getText().toString();
			if("".equals(cau7.getText().toString())) array_cau_tra_loi[6] = "_"; else array_cau_tra_loi[6] = cau7.getText().toString();
			if("".equals(cau8.getText().toString())) array_cau_tra_loi[7] = "_"; else array_cau_tra_loi[7] = cau8.getText().toString();
			if("".equals(cau9.getText().toString())) array_cau_tra_loi[8] = "_"; else array_cau_tra_loi[8] = cau9.getText().toString();
			if("".equals(cau10.getText().toString())) array_cau_tra_loi[9] = "_"; else array_cau_tra_loi[9] = cau10.getText().toString();
			break;
		case 9:
			if("".equals(cau1.getText().toString())) array_cau_tra_loi[0] = "_"; else array_cau_tra_loi[0] = cau1.getText().toString();
			if("".equals(cau2.getText().toString())) array_cau_tra_loi[1] = "_"; else array_cau_tra_loi[1] = cau2.getText().toString();
			if("".equals(cau3.getText().toString())) array_cau_tra_loi[2] = "_"; else array_cau_tra_loi[2] = cau3.getText().toString();
			if("".equals(cau4.getText().toString())) array_cau_tra_loi[3] = "_"; else array_cau_tra_loi[3] = cau4.getText().toString();
			if("".equals(cau5.getText().toString())) array_cau_tra_loi[4] = "_"; else array_cau_tra_loi[4] = cau5.getText().toString();
			if("".equals(cau6.getText().toString())) array_cau_tra_loi[5] = "_"; else array_cau_tra_loi[5] = cau6.getText().toString();
			if("".equals(cau7.getText().toString())) array_cau_tra_loi[6] = "_"; else array_cau_tra_loi[6] = cau7.getText().toString();
			if("".equals(cau8.getText().toString())) array_cau_tra_loi[7] = "_"; else array_cau_tra_loi[7] = cau8.getText().toString();
			if("".equals(cau9.getText().toString())) array_cau_tra_loi[8] = "_"; else array_cau_tra_loi[8] = cau9.getText().toString();
			break;
		case 8:
			if("".equals(cau1.getText().toString())) array_cau_tra_loi[0] = "_"; else array_cau_tra_loi[0] = cau1.getText().toString();
			if("".equals(cau2.getText().toString())) array_cau_tra_loi[1] = "_"; else array_cau_tra_loi[1] = cau2.getText().toString();
			if("".equals(cau3.getText().toString())) array_cau_tra_loi[2] = "_"; else array_cau_tra_loi[2] = cau3.getText().toString();
			if("".equals(cau4.getText().toString())) array_cau_tra_loi[3] = "_"; else array_cau_tra_loi[3] = cau4.getText().toString();
			if("".equals(cau5.getText().toString())) array_cau_tra_loi[4] = "_"; else array_cau_tra_loi[4] = cau5.getText().toString();
			if("".equals(cau6.getText().toString())) array_cau_tra_loi[5] = "_"; else array_cau_tra_loi[5] = cau6.getText().toString();
			if("".equals(cau7.getText().toString())) array_cau_tra_loi[6] = "_"; else array_cau_tra_loi[6] = cau7.getText().toString();
			if("".equals(cau8.getText().toString())) array_cau_tra_loi[7] = "_"; else array_cau_tra_loi[7] = cau8.getText().toString();
			break;
		case 7:
			if("".equals(cau1.getText().toString())) array_cau_tra_loi[0] = "_"; else array_cau_tra_loi[0] = cau1.getText().toString();
			if("".equals(cau2.getText().toString())) array_cau_tra_loi[1] = "_"; else array_cau_tra_loi[1] = cau2.getText().toString();
			if("".equals(cau3.getText().toString())) array_cau_tra_loi[2] = "_"; else array_cau_tra_loi[2] = cau3.getText().toString();
			if("".equals(cau4.getText().toString())) array_cau_tra_loi[3] = "_"; else array_cau_tra_loi[3] = cau4.getText().toString();
			if("".equals(cau5.getText().toString())) array_cau_tra_loi[4] = "_"; else array_cau_tra_loi[4] = cau5.getText().toString();
			if("".equals(cau6.getText().toString())) array_cau_tra_loi[5] = "_"; else array_cau_tra_loi[5] = cau6.getText().toString();
			if("".equals(cau7.getText().toString())) array_cau_tra_loi[6] = "_"; else array_cau_tra_loi[6] = cau7.getText().toString();
			break;
		case 6:
			if("".equals(cau1.getText().toString())) array_cau_tra_loi[0] = "_"; else array_cau_tra_loi[0] = cau1.getText().toString();
			if("".equals(cau2.getText().toString())) array_cau_tra_loi[1] = "_"; else array_cau_tra_loi[1] = cau2.getText().toString();
			if("".equals(cau3.getText().toString())) array_cau_tra_loi[2] = "_"; else array_cau_tra_loi[2] = cau3.getText().toString();
			if("".equals(cau4.getText().toString())) array_cau_tra_loi[3] = "_"; else array_cau_tra_loi[3] = cau4.getText().toString();
			if("".equals(cau5.getText().toString())) array_cau_tra_loi[4] = "_"; else array_cau_tra_loi[4] = cau5.getText().toString();
			if("".equals(cau6.getText().toString())) array_cau_tra_loi[5] = "_"; else array_cau_tra_loi[5] = cau6.getText().toString();
			break;
		case 5:
			if("".equals(cau1.getText().toString())) array_cau_tra_loi[0] = "_"; else array_cau_tra_loi[0] = cau1.getText().toString();
			if("".equals(cau2.getText().toString())) array_cau_tra_loi[1] = "_"; else array_cau_tra_loi[1] = cau2.getText().toString();
			if("".equals(cau3.getText().toString())) array_cau_tra_loi[2] = "_"; else array_cau_tra_loi[2] = cau3.getText().toString();
			if("".equals(cau4.getText().toString())) array_cau_tra_loi[3] = "_"; else array_cau_tra_loi[3] = cau4.getText().toString();
			if("".equals(cau5.getText().toString())) array_cau_tra_loi[4] = "_"; else array_cau_tra_loi[4] = cau5.getText().toString();
			break;
		case 4:
			if("".equals(cau1.getText().toString())) array_cau_tra_loi[0] = "_"; else array_cau_tra_loi[0] = cau1.getText().toString();
			if("".equals(cau2.getText().toString())) array_cau_tra_loi[1] = "_"; else array_cau_tra_loi[1] = cau2.getText().toString();
			if("".equals(cau3.getText().toString())) array_cau_tra_loi[2] = "_"; else array_cau_tra_loi[2] = cau3.getText().toString();
			if("".equals(cau4.getText().toString())) array_cau_tra_loi[3] = "_"; else array_cau_tra_loi[3] = cau4.getText().toString();
			break;

		default:
			break;
		}
	}
	public void checkdiemso(String num)
	{
		checkcauchualam(num);
		for(int i =0 ; i< Integer.valueOf(num); i++)
		{
			if("_".equals(array_cau_tra_loi[i]))
			{
				array_diemso[i] = "_"; 
			}
			else if( (array_cau_tra_loi[i].toUpperCase()).equals( (array_dapan[i].toUpperCase()))  )
			{
				Log.e("--------Bằng nhau-------", "+++++++++"+array_dapan[i]+"==========");
				array_diemso[i] = "1";
			}
			else
			{
				Log.e("--------Khác nhau-------", "+++++++++"+array_dapan[i]+"==========");
				array_diemso[i] = "0";
			}
		}
	}
	//
	@Override
	public void onBackPressed()
	{
		Log.e("----------onbackpress","----------onbackpress");
		AlertDialog.Builder builder = new AlertDialog.Builder(Tab8_One_Sort.this);
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
			pDialog = new ProgressDialog(Tab8_One_Sort.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... param) 
		{
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id_user", id_user));
			params.add(new BasicNameValuePair("id_level", "9"));
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
	
}


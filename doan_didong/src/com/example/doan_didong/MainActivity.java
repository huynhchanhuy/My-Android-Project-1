package com.example.doan_didong;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.example.doan_didong.Tab2_ThayDoiMatKhauActivity.GetInformationProgress;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


public class MainActivity extends SlidingFragmentActivity
{
	private SlidingMenu slidingMenu;
	//
	//Main FragmentActivity
	//
	FunctionCheck instantFunctionCheck;
	JSONParser jsonParser = new JSONParser();
	Bundle b;
	static public int iduser;
	static public String username;
	static public String hovaten;
	static public String ngaysinh;
	static public String gioitinh;
	static public String diachi;
	static public String email;
	static public String sdt;
	static public int active;
	static public int usertype;
	static public String link_hinhdaidien;
	boolean doubleBackToExitPressedOnce = false;
	private DownloadDataFromServerProgress task_downloadfiledata;
	public int all_item_tudien_sdcard;
	public int all_item_thanhngu_sdcard;
	public int all_item_nguphap_sdcard;
	public int all_item_audio_sdcard;
	public int all_item_video_sdcard;
	
	//
	//Tab2 Thong Tin Ca Nhan
	//
	static final public int GET_CODE = 1;
	static final public int CHANGE_IMAGE = 11;
	static public Bitmap bmImg = null;
	
	//
	//Tab3 TuDien
	//
	public static ArrayList<TuDien> itemlistTuDien;
	public static ArrayList<TuDien> temp_itemlistTuDien;
	public static int tudien_all_item;
	public static int tudien_start;
	public static int tudien_end_of_list;
	public static int dang_tim_kiem;
	
	//
	//Tab4 Thanh Ngu
	//
	public static ArrayList<ThanhNgu> itemlistThanhNgu;
	public static int thanhngu_all_item;
	public static int thanhngu_start;
	public static int thanhngu_end_of_list;
	public static int thanhngu_stt;
	
	//
	//Tab5 Ngữ Pháp
	//
	public static ArrayList<NguPhap> itemlistNguPhap;
	public static int nguphap_all_item;
	public static int nguphap_start;
	public static int nguphap_end_of_list;
	public static int nguphap_stt;
	//
	//Tab6 Audio
	//
	public static ArrayList<Audio> itemlistAudio;
	public static int audio_all_item;
	public static int audio_start;
	public static int audio_end_of_list;
	public static int audio_stt;
	
	//
	//Tab7 Video
	//
	public static ArrayList<Video> itemlistVideo;
	public static int video_all_item;
	public static int video_start;
	public static int video_end_of_list;
	public static int video_stt;
	//
	//Tab8 TracNghiem
	//
	public static ArrayList<TracNghiem> itemlistTracNghiem;
	public static int tracnghiem_all_item;
	//
	//Tab10 Kết quả
	//
	public static ArrayList<KetQua> itemlistKetQua;
	
	//
	//
	//
	public ActionBar mActionBar;
	public ViewPager mPager;
	public Tab tab;
	public ViewPagerAdapter viewpageradapter;
	public FragmentManager fm;
	
	ListView lv_sliding_menu_id;
	ArrayList<MainActivity.SampleItem> itemlisSlidingMenu;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//không cho xoay màn hình
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
		

	    

		setBehindContentView(R.layout.menu_sliding_left);

		setSlidingActionBarEnabled(true);
	    slidingMenu = getSlidingMenu();
	    slidingMenu.setMode(SlidingMenu.LEFT);
	    slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
	    slidingMenu.setShadowDrawable(R.drawable.shadow);
	    slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	    slidingMenu.setFadeDegree(0.35f);
	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//	    slidingMenu.attachToActivity(MainActivity.this, SlidingMenu.SLIDING_CONTENT);
//	    slidingMenu.setMenu(R.layout.menu_sliding_left);
	    
	    itemlisSlidingMenu = new ArrayList<MainActivity.SampleItem>();
	    lv_sliding_menu_id = (ListView) findViewById(R.id.sliding_menu_id);
	    SampleAdapter_Custom adapter = new SampleAdapter_Custom(getApplicationContext(),R.layout.menu_sliding_left, itemlisSlidingMenu);
		
	    adapter.add(new SampleItem("TÙY CHỌN ", R.drawable.icon_logout, 4));
	    adapter.add(new SampleItem("   Tin Nhắn ", R.drawable.icon_mail, 1));
	    adapter.add(new SampleItem("   Liên Hệ ", R.drawable.icon_lienhe, 2));
	    adapter.add(new SampleItem("   Đăng Xuất ", R.drawable.icon_logout, 3));
	    
	    
	    adapter.add(new SampleItem("YÊU THÍCH ", R.drawable.icon_logout, 4));
	    
	    adapter.add(new SampleItem("   Từ Thông Dụng ", R.drawable.icon_tu_moi, 5));
	    adapter.add(new SampleItem("   Thành Ngữ ", R.drawable.icon_thanh_ngu, 6));
	    adapter.add(new SampleItem("   Bài Nghe ", R.drawable.icon_audio_tab, 7));
	    adapter.add(new SampleItem("   Video ", R.drawable.icon_video_tab, 8));
	    
	    adapter.add(new SampleItem("CHIA SẺ ", R.drawable.icon_logout, 4));
	    adapter.add(new SampleItem("   Facebook ", R.drawable.icon_facebook, 9));
	    adapter.add(new SampleItem("   Twitter ", R.drawable.icon_twitter, 10));
	    adapter.add(new SampleItem("   Google+ ", R.drawable.icon_google, 11));
		
		lv_sliding_menu_id.setAdapter(adapter);
		
		//
		//Tab3 TuDien
		//
		itemlistTuDien = new ArrayList<TuDien>();
		temp_itemlistTuDien = new ArrayList<TuDien>();
		tudien_start = 0;
		tudien_all_item = 10;
		tudien_end_of_list=0;
		dang_tim_kiem=0;
		
		//
		//Tab4 thanh ngu
		//
		itemlistThanhNgu = new ArrayList<MainActivity.ThanhNgu>();
		thanhngu_all_item=10;
		thanhngu_start=0;
		thanhngu_end_of_list=0;
		thanhngu_stt=1;
		
		//
		//Tab5 thanh ngu
		//
		itemlistNguPhap = new ArrayList<MainActivity.NguPhap>();
		nguphap_all_item=10;
		nguphap_start=0;
		nguphap_end_of_list=0;
		nguphap_stt=1;
		
		//
		//Tab6 Audio
		//
		itemlistAudio = new ArrayList<MainActivity.Audio>();
		audio_all_item=10;
		audio_start=0;
		audio_end_of_list=0;
		audio_stt=1;
		//
		//Tab7 Video
		//
		itemlistVideo = new ArrayList<MainActivity.Video>();
		video_all_item=10;
		video_start=0;
		video_end_of_list=0;
		video_stt=1;
		//
		//Tab8 TracNghiem
		//
		itemlistTracNghiem = new ArrayList<MainActivity.TracNghiem>();
		tracnghiem_all_item=1;
		//
		//Tab10 KetQua
		//
		itemlistKetQua = new ArrayList<MainActivity.KetQua>();
		//
		//
		
		mActionBar = getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
				
		// Locate ViewPager in main.xml
		mPager = (ViewPager) findViewById(R.id.view_pager);
		fm = getSupportFragmentManager();

		// Capture ViewPager page swipes
		ViewPager.SimpleOnPageChangeListener ViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				// Find the ViewPager Position
				mActionBar.setSelectedNavigationItem(position);
				switch (position) {
				case 0:
					getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				default:
					getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
					break;
				}
			}
		};

		mPager.setOnPageChangeListener(ViewPagerListener);
		// Locate the adapter class called ViewPagerAdapter.java
		viewpageradapter = new ViewPagerAdapter(fm);
		// Set the View Pager Adapter into ViewPager
		mPager.setAdapter(viewpageradapter);
		
		// Capture tab button clicks
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				// Pass the position on tab click to ViewPager
				if(tab.getPosition() == 0 ) tab.setIcon(R.drawable.icon_home);  //home
				if(tab.getPosition() == 1 ) tab.setIcon(R.drawable.icon_thong_tin_ca_nhan);//thongtin
				if(tab.getPosition() == 2 ) tab.setIcon(R.drawable.icon_tu_moi);//từ điển
				if(tab.getPosition() == 3 ) tab.setIcon(R.drawable.icon_thanh_ngu);//thành ngữ
				if(tab.getPosition() == 4 ) tab.setIcon(R.drawable.icon_ngu_phap);//ngữ pháp
				if(tab.getPosition() == 5 ) tab.setIcon(R.drawable.icon_audio_tab);//audio
				if(tab.getPosition() == 6 ) tab.setIcon(R.drawable.icon_video_tab);//video
				if(tab.getPosition() == 7 ) tab.setIcon(R.drawable.icon_trac_nghiem_tab);//trac nghiem
				if(tab.getPosition() == 8 ) tab.setIcon(R.drawable.icon_yeu_thich_tab);//yeu thich
				if(tab.getPosition() == 9 ) tab.setIcon(R.drawable.icon_ket_qua);//ket qua
				
				
				mPager.setCurrentItem(tab.getPosition());
//				Log.d("MainActivity message"+ (tab.getPosition()+1), "đang ở tab"+ (tab.getPosition()+1));
				//Toast.makeText(getApplicationContext(), "đang ở tab "+(tab.getPosition()+1) + " " + hovaten, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				if(tab.getPosition() == 0 ) tab.setIcon(R.drawable.icon_home_select);  //home
				if(tab.getPosition() == 1 ) tab.setIcon(R.drawable.icon_thong_tin_ca_nhan_select);//thongtin
				if(tab.getPosition() == 2 ) tab.setIcon(R.drawable.icon_tu_moi_select);//từ điển
				if(tab.getPosition() == 3 ) tab.setIcon(R.drawable.icon_thanh_ngu_select);//thành ngữ
				if(tab.getPosition() == 4 ) tab.setIcon(R.drawable.icon_ngu_phap_select);//ngữ pháp
				if(tab.getPosition() == 5 ) tab.setIcon(R.drawable.icon_audio_tab_select);//audio
				if(tab.getPosition() == 6 ) tab.setIcon(R.drawable.icon_video_tab_select);//video
				if(tab.getPosition() == 7 ) tab.setIcon(R.drawable.icon_trac_nghiem_tab_select);//trac nghiem
				if(tab.getPosition() == 8 ) tab.setIcon(R.drawable.icon_yeu_thich_tab_select);//yeu thich
				if(tab.getPosition() == 9 ) tab.setIcon(R.drawable.icon_ket_qua_select);//ket qua
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				mPager.setCurrentItem(tab.getPosition());
				//Toast.makeText(getApplicationContext(), "đang ở tab"+tab.getPosition() + hovaten, Toast.LENGTH_LONG).show();
			}
		};

		tab = mActionBar.newTab().setText("Home").setTabListener(tabListener).setIcon(R.drawable.icon_home_select);
		mActionBar.addTab(tab);
		
		tab = mActionBar.newTab().setText("Thông Tin").setTabListener(tabListener).setIcon(R.drawable.icon_thong_tin_ca_nhan_select);
		mActionBar.addTab(tab);
		
		tab = mActionBar.newTab().setText("Từ Thông Dụng").setTabListener(tabListener).setIcon(R.drawable.icon_tu_moi_select);
		mActionBar.addTab(tab);

		tab = mActionBar.newTab().setText("Thành Ngữ").setTabListener(tabListener).setIcon(R.drawable.icon_thanh_ngu_select);
		mActionBar.addTab(tab);
		
		tab = mActionBar.newTab().setText("Ngữ Pháp").setTabListener(tabListener).setIcon(R.drawable.icon_ngu_phap_select);
		mActionBar.addTab(tab);
		
		tab = mActionBar.newTab().setText("Bài Nghe").setTabListener(tabListener).setIcon(R.drawable.icon_audio_tab_select);
		mActionBar.addTab(tab);
		
		tab = mActionBar.newTab().setText("Xem Video").setTabListener(tabListener).setIcon(R.drawable.icon_video_tab_select);
		mActionBar.addTab(tab);
		
		tab = mActionBar.newTab().setText("Bài Thi").setTabListener(tabListener).setIcon(R.drawable.icon_trac_nghiem_tab_select);
		mActionBar.addTab(tab);
		
		tab = mActionBar.newTab().setText("Yêu Thích").setTabListener(tabListener).setIcon(R.drawable.icon_yeu_thich_tab_select);
		mActionBar.addTab(tab);
		
		tab = mActionBar.newTab().setText("Kết Quả").setTabListener(tabListener).setIcon(R.drawable.icon_ket_qua_select);
		mActionBar.addTab(tab);	
		
		
		if(FunctionFolderAndFile.createFolderByFoldername("version")) //tạo folder version trong sdcard
		{
			if(FunctionFolderAndFile.checkFileExist("version", "version.txt")) // file version.txt tồn tại
			{
				//get JSON từ file version.txt ra
				// aysntask get JSON từ table version về
				if(FunctionFolderAndFile.checkFileExist("version", "version.txt")==false) // file version.txt tồn tại
				{
					task_downloadfiledata = new DownloadDataFromServerProgress();
					task_downloadfiledata.execute("tudien.txt");
				}
				
				Log.e("-------có file version------","-------có file version------");
				String json_version = FunctionFolderAndFile.readFromFile( "version", "version.txt");
				if(json_version.length() != 0)
				{
					JSONObject json;
					try {
						json = JSONParser.getJSONObjectFromJString(json_version);
						all_item_tudien_sdcard = json.getInt("tudien");
						all_item_thanhngu_sdcard = json.getInt("thanhngu");
						all_item_nguphap_sdcard = json.getInt("nguphap");
						all_item_audio_sdcard = json.getInt("audio");
						all_item_video_sdcard = json.getInt("video");
						
						Log.e("----------check all_item---------",
								"tudien: "+all_item_tudien_sdcard + 
								" thanhngu: "+ all_item_thanhngu_sdcard+
								" nguphap: "+ all_item_nguphap_sdcard +
								" audio: "+ all_item_audio_sdcard+
								" video: "+ all_item_video_sdcard
								);
						new CheckServerUpdateProgress().execute();
						
					} catch (JSONException e) 
					{
						e.printStackTrace();
					}
				} else 
					{
						Log.e("-------get json version FAIL------","-------get json version FAIL------");
					}
			}
			else
			{
				//download file về (tudien.txt, audio.txt,nguphap.txt...)
				//cuối cùng download file version.txt
				if(instantFunctionCheck.checkInternetConnection())
				{
					if(FunctionFolderAndFile.checkFileExist("version", "version.txt")==false) // file version.txt tồn tại
					{
						task_downloadfiledata = new DownloadDataFromServerProgress();
						task_downloadfiledata.execute("tudien.txt");
					}
					
					task_downloadfiledata = new DownloadDataFromServerProgress();
					task_downloadfiledata.execute("thanhngu.txt");
					
					task_downloadfiledata = new DownloadDataFromServerProgress();
					task_downloadfiledata.execute("nguphap.txt");
					
					task_downloadfiledata = new DownloadDataFromServerProgress();
					task_downloadfiledata.execute("audio.txt");
					
					task_downloadfiledata = new DownloadDataFromServerProgress();
					task_downloadfiledata.execute("video.txt");
					
					task_downloadfiledata = new DownloadDataFromServerProgress();
					task_downloadfiledata.execute("level.txt");
					
					task_downloadfiledata = new DownloadDataFromServerProgress();
					task_downloadfiledata.execute("version.txt");
					
					
					
				} else Toast.makeText(getApplicationContext(), "Kiểm tra kết nối internet.", Toast.LENGTH_SHORT).show();
			}
		}
	}
	//
	//
	//
	static public class SampleItem 
	{
		public String tag;
		public int iconRes;
		public int id;
		public SampleItem(String tag, int iconRes, int id) 
		{
			this.tag = tag; 
			this.iconRes = iconRes;
			this.id = id;
		}
		public int getId(){ return this.id; };
		public String getTag(){ return this.tag; };
		public int getIconRes(){ return this.iconRes; };
		
		public void setId(int id) {		this.id= id;};
		public void setTag(String id) {	this.tag= id;};
		public void setIconRes(int id) {this.iconRes = id;};
	}

	public class SampleAdapter_Custom extends ArrayAdapter<MainActivity.SampleItem> 
	{
		private Context mContext;
	    private ArrayList<MainActivity.SampleItem> items=null;
	    int resource;
	    
	    public SampleAdapter_Custom(Context context, int textViewResourceId,ArrayList<MainActivity.SampleItem> objects) 
		{
			  super(context, textViewResourceId, objects);
			  this.mContext = context;
			  this.resource = textViewResourceId;
			  this.items = objects; 
		}
	    @Override
		public MainActivity.SampleItem getItem(int position) 
		{
			return super.getItem(position);
		}
		
		@Override
		public long getItemId(int position) 
		{
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) 
			{
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_sliding_left_row, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.sliding_menu_row_icon);
			TextView title = (TextView) convertView.findViewById(R.id.sliding_menu_row_title);
			final MainActivity.SampleItem iContent = items.get(position);
			if(iContent.getId()==4)
			{
				icon.setImageResource(iContent.getIconRes());
				icon.setVisibility(View.GONE);
				convertView.setBackgroundColor(Color.TRANSPARENT);
				title.setTextColor(Color.WHITE);
				title.setText(iContent.getTag());
			}
			else
			{
				icon.setImageResource(iContent.getIconRes());
				icon.setVisibility(View.VISIBLE);
				convertView.setBackgroundResource(R.drawable.item_listview_style);
				title.setTextColor(Color.BLACK);
				title.setText(iContent.getTag());
			}
			
			
			convertView.setOnClickListener(new View.OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					switch (iContent.getId()) 
					{
					case 1:
						Toast.makeText(getApplicationContext(), "Chức năng này đang phát triển!", Toast.LENGTH_SHORT).show();
						break;
					case 2:
						/*Intent ii = new Intent(getApplicationContext(), SendMailToAdmin.class);
		            	startActivity(ii);*/
						
						/*Intent dialogIntent = new Intent(android.content.Intent.ACTION_SEND); 
						dialogIntent.setType("plain/text");
						dialogIntent.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivity");
						dialogIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "tranvokhoinguyen@gmail.com"); 
						dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
						getApplication().startActivity(dialogIntent); */
						
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

	                     } catch (Throwable t) {
	                           Toast.makeText(getApplicationContext(),
	                                         "Gửi thất bại, vui lòng thử lại",Toast.LENGTH_LONG).show();
	                     }
						break;
					case 3:
						Log.e("click thoát", "click thoát");
						taoDialog();
						break;
					case 4:
						break;
					case 5:
						Intent i0 = new Intent(getApplicationContext(), Tab9_TuThongDung_YeuThich.class);
		            	i0.putExtra(Constant.KEYWORD_USER_ID, String.valueOf(MainActivity.iduser));
		            	i0.putExtra("loai", "tudien");
		            	startActivity(i0);
						break;
					case 6:
						Intent i1 = new Intent(getApplicationContext(), Tab9_ThanhNgu_YeuThich.class);
		            	i1.putExtra(Constant.KEYWORD_USER_ID, String.valueOf(MainActivity.iduser));
		            	i1.putExtra("loai", "thanhngu");
		            	startActivity(i1);
						break;
					case 7:
						Intent i2 = new Intent(getApplicationContext(), Tab9_Audio_YeuThich.class);
		            	i2.putExtra(Constant.KEYWORD_USER_ID, String.valueOf(MainActivity.iduser));
		            	i2.putExtra("loai", "audio");
		            	startActivity(i2);
						break;
					case 8:
						Intent i3 = new Intent(getApplicationContext(), Tab9_Video_YeuThich.class);
		            	i3.putExtra(Constant.KEYWORD_USER_ID, String.valueOf(MainActivity.iduser));
		            	i3.putExtra("loai", "video");
		            	startActivity(i3);
						break;
					case 9:
						taoDialogFanpage_Share();
						break;
					case 10:
						//String url = "https://www.facebook.com/plugins/like.php?layout=standard&show_faces=true&width=80&height=50&action=like&colorscheme=light&href=https://www.facebook.com/pages/Easy-English/477525825689763";
			             //   webview = (WebView) findViewById(R.id.webview);
			               // webView.loadUrl(url);
			                // webView.setWebViewClient(new WebViewClient());
						
						Intent i10 = new Intent(Intent.ACTION_VIEW);
					    String url = "https://twitter.com/?lang=en";
					    i10.setData(Uri.parse(url));
					    startActivity(i10);
						break;
					case 11:
						
						Intent i11 = new Intent(Intent.ACTION_VIEW);
					    String url11 = "https://plus.google.com/b/117170793212409164300/communities/113005464801500771654";
					    i11.setData(Uri.parse(url11));
					    startActivity(i11);
						break;
					default:
						break;
					}
				}
			});
			/*icon.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View v) 
				{
					if(iContent.getId()==9)
					{
						shareFaceBook();
						
					}
				}
			});*/
			return convertView;
		}

	}
	//
	public void shareFaceBook()
	{
	    int i = 0;
		//sharing implementation
	    List<Intent> targetedShareIntents = new ArrayList<Intent>();
	    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
	    sharingIntent.setType("text/plain");

	    PackageManager pm = getPackageManager();
	    List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
	    for(final ResolveInfo app : activityList) 
	    {

	         String packageName = app.activityInfo.packageName;
	         Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
	         targetedShareIntent.setType("text/plain");
	         targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
	         //if(TextUtils.equals(packageName, "com.facebook.katana"))
	         if(packageName.contains("facebook"))
	         {
	        	 i=1;
	        	 targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://www.facebook.com/pages/Easy-English/477525825689763");
	             targetedShareIntent.setPackage(packageName);
		         targetedShareIntents.add(targetedShareIntent);
		         
	         } 
	         else 
	         {
	             //targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
	        	 //i=0;
	         }
	    }
	    if(i == 1)
	    {
	    	Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Share");
//		    Intent chooserIntent = Intent.createChooser(targetedShareIntents, "Share");
		    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
		    startActivity(chooserIntent);
	    }
	    else
	    {
	    	Toast.makeText(getApplicationContext(), "Please install App Facebook on your device", Toast.LENGTH_SHORT).show();
	    }
	    

	}
	//
	//
	public void taoDialog()
	{
		Dialog dialog;
        AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc muốn đăng xuất ?")
	       		.setCancelable(false)
	       		.setIcon(R.drawable.icon_canh_bao)
	       		.setTitle("Đăng Xuất?")
	       		.setPositiveButton("Có", new DialogInterface.OnClickListener() 
	       		{
    	           public void onClick(DialogInterface dialog, int id) 
    	           {
    	        	   Intent i = new Intent(getApplicationContext(), LoginActivity.class);
    	        	   startActivity(i); 
    	        	   finish();
    	        	   MainActivity.bmImg = null;
    	           }
	       		})
	       		.setNegativeButton("Không", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	           }
	       		});
        dialog = builder.create();
        dialog.show();
	}
	public void taoDialogFanpage_Share()
	{
		Dialog dialog;
        AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(this);
        builder.setMessage("Like Fanpage, share Fanpage on your timeline")
	       		.setCancelable(false)
	       		.setIcon(R.drawable.icon_thongbao)
	       		.setTitle("Like and Share")
	       		.setNeutralButton("Like Fanpage", new DialogInterface.OnClickListener() 
	       		{
    	           public void onClick(DialogInterface dialog, int id) 
    	           {
    	        	    Intent i10 = new Intent(Intent.ACTION_VIEW);
					    String url = "https://m.facebook.com/pages/Easy-English/477525825689763";
					    i10.setData(Uri.parse(url));
					    startActivity(i10);
    	           }
	       		})
	       		.setNegativeButton("Share via App Facebook", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) 
    	           {
    	                shareFaceBook();
    	           }
	       		})
	       		.setPositiveButton("Cancel", new DialogInterface.OnClickListener()
	       		{
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
        dialog = builder.create();
        dialog.show();
	}
	//
	//
	//
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) 
		{
			case android.R.id.home:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) 
	{
//		getSupportMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	
	//
	//Check server update
	//
	//
	static class DownloadDataFromServerProgress extends AsyncTask<String, String, String> 
	{	  
		int download_ok;
		String filename;
		String toast;
		BufferedInputStream in;
	    FileOutputStream fout;
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			download_ok=0;
			in = null;
			fout = null;
		}

		@Override
		protected String doInBackground(String... param) 
		{
			
			filename=param[0];
			String file_server = Constant.BASE_URL_SERVER+ "version/" + param[0];
			String file_sdcard = Environment.getExternalStorageDirectory() + "/hocngoaingu/version/" + param[0];
			Log.e("----------download file: "+ param[0],"----------download file: "+ file_server);
			int count;
		    try
		    {
		    	    URL url = new URL(file_server);
		            in = new BufferedInputStream(url.openStream());
		            fout = new FileOutputStream(file_sdcard);
		 
		            byte data[] = new byte[1024];
		            Log.e("------Availabe download------",""+String.valueOf(in.available()));
		            if(in.available() > 0)
		            {
		            	download_ok=1;
		            	while ((count = in.read(data, 0, 1024)) != -1)
			            {
			                fout.write(data, 0, count);
			            }
		            }
		    } catch (MalformedURLException e) 
		    {
		    	e.printStackTrace(); download_ok=0;
		    } catch (IOException e) 
		    {
		    	e.printStackTrace(); download_ok=0;
		    }
		    finally
		    {
		    	try {    
		    		if (in != null) in.close();
		    		if (fout != null) fout.close();
		    	}
		    	catch (IOException e) 
		    	{
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
		    }
			return null;
		}

		@Override
		protected void onPostExecute(String unused) 
		{
			if(download_ok==1) { Log.e("-------download file data SUCCESS-------","-------download file data SUCCESS: "+download_ok); }
			else if(download_ok==0) 
			{
				Log.e("-------download file data FAIL-------","-------download file data FAIL-------");
				new DownloadDataFromServerProgress().execute(filename);
			}
		}
	}
	
	
	//
	//Check and download file data 
	//
	//
	class CheckServerUpdateProgress extends AsyncTask<String, String, String> 
	{	  
		int success111;
		int server_tudien;
		int server_thanhngu;
		int server_nguphap;
		int server_audio;
		int server_video;
		boolean isUpdate;
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			success111 = 0;
			isUpdate = false;
			Log.d("------check version-----","------check version-----");
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("request", "post_check_version"));
			
			String json_string = jsonParser.makeHttpRequest(Constant.BASE_URL_SERVER+"totnghiep_create_version.php", "POST", params);
			if(json_string.length() != 0)
			{
				JSONObject json;
				try {
					json = JSONParser.getJSONObjectFromJString(json_string);
					if(json.getInt("tudien") > 0 && json.getInt("thanhngu") > 0 && json.getInt("nguphap") > 0 && json.getInt("audio") > 0
							&& json.getInt("video") > 0 ) 
					{
						success111=1;
						
						server_tudien = json.getInt("tudien");
						server_thanhngu = json.getInt("thanhngu");
						server_nguphap = json.getInt("nguphap");
						server_audio = json.getInt("audio");
						server_video = json.getInt("video");
					}
					else success111=0; 
					
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else 
				{
					success111=3;
				}

			return null;
		}
		protected void onPostExecute(String file_url) 
		{
			if(success111==1)
			{
				if(server_tudien != all_item_tudien_sdcard && instantFunctionCheck.checkInternetConnection())
				{	
					Log.e("------Xoa va download tudien.txt-----","------Xoa va download video.txt-----");
					if(FunctionFolderAndFile.removeFileFromSdcard("version","tudien.txt"))
					{
						task_downloadfiledata = new DownloadDataFromServerProgress();
						task_downloadfiledata.execute("tudien.txt");
						isUpdate = true;
					}
				}
				if(server_thanhngu != all_item_thanhngu_sdcard && instantFunctionCheck.checkInternetConnection())
				{	
					Log.e("------Xoa va download thanhngu.txt-----","------thanhngu update DOWNLOAD-----");
					if(FunctionFolderAndFile.removeFileFromSdcard("version","thanhngu.txt"))
					{
						task_downloadfiledata = new DownloadDataFromServerProgress();
						task_downloadfiledata.execute("thanhngu.txt");
						isUpdate = true;
					}
				}
				if(server_nguphap != all_item_nguphap_sdcard && instantFunctionCheck.checkInternetConnection())
				{	
					Log.e("------Xoa va download nguphap.txt-----","------nguphap update DOWNLOAD-----");
					if(FunctionFolderAndFile.removeFileFromSdcard("version","nguphap.txt"))
					{
						task_downloadfiledata = new DownloadDataFromServerProgress();
						task_downloadfiledata.execute("nguphap.txt");
						isUpdate = true;
					}
				}
				if(server_audio != all_item_audio_sdcard && instantFunctionCheck.checkInternetConnection())
				{	
					Log.e("------Xoa va download audio.txt-----","------audio update DOWNLOAD-----");
					if(FunctionFolderAndFile.removeFileFromSdcard("version","audio.txt"))
					{
						task_downloadfiledata = new DownloadDataFromServerProgress();
						task_downloadfiledata.execute("audio.txt");
						isUpdate = true;
					}
				}
				if(server_video != all_item_video_sdcard && instantFunctionCheck.checkInternetConnection())
				{	
					Log.e("------------Xoa va download video.txt----------","------------Xoa va download video.txt----------");
					if(FunctionFolderAndFile.removeFileFromSdcard("version","video.txt"))
					{
						task_downloadfiledata = new DownloadDataFromServerProgress();
						task_downloadfiledata.execute("video.txt");
						isUpdate = true;
					}
				}
				if(instantFunctionCheck.checkInternetConnection())
				{	
					if( FunctionFolderAndFile.checkFileExist("version", "level.txt")==false )
					{
						
						Log.e("------------Down file LEVEL.txt----------","------------file LEVEL.txt----------");
						task_downloadfiledata = new DownloadDataFromServerProgress();
						task_downloadfiledata.execute("level.txt");
					}
				}
				if(isUpdate == true && instantFunctionCheck.checkInternetConnection())
				{	
					Log.e("------Xoa va download version.txt-----","------Xoa va download version.txt-----");
					if(FunctionFolderAndFile.removeFileFromSdcard("version","version.txt"))
					{
						task_downloadfiledata = new DownloadDataFromServerProgress();
						task_downloadfiledata.execute("version.txt");
					}
				}
					
			}
			if(success111==0) 
			{
				Log.d("---Success=0 lỗi internet","---Success=0 lỗi internet");
				new CheckServerUpdateProgress().execute();
			}
				
			if(success111==3) 
			{
				Log.d("---Success=3 JSON=rỗng", "---Success=3 JSON=rỗng");
				new CheckServerUpdateProgress().execute();
			}
		}

	}
	
	
	
	


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		Log.d("------  Main ActivityResult", String.format("--------- Main ActivityResult resultCode="+resultCode));
        if (requestCode == GET_CODE) 
        {
            if (resultCode == RESULT_CANCELED) 
            {} 
            else 
            {
                if (data != null && resultCode == RESULT_OK) 
                {
	                	b = data.getExtras();
	            		
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
	            		
	                	fm.getFragments().get(1).onActivityResult(requestCode, resultCode, data);
	                    mPager.setCurrentItem(1);
                }
                
            	if (data != null && resultCode == MainActivity.CHANGE_IMAGE) 
	            {
            		Log.e("MainActivity===>>result change hình dai dien","MainActivity===>>result change hình dai dien");	
            		fm.getFragments().get(1).onActivityResult(requestCode, resultCode, data);
	                mPager.setCurrentItem(1);
	            }
	        }
        }
    }
	@Override
	public void onBackPressed()
	{
	        if (doubleBackToExitPressedOnce) {
	            super.onBackPressed();
	            finish();
	            bmImg = null;
	        }
	        this.doubleBackToExitPressedOnce = true;
	        Toast.makeText(this, "Vui lòng nhấn lần nữa để thoát!", Toast.LENGTH_SHORT).show();
	        new Handler().postDelayed(new Runnable() {

	            @Override
	            public void run() 
	            {
	            	doubleBackToExitPressedOnce=false;   
	            }
	        }, 2000);
	 }
	//
	//Tab3 TuDien
	//
	
	static public class TuDien 
	{
		private String id; 
	    private String tu;
	    private String loaitu;
	    private String phienam;
	    private String dichnghia;
	    private String nhieunghia;
	    private String link_audio;
	    private int is_gone;
	    
	    public TuDien(String id, String tudien, String phienam, String loaitu, 
	    		String dichnghia, String nghiathem, String audio, int is_gone )
	    {
	        this.id = id;
	        this.tu = tudien;
	        this.loaitu = phienam;
	        this.phienam = loaitu;
	        this.dichnghia = dichnghia;
	        this.nhieunghia = nghiathem;
	        this.link_audio = audio;
	        this.is_gone=is_gone;
	    }
	    
	    public String getId() { return id;}
	    public String getTu() { return tu;}
	    public String getLoaitu() { return loaitu;}
	    public String getPhienam() { return phienam;}
	    
	    public String getDichnghia() { return dichnghia;}
	    public String getNhieunghia() { return nhieunghia;}
	    public String getLink_audio() { return link_audio;}
	    public int getIs_gone()		{return is_gone;}
	    
	 
	    public void setId(String id) {this.id = id;}
	    public void setTu(String tudien) {this.tu = tudien;}
	    public void setLoaitu(String tuloai) {this.loaitu = tuloai;}
	    public void setPhienam(String phienam) {this.phienam = phienam;}
	    
	    public void setDichnghia(String dichnghia) {this.dichnghia = dichnghia;}
	    public void setNhieunghia(String nghiathem) {this.nhieunghia = nghiathem;}
	    
	    public void setLink_audio(String audio) {this.link_audio = audio;}
	    public void setIs_gone(int isgone){this.is_gone=isgone;}
	}
	//
	//Tab4 thanh ngu
	//
	static public class ThanhNgu 
	{
		private String id; 
	    private String thanhngu;
	    private String noidung;
	    private String vidu;
	    private int is_gone;
	    
	    public ThanhNgu(String id, String thanhngu, String noidung, String vidu, int is_gone )
	    {
	        this.id = id;
	        this.thanhngu = thanhngu;
	        this.noidung = noidung;
	        this.vidu = vidu;
	        this.is_gone=is_gone;
	    }
	    public String getId() { return id;}
	    public String getThanhngu() { return thanhngu;}
	    public String getNoidung() { return noidung;}
	    public String getVidu() { return vidu;}
	    public int getIs_gone()		{return is_gone;}
	 
	    public void setId(String id) {this.id = id;}
	    public void setThanhngu(String tudien) {this.thanhngu = tudien;}
	    public void setNoidung(String tuloai) {this.noidung = tuloai;}
	    public void setVidu(String phienam) {this.vidu = phienam;}
	    public void setIs_gone(int isgone){this.is_gone=isgone;}
	}
	
	//
	//Tab5 Ngữ Pháp
	//
	static public class NguPhap 
	{
		private String id; 
	    private String tieude;
	    private String tomtat;
	    private String noidung;
	    private String luot_view;
	    public NguPhap(String id, String tieude, String tomtat, String noidung, String luot_view )
	    {
	        this.id = id;
	        this.tieude = tieude;
	        this.tomtat = tomtat;
	        this.noidung = noidung;
	        this.luot_view = luot_view;
	    }
	    public String getId() { return id;}
	    public String getTieude() { return tieude;}
	    public String getTomtat() { return tomtat;}
	    public String getNoidung() { return noidung;}
	    public String getLuot_view() { return luot_view;}
	    
	    public void setId(String id) {this.id = id;}
	    public void setTieude(String tieude) {this.tieude = tieude;}
	    public void setTomtat(String tomtat) {this.tomtat = tomtat;}
	    public void setNoidung(String noidung) {this.noidung = noidung;}
	    public void setLuot_view(String luot_view) {this.luot_view = luot_view;}
	}
	//
	//Tab6 Audio
	//
	static public class Audio 
	{
		private String id; 
	    private String tieude;
	    private String tomtat;
	    private String ngaydang;
	    private String luot_view;
	    public Audio(String id, String title, String text, String ngaytao, String luot_view )
	    {
	        this.id = id;
	        this.tieude = title;
	        this.tomtat = text;
	        this.ngaydang = ngaytao;
	        this.luot_view = luot_view;
	    }
	    public String getId() { return id;}
	    public String getTieude() { return tieude;}
	    public String getTomtat() { return tomtat;}
	    public String getNgaydang() { return ngaydang;}
	    public String getLuot_view() { return luot_view;}
	    
	    public void setId(String id) {this.id = id;}
	    public void setTieude(String tieude) {this.tieude = tieude;}
	    public void setTomtat(String tomtat) {this.tomtat = tomtat;}
	    public void setNgaydang(String noidung) {this.ngaydang = noidung;}
	    public void setLuot_view(String luot_view) {this.luot_view = luot_view;}
	}
	//
	//Tab7 Video
	//
	static public class Video 
	{
		private String id; 
	    private String title;
	    private String mo_ta;
	    private String link;
	    private String ngay_tao;
	    private String luot_view;
	    public Video(String id, String title,String mo_ta, String link, String ngay_tao, String luot_view )
	    {
	        this.id = id;
	        this.title = title;
	        this.mo_ta = mo_ta;
	        this.link = link;
	        this.ngay_tao = ngay_tao;
	        this.luot_view = luot_view;
	    }
	    public String getId() { return id;}
	    public String getTitle() { return title;}
	    public String getMo_ta() { return mo_ta;}
	    public String getLink() { return link;}
	    public String getNgay_tao() { return ngay_tao;}
	    public String getLuot_view() { return luot_view;}
	    
	    public void setId(String id) {this.id = id;}
	    public void setTitle(String title) {this.title = title;}
	    public void setMo_ta(String mota) {this.mo_ta = mota;}
	    public void setLink(String link) {this.link = link;}
	    public void setNgay_tao(String ngay_tao) {this.ngay_tao = ngay_tao;}
	    public void setLuot_view(String luot_view) {this.luot_view = luot_view;}
	}
	//
	//Tab8 TracNghiem
	//
	static public class TracNghiem 
	{
		private String id; 
	    private String title;
	    private String text;
	    public TracNghiem(String id, String title,String text)
	    {
	        this.id = id;
	        this.title = title;
	        this.text = text;
	    }
	    public String getId() { return id;}
	    public String getTitle() { return title;}
	    public String getText() { return text;}
//	    public String getNgay_tao() { return ngay_tao;}
	    
	    public void setId(String id) {this.id = id;}
	    public void setTitle(String title) {this.title = title;}
	    public void setMo_ta(String mota) {this.text = mota;}
//	    public void setNgay_tao(String ngay_tao) {this.ngay_tao = ngay_tao;}
	}
	//
	//Tab10 Kết Quả
	//
	static public class KetQua 
	{
		private String id; 
	    private String title;
	    private String diemso;
	    private String ngaythi;
	    private String socauhoi;
	    private String socaudung;
	    
	    public KetQua(String id, String title,String diemso,String ngaythi, String socauhoi, String socaudung)
	    {
	        this.id = id;
	        this.title = title;
	        this.diemso = diemso;
	        this.ngaythi = ngaythi;
	        this.socauhoi = socauhoi;
	        this.socaudung = socaudung;
	    }
	    public String getId() { return id;}
	    public String getTitle() { return title;}
	    public String getDiemso() { return diemso;}
	    public String getNgaythi() { return ngaythi;}
	    public String getSocauhoi() { return socauhoi;}
	    public String getSocaudung() { return socaudung;}
	    
	    public void setId(String id) {this.id = id;}
	    public void setTitle(String title) {this.title = title;}
	    public void setDiemso(String diemso) {this.diemso = diemso;}
	    public void setNgaythi(String ngaythi) {this.ngaythi= ngaythi;}
	    public void setSocauhoi(String socauhoi) {this.socauhoi = socauhoi;}
	    public void setSocaudung(String socaudung) {this.socaudung = socaudung;}
	}
}
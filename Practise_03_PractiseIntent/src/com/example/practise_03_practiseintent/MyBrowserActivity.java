package com.example.practise_03_practiseintent;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyBrowserActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_browser);
		
		Uri url = this.getIntent().getData();
		WebView webView = (WebView)findViewById(R.id.Webview01);
		webView.setWebViewClient(new Callback());
		webView.loadUrl(url.toString());
	}

	private class Callback extends WebViewClient
	{

		@Override
		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
			// TODO Auto-generated method stub
			//return super.shouldOverrideKeyEvent(view, event);
			return false;
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_browser, menu);
		return true;
	}

}

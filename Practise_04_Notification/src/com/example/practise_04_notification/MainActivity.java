package com.example.practise_04_notification;

import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	int notificationID = 1;
	/**Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button button = (Button)findViewById(R.id.btn_displaynotif);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				displayNotification();
			}
		});
	}
	
	protected void displayNotification() {
		//---Pending Intent to launch activity if the user selects 
		// this notification ---
		Intent i = new Intent(this, NotificationView.class);
		i.putExtra("notificationID", notificationID);
		
		//PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
		NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		Log.d("event", System.currentTimeMillis()+"");
		
		Notification notif = 
				new Notification(
						R.drawable.ic_launcher,
						"Reminder: Meeting starts in 5 minutes", 
						System.currentTimeMillis());
		
		CharSequence from = "System Alarm";
		CharSequence message = "Meeting with customer at 3pm...";
		
		notif.setLatestEventInfo(this, from, message, pendingIntent);
		notif.defaults |= Notification.DEFAULT_SOUND;
		//notif.sound = Uri.parse("file:///sdcard/notification/ringer.mp3");
		notif.defaults |= Notification.DEFAULT_LIGHTS;
		notif.defaults |= Notification.DEFAULT_VIBRATE;
		//long[] vibrate = {0,100,200,300};
		//notif.vibrate = vibrate;
		//--100ms delay, vibrate for 250ms, pause for 100 ms and then vibrate for 500ms
		
		//notif.vibrate = new long[] {100,250,100,50000};
		nm.notify(notificationID, notif);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

package edu.ksu.cis.android.project3;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AntiTheftMain extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        }
    
	public void startAlarmService(View view) {
		
		//CREATE NOTIFICATION
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = "Anti Theft Service Started";
		Long when = System.currentTimeMillis();
		
		Notification notification = new Notification(icon, tickerText, when);
		
		Context context = getApplicationContext();
		CharSequence contentTitle = "Anti Theft Service";
		CharSequence contentText = "Status: Running";
		Intent notificationIntent = new Intent(this, AntiTheftService.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notification.flags |= Notification.FLAG_NO_CLEAR;
		final int HELLO_ID = 1;

		mNotificationManager.notify(HELLO_ID, notification);
		startService(new Intent(this, AntiTheftService.class));
	}

	public void stopAlarmService(View view) {
		
		stopService(new Intent(this, AntiTheftService.class));
	}
}
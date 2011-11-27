package edu.ksu.cis.android.project3;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class AntiTheftMain extends Activity {
	
	NotificationManager mNotificationManager;
	final int NOTIFICATION_ID = 1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        String ns = Context.NOTIFICATION_SERVICE;
        mNotificationManager = (NotificationManager) getSystemService(ns);
    }
    
	public void startAlarmService(View view) 
	{
		int delay=1000;
		RadioButton rb1=(RadioButton)findViewById(R.id.radio0);
		RadioButton rb2=(RadioButton)findViewById(R.id.radio1);
		RadioButton rb3=(RadioButton)findViewById(R.id.radio2);
		if(rb1.isChecked())
		{
			delay = 1000;
		}
		else if(rb2.isChecked())
		{
			delay = 5000;
		}
		else if(rb3.isChecked())
		{
			delay = 10000;
		}
		
		//CREATE NOTIFICATION		
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = "Anti Theft Service Started";
		Long when = System.currentTimeMillis();
		
		Notification notification = new Notification(icon, tickerText, when);
		
		Context context = getApplicationContext();
		CharSequence contentTitle = "Anti Theft Service";
		CharSequence contentText = "Status: Running";
		Intent notificationIntent = new Intent(this, AntiTheftMain.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notification.flags |= Notification.FLAG_NO_CLEAR;
	

		mNotificationManager.notify(NOTIFICATION_ID, notification);
	}

	public void stopAlarmService(View view) {
		
		mNotificationManager.cancel(NOTIFICATION_ID);
		stopService(new Intent(this, AntiTheftService.class));
	}
}
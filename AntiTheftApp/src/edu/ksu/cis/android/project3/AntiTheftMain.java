package edu.ksu.cis.android.project3;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
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
		
		startService(new Intent(this, AntiTheftService.class));
	}

	public void stopAlarmService(View view) {
		
		stopService(new Intent(this, AntiTheftService.class));
	}
}
package edu.ksu.cis.android.project3;

import android.app.Activity;
import android.app.ActivityManager;
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
		if(!isMyServiceRunning())
		{
			startService(new Intent(this, AntiTheftService.class));
		}
	}

	public void stopAlarmService(View view) {
		if(isMyServiceRunning())
		{
			stopService(new Intent(this, AntiTheftService.class));
		}
		final MediaPlayer mp = MediaPlayer.create(getBaseContext(), R.raw.alarm);
        mp.start();
        mp.setOnCompletionListener(new OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
	}
	
	private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if ("edu.ksu.cis.android.project3.AntiTheftService".equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}

}
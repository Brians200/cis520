package edu.ksu.cis.android.project3;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;

public class AntiTheftService extends Service implements SensorEventListener{

	boolean alarmRunning;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
    private long lastUpdate = -1;
 
	long timeBeforeAlarmGoesOff = 5000;
    boolean isTimerRunning;
    Timer timer;
    
    MediaPlayer mp;
    
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		alarmRunning = false;
		isTimerRunning=false;
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		 
	}

	

	@Override
	public void onDestroy() {
		alarmRunning = false;
		if(isTimerRunning)
		{
			isTimerRunning=false;
			timer.cancel();
		}
		if(mp!=null&&mp.isPlaying())
		{
			mp.stop();
		}
		mSensorManager.unregisterListener(this);
		

	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		alarmRunning = true;
		isTimerRunning=false;
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);	    
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
			float[] values = event.values;
			// Movement
			float x = values[0];
			float y = values[1];
			float z = values[2];

			float accelationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
			long actualTime = System.currentTimeMillis();
			if (accelationSquareRoot >= 1) //
			{
				if (actualTime - lastUpdate < 200) {
					return;
				}
				lastUpdate = actualTime;
				//Device was moved

				if(!isTimerRunning)
				{
					isTimerRunning=true;
					timer = new Timer();
					timer.schedule(new SetOffAlarm(),timeBeforeAlarmGoesOff);
				}
			}

		}


	}
	
	class SetOffAlarm extends TimerTask {
		public void run() {
	
			AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

			//NOT REALLY SURE WHAT THE FLAG WILL BE using 0 for now?
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);

			//stop sound if already playing
			if(mp!=null&&mp.isPlaying())
			{
				mp.stop();
			}
			mp = MediaPlayer.create(getBaseContext(), R.raw.baby);
		
			//Loop until the alarm is silenced
			mp.setLooping(true);
		
            mp.start();
            mp.setOnCompletionListener(new OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
			
            alarmRunning = false;
   			isTimerRunning=false;
			timer.cancel(); //Not necessary because we call System.exit
			//System.exit(0); //Stops the AWT thread (and everything else)
		}
	}
}

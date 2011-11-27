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
import android.os.Bundle;
import android.os.IBinder;

public class AntiTheftService extends Service
{
	private SensorManager mSensorManager;
	private float mAccel; // acceleration apart from gravity
	private float mAccelCurrent; // current acceleration including gravity
	private float mAccelLast; // last acceleration including gravity

	private long alarmDelay = 3000;
	Timer timer;
	MediaPlayer mp;
	
	private long currentMovementTime = 0;
	private long previousMovementTime = System.currentTimeMillis();
	
	private final SensorEventListener mSensorListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent se) {
			float x = se.values[0];
	    	float y = se.values[1];
	    	float z = se.values[2];
	    	mAccelLast = mAccelCurrent;
	    	mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
	    	float delta = mAccelCurrent - mAccelLast;
	    	mAccel = mAccel * 0.9f + delta; // perform low-cut filter
	    }

	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    
	    }
	  };

	  protected void onResume() {
		  mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	  }

	  protected void onStop() {
	    mSensorManager.unregisterListener(mSensorListener);
	  }

   
    
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {

	}

	

	@Override
	public void onDestroy() {
		timer.cancel();
		if(mp!=null&&mp.isPlaying())
		{
			mp.stop();
		}
		

	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	    //Poll this to find acceleration
	    mAccel = 0.00f;
	    mAccelCurrent = SensorManager.GRAVITY_EARTH;
	    mAccelLast = SensorManager.GRAVITY_EARTH;
	    
	    timer = new Timer();
		//checks every 100 ms for movement
		timer.scheduleAtFixedRate(new TriggerAlarmQuestionMark(), 0,10);
		currentMovementTime = 0;
		previousMovementTime = System.currentTimeMillis();
		Bundle extras = intent.getExtras();
		alarmDelay = extras.getInt("Time");

	}
	
	
	public void StartAlarmTimer()
	{
		timer.cancel();
		timer = new Timer();
		timer.schedule(new PlayAlarmSound(),alarmDelay);
	}
	
	

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	
	
	class TriggerAlarmQuestionMark extends TimerTask {
		public void run() {
			long timeDifference = System.currentTimeMillis()-previousMovementTime;
			if(mAccel>.5)
			{
				currentMovementTime+=timeDifference;
				if(currentMovementTime>=1000)
				{
					StartAlarmTimer();
				}
			}
			else
			{
				currentMovementTime = 0;
			}
		}
	}
	
	class PlayAlarmSound extends TimerTask {
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
			
			timer.cancel(); //Not necessary because we call System.exit
			//System.exit(0); //Stops the AWT thread (and everything else)
		}
	}
}

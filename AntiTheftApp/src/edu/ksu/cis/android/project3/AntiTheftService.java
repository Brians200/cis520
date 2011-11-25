package edu.ksu.cis.android.project3;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

public class AntiTheftService extends Service implements SensorEventListener{

	boolean alarmRunning;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
    private long lastUpdate = -1;
 
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		alarmRunning = false;
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		 
	}

	

	@Override
	public void onDestroy() {
		alarmRunning = false;
		mSensorManager.unregisterListener(this);

	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		alarmRunning = true;
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
			if (accelationSquareRoot >= 2) //
			{
				if (actualTime - lastUpdate < 200) {
					return;
				}
				lastUpdate = actualTime;
				//Device was moved
				int X = 3;
				
			}

		}


	}
	
	
}

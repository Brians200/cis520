package edu.ksu.cis.android.project3;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.IBinder;

@SuppressWarnings("deprecation")
public class AntiTheftService extends Service implements SensorEventListener{

	boolean alarmRunning;
	private SensorManager sensorMgr;
    private long lastUpdate = -1;
    private float x, y, z;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 800;
 

	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		alarmRunning = false;
		
		sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
		boolean accelSupported = sensorMgr.registerListener((SensorListener) this,
			SensorManager.SENSOR_ACCELEROMETER,
			SensorManager.SENSOR_DELAY_GAME);
	 
		if (!accelSupported) {
		    // on accelerometer on this device
		    sensorMgr.unregisterListener((SensorListener) this,
	                SensorManager.SENSOR_ACCELEROMETER);
		}

	}

	@Override
	public void onDestroy() {
		alarmRunning = false;
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		alarmRunning = true;
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.equals(SensorManager.SENSOR_ACCELEROMETER)) {
		    long curTime = System.currentTimeMillis();
		    // only allow one update every 100ms.
		    if ((curTime - lastUpdate) > 100) {
			long diffTime = (curTime - lastUpdate);
			lastUpdate = curTime;
			x = event.values[SensorManager.DATA_X];
			y = event.values[SensorManager.DATA_Y];
			z = event.values[SensorManager.DATA_Z];
	 
			float speed = Math.abs(x+y+z - last_x - last_y - last_z)
	                              / diffTime * 10000;
			if (speed > SHAKE_THRESHOLD) {
			    int x= 3;
			    x=x+3;
			}
			last_x = x;
			last_y = y;
			last_z = z;
		    }
		}

	}
	
	
}

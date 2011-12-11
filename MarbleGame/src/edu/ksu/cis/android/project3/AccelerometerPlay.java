package edu.ksu.cis.android.project3;
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.Preference;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * This is an example of using the accelerometer to integrate the device's
 * acceleration to a position using the Verlet method. This is illustrated with
 * a very simple particle system comprised of a few iron balls freely moving on
 * an inclined wooden table. The inclination of the virtual table is controlled
 * by the device's accelerometer.
 * 
 * @see SensorManager
 * @see SensorEvent
 * @see Sensor
 */

public class AccelerometerPlay extends Activity {

    private SimulationView mSimulationView;
    private SensorManager mSensorManager;
    private PowerManager mPowerManager;
    private WindowManager mWindowManager;
    private Display mDisplay;
    private WakeLock mWakeLock;
    
    //Our implementation
    private long score = 0;
    private long lastTime = 0;
    final private String highScorePreference = "high_scores";
    //Our implementation

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get an instance of the SensorManager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get an instance of the PowerManager
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);

        // Get an instance of the WindowManager
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();

        // Create a bright wake lock
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass()
                .getName());
        
        //Our implementation
        lastTime = System.currentTimeMillis();
        SharedPreferences sp = getPreferences(0);
        SharedPreferences.Editor editor = sp.edit();
        if(sp.getLong("firstHighScore",-1)==-1)
        {
        	editor.putLong("firstHighScore", 0);
        }
        if(sp.getLong("secondHighScore",-1)==-1)
        {
        	editor.putLong("secondHighScore", 0);
        }
        if(sp.getLong("thirdHighScore",-1)==-1)
        {
        	editor.putLong("thirdHighScore", 0);
        }
        if(sp.getLong("fourthHighScore",-1)==-1)
        {
        	editor.putLong("fourthHighScore", 0);
        }
        if(sp.getLong("fifthHighScore",-1)==-1)
        {
        	editor.putLong("fifthHighScore", 0);
        }
        if(sp.getLong("sixthHighScore",-1)==-1)
        {
        	editor.putLong("sixthHighScore", 0);
        }
        if(sp.getLong("seventhHighScore",-1)==-1)
        {
        	editor.putLong("seventhHighScore", 0);
        }
        if(sp.getLong("eighthHighScore",-1)==-1)
        {
        	editor.putLong("eighthHighScore", 0);
        }
        if(sp.getLong("ninthHighScore",-1)==-1)
        {
        	editor.putLong("ninthHighScore", 0);
        }
        if(sp.getLong("tenthHighScore",-1)==-1)
        {
        	editor.putLong("tenthHighScore", 0);
        }
        
        //Our implementation

        // instantiate our simulation view and set it as the activity's content
        mSimulationView = new SimulationView(this);
        setContentView(mSimulationView);
    }
    
    //Our implementation
    public void resumeTheGame(View view)
    {
    	setContentView(mSimulationView);
    	onResume();
    }
    
    public void pauseTheGame(View view)
    {
    	onPause();
    	showMainMenu(view);
    }
    
    public void showMainMenu(View view)
    {
    	setContentView(R.layout.main);
    }
    
    public void resetHighScores()
    {
    	SharedPreferences sp = getPreferences(0);
    	SharedPreferences.Editor editor = sp.edit();
    	editor.putLong("firstHighScore", 0);
    	editor.putLong("secondHighScore", 0);
    	editor.putLong("thirdHighScore", 0);
    	editor.putLong("fourthHighScore", 0);
    	editor.putLong("fifthHighScore", 0);
    	editor.putLong("sixthHighScore", 0);
    	editor.putLong("seventhHighScore", 0);
    	editor.putLong("eighthHighScore", 0);
    	editor.putLong("ninthHighScore", 0);
    	editor.putLong("tenthHighScore", 0);
    	editor.commit();
    }
    
    public void resetGame(View view)
    {
    	score = 0;
    	lastTime = System.currentTimeMillis();
    	mSimulationView.stopSimulation();
    	mSimulationView = new SimulationView(this);
    	setContentView(mSimulationView);
    	onResume();
    }
    
    public void showHighScores(View view)
    {
    	setContentView(R.layout.high_scores);
    	//TODO: going to have to change this to read from the file
    	
    	SharedPreferences scores =  getPreferences(0);
    	
    	
    	//textView2 - textView11 are the TextViews with the high scores on the high scores page
    	TextView tv = (TextView) findViewById(R.id.textView2);
    	tv.setText("1. " + scores.getLong("firstHighScore", 0));
    	
    	tv = (TextView) findViewById(R.id.textView3);
    	tv.setText("2. " + scores.getLong("secondHighScore", 0));
    	
    	tv = (TextView) findViewById(R.id.textView4);
    	tv.setText("3. " + scores.getLong("thirdHighScore", 0));
    	
    	tv = (TextView) findViewById(R.id.textView5);
    	tv.setText("4. " + scores.getLong("fourthHighScore", 0));
    	
    	tv = (TextView) findViewById(R.id.textView6);
    	tv.setText("5. " + scores.getLong("fifthHighScore", 0));
    	
    	tv = (TextView) findViewById(R.id.textView7);
    	tv.setText("6. " + scores.getLong("sixthHighScore", 0));
    	
    	tv = (TextView) findViewById(R.id.textView8);
    	tv.setText("7. " + scores.getLong("seventhHighScore", 0));
    	
    	tv = (TextView) findViewById(R.id.textView9);
    	tv.setText("8. " + scores.getLong("eighthHighScore", 0));
    	
    	tv = (TextView) findViewById(R.id.textView10);
    	tv.setText("9. " + scores.getLong("ninthHighScore", 0));
    	
    	tv = (TextView) findViewById(R.id.textView11);
    	tv.setText("10. " + scores.getLong("tenthHighScore", 0));
    }
    
    public void endGame()
    {
    	setContentView(R.layout.end_game);
    	long temp = System.currentTimeMillis();
    	
    	TextView tv = (TextView)findViewById(R.id.textView13);
    	score += temp - lastTime;
    	tv.setText("Your score is: " + score);
    	
    	long highScores[] = new long[10];
    	SharedPreferences sp = getPreferences(0);
    	highScores[0] = sp.getLong("firstHighScore", 0);
    	highScores[1] = sp.getLong("secondHighScore", 0);
    	highScores[2] = sp.getLong("thirdHighScore", 0);
    	highScores[3] = sp.getLong("fourthHighScore", 0);
    	highScores[4] = sp.getLong("fifthHighScore", 0);
    	highScores[5] = sp.getLong("sixthHighScore", 0);
    	highScores[6] = sp.getLong("seventhHighScore", 0);
    	highScores[7] = sp.getLong("eighthHighScore", 0);
    	highScores[8] = sp.getLong("ninthHighScore", 0);
    	highScores[9] = sp.getLong("tenthHighScore", 0);
    	
    	highScores = insertHighScore(score, highScores);
    	
    	SharedPreferences.Editor editor = sp.edit();
    	editor.putLong("firstHighScore", highScores[0]);
    	editor.putLong("secondHighScore", highScores[1]);
    	editor.putLong("thirdHighScore", highScores[2]);
    	editor.putLong("fourthHighScore", highScores[3]);
    	editor.putLong("fifthHighScore", highScores[4]);
    	editor.putLong("sixthHighScore", highScores[5]);
    	editor.putLong("seventhHighScore", highScores[6]);
    	editor.putLong("eighthHighScore", highScores[7]);
    	editor.putLong("ninthHighScore", highScores[8]);
    	editor.putLong("tenthHighScore", highScores[9]);
    }
    
    protected long[] insertHighScore(long score, long[] highScores)
    {
    	long[] retVal = highScores;
    	long temp = score;
    	long temp2;
    	int count = 0;
    	while(count < 10)
    	{
    		if(retVal[count] < temp)
    		{
    			temp2 = retVal[count];
    			retVal[count] = temp;
    			temp = temp2;
    		}
    		count++;
    	}
    	return retVal;
    }
    //Our implementation

    @Override
    protected void onResume() {
        super.onResume();
        /*
         * when the activity is resumed, we acquire a wake-lock so that the
         * screen stays on, since the user will likely not be fiddling with the
         * screen or buttons.
         */
        mWakeLock.acquire();

        //Our implementation
        lastTime = System.currentTimeMillis();
        //Our implementation
        
        // Start the simulation
        mSimulationView.startSimulation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*
         * When the activity is paused, we make sure to stop the simulation,
         * release our sensor resources and wake locks
         */
        
        //Our implementation
        long temp = System.currentTimeMillis();
        score += temp - lastTime;
        //Our implementation

        // Stop the simulation
        mSimulationView.stopSimulation();

        // and release our wake-lock
        mWakeLock.release();
    }

    class SimulationView extends View implements SensorEventListener {
        // diameter of the balls in meters
        private static final float sBallDiameter = 0.004f;
        private static final float sBallDiameter2 = sBallDiameter * sBallDiameter;

        // friction of the virtual table and air
        private static final float sFriction = 0.1f;

        private Sensor mAccelerometer;
        private long mLastT;
        private float mLastDeltaT;

        private float mXDpi;
        private float mYDpi;
        private float mMetersToPixelsX;
        private float mMetersToPixelsY;
        private Bitmap mBitmap;
        private Bitmap mWood;
        private float mXOrigin;
        private float mYOrigin;
        private float mSensorX;
        private float mSensorY;
        private long mSensorTimeStamp;
        private long mCpuTimeStamp;
        private float mHorizontalBound;
        private float mVerticalBound;
        private final ParticleSystem mParticleSystem = new ParticleSystem();
        
        //Our Implementation
        private float mScreenWidth;
        private float mScreenHeight;
        
        private ArrayList<float[]> walls;
        private float[] circle;
        private int whichMaze;
        //Our Implementation

        /*
         * Each of our particle holds its previous and current position, its
         * acceleration. for added realism each particle has its own friction
         * coefficient.
         */
        class Particle {
            private float mPosX;
            private float mPosY;
            private float mAccelX;
            private float mAccelY;
            private float mLastPosX;
            private float mLastPosY;
            private float mOneMinusFriction;

            Particle() {
                // make each particle a bit different by randomizing its
                // coefficient of friction
                final float r = ((float) Math.random() - 0.5f) * 0.2f;
                mOneMinusFriction = 1.0f - sFriction + r;
            }

            public void computePhysics(float sx, float sy, float dT, float dTC) {
                // Force of gravity applied to our virtual object
                final float m = 1000.0f; // mass of our virtual object
                final float gx = -sx * m;
                final float gy = -sy * m;

                /*
                 * �F = mA <=> A = �F / m We could simplify the code by
                 * completely eliminating "m" (the mass) from all the equations,
                 * but it would hide the concepts from this sample code.
                 */
                final float invm = 1.0f / m;
                final float ax = gx * invm;
                final float ay = gy * invm;

                /*
                 * Time-corrected Verlet integration The position Verlet
                 * integrator is defined as x(t+�t) = x(t) + x(t) - x(t-�t) +
                 * a(t)�t�2 However, the above equation doesn't handle variable
                 * �t very well, a time-corrected version is needed: x(t+�t) =
                 * x(t) + (x(t) - x(t-�t)) * (�t/�t_prev) + a(t)�t�2 We also add
                 * a simple friction term (f) to the equation: x(t+�t) = x(t) +
                 * (1-f) * (x(t) - x(t-�t)) * (�t/�t_prev) + a(t)�t�2
                 */
                final float dTdT = dT * dT;
                final float x = mPosX + mOneMinusFriction * dTC * (mPosX - mLastPosX) + mAccelX
                        * dTdT;
                final float y = mPosY + mOneMinusFriction * dTC * (mPosY - mLastPosY) + mAccelY
                        * dTdT;
                mLastPosX = mPosX;
                mLastPosY = mPosY;
                mPosX = x;
                mPosY = y;
                mAccelX = ax;
                mAccelY = ay;
            }

            /*
             * Resolving constraints and collisions with the Verlet integrator
             * can be very simple, we simply need to move a colliding or
             * constrained particle in such way that the constraint is
             * satisfied.
             */
            public void resolveCollisionWithBounds() {
                final float xmax = mHorizontalBound;
                final float ymax = mVerticalBound;
                final float x = mPosX;
                final float y = mPosY;
                
                final float circleX = (((circle[0] - mXOrigin)/mMetersToPixelsX) - sBallDiameter);
                final float circleY = (((circle[1] - mYOrigin)/mMetersToPixelsY));
                double xSquared = (x - circleX) * (x - circleX);
                double ySquared = (y - circleY) * (y - circleY);
                double distance = Math.sqrt(xSquared + ySquared);
                if(distance < .002)
                {
                	endGame();
                }
                
                if (x > xmax) {
                    mPosX = xmax;
                } else if (x < -xmax) {
                    mPosX = -xmax;
                }
                if (y > ymax) {
                    mPosY = ymax;
                } else if (y < -ymax) {
                    mPosY = -ymax;
                }
                
                float[] wall;
                float[] wall1 = new float[4];
                int min;
                for(int for_i = 0; for_i < walls.size(); for_i++)
                {
                	wall = walls.get(for_i);
                	wall1[0] = (((wall[0] - mXOrigin)/mMetersToPixelsX) - sBallDiameter);
                	wall1[1] = (((wall[1] - mYOrigin)/mMetersToPixelsY));
                	wall1[2] = (((wall[2] - mXOrigin)/mMetersToPixelsX));
                	wall1[3] = (((wall[3] - mYOrigin)/mMetersToPixelsY) + sBallDiameter);
                	if((x > wall1[0] && x < wall1[2]) && (y > wall1[1] && y < wall1[3]))
                	{
                		min = findMin(Math.abs(x-wall1[0]), Math.abs(y-wall1[1]), Math.abs(x-wall1[2]), Math.abs(y-wall1[3]));
                		if(min == 0 || min == 2)
                		{
                			mPosX = wall1[min];
                		}
                		else
                		{
                			mPosY = wall1[min];
                		}
                	}
                }
            }
        }
        
        private int findMin(float one, float two, float three, float four)
        {
        	if(one < two)
        	{
        		if(one < three)
        		{
        			if(one < four)
        			{
        				return 0;
        			}
        			else
        			{
        				return 3;
        			}
        		}
        		else
        		{
        			if(three < four)
        			{
        				return 2;
        			}
        			else
        			{
        				return 3;
        			}
        		}
        	}
        	else
        	{
        		if(two < three)
        		{
        			if(two < four)
        			{
        				return 1;
        			}
        			else
        			{
        				return 3;
        			}
        		}
        		else
        		{
        			if(three < four)
        			{
        				return 2;
        			}
        			else
        			{
        				return 3;
        			}
        		}
        	}
        }

        /*
         * A particle system is just a collection of particles
         */
        class ParticleSystem {
            static final int NUM_PARTICLES = 1;
            private Particle mBalls[] = new Particle[NUM_PARTICLES];

            ParticleSystem() {
                /*
                 * Initially our particles have no speed or acceleration
                 */
                for (int i = 0; i < mBalls.length; i++) {
                    mBalls[i] = new Particle();
                }
            }

            /*
             * Update the position of each particle in the system using the
             * Verlet integrator.
             */
            private void updatePositions(float sx, float sy, long timestamp) {
                final long t = timestamp;
                if (mLastT != 0) {
                    final float dT = (float) (t - mLastT) * (1.0f / 1000000000.0f);
                    if (mLastDeltaT != 0) {
                        final float dTC = dT / mLastDeltaT;
                        final int count = mBalls.length;
                        for (int i = 0; i < count; i++) {
                            Particle ball = mBalls[i];
                            ball.computePhysics(sx, sy, dT, dTC);
                        }
                    }
                    mLastDeltaT = dT;
                }
                else
                {
                    //is this maybe where we would set the starting position of the ball?
                }
                mLastT = t;
            }

            /*
             * Performs one iteration of the simulation. First updating the
             * position of all the particles and resolving the constraints and
             * collisions.
             */
            public void update(float sx, float sy, long now) {
                // update the system's positions
                updatePositions(sx, sy, now);

                // We do no more than a limited number of iterations
                final int NUM_MAX_ITERATIONS = 10;

                /*
                 * Resolve collisions, each particle is tested against every
                 * other particle for collision. If a collision is detected the
                 * particle is moved away using a virtual spring of infinite
                 * stiffness.
                 */
                boolean more = true;
                final int count = mBalls.length;
                for (int k = 0; k < NUM_MAX_ITERATIONS && more; k++) {
                    more = false;
                    for (int i = 0; i < count; i++) {
                        Particle curr = mBalls[i];
                        for (int j = i + 1; j < count; j++) {
                            Particle ball = mBalls[j];
                            float dx = ball.mPosX - curr.mPosX;
                            float dy = ball.mPosY - curr.mPosY;
                            float dd = dx * dx + dy * dy;
                            // Check for collisions
                            if (dd <= sBallDiameter2) {
                                /*
                                 * add a little bit of entropy, after nothing is
                                 * perfect in the universe.
                                 */
                                dx += ((float) Math.random() - 0.5f) * 0.0001f;
                                dy += ((float) Math.random() - 0.5f) * 0.0001f;
                                dd = dx * dx + dy * dy;
                                // simulate the spring
                                final float d = (float) Math.sqrt(dd);
                                final float c = (0.5f * (sBallDiameter - d)) / d;
                                curr.mPosX -= dx * c;
                                curr.mPosY -= dy * c;
                                ball.mPosX += dx * c;
                                ball.mPosY += dy * c;
                                more = true;
                            }
                        }
                        /*
                         * Finally make sure the particle doesn't intersects
                         * with the walls.
                         */
                        curr.resolveCollisionWithBounds();
                    }
                }
            }

            public int getParticleCount() {
                return mBalls.length;
            }

            public float getPosX(int i) {
                return mBalls[i].mPosX;
            }

            public float getPosY(int i) {
                return mBalls[i].mPosY;
            }
        }

        public void startSimulation() {
            /*
             * It is not necessary to get accelerometer events at a very high
             * rate, by using a slower rate (SENSOR_DELAY_UI), we get an
             * automatic low-pass filter, which "extracts" the gravity component
             * of the acceleration. As an added benefit, we use less power and
             * CPU resources.
             */
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        }

        public void stopSimulation() {
            mSensorManager.unregisterListener(this);
        }

        public SimulationView(Context context) {
            super(context);
            ((View)this).setOnClickListener(new OnClickListener()
            {
            	public void onClick(View v)
            	{
            		pauseTheGame(v);
            	}
            });
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            mXDpi = metrics.xdpi;
            mYDpi = metrics.ydpi;
            mMetersToPixelsX = mXDpi / 0.0254f;
            mMetersToPixelsY = mYDpi / 0.0254f;

            // rescale the ball so it's about 0.5 cm on screen
            Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
            final int dstWidth = (int) (sBallDiameter * mMetersToPixelsX + 0.5f);
            final int dstHeight = (int) (sBallDiameter * mMetersToPixelsY + 0.5f);
            mBitmap = Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true);

            Options opts = new Options();
            opts.inDither = true;
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            mWood = BitmapFactory.decodeResource(getResources(), R.drawable.wood, opts);
            
            //here we can decide what maze to use, we'll randomly select one and call the corresponding method
            Random r = new Random();
            whichMaze = r.nextInt(3);
            if(whichMaze == 0)
            {
            	walls = generateWalls();
            	circle = new float[2];
            	circle[0] = 627.5f;
            	circle[1] = 363.5f;
            }
            else if(whichMaze == 1)
            {
            	walls = generateWalls2();
            	circle = new float[2];
            	circle[0] = 50;
            	circle[1] = 363.5f;
            }
            else
            {
                walls = generateWalls3();
                circle = new float[2];
                circle[0] = 50;
                circle[1] = 363.5f;
            }
        }
        
        private ArrayList<float[]> generateWalls3()
        {
        	ArrayList<float[]> retVal = new ArrayList<float[]>();
        	float[] wall = new float[4];
        	wall[0] = 100;
        	wall[1] = 63.5f;
        	wall[2] = 1280;
        	wall[3] = 113.5f;
        	retVal.add(wall);
        	
        	wall = new float[4];
        	wall[0] = 100;
        	wall[1] = 663.5f;
        	wall[2] = 1280;
        	wall[3] = 713.5f;
        	retVal.add(wall);
        	
        	wall = new float[4];
        	wall[0] = 1180;
        	wall[1] = 63.5f;
        	wall[2] = 1280;
        	wall[3] = 713.5f;
        	retVal.add(wall);
        	
        	wall = new float[4];
        	wall[0] = 200;
        	wall[1] = 263.5f;
        	wall[2] = 1000;
        	wall[3] = 313.5f;
        	retVal.add(wall);
        	
        	wall = new float[4];
        	wall[0] = 200;
        	wall[1] = 413.5f;
        	wall[2] = 1000;
        	wall[3] = 463.5f;
        	retVal.add(wall);
        	
        	wall = new float[4];
        	wall[0] = 200;
        	wall[1] = 263.5f;
        	wall[2] = 300;
        	wall[3] = 463.5f;
        	retVal.add(wall);
        	return retVal;
        }
        
        private ArrayList<float[]> generateWalls2()
        {
        	ArrayList<float[]> retVal = new ArrayList<float[]>();
        	float[] wall = new float[4];
        	wall[0] = 100;
        	wall[1] = 163.5f;
        	wall[2] = 200;
        	wall[3] = 563.5f;
        	retVal.add(wall);
        	
        	wall = new float[4];
        	wall[0] = 1000;
        	wall[1] = 163.5f;
        	wall[2] = 1100;
        	wall[3] = 563.5f;
        	retVal.add(wall);
        	
        	wall = new float[4];
        	wall[0] = 150;
        	wall[1] = 163.5f;
        	wall[2] = 950;
        	wall[3] = 213.5f;
        	retVal.add(wall);
        	
        	wall = new float[4];
        	wall[0] = 150;
        	wall[1] = 513.5f;
        	wall[2] = 950;
        	wall[3] = 563.5f;
        	retVal.add(wall);
        	return retVal;
        }
        
        private ArrayList<float[]> generateWalls()
        {
        	ArrayList<float[]> retVal = new ArrayList<float[]>();
        	float[] wall = new float[4];
        	wall[0] = 500;
        	wall[1] = 300;
        	wall[2] = 600;
        	wall[3] = 450;
        	retVal.add(wall);
        	
        	return retVal;
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            // compute the origin of the screen relative to the origin of
            // the bitmap
            mXOrigin = (w - mBitmap.getWidth()) * 0.5f;
            mYOrigin = (h - mBitmap.getHeight()) * 0.5f;
            mHorizontalBound = ((w / mMetersToPixelsX - sBallDiameter) * 0.5f);
            mVerticalBound = ((h / mMetersToPixelsY - sBallDiameter) * 0.5f);
            mScreenWidth = w;
            mScreenHeight = h;
        }

        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
                return;
            /*
             * record the accelerometer data, the event's timestamp as well as
             * the current time. The latter is needed so we can calculate the
             * "present" time during rendering. In this application, we need to
             * take into account how the screen is rotated with respect to the
             * sensors (which always return data in a coordinate space aligned
             * to with the screen in its native orientation).
             */

            switch (mDisplay.getRotation()) {
                case Surface.ROTATION_0:
                    mSensorX = event.values[0];
                    mSensorY = event.values[1];
                    break;
                case Surface.ROTATION_90:
                    mSensorX = -event.values[1];
                    mSensorY = event.values[0];
                    break;
                case Surface.ROTATION_180:
                    mSensorX = -event.values[0];
                    mSensorY = -event.values[1];
                    break;
                case Surface.ROTATION_270:
                    mSensorX = event.values[1];
                    mSensorY = -event.values[0];
                    break;
            }

            mSensorTimeStamp = event.timestamp;
            mCpuTimeStamp = System.nanoTime();
        }

        @Override
        protected void onDraw(Canvas canvas) {

        	 /*
             * draw the background
             */
        	
            //canvas.drawBitmap(mWood, 0, 0, null);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            canvas.drawRect(0, 0, mScreenWidth, mScreenHeight, paint);
            paint.setColor(Color.BLUE);
            int counter;
            float[] wall;
            for(counter = 0; counter < walls.size(); counter++)
            {
            	wall = walls.get(counter);
            	canvas.drawRect(wall[0],wall[1],wall[2],wall[3],paint);
            }
            paint.setColor(Color.BLACK);
            canvas.drawCircle(circle[0], circle[1], 20f, paint);

            /*
             * compute the new position of our object, based on accelerometer
             * data and present time.
             */

            final ParticleSystem particleSystem = mParticleSystem;
            final long now = mSensorTimeStamp + (System.nanoTime() - mCpuTimeStamp);
            final float sx = mSensorX;
            final float sy = mSensorY;

            particleSystem.update(sx, sy, now);

            final float xc = mXOrigin;
            final float yc = mYOrigin;
            final float xs = mMetersToPixelsX;
            final float ys = mMetersToPixelsY;
            final Bitmap bitmap = mBitmap;
            final int count = particleSystem.getParticleCount();
            for (int i = 0; i < count; i++) {
                /*
                 * We transform the canvas so that the coordinate system matches
                 * the sensors coordinate system with the origin in the center
                 * of the screen and the unit is the meter.
                 */

                final float x = xc + particleSystem.getPosX(i) * xs;
                final float y = yc - particleSystem.getPosY(i) * ys;
                canvas.drawBitmap(bitmap, x, y, null);
            }

            // and make sure to redraw asap
            invalidate();
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
        
        protected void onClickListener(View v)
        {
        	pauseTheGame(v);
        }
    }
}
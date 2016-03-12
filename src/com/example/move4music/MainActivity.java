package com.example.move4music;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

public class MainActivity extends Activity {

    DrawingView dv ; 
    Map<Integer, Integer> mapa = new HashMap<Integer, Integer>();
	Map<Integer, Integer> pause = new HashMap<Integer, Integer>();
	SoundPool sp;
	private VelocityTracker mVelocityTracker = null;
	private AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dv = new DrawingView(this);
        setContentView(dv);
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		sp = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
		int som1 = sp.load(this, R.raw.piano, 1);
		int som2 = sp.load(this, R.raw.viva_la_vida, 1);
		mapa.put(0, som1);
		mapa.put(1, som2);
    }

    public class DrawingView extends View {


		private Paint paint = new Paint();
		private Paint paint2 = new Paint();
		private Paint paint3 = new Paint();
		private Paint paint4 = new Paint(); 
		public DrawingView(Context context) {
			super(context);
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setDither(true);
			paint.setColor(Color.GREEN);
			paint.setStyle(Paint.Style.FILL);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStrokeWidth(5);
			paint2 = new Paint();
			paint2.setAntiAlias(true);
			paint2.setDither(true);
			paint2.setColor(Color.BLUE);
			paint2.setStyle(Paint.Style.FILL_AND_STROKE);
			paint2.setStrokeJoin(Paint.Join.ROUND);
			paint2.setStrokeCap(Paint.Cap.ROUND);
			paint2.setStrokeWidth(1);
			paint3 = new Paint();
			paint3.setAntiAlias(true);
			paint3.setDither(true);
			paint3.setColor(Color.RED);
			paint3.setStyle(Paint.Style.STROKE);
			paint3.setStrokeJoin(Paint.Join.ROUND);
			paint3.setStrokeCap(Paint.Cap.ROUND);
			paint3.setStrokeWidth(4);
			paint4 = new Paint();
			paint4.setAntiAlias(true);
			paint4.setDither(true);
			paint4.setColor(Color.BLACK);
			paint4.setStyle(Paint.Style.STROKE);
			paint4.setStrokeJoin(Paint.Join.ROUND);
			paint4.setStrokeCap(Paint.Cap.ROUND);
			paint4.setStrokeWidth(12);
		}

    	@Override
    	protected void onDraw(Canvas canvas) {
    	    for (int size = paths.size(), i = 0; i < size; i++) {
    	        Path path = paths.get(i);
    	        if (path != null) {
    	        	if ((i+1)%4==0) {
    	        		canvas.drawPath(path, paint);
    	        	} else if ((i+1)%4==1) {
    	        		canvas.drawPath(path, paint2);
    	        	} else if ((i+1)%4==2) {
    	        		canvas.drawPath(path, paint3);
    	        	} else if ((i+1)%4==3) {
    	        		canvas.drawPath(path, paint4);
    	        	}
    	        }
    	    }
    	}

    	private HashMap<Integer, Float> mX = new HashMap<Integer, Float>();
    	private HashMap<Integer, Float> mY = new HashMap<Integer, Float>();
    	private HashMap<Integer, Path> paths = new HashMap<Integer, Path>();

    	@Override
    	public boolean onTouchEvent(MotionEvent event) {
    	    int maskedAction = event.getActionMasked();
    	    int index = event.getActionIndex();
            int action = event.getActionMasked();
    		int pointerId = event.getPointerId(index);
    		int pointerIndex = event.findPointerIndex(pointerId);
    	    Log.d("", "onTouchEvent");
    	    Path p;

    	    switch (maskedAction) {
    	    
    	        case MotionEvent.ACTION_DOWN:
    	        	if (mVelocityTracker == null) {
    					// Retrieve a new VelocityTracker object to watch the velocity
    					// of a motion.
    					mVelocityTracker = VelocityTracker.obtain();
    				} else {
    					// Reset the velocity tracker back to its initial state.
    					mVelocityTracker.clear();
    				}
    				// Add a user's movement to the tracker.
    	        	p = new Path();
	                p.moveTo(event.getX(pointerIndex), event.getY(pointerIndex));
    	        	paths.put(pointerId, p);
	                mX.put(pointerId, event.getX(pointerIndex));
	                mY.put(pointerId, event.getY(pointerIndex));
    				mVelocityTracker.addMovement(event);
    				playSound(pointerId);
    				break;
    	        case MotionEvent.ACTION_POINTER_DOWN: {
    	        	p = new Path();
	                p.moveTo(event.getX(pointerIndex), event.getY(pointerIndex));
    	        	paths.put(pointerId, p);
	                mX.put(pointerId, event.getX(pointerIndex));
	                mY.put(pointerId, event.getY(pointerIndex));
    	        	playSound(pointerId);
    	            break;
    	        }
    	        case MotionEvent.ACTION_MOVE: {
    	        	mVelocityTracker.addMovement(event);
    	            mVelocityTracker.computeCurrentVelocity(10000);
    	            for (int key: pause.keySet()) {
    	                p = paths.get(key);
    	                if (p != null) {
    	                	
    	                    float x = event.getX(event.findPointerIndex(key));
    	                    float y = event.getY(event.findPointerIndex(key));
    	                    p.quadTo(mX.get(key), mY.get(key), (x + mX.get(key)) / 2,
    	                            (y + mY.get(key)) / 2);
    	                    mX.put(key, event.getX(event.findPointerIndex(key)));
    	                    mY.put(key, event.getY(event.findPointerIndex(key)));
    	                }
    	                float xPos = VelocityTrackerCompat.getXVelocity(mVelocityTracker, 
    	            			key);
    	            	float yPos = VelocityTrackerCompat.getYVelocity(mVelocityTracker, 
    	            			key);
    	            	mudaVelocidade(key, yPos);
    	            	mudaVolume(key, xPos);
    	            }
    	            invalidate();
    	            break;
    	        }
    	        case MotionEvent.ACTION_UP:
    	        	p = paths.get(pointerId);
    	        	pauseSound(pointerId);
    				break;
    	        case MotionEvent.ACTION_POINTER_UP:
    	        	pauseSound(pointerId);
    				break;
    	    }

    	    return true;
    	}
    	
    	private void playSound(int index) {
    		int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    		int idS = sp.play(mapa.get((index+1)%2), streamVolume, streamVolume, 1, -1, 1f);
    		pause.put(index, idS);
    	}
    	
    	private void pauseSound(int index) {
    		sp.stop(pause.get(index));
    		pause.remove(index);
    	}
    	
    	private void mudaVelocidade(int index, float y) {
    		float velocidade = (Math.abs(y)/10000f) + 1f;
    		Log.d("", "mudaVelocidade: " + velocidade);
    		sp.setRate(pause.get(index), velocidade);
    	}
    	
    	private void mudaVolume(int index, float x) {
    		float volume = (Math.abs(x)/20000f) + 0.5f;
    		sp.setVolume(pause.get(index), volume, volume);
    	}
    }
}
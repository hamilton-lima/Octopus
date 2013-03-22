package com.example.octopus;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ViewQueTeQueroVer extends View implements Runnable {

	private static final int MAX_FINGERS = 20;	
	private static final int RADIUS = 20;
	
	private long time = 1;
	private PointF[] fingers;
	private Paint paint;

	public ViewQueTeQueroVer(Context context) {
		super(context);

		fingers = new PointF[MAX_FINGERS];
		for (int i = 0; i < fingers.length; i++) {
			fingers[i] = new PointF(-1, -1);
		}

		paint = new Paint();
		paint.setColor(Color.GREEN);

		setFocusableInTouchMode(true);
		setClickable(true);
		setLongClickable(true);

		Thread thread = new Thread(this);
		thread.start();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int id = -1;

		for (int i = 0; i < fingers.length; i++) {
			if (i < event.getPointerCount()) {
				id = event.getPointerId(i);
				fingers[i].set(event.getX(id), event.getY(id));
			} else {
				fingers[i].set(-1, -1);
			}
		}

		return super.onTouchEvent(event);
	}

	public void draw(Canvas canvas) {

		super.draw(canvas);

		for (int i = 0; i < fingers.length; i++) {
			if( fingers[i].x > 0 && fingers[i].y > 0 ){
				canvas.drawCircle(fingers[i].x, fingers[i].y, RADIUS, paint);
			}
		}
		
	}

	public void run()  {
		while (true) {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				Log.e(Const.TAG, "interrupcao do run()");
			}
			
			update();
			postInvalidate();
		}

	}

	private void update() {

	}

}

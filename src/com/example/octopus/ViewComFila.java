package com.example.octopus;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

public class ViewComFila extends View implements Runnable {

	private static final int RADIUS = 50;

	private long time = 1;
	private Paint paint;
	private Queue<MotionEvent> fila;
	private SparseArray<PointF> dedos = new SparseArray<PointF>();
	private boolean alive;

	private Thread thread;

	public ViewComFila(Context context) {
		super(context);
		fila = new LinkedList<MotionEvent>();
		alive = true;

		paint = new Paint();
		paint.setColor(Color.GREEN);

		setFocusableInTouchMode(true);
		setClickable(true);
		setLongClickable(true);

		thread = new Thread(this);
		thread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		fila.add(event);
		return super.onTouchEvent(event);
	}

	public void draw(Canvas canvas) {
		super.draw(canvas);

		for (int i = 0; i < dedos.size(); i++) {
			PointF point = (PointF) dedos.valueAt(i);
			canvas.drawCircle(point.x, point.y, RADIUS, paint);
		}
	}

	public void run() {
		while (alive) {
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
		processEventQueue();
	}

	private void processEventQueue() {

		MotionEvent event = (MotionEvent) fila.poll();
		if (event != null) {

			int action = MotionEventCompat.getActionMasked(event);

			// primeiro toque na tela
			if (action == MotionEvent.ACTION_DOWN) {
				int id = event.getPointerId(event.getActionIndex());
				PointF point = new PointF(event.getX(id), event.getY(id));
				dedos.put(id, point);
			}

			// segundo toque na tela
			if (action == MotionEvent.ACTION_POINTER_DOWN) {
				int id = event.getPointerId(event.getActionIndex());
				PointF point = new PointF(event.getX(id), event.getY(id));
				dedos.put(id, point);
			}

			// move
			if (action == MotionEvent.ACTION_MOVE) {

				for (int i = 0; i < event.getPointerCount(); i++) {
					int id = event.getPointerId(i);
					PointF point = (PointF) dedos.get(id);

					if (point != null) {
						point.set(event.getX(id), event.getY(id));
					}
				}
			}

			// remove primeiro toque
			if (action == MotionEvent.ACTION_UP) {
				int id = event.getPointerId(0);
				dedos.remove(id);
			}

			// remove segundo toque na tela
			if (action == MotionEvent.ACTION_POINTER_UP) {
				int id = event.getPointerId(event.getActionIndex());
				dedos.remove(id);
			}

		}

	}

	public void hastaLaVista() {
		alive = false;
	}

}

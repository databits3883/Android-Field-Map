package com.deadman.strat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends Activity {

    MyView mv;
    private Paint       mPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      // Go Full screen
      View decorView = getWindow().getDecorView();
      int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
      decorView.setSystemUiVisibility(uiOptions);

      mv= new MyView(this);
      mv.setDrawingCacheEnabled(true);
      mv.setBackgroundResource(R.drawable.field);
      setContentView(mv);
      mPaint = new Paint();
      mPaint.setAntiAlias(true);
      mPaint.setDither(true);
      mPaint.setColor(getResources().getColor(R.color.green_900,null));
      mPaint.setStyle(Paint.Style.STROKE);
      mPaint.setStrokeJoin(Paint.Join.ROUND);
      mPaint.setStrokeCap(Paint.Cap.ROUND);
      mPaint.setStrokeWidth(20);
    }

    @Override
    public void onResume() {
      super.onResume();
      // Go Full screen
      View decorView = getWindow().getDecorView();
      int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
      decorView.setSystemUiVisibility(uiOptions);
    }
    public class MyView extends View {
      private Bitmap  mBitmap;
      private Canvas  mCanvas;
      private Path    mPath;
      private Paint   mBitmapPaint;
      Context context;

      public MyView(Context c) {
        super(c);
        context=c;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

      }

      @Override
      protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

      }

      @Override
      protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
      }

      private float mX, mY;
      private static final float TOUCH_TOLERANCE = 4;

      private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;

      }
      private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
          mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
          mX = x;
          mY = y;
        }
      }

      private void touch_up() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        mPath.reset();
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
      }

      boolean touchactive = false;

      @SuppressLint("ClickableViewAccessibility")
      @Override
      public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            if (x < 0.04 * mCanvas.getWidth()) {
              mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            } else if (x > mCanvas.getWidth() * 0.96) {
              if (mPaint.getColor() == getResources().getColor(R.color.green_900,null)){
                mPaint.setColor(getResources().getColor(R.color.grey_900,null));
              } else {
                mPaint.setColor(getResources().getColor(R.color.green_900,null));
              }
            } else {
              touch_start(x, y);
              touchactive = true;
            }
            invalidate();
            break;
          case MotionEvent.ACTION_MOVE:
            if (touchactive) {
              touch_move(x, y);
              invalidate();
            }
            break;
          case MotionEvent.ACTION_UP:
            if (touchactive) {
              touch_up();
              invalidate();
            }
            touchactive = false;
            break;
        }
        return true;
      }
    }
  }
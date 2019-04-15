package com.deadman.strat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyDrawView extends View {
  public static Paint   mPaint;
  private Bitmap  mBitmap;
  public static Canvas  mCanvas;
  private Path    mPath;
  private Paint   mBitmapPaint;

  public MyDrawView(Context c) {
    super(c);
    init();
  }

  private void init(){
    mPath = new Path();
    mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setDither(true);
    mPaint.setColor(getResources().getColor(R.color.green,null));
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setStrokeJoin(Paint.Join.ROUND);
    mPaint.setStrokeCap(Paint.Cap.ROUND);
    mPaint.setStrokeWidth(20);

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

  @SuppressLint("ClickableViewAccessibility")
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    float x = event.getX();
    float y = event.getY();
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        touch_start(x, y);
        invalidate();
        break;
      case MotionEvent.ACTION_MOVE:
        touch_move(x, y);
        invalidate();
        break;
      case MotionEvent.ACTION_UP:
        touch_up();
        invalidate();
        break;
    }
    return true;
  }

  public MyDrawView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public MyDrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }
}
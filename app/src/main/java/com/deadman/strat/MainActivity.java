package com.deadman.strat;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Go Full screen
    View decorView = getWindow().getDecorView();
    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    decorView.setSystemUiVisibility(uiOptions);

    setContentView(R.layout.main);

    View mv = findViewById(R.id.canvas_view);
    mv.setDrawingCacheEnabled(true);
    mv.setBackgroundResource(R.drawable.field);

    Button red_button = findViewById(R.id.red_button);
    red_button.setOnClickListener(v -> red());

    Button green_button = findViewById(R.id.green_button);
    green_button.setOnClickListener(v -> green());

    Button blue_button = findViewById(R.id.blue_button);
    blue_button.setOnClickListener(v -> blue());

    Button clear_button = findViewById(R.id.clear_button);
    clear_button.setOnClickListener(v -> clear());
  }

  @Override
  public void onResume() {
    super.onResume();
    // Go Full screen
    View decorView = getWindow().getDecorView();
    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    decorView.setSystemUiVisibility(uiOptions);
  }

  private void red(){
    MyDrawView.mPaint.setColor(getResources().getColor(R.color.red,null));
  }

  private void green(){
    MyDrawView.mPaint.setColor(getResources().getColor(R.color.green,null));
  }

  private void blue(){
    MyDrawView.mPaint.setColor(getResources().getColor(R.color.blue,null));
  }

  private void clear(){
    View mv = findViewById(R.id.canvas_view);
    MyDrawView.mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    mv.invalidate();
  }
}
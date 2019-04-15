package com.deadman.strat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends Activity {
  public static Drawable background;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Go Full screen
    View decorView = getWindow().getDecorView();
    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    decorView.setSystemUiVisibility(uiOptions);

    setContentView(R.layout.main);

    Button red_button = findViewById(R.id.red_button);
    red_button.setOnClickListener(v -> red());

    Button green_button = findViewById(R.id.green_button);
    green_button.setOnClickListener(v -> green());

    Button blue_button = findViewById(R.id.blue_button);
    blue_button.setOnClickListener(v -> blue());

    Button clear_button = findViewById(R.id.clear_button);
    clear_button.setOnClickListener(v -> clear());

    Button settings_button = findViewById(R.id.settings_button);
    settings_button.setOnClickListener(v -> settings());

  }

  @Override
  public void onResume() {
    super.onResume();
    // Go Full screen
    View decorView = getWindow().getDecorView();
    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    decorView.setSystemUiVisibility(uiOptions);

    View mv = findViewById(R.id.canvas_view);
    mv.setDrawingCacheEnabled(true);

    File map_background = new File(
        Environment.getExternalStorageDirectory() + File.separator + "FRC" + File.separator + "map_background.txt");

    if (background == null & !map_background.exists()) {
      Drawable field = getDrawable(R.drawable.field);
      mv.setBackground(field);
    } else if (map_background.exists()) {
      int length = (int) map_background.length();

      byte[] bytes = new byte[length];

      try {
        FileInputStream in = new FileInputStream(map_background);
        in.read(bytes);
        in.close();
      } catch (
          FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
      Bitmap bmp = BitmapFactory.decodeFile(new String(bytes));
      mv.setBackground(new BitmapDrawable(getResources(), bmp));
    } else {
      mv.setBackground(background);
    }

  }

  private void settings() {
    Intent intent = new Intent(this, Settings.class);
    startActivity(intent);
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
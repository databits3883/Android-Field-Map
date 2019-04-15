package com.deadman.strat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;

public class Settings extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Go Full screen
    View decorView = getWindow().getDecorView();
    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    decorView.setSystemUiVisibility(uiOptions);

    setContentView(R.layout.settings);

    Button btn = findViewById(R.id.back_button);
    btn.setOnClickListener(v -> back());

    Button select = findViewById(R.id.select_button);
    select.setOnClickListener(v -> selector());

  }

  private void back(){
    this.onBackPressed();
  }

  private void selector(){
    ImagePicker.create(this)
        .returnMode(ReturnMode.ALL)
        .imageDirectory("FRC")
        .folderMode(true)
        .toolbarFolderTitle("Find your image")
        .toolbarImageTitle("Tap to select")
        .toolbarArrowColor(Color.BLACK)
        .includeVideo(false)
        .single()
        .showCamera(false)
        .start();
  }

  @Override
  protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
    if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
      Image image = ImagePicker.getFirstImageOrNull(data);
      Bitmap bmp = BitmapFactory.decodeFile(image.getPath());
      MainActivity.background = new BitmapDrawable(getResources(), bmp);
      back();
    }
    super.onActivityResult(requestCode, resultCode, data);
  }
}

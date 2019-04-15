package com.deadman.strat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Settings extends Activity {

  File FRC = new File(Environment.getExternalStorageDirectory() + File.separator + "FRC");
  File map_background = new File(
      Environment.getExternalStorageDirectory() + File.separator + "FRC" + File.separator + "map_background.txt");

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

    Button cache = findViewById(R.id.remove_cache_button);
    cache.setOnClickListener(v -> remove_cache());

    if (!FRC.exists()) {
      FRC.mkdirs();
    }
    rescan(FRC.getAbsolutePath());

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

      String imgpath = image.getPath();
      try {
        FileOutputStream stream = new FileOutputStream(map_background);
        stream.write(imgpath.getBytes());
        stream.close();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
      rescan(map_background.getAbsolutePath());

      back();
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  private void remove_cache(){
    map_background.delete();
    rescan(FRC.getAbsolutePath());
    back();
  }

  // Function to scan the edited file so it shows up right away in MTP/OTG
  private void rescan(String file) {
    MediaScannerConnection.scanFile(this,
        new String[]{file}, null,
        (path, uri) -> {
          Log.i("ExternalStorage", "Scanned " + path + ":");
          Log.i("ExternalStorage", "-> uri=" + uri);
        });
  }

}

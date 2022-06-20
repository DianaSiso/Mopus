/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mopus.qrCode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mopus.BuildConfig;
import com.example.mopus.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kotlin.jvm.internal.DefaultConstructorMarker;


/** Demo app chooser which allows you pick from all available testing Activities. */
public final class ChooserActivity extends AppCompatActivity
    implements ActivityCompat.OnRequestPermissionsResultCallback {
  private static final String TAG = "ChooserActivity";
  private static final String[] REQUIRED_RUNTIME_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
  @NotNull
  public static final ChooserActivity.Companion Companion = new ChooserActivity.Companion(null);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    if (BuildConfig.DEBUG) {
      StrictMode.setThreadPolicy(
          new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
      StrictMode.setVmPolicy(
          new StrictMode.VmPolicy.Builder()
              .detectLeakedSqlLiteObjects()
              .detectLeakedClosableObjects()
              .penaltyLog()
              .build());
    }
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate");

    setContentView(R.layout.activity_chooser);

    Button scanButton = findViewById(R.id.scan_button);

    scanButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(view.getContext(), CameraXLivePreviewActivity.class));
      }
    }

    );

    if (!allRuntimePermissionsGranted()) {
      getRuntimePermissions();
    }
  }

  private final boolean allRuntimePermissionsGranted() {
    String[] var3 = REQUIRED_RUNTIME_PERMISSIONS;

    for(int i = 0; i < var3.length; ++i) {
      String permission = var3[i];
      if (permission != null) {
        if (!this.isPermissionGranted(this, permission)) {
          return false;
        }
      }
    }

    return true;
  }

  private final void getRuntimePermissions() {
    ArrayList<String> permissionsToRequest = new ArrayList();

    for(int i = 0; i < REQUIRED_RUNTIME_PERMISSIONS.length; ++i) {
      String permission = REQUIRED_RUNTIME_PERMISSIONS[i];
      if (permission != null) {
        if (!this.isPermissionGranted(this, permission)) {
          permissionsToRequest.add(permission);
        }
      }
    }


    if (!permissionsToRequest.isEmpty()) {
      //Object[] var10001 = permissionsToRequest.toArray(new String[0]);
      if (this == null) {
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
      }

      ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), 1);
    }

  }

  private final boolean isPermissionGranted(Context context, String permission) {
    if (ContextCompat.checkSelfPermission(context, permission) == 0) {
      Log.i("EntryChoiceActivity", "Permission granted: " + permission);
      return true;
    } else {
      Log.i("EntryChoiceActivity", "Permission NOT granted: " + permission);
      return false;
    }
  }

  public static final class Companion {
    private Companion() {
    }

    // $FF: synthetic method
    public Companion(DefaultConstructorMarker $constructor_marker) {
      this();
    }
  }
}

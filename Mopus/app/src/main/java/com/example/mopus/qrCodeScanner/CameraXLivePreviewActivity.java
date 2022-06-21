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

package com.example.mopus.qrCodeScanner;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory;

import com.example.mopus.R;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.example.mopus.qrCodeScanner.preference.PreferenceUtils;
import com.example.mopus.qrCodeScanner.preference.SettingsActivity;

import java.util.List;

/** Live preview demo app for ML Kit APIs using CameraX. */
@KeepName
@RequiresApi(VERSION_CODES.LOLLIPOP)
public final class CameraXLivePreviewActivity extends AppCompatActivity
    implements OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {
  private static final String TAG = "CameraXLivePreview";

  private static final String BARCODE_SCANNING = "Barcode Scanning";

  private static final String STATE_SELECTED_MODEL = "selected_model";

  private PreviewView previewView;
  private GraphicOverlay graphicOverlay;

  @Nullable private ProcessCameraProvider cameraProvider;
  @Nullable private Preview previewUseCase;
  @Nullable private ImageAnalysis analysisUseCase;
  @Nullable private VisionImageProcessor imageProcessor;
  private boolean needUpdateGraphicOverlayImageSourceInfo;

  private String selectedModel = BARCODE_SCANNING;
  private int lensFacing = CameraSelector.LENS_FACING_BACK;
  private CameraSelector cameraSelector;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate");

    if (savedInstanceState != null) {
      selectedModel = savedInstanceState.getString(STATE_SELECTED_MODEL, BARCODE_SCANNING);
    }
    cameraSelector = new CameraSelector.Builder().requireLensFacing(lensFacing).build();

    setContentView(R.layout.activity_vision_camerax_live_preview);
    previewView = findViewById(R.id.preview_view);
    if (previewView == null) {
      Log.d(TAG, "previewView is null");
    }
    graphicOverlay = findViewById(R.id.graphic_overlay);
    if (graphicOverlay == null) {
      Log.d(TAG, "graphicOverlay is null");
    }

    ToggleButton facingSwitch = findViewById(R.id.facing_switch);
    facingSwitch.setOnCheckedChangeListener(this);

    try {
      new ViewModelProvider(this, (ViewModelProvider.Factory) AndroidViewModelFactory.getInstance(getApplication()))
              .get(CameraXViewModel.class)
              .getProcessCameraProvider()
              .observe(
                      this,
                      provider -> {
                        cameraProvider = provider;
                        bindAllCameraUseCases();
                      });
    } catch(Exception e) {
      Log.d("CAMERA", e.toString());
    }

    ImageView settingsButton = findViewById(R.id.settings_button);
    settingsButton.setOnClickListener(
        v -> {
          Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
          intent.putExtra(
              SettingsActivity.EXTRA_LAUNCH_SOURCE,
              SettingsActivity.LaunchSource.CAMERAX_LIVE_PREVIEW);
          startActivity(intent);
        });
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle bundle) {
    super.onSaveInstanceState(bundle);
    bundle.putString(STATE_SELECTED_MODEL, selectedModel);
  }

  @Override
  public synchronized void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    // An item was selected. You can retrieve the selected item using
    // parent.getItemAtPosition(pos)
    selectedModel = parent.getItemAtPosition(pos).toString();
    Log.d(TAG, "Selected model: " + selectedModel);
    bindAnalysisUseCase();
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    // Do nothing.
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    if (cameraProvider == null) {
      return;
    }
    int newLensFacing =
        lensFacing == CameraSelector.LENS_FACING_FRONT
            ? CameraSelector.LENS_FACING_BACK
            : CameraSelector.LENS_FACING_FRONT;
    CameraSelector newCameraSelector =
        new CameraSelector.Builder().requireLensFacing(newLensFacing).build();
    try {
      if (cameraProvider.hasCamera(newCameraSelector)) {
        Log.d(TAG, "Set facing to " + newLensFacing);
        lensFacing = newLensFacing;
        cameraSelector = newCameraSelector;
        bindAllCameraUseCases();
        return;
      }
    } catch (CameraInfoUnavailableException e) {
      // Falls through
    }
    Toast.makeText(
            getApplicationContext(),
            "This device does not have lens with facing: " + newLensFacing,
            Toast.LENGTH_SHORT)
        .show();
  }

  @Override
  public void onResume() {
    super.onResume();
    bindAllCameraUseCases();
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (imageProcessor != null) {
      imageProcessor.stop();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (imageProcessor != null) {
      imageProcessor.stop();
    }
  }

  private void bindAllCameraUseCases() {
    if (cameraProvider != null) {
      // As required by CameraX API, unbinds all use cases before trying to re-bind any of them.
      cameraProvider.unbindAll();
      bindPreviewUseCase();
      bindAnalysisUseCase();
    }
  }

  private void bindPreviewUseCase() {
    if (!PreferenceUtils.isCameraLiveViewportEnabled(this)) {
      return;
    }
    if (cameraProvider == null) {
      return;
    }
    if (previewUseCase != null) {
      cameraProvider.unbind(previewUseCase);
    }

    Preview.Builder builder = new Preview.Builder();
    Size targetResolution = PreferenceUtils.getCameraXTargetResolution(this, lensFacing);
    if (targetResolution != null) {
      builder.setTargetResolution(targetResolution);
    }
    previewUseCase = builder.build();
    previewUseCase.setSurfaceProvider(previewView.getSurfaceProvider());
    cameraProvider.bindToLifecycle(/* lifecycleOwner= */ this, cameraSelector, previewUseCase);
  }

  private void bindAnalysisUseCase() {
    if (cameraProvider == null) {
      return;
    }
    if (analysisUseCase != null) {
      cameraProvider.unbind(analysisUseCase);
    }
    if (imageProcessor != null) {
      imageProcessor.stop();
    }

    try {
      switch (selectedModel) {
        case BARCODE_SCANNING:
          Log.i(TAG, "Using Barcode Detector Processor");
          imageProcessor = new BarcodeScannerProcessor(this);
          break;
        default:
          throw new IllegalStateException("Invalid model name");
      }
    } catch (Exception e) {
      Log.e(TAG, "Can not create image processor: " + selectedModel, e);
      Toast.makeText(
              getApplicationContext(),
              "Can not create image processor: " + e.getLocalizedMessage(),
              Toast.LENGTH_LONG)
          .show();
      return;
    }

    ImageAnalysis.Builder builder = new ImageAnalysis.Builder();
    Size targetResolution = PreferenceUtils.getCameraXTargetResolution(this, lensFacing);
    if (targetResolution != null) {
      builder.setTargetResolution(targetResolution);
    }
    analysisUseCase = builder.build();

    needUpdateGraphicOverlayImageSourceInfo = true;
    analysisUseCase.setAnalyzer(
        // imageProcessor.processImageProxy will use another thread to run the detection underneath,
        // thus we can just runs the analyzer itself on main thread.
        ContextCompat.getMainExecutor(this),
        imageProxy -> {
          if (needUpdateGraphicOverlayImageSourceInfo) {
            boolean isImageFlipped = lensFacing == CameraSelector.LENS_FACING_FRONT;
            int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();
            if (rotationDegrees == 0 || rotationDegrees == 180) {
              graphicOverlay.setImageSourceInfo(
                  imageProxy.getWidth(), imageProxy.getHeight(), isImageFlipped);
            } else {
              graphicOverlay.setImageSourceInfo(
                  imageProxy.getHeight(), imageProxy.getWidth(), isImageFlipped);
            }
            needUpdateGraphicOverlayImageSourceInfo = false;
          }
          try {
            imageProcessor.processImageProxy(imageProxy, graphicOverlay);
          } catch (MlKitException e) {
            Log.e(TAG, "Failed to process image. Error: " + e.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT)
                .show();
          }
        });

    cameraProvider.bindToLifecycle(/* lifecycleOwner= */ this, cameraSelector, analysisUseCase);
  }

  /** Barcode Detector Demo. */
  public class BarcodeScannerProcessor extends VisionProcessorBase<List<Barcode>> {

    private static final String TAG = "BarcodeProcessor";

    private final BarcodeScanner barcodeScanner;

    public BarcodeScannerProcessor(Context context) {
      super(context);
      // Note that if you know which format of barcode your app is dealing with, detection will be
      // faster to specify the supported barcode formats one by one, e.g.
      new BarcodeScannerOptions.Builder()
              .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
              .build();
      barcodeScanner = BarcodeScanning.getClient();
    }

    @Override
    public void stop() {
      super.stop();
      barcodeScanner.close();
    }

    @Override
    protected Task<List<Barcode>> detectInImage(InputImage image) {
      return barcodeScanner.process(image);
    }

    @Override
    protected void onSuccess(
            @NonNull List<Barcode> barcodes, @NonNull GraphicOverlay graphicOverlay) {
      if (barcodes.isEmpty()) {
        Log.v(MANUAL_TESTING_LOG, "No barcode has been detected");
      } else {
        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        intent.putExtra("key", barcodes.get(0).getDisplayValue());
      /*for (int i = 0; i < barcodes.size(); ++i) {
        Barcode barcode = barcodes.get(i);
        Log.d(TAG, barcodes.size() + " - " + barcode.getDisplayValue());
        graphicOverlay.add(new BarcodeGraphic(graphicOverlay, barcode));
        logExtrasForTesting(barcode);
      }*/
        startActivity(intent);
      }
    }

    /*private static void logExtrasForTesting(Barcode barcode) {
      if (barcode != null) {
        if (barcode.getBoundingBox() != null) {
          Log.v(
                  MANUAL_TESTING_LOG,
                  String.format(
                          "Detected barcode's bounding box: %s", barcode.getBoundingBox().flattenToString()));
        }
        if (barcode.getCornerPoints() != null) {
          Log.v(
                  MANUAL_TESTING_LOG,
                  String.format(
                          "Expected corner point size is 4, get %d", barcode.getCornerPoints().length));
        }
        for (Point point : barcode.getCornerPoints()) {
          Log.v(
                  MANUAL_TESTING_LOG,
                  String.format("Corner point is located at: x = %d, y = %d", point.x, point.y));
        }
        Log.v(MANUAL_TESTING_LOG, "barcode display value: " + barcode.getDisplayValue());
        Log.v(MANUAL_TESTING_LOG, "barcode raw value: " + barcode.getRawValue());
        Barcode.DriverLicense dl = barcode.getDriverLicense();
        if (dl != null) {
          Log.v(MANUAL_TESTING_LOG, "driver license city: " + dl.getAddressCity());
          Log.v(MANUAL_TESTING_LOG, "driver license state: " + dl.getAddressState());
          Log.v(MANUAL_TESTING_LOG, "driver license street: " + dl.getAddressStreet());
          Log.v(MANUAL_TESTING_LOG, "driver license zip code: " + dl.getAddressZip());
          Log.v(MANUAL_TESTING_LOG, "driver license birthday: " + dl.getBirthDate());
          Log.v(MANUAL_TESTING_LOG, "driver license document type: " + dl.getDocumentType());
          Log.v(MANUAL_TESTING_LOG, "driver license expiry date: " + dl.getExpiryDate());
          Log.v(MANUAL_TESTING_LOG, "driver license first name: " + dl.getFirstName());
          Log.v(MANUAL_TESTING_LOG, "driver license middle name: " + dl.getMiddleName());
          Log.v(MANUAL_TESTING_LOG, "driver license last name: " + dl.getLastName());
          Log.v(MANUAL_TESTING_LOG, "driver license gender: " + dl.getGender());
          Log.v(MANUAL_TESTING_LOG, "driver license issue date: " + dl.getIssueDate());
          Log.v(MANUAL_TESTING_LOG, "driver license issue country: " + dl.getIssuingCountry());
          Log.v(MANUAL_TESTING_LOG, "driver license number: " + dl.getLicenseNumber());
        }
      }
    }*/

    @Override
    protected void onFailure(@NonNull Exception e) {
      Log.e(TAG, "Barcode detection failed " + e);
    }
  }
}

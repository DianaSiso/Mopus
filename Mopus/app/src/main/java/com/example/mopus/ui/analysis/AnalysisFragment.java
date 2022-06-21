package com.example.mopus.ui.analysis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mopus.databinding.FragmentAnalysisBinding;
import com.example.mopus.qrCodeScanner.CameraXLivePreviewActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kotlin.jvm.internal.DefaultConstructorMarker;

public class AnalysisFragment extends Fragment {

    private FragmentAnalysisBinding binding;
    private static final String TAG = "AnalysisFragment";
    private static final String[] REQUIRED_RUNTIME_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    @NotNull
    public static final AnalysisFragment.Companion Companion = new AnalysisFragment.Companion(null);

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        AnalysisViewModel dashboardViewModel =
                new ViewModelProvider(this).get(AnalysisViewModel.class);

        binding = FragmentAnalysisBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAnalysis;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        final TextView textInstructionView = binding.instructions;
        dashboardViewModel.getInstructionsText().observe(getViewLifecycleOwner(), textInstructionView::setText);

        Button scanButton = binding.scan;
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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private final boolean allRuntimePermissionsGranted() {
        String[] var3 = REQUIRED_RUNTIME_PERMISSIONS;

        for(int i = 0; i < var3.length; ++i) {
            String permission = var3[i];
            if (permission != null) {
                if (!this.isPermissionGranted(getContext(), permission)) {
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
                if (!this.isPermissionGranted(getContext(), permission)) {
                    permissionsToRequest.add(permission);
                }
            }
        }


        if (!permissionsToRequest.isEmpty()) {
            //Object[] var10001 = permissionsToRequest.toArray(new String[0]);
            if (this == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
            }

            ActivityCompat.requestPermissions(getActivity(), permissionsToRequest.toArray(new String[0]), 1);
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
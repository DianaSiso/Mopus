package com.example.mopus.ui.stats;

import android.graphics.Color;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.mopus.QrCodeActivity;
import com.example.mopus.R;
import com.example.mopus.databinding.FragmentStatsBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class StatsFragment extends Fragment {

    private FragmentStatsBinding binding;
    ArrayList<BarEntry> barArrayList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StatsViewModel statsViewModel =
                new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory()).get(StatsViewModel.class);

        binding = FragmentStatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Spinner spinner_months= binding.spinnerMonths;
        ArrayAdapter<CharSequence> adapter_months=ArrayAdapter.createFromResource(getContext(), R.array.months, android.R.layout.simple_spinner_item);
        adapter_months.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_months.setAdapter(adapter_months);

        Spinner spinner_hours_months= binding.spinnerHoursMonth;
        ArrayAdapter<CharSequence> adapter_hours_months=ArrayAdapter.createFromResource(getContext(), R.array.months, android.R.layout.simple_spinner_item);
        adapter_hours_months.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_hours_months.setAdapter(adapter_hours_months);

        Spinner spinner_hours_days= binding.spinnerHoursDay;
        ArrayAdapter<CharSequence> adapter_hours_days=ArrayAdapter.createFromResource(getContext(), R.array.days, android.R.layout.simple_spinner_item);
        adapter_hours_days.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_hours_days.setAdapter(adapter_hours_days);

        setHasOptionsMenu(true);

        return root;
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        inflater.inflate(R.menu.appbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("QR CODE", "CLICKED");
        Intent intent = new Intent(getActivity(), QrCodeActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

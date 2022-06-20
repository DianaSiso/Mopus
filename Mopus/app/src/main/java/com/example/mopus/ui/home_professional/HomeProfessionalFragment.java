package com.example.mopus.ui.home_professional;

import android.content.Context;
import android.content.Intent;
import android.graphics.Region;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mopus.MainActivity;
import com.example.mopus.R;
import com.example.mopus.databinding.FragmentHomeBinding;
import com.example.mopus.databinding.FragmentHomeProfessionalBinding;
import com.example.mopus.model.Scan;
import com.example.mopus.qrCode.ResultActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HomeProfessionalFragment extends Fragment {

    private FragmentHomeProfessionalBinding binding;

    private ScansAdapter adapter;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeProfessioanlViewModel homeProfessionalViewModel =
                new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory()).get(HomeProfessioanlViewModel.class);

        binding = FragmentHomeProfessionalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeProfessionalViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        generateScansList();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void generateDataList(List<Scan> scans) {
        recyclerView = binding.scansList;
        adapter = new ScansAdapter(scans);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void generateScansList() {
        HashMap<String, Region> regions = new HashMap<>();
        // TODO: get data from database
        /*GetDataService apiService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        Call<RegionGroup> call = apiService.getRegions();
        call.enqueue(new Callback<RegionGroup>() {
            @Override
            public void onResponse(Call<RegionGroup> call, Response<RegionGroup> response) {
                int statusCode = response.code();
                RegionGroup regionGroup = response.body();
                for(Region region : regionGroup.getRegions()) {
                    regions.put(region.getLocal(), region);
                }
                ArrayList regionsName = new ArrayList(regions.values());
                Collections.sort(regionsName, Region.RegionNameComparator);
                generateDataList(regionsName);
            }

            @Override
            public void onFailure(Call<RegionGroup> call, Throwable t) {
                Log.e("main", "error calling remote api: " + t.getLocalizedMessage());
                //listener.onFailure(t);
                Toast.makeText(getApplicationContext(),"Error calling remote api",Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    class ScansAdapter extends
            RecyclerView.Adapter<ScansAdapter.ScansViewHolder> {

        private List<Scan> scans;

        public ScansAdapter(List<Scan> scans) {
            this.scans = scans;
        }

        @NonNull
        @Override
        public ScansAdapter.ScansViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_scans_item,
                    parent, false);
            return new ScansAdapter.ScansViewHolder(mItemView, this);

        }

        @Override
        public void onBindViewHolder(@NonNull ScansAdapter.ScansViewHolder holder, int position) {
            Scan mCurrent = scans.get(position);
            holder.userView.setText(mCurrent.getUserEmail());
            holder.dateView.setText(mCurrent.getDateTime());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /* if (mTwoPane) {
                        int selectedRegion = mCurrent.getGlobalLocal();
                        WeatherFragment fragment = WeatherFragment.newInstance(selectedRegion);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.weather_detail_container, fragment)
                                .addToBackStack(null)
                                .commit();
                    } else { */
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ResultActivity.class);
                        intent.putExtra("key", mCurrent.getUserEmail());
                        context.startActivity(intent);
                    // }
                }
            });

        }

        @Override
        public int getItemCount() {
            return scans.size();
        }

        class ScansViewHolder extends RecyclerView.ViewHolder {

            final TextView userView;
            final TextView dateView;
            final View mView;
            final ScansAdapter mAdapter;

            public ScansViewHolder(View itemView, ScansAdapter adapter) {
                super(itemView);
                mView = itemView;
                userView = (TextView) itemView.findViewById(R.id.user_scan);
                dateView = (TextView) itemView.findViewById(R.id.user_scan_date);
                this.mAdapter = adapter;
            }
        }

    }

}
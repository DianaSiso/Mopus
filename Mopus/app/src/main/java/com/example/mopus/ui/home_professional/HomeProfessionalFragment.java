package com.example.mopus.ui.home_professional;

import android.content.Context;
import android.content.Intent;
import android.graphics.Region;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mopus.R;
import com.example.mopus.databinding.FragmentHomeProfessionalBinding;
import com.example.mopus.model.Scan;
import com.example.mopus.qrCodeScanner.ResultActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeProfessionalFragment extends Fragment {

    private static final String TAG = "HOME_PROFESSIONAL_FRAGMENT";

    private FragmentHomeProfessionalBinding binding;

    private ScansAdapter adapter;
    private RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private List<Scan> scans;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeProfessioanlViewModel homeProfessionalViewModel =
                new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory()).get(HomeProfessioanlViewModel.class);

        binding = FragmentHomeProfessionalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        final TextView textView = binding.textHome;
        homeProfessionalViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        scans = new ArrayList<>();

        getDatabaseInfo();
        return root;
    }

    private void getDatabaseInfo() {
        String email = mAuth.getCurrentUser().getEmail();
        List<Scan> scans = new ArrayList<>();

        final DocumentReference docRef = db.collection("professional_scans").document(email);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    if(snapshot.getData().size() > 0) {
                        Map<String, Object> result = snapshot.getData();
                        scans.clear();
                            for(String scanKey : result.keySet()) {
                                Map<String, Object> scan = (Map<String, Object>) result.get(scanKey);
                                Scan s = new Scan(scanKey, String.valueOf(scan.get("user_email")), String.valueOf(scan.get("date")));
                                if(!scans.contains(s)) {
                                    scans.add(s);
                                }
                            }
                        }
                    generateDataList(scans);

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
        //return scans;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void generateDataList(List<Scan> scans) {
        recyclerView = getActivity().findViewById(R.id.scans_list);/*binding.scansList;*/
        adapter = new ScansAdapter(scans);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void generateScansList() {
        HashMap<String, Scan> scans = new HashMap<>();
        //scans = homeProfessionalViewModel.getScans();
        // TODO: get data from database

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
            String userEmail = mCurrent.getUserEmail();
            String date = mCurrent.getDateTime();
            holder.userView.setText(userEmail);
            holder.dateView.setText(date);

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
                        JSONObject scanKey = new JSONObject();
                        try {
                            scanKey.put("email", userEmail);
                            scanKey.put("date", date);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                        intent.putExtra("key", scanKey.toString());
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
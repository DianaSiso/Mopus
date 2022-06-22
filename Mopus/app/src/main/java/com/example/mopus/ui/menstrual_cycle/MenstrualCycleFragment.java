package com.example.mopus.ui.menstrual_cycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mopus.databinding.FragmentMenstrualCycleBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class MenstrualCycleFragment extends Fragment {
    private FragmentMenstrualCycleBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MenstrualCycleViewModel menstrualCycleViewModel =
                new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory()).get(MenstrualCycleViewModel.class);

        binding = FragmentMenstrualCycleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        averageCycle();

        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d("MonthCalendar", String.valueOf(month));
        String monthDB = "";
        switch (month) {
            case 1:
                monthDB = "Jan";
                previousCycle("Dec");
                break;
            case 2:
                monthDB = "Feb";
                previousCycle("Jan");
                break;
            case 3:
                monthDB = "Mar";
                previousCycle("Feb");
                break;
            case 4:
                monthDB = "Apr";
                previousCycle("Mar");
                break;
            case 5:
                monthDB = "May";
                previousCycle("Apr");
                break;
            case 6:
                monthDB = "Jun";
                previousCycle("May");
                break;
            case 7:
                monthDB = "July";
                previousCycle("Jun");
                break;
            case 8:
                monthDB = "Aug";
                previousCycle("July");
                break;
            case 9:
                monthDB = "Sept";
                previousCycle("Aug");
                break;
            case 10:
                monthDB = "Oct";
                previousCycle("Sept");
                break;
            case 11:
                monthDB = "Nov";
                previousCycle("Oct");
                break;
            case 12:
                monthDB = "Dec";
                previousCycle("Nov");
                break;
        }

        return root;
    }

    private void previousCycle(String monthPrevious) {
        String email = getActivity().getIntent().getStringExtra("email");
        Log.d("Email MC", email);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = db.collection("menstruation").document(email);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Getting data from DB", "... Complete.");
                        ArrayList<Double> m = (ArrayList<Double>) document.getData().get(monthPrevious);
                        Log.d("MC", String.valueOf(m.size()));
                        TextView pm = binding.valuePreviousMenstruation;
                        pm.setText("   " + String.valueOf(m.size()) + "  days");
                    }
                } else {
                    Log.d(email, "Failed with: ", task.getException());
                }
            }
        });
    }

    private void averageCycle() {
        ArrayList<Integer> duration = new ArrayList<>();
        String email = getActivity().getIntent().getStringExtra("email");
        Log.d("Email MC", email);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = db.collection("menstruation").document(email);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Getting data from DB", "... Complete.");
                        Map<String, Object> user = document.getData();
                        Log.d("AAAA", String.valueOf(user));
                        for (String key : user.keySet()) {
                            ArrayList<Double> m = (ArrayList<Double>) user.get(key);
                            duration.add(m.size());
                            Log.d("AAAA", String.valueOf(m.size()));
                        }
                    }
                } else {
                    Log.d(email, "Failed with: ", task.getException());
                }

                Log.d("AAAA", String.valueOf(duration));
                int total = 0;
                for (int y = 0; y<duration.size(); y++) {
                    total = total + duration.get(y);
                }
                double value = Double.parseDouble(String.valueOf(total)) / Double.parseDouble(String.valueOf(duration.size()));
                DecimalFormat f = new DecimalFormat("##.00");
                TextView pm = binding.valueAverage;
                pm.setText("   " + String.valueOf(f.format(value)) + "  days");
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

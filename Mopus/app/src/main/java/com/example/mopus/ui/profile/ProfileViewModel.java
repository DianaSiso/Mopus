package com.example.mopus.ui.profile;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> mValue;

    private MutableLiveData<String> firstName;
    private MutableLiveData<String> lastName;
    private MutableLiveData<String> birthDate;
    private MutableLiveData<String> phoneNumber;
    private MutableLiveData<String> email;
    private MutableLiveData<String> weight;
    private MutableLiveData<String> height;
    private MutableLiveData<String> imc;
    private MutableLiveData<Boolean> hasMenstrualCycle;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static final String TAG = "Profile";


    public ProfileViewModel() {
        mValue = new MutableLiveData<>();
        mValue.setValue("This is Profile fragment");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        firstName = new MutableLiveData<>();
        lastName = new MutableLiveData<>();
        birthDate = new MutableLiveData<>();
        phoneNumber = new MutableLiveData<>();
        email = new MutableLiveData<>();
        weight = new MutableLiveData<>();
        height = new MutableLiveData<>();
        imc = new MutableLiveData<>();
        hasMenstrualCycle = new MutableLiveData<>();
        email.setValue(mAuth.getCurrentUser().getEmail());

        db.collection("users")
                .whereEqualTo("id", mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map<String, Object> user = document.getData();
                                //Log.d(TAG, String.valueOf(user.get("firstName")));
                                firstName.setValue(String.valueOf(user.get("firstName")));
                                lastName.setValue(String.valueOf(user.get("lastName")));
                                birthDate.setValue(user.get("birthDay") == null ? "Birth Date" : String.valueOf(user.get("birthDay")));
                                phoneNumber.setValue(user.get("phone") == null ? "Phone Number" : String.valueOf(user.get("phone")));
                                weight.setValue(String.valueOf(user.get("weight")));
                                height.setValue(String.valueOf(user.get("height")));
                                imc.setValue(String.valueOf(user.get("imc")));
                                try {
                                    hasMenstrualCycle.setValue(!(Boolean) user.get("hasMenstrualCycle"));
                                } catch(Exception e) {
                                    hasMenstrualCycle.setValue(true);
                                }
                                break;
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }



    public LiveData<String> getText() {
        return mValue;
    }

    public LiveData<String> getFirstName() { return firstName; }

    public LiveData<String> getLastName() { return lastName; }

    public LiveData<String> getBirthDate() { return birthDate; }

    public LiveData<String> getPhoneNumber() { return phoneNumber; }

    public LiveData<String> getEmail() { return email; }

    public LiveData<String> getIMC() { return imc; }

    public MutableLiveData<Boolean> hasMenstrualCycle() { return hasMenstrualCycle; };

}

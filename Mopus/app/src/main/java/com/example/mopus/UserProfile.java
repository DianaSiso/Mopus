package com.example.mopus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

    public void clickToChangePassword(View view) {
        Intent intent = new Intent(this, ChangePassword.class);
        startActivity(intent);
    }

    public void clickToChangeInfo(View view) {
        Log.d("clickID", String.valueOf(view.getId()));
        String id = String.valueOf(view.getId());
        String from = "";
        switch (id) {
            case "2131165334":
                from = "first_name";
                break;
            case "2131165343":
                from = "last_name";
                break;
            case "2131165337":
                from = "phone_number";
                break;
            case "2131165333":
                from = "email";
        }
        Intent intent = new Intent(this, ChangeInfo.class);
        intent.putExtra("From", from);
        startActivity(intent);
    }
}
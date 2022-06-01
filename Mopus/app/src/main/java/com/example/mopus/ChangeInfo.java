package com.example.mopus;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.TextView;

public class ChangeInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        String from = getIntent().getExtras().getString("From");
        Log.d("check", from);
        TextView info = (TextView) findViewById(R.id.change_info_info);

        switch (from) {
            case "first_name":
                info.setHint("First Name");
                break;
            case "last_name":
                info.setHint("Last Name");
                break;
            case "phone_number":
                info.setHint("Phone Number");
                info.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case "email":
                info.setHint("E-mail");
                info.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;

        }
    }



}
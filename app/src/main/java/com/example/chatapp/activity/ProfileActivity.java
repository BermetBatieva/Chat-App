package com.example.chatapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private EditText nameEditText;
    private String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameEditText = findViewById(R.id.nameEditText);
        phoneNumber = getIntent().getStringExtra("number");
    }

    @SuppressLint("SetTextI18n")
    public void onSaveName(View view) {
        String name = nameEditText.getText().toString();
        if (name.trim().isEmpty()) {
            nameEditText.setText("User name");
            return;
        }


        Map<String, Object> map = new HashMap<>();
        map.put("displayName", name);
        map.put("phoneNumber", phoneNumber);


        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .set(map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Not successful", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }


}

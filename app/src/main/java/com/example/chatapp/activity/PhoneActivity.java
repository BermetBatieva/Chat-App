package com.example.chatapp.activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.chatapp.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private Button register;
    private String phoneNumber;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        phoneNumberEditText = findViewById(R.id.inputPhoneEditTxt);
        register = findViewById(R.id.signIn);
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
            }
        };
        register.setOnClickListener(v -> {
            onSendCode();

        });
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(PhoneActivity.this, ProfileActivity.class);
                intent.putExtra("number", phoneNumber);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Authentication failed!" + task.getException().getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onSendCode() {
        phoneNumber = phoneNumberEditText.getText().toString().trim();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,
                60, TimeUnit.SECONDS, this, callbacks);
    }

    public void onCheckCode() {
        String code = phoneNumberEditText.getText().toString().trim();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signIn(credential);
    }
}
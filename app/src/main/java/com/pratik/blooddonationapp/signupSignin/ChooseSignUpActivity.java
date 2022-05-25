package com.pratik.blooddonationapp.signupSignin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.pratik.blooddonationapp.R;

public class ChooseSignUpActivity extends AppCompatActivity {

    Button donorBtn, recipientBtn, alreadyAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sign_up);

        donorBtn = findViewById(R.id.donorBtn);
        recipientBtn = findViewById(R.id.recipientBtn);
        alreadyAcc = findViewById(R.id.alreadyAcc);

        donorBtn.setOnClickListener(v-> startActivity(new Intent(ChooseSignUpActivity.this, DonorSignUpActivity.class)));
        recipientBtn.setOnClickListener(v-> startActivity(new Intent(ChooseSignUpActivity.this, RecipientSignUpActivity.class)));

        alreadyAcc.setOnClickListener(v -> {
            startActivity(new Intent(ChooseSignUpActivity.this, SignInActivity.class));
            finish();
        });
    }
}
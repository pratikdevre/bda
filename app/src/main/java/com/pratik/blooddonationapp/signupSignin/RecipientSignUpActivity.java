package com.pratik.blooddonationapp.signupSignin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.pratik.blooddonationapp.Database.dbQueries;
import com.pratik.blooddonationapp.InterFace.MyCompleteListener;
import com.pratik.blooddonationapp.MainActivity;
import com.pratik.blooddonationapp.R;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipientSignUpActivity extends AppCompatActivity {

    TextInputLayout name, email, pass, cpass, phone;
    String name1, email1, pass1, cpass1, phone1, bloodgrp1, recipientLocation1;
    Spinner bloodgrp, recipientLocation;
    Button register;
    RelativeLayout selectImg;
    CircleImageView recipientImg;
    Uri imgUri;

    Dialog progressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_sign_up);

        init();
        showProgressDialog();
        
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            assert result.getData() != null;
            imgUri = result.getData().getData();
            Glide.with(getApplicationContext()).load(imgUri).into(recipientImg);
        });
        selectImg.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            Intent chooser = Intent.createChooser(intent, "Select Image...");
            activityResultLauncher.launch(chooser);
        });


        register.setOnClickListener(v -> {
            name1 = Objects.requireNonNull(name.getEditText()).getText().toString().trim();
            email1 = Objects.requireNonNull(email.getEditText()).getText().toString().trim();
            pass1 = Objects.requireNonNull(pass.getEditText()).getText().toString().trim();
            cpass1 = Objects.requireNonNull(cpass.getEditText()).getText().toString().trim();
            phone1 = Objects.requireNonNull(phone.getEditText()).getText().toString().trim();
            bloodgrp1 = bloodgrp.getSelectedItem().toString();
            recipientLocation1 = recipientLocation.getSelectedItem().toString();

            if (validateData()) {
                progressDialog.show();
                dbQueries.signUpRecipient(email1, pass1, name1, phone1, bloodgrp1, imgUri, recipientLocation1, new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Registration Successful as Blood Recipient!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RecipientSignUpActivity.this, MainActivity.class));
                        finish();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(RecipientSignUpActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });

    }




    private boolean validateData() {
        if (name1.length() == 0) {
            name.setError("Blank field not allowed");
            return false;
        } else if (name1.charAt(0) >= '0' && name1.charAt(0) <= '9') {
            name.setError("Numbers at starting not allowed.");
            return false;
        } else name.setErrorEnabled(false);

        if (phone1.length() != 10) {
            phone.setError("Please enter 10 digits");
            return false;
        } else phone.setErrorEnabled(false);
        if (email1.length() == 0) {
            email.setError("Blank field not allowed");
            return false;
        } else email.setErrorEnabled(false);

        if (pass1.length() == 0) {
            pass.setError("Blank field not allowed");
            return false;
        } else pass.setErrorEnabled(false);

        if (cpass1.length() == 0) {
            cpass.setError("Blank field not allowed");
            return false;
        } else cpass.setErrorEnabled(false);

        if (!pass1.equals(cpass1)) {
            cpass.setError("Password is not same");
            return false;
        } else cpass.setErrorEnabled(false);
        if (bloodgrp1.equals("Select your blood group")) {
            Toast.makeText(this, "Select your blood group", Toast.LENGTH_LONG).show();
            return false;
        }
        if (recipientLocation1.equals("Select your city")) {
            Toast.makeText(this, "Select your city", Toast.LENGTH_LONG).show();
            return false;
        }
        if (imgUri == null) {
            Toast.makeText(this, "Set your profile photo", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
    @SuppressLint("SetTextI18n")
    private void showProgressDialog() {
        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.progressdialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView dialogText = progressDialog.findViewById(R.id.dialogText);
        dialogText.setText("Please wait...");
    }
    private void init() {
        name = findViewById(R.id.recipientUserName);
        email = findViewById(R.id.recipientEmail);
        pass = findViewById(R.id.recipientPassword);
        cpass = findViewById(R.id.recipientConfirmPassword);
        phone = findViewById(R.id.recipientPhoneNumber);
        bloodgrp = findViewById(R.id.recipientBloodGroup);
        register = findViewById(R.id.recipientRegisterBtn);
        recipientImg = findViewById(R.id.recipientImg);
        selectImg = findViewById(R.id.rselectImg);
        recipientLocation = findViewById(R.id.recipientLocation);

    }

}
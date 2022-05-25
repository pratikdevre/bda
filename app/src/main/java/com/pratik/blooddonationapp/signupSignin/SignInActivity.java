package com.pratik.blooddonationapp.signupSignin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.pratik.blooddonationapp.Database.dbQueries;
import com.pratik.blooddonationapp.InterFace.MyCompleteListener;
import com.pratik.blooddonationapp.MainActivity;
import com.pratik.blooddonationapp.R;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    Button noaccsignUp, signinBtn;
    TextInputLayout signin_email, signin_pswd;
    String signin_email1, signin_pswd1;
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        noaccsignUp = findViewById(R.id.noaccsignUp);
        signinBtn = findViewById(R.id.signinBtn);
        signin_email = findViewById(R.id.signin_email);
        signin_pswd = findViewById(R.id.signin_password);
        showProgressDialog();

        signinBtn.setOnClickListener(v -> {
            signin_email1 = Objects.requireNonNull(signin_email.getEditText()).getText().toString().trim();
            signin_pswd1 = Objects.requireNonNull(signin_pswd.getEditText()).getText().toString().trim();
            if (validateData()) {
                progressDialog.show();
                dbQueries.signInUser(signin_email1, signin_pswd1, new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(SignInActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        noaccsignUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, ChooseSignUpActivity.class));
            finish();
        });

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

    private boolean validateData() {
        if (signin_email1.length() == 0) {
            signin_email.setError("Blank field not allowed");
            return false;
        } else signin_email.setErrorEnabled(false);
        if (signin_pswd1.length() == 0) {
            signin_pswd.setError("Blank field not allowed");
            return false;
        } else signin_pswd.setErrorEnabled(false);

        return true;
    }
}
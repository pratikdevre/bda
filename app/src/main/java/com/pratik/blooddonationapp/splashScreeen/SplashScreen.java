package com.pratik.blooddonationapp.splashScreeen;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.pratik.blooddonationapp.Database.dbQueries;
import com.pratik.blooddonationapp.MainActivity;
import com.pratik.blooddonationapp.R;
import com.pratik.blooddonationapp.signupSignin.SignInActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(() -> {
            if (dbQueries.checkUserLoggedIn()){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            else{
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
            finish();

        }, 2000);
    }

}
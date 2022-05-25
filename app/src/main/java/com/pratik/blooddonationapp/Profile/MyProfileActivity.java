package com.pratik.blooddonationapp.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pratik.blooddonationapp.Database.dbQueries;
import com.pratik.blooddonationapp.R;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {
    CircleImageView imageView;
    TextView name, phone, email, type, location;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        init();
        setData();
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        name.setText("Name: " + dbQueries.profileModel.getName());
        email.setText("Email: " + dbQueries.profileModel.getEmail());
        phone.setText("Phone No.: " + dbQueries.profileModel.getPhone());
        type.setText("Type: " + dbQueries.profileModel.getType());
        location.setText("Location: " + dbQueries.profileModel.getLocation());
        Glide.with(getApplicationContext()).load(dbQueries.profileModel.getProfilePic()).into(imageView);

    }

    private void init() {
        name = findViewById(R.id.myName);
        email = findViewById(R.id.myEmail);
        phone = findViewById(R.id.myPhone);
        imageView = findViewById(R.id.myPhoto);
        type = findViewById(R.id.myType);
        location = findViewById(R.id.myLocation);

        toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}
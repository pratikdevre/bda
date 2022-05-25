package com.pratik.blooddonationapp.Profile;

import static com.pratik.blooddonationapp.Database.dbQueries.profileModel;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.pratik.blooddonationapp.Database.dbQueries;
import com.pratik.blooddonationapp.InterFace.MyCompleteListener;
import com.pratik.blooddonationapp.Models.DonorsModel;
import com.pratik.blooddonationapp.R;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FullInformationActivity extends AppCompatActivity {

    TextView name, email, phone, bloodgrp, type, location;
    ImageView sendEmail;
    CircleImageView photo;
    DonorsModel donorsModel;
    Toolbar toolbar;
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_information);

        donorsModel = dbQueries._selected_user;
        init();
        showProgressDialog();
        setData();

    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        name.setText("Name: " + donorsModel.getName());
        type.setText(donorsModel.getType().toUpperCase());
        phone.setText("Phone No.: " + donorsModel.getPhone());
        bloodgrp.setText("Blood Group: " + donorsModel.getBloodGroup());
        location.setText("Location: " + donorsModel.getLocation());
        email.setText("Email id: " + donorsModel.getEmail());



        sendEmail.setOnClickListener(v -> new AlertDialog.Builder(FullInformationActivity.this)
                .setMessage("Click 'SEND' to send message")
                .setTitle("Send Email")
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("SEND", (dialogInterface, i) ->
                        {
                            progressDialog.show();
                            String msg;
                            if (profileModel.getType().equals("recipient")) {
                                msg = "Hello " + donorsModel.getName() + ",\n" +
                                        profileModel.getName() + " would like blood donation from you. Here is his/hers details:\n" +
                                        "Name: " + profileModel.getName() + "\n" +
                                        "Email: " + profileModel.getEmail() + "\n" +
                                        "Phone Number: " + profileModel.getPhone() + "\n" +
                                        "Blood Group: " + profileModel.getBloodGroup() + "\n" +
                                        "Kindly Reach out to him/her. May be your blood can save his/her life\n" +
                                        "Thank You!\n" +
                                        "BLOOD DONATION APP - DONATE BLOOD, SAVE LIVES!";
                            }
                            else {
                                msg = "Hello " + donorsModel.getName() + ",\n" +
                                        profileModel.getName() + " would like to donate blood to you. Here is his/hers details:\n" +
                                        "Name: " + profileModel.getName() + "\n" +
                                        "Email: " + profileModel.getEmail() + "\n" +
                                        "Phone Number: " + profileModel.getPhone() + "\n" +
                                        "Blood Group: " + profileModel.getBloodGroup() + "\n" +
                                        "Kindly Reach out to him/her. May be his/her blood can save your life\n" +
                                        "Thank You!\n" +
                                        "BLOOD DONATION APP - DONATE BLOOD, SAVE LIVES!";
                            }

                            dbQueries.sendMessageToUser(donorsModel.getCurrentUserId(), msg, new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    progressDialog.dismiss();
                                    dbQueries.addToSentEmail(donorsModel.getName(), donorsModel.getEmail(), new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            new AlertDialog.Builder(FullInformationActivity.this)
                                                    .setTitle("Success")
                                                    .setMessage("Message sent successfully!")
                                                    .setCancelable(true)
                                                    .setNegativeButton("OK", (dialogInterface1, i1) -> dialogInterface1.dismiss())
                                                    .show();
                                        }
                                        @Override
                                        public void onFailure(Exception e) {
                                            Toast.makeText(FullInformationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(FullInformationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Toast.makeText(this, "Wait I am doing something", Toast.LENGTH_SHORT).show();
                        }
                )
                .setCancelable(true)
                .setIcon(R.drawable.ic_baseline_email_24)
                .show());

        Glide.with(getApplicationContext()).load(Uri.parse(donorsModel.getProfilePic())).into(photo);


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
        name = findViewById(R.id.full_name);
        email = findViewById(R.id.fullEmail);
        phone = findViewById(R.id.fullPhone);
        bloodgrp = findViewById(R.id.full_bloodGrp);
        location = findViewById(R.id.full_location);
        type = findViewById(R.id.full_type);
        sendEmail = findViewById(R.id.full_email);
        photo = findViewById(R.id.full_photo);
        toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}
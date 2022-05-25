package com.pratik.blooddonationapp.Profile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pratik.blooddonationapp.Adapters.NotificationsAdapter;
import com.pratik.blooddonationapp.Database.dbQueries;
import com.pratik.blooddonationapp.R;

import java.util.Objects;

public class NotificationActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rcv_notification;
    Dialog progressDialog;
    NotificationsAdapter adapter;
    TextView nothing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        nothing = findViewById(R.id.nothing2);
        toolbar = findViewById(R.id.toolbar2);
        rcv_notification = findViewById(R.id.rcv_notification);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        showProgressDialog();
        progressDialog.show();


        if (dbQueries._sentEmailsList.size() == 0) {
            nothing.setVisibility(View.VISIBLE);
            rcv_notification.setVisibility(View.GONE);
        } else {
            nothing.setVisibility(View.GONE);
            rcv_notification.setVisibility(View.VISIBLE);


            adapter = new NotificationsAdapter(dbQueries._notificationList);
            rcv_notification.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            rcv_notification.setAdapter(adapter);
            rcv_notification.setHasFixedSize(true);
            rcv_notification.setItemAnimator(null);
        }
        progressDialog.dismiss();


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
}
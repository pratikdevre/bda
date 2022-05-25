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

import com.pratik.blooddonationapp.Adapters.SentEmailsAdapter;
import com.pratik.blooddonationapp.Database.dbQueries;
import com.pratik.blooddonationapp.R;

import java.util.Objects;

public class SentEmailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rcv_sentEmail;
    Dialog progressDialog;
    SentEmailsAdapter adapter;
    TextView nothing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_emails);
        toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        showProgressDialog();
        progressDialog.show();
        rcv_sentEmail = findViewById(R.id.rcv_sentEmail);
        nothing = findViewById(R.id.nothing);

        if (dbQueries._sentEmailsList.size() == 0) {
            nothing.setVisibility(View.VISIBLE);
            rcv_sentEmail.setVisibility(View.GONE);
        } else {
            nothing.setVisibility(View.GONE);
            rcv_sentEmail.setVisibility(View.VISIBLE);
            adapter = new SentEmailsAdapter(dbQueries._sentEmailsList);
            rcv_sentEmail.setAdapter(adapter);
            rcv_sentEmail.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            rcv_sentEmail.setHasFixedSize(true);
            rcv_sentEmail.setItemAnimator(null);
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
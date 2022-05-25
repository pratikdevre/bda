package com.pratik.blooddonationapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.pratik.blooddonationapp.Adapters.MainAdapter;
import com.pratik.blooddonationapp.Database.dbQueries;
import com.pratik.blooddonationapp.InterFace.MyCompleteListener;
import com.pratik.blooddonationapp.Models.DonorsModel;
import com.pratik.blooddonationapp.Profile.AboutActivity;
import com.pratik.blooddonationapp.Profile.MyProfileActivity;
import com.pratik.blooddonationapp.Profile.NotificationActivity;
import com.pratik.blooddonationapp.Profile.SentEmailsActivity;
import com.pratik.blooddonationapp.signupSignin.SignInActivity;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    Dialog progressDialog;
    RecyclerView rcv_main;
    MainAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        showProgressDialog();
        progressDialog.show();
        setToolbar();
        getAllUserData();
        setNavigationView();
    }


    private void setToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home");

//        for navigation view
        navigationView.bringToFront();
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void getAllUserData() {
        dbQueries.getAllUsersInfoFromDB(new MyCompleteListener() {
            @Override
            public void onSuccess() {

                CircleImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.navigation_photo);
                Glide.with(getApplicationContext()).load(dbQueries.profileModel.getProfilePic()).into(imageView);
                TextView navName, navType;
                navName = navigationView.getHeaderView(0).findViewById(R.id.navigationUserName);
                navType = navigationView.getHeaderView(0).findViewById(R.id.navigationUserType);

                navName.setText(dbQueries.profileModel.getName());
                navType.setText(dbQueries.profileModel.getBloodGroup());


                adapter = new MainAdapter(dbQueries._usersListByType, getApplicationContext());
                rcv_main.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                rcv_main.setAdapter(adapter);
                rcv_main.setHasFixedSize(true);
                rcv_main.setItemAnimator(null);
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void setNavigationView() {
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                progressDialog.show();
                drawerLayout.closeDrawer(GravityCompat.START);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
                searchQueries(dbQueries._usersListByType);

            } else if (id == R.id.nav_refresh) {
                progressDialog.show();
                drawerLayout.closeDrawer(GravityCompat.START);
                dbQueries.getAllUsersInfoFromDB(new MyCompleteListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "Page refreshed.", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (id == R.id.nav_searchByLocation) {
                progressDialog.show();
                drawerLayout.closeDrawer(GravityCompat.START);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Search by location...");
                searchQueries(dbQueries._usersListByLocation);

            } else if (id == R.id.nav_searchByBloodGroup) {
                progressDialog.show();
                drawerLayout.closeDrawer(GravityCompat.START);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Search blood for me...");
                searchQueries(dbQueries._usersListByBloodGroup);

            } else if (id == R.id.nav_donorsEmail) {
                sentEmail();
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(MainActivity.this, MyProfileActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (id == R.id.nav_Notification) {
                getNotifications();
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (id == R.id.nav_about) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (id == R.id.nav_logout) {
                drawerLayout.closeDrawer(GravityCompat.START);
                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Do you want to Logout?")
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .setCancelable(true)
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("Yes", (dialog, which) -> {
                            dbQueries.logoutLoggedUser();
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            this.finish();
                            Toast.makeText(getApplicationContext(), "Logged out Successful", Toast.LENGTH_SHORT).show();
                        })
                        .show();
            }
            return true;
        });
    }

    private void getNotifications() {
        progressDialog.show();
        dbQueries.getNotifications(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                progressDialog.hide();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(MainActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });
    }

    private void sentEmail() {
        dbQueries.fetchSentEmails(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                startActivity(new Intent(getApplicationContext(), SentEmailsActivity.class));
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(MainActivity.this, "Something went Wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchQueries(List<DonorsModel> list) {
        adapter = new MainAdapter(list, getApplicationContext());
        rcv_main.setAdapter(adapter);
        rcv_main.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rcv_main.setHasFixedSize(true);
        rcv_main.setItemAnimator(null);
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    private void init() {
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        rcv_main = findViewById(R.id.rcv_main);
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


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Exit")
                    .setCancelable(true)
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage("Do you want to exit?")
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .show();
        }
    }
}

package com.pratik.blooddonationapp.Adapters;

import static com.pratik.blooddonationapp.Database.dbQueries.profileModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pratik.blooddonationapp.Database.dbQueries;
import com.pratik.blooddonationapp.InterFace.MyCompleteListener;
import com.pratik.blooddonationapp.Models.DonorsModel;
import com.pratik.blooddonationapp.Profile.FullInformationActivity;
import com.pratik.blooddonationapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
    public static List<DonorsModel> list;
    @SuppressLint("StaticFieldLeak")
    static Context context;

    public MainAdapter(List<DonorsModel> list, Context context) {
        MainAdapter.list = list;
        MainAdapter.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_d_r_info, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, location, bloodGroup, type;
        ImageView email;
        CircleImageView photo;
        View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.home_name);
            location = itemView.findViewById(R.id.home_location);
            bloodGroup = itemView.findViewById(R.id.home_bloodGrp);
            type = itemView.findViewById(R.id.home_type);
            email = itemView.findViewById(R.id.home_email);
            photo = itemView.findViewById(R.id.home_photo);
            view = itemView;

        }

        private void setData(int pos) {
            String a, b;
            a = list.get(pos).getName();
            b = list.get(pos).getLocation();
            a = (a.length() > 10) ? a.substring(0, 12) + "..." : a;
            b = (b.length() > 10) ? b.substring(0, 12) + "..." : b;
            name.setText(a);
            location.setText(b);
            bloodGroup.setText(list.get(pos).getBloodGroup());
            type.setText(list.get(pos).getType());
            Glide.with(context).load(Uri.parse(list.get(pos).getProfilePic())).into(photo);

            email.setOnClickListener(v ->
                    new AlertDialog.Builder(v.getContext())
                            .setMessage("Click 'SEND' to send email")
                            .setTitle("Send Email")
                            .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                            .setPositiveButton("SEND", (dialogInterface, i) ->
                            {
                                String msg;
                                if (profileModel.getType().equals("recipient")) {
                                    msg = "Hello " + list.get(pos).getName() + ",\\n" +
                                            profileModel.getName() + " would like blood donation from you. Here is his/hers details:\\n" +
                                            "Name: " + profileModel.getName() + "\\n" +
                                            "Email: " + profileModel.getEmail() + "\\n" +
                                            "Phone Number: " + profileModel.getPhone() + "\\n" +
                                            "Blood Group: " + profileModel.getBloodGroup() + "\\n" +
                                            "Kindly Reach out to him/her. May be your blood can save his/her life\\n" +
                                            "Thank You!\\n" +
                                            "BLOOD DONATION APP - DONATE BLOOD, SAVE LIVES!";
                                } else {
                                    msg = "Hello " + list.get(pos).getName() + ",\\n" +
                                            profileModel.getName() + " would like to donate blood to you. Here is his/hers details:\\n" +
                                            "Name: " + profileModel.getName() + "\\n" +
                                            "Email: " + profileModel.getEmail() + "\\n" +
                                            "Phone Number: " + profileModel.getPhone() + "\\n" +
                                            "Blood Group: " + profileModel.getBloodGroup() + "\\n" +
                                            "Kindly Reach out to him/her. May be his/her blood can save your life\\n" +
                                            "Thank You!\\n" +
                                            "BLOOD DONATION APP - DONATE BLOOD, SAVE LIVES!";
                                }

                                dbQueries.sendMessageToUser(list.get(pos).getCurrentUserId(), msg, new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        dbQueries.addToSentEmail(list.get(pos).getName(), list.get(pos).getEmail(), new MyCompleteListener() {
                                            @Override
                                            public void onSuccess() {
                                                new AlertDialog.Builder(v.getContext())
                                                        .setTitle("Success")
                                                        .setMessage("Message sent successfully!")
                                                        .setCancelable(true)
                                                        .setNegativeButton("OK", (dialogInterface1, i1) -> dialogInterface1.dismiss())
                                                        .show();
                                            }

                                            @Override
                                            public void onFailure(Exception e) {
                                                Toast.makeText(v.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(v.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }).setCancelable(true)
                            .setIcon(R.drawable.ic_baseline_email_24)
                            .show()
            );

            view.setOnClickListener(v -> {
                dbQueries._selected_user.setName(list.get(pos).getName());
                dbQueries._selected_user.setEmail(list.get(pos).getEmail());
                dbQueries._selected_user.setPhone(list.get(pos).getPhone());
                dbQueries._selected_user.setBloodGroup(list.get(pos).getBloodGroup());
                dbQueries._selected_user.setLocation(list.get(pos).getLocation());
                dbQueries._selected_user.setType(list.get(pos).getType());
                dbQueries._selected_user.setProfilePic(list.get(pos).getProfilePic());
                dbQueries._selected_user.setCurrentUserId(list.get(pos).getCurrentUserId());

                context.startActivity(new Intent(context, FullInformationActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                );
            });
        }
    }
}

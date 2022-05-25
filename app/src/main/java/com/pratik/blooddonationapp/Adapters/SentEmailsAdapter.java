package com.pratik.blooddonationapp.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pratik.blooddonationapp.Models.SentEmailsModel;
import com.pratik.blooddonationapp.R;

import java.util.List;

public class SentEmailsAdapter extends RecyclerView.Adapter<SentEmailsAdapter.MyViewHolder> {
    List<SentEmailsModel> list;

    public SentEmailsAdapter(List<SentEmailsModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SentEmailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_sent_email_layout, parent, false);
        return new SentEmailsAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SentEmailsAdapter.MyViewHolder holder, int position) {
        holder.name.setText("Name: "+list.get(position).getName());
        holder.email.setText("Email id: "+list.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.sent_name);
            email = itemView.findViewById(R.id.sent_email);
        }
    }
}

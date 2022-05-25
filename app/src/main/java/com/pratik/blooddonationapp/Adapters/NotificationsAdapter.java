package com.pratik.blooddonationapp.Adapters;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pratik.blooddonationapp.Models.NotificationModel;
import com.pratik.blooddonationapp.Models.SentEmailsModel;
import com.pratik.blooddonationapp.R;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder>{
    List<NotificationModel> list;

    public NotificationsAdapter(List<NotificationModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_notification_layout, parent, false);
        return new NotificationsAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.MyViewHolder holder, int position) {
        holder.name.setText("Email: "+list.get(position).getName());
        holder.msg.setText("Message: "+list.get(position).getMsg());
        holder.copy.setOnClickListener(v->{
            ClipboardManager clipboardManager = (ClipboardManager) holder.itemView.getContext().getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Copied Text", list.get(position).getMsg());
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(holder.itemView.getContext(), "Message Copied", Toast.LENGTH_SHORT).show();

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, msg;
        ImageView copy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.notification_name);
            msg = itemView.findViewById(R.id.notification_msg);
            copy = itemView.findViewById(R.id.notification_copy);
        }
    }
}

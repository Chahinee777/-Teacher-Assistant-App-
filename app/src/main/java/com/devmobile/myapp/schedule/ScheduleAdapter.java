package com.devmobile.myapp.schedule;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.devmobile.myapp.AppBase;
import com.devmobile.myapp.R;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private ArrayList<String> subjects;
    private ArrayList<String> times;
    private ArrayList<String> subx;
    private Activity activity;

    public ScheduleAdapter(Activity activity, ArrayList<String> subjects, ArrayList<String> times, ArrayList<String> subx) {
        this.subjects = subjects;
        this.times = times;
        this.subx = subx;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.schedule_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.subjectTextView.setText(subjects.get(position));
        holder.timeTextView.setText(times.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = subx.get(holder.getAdapterPosition());
                String time = times.get(holder.getAdapterPosition());

                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle("Update Schedule");
                alert.setMessage("Do you want to update this schedule?");
                alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(activity, UpdateScheduleActivity.class);
                        intent.putExtra("subject", subject);
                        intent.putExtra("time", time);
                        activity.startActivity(intent);
                    }
                });
                alert.setNegativeButton("Close", null);
                alert.show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String subject = subx.get(holder.getAdapterPosition());
                String time = times.get(holder.getAdapterPosition());

                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle("Delete Schedule?");
                alert.setMessage("Do you want to delete this schedule?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String qu = "DELETE FROM SCHEDULE WHERE subject = '" + subject + "' AND timex = '" + time + "'";
                        if (AppBase.handler.execAction(qu)) {
                            ((Scheduler) activity).loadSchedules();
                            Toast.makeText(activity, "Deleted", Toast.LENGTH_LONG).show();
                        } else {
                            ((Scheduler) activity).loadSchedules();
                            Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("NO", null);
                alert.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTextView;
        TextView timeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTextView = itemView.findViewById(R.id.scheduleSubject);
            timeTextView = itemView.findViewById(R.id.scheduleTime);
        }
    }
}
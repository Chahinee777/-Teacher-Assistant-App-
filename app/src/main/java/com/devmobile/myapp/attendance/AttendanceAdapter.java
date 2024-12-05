package com.devmobile.myapp.attendance;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devmobile.myapp.AppBase;
import com.devmobile.myapp.R;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    private ArrayList<String> nameList;
    private ArrayList<String> registers;
    private ArrayList<Boolean> attendanceList;
    private Activity activity;

    public AttendanceAdapter(Activity activity, ArrayList<String> nameList, ArrayList<String> registers) {
        this.nameList = nameList;
        this.activity = activity;
        this.registers = registers;
        attendanceList = new ArrayList<>();
        for (int i = 0; i < nameList.size(); i++) {
            attendanceList.add(true);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_ele, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(nameList.get(position));
        holder.checkBox.setChecked(attendanceList.get(position));
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendanceList.set(holder.getAdapterPosition(), holder.checkBox.isChecked());
                Log.d("Attendance", nameList.get(holder.getAdapterPosition()) + " is absent " + attendanceList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    public void saveAll() {
        for (int i = 0; i < nameList.size(); i++) {
            int sts = attendanceList.get(i) ? 1 : 0;
            String qu = "INSERT INTO ATTENDANCE VALUES('" + AttendanceActivity.time + "'," +
                    "" + AttendanceActivity.period + "," +
                    "'" + registers.get(i) + "'," +
                    "" + sts + ");";
            AppBase.handler.execAction(qu);
        }
        activity.finish();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.attendanceName);
            checkBox = itemView.findViewById(R.id.attMarker);
        }
    }
}
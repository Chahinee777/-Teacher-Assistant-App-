package com.devmobile.myapp.components;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.devmobile.myapp.R;
import com.devmobile.myapp.AppBase;
import com.devmobile.myapp.attendance.AttendanceActivity;
import com.devmobile.myapp.notes.NoteActivity;
import com.devmobile.myapp.profile.ProfileActivity;
import com.devmobile.myapp.schedule.Scheduler;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    public static Activity activity;
    ArrayList<String> names;

    public GridAdapter(Activity activity, ArrayList<String> names) {
        this.activity = activity;
        this.names = names;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            LayoutInflater vi = LayoutInflater.from(activity);
            v = vi.inflate(R.layout.grid_layout, null);
        }
        TextView textView = v.findViewById(R.id.namePlacer);
        ImageView imageView = v.findViewById(R.id.imageHolder);

        if (names.get(position).equals("ATTENDANCE")) {
            imageView.setImageResource(R.drawable.ic_attendance);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchIntent = new Intent(activity, AttendanceActivity.class);
                    activity.startActivity(launchIntent);
                }
            });
        } else if (names.get(position).equals("SCHEDULER")) {
            imageView.setImageResource(R.drawable.ic_schedule);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchIntent = new Intent(activity, Scheduler.class);
                    activity.startActivity(launchIntent);
                }
            });
        } else if (names.get(position).equals("NOTES")) {
            imageView.setImageResource(R.drawable.ic_notes);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchIntent = new Intent(activity, NoteActivity.class);
                    activity.startActivity(launchIntent);
                }
            });
        } else if (names.get(position).equals("PROFILE")) {
            imageView.setImageResource(R.drawable.ic_profile);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchIntent = new Intent(activity, ProfileActivity.class);
                    activity.startActivity(launchIntent);
                }
            });
        }

        textView.setText(names.get(position));
        return v;
    }
}
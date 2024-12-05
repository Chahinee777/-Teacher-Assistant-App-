package com.devmobile.myapp.schedule;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.devmobile.myapp.R;
import com.devmobile.myapp.database.DatabaseHandler;

import java.util.ArrayList;

public class Scheduler extends AppCompatActivity {

    RecyclerView recyclerView;
    ScheduleAdapter adapter;
    ArrayList<String> subs;
    ArrayList<String> subx;
    ArrayList<String> times;
    AppCompatActivity activity = this;
    DatabaseHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);

        handler = new DatabaseHandler(this); // Initialize DatabaseHandler

        FloatingActionButton fab = findViewById(R.id.fab_sch);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = new Intent(activity, CreateScheduleActivity.class);
                startActivity(launchIntent);
            }
        });

        subs = new ArrayList<>();
        times = new ArrayList<>();
        subx = new ArrayList<>();
        recyclerView = findViewById(R.id.schedulerRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadSchedules();
    }

    public void loadSchedules() {
        subs.clear();
        times.clear();
        subx.clear();
        String qu = "SELECT * FROM SCHEDULE ORDER BY subject";
        Cursor cursor = handler.execQuery(qu);
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(activity, "No Schedules Available", Toast.LENGTH_LONG).show();
        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                subx.add(cursor.getString(1));
                subs.add(cursor.getString(1) + "\nfor " + cursor.getString(0) + "\nat " + cursor.getString(2) + " : " + cursor.getString(3));
                times.add(cursor.getString(2));
                cursor.moveToNext();
            }
        }
        adapter = new ScheduleAdapter(this, subs, times, subx);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scheduler_menu, menu);
        return true;
    }

    public void refresh(MenuItem item) {
        loadSchedules();
    }
}
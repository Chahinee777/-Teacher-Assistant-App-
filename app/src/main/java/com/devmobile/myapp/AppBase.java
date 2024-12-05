package com.devmobile.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.devmobile.myapp.components.About;
import com.devmobile.myapp.components.GridAdapter;
import com.devmobile.myapp.components.ManageAccountActivity;
import com.devmobile.myapp.components.SettingsActivity;
import com.devmobile.myapp.database.DatabaseHandler;
import com.devmobile.myapp.login.LoginActivity;
import com.devmobile.myapp.services.ConnectivityService;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class AppBase extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static ArrayList<String> divisions;
    public static DatabaseHandler handler;
    public static AppCompatActivity activity;
    ArrayList<String> basicFields;
    GridAdapter adapter;
    GridView gridView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        basicFields = new ArrayList<>();
        handler = new DatabaseHandler(this);
        activity = this;

        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
        }
        divisions = new ArrayList<>();
        divisions.add("L3DSI1");
        divisions.add("L3DSI2");
        divisions.add("L3DSI3");

        gridView = findViewById(R.id.grid);
        basicFields.add("ATTENDANCE");
        basicFields.add("SCHEDULER");
        basicFields.add("NOTES");
        basicFields.add("PROFILE");

        adapter = new GridAdapter(this, basicFields);
        gridView.setAdapter(adapter);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set greeting text
        String teacherName = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("username", "Teacher");
        TextView navTeacherName = navigationView.getHeaderView(0).findViewById(R.id.nav_teacher_name);
        navTeacherName.setText("Good Morning, " + teacherName);

        // Start ConnectivityService
        Intent connectivityServiceIntent = new Intent(this, ConnectivityService.class);
        startService(connectivityServiceIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mai_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.about) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_manage_account) {
            Intent intent = new Intent(this, ManageAccountActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            // Handle logout
            getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .edit()
                .putBoolean("isLoggedIn", false)
                .apply();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
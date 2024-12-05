package com.devmobile.myapp.notes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.devmobile.myapp.R;
import com.devmobile.myapp.AppBase;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NoteAdapter adapter;
    ArrayList<String> titles;
    ArrayList<String> contents;
    AppCompatActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        titles = new ArrayList<>();
        contents = new ArrayList<>();

        FloatingActionButton fab = findViewById(R.id.fab_Note);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = new Intent(activity, NoteCreate.class);
                startActivity(launchIntent);
            }
        });

        recyclerView = findViewById(R.id.noteRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadNotes();
    }

    public void loadNotes() {
        titles.clear();
        contents.clear();
        String username = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("username", "");
        String qu = "SELECT * FROM NOTES WHERE username = '" + username + "'";
        Cursor cursor = AppBase.handler.execQuery(qu);
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(getBaseContext(), "No Notes Found", Toast.LENGTH_LONG).show();
        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                titles.add(cursor.getString(0));
                contents.add(cursor.getString(1));
                cursor.moveToNext();
            }
            adapter = new NoteAdapter(this, titles, contents);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_menu, menu);
        return true;
    }

    public void refreshNote(MenuItem item) {
        loadNotes();
    }
}
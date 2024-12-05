package com.devmobile.myapp.notes;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devmobile.myapp.R;
import com.devmobile.myapp.AppBase;

public class UpdateNoteActivity extends AppCompatActivity {

    EditText titleEditText, bodyEditText;
    String originalTitle, originalBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        titleEditText = findViewById(R.id.noteTitle);
        bodyEditText = findViewById(R.id.noteBody);

        originalTitle = getIntent().getStringExtra("title");
        originalBody = getIntent().getStringExtra("body");

        titleEditText.setText(originalTitle);
        bodyEditText.setText(originalBody);

        Button updateButton = findViewById(R.id.noteUpdateButton);
        assert updateButton != null;
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNote();
            }
        });
    }

        private void updateNote() {
        String newTitle = titleEditText.getText().toString();
        String newBody = bodyEditText.getText().toString();
        String username = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("username", "");
    
        if (newTitle.isEmpty() || newBody.isEmpty()) {
            Toast.makeText(this, "Title and Body cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
    
        String qu = "UPDATE NOTES SET title = '" + newTitle + "', body = '" + newBody + "' WHERE title = '" + originalTitle + "' AND body = '" + originalBody + "' AND username = '" + username + "'";
        if (AppBase.handler.execAction(qu)) {
            Toast.makeText(this, "Note Updated", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Update Failed", Toast.LENGTH_LONG).show();
        }
    }
}
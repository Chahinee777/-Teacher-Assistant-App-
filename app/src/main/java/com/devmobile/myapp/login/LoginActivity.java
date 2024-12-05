package com.devmobile.myapp.login;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devmobile.myapp.AppBase;
import com.devmobile.myapp.R;
import com.devmobile.myapp.database.DatabaseHandler;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHandler = new DatabaseHandler(this);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser();
            }
        });
    }

    private void authenticateUser() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Log.d("LoginActivity", "Authenticating user: " + username);

        String query = "SELECT * FROM TEACHER WHERE username = '" + username + "' AND password = '" + password + "'";
        Cursor cursor = dbHandler.execQuery(query);
        if (cursor != null && cursor.moveToFirst()) {
            Log.d("LoginActivity", "Authentication successful");
            // Save login state and username
            getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .edit()
                .putBoolean("isLoggedIn", true)
                .putString("username", username)
                .apply();

            Intent intent = new Intent(LoginActivity.this, AppBase.class);
            startActivity(intent);
            finish();
        } else {
            Log.d("LoginActivity", "Authentication failed");
            Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }
}
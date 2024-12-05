package com.devmobile.myapp.register;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devmobile.myapp.R;
import com.devmobile.myapp.database.DatabaseHandler;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHandler = new DatabaseHandler(this);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username and Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String qu = "INSERT INTO TEACHER (username, password) VALUES ('" + username + "', '" + password + "')";
        if (dbHandler.execAction(qu)) {
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show();
        }
    }
}
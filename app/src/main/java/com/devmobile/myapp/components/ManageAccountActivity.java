package com.devmobile.myapp.components;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.devmobile.myapp.R;
import com.devmobile.myapp.database.DatabaseHandler;

public class ManageAccountActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CALL_PERMISSION = 1;
    private static final int STORAGE_PERMISSION_CODE = 101;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button saveButton;
    private Button logoutButton;
    private Button emailButton;
    private Button smsButton;
    private Button callButton;
    private Button changeLogoButton;
    private ImageView teacherLogo;
    private DatabaseHandler dbHandler;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        dbHandler = new DatabaseHandler(this);

        teacherLogo = findViewById(R.id.teacher_logo);
        teacherLogo.setImageResource(R.drawable.teacher);

        TextView currentUsernameTextView = findViewById(R.id.current_username);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        saveButton = findViewById(R.id.save_button);
        logoutButton = findViewById(R.id.logout_button);
        emailButton = findViewById(R.id.email_button);
        smsButton = findViewById(R.id.sms_button);
        callButton = findViewById(R.id.call_button);
        changeLogoButton = findViewById(R.id.change_logo_button);

        // Load current username
        String currentUsername = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("username", "");
        currentUsernameTextView.setText(currentUsername);
        usernameEditText.setText(currentUsername);

        // Load saved image URI
        Uri savedImageUri = getImageUri();
        if (savedImageUri != null) {
            teacherLogo.setImageURI(savedImageUri);
        }

        changeLogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStoragePermissions()) {
                    openImageChooser();
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAccount();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS();
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            teacherLogo.setImageURI(imageUri);
            saveImageUri(imageUri);
        }
    }

    private void saveImageUri(Uri uri) {
        getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
            .edit()
            .putString("teacherLogoUri", uri.toString())
            .apply();
    }

    private Uri getImageUri() {
        String uriString = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("teacherLogoUri", null);
        return uriString != null ? Uri.parse(uriString) : null;
    }

    private boolean checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return false;
        } else {
            return true;
        }
    }

    private boolean checkStoragePermissions() {
        boolean readPermission = checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        boolean writePermission = checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        return readPermission && writePermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageChooser();
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "chahineaouledamor721@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void sendSMS() {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", "53168665", null));
        smsIntent.putExtra("sms_body", "Contact app support");
        startActivity(smsIntent);
    }

    private void makeCall() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:53168665"));
            startActivity(callIntent);
        }
    }

    private void updateAccount() {
        String newUsername = usernameEditText.getText().toString();
        String newPassword = passwordEditText.getText().toString();

        if (newUsername.isEmpty() && newPassword.isEmpty()) {
            Toast.makeText(this, "Username and Password cannot both be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUsername = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("username", "");
        boolean updateSuccess = false;

        if (!newUsername.isEmpty() && !newPassword.isEmpty()) {
            String query = "UPDATE TEACHER SET username = '" + newUsername + "', password = '" + newPassword + "' WHERE username = '" + currentUsername + "'";
            updateSuccess = dbHandler.execAction(query);
        } else if (!newUsername.isEmpty()) {
            String query = "UPDATE TEACHER SET username = '" + newUsername + "' WHERE username = '" + currentUsername + "'";
            updateSuccess = dbHandler.execAction(query);
        } else if (!newPassword.isEmpty()) {
            String query = "UPDATE TEACHER SET password = '" + newPassword + "' WHERE username = '" + currentUsername + "'";
            updateSuccess = dbHandler.execAction(query);
        }

        if (updateSuccess) {
            if (!newUsername.isEmpty()) {
                getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("username", newUsername)
                    .apply();
            }
            Toast.makeText(this, "Account updated successfully", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update account", Toast.LENGTH_LONG).show();
        }
    }

    private void logout() {
        getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
            .edit()
            .putBoolean("isLoggedIn", false)
            .apply();
        Intent intent = new Intent(ManageAccountActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
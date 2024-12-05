package com.devmobile.myapp.profile;

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

public class EditStudentActivity extends AppCompatActivity {
    AppCompatActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__student);

        EditText adm = findViewById(R.id.register_);
        String admissionNumber = getIntent().getStringExtra("admissionNumber");
        if (admissionNumber != null) {
            adm.setText(admissionNumber);
            loadStudentDetails(admissionNumber);
        }

        Button loadButton = findViewById(R.id.loadForEdit);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String admissionNumber = adm.getText().toString().toUpperCase();
                loadStudentDetails(admissionNumber);
            }
        });

        Button saveEdit = findViewById(R.id.buttonSAVEEDITS);
        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = findViewById(R.id.edit_name_);
                EditText roll = findViewById(R.id.roll_);
                EditText contact = findViewById(R.id.contact_);
                String qu = "UPDATE STUDENT SET name = '" + name.getText().toString() + "' , " +
                        " roll = " + roll.getText().toString() + " , contact = '" + contact.getText().toString() + "' " +
                        "WHERE regno = '" + adm.getText().toString().toUpperCase() + "'";
                Log.d("EditStudentActivity", qu);
                if (AppBase.handler.execAction(qu)) {
                    Toast.makeText(getBaseContext(), "Edit Saved", Toast.LENGTH_LONG).show();
                    activity.finish();
                } else {
                    Toast.makeText(getBaseContext(), "Error Occurred", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadStudentDetails(String admissionNumber) {
        String qu = "SELECT * FROM STUDENT WHERE regno = '" + admissionNumber + "'";
        Cursor cr = AppBase.handler.execQuery(qu);
        if (cr == null || cr.getCount() == 0) {
            Toast.makeText(getBaseContext(), "No Such Student", Toast.LENGTH_LONG).show();
        } else {
            cr.moveToFirst();
            try {
                EditText name = findViewById(R.id.edit_name_);
                EditText roll = findViewById(R.id.roll_);
                EditText contact = findViewById(R.id.contact_);
                assert name != null;
                name.setText(cr.getString(0));
                assert roll != null;
                roll.setText(cr.getString(4));
                assert contact != null;
                contact.setText(cr.getString(3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
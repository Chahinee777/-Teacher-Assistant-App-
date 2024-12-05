package com.devmobile.myapp.schedule;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devmobile.myapp.R;
import com.devmobile.myapp.AppBase;

public class UpdateScheduleActivity extends AppCompatActivity {

    EditText subjectEditText;
    TimePicker timePicker;
    String originalSubject, originalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_schedule);

        subjectEditText = findViewById(R.id.subjectName);
        timePicker = findViewById(R.id.timePicker);

        originalSubject = getIntent().getStringExtra("subject");
        originalTime = getIntent().getStringExtra("time");

        subjectEditText.setText(originalSubject);
        // Assuming time is in "HH:mm" format
        String[] timeParts = originalTime.split(":");
        timePicker.setCurrentHour(Integer.parseInt(timeParts[0]));
        timePicker.setCurrentMinute(Integer.parseInt(timeParts[1]));

        Button updateButton = findViewById(R.id.scheduleUpdateButton);
        assert updateButton != null;
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSchedule();
            }
        });
    }

    private void updateSchedule() {
        String newSubject = subjectEditText.getText().toString();
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        String newTime = hour + ":" + minute;

        if (newSubject.isEmpty()) {
            Toast.makeText(this, "Subject cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String qu = "UPDATE SCHEDULE SET subject = '" + newSubject + "', timex = '" + newTime + "' WHERE subject = '" + originalSubject + "' AND timex = '" + originalTime + "'";
        if (AppBase.handler.execAction(qu)) {
            Toast.makeText(this, "Schedule Updated", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Update Failed", Toast.LENGTH_LONG).show();
        }
    }
}
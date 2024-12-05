package com.devmobile.myapp.schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.devmobile.myapp.AppBase;
import com.devmobile.myapp.R;
import com.devmobile.myapp.database.DatabaseHandler;
import com.devmobile.myapp.notification.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateScheduleActivity extends AppCompatActivity {

    Spinner classSelect, daySelect;
    ArrayAdapter<String> adapterSpinner, days;
    DatabaseHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_create);

        handler = new DatabaseHandler(this); // Initialize DatabaseHandler

        classSelect = findViewById(R.id.classSelector);
        daySelect = findViewById(R.id.daySelector);

        adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, AppBase.divisions);
        classSelect.setAdapter(adapterSpinner);

        ArrayList<String> weekdays = new ArrayList<>();
        weekdays.add("MONDAY");
        weekdays.add("TUESDAY");
        weekdays.add("WEDNESDAY");
        weekdays.add("THURSDAY");
        weekdays.add("FRIDAY");
        weekdays.add("SATURDAY");
        weekdays.add("SUNDAY");
        days = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, weekdays);
        daySelect.setAdapter(days);

        Button btn = findViewById(R.id.saveBUTTON_SCHEDULE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSchedule(v);
            }
        });
    }

    private void saveSchedule(View v) {
        String daySelected = daySelect.getSelectedItem().toString();
        String classSelected = classSelect.getSelectedItem().toString();
        EditText editText = findViewById(R.id.subjectName);
        String subject = editText.getText().toString();
        if (subject.length() < 2) {
            Toast.makeText(this, "Enter Valid Subject Name", Toast.LENGTH_SHORT).show();
            return;
        }
        TimePicker timePicker = findViewById(R.id.timePicker);
        int hour = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();

        String sql = "INSERT INTO SCHEDULE VALUES('" + classSelected + "'," +
                "'" + subject + "'," +
                "'" + hour + ":" + min + "'," +
                "'" + daySelected + "');";
        Log.d("Schedule", sql);
        if (handler.execAction(sql)) {
            Toast.makeText(this, "Scheduling Done", Toast.LENGTH_LONG).show();
            scheduleClassReminder(subject);
            finish();
        } else {
            Toast.makeText(this, "Failed To Schedule", Toast.LENGTH_LONG).show();
        }
    }

    private void scheduleClassReminder(String subject) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 1); // Schedule for 1 minute from now

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("subject", subject);
        intent.putExtra("time", "in 1 minute");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}
package com.devmobile.myapp.database;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myAppDatabase";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_TEACHER = "teacher";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    private Activity activity;

    public DatabaseHandler(Activity activity) {
        super(activity, DATABASE_NAME, null, DATABASE_VERSION);
        this.activity = activity;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    public void createTables(SQLiteDatabase db) {
        String CREATE_TEACHER_TABLE = "CREATE TABLE " + TABLE_TEACHER + "("
                + KEY_USERNAME + " TEXT PRIMARY KEY,"
                + KEY_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_TEACHER_TABLE);
    
        // Insert the default teacher
        String insertTeacher = "INSERT INTO " + TABLE_TEACHER + " (" + KEY_USERNAME + ", " + KEY_PASSWORD + ") VALUES ('hend', 'benayed')";
        db.execSQL(insertTeacher);
    
        // Create other tables if needed
        String CREATE_STUDENT_TABLE = "CREATE TABLE IF NOT EXISTS STUDENT(name varchar(1000), cl varchar(100), regno varchar(100) PRIMARY KEY, contact varchar(100), roll int)";
        db.execSQL(CREATE_STUDENT_TABLE);
    
        String CREATE_ATTENDANCE_TABLE = "CREATE TABLE IF NOT EXISTS ATTENDANCE(date varchar(100), hour int, register varchar(100), isPresent int)";
        db.execSQL(CREATE_ATTENDANCE_TABLE);
    
        String CREATE_NOTES_TABLE = "CREATE TABLE IF NOT EXISTS NOTES(title varchar(1000), body varchar(1000), cls varchar(100), sub varchar(100), username varchar(100))";
        db.execSQL(CREATE_NOTES_TABLE);
    
        String CREATE_SCHEDULE_TABLE = "CREATE TABLE IF NOT EXISTS SCHEDULE(class varchar(100), subject varchar(100), timex varchar(100), day varchar(100))";
        db.execSQL(CREATE_SCHEDULE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER);
        db.execSQL("DROP TABLE IF EXISTS STUDENT");
        db.execSQL("DROP TABLE IF EXISTS ATTENDANCE");
        db.execSQL("DROP TABLE IF EXISTS NOTES");
        db.execSQL("DROP TABLE IF EXISTS SCHEDULE");

        // Create tables again
        onCreate(db);
    }

    public boolean execAction(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(query);
            return true;
        } catch (Exception e) {
            Log.e("DatabaseHandler", "Error executing action", e);
            return false;
        }
    }

    public Cursor execQuery(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            return db.rawQuery(query, null);
        } catch (Exception e) {
            Log.e("DatabaseHandler", "Error executing query", e);
            return null;
        }
    }
}
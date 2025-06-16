package com.example.uas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserDB.db";
    private static final int DATABASE_VERSION = 2; // Increment version untuk upgrade
    private static final String TABLE_USERS = "users";
    private static final String TABLE_HISTORY = "history";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_SUBJECT = "subject";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PHONE + " TEXT)";
        db.execSQL(createUsersTable);

        String createHistoryTable = "CREATE TABLE " + TABLE_HISTORY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_SUBJECT + " TEXT, " +
                COLUMN_TIMESTAMP + " LONG)";
        db.execSQL(createHistoryTable);
        android.util.Log.d("DBHelper", "Database created with tables: " + TABLE_USERS + ", " + TABLE_HISTORY);
    }
    public List<String> getRecentHistory(String username) {
        List<String> historyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_SUBJECT + " FROM " + TABLE_HISTORY +
                " WHERE " + COLUMN_USERNAME + " = ? ORDER BY " + COLUMN_TIMESTAMP + " DESC LIMIT 10";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        while (cursor.moveToNext()) {
            historyList.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBJECT)));
        }
        cursor.close();
        db.close();
        return historyList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add new columns to existing users table
            try {
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_NAME + " TEXT");
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_EMAIL + " TEXT");
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_PHONE + " TEXT");
                android.util.Log.d("DBHelper", "Database upgraded: added profile columns");
            } catch (Exception e) {
                android.util.Log.e("DBHelper", "Error upgrading database: " + e.getMessage());
                // If error, recreate tables
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
                onCreate(db);
            }
        }
    }

    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_NAME, username); // Default name sama dengan username
        long result = db.insert(TABLE_USERS, null, values);
        if (result == -1) {
            android.util.Log.e("DBHelper", "Failed to insert user: " + username);
        } else {
            android.util.Log.d("DBHelper", "User inserted: " + username);
        }
        db.close();
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Method baru untuk mendapatkan profile user
    public UserProfile getUserProfile(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_NAME + ", " + COLUMN_EMAIL + ", " +
                COLUMN_PHONE + ", " + COLUMN_PASSWORD + " FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        UserProfile profile = null;
        if (cursor.moveToFirst()) {
            profile = new UserProfile();
            profile.name = cursor.getString(0) != null ? cursor.getString(0) : username;
            profile.email = cursor.getString(1) != null ? cursor.getString(1) : "";
            profile.phone = cursor.getString(2) != null ? cursor.getString(2) : "";
            profile.password = cursor.getString(3) != null ? cursor.getString(3) : "";
        }

        cursor.close();
        db.close();
        return profile;
    }

    // Method baru untuk update profile user
    public boolean updateUserProfile(String username, String name, String email, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_PASSWORD, password);

        int result = db.update(TABLE_USERS, values, COLUMN_USERNAME + " = ?", new String[]{username});

        if (result > 0) {
            android.util.Log.d("DBHelper", "Profile updated for user: " + username);
        } else {
            android.util.Log.e("DBHelper", "Failed to update profile for user: " + username);
        }

        db.close();
        return result > 0;
    }

    public void addHistory(String username, String subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_SUBJECT, subject);
        values.put(COLUMN_TIMESTAMP, System.currentTimeMillis());
        db.insert(TABLE_HISTORY, null, values);
        db.close();
    }

    public List<String> getRecentSubjects(String username) {
        List<String> subjects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_SUBJECT + " FROM " + TABLE_HISTORY +
                " WHERE " + COLUMN_USERNAME + " = ? ORDER BY " + COLUMN_TIMESTAMP + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        while (cursor.moveToNext()) {
            subjects.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBJECT)));
        }
        cursor.close();
        db.close();
        return subjects;
    }

    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USERNAME + " FROM " + TABLE_USERS, null);
        while (cursor.moveToNext()) {
            users.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)));
        }
        cursor.close();
        db.close();
        return users;
    }

    // Inner class untuk UserProfile
    public static class UserProfile {
        public String name;
        public String email;
        public String phone;
        public String password;
    }
}
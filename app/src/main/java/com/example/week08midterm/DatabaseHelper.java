package com.example.week08midterm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "StudentDB.db";
    public static final String TABLE_NAME = "students";
    public static final String COL_ID = "student_id";
    public static final String COL_NAME = "student_name";
    public static final String COL_ENROLLED = "has_enrolled";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " TEXT PRIMARY KEY, " +
                COL_NAME + " TEXT, " +
                COL_ENROLLED + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 1. ADD STUDENT
    public boolean insertStudent(String id, String name, boolean isEnrolled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, id);
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_ENROLLED, isEnrolled ? 1 : 0);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // 2. EDIT STUDENT (Matches by Student ID)
    public boolean updateStudent(String id, String name, boolean isEnrolled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_ENROLLED, isEnrolled ? 1 : 0);

        int result = db.update(TABLE_NAME, contentValues, COL_ID + " = ?", new String[]{id});
        return result > 0;
    }

    // 3. DELETE STUDENT (Matches by Student ID)
    public Integer deleteStudent(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{id});
    }

    // 4. VIEW ALL STUDENTS
    public Cursor getAllStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
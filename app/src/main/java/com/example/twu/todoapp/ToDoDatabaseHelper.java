package com.example.twu.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by twu on 8/4/16.
 */
public class ToDoDatabaseHelper extends SQLiteOpenHelper {
    private static ToDoDatabaseHelper sInstance;

    // Database Info
    private static final String DATABASE_NAME = "toDoDatabase";
    private static final int DATABASE_VERSION = 6;

    // Table Names
    private static final String TABLE_TASKS = "tasks";

    // Task Table Columns
    private static final String KEY_TASK_ID = "id";
    private static final String KEY_TASK_NAME = "name";
    private static final String KEY_TASK_PRIORITY = "priority";
    private static final String KEY_TASK_TIME_ESTIMATE = "timeEstimate";

    public ToDoDatabaseHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS +
                "(" +
                    KEY_TASK_ID + " INTEGER PRIMARY KEY, "  +
                    KEY_TASK_NAME + " TEXT, " +
                    KEY_TASK_PRIORITY + " TEXT, " +
                    KEY_TASK_TIME_ESTIMATE + " INTEGER" +
                ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            onCreate(db);
        }
    }

    public static synchronized ToDoDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ToDoDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public void addTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TASK_NAME, task.name);
            values.put(KEY_TASK_PRIORITY, task.priority);
            values.put(KEY_TASK_TIME_ESTIMATE, task.timeEstimate);

            db.insertOrThrow(TABLE_TASKS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add task to database");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        // SELECT * FROM TASKS
        String TASKS_SELECT_QUERY = String.format("SELECT * FROM %s", TABLE_TASKS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TASKS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Task newTask = new Task();
                    newTask.id = cursor.getInt(cursor.getColumnIndex(KEY_TASK_ID));
                    newTask.name = cursor.getString(cursor.getColumnIndex(KEY_TASK_NAME));
                    newTask.priority = cursor.getString(cursor.getColumnIndex(KEY_TASK_PRIORITY));
                    newTask.timeEstimate = cursor.getInt(cursor.getColumnIndex(KEY_TASK_TIME_ESTIMATE));
                    tasks.add(newTask);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to get tasks from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return tasks;
    }

    public void updateTask (int id, Task updatedTask) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_PRIORITY, updatedTask.priority);
        values.put(KEY_TASK_NAME, updatedTask.name);
        values.put(KEY_TASK_TIME_ESTIMATE, updatedTask.timeEstimate);
        int rows = db.update(TABLE_TASKS, values, KEY_TASK_ID + "= ?",
                new String[] {Integer.toString(id)}
        );
    }

    public void deleteTask (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_TASK_ID + "=?", new String[] {String.valueOf(id)});
    }
}


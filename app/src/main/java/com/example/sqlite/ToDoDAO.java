package com.example.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import java.util.ArrayList;

public class ToDoDAO {
    private final DbHelper dbHelper;

    public ToDoDAO(Context context) {
        dbHelper = new DbHelper(context);
    }

    public ArrayList<ToDo> getListToDo() {
        ArrayList<ToDo> list = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        database.beginTransaction();
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM TODO", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    list.add(new ToDo(cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getInt(5)));
                } while (cursor.moveToNext());
                database.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.e(TAG, "getListTodo" + e);
        } finally {
            database.endTransaction();
        }
        return list;
    }

    public boolean Add(ToDo toDo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TITLE", toDo.getTitle());
        cv.put("CONTENT", toDo.getContent());
        cv.put("DATE", toDo.getDate());
        cv.put("STATUS", 0);
        long check = db.insert("TODO", null, cv);
        return check != -1;
    }

    public boolean Update(ToDo toDo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TITLE", toDo.getTitle());
        cv.put("CONTENT", toDo.getContent());
        cv.put("DATE", toDo.getDate());
        cv.put("TYPE", toDo.getType());
        cv.put("STATUS", toDo.getStatus());

        int rowsAffected = db.update("TODO", cv, "ID=?", new String[]{String.valueOf(toDo.getId())});
        db.close();

        return rowsAffected > 0;
    }

    public boolean Delete(int Id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long check = db.delete("TODO", "ID=?", new String[]{String.valueOf(Id)});
        return check != 1;
    }

    // This method is not used in the provided code
    public boolean deleteToDo(int toDoId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affectedRows = db.delete("TODO", "ID=?", new String[]{String.valueOf(toDoId)});
        return affectedRows > 0;
    }
}

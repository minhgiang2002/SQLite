package com.example.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, "TodoDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE TODO(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TITLE TEXT, CONTENT TEXT, DATE TEXT, TYPE TEXT, STATUS INTEGER)";
        db.execSQL(sql);

        String data = "INSERT INTO TODO VALUES(1, 'Học Java', 'Học Java cơ bản', '27/2/2023', 'Bình thường', 1)," +
                "(2, 'Học React Native', 'Học React Native cơ bản', '24/3/2023', 'Khó', 0)," +
                "(3, 'Học Kotlin', 'Học kotlin cơ bản', '1/4/2023', 'De', 0)";
        db.execSQL(data);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS TODO");
            onCreate(db);
        }
    }
}

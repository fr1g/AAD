package moe.vot.own.projs.aad.pr.navi.contactbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = "myLogs";
    public DBHelper(Context context) {
        super(context, "myDB", null, 1);
    }
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        db.execSQL("create table mytable ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "email text" + ");");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}

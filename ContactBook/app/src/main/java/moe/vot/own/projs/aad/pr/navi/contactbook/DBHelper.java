package moe.vot.own.projs.aad.pr.navi.contactbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = "myLogs";
    String tableName, dbName;

    public DBHelper(Context context, String tableName, String dbName) {
        super(context, dbName, null, 1);
        this.dbName = dbName;
        this.tableName = tableName;
    }
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        db.execSQL("create table " + this.tableName + " ("
                + "id integer primary key autoincrement,"
                + "name text"
                + ",email text"
                + ",date text"
                + ");"); // this can be extracted.
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}

package moe.vot.own.projs.aad.pr.navi.contactbook;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class ContactDbContext implements AutoCloseable {

    public SQLiteDatabase DB;
    private DBHelper _h;
    private ContentValues _cv;

    public ContactDbContext(DBHelper dbHelper){
        _h = dbHelper;
        DB = _h.getWritableDatabase();
    }

    public ContactDbContext Store(String key, String value){
        if(_cv == null) _cv = new ContentValues();
        _cv.put(key, value);
        return this;
    }

    public long Keep(){
        var value = DB.insert(_h.tableName, null, this._cv);
        this._cv = null;
        return value;
    }

    public int Reset() throws android.database.SQLException{
        var res = DB.delete(_h.tableName, null, null);
        DB.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE name = '" + _h.tableName + "'");
        return res;
    }

    public ContactDbContext(String name, String email){
        ContentValues cv = new ContentValues();

    }

    @Override
    public void close() throws Exception {
        if(DB != null) DB.close(); // todo maybe cause problem?
        if(_h != null) _h.close();
    }
}

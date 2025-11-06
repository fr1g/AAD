package moe.vot.own.projs.aad.pr.navi.contactbook;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class ContactDbContext {

    private SQLiteDatabase _db;

    public ContactDbContext(MainActivity.DBHelper dbHelper)

    public ContactDbContext(String name, String email){
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // получаем данные из полей ввода

        // подключаемся к БД
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
    }
}

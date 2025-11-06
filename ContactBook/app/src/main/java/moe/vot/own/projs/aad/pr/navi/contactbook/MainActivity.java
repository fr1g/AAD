package moe.vot.own.projs.aad.pr.navi.contactbook;

import moe.vot.own.projs.aad.pr.navi.contactbook.DBHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = DBHelper.LOG_TAG;

    Button mBtnAdd, mBtnRead, mBtnClear;
    EditText mEdtName, mEdtEmail;
    DBHelper mDbHelper;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Binder.setClickProcess((Button)findViewById(R.id.buttonAdd), (v) -> {
            Log.d(LOG_TAG, "--- Insert in mytable: ---");
            // подготовим данные для вставки в виде пар: наименование столбца -
            // значение
            cv.put("name", name);
            cv.put("email", email);
            // вставляем запись и получаем ее ID
            long rowID = db.insert("mytable", null, cv);
            Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        } );
        mBtnAdd.setOnClickListener(this:: onClick);
        mBtnRead = (Button) findViewById(R.id.buttonRead);
        mBtnRead.setOnClickListener(this :: onClick);
        mBtnClear = (Button) findViewById(R.id.buttonClear);
        mBtnClear.setOnClickListener(this :: onClick);
        mEdtName = (EditText) findViewById(R.id.editTextName);
        mEdtEmail = (EditText) findViewById(R.id.editTextEmail);
        mDbHelper = new DBHelper(this);
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {

        if (v.getId() == R.id.buttonAdd){

        }
        if (v.getId() == R.id.buttonRead){
            Log.d(LOG_TAG, "--- Rows in mytable: ---");
            // делаем запрос всех данных из таблицы mytable, получаем Cursor
            Cursor c = db.query("mytable", null, null, null, null, null, null);
            // ставим позицию курсора на первую строку выборки
            // если в выборке нет строк, вернется false
            if (c.moveToFirst()) {
                // определяем номера столбцов по имени в выборке
                int idColIndex = c.getColumnIndex("id");
                int nameColIndex = c.getColumnIndex("name");
                int emailColIndex = c.getColumnIndex("email");
                do {
                    // получаем значения по номерам столбцов и пишем все в лог
                    Log.d(LOG_TAG,
                            "ID = " + c.getInt(idColIndex) + ", name = "
                                    + c.getString(nameColIndex) + ", email = "
                                    + c.getString(emailColIndex));
                } while (c.moveToNext());
            } else
                Log.d(LOG_TAG, "0 rows");
            c.close();
        }
        if (v.getId() == R.id.buttonClear){
            Log.d(LOG_TAG, "--- Clear mytable: ---");
            // удаляем все записи
            int clearCount = db.delete("mytable", null, null);
            Log.d(LOG_TAG, "deleted rows count = " + clearCount);
        }
        mDbHelper.close();
    }

}
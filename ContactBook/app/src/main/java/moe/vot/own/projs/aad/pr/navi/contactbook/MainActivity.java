package moe.vot.own.projs.aad.pr.navi.contactbook;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = DBHelper.LOG_TAG;

    EditText nameField, emailField;
    DBHelper mDbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new DBHelper(this, "ContactBook", "AADC");
        setContentView(R.layout.activity_main);

        Binder.setClickProcess((Button)findViewById(R.id.buttonAdd), (v) -> {

            Log.d(LOG_TAG, "--- Insert in table: ---");
            try(var context = new ContactDbContext(mDbHelper)) {
                var newId = context.Store("name", nameField.getText().toString())
                            .Store("email", emailField.getText().toString())
                            .Keep();
                Log.d(LOG_TAG, "row inserted, ID = " + newId);

            } catch (Exception ex){ }

        } );

        Binder.setClickProcess((Button) findViewById(R.id.buttonRead), (v) -> {

            Log.d(LOG_TAG, "--- Rows in mytable: ---");

            Cursor c = null;
            try(var context = new ContactDbContext(mDbHelper)){

                c = context.DB.query(mDbHelper.tableName,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);

                if (c.moveToFirst()) {
                    int idColIndex = c.getColumnIndex("id");
                    int nameColIndex = c.getColumnIndex("name");
                    int emailColIndex = c.getColumnIndex("email");
                    do {
                        Log.d(LOG_TAG, "ID = " + c.getInt(idColIndex) + ", name = "
                                        + c.getString(nameColIndex) + ", email = "
                                        + c.getString(emailColIndex));
                    } while (c.moveToNext());
                } else Log.d(LOG_TAG, "0 rows");
            } catch(Exception ex){ }
            finally {
                if(c != null) c.close();
            }

        });

        Binder.setClickProcess((Button) findViewById(R.id.buttonClear), (v) -> {
            try(var context = new ContactDbContext(mDbHelper)){
                Log.d(LOG_TAG, "--- Clear mytable: ---");
                int clearCount = context.Reset();
                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
            } catch(Exception ex) {

            }
        });

        nameField = (EditText) findViewById(R.id.editTextName);
        emailField = (EditText) findViewById(R.id.editTextEmail);
    }


}
package moe.vot.own.projs.aad.pr.navi.contactbook;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.gesture.GestureStroke;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = DBHelper.LOG_TAG;
    EditText nameField, emailField, etDate;
    DBHelper mDbHelper;
    Calendar calendar;
    String dateSelected;
    SimpleDateFormat dateFormat;

    List<Content> resultSet = new ArrayList<>();
    RecyclerView recyclerView;

    void applyList(List<Content> set){
        if(set.isEmpty()) {
            findViewById(R.id.recyclerView).setVisibility(RecyclerView.INVISIBLE);
            findViewById(R.id.noresult).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.recyclerView).setVisibility(RecyclerView.VISIBLE);
            findViewById(R.id.noresult).setVisibility(View.INVISIBLE);
        }
        if(recyclerView != null) {
            Log.d(LOG_TAG, "appl");
            recyclerView.setAdapter(new ContentListRenderAdapter(set));
        }
        Toast.makeText(this, "Total: " + set.size(), Toast.LENGTH_SHORT).show();
    }

    private void setCurrentDate() {
        String currentDate = dateFormat.format(calendar.getTime());
        etDate.setText(currentDate);
        dateSelected = currentDate;
    }

    private void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    String selectedDate = dateFormat.format(calendar.getTime());
                    etDate.setText(selectedDate);
                    dateSelected = selectedDate;
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new DBHelper(this, "ContactBook", "NAADC");
        setContentView(R.layout.activity_main);

        etDate = findViewById(R.id.etDate);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.recyclerView).setVisibility(RecyclerView.INVISIBLE);
        setCurrentDate();
        etDate.setOnClickListener(v -> showDatePicker()); // dont know before.

        Binder.setClickProcess((Button)findViewById(R.id.buttonAdd), (v) -> {

            Log.d(LOG_TAG, "--- Insert in table: ---");
            try(var context = new ContactDbContext(mDbHelper)) {
                var newId = context.Store("name", nameField.getText().toString())
                                   .Store("email", emailField.getText().toString())
                                   .Store("date", dateSelected)
                                   .Keep();
                Log.d(LOG_TAG, "row inserted, ID = " + newId);

            } catch (Exception ex){ }
            findViewById(R.id.buttonRead).performClick();
        } );

        Binder.setClickProcess((Button) findViewById(R.id.buttonRead), (v) -> {
            Log.d(LOG_TAG, "--- Rows in mytable: ---");
            resultSet.clear();
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
                    int dateColIndex = c.getColumnIndex("date");
                    do {
                        resultSet.add(new Content(c.getInt(idColIndex), c.getString(nameColIndex), c.getString(emailColIndex), c.getString(dateColIndex)));
                        Log.d(LOG_TAG, "ID = " + c.getInt(idColIndex) + ", name = "
                                        + c.getString(nameColIndex) + ", email = "
                                        + c.getString(emailColIndex) + ". at: " + c.getString(dateColIndex));
                    } while (c.moveToNext());
                } else Log.d(LOG_TAG, "0 rows");
                applyList(resultSet);
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
            } catch(Exception ex) { }
            findViewById(R.id.buttonRead).performClick();
        });
        nameField = (EditText) findViewById(R.id.editTextName);
        emailField = (EditText) findViewById(R.id.editTextEmail);
    }
}
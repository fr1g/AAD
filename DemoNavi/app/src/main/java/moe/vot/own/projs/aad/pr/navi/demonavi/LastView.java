package moe.vot.own.projs.aad.pr.navi.demonavi;

import static moe.vot.own.projs.aad.pr.navi.demonavi.Binder.setClickProcess;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LastView extends AppCompatActivity {

    final String TAG = "inspect";

    void proccDisplayMsg(){
        String msg = (String)(getIntent().getSerializableExtra("msg"));
        ((TextView)findViewById(R.id.show)).setText(msg == null || msg.isBlank() ? "No Content" : msg);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.d(TAG, "onCreate going");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);
        proccDisplayMsg();
        Log.d(TAG, "decided display");
        setClickProcess((ImageButton)findViewById(R.id.Home), (v) -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            Log.d(TAG, "onClick going");
        });
    }

}

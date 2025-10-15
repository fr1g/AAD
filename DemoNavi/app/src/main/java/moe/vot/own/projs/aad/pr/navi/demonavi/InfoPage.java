package moe.vot.own.projs.aad.pr.navi.demonavi;

import static moe.vot.own.projs.aad.pr.navi.demonavi.Binder.setClickProcess;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class InfoPage extends AppCompatActivity {

    void proccDisplayMsg(){
        String msg = (String)(getIntent().getSerializableExtra("msg"));
        ((TextInputEditText)findViewById(R.id.input)).setText(msg == null || msg.isBlank() ? "" : msg);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        proccDisplayMsg();
        setClickProcess((Button) findViewById(R.id.man), (v) -> {
            var i = new Intent(getApplicationContext(), MainActivity.class);
            var val = ((TextInputEditText)findViewById(R.id.input)).getText();
            i.putExtra("msg", (val == null ? "Nothing there." : val.toString()));
            startActivity(i);
        });
    }

}

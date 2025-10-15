package moe.vot.own.projs.aad.pr.navi.demonavi;

import static moe.vot.own.projs.aad.pr.navi.demonavi.Binder.setClickProcess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{// implements View.OnClickListener {

    boolean isGotContent = false;

    void proccDisplayMsg(){
        String msg = (String)(getIntent().getSerializableExtra("msg"));
        isGotContent = !(msg == null || msg.isBlank());
        ((TextView)findViewById(R.id.textView)).setText(!isGotContent ? "NO-MSG" : msg);
        if(isGotContent) findViewById(R.id.Home).setVisibility(View.VISIBLE);
        else findViewById(R.id.Home).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        proccDisplayMsg();

        setClickProcess((Button) findViewById(R.id.FirstBtn), (view -> {
            startActivity(new Intent(getApplicationContext(), InfoPage.class));
        }));

        setClickProcess((Button) findViewById(R.id.Home), (v) -> {
            var i = new Intent(getApplicationContext(), InfoPage.class);
            var val = ((TextView)findViewById(R.id.textView)).getText();
            i.putExtra("msg", (val == null || val.equals("NO-MSG") ? "" : val.toString()));
            startActivity(i);
        });

        setClickProcess((Button) findViewById(R.id.MakeItBig), (v) -> {
            var i = new Intent(getApplicationContext(), LastView.class);
            var val = ((TextView)findViewById(R.id.textView)).getText();
            i.putExtra("msg", (val == null ? "*Nothing Actually*" : val.toString()));
            startActivity(i);
        });
    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);
        proccDisplayMsg();
    }

}

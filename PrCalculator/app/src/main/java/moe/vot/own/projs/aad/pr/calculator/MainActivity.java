package moe.vot.own.projs.aad.pr.calculator;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {

    final String ERR = "Error", DEFAULT = "Enter something...", EMPTY = "0.0", NOTHING = "";

    Button num0, num1, num2, num3, num4, num5, num6, num7, num8, num9,
            opAc, opC, opSqrt, opSq, opPlus, opMin, opDiv, opTime, opEqual,
            charDot;

    TextView lastFormulae, currentFormulae;
    String last, current, recent; // recent -> last -> current

    boolean canUse = false;

    void setLast( ){ // apply last and formulae
        // the last formulae: it can only subtracted from last operation
        current = last;
        last = recent;
    }

    void setAll(String content){
        var cleaned = content.trim().replaceAll(" {2}", " ");
        recent = last;
        last = current;
        current = cleaned;
    }

    void setCurrent(String content){
        setCurrent(content, false);
    }

    void setErr(){
        setCurrent(ERR, true);
    }

    void setCurrent(String content, boolean override){
        if(content.equals(ERR) && !override) return; // prevent input
        current = content;
        currentFormulae.setText(content);
    }

    void applyView(){
        lastFormulae.setText(last);
        currentFormulae.setText(current);
    }

    void appendToCurrent(String input, boolean isCalcOp){
        if(input.equals(".") || isCalcOp){
            if(current.endsWith(" ") || current.equals(EMPTY) || current.isEmpty() || current.isBlank()){
                setErr();
                return;
            }
        }
        var cleaned = (current + (isCalcOp ? " " + input + " " : input) ).replaceAll(" {2}", "").trim();
        setCurrent(cleaned);
    }

    void setClickProcess(Button btn, Consumer<View> onclick){
        if(!canUse) return;
        btn.setOnClickListener((View.OnClickListener) onclick::accept);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        num0 = findViewById(R.id.button0);
        num1 = findViewById(R.id.button1);
        num2 = findViewById(R.id.button2);
        num3 = findViewById(R.id.button3);
        num4 = findViewById(R.id.button4);
        num5 = findViewById(R.id.button5);
        num6 = findViewById(R.id.button6);
        num7 = findViewById(R.id.button7);
        num8 = findViewById(R.id.button8);
        num9 = findViewById(R.id.button9);
        opAc = findViewById(R.id.buttonClear);
        opC = findViewById(R.id.buttonUndo);
        opPlus = findViewById(R.id.buttonPlus);
        opMin = findViewById(R.id.buttonMinus);
        opDiv = findViewById(R.id.buttonDiv);
        opTime = findViewById(R.id.buttonTimes);
        opEqual = findViewById(R.id.buttonEq);
        opSq = findViewById(R.id.buttonSq);
        opSqrt = findViewById(R.id.buttonSqrt);
        charDot = findViewById(R.id.buttonDot);

        currentFormulae = findViewById(R.id.curr);
        lastFormulae = findViewById(R.id.last);


        setClickProcess(num0, (v) -> {
            appendToCurrent("0", false);
        });


    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            last = "";
            current = NOTHING;
            applyView();
        }, 1000);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            last = "";
            current = EMPTY;
            applyView();
        }, 1500);
        canUse = true;
    }
}
package moe.vot.own.projs.aad.pr.calculator;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.function.Consumer;

import moe.vot.own.projs.aad.pr.calculator.CalcHelper.GeneralCalculation;

public class MainActivity extends AppCompatActivity {

    final String ERR = "Error", DEFAULT = "Enter something...", EMPTY = "0.0", NOTHING = "", DIVIDE = "÷", TIMES = "×";

//    GeneralCalculation calc;

    Button[] nums = new Button[10];
    Button opAc, opC, opSqrt, opSq, opPlus, opMin, opDiv, opTime, opEqual, charDot, Mc, MrOp;

    double savedMVal = 0.0d;

    TextView lastFormulae, currentFormulae;
    String last, current, recent; // recent -> last -> current

    boolean canUse = false, init = true;

    void undo( ){ // apply last and formulae
        // the last formulae: it can only subtracted from last operation
        current = last;
        last = recent;
        recent = "-";
    }

    void next(){
        recent = last;
        last = current;
        // current = ...whatever
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

    String getCurrentPart(){
        var x = (current).trim().split(" ");
        return (current.contains(" ") ? x[x.length - 1] : current);
    }

    boolean tryParseDouble(String x){
        try {
            return !Double.isNaN(Double.parseDouble(x));
        }catch (Exception ex){
            return false;
        }
    }

    void appendToCurrent(String input, boolean isCalcOp){ // todo after clicked dot then input anything will cause empty-up

        if(init){
            if(!(isCalcOp && tryParseDouble(getCurrentPart()))) // current? currentPart? // not operator, not number
                current = "";

            init = false;
        }
        if(isCalcOp && getCurrentPart().endsWith(".") || isCalcOp && !tryParseDouble(getCurrentPart())) return;

        if(input.equals(".")){
            if(getCurrentPart().contains(".")) return;
            if(!tryParseDouble(getCurrentPart())) return;
        }
        var cleaned = (current + (isCalcOp ? " " + input + " " : input) ).replaceAll(" {2}", "");
        setCurrent(cleaned);
    }

    void setClickProcess(Button btn, Consumer<View> onclick){
        btn.setOnClickListener((View.OnClickListener) v -> {
            if(!canUse) return;
            onclick.accept(v);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        nums[0] = findViewById(""); // worse than javascript
        nums[0] = findViewById(R.id.button0);
        nums[1] = findViewById(R.id.button1);
        nums[2] = findViewById(R.id.button2);
        nums[3] = findViewById(R.id.button3);
        nums[4] = findViewById(R.id.button4);
        nums[5] = findViewById(R.id.button5);
        nums[6] = findViewById(R.id.button6);
        nums[7] = findViewById(R.id.button7);
        nums[8] = findViewById(R.id.button8);
        nums[9] = findViewById(R.id.button9);
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

        MrOp = findViewById(R.id.buttonM);
        Mc = findViewById(R.id.buttonMC);

        currentFormulae = findViewById(R.id.curr);
        lastFormulae = findViewById(R.id.last);



        int index = -1;
        for(var sub : nums){
            int finalIndex = ++index; // idiot java.
            setClickProcess(sub, (v) -> {
                appendToCurrent(finalIndex + "", false);
            });
        }

        setClickProcess(charDot, (v) -> {
            Toast.makeText(getApplicationContext(), "init: " + init + ", curr: " + current, LENGTH_SHORT).show();
            if(init && tryParseDouble(getCurrentPart())) init = false;
            appendToCurrent(".", false);
        });

        setClickProcess(opPlus, (v) -> {
            appendToCurrent("+", true);
        });
        setClickProcess(opMin, (v) -> {
            appendToCurrent("-", true);
        });
        setClickProcess(opTime, (v) -> {
            appendToCurrent(TIMES, true);
        });
        setClickProcess(opDiv, (v) -> {
            appendToCurrent(DIVIDE, true);
        });

        setClickProcess(opAc, (v) -> {
            setAll(EMPTY);
            last = "-";
            applyView();
            init = true;
        });

        setClickProcess(opC, (view) -> {
            if(current.equals("-")) return;
            undo();
            applyView();
            init = false;
        });

        setClickProcess(opEqual, (v) -> {
            next();
            try(GeneralCalculation calc = new GeneralCalculation()){
                current = calc.calc(current.replaceAll(TIMES, "*").replaceAll(DIVIDE, "/"));
                Toast.makeText(getApplicationContext(), "last: " + last + ", curr: " + current, LENGTH_SHORT).show();
            }catch (Exception ex){
                current = Objects.requireNonNull(ex.getMessage()).split("..")[0];
            }

            if(!tryParseDouble(current)) setErr(); // not tested

            applyView();
            init = true;
            Toast.makeText(getApplicationContext(), "init: " + init + ", curr: " + current, LENGTH_SHORT).show();
        });

        setClickProcess(opSq, (v) -> {
            next();
            if(!tryParseDouble(current))
                setErr();
            else
                current = Math.pow(Double.parseDouble(current), 2) + "";
            applyView();
            init = true;
        });

        setClickProcess(opSqrt, (v) -> {
            next();
            if(!tryParseDouble(current))
                setErr();
            else
                current = Math.sqrt(Double.parseDouble(current)) + "";
            applyView();
            init = true;
        });

        setClickProcess(MrOp, (v) -> { // saving into M

//            if() return;
            if(init && tryParseDouble(getCurrentPart())){
                savedMVal = Double.parseDouble(current); // ?
                if(savedMVal == 0.0d) return;
                MrOp.setText("MR");
                return;
            }
            if(savedMVal != 0.0d && !tryParseDouble(getCurrentPart()) && !current.isEmpty() && !current.isBlank()){ // OP ending, not blanc, then insert M value inside.
                appendToCurrent(savedMVal + "", false);
            }

        });

        setClickProcess(Mc, (v) -> { // clear M
            savedMVal = 0.0d;
            MrOp.setText("MP");
        });

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            last = DEFAULT;
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
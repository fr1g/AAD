package moe.vot.own.projs.aad.pr.calculator;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.function.Consumer;

import moe.vot.own.projs.aad.pr.calculator.CalcHelper.GeneralCalculation;

public class MainActivity extends AppCompatActivity {

    final String ERR = "Error", DEFAULT = "Enter something...", EMPTY = "0.0", NOTHING = "", DIVIDE = "÷", TIMES = "×";

//    GeneralCalculation calc;

    Button num0, num1, num2, num3, num4, num5, num6, num7, num8, num9,
            opAc, opC, opSqrt, opSq, opPlus, opMin, opDiv, opTime, opEqual,
            charDot;

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

    void appendToCurrent(String input, boolean isCalcOp){
//        if(isCalcOp){ // if result contains dot set dotted true
//            dotted = false;
//        }
        if(init){
            if(!(isCalcOp && tryParseDouble(current)))
                current = "";

            init = false;
        }
        if(isCalcOp && getCurrentPart().endsWith(".") || isCalcOp && !tryParseDouble(getCurrentPart())) return;

        if(input.equals(".")){
            if(getCurrentPart().contains(".")) return;
            if(current.endsWith(" ") || current.equals(EMPTY) || current.isEmpty() || current.isBlank()) return;
        }
        var cleaned = (current + (isCalcOp ? " " + input + " " : input) ).replaceAll(" {2}", "");
        setCurrent(cleaned);
    }

    void setClickProcess(Button btn, Consumer<View> onclick){
        btn.setOnClickListener((View.OnClickListener) new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!canUse) return;
                onclick.accept(v);
            }
        });
    }

//    void deInit(String with){
//        if(current.equals(EMPTY)){
//
//        }
//        else{
//
//        }
//    }

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

//        calc = new GeneralCalculation();

        setClickProcess(num0, (v) -> {
            appendToCurrent("0", false);
        });
        setClickProcess(num1, (v) -> {
            appendToCurrent("1", false);
        });
        setClickProcess(num2, (v) -> {
            appendToCurrent("2", false);
        });
        setClickProcess(num3, (v) -> {
            appendToCurrent("3", false);
        });
        setClickProcess(num4, (v) -> {
            appendToCurrent("4", false);
        });
        setClickProcess(num5, (v) -> {
            appendToCurrent("5", false);
        });
        setClickProcess(num6, (v) -> {
            appendToCurrent("6", false);
        });
        setClickProcess(num7, (v) -> {
            appendToCurrent("7", false);
        });
        setClickProcess(num8, (v) -> {
            appendToCurrent("8", false);
        });
        setClickProcess(num9, (v) -> {
            appendToCurrent("9", false);
        });

        setClickProcess(charDot, (v) -> {
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
            undo();
            applyView();
            init = false;
        });

        setClickProcess(opEqual, (v) -> {
            next();
            try(GeneralCalculation calc = new GeneralCalculation()){
                current = calc.calc(current.replaceAll(TIMES, "*")).replaceAll(DIVIDE, "/");

            }catch (Exception ex){
                current = Objects.requireNonNull(ex.getMessage()).split("..")[0];
            }

            applyView();
            init = true;
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
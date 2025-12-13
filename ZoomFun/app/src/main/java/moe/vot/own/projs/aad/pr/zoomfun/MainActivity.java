package moe.vot.own.projs.aad.pr.zoomfun;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends Activity implements View.OnTouchListener {

    String str = "", result = "", N = "; \n";
    TextView text;
    int up, down;
    boolean isTouching = false;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        text = new TextView(this);
        text.setTextSize(24);
        text.setVisibility(View.VISIBLE);
        text.setText("init");
        text.setOnTouchListener(this);
        text.performClick();
        setContentView(text);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motion){
        view.performClick();
        var actionMask = motion.getActionMasked();
        var pointerIndex = motion.getActionIndex();
        var pointerCount = motion.getPointerCount();

//        Log.i(":mask:", (MotionEvent.actionToString(actionMask)));

        switch (actionMask){
            case MotionEvent.ACTION_UP:
                isTouching = false;
            case MotionEvent.ACTION_POINTER_UP:
                up = pointerIndex;
                break;

            case MotionEvent.ACTION_DOWN:
                isTouching = true;
            case MotionEvent.ACTION_POINTER_DOWN:
                down = pointerIndex;
                break;

            case MotionEvent.ACTION_MOVE:
                str = "";
                for(int i = 0; i < 8; i++){
//                    Log.v(":perform: s", str);
                    str += ("index: " + i + N);
                    if(i < pointerCount){
                        str += ("id: " + motion.getPointerId(i) + N);
                        str += ("x: " + motion.getX(i) + N);
                        str += ("y: " + motion.getY(i) + N);
                    } else {
                        str += ("[empty]" + N);
                    }
                    str += "---" + N;
                }
                break;

//            default: return false;
        }

        result = "down: " + down + "; up: " + up + N;
        if(isTouching)
            result += ("pointerCount: " + pointerCount + "; \n" + str);

        text.setText(N + str);
//        Log.v(":perform:", str);
        return true;
    }
}
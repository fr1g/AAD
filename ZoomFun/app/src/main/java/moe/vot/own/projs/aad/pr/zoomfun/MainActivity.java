package moe.vot.own.projs.aad.pr.zoomfun;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    ImageView zoom;

    float[] zoomScope = new float[4];
    float distanceNew, distanceLast;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zoom = new ImageView(this);

        var rootLayout = new FrameLayout(this);
        rootLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        zoom.setAdjustViewBounds(true); // 这个非常重要！
        zoom.setImageResource(R.drawable.ic_launcher_background);
        zoom.setScaleType(ImageView.ScaleType.FIT_CENTER);

        var params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,  // 宽度占满
                FrameLayout.LayoutParams.WRAP_CONTENT   // 高度自适应
        );
        params.gravity = Gravity.CENTER;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;

        params.setMargins(20, 20, 20, 20);
        zoom.setLayoutParams(params);
        zoom.setBackgroundColor(0x80FF0000);
        rootLayout.addView(zoom);

        text = new TextView(this);
        text.setTextSize(12);

        var textParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(16, 16, 16, 16);
//        textParams.
        text.setLayoutParams(textParams);

        text.setText("init");

        zoom.setOnTouchListener(this);
        text.setOnTouchListener(this);

        rootLayout.addView(text);
        setContentView(rootLayout);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motion){
        var actionMask = motion.getActionMasked();

        if ( view instanceof TextView && actionMask == MotionEvent.ACTION_DOWN )
        {
            zoom.setScaleX(1);
            zoom.setScaleY(1);
            Toast.makeText(this, "RESET IMG", Toast.LENGTH_SHORT).show();
            return true;
        }

        var isZoomIn = false;
        view.performClick();
        var pointerIndex = motion.getActionIndex();
        var pointerCount = motion.getPointerCount();

//        Log.i(":mask:", (MotionEvent.actionToString(actionMask)));
        distanceLast = distanceNew;
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
                for(int i = 0; i < 2; i++){
                    str += ("index: " + i + N);
                    if(i >= pointerCount) continue;
                    if(i == 0){
                        zoomScope[0] = motion.getX(i);
                        zoomScope[1] = motion.getY(i);
                    } else if(i == 1){
                        zoomScope[2] = motion.getX(i);
                        zoomScope[3] = motion.getY(i);
                    }
                    str += ("x1: " + zoomScope[0] + N + "y1: " + zoomScope[1] + N);
                    str += ("x2: " + zoomScope[2] + N + "y2: " + zoomScope[3] + N);
                    str += "---" + N;

                }
                if(zoom != null){ // 230 as lowest
                    distanceNew = (float) Math.hypot(zoomScope[0] - zoomScope[2], zoomScope[1] - zoomScope[3]);
                    if(isTouching) isZoomIn = distanceLast - distanceNew < 0;
                    var scale = zoom.getScaleX(); // definitely same, but needed to set them same
                    var rate = distanceNew / 500;
                    var newScale = (isZoomIn ? (scale + scale * rate) : (scale - scale * rate));
                    zoom.setScaleX(newScale);
                    zoom.setScaleY(newScale);
                }
                break;

//            default: return false;
        }

        result = "down: " + down + "; up: " + up + N;
        if(isTouching)
            result += ("pointerCount: " + pointerCount + "; \n" + str);

        str += ("d: old: " + distanceLast + "; new: " + distanceNew + "; isZoomIn: " + isZoomIn + N);
        text.setText(N + str);
//        Log.v(":perform:", str);

        return true;
    }
}
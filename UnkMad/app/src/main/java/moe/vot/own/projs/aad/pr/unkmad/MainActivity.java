package moe.vot.own.projs.aad.pr.unkmad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    int a(int percentage){
        return (percentage * 255) / 100;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawView(this));

    }

    Path triangle(float[][] points){
        // skip err hand.
        var path = new Path();
        path.moveTo(points[0][0], points[0][1]);
        path.lineTo(points[1][0], points[1][1]);
        path.lineTo(points[2][0], points[2][1]);
        path.close();
        return path;
    }

//    class DrawView extends SurfaceView implements SurfaceHolder.Callback {
    class DrawView extends View {

        Paint p;
        Path t, tReversed, tMiddle, tFit;

        Rect r;

        public DrawView(Context context) {
            super(context);
            p = new Paint();
            t = triangle(new float[][]{ {500, 500}, {600, 500}, {399, 600} });
            tReversed = triangle(new float[][]{ {500, 500}, {399, 500}, {600, 600} });
            tMiddle = triangle(new float[][]{ {525, 500}, {475, 500}, {500, 450} });

            r = new Rect(160, 350, 660, 750);
            tFit = triangle(new float[][]{ {260, 1050}, {510, 1234}, {760, 1050} });
        }

        @Override
        protected void onDraw(Canvas canvas){
            canvas.drawARGB(a(91), 100, 180, 199);
            p.setColor(Color.parseColor("#fecaca"));
            p.setStrokeWidth(10);

            canvas.drawPath(t, p);
            canvas.drawPath(tReversed, p);
            canvas.drawPath(tMiddle, p);

            p.setColor(Color.WHITE);
            r.offset(100, 300);
            canvas.drawRect(r, p);
            canvas.drawPath(tFit, p);

        }
    }
}
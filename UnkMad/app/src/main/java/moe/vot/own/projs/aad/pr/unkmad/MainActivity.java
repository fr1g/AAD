package moe.vot.own.projs.aad.pr.unkmad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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


//    class DrawView extends SurfaceView implements SurfaceHolder.Callback {
    class DrawView extends View {

        Paint p;
        RectF rf;

        float[] p1, p2;

        public DrawView(Context context) {
            super(context);
            p = new Paint();
            rf = new RectF(555, 111, 777, 333);
            p1 = new float[] { 100, 50, 150, 100, 150, 200, 50, 100 };
            p2 = new float[] {
                    300, 200,  600, 200,  300, 300,
                    600, 300,  400, 100,  400, 500,
                    100, 500,  400
            };
        }

        @Override
        protected void onDraw(Canvas canvas){
            canvas.drawARGB(a(95), 200, 180, 199);
            p.setColor(Color.parseColor("#fecaca"));
            p.setStrokeWidth(5);
            canvas.drawPoints(p1, p);
            p.setColor(Color.parseColor("#FED7AA"));
            canvas.drawLines(p2, p);
            canvas.drawRoundRect(rf, 23, 23, p);
            rf.offset(0 ,123);
            canvas.drawOval(rf, p);
            rf.offsetTo(888, 111);
            rf.inset(0, -33);
            canvas.drawArc(rf, 90, 270, true, p);
            rf.offset(0, 123);
            canvas.drawArc(rf, 90, 270, false, p);
            p.setStrokeWidth(23);
            canvas.drawLine(123, 234, 123, 456, p);
            p.setColor(Color.parseColor("#DEA8A5"));
            p.setTextSize(33);
            canvas.drawText("Moscow", 128, 500, p);
            p.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Vladimir", 240, 512, p);
            p.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("Novosibirsk", 550, 525, p);

        }

    }

    class DrawThread extends Thread {
        private boolean _running = false;
        private SurfaceHolder _holder;

        public DrawThread(SurfaceHolder sh){
            this._holder = sh;
        }

        public void setRunning(boolean run){
            this._running = run;
        }

        @Override
        public void run(){
            Canvas c;
            while (_running)  {
                c = null;
                try {
                    c = _holder.lockCanvas(null);
                    if(c == null) continue;
                    c.drawColor(Color.WHITE);
                } catch (Exception ex){
                    Logger.getLogger("DT").log(Level.INFO, "EX: " + ex.getMessage());
                } finally {
                    if(c != null) _holder.unlockCanvasAndPost(c);
                }
            }
        }

    }

}
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
        Rect r;

        public DrawView(Context context) {
            super(context);
            p = new Paint();
            r = new Rect(100, 300, 300, 500);
        }

        @Override
        protected void onDraw(Canvas canvas){
            canvas.drawARGB(a(95), 100, 180, 199);
            p.setColor(Color.parseColor("#fecaca"));
            p.setStrokeWidth(10);
            p.setTextSize(33);

            canvas.drawText((
                "W: " + getWidth() + "; H: " + getHeight() + "; // says, use getWidth instead of canvas.getWidth"
            ), 10, 150, p);


            p.setStyle(Paint.Style.FILL);
            r.offset(123, 0);
            canvas.drawRect(r, p);
            p.setStyle(Paint.Style.STROKE);
            r.offset(256, 0);
            canvas.drawRect(r, p);
            p.setStyle(Paint.Style.FILL_AND_STROKE);
            r.offset(234, 0);
            canvas.drawRect(r, p);
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
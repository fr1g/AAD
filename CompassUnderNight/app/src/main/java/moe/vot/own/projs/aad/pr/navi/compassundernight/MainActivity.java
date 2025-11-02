package moe.vot.own.projs.aad.pr.navi.compassundernight;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager _sensor;

    Sensor _magnetic, _light, _acc;

    TextView _l, _d, _b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Binder.setClickProcess((Button) findViewById(R.id.button), (v) -> {
            startActivity(new Intent(getApplicationContext(), PrimerReproduceActivity.class));
        });

        _sensor = (SensorManager) getSystemService(SENSOR_SERVICE);

        _light = _sensor.getDefaultSensor(Sensor.TYPE_LIGHT);
        _magnetic = _sensor.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        _acc = _sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        _l = findViewById(R.id.light);
        _d = findViewById(R.id.direction);
        _b = findViewById(R.id.brightness);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    float[] lastMagData = {0f, 0f, 0f};
    float[] lastAccData = {0.38f, 9.8f, 0f};

    void setWindowBrightness(float brightnessPercent) {
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        layoutParams.screenBrightness = Math.max(0.0f, Math.min(1.0f, brightnessPercent / 100.0f));

        window.setAttributes(layoutParams);
    }

    void resetWindowBrightness() {
        setWindowBrightness(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        switch (event.sensor.getType()){
            case Sensor.TYPE_LIGHT:
                var val = event.values[0];
                var brightness = 0f;
                _l.setText(val + "");
                if(val >= 66)
                    brightness = 20f;
                else brightness = (100f - val / 2);
                setWindowBrightness(brightness);
                _b.setText(brightness + "%");
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                lastMagData = new float[] {event.values[0], event.values[1], event.values[2]};
                break;
            case Sensor.TYPE_ACCELEROMETER:
                lastAccData = new float[] {event.values[0], event.values[1], event.values[2]};
//                Log.println(Log.DEBUG, "IMP", "ACCC: " + lastMagData[0] + " " + lastMagData[1]);

                break;
            default:
                return;
        }
        _d.setText(calcDir(lastAccData, lastMagData));
    }

    String calcDir(float[] acc, float[] mag){
        float[] R = new float[9]; // 旋转矩阵
        float[] values = new float[3]; // 存放方向结果

        // 计算旋转矩阵
        var n = SensorManager.getRotationMatrix(R, null, acc, mag);
        // 从旋转矩阵中获取方向，values[0]即为方位角（弧度）
        values = SensorManager.getOrientation(R, values);

        // 将方位角从弧度转换为角度
        float azimuth = (float) Math.toDegrees(values[0]);
        // 将角度调整到0-360度范围
        if (azimuth < 0) {
            azimuth += 360;
        }

        Log.println(Log.DEBUG, "IMP", azimuth + " !!!-- " + n);

        var returns = "";
        if(337.5 < azimuth || azimuth <= 22.5) returns = "N";
        else if(292.5 < azimuth && azimuth <= 337.5) returns = "NW";
        else if(247.5 < azimuth && azimuth <= 292.5) returns = "W";
        else if(202.5 < azimuth && azimuth <= 247.5) returns = "SW";
        else if(157.5 < azimuth && azimuth <= 202.5) returns = "S";
        else if(112.5 < azimuth && azimuth <= 157.5) returns = "SE";
        else if(67.5 < azimuth && azimuth <= 112.5) returns = "E";
        else if(22.5 < azimuth && azimuth <= 67.5) returns = "NE";
        else returns = "not included: " + azimuth;

        return returns;
     }

    @Override
    protected void onStart() {
        super.onStart();
        _sensor.registerListener(this, _light,
                SensorManager.SENSOR_DELAY_GAME);
        _sensor.registerListener(this, _magnetic,
                SensorManager.SENSOR_DELAY_GAME);
        _sensor.registerListener(this, _acc,
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        _sensor.unregisterListener(this, _light);
        _sensor.unregisterListener(this, _magnetic);
        _sensor.unregisterListener(this, _acc);
    }

}
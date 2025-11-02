package moe.vot.own.projs.aad.pr.navi.compassundernight;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PrimerReproduceActivity extends AppCompatActivity implements SensorEventListener {

    TextView mAccelerometerX,
            mAccelerometerY,
            mAccelerometerZ,
            mMagneticX,
            mMagneticY,
            mMagneticZ,
            mProximity, mLight;
    SensorManager sensorManager;
    Sensor mAccelerometerSensor,
            mProximitySensor,
            mMagneticSensor,
            mLightSensor;
    float mMaxValue;
    float mValue;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.primer);
        // 0: title
        mAccelerometerX = findViewById(R.id.textview1);
        mAccelerometerY = findViewById(R.id.textview2);
        mAccelerometerZ = findViewById(R.id.textview3);
        // 4: title
        mMagneticX = findViewById(R.id.textview5);
        mMagneticY = findViewById(R.id.textview6);
        mMagneticZ = findViewById(R.id.textview7);
        // 8: title
        mProximity = findViewById(R.id.textview9);
        // 10: title
        mLight = findViewById(R.id.textview11);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometerSensor =
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagneticSensor =
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mProximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mLightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mMaxValue = mLightSensor.getMaximumRange();
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mAccelerometerX.setText(Float.toString(event.values[0]));
            mAccelerometerY.setText(Float.toString(event.values[1]));
            mAccelerometerZ.setText(Float.toString(event.values[2]));
        }
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            mMagneticX.setText(Float.toString(event.values[0]));
            mMagneticY.setText(Float.toString(event.values[1]));
            mMagneticZ.setText(Float.toString(event.values[2]));
        }
        if(event.sensor.getType() == Sensor.TYPE_PROXIMITY){
            mProximity.setText(Float.toString(event.values[0]));
        }
        if(event.sensor.getType() == Sensor.TYPE_LIGHT){

            mLight.setText(Float.toString(event.values[0]));
            mValue =event.values[0];
            WindowManager.LayoutParams layout = getWindow().getAttributes();
            layout.screenBrightness = (int)(255f * mValue / mMaxValue);
            getWindow().setAttributes(layout);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, mAccelerometerSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, mMagneticSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, mProximitySensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, mLightSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this, mAccelerometerSensor);
        sensorManager.unregisterListener(this, mMagneticSensor);
        sensorManager.unregisterListener(this, mProximitySensor);
        sensorManager.unregisterListener(this, mLightSensor);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
//        sensorManager.unregisterListener(this, mLightSensor); // why so?
    }

}

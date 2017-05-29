package com.mi12.divita_pfister.geoin;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class StepSensor implements SensorEventListener {

    private MainActivity display;

    private SensorManager mSensorManager;
    private Sensor sensorStep;

    int SENSOR_DELAY = SensorManager.SENSOR_DELAY_FASTEST;

    public StepSensor(MainActivity display){
        this.display = display;

        mSensorManager = (SensorManager) display.getSystemService(Context.SENSOR_SERVICE);
        sensorStep = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        mSensorManager.registerListener(this, sensorStep, SENSOR_DELAY);
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            // Trigger stuff here
            // Create HistoryValue
            //display.displaySetStep();
        }
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}

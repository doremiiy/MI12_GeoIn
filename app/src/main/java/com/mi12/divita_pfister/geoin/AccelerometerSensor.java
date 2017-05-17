package com.mi12.divita_pfister.geoin;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.FloatProperty;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AccelerometerSensor implements SensorEventListener {

    /***** Attributs *****/
    private MainActivity display;

    private SensorManager mSensorManager;
    private Sensor sensorAccelerometer;
    private int SENSOR_DELAY = 1000;

    private AccelerometerValue accelerometerValue = null;
    private AccelerometerValue gravity = new AccelerometerValue(0, 0, 0);

    static final float ALPHA = 0.25f;

    private List<AccelerometerValue> AccelerometerValue_l;

    private boolean acquisitionStarted = false;

    private int stepCounter;

    private float previousZvalue = 0;
    /**********/

    /**
     * Constructeur
     *
     * @param display
     */
    public AccelerometerSensor(MainActivity display) {
        this.display = display;
        this.stepCounter = 0;

        this.AccelerometerValue_l = new ArrayList<AccelerometerValue>();

        mSensorManager = (SensorManager) display.getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this, sensorAccelerometer, SENSOR_DELAY);
    }

    public List<AccelerometerValue> getAccelerometerValueList(){
        return this.AccelerometerValue_l;
    }

    public void resetAccelerometerValueList(){
        this.AccelerometerValue_l = new ArrayList<AccelerometerValue>();
    }

    public int getStepCounter(){
        return this.stepCounter;
    }

    public void resetStepCounter(){
        this.stepCounter = 0;
        display.setStepCounterLabel();
    }

    /**
     * méthode appelée lorsqu'une nouvelle valeur de l'accélerometre est recue
     *
     * @param event
     */
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if(accelerometerValue != null){
                previousZvalue = accelerometerValue.getZvalue();
            }

            accelerometerValue = gravityFilter(new AccelerometerValue(event.values[0], event.values[1], event.values[2]));

            display.setAccelerometerXLabel(Float.toString(accelerometerValue.getXvalue()));
            display.setAccelerometerYLabel(Float.toString(accelerometerValue.getYvalue()));
            display.setAccelerometerZLabel(Float.toString(accelerometerValue.getZvalue()));

            if(accelerometerValue != null && this.acquisitionStarted == true){
                this.AccelerometerValue_l.add(accelerometerValue);
                if(accelerometerValue.getZvalue() >= 0.5 && previousZvalue < 0.5){
                    this.stepCounter += 1;
                    display.setStepCounterLabel();
                }
            }
        }
    }

    public void switchAcquisitionState(){
        this.acquisitionStarted = !this.acquisitionStarted;
    }

    protected AccelerometerValue gravityFilter(AccelerometerValue input) {
        // Isolate the force of gravity with the low-pass filter.
        gravity.setValues(
                ALPHA * gravity.getXvalue() + (1 - ALPHA) * input.getXvalue(),
                ALPHA * gravity.getYvalue() + (1 - ALPHA) * input.getYvalue(),
                ALPHA * gravity.getZvalue() + (1 - ALPHA) * input.getZvalue()
        );

        return new AccelerometerValue(
                input.getXvalue() - gravity.getXvalue(),
                input.getYvalue() - gravity.getYvalue(),
                input.getZvalue() - gravity.getZvalue()
        );
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}

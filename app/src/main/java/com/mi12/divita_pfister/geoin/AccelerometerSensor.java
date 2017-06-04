package com.mi12.divita_pfister.geoin;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.FloatProperty;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccelerometerSensor implements SensorEventListener {


    private MainActivity display;

    private SensorManager mSensorManager;
    private Sensor sensorAccelerometer;
    private int SENSOR_DELAY = 100000;

    private AccelerometerValue accelerometerValue = null;
    private AccelerometerValue gravity = new AccelerometerValue(0, 0, 0);

    private int ACCELEROMETER_RECORD_SIZE = 10;
    private static final float ALPHA = 0.25f;
    private float VELOCITY_THRESHOLD = 4f;
    private int TIMESTAMP_THRESHOLD = 250000000;
    private int ACCELERATION_SIZE = 50;

    private float[] accelerometerRecord_a = new float[ACCELEROMETER_RECORD_SIZE];
    private int accelerometerCounter = 0;
    private long lastTimestamp = 0;
    private float oldVelocity = 0;
    private int stepCounter = 0;
    private int OrientationZVectorCounter = 0;
    private float[] accelerationX = new float[ACCELERATION_SIZE];
    private float[] accelerationY = new float[ACCELERATION_SIZE];
    private float[] accelerationZ = new float[ACCELERATION_SIZE];

    /**
     * Constructeur
     *
     * @param MainActivity display
     */
    public AccelerometerSensor(MainActivity display) {
        this.display = display;

        mSensorManager = (SensorManager) display.getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    /**
     * Retourne le nombre de pas depuis la dernière remise à 0
     *
     * @return int
     */
    public int getStepCounter() {
        return this.stepCounter;
    }

    /**
     * Permet de setter la valeur du champ stepcounter
     */
    public void setStepCounter(int value) {
        this.stepCounter = value;
    }

    /**
     * Fonction qui remet à 0 le compteur de pas
     */
    public void resetStepCounter() {
        this.stepCounter = 0;
        display.setStepCounterLabel();
    }

    /**
     * méthode appelée lorsqu'une nouvelle valeur de l'accélerometre est recue
     *
     * @param SensorEvent event
     */
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            AccelerometerValue accelerometerValue = new AccelerometerValue(event.values[0], event.values[1], event.values[2]);
            registerNewAccelerometerValue(event.timestamp, accelerometerValue);
        }
    }

    private void registerNewAccelerometerValue(long timestamp, AccelerometerValue accelerometerValue) {
        OrientationZVectorCounter++;
        accelerationX[OrientationZVectorCounter % ACCELERATION_SIZE] = accelerometerValue.getXvalue();
        accelerationY[OrientationZVectorCounter % ACCELERATION_SIZE] = accelerometerValue.getYvalue();
        accelerationZ[OrientationZVectorCounter % ACCELERATION_SIZE] = accelerometerValue.getZvalue();

        float[] orientationZ = new float[3];
        orientationZ[0] = vectorSum(accelerationX) / Math.min(OrientationZVectorCounter, ACCELERATION_SIZE);
        orientationZ[1] = vectorSum(accelerationY) / Math.min(OrientationZVectorCounter, ACCELERATION_SIZE);
        orientationZ[2] = vectorSum(accelerationZ) / Math.min(OrientationZVectorCounter, ACCELERATION_SIZE);

        float normZ = normalizeVector(orientationZ);

        orientationZ[0] = orientationZ[0] / normZ;
        orientationZ[1] = orientationZ[1] / normZ;
        orientationZ[2] = orientationZ[2] / normZ;

        float realZ = (orientationZ[0] * accelerometerValue.getXvalue() + orientationZ[1] * accelerometerValue.getYvalue() + orientationZ[2] * accelerometerValue.getZvalue()) - normZ;

        accelerometerCounter++;
        accelerometerRecord_a[accelerometerCounter % ACCELEROMETER_RECORD_SIZE] = realZ;

        float velocity = vectorSum(accelerometerRecord_a);

        if(velocity > VELOCITY_THRESHOLD && oldVelocity <= VELOCITY_THRESHOLD && timestamp - lastTimestamp > TIMESTAMP_THRESHOLD){
            stepCounter++;
            display.setStepCounterLabel();
            lastTimestamp = timestamp;
        }

        oldVelocity = velocity;
    }

    public float normalizeVector(float[] vector){
        float result = 0;
        for(int i=0; i<vector.length; i++){
            result += vector[i] * vector[i];
        }
        return (float) Math.sqrt(result);
    }

    private float vectorSum(float[] vector) {
        float result = 0;
        for (int i = 0; i < vector.length; i++) {
            result += vector[i];
        }
        return result;
    }

    /**
     * Fonction qui filtre la gravité sur les valeurs de l'accéléromètre
     *
     * @param AccelerometerValue input
     * @return AccelerometerValue
     */
    private AccelerometerValue gravityFilter(AccelerometerValue input) {
        //Isolation de la gravité avec un filtre passe-bas
        gravity.setValues(
                ALPHA * gravity.getXvalue() + (1 - ALPHA) * input.getXvalue(),
                ALPHA * gravity.getYvalue() + (1 - ALPHA) * input.getYvalue(),
                ALPHA * gravity.getZvalue() + (1 - ALPHA) * input.getZvalue()
        );

        //On enlève ensuite la gravité aux mesures et on retourne les valeurs filtrées
        return new AccelerometerValue(
                input.getXvalue() - gravity.getXvalue(),
                input.getYvalue() - gravity.getYvalue(),
                input.getZvalue() - gravity.getZvalue()
        );
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}

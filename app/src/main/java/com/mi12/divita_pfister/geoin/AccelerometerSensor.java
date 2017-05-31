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

    private static final float ALPHA = 0.25f;

    private List<AccelerometerValue> AccelerometerValue_l;

    private boolean acquisitionStarted = false;

    private int stepCounter;

    private boolean recording_for_step_counter = false;
    private List<Float> accelerometerValueYFiltered_l;
    private List<Float> accelerometerValueZFiltered_l;

    private double min_Y_value, max_Y_value, min_Z_value, max_Z_value;
    private double Y_VALUE_THRESHHOLD = 0.5;
    private double Y_HEIGHT_THRESHOLD = 0.5;
    private double Z_HEIGHT_THRESHOLD = 0.5;


    /**
     * Constructeur
     *
     * @param MainActivity display
     */
    private AccelerometerSensor(MainActivity display) {
        this.display = display;
        this.stepCounter = 0;

        this.AccelerometerValue_l = new ArrayList<>();

        this.accelerometerValueYFiltered_l = new ArrayList<>();
        this.accelerometerValueZFiltered_l = new ArrayList<>();

        mSensorManager = (SensorManager) display.getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this, sensorAccelerometer, SENSOR_DELAY);
    }

    /**
     * Retourne la liste des valeurs de l'accéléromètre conservées
     *
     * @return List<AccelerometerValue>
     */
    private List<AccelerometerValue> getAccelerometerValueList() {
        return this.AccelerometerValue_l;
    }

    /**
     * Remet à 0 la liste des valeurs d'accéléromètre conservées
     */
    private void resetAccelerometerValueList() {
        this.AccelerometerValue_l = new ArrayList<AccelerometerValue>();
    }

    /**
     * Retourne la liste des valeurs de Y filtrées de l'accéléromètre
     *
     * @return List<Float>
     */
    private List<Float> getAccelerometerValueYFiltered_l() {
        return this.accelerometerValueYFiltered_l;
    }
    /**
     * Retourne la liste des normes filtrées de l'accéléromètre
     *
     * @return List<Float>
     */
    private List<Float> getAccelerometerValueZFiltered_l() {
        return this.accelerometerValueZFiltered_l;
    }

    /**
     * Remet à 0 la liste des valeurs de y filtrées de l'accéléromètre
     */
    private void resetAccelerometerValueYFiltered() {
        this.accelerometerValueYFiltered_l = new ArrayList<>();
    }

    /**
     * Remet à 0 la liste des valeurs de z filtrées de l'accéléromètre
     */
    private void resetAccelerometerValueZFiltered() {
        this.accelerometerValueZFiltered_l = new ArrayList<>();
    }

    /**
     * Retourne le nombre de pas depuis la dernière remise à 0
     *
     * @return int
     */
    private int getStepCounter() {
        return this.stepCounter;
    }

    /**
     * Permet de setter la valeur du champ stepcounter
     */
    private void setStepCounter(int value) {
        this.stepCounter = value;
    }

    /**
     * Fonction qui remet à 0 le compteur de pas
     */
    private void resetStepCounter() {
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
            // Filtrage de la gravité
            accelerometerValue = gravityFilter(new AccelerometerValue(event.values[0], event.values[1], event.values[2]));

            display.setAccelerometerXLabel(Float.toString(accelerometerValue.getXvalue()));
            display.setAccelerometerYLabel(Float.toString(accelerometerValue.getYvalue()));
            display.setAccelerometerZLabel(Float.toString(accelerometerValue.getZvalue()));

            //Récupération des valeurs pour les exporter sur scilab/matlab
            if (this.acquisitionStarted) {
                //On ajoute la valeur dans la liste
                this.AccelerometerValue_l.add(accelerometerValue);
            }

            if (accelerometerValue.getYvalue() >= Y_VALUE_THRESHHOLD && !recording_for_step_counter) {
                recording_for_step_counter = true;
            }

            if (accelerometerValue.getYvalue() < Y_HEIGHT_THRESHOLD && recording_for_step_counter) {
                recording_for_step_counter = false;
                //traitement de la liste
                max_Y_value = Collections.max(getAccelerometerValueYFiltered_l());
                min_Y_value = Collections.min(getAccelerometerValueYFiltered_l());
                max_Z_value = Collections.max(getAccelerometerValueZFiltered_l());
                min_Z_value = Collections.min(getAccelerometerValueZFiltered_l());
                if (max_Y_value - min_Y_value >= Y_HEIGHT_THRESHOLD && max_Z_value - min_Z_value >= Z_HEIGHT_THRESHOLD) {
                    setStepCounter(getStepCounter() + 1);
                    display.setStepCounterLabel();
                }
                resetAccelerometerValueYFiltered();
                resetAccelerometerValueZFiltered();
            }

            if(recording_for_step_counter){
                this.accelerometerValueYFiltered_l.add(accelerometerValue.getYvalue());
                this.accelerometerValueZFiltered_l.add(accelerometerValue.getZvalue());
            }
        }
    }

    /**
     * Fonction qui change l'état de la variable acquisitionStarted lorsque l'utilisateur appuie sur le bouton
     */
    private void switchAcquisitionState() {
        this.acquisitionStarted = !this.acquisitionStarted;
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

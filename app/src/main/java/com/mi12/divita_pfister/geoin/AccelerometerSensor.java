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

    /***** Attributs *****/
    private MainActivity display;

    private SensorManager mSensorManager;
    private Sensor sensorAccelerometer;
    private int SENSOR_DELAY = 100000;

    private AccelerometerValue accelerometerValue = null;
    private AccelerometerValue gravity = new AccelerometerValue(0, 0, 0);

    static final float ALPHA = 0.25f;

    private List<AccelerometerValue> AccelerometerValue_l;

    private boolean acquisitionStarted = false;

    private int stepCounter;

    private boolean recording_for_step_counter = false;
    private List<Double> AccelerometerValueNormFiltered_l;

    private double NORM_THRESHHOLD = 0.2;
    private double HEIGHT_THRESHOLD = 0.2;

    private double accelerometerValueNorm;

    private double min_value, max_value;
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

        this.AccelerometerValueNormFiltered_l = new ArrayList<Double>();

        mSensorManager = (SensorManager) display.getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this, sensorAccelerometer, SENSOR_DELAY);
    }

    /**
     * Retourne la liste des valeurs de l'accéléromètre conservées
     *
     * @return
     */
    public List<AccelerometerValue> getAccelerometerValueList() {
        return this.AccelerometerValue_l;
    }

    /**
     * Remet à 0 la liste des valeurs d'accéléromètre conservées
     */
    public void resetAccelerometerValueList() {
        this.AccelerometerValue_l = new ArrayList<AccelerometerValue>();
    }

    /**
     * Retourne la liste des normes filtrées de l'accéléromètre
     *
     * @return
     */
    public List<Double> getAccelerometerValueNormFiltered() {
        return this.AccelerometerValueNormFiltered_l;
    }

    /**
     * Remet à 0 la liste des normes filtrées de l'accéléromètre
     */
    public void resetAccelerometerValueNormFiltered() {
        this.AccelerometerValueNormFiltered_l = new ArrayList<Double>();
    }

    /**
     * Retourne le nombre de pas depuis la dernière remise à 0
     *
     * @return
     */
    public int getStepCounter() {
        return this.stepCounter;
    }

    /**
     * Permet de setter la valeur du champ stepcounter
     *
     * @param value
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
     * @param event
     */
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Filtrage de la gravité
            accelerometerValue = gravityFilter(new AccelerometerValue(event.values[0], event.values[1], event.values[2]));

            //accelerometerValueNorm = accelerometerValue.getNorm();

            display.setAccelerometerXLabel(Float.toString(accelerometerValue.getXvalue()));
            display.setAccelerometerYLabel(Float.toString(accelerometerValue.getYvalue()));
            display.setAccelerometerZLabel(Float.toString(accelerometerValue.getZvalue()));

            //Récupération des valeurs pour les exporter sur scilab/matlab
            if (this.acquisitionStarted == true) {
                //On ajoute la valeur dans la liste
                this.AccelerometerValue_l.add(accelerometerValue);
            }

            if (accelerometerValueNorm >= NORM_THRESHHOLD && recording_for_step_counter == false) {
                recording_for_step_counter = true;
            }

            if (accelerometerValueNorm < NORM_THRESHHOLD && recording_for_step_counter == true) {
                recording_for_step_counter = false;
                //traitement de la liste
                max_value = Collections.max(getAccelerometerValueNormFiltered());
                min_value = Collections.min(getAccelerometerValueNormFiltered());
                if (max_value - min_value >= HEIGHT_THRESHOLD) {
                    setStepCounter(getStepCounter() + 1);
                    display.setStepCounterLabel();
                }
                resetAccelerometerValueNormFiltered();
            }

            if(recording_for_step_counter == true){
                this.AccelerometerValueNormFiltered_l.add(accelerometerValueNorm);
            }
        }
    }

    /**
     * Fonction qui change l'état de la variable acquisitionStarted lorsque l'utilisateur appuie sur le bouton
     */
    public void switchAcquisitionState() {
        this.acquisitionStarted = !this.acquisitionStarted;
    }

    /**
     * Fonction qui filtre la gravité sur les valeurs de l'accéléromètre
     *
     * @param input
     * @return
     */
    protected AccelerometerValue gravityFilter(AccelerometerValue input) {
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

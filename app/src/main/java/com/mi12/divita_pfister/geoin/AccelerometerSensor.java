package com.mi12.divita_pfister.geoin;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class AccelerometerSensor implements SensorEventListener {

    private MapsActivity display;

    private SensorManager mSensorManager;
    private Sensor sensorAccelerometer;

    private int ACCELEROMETER_RECORD_SIZE = 10;
    private float VELOCITY_THRESHOLD = 4f;
    private int TIMESTAMP_THRESHOLD = 250000000;
    private int ACCELERATION_SIZE = 50;
    private int SENSOR_DELAY = 10000;

    private float[] accelerometerRecord_a = new float[ACCELEROMETER_RECORD_SIZE];
    private int accelerometerCounter = 0;
    private long lastTimestamp = 0;
    private float oldVelocity = 0;
    private int stepCounter = 0;
    private int orientationZVectorCounter = 0;
    private float[] accelerationX = new float[ACCELERATION_SIZE];
    private float[] accelerationY = new float[ACCELERATION_SIZE];
    private float[] accelerationZ = new float[ACCELERATION_SIZE];

    private VectorMath vecMath = new VectorMath();

    /**
     * Constructeur
     * @param MapsActivity display
     */
    public AccelerometerSensor(MapsActivity display) {
        this.display = display;

        mSensorManager = (SensorManager) display.getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    /**
     * Retourne le nombre de pas depuis la dernière remise à 0
     * @return int
     */
    public int getStepCounter() {
        return this.stepCounter;
    }

    /**
     * méthode appelée lorsqu'une nouvelle valeur de l'accélerometre est recue
     * @param SensorEvent event
     */
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            AccelerometerValue accelerometerValue = new AccelerometerValue(event.values[0], event.values[1], event.values[2]);
            registerNewAccelerometerValue(event.timestamp, accelerometerValue);
        }
    }

    /**
     * Traitement réalisé sur les nouvelles valeurs d'accélération recues : estimation de la composante Z, filtrage de la gravité et déctection du pas
     * @param timestamp
     * @param accelerometerValue
     */
    private void registerNewAccelerometerValue(long timestamp, AccelerometerValue accelerometerValue) {
        // Récupération dans un tableau des valeurs de l'accéléromètre
        float[] accelerometerValue_a = new float[3];
        accelerometerValue_a[0] = accelerometerValue.getXvalue();
        accelerometerValue_a[1] = accelerometerValue.getYvalue();
        accelerometerValue_a[2] = accelerometerValue.getZvalue();

        // Détermination de l'orientation du vecteur Z
        orientationZVectorCounter++;
        accelerationX[orientationZVectorCounter % ACCELERATION_SIZE] = accelerometerValue_a[0];
        accelerationY[orientationZVectorCounter % ACCELERATION_SIZE] = accelerometerValue_a[1];
        accelerationZ[orientationZVectorCounter % ACCELERATION_SIZE] = accelerometerValue_a[2];

        float[] orientationZ = new float[3];
        orientationZ[0] = vecMath.vectorSum(accelerationX) / Math.min(orientationZVectorCounter, ACCELERATION_SIZE);
        orientationZ[1] = vecMath.vectorSum(accelerationY) / Math.min(orientationZVectorCounter, ACCELERATION_SIZE);
        orientationZ[2] = vecMath.vectorSum(accelerationZ) / Math.min(orientationZVectorCounter, ACCELERATION_SIZE);

        float normZ = vecMath.normalizeVector(orientationZ);

        orientationZ[0] = orientationZ[0] / normZ;
        orientationZ[1] = orientationZ[1] / normZ;
        orientationZ[2] = orientationZ[2] / normZ;

        // Suppression de la gravité sur la composante de l'accélération portée par Z
        float realZ = vecMath.dotMult(orientationZ, accelerometerValue_a) - normZ;
        accelerometerCounter++;
        accelerometerRecord_a[accelerometerCounter % ACCELEROMETER_RECORD_SIZE] = realZ;

        float velocity = vecMath.vectorSum(accelerometerRecord_a);

        /* Détermination du pas : si l'accélération calculée sur les ACCELEROMETER_RECORD_SIZE
        derniers records est suffisamment grande, et que l'accélération au temps t-1 est
        suffisamment petite, et que le temps est sufisamment grand, on considère qu'un pas est fait
        */
        if(velocity > VELOCITY_THRESHOLD && oldVelocity <= VELOCITY_THRESHOLD && timestamp - lastTimestamp > TIMESTAMP_THRESHOLD){
            stepDetected(timestamp);
            lastTimestamp = timestamp;
        }

        oldVelocity = velocity;
    }

    /**
     * Fonction appelée lorsqu'un pas est detecté. On récupère également le timestamp pour traitemements postérieurs
     * @param timestamp
     */
    public void stepDetected(long timestamp){
        stepCounter++;
        // display.setStepCounterLabel();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

}

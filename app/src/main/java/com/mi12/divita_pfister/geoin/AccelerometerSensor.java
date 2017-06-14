package com.mi12.divita_pfister.geoin;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class AccelerometerSensor implements SensorEventListener {

    private Controller controller;

    private SensorManager mSensorManager;
    private Sensor sensorAccelerometer;

    private int ACCELEROMETER_RECORD_SIZE = 10;
    public static float VELOCITY_THRESHOLD = 12f;
    private int TIMESTAMP_THRESHOLD = 250000000;
    private int ACCELERATION_SIZE = 50;
    private int SENSOR_DELAY = SensorManager.SENSOR_DELAY_FASTEST;

    private float[] accelerometerRecord_a = new float[ACCELEROMETER_RECORD_SIZE];
    private int accelerometerCounter = 0;
    private long lastTimestamp = 0;
    private float oldVelocity = 0;
    private int orientationZVectorCounter = 0;
    private float[] accelerationX = new float[ACCELERATION_SIZE];
    private float[] accelerationY = new float[ACCELERATION_SIZE];
    private float[] accelerationZ = new float[ACCELERATION_SIZE];

    /**
     * Constructeur
     * @param Controller controller
     */
    public AccelerometerSensor(Controller controller) {

        this.controller = controller;

        mSensorManager = (SensorManager) controller.display.getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, sensorAccelerometer, SENSOR_DELAY);
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
        orientationZ[0] = VectorMath.vectorSum(accelerationX) / Math.min(orientationZVectorCounter, ACCELERATION_SIZE);
        orientationZ[1] = VectorMath.vectorSum(accelerationY) / Math.min(orientationZVectorCounter, ACCELERATION_SIZE);
        orientationZ[2] = VectorMath.vectorSum(accelerationZ) / Math.min(orientationZVectorCounter, ACCELERATION_SIZE);

        float normZ = VectorMath.normalizeVector(orientationZ);

        orientationZ[0] = orientationZ[0] / normZ;
        orientationZ[1] = orientationZ[1] / normZ;
        orientationZ[2] = orientationZ[2] / normZ;

        // Suppression de la gravité sur la composante de l'accélération portée par Z
        float realZ = VectorMath.dotMult(orientationZ, accelerometerValue_a) - normZ;
        accelerometerCounter++;
        accelerometerRecord_a[accelerometerCounter % ACCELEROMETER_RECORD_SIZE] = realZ;

        float velocity = VectorMath.vectorSum(accelerometerRecord_a);

        /* Détermination du pas : si l'accélération calculée sur les ACCELEROMETER_RECORD_SIZE
        derniers records est suffisamment grande, et que l'accélération au temps t-1 est
        suffisamment petite, et que le temps est sufisamment grand, on considère qu'un pas est fait
        */
        if(velocity > VELOCITY_THRESHOLD && oldVelocity <= VELOCITY_THRESHOLD && timestamp - lastTimestamp > TIMESTAMP_THRESHOLD){
            controller.onStepDetected(timestamp);
            lastTimestamp = timestamp;
        }

        oldVelocity = velocity;
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

}

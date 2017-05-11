package com.mi12.divita_pfister.geoin;


import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.content.Context;


public class Orientation implements SensorEventListener {

    private int SENSOR_DELAY = 1000;
    Context mContext;

    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    private SensorManager mSensorManager;
    private Sensor sensorAccelerometer, sensorMagneticField;

    public Orientation(Context mContext){
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mSensorManager.registerListener(this, sensorAccelerometer, SENSOR_DELAY);
        mSensorManager.registerListener(this, sensorMagneticField, SENSOR_DELAY);
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mAccelerometerReading,
                    0, mAccelerometerReading.length);
        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mMagnetometerReading,
                    0, mMagnetometerReading.length);
        }
    }

    public float[] getOrientationAngles() {
        mSensorManager.getRotationMatrix(
            mRotationMatrix, null, mAccelerometerReading, mMagnetometerReading
        );
        mSensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
        return mOrientationAngles;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    // detroy : mSensorManager.unregisterListener(this);
}

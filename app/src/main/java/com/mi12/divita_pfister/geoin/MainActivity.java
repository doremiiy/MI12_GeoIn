package com.mi12.divita_pfister.geoin;


import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.content.Context;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements SensorEventListener, View.OnClickListener {

    private int SENSOR_DELAY = 1000;

    private TextView labelX;
    private TextView labelY;
    private TextView labelZ;
    private TextView info;

    private Button acquisition;

    private boolean acquisition_started = false;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private List<Accelerometer> AccelerometerValue_l;
    private Accelerometer[] AccelerometerValue_a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        labelX = (TextView) findViewById(R.id.labelX);
        labelY = (TextView) findViewById(R.id.labelY);
        labelZ = (TextView) findViewById(R.id.labelZ);
        info = (TextView) findViewById(R.id.info);

        acquisition = (Button) findViewById(R.id.acquisition);
        acquisition.setOnClickListener(MainActivity.this);

        labelX.setText("X value");
        labelY.setText("Y value");
        labelZ.setText("Z value");

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SENSOR_DELAY);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER && acquisition_started == true) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            Accelerometer AccelerometerValue = new Accelerometer(x, y, z);

            labelX.setText(Float.toString(x));
            labelY.setText(Float.toString(y));
            labelZ.setText(Float.toString(z));

            AccelerometerValue_l.add(AccelerometerValue);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SENSOR_DELAY);
    }

    public void onClick(View view) {
        if (view.equals(acquisition)) {
            if (acquisition_started == false) {
                AccelerometerValue_l = new ArrayList<Accelerometer>();
                acquisition_started = true;
                acquisition.setText("Arrêter l'acquisition");
            } else {
                acquisition_started = false;
                AccelerometerValue_a = AccelerometerValue_l.toArray(new Accelerometer[AccelerometerValue_l.size()]);
                acquisition.setText("Commencer l'acquisition");

                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(path, "test.txt");
                try {
                    path.mkdirs();
                    OutputStream os = new FileOutputStream(file);
                    for (int i = 0; i < AccelerometerValue_a.length; i++) {
                        os.write((AccelerometerValue_a[i].toString() + "\n").getBytes());
                    }
                    os.close();
                } catch (IOException e) {
                    Log.w("External Storage", "Error writing" + file, e);
                }
            }
        }
    }
}

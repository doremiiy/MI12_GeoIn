package com.mi12.divita_pfister.geoin;


import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.content.Context;
import android.os.SystemClock;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements SensorEventListener, View.OnClickListener {

    private TextView labelX;
    private TextView labelY;
    private TextView labelZ;
    private TextView WCET_result;

    private Button start_wcet;

    private boolean start_wcet_clicked = false;

    private long start_time, end_time, result_time[];
    private int cmp;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        labelX = (TextView) findViewById(R.id.labelX);
        labelY = (TextView) findViewById(R.id.labelY);
        labelZ = (TextView) findViewById(R.id.labelZ);
        WCET_result = (TextView) findViewById(R.id.WCET_result);

        start_wcet = (Button) findViewById(R.id.Start_wcet);
        start_wcet.setOnClickListener(MainActivity.this);

        labelX.setText("X value");
        labelY.setText("Y value");
        labelZ.setText("Z value");

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        cmp = 0;
        result_time = new long[10000];
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int max_loop = 100;
        end_time = System.nanoTime();
        if (start_wcet_clicked){
            result_time[cmp] = end_time - start_time;
            if (cmp < max_loop) cmp++;
            else {
                long min = result_time[0], max = result_time[0];
                long mean = 0;
                for (int i=0;i<max_loop;i++){
                    if (min > result_time[i]) min = result_time[i];
                    else if (max < result_time[i]) max = result_time[i];
                    mean += result_time[i];
                }
                mean /= max_loop;
                WCET_result.setText("Min: " + Long.toString(min) + " ns\nMax : " + Long.toString(max) + " ns\nMean: " + Long.toString(mean) + " ns" );
            }
        }
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            labelX.setText(Float.toString(x));
            labelY.setText(Float.toString(y));
            labelZ.setText(Float.toString(z));
        }
        start_time = System.nanoTime();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onClick(View v) {
        start_wcet_clicked = true;
    }
}
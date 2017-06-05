package com.mi12.divita_pfister.geoin;


import android.os.Bundle;
import android.app.Activity;

import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {


    /***** Définition des objets de la vue *****/
    private TextView step_counter;
    private Button step_counter_reset_button;

    private OrientationSensor Orientation;
    private AccelerometerSensor Accelerometer;
    /**********/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        step_counter = (TextView) findViewById(R.id.step_counter);
        step_counter.setText("0");

        step_counter_reset_button = (Button) findViewById(R.id.step_counter_reset_button);
        step_counter_reset_button.setOnClickListener(MainActivity.this);

        Orientation = new OrientationSensor(this);
        Accelerometer = new AccelerometerSensor(this);
    }

    /**
     * Méthode pour définir l'action des clics sur les bouttons
     *
     * @param view
     */
    public void onClick(View view) {
        if(view.equals(step_counter_reset_button)){
            Accelerometer.resetStepCounter();
        }
    }

    public void setStepCounterLabel(){
        step_counter.setText(Integer.toString(Accelerometer.getStepCounter()));
    }

}

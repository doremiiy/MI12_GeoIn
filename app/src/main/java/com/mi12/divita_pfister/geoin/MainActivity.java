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
    private TextView accelerometerX;
    private TextView accelerometerY;
    private TextView accelerometerZ;
    private TextView compassX;
    private TextView compassY;
    private TextView compassZ;
    private TextView step_counter;
    private Button step_counter_reset_button;
    private Button acquisition_button;
    /**********/

    private boolean acquisition_button_started = false;

    private OrientationSensor Orientation;
    private AccelerometerSensor Accelerometer;

    private List<AccelerometerValue> AccelerometerValue_l;
    private AccelerometerValue[] AccelerometerValue_a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accelerometerX = (TextView) findViewById(R.id.accelerometerX);
        accelerometerY = (TextView) findViewById(R.id.accelerometerY);
        accelerometerZ = (TextView) findViewById(R.id.accelerometerZ);
        accelerometerX.setText("X value");
        accelerometerY.setText("Y value");
        accelerometerZ.setText("Z value");

        compassX = (TextView) findViewById(R.id.compassX);
        compassY = (TextView) findViewById(R.id.compassY);
        compassZ = (TextView) findViewById(R.id.compassZ);
        compassX.setText("Azimuth");
        compassY.setText("Pitch");
        compassZ.setText("Roll");

        step_counter = (TextView) findViewById(R.id.step_counter);
        step_counter.setText("0");

        step_counter_reset_button = (Button) findViewById(R.id.step_counter_reset_button);
        step_counter_reset_button.setOnClickListener(MainActivity.this);

        acquisition_button = (Button) findViewById(R.id.acquisition_button);
        acquisition_button.setOnClickListener(MainActivity.this);

        Orientation = new OrientationSensor(this);
        Accelerometer = new AccelerometerSensor(this);
    }

    /**
     * Méthode pour définir l'action des clics sur les bouttons
     *
     * @param view
     */
    public void onClick(View view) {
        if (view.equals(acquisition_button)) {
            if (acquisition_button_started == false) {
                Accelerometer.switchAcquisitionState();
                Accelerometer.resetAccelerometerValueList();
                acquisition_button_started = true;
                acquisition_button.setText("Arrêter l'acquisition");
            } else {
                acquisition_button_started = false;
                AccelerometerValue_l = new ArrayList<AccelerometerValue>();
                AccelerometerValue_l = Accelerometer.getAccelerometerValueList();
                AccelerometerValue_a = AccelerometerValue_l.toArray(new AccelerometerValue[AccelerometerValue_l.size()]);
                acquisition_button.setText("Commencer l'acquisition");

                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(path, "accelerometer_data_flow.txt");
                try {
                    path.mkdirs();
                    OutputStream os = new FileOutputStream(file);
                    for (int i = 0; i < AccelerometerValue_a.length; i++) {
                        os.write((AccelerometerValue_a[i].getFormattedString() + "\n").getBytes());
                    }
                    os.close();
                } catch (IOException e) {
                    Log.w("External Storage", "Error writing" + file, e);
                }
            }
        }
        if(view.equals(step_counter_reset_button)){
            Accelerometer.resetAccelerometerValueList();
        }
    }

    /**
     * Méthode pour setter le texte du textview AccelerometerX
     *
     * @param textview_content
     */
    public void setAccelerometerXLabel(String textview_content) {
        accelerometerX.setText(textview_content);
    }

    /**
     * Méthode pour setter le texte du textview AccelerometerY
     *
     * @param textview_content
     */
    public void setAccelerometerYLabel(String textview_content) {
        accelerometerY.setText(textview_content);
    }

    /**
     * Méthode pour setter le texte du textview AccelerometerZ
     *
     * @param textview_content
     */
    public void setAccelerometerZLabel(String textview_content) {
        accelerometerZ.setText(textview_content);
    }

    /**
     * Méthode pour setter le texte du textview CompassX
     *
     * @param textview_content
     */
    public void setCompassXLabel(String textview_content) {
        compassX.setText(textview_content);
    }

    /**
     * Méthode pour setter le texte du textview CompassY
     *
     * @param textview_content
     */
    public void setCompassYLabel(String textview_content) {
        compassY.setText(textview_content);
    }

    /**
     * Méthode pour setter le texte du textview CompassZ
     *
     * @param textview_content
     */
    public void setCompassZLabel(String textview_content) {
        compassZ.setText(textview_content);
    }

    public void setStepCounterLabel(){
        step_counter.setText(Integer.toString(Accelerometer.getStepCounter()));
    }

    /*public void displaySetStep() {
        int number = Integer.parseInt(step_number.getText().toString());
        number += 1;
        step_number.setText(Integer.toString(number));
    }*/
}

package com.mi12.divita_pfister.geoin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Class that represents the setting activity
 */
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText stepEdit, outdoorToIndoor, indoorToOutdoor, velocityThreshold, recordedValues;
    private Button saveButton;

    /**
     * Method called on the creation of the settings activity
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        stepEdit = (EditText)findViewById(R.id.step_edit);
        outdoorToIndoor = (EditText)findViewById(R.id.outdoor2indoor);
        indoorToOutdoor = (EditText)findViewById(R.id.indoor2outdoor);
        velocityThreshold = (EditText)findViewById(R.id.velocity_threshold);
        recordedValues = (EditText)findViewById(R.id.recorded_values);
        saveButton = (Button)findViewById(R.id.save_setting);

        // print step distance in cm
        stepEdit.setText(Integer.toString((int) (Controller.STEP_DISTANCE * 100)));
        outdoorToIndoor.setText(Float.toString(
                GpsSensor.OUTDOOR_2_INDOOR_THRESHOLD/GpsSensor.MAX_VALUES));
        indoorToOutdoor.setText(Float.toString(
                GpsSensor.INDOOR_2_OUTDOOR_THRESHOLD/GpsSensor.MAX_VALUES));
        velocityThreshold.setText(Float.toString(AccelerometerSensor.VELOCITY_THRESHOLD));
        recordedValues.setText(Integer.toString(GpsSensor.MAX_VALUES));

        saveButton.setOnClickListener(SettingsActivity.this);
    }

    /**
     * Method called when a button on this activity is clicked
     * @param view View
     */
    public void onClick(View view) {
        if (view.equals(saveButton)){
            Controller.STEP_DISTANCE = Float.parseFloat(stepEdit.getText().toString()) /100;
            GpsSensor.MAX_VALUES = Integer.parseInt(recordedValues.getText().toString());
            GpsSensor.OUTDOOR_2_INDOOR_THRESHOLD = Float.parseFloat(
                    outdoorToIndoor.getText().toString()) * GpsSensor.MAX_VALUES;
            GpsSensor.INDOOR_2_OUTDOOR_THRESHOLD = Float.parseFloat(
                    indoorToOutdoor.getText().toString()) * GpsSensor.MAX_VALUES;
            AccelerometerSensor.VELOCITY_THRESHOLD = Float.parseFloat(
                    velocityThreshold.getText().toString());
            this.finish();
        }
    }


}

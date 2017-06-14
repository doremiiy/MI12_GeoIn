package com.mi12.divita_pfister.geoin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText stepEdit, outdoorToIndoor, indoorToOutdoor, velocityThreshold;
    private Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        stepEdit = (EditText)findViewById(R.id.step_edit);
        outdoorToIndoor = (EditText)findViewById(R.id.outdoor2indoor);
        indoorToOutdoor = (EditText)findViewById(R.id.indoor2outdoor);
        velocityThreshold = (EditText)findViewById(R.id.velocity_threshold);
        saveButton = (Button)findViewById(R.id.save_setting);

        stepEdit.setText(Integer.toString((int) (Controller.STEP_DISTANCE * 100))); // print in cm
        outdoorToIndoor.setText(Float.toString(GpsSensor.OUTDOOR_2_INDOOR_THRESHOLD));
        indoorToOutdoor.setText(Float.toString(GpsSensor.INDOOR_2_OUTDOOR_THRESHOLD));
        velocityThreshold.setText(Float.toString(AccelerometerSensor.VELOCITY_THRESHOLD));

        saveButton.setOnClickListener(SettingsActivity.this);
    }
    public void onClick(View view) {
        if (view.equals(saveButton)){
            Controller.STEP_DISTANCE = Float.parseFloat(stepEdit.getText().toString()) /100;

            GpsSensor.OUTDOOR_2_INDOOR_THRESHOLD = Float.parseFloat(outdoorToIndoor.getText().toString());
            GpsSensor.INDOOR_2_OUTDOOR_THRESHOLD = Float.parseFloat(indoorToOutdoor.getText().toString());
            AccelerometerSensor.VELOCITY_THRESHOLD = Float.parseFloat(velocityThreshold.getText().toString());
            this.finish();
        }
    }


}

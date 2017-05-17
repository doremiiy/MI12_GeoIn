package com.mi12.divita_pfister.geoin;


import android.os.Bundle;
import android.app.Activity;

import android.widget.Button;
import android.widget.TextView;
import android.view.View;


public class MainActivity extends Activity implements View.OnClickListener {


    private TextView labelX;
    private TextView labelY;
    private TextView labelZ;
    private TextView step_number;
    private OrientationSensor or;
    private StepSensor st;
    private Button acquisition;

    private boolean acquisition_started = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        labelX = (TextView) findViewById(R.id.labelX);
        labelY = (TextView) findViewById(R.id.labelY);
        labelZ = (TextView) findViewById(R.id.labelZ);
        step_number = (TextView) findViewById(R.id.step_number);

        acquisition = (Button) findViewById(R.id.acquisition);
        acquisition.setOnClickListener(MainActivity.this);

        labelX.setText("X value");
        labelY.setText("Y value");
        labelZ.setText("Z value");

        //or = new OrientationSensor(this);
        st = new StepSensor(this);
    }


    public void onClick(View view) {
        if (view.equals(acquisition)) {

        }
    }
    public void setXYZLabels (String a, String b, String c){
        labelX.setText(a);
        labelY.setText(b);
        labelZ.setText(c);
    }
    public void displaySetStep() {
        int number = Integer.parseInt(step_number.getText().toString());
        number += 1;
        step_number.setText(Integer.toString(number));
    }
}

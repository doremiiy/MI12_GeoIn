package com.mi12.divita_pfister.geoin;


import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;


public class MainActivity extends Activity implements View.OnClickListener {


    private TextView labelX;
    private TextView labelY;
    private TextView labelZ;
    private TextView info;
    private Orientation or;
    private Button acquisition;

    private boolean acquisition_started = false;


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

        or = new Orientation(this);
    }


    public void onClick(View view) {
        if (view.equals(acquisition)) {
            float test[];
            while(true) {
                test = or.getOrientationAngles();
                labelX.setText(Float.toString(test[0] * 180 / (float) Math.PI));
                labelY.setText(Float.toString(test[1] * 180 / (float) Math.PI));
                labelZ.setText(Float.toString(test[2] * 180 / (float) Math.PI));
                SystemClock.sleep(100);
            }
        }
    }
}

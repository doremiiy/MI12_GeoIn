package com.mi12.divita_pfister.geoin;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;



public class MainActivity extends Activity implements View.OnClickListener {

    private TextView lat, lon, acc;
    private Button acquisition_button;


    private GpsSensor gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lat = (TextView) findViewById(R.id.lat);
        lon = (TextView) findViewById(R.id.lon);
        acc = (TextView) findViewById(R.id.acc);

        acquisition_button = (Button) findViewById(R.id.mbutton);
        acquisition_button.setOnClickListener(MainActivity.this);
        //gps = new GpsSensor(this);

        // Testing maps Activity
        // TODO: check is GPS is ready, then start the map activity
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void onClick(View view) {
        if(view.equals(acquisition_button)){
            if (gps.isReady()) {
                GpsValue current = gps.getLastPosition();
                lat.setText(Double.toString(current.latitude));
                lon.setText(Double.toString(current.longitude));
                acc.setText(Double.toString(current.accuracy));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults ) {
        switch (requestCode){
            case 10 :
                if (grantResults.length > 0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    gps.configure();
                return;
        }
    }
}

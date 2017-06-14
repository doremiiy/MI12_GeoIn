package com.mi12.divita_pfister.geoin;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Controller controller;

    private GoogleMap mMap;
    private Marker markerPosition;

    private TextView appMode;
    private Button stepCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        controller = new Controller(this);

        stepCounter = (Button) findViewById(R.id.step_counter);
        appMode = (TextView)findViewById(R.id.app_mode);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng entreePierreGuillaumat1 = new LatLng(49.400171, 2.7999994);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(entreePierreGuillaumat1)
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.walker))
                .visible(false);
        markerPosition = mMap.addMarker(markerOptions);

        CameraPosition camPos = new CameraPosition.Builder().target(entreePierreGuillaumat1)
                .zoom(20)
                .bearing(0)
                .tilt(0)
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(camUpd3);
    }

    /**
     * @return the position of the user printed on the map.
     */
    public LatLng getCurrentPosition(){
        return markerPosition.getPosition();
    }

    /**
     * Print the user's position on the map.
     * @param position
     */
    public void setUserPosition (Position position) {
        LatLng myLaLn = new LatLng(position.latitude, position.longitude);

        CameraPosition current = mMap.getCameraPosition();
        CameraPosition camPos = new CameraPosition.Builder()
                .target(myLaLn)
                .zoom(current.zoom)
                .bearing(current.bearing)
                .tilt(current.tilt)
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(camUpd3);

        if(markerPosition.isVisible() == false) markerPosition.setVisible(true);
        markerPosition.setPosition(myLaLn);
    }


    public void setStepCounterLabel(int step_counter) {
        stepCounter.setText(Integer.toString(step_counter));
    }
    public void printMode(boolean mode, float gps_accuracy) {
        appMode.setText(((mode ? "Indoor" : "Outdoor") + " (" +Float.toString(gps_accuracy)+")" ));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults ) {
        switch (requestCode){
            case 10 :
                if (grantResults.length > 0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    controller.gps.configure();
                return;
        }
    }
    public void startSettingActivity(View view){
        Intent intent = new Intent(MapsActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
    public void resetStepCounter(View view){
        controller.setStepCounter(0);
        this.setStepCounterLabel(controller.getStepCounter());
    }
}

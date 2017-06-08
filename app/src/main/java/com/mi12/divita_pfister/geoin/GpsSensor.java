package com.mi12.divita_pfister.geoin;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;


public class GpsSensor {

    public int SENSOR_DELAY = 250;
    private int MAX_VALUES = 10;
    public float INDOOR_2_OUTDOOR_THRESHOLD = 6f * MAX_VALUES;
    public float OUTDOOR_2_INDOOR_THRESHOLD = 8f * MAX_VALUES;

    private Controller controller;
    private LocationListener locationListener;
    private LocationManager locationManager;

    private GpsValue lastPosition;
    private boolean isReady;
    private int pointer = 0;
    private boolean isIndoorMode = false;
    private float lastAccuracies[] = new float[MAX_VALUES];

    public GpsSensor (final Controller controller) {
        this.controller = controller;
        locationManager = (LocationManager) controller.display.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                lastPosition = new GpsValue(
                        new double[]{location.getLatitude(), location.getLongitude()},
                        location.getAccuracy(), location.getTime()
                );
                if (!isReady) {
                    controller.onGpsReady(lastPosition);
                    isReady = true;
                }
                recordNewGpsValue(lastPosition.getAccuracy());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {
                isReady = false;
                //go to setting panel if GPS is not active
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                controller.display.startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    controller.display, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                            controller.display, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                controller.display.requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 10);
                return;
            }
        }
        configure();
    }


    /**
     * Configure GpsSensor to get new value Twice per seconde if the position has moved by 20cm.
     */
    public void configure () {
        locationManager.requestLocationUpdates("gps", SENSOR_DELAY, 0, locationListener);
    }

    /**
     * Give the last know Gps Posistion.
     * @return GpsValue
     */
    public GpsValue getLastPosition () {
        return lastPosition;
    }

    /**
     * Give th state of the GpsSensor. It's is not ready if there is no value no last value to
     * return.
     * @return boolean
     */
    public boolean isReady (){
        return this.isReady;
    }

    public boolean getIndoorMode() { return isIndoorMode; }

    public void recordNewGpsValue(float accuracy) {
        lastAccuracies[pointer] = accuracy;
        if(VectorMath.vectorSum(lastAccuracies) >= OUTDOOR_2_INDOOR_THRESHOLD && this.isIndoorMode == false){
            this.isIndoorMode = true;
        }
        if(VectorMath.vectorSum(lastAccuracies) < INDOOR_2_OUTDOOR_THRESHOLD && this.isIndoorMode == true){
            this.isIndoorMode = false;
        }
        pointer = (pointer + 1) % MAX_VALUES;
        controller.display.printMode(isIndoorMode);
        controller.display.printGpsAccuracy(accuracy);
    }
}

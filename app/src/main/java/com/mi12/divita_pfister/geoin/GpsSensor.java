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


/**
 * Class that define the GpsSensor
 */
public class GpsSensor {

    public int SENSOR_DELAY = 0;
    public static int MAX_VALUES = 5;
    public static float INDOOR_2_OUTDOOR_THRESHOLD = 5f * MAX_VALUES;
    public static float OUTDOOR_2_INDOOR_THRESHOLD = 5f * MAX_VALUES;

    private Controller controller;
    private LocationListener locationListener;
    private LocationManager locationManager;

    private GpsValue lastPosition;
    private boolean isReady;
    private int pointer = 0;
    private boolean isIndoorMode = false;
    private float lastAccuracies[] = new float[MAX_VALUES];

    /**
     * Constructor
     * @param controller is the controller that instantiate this sensor
     */
    public GpsSensor (final Controller controller) {
        this.controller = controller;
        locationManager = (LocationManager) controller
                .display
                .getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            /**
             * Called when a new location value is return by the sensor
             * @param location associated that contains the coordinates and the timestamp
             *                 and a accuracy
             */
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

    /**
     * Give the mode the application must use
     * @return the current mode.
     */
    public boolean getIndoorMode() {
        return isIndoorMode;
    }

    /**
     * register a  new value in the accuracies array, calculate the mean and change the mode
     * if needed
     * @param accuracy is the accuracy of the Value obtain by the GpsSensor
     */
    public void recordNewGpsValue(float accuracy) {
        lastAccuracies[pointer] = accuracy;
        if(
                //VectorMath.vectorSum(lastAccuracies) >= OUTDOOR_2_INDOOR_THRESHOLD
                VectorMath.vectorSum(new float[]{lastAccuracies[(MAX_VALUES + pointer-1)%MAX_VALUES], lastAccuracies[pointer]}) >= OUTDOOR_2_INDOOR_THRESHOLD * 2f/ (float)MAX_VALUES
                && !this.isIndoorMode
        ){
            this.isIndoorMode = true;
        }
        if(VectorMath.vectorSum(lastAccuracies) < INDOOR_2_OUTDOOR_THRESHOLD && this.isIndoorMode){
            this.isIndoorMode = false;
        }
        pointer = (pointer + 1) % MAX_VALUES;
        // near a entrance
        StepPosition entreePierreGuillaumat1 = new StepPosition(new double[]{49.400223, 2.800120}, 0);
        if(Position.calculateDistance(entreePierreGuillaumat1, lastPosition) < 3){
            isIndoorMode = true;
        }

        controller.display.printMode(isIndoorMode, accuracy);
    }
}

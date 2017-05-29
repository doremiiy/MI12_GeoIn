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
import android.util.Log;

//TODO: add timestamps info and return a lastvalue only if it's not too old
public class GpsSensor {

    private MainActivity display;
    private LocationListener mlocationListener;
    private LocationManager mlocationManager;

    private GpsValue lastPosition;
    private boolean isReady;

    public GpsSensor(final MainActivity display) {
        this.display = display;
        mlocationManager = (LocationManager) display.getSystemService(Context.LOCATION_SERVICE);
        mlocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                lastPosition = new GpsValue(
                        new double[]{location.getLongitude(), location.getLatitude()},
                        location.getAccuracy()
                );
                isReady = true;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
                isReady = false;
                //go to setting panel if GPS is not active
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                display.startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(display, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(display, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                display.requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 10);
                return;
            }
        }
        configure();
    }


    public void configure() {
        mlocationManager.requestLocationUpdates("gps", 500, 0.2f, mlocationListener);
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
     *
     * @return boolean
     */
    public boolean isReady(){
        return this.isReady;
    }
}

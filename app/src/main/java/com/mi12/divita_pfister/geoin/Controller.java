package com.mi12.divita_pfister.geoin;


import com.google.android.gms.maps.model.LatLng;

public class Controller {
    private MapsActivity display;
    private GpsSensor gps;
    private AccelerometerSensor accelerometer;
    private OrientationSensor orientation;

    public HistoryValue[] history = new HistoryValue[1000];

    /**
     * Constructor
     * @param display
     */
    public Controller(MapsActivity display){
        this.display = display;
        this.gps = new GpsSensor(this.display);
        this.accelerometer = new AccelerometerSensor(this.display);
        this.orientation = new OrientationSensor(this.display);
    }

    /**
     * @param distance
     * @param bearing
     * @return new position of the user in coordinate
     */
    public LatLng addStepDistance(float distance, float bearing) {
        LatLng oldPosition = display.getCurrentPosition();
        double lat1 = Math.toRadians(oldPosition.latitude);
        double lon1 = Math.toRadians(oldPosition.longitude);
        double earthRadius = 6371;
        double angularDistance = distance/earthRadius;
        double bearingRad = Math.toRadians(bearing);

        double lat2 = Math.asin(
                Math.sin(lat1) * Math.cos(angularDistance) +
                Math.cos(lat1) * Math.sin(angularDistance) * Math.cos(bearingRad)
        );
        double lon2 = lon1 + Math.atan2(
                Math.sin(bearingRad) * Math.sin(angularDistance)*Math.cos(lat1),
                Math.cos(angularDistance)-Math.sin(lat1)*Math.sin(lat2)
        );
       return new LatLng(Math.toDegrees(lat2), Math.toDegrees(lon2));
    }


    /**
     * Action when we detect a step
     */
    public void onStepDetected(){

    }
}

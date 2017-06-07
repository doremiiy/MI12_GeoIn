package com.mi12.divita_pfister.geoin;


import com.google.android.gms.maps.model.LatLng;

/**
 *
 */
public class Controller {
    public MapsActivity display;
    public GpsSensor gps;
    private AccelerometerSensor accelerometer;
    private OrientationSensor orientation;

    public float STEP_DISTANCE = 0.75f;

    public int MAX_HISTORY = 100;
    private int historyPointer =0;
    private HistoryValue[] history = new HistoryValue[MAX_HISTORY];
    private int stepCounter = 0;

    /**
     * Constructor
     * @param display
     */
    public Controller(MapsActivity display){
        this.display = display;
        this.gps = new GpsSensor(this);
        this.accelerometer = new AccelerometerSensor(this);
        this.orientation = new OrientationSensor(this.display);
    }

    private boolean isIndoorMode() {
        return false;
    }

    /**
     * Action when we detect a step
     * @param timestamp
     */
    public void onStepDetected(long timestamp){
        stepCounter++;
        display.setLabel1(stepCounter);

        if(gps.isReady()) {
            GpsValue gpsValue = gps.getLastPosition();
            display.setUserPosition(gpsValue);
            display.setLabel2(gpsValue.getAccuracy());

            StepPosition temp;
            if (history[(historyPointer - 1) % MAX_HISTORY] == null){
                temp = new StepPosition(
                        new double[]{gpsValue.getLatitude(), gpsValue.getLongitude()},
                        gpsValue.getDatetime()
                );
            } else {
                // TODO: Check if roll and pitch are good enough
                temp = Position.addStepDistance(
                        history[(historyPointer - 1) % MAX_HISTORY].stepPosition,
                        STEP_DISTANCE,
                        orientation.getOrientationAngles().getAzimuth(),
                        timestamp
                );
            }
            history[historyPointer % MAX_HISTORY] = new HistoryValue(
                    temp,
                    gpsValue,
                    isIndoorMode()
            );
            historyPointer++;
        }
    }

    public void gpsIsReady(){
        display.setLabel3(true);
    }

    /**
     * Retourne le nombre de pas depuis la dernière remise à 0
     * @return int
     */
    public int getStepCounter() {
        return this.stepCounter;
    }

    /**
     * Fonction appelée lorsqu'un pas est detecté. On récupère également le timestamp pour traitemements postérieurs
     */
}

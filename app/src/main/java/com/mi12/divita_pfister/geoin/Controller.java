package com.mi12.divita_pfister.geoin;


import android.util.Log;

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
    public  int RESET_INTERVAL = 10;

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
        this.orientation = new OrientationSensor(this.display);;
    }

    private boolean recordHistoryValue (boolean reset, boolean isIndoorMode, GpsValue gpsValue, long timestamp) {
        if (history[(historyPointer - 1 + MAX_HISTORY) % MAX_HISTORY] == null) {
            reset = true;
            if(isIndoorMode) {
                return false;
            }
        }
        StepPosition stepPosition;
        if(reset && !isIndoorMode) {
            stepPosition = new StepPosition(
                    new double[]{gpsValue.getLatitude(), gpsValue.getLongitude()},
                    gpsValue.getDatetime()
            );
        } else {
            stepPosition = Position.addStepDistance(
                    history[(historyPointer - 1 + MAX_HISTORY) % MAX_HISTORY].stepPosition,
                    STEP_DISTANCE,
                    orientation.getOrientationAngles().getAzimuth(),
                    timestamp
            );
        }
        history[historyPointer] = new HistoryValue(
                stepPosition,
                gpsValue,
                isIndoorMode
        );
        return true;
    }

    /**
     * Action when we detect a step
     * @param timestamp
     */
    public void onStepDetected(long timestamp){
        if(gps.isReady()) {
            GpsValue gpsValue = gps.getLastPosition();
            StepPosition stepPosition;
            boolean isIndoorMode = gps.getIndoorMode();
            boolean reset = false;
            if (
                    history[(historyPointer - 1 + MAX_HISTORY) % MAX_HISTORY] != null &&
                    history[(historyPointer - 1 + MAX_HISTORY) % MAX_HISTORY].isIndoorMode &&
                            isIndoorMode
                    ){
                reset = true;
            }
            if(!isIndoorMode && historyPointer % RESET_INTERVAL == 0){
                reset = true;
            }
            if(recordHistoryValue(reset, isIndoorMode, gpsValue, timestamp)) {
                if (isIndoorMode) {
                    display.setUserPosition(history[historyPointer].gpsPosition);
                } else {
                    display.setUserPosition(history[historyPointer].stepPosition);
                }
                historyPointer = (historyPointer + 1) % MAX_HISTORY;
            }
            // Debug
            stepCounter++;
            display.setLabel1(stepCounter);
        }
    }

    public void onGpsReady(GpsValue firstPosition){
        display.setUserPosition(firstPosition);
    }
}

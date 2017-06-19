package com.mi12.divita_pfister.geoin;

/**
 * Class to save the values of recorded position with gps and step distance/orientation calculation
 */
public class HistoryValue {
    public StepPosition stepPosition;
    public GpsValue gpsPosition;
    public boolean isIndoorMode;

    /**
     * Constructor
     * @param stepPosition either gps value or calculated
     * @param gpsPosition recorded with LocationManager
     * @param isIndoorMode boolean to say if the mode at the time we recorded each value was indoor or outdoor
     */
    public HistoryValue (StepPosition stepPosition, GpsValue gpsPosition, boolean isIndoorMode){
        this.stepPosition = stepPosition;
        this.gpsPosition = gpsPosition;
        this.isIndoorMode = isIndoorMode;
    }
}

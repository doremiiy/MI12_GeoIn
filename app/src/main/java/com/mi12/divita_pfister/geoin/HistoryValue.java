package com.mi12.divita_pfister.geoin;


public class HistoryValue {
    public StepPosition stepPosition;
    public GpsValue gpsPosition;
    public boolean isIndoorMode;

    public HistoryValue (StepPosition stepPosition, GpsValue gpsPosition, boolean isIndoorMode){
        this.stepPosition = stepPosition;
        this.gpsPosition = gpsPosition;
        this.isIndoorMode = isIndoorMode;
    }
}

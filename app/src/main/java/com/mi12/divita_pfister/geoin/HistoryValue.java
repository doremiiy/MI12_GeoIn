package com.mi12.divita_pfister.geoin;


public class HistoryValue {
    public OrientationValue orientation;
    public GpsValue lastGpsPosition;
    public long stepTimestamps;

    public HistoryValue (OrientationValue orientation, GpsValue gpsValue, long stepTimestamps){
        this.orientation = orientation;
        this.lastGpsPosition = gpsValue;
        this.stepTimestamps = stepTimestamps;
    }
}

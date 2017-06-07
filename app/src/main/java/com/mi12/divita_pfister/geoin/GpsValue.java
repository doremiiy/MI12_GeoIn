package com.mi12.divita_pfister.geoin;


public class GpsValue extends Position {
    private float accuracy;

    public GpsValue (double [] values, float accuracy, long datetime) {
        this.latitude = values[0];
        this.longitude = values[1];
        this.accuracy = accuracy;
        this.datetime = datetime;
    }

    public float getAccuracy() {
        return accuracy;
    }
}

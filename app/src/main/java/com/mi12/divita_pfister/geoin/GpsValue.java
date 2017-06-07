package com.mi12.divita_pfister.geoin;


public class GpsValue {
    public double latitude;
    public double longitude;
    public float accuracy;
    public long datetime;

    public GpsValue (double [] values, float accuracy, long datetime) {
        this.latitude = values[0];
        this.longitude = values[1];
        this.accuracy = accuracy;
        this.datetime = datetime;
    }
}

package com.mi12.divita_pfister.geoin;


public class GpsValue {
    public double latitude;
    public double longitude;
    public float accuracy;

    public GpsValue (double [] values, float accuracy) {
        this.latitude = values[0];
        this.longitude = values[1];
        this.accuracy = accuracy;
    }
}

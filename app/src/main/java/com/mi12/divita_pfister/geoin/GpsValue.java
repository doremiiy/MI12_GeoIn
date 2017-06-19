package com.mi12.divita_pfister.geoin;

/**
 * Class that represens a position obtained with the gps
 */
public class GpsValue extends Position {
    private float accuracy;

    /**
     * Constructor
     * @param values array of values containing latitude and longitude
     * @param accuracy float, accuracy of the gps value received
     * @param datetime timestamp associated
     */
    public GpsValue (double [] values, float accuracy, long datetime) {
        this.latitude = values[0];
        this.longitude = values[1];
        this.accuracy = accuracy;
        this.datetime = datetime;
    }

    /**
     * Accessor
     * @return accuracy
     */
    public float getAccuracy() {
        return accuracy;
    }
}

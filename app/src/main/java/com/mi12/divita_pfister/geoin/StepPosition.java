package com.mi12.divita_pfister.geoin;

/**
 * Class that represents a position calculated when a step is detected
 */
public class StepPosition extends Position {

    /**
     * Constructor
     * @param values array containing latitude and longitude
     * @param datetime timestamp associated
     */
    public StepPosition(double[] values, long datetime) {
        this.latitude = values[0];
        this.longitude = values[1];
        this.datetime = datetime;
    }
}

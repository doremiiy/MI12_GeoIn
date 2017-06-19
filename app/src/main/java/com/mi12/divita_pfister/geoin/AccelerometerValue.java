package com.mi12.divita_pfister.geoin;

/**
 * Class that represents a value of the accelerometer sensor
 */
public class AccelerometerValue {
    private float X;
    private float Y;
    private float Z;

    /**
     * Constructor
     * @param X acceleration on the phone x axis
     * @param Y acceleration on the phone y axis
     * @param Z acceleration on the phone z axis
     */
    public AccelerometerValue(float X, float Y, float Z) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }

    /**
     * Accessor
     * @return acceleration on the phone x axis
     */
    public float getXvalue() {
        return this.X;
    }

    /**
     * Accessor
     * @return acceleration on the phone y axis
     */
    public float getYvalue() {
        return this.Y;
    }

    /**
     * Accessor
     * @return acceleration on the phone z axis
     */
    public float getZvalue() {
        return this.Z;
    }
}

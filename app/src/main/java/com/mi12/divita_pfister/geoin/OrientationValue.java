package com.mi12.divita_pfister.geoin;

/**
 * Class that represents a value of the orientation sensor
 */
public class OrientationValue {
    private float azimuth;
    private float pitch;
    private float roll;

    /**
     * Constructor
     * @param value
     */
    public OrientationValue (float[] value){
        this.azimuth = value[0];
        this.pitch = value[1];
        this.roll = value[2];
    }

    /**
     * Accessor
     * @return azimuth
     */
    public float getAzimuth(){
        return this.azimuth;
    }

    /**
     * Accessor
     * @return pitch
     */
    public float getPitch(){
        return this.pitch;
    }

    /**
     * Accessor
     * @return roll
     */
    public float getRoll(){
        return this.roll;
    }
}

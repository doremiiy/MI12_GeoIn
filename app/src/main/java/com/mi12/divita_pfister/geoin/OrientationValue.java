package com.mi12.divita_pfister.geoin;


public class OrientationValue {
    public float azimuth;
    public float pitch;
    public float roll;

    public OrientationValue (float valueA, float valueB, float valueC){
        this.azimuth = valueA;
        this.pitch = valueB;
        this.roll = valueC;
    }
    public OrientationValue (float[] value){
        this.azimuth = value[0];
        this.pitch = value[1];
        this.roll = value[2];
    }
}

package com.mi12.divita_pfister.geoin;


public class OrientationValue {
    private float azimuth;
    private float pitch;
    private float roll;

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

    public float getAzimuth(){
        return this.azimuth;
    }

    public float getPitch(){
        return this.pitch;
    }

    public float getRoll(){
        return this.roll;
    }
}

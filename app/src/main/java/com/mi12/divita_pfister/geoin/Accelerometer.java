package com.mi12.divita_pfister.geoin;


public class Accelerometer {
    private float x;
    private float y;
    private float z;

    public Accelerometer(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString(){
        return Float.toString(this.x) + " " + Float.toString(this.y) + " " + Float.toString(this.z);
    }
}
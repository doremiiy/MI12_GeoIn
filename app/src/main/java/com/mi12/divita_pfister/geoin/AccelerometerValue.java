package com.mi12.divita_pfister.geoin;

public class AccelerometerValue {
    private float X;
    private float Y;
    private float Z;

    public AccelerometerValue(float X, float Y, float Z) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }

    public float getXvalue() {
        return this.X;
    }

    public float getYvalue() {
        return this.Y;
    }

    public float getZvalue() {
        return this.Z;
    }

    public void setValues(float X, float Y, float Z) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }

    public String getFormattedString() {
        return Float.toString(this.X) + " " + Float.toString(this.Y) + " " + Float.toString(this.Z);
    }

    public double getNorm() {
        return Math.sqrt(Math.pow(this.getXvalue(), 2) + Math.pow(this.getYvalue(), 2) + Math.pow(this.getZvalue(), 2));
    }
}

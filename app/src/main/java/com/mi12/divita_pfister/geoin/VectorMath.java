package com.mi12.divita_pfister.geoin;

public class VectorMath {

    public VectorMath(){}

    public float normalizeVector(float[] vector) {
        float result = 0;
        for (int i = 0; i < vector.length; i++) {
            result += vector[i] * vector[i];
        }
        return (float) Math.sqrt(result);
    }

    public float vectorSum(float[] vector) {
        float result = 0;
        for (int i = 0; i < vector.length; i++) {
            result += vector[i];
        }
        return result;
    }

    public float dotMult(float[] vector1, float[] vector2) {
        float result = 0;
        for (int i = 0; i < vector1.length; i++) {
            result += vector1[i] * vector2[i];
        }
        return result;
    }
}

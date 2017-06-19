package com.mi12.divita_pfister.geoin;

/**
 * Abstract class used to make optimized operations on vectors
 */
public abstract class VectorMath {

    /**
     * Methods that calculates the norm of a vector
     * @param vector array (n*1)
     * @return float
     */
    public static float normalizeVector(float[] vector) {
        float result = 0;
        for (int i = 0; i < vector.length; i++) {
            result += vector[i] * vector[i];
        }
        return (float) Math.sqrt(result);
    }

    /**
     * Methods that sums the components of a vector
     * @param vector array (n*1)
     * @return float
     */
    public static float vectorSum(float[] vector) {
        float result = 0;
        for (int i = 0; i < vector.length; i++) {
            result += vector[i];
        }
        return result;
    }

    /**
     * Methods that makes the cross product between two vectors
     * @param vector1 array (n*1)
     * @param vector2 array (n*1)
     * @return float
     */
    public static float dotMult(float[] vector1, float[] vector2) {
        float result = 0;
        for (int i = 0; i < vector1.length; i++) {
            result += vector1[i] * vector2[i];
        }
        return result;
    }
}

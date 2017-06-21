package com.mi12.divita_pfister.geoin;

/**
 * Abstract class that represents a user position
 */
public abstract class Position {
    protected static double EARTH_RADIUS = 6371000;
    protected double latitude;
    protected double longitude;
    protected long datetime;

    /**
     * Accessor
     * @return datetime
     */
    public long getDatetime() {
        return datetime;
    }

    /**
     * Accessor
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Setter
     * @param longitude longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Accessor
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Setter
     * @param latitude latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * adds a distance to a position knowing the orientation. Returns the new calculated position
     * @param distance of one step
     * @param bearing (in radian)
     * @return new position of the user in coordinate
     */
    public static StepPosition addStepDistance(
            Position previousPosition, float distance, float bearing, long timestamp
    ) {
        double lat1 = Math.toRadians(previousPosition.getLatitude());
        double lon1 = Math.toRadians(previousPosition.getLongitude());
        double angularDistance = distance/EARTH_RADIUS;

        double lat2 = Math.asin(
                Math.sin(lat1) * Math.cos(angularDistance) +
                        Math.cos(lat1) * Math.sin(angularDistance) * Math.cos(bearing)
        );
        double lon2 = lon1 + Math.atan2(
                Math.sin(bearing) * Math.sin(angularDistance)*Math.cos(lat1),
                Math.cos(angularDistance)-Math.sin(lat1)*Math.sin(lat2)
        );
        return new StepPosition(
                new double[]{Math.toDegrees(lat2), Math.toDegrees(lon2)}, timestamp
        );
    }

    /**
     * @param a is a first Position
     * @param b is a second Position
     * @return the distance between Position a and b
     */
    public static double calculateDistance(Position a, Position b){
        double lata = Math.toRadians(a.getLatitude());
        double lona = Math.toRadians(a.getLongitude());
        double latb = Math.toRadians(b.getLatitude());
        double lonb = Math.toRadians(b.getLongitude());


        return EARTH_RADIUS * (Math.PI/2 - Math.asin(Math.sin(latb) * Math.sin(lata) + Math.cos(lonb - lona) * Math.cos(latb) * Math.cos(lata)));
    }
}

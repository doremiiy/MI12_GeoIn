package com.mi12.divita_pfister.geoin;


public abstract class Position {
    protected double latitude;
    protected double longitude;
    protected long datetime;

    public long getDatetime() {
        return datetime;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @param distance
     * @param bearing (in radian)
     * @return new position of the user in coordinate
     */
    public static StepPosition addStepDistance(Position previousPosition, float distance, float bearing, long timestamp) {
        double lat1 = Math.toRadians(previousPosition.getLatitude());
        double lon1 = Math.toRadians(previousPosition.getLongitude());
        double earthRadius = 6371000;
        double angularDistance = distance/earthRadius;

        double lat2 = Math.asin(
                Math.sin(lat1) * Math.cos(angularDistance) +
                        Math.cos(lat1) * Math.sin(angularDistance) * Math.cos(bearing)
        );
        double lon2 = lon1 + Math.atan2(
                Math.sin(bearing) * Math.sin(angularDistance)*Math.cos(lat1),
                Math.cos(angularDistance)-Math.sin(lat1)*Math.sin(lat2)
        );
        return new StepPosition(new double[]{Math.toDegrees(lat2), Math.toDegrees(lon2)}, timestamp);
    }
}

package com.mi12.divita_pfister.geoin;

import com.google.android.gms.maps.model.LatLng;

public class StepPosition extends Position {

    public StepPosition(double[] values, long datetime) {
        this.latitude = values[0];
        this.longitude = values[1];
        this.datetime = datetime;
    }
}

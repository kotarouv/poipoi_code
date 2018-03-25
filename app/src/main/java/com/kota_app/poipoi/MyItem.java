package com.kota_app.poipoi;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by kota327 on 3/7/2018.
 */

public class MyItem implements ClusterItem {
    private final LatLng mPosition;

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}

package four.easytravel;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Nick on 2018-05-08.
 */

public class NearbyPlace {

    String name;
    LatLng latLng;

    NearbyPlace(String name, LatLng latLng){

        this.name = name;
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

}

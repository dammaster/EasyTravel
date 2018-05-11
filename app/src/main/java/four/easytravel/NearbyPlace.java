package four.easytravel;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Nick on 2018-05-08.
 */

public class NearbyPlace {

    String name;
    LatLng latLng;
    String placeId;
    String address;

    NearbyPlace(String name, LatLng latLng, String placeId, String address){

        this.name = name;
        this.latLng = latLng;
        this.placeId = placeId;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getAddress() {
        return address;
    }

}

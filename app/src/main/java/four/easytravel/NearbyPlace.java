package four.easytravel;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Nick on 2018-05-08.
 */

public class NearbyPlace {

    String name;
    LatLng latLng;
    String placeId;
    String address;
    String photoRef;

    Bitmap image;


    NearbyPlace(String name, LatLng latLng, String placeId, String address, String photoRef){

        this.name = name;
        this.latLng = latLng;
        this.placeId = placeId;
        this.address = address;
        this.photoRef = photoRef;
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

    public String getPhotoRef() {
        return photoRef;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}

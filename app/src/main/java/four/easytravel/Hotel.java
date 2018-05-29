package four.easytravel;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Hotel extends AppCompatActivity implements View.OnClickListener {

    private String TAG = Result.class.getSimpleName();
    String st;
    public static double lat;
    public static double lng;
    EditText checkIn,checkOut;
    String checkInString, checkOutString;
    private int mYear, mMonth, mDay;
    String zero;
    LatLng latLng;
    //EditText location_tf;
    String location;
    Button b1,b2,b3;
    String currentDate;
    PlaceAutocompleteFragment autocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        checkIn=(EditText)findViewById(R.id.checkIn);
        checkOut=(EditText)findViewById(R.id.checkOut);

        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2);
        b3 = (Button) findViewById(R.id.b3);

        location = "";



        checkIn.setOnClickListener(this);
        checkOut.setOnClickListener(this);

        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName());
                location = place.getName().toString();
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });



        // currentDate dafault
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // currentDate = sdf.format(new Date());
        // Toast.makeText(Hotel.this, currentDate, Toast.LENGTH_LONG).show();


    }

    @Override
    public void onClick(View view) {

        if (view == checkIn) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            if (monthOfYear <10){ zero = "0";}else{zero = "";}
                            checkIn.setText(year + "-" + zero +(monthOfYear + 1) + "-" + dayOfMonth);



                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }if(view == checkOut) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {




                            if (monthOfYear <10){ zero = "0";}else{zero = "";}
                            checkOut.setText(year + "-" + zero +(monthOfYear + 1) + "-" + dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

    }

    public void onSearch(View view) {


        //location_tf = (EditText) findViewById(R.id.TFadreess);
        //location = location_tf.getText().toString();
        List<Address> addressList;
        if (location != null || !location.equals("")) {


            Geocoder geocoder = new Geocoder(this);


            try {
                addressList = geocoder.getFromLocationName(location, 1);
                Address address = addressList.get(0);

                lat = address.getLatitude();
                lng = address.getLongitude();

                //  latLng = new LatLng(address.getLatitude(), address.getLongitude());


                checkInString = checkIn.getText().toString();
                checkOutString = checkOut.getText().toString();

                if (checkInString.isEmpty()) {
                    Toast.makeText(this, "Check-in date is empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (checkOutString.isEmpty()) {
                    Toast.makeText(this, "Check-Out date is empty", Toast.LENGTH_LONG).show();
                    return;
                }


                Intent intent = new Intent(this, Result.class);

                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("location", location);
                intent.putExtra("checkIn", checkInString);
                intent.putExtra("checkOut", checkOutString);
                startActivity(intent);


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Sorry, city not found, try again!", Toast.LENGTH_LONG).show();


            }

        }

    }


    public void singapore(View view){
        Intent intent = new Intent(this, GooglePlace.class);
        intent.putExtra("cityLocate", "Singapore");
        intent.putExtra("latLng", new LatLng(1.290270, 103.851959));
        startActivity(intent);

    }
    public void santamonica(View view){
        Intent intent = new Intent(this, GooglePlace.class);
        intent.putExtra("cityLocate", "Santa Monica");
        intent.putExtra("latLng", new LatLng(34.024212, -118.496475));
        startActivity(intent);

    }
    public void lasvegas(View view){
        Intent intent = new Intent(this, GooglePlace.class);
        intent.putExtra("cityLocate", "Las Vegas");
        intent.putExtra("latLng", new LatLng(36.114647, -115.172813));
        startActivity(intent);

    }

    public void translate(View view){
        Intent intent = new Intent(this, GoogleMain.class);
        startActivity(intent);

    }



}


package four.easytravel;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
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
    EditText location_tf;
    String location;
    Button b1,b2,b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        checkIn=(EditText)findViewById(R.id.checkIn);
        checkOut=(EditText)findViewById(R.id.checkOut);

        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2);
        b3 = (Button) findViewById(R.id.b3);



        checkIn.setOnClickListener(this);
        checkOut.setOnClickListener(this);








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



        location_tf = (EditText) findViewById(R.id.TFadreess);
        location = location_tf.getText().toString();
        List<Address> addressList;
        if (location != null || !location.equals("")) ;
        {


            Geocoder geocoder = new Geocoder(this);


            try {
                addressList = geocoder.getFromLocationName(location, 1);
                Address address = addressList.get(0);

                lat = address.getLatitude();
                lng = address.getLongitude();

              //  latLng = new LatLng(address.getLatitude(), address.getLongitude());


                checkInString = checkIn.getText().toString();
                checkOutString = checkOut.getText().toString();


                Intent intent = new Intent(this, Result.class);

                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("location", location);
                intent.putExtra("checkIn", checkInString);
                intent.putExtra("checkOut", checkOutString);
                startActivity(intent);





            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Hotel.this, "Sorry, city not found, try again!", Toast.LENGTH_LONG).show();


            }



        }








        }


    public void singapore(View view){
        Intent intent = new Intent(this, GooglePlace.class);
        intent.putExtra("cityLocate", "Singapore");
        startActivity(intent);

    }
    public void santamonica(View view){
        Intent intent = new Intent(this, GooglePlace.class);
        intent.putExtra("cityLocate", "Santa Monica");
        startActivity(intent);

    }
    public void lasvegas(View view){
        Intent intent = new Intent(this, GooglePlace.class);
        intent.putExtra("cityLocate", "Las Vegas");
        startActivity(intent);

    }



    }


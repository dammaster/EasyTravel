package four.easytravel;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        checkIn=(EditText)findViewById(R.id.checkIn);
        checkOut=(EditText)findViewById(R.id.chechOut);





        checkIn.setOnClickListener(this);
        checkOut.setOnClickListener(this);






        EditText location_tf = (EditText) findViewById(R.id.TFadreess);
        String location = location_tf.getText().toString();
        List<Address> addressList;
        if (location != null || !location.equals("")) ;
        {


            Geocoder geocoder = new Geocoder(this);




            try {
                addressList = geocoder.getFromLocationName(location, 1);
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());



                 checkInString = checkIn.getText().toString();
                 checkOutString = checkOut.getText().toString();






            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Hotel.this, "Write a city", Toast.LENGTH_SHORT).show();
            }





        }





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




                Intent intent = new Intent(this, Result.class);

              //  intent.putExtra("to", to_String);
                intent.putExtra("checkIn", checkInString);
                intent.putExtra("checkOut", checkOutString);
                startActivity(intent);




        }

    }


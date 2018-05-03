package four.easytravel;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText checkIn,checkOut, from, to;
    private int mYear, mMonth, mDay;
    String zero;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkIn=(EditText)findViewById(R.id.checkIn);
        checkOut=(EditText)findViewById(R.id.chechOut);
        from= (EditText) findViewById(R.id.from);
        to= (EditText) findViewById(R.id.to);

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



    public void send (View view){

        String fromString = from.getText().toString();
        String to_String = to.getText().toString();
        String checkInString = checkIn.getText().toString();
        String checkOutString = checkOut.getText().toString();



        Intent intent = new Intent(this, Result.class);
        intent.putExtra("from", fromString);
        intent.putExtra("to", to_String);
        intent.putExtra("checkIn", checkInString);
        intent.putExtra("checkOut", checkOutString);
        startActivity(intent);




    }


}

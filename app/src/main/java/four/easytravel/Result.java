package four.easytravel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Result extends AppCompatActivity {

    private String TAG = Result.class.getSimpleName();
    private ListView lv;
    private String checkIn,checkOut,location;
    public static double lat;
    public static double lng;
    Button buttonSearch;
    TextView dateCheckInCheckOut;

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);



        location = getIntent().getStringExtra("location");
        checkIn = getIntent().getStringExtra("checkIn");
        checkOut = getIntent().getStringExtra("checkOut");
        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);


        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSearch.setText(location);

        dateCheckInCheckOut=(TextView)findViewById(R.id.dateCheckInCheckOut);
        String checkInOut = checkIn + " - " + checkOut;
        dateCheckInCheckOut.setText(checkInOut);




        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        new GetContacts().execute();






    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         //   Toast.makeText(Result.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response

            String url = "http://api.sandbox.amadeus.com/v1.2/hotels/search-circle?latitude="+lat+"&longitude="+lng+"&radius=50&check_in="+checkIn+"&check_out="+checkOut+"&number_of_results=50&apikey=GMGRaaEkyZI20SgUDtUYOkxihT9VPnQF";
            //  String url = "http://api.sandbox.amadeus.com/v1.2/hotels/search-circle?latitude=43.6&longitude=7.2&radius=50&check_in=2018-09-01&check_out=2018-09-03&number_of_results=10&apikey=GMGRaaEkyZI20SgUDtUYOkxihT9VPnQF";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray results = jsonObj.getJSONArray("results");

                    // looping through All Contacts
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject c = results.getJSONObject(i);


                        String property_name = c.getString("property_name");

                        JSONObject address = c.getJSONObject("address");
                        String line1 = address.getString("line1");

                        JSONObject total_price = c.getJSONObject("total_price");
                        String amount = total_price.getString("amount");
                        String currency = total_price.getString("currency");






                        // tmp hash map for single contact
                        HashMap<String, String> result = new HashMap<>();

                      //  adding each child node to HashMap key => value
                      //  result.put("origen", origen);
                        result.put("property_name", property_name);
                        result.put("line1", line1);
                        result.put("amount", amount);
                        result.put("currency", currency);


                        // adding contact to contact list
                        contactList.add(result);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(Result.this, contactList,
                    R.layout.list_item, new String[]{ "property_name","line1","amount","currency"},
                    new int[]{R.id.property_name,R.id.line1,R.id.amount,R.id.currency});

            lv.setAdapter(adapter);
        }
    }







    public void back(View view){
        finish();

    }

}

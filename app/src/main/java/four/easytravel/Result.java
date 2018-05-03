package four.easytravel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Result extends AppCompatActivity {

    private String TAG = Result.class.getSimpleName();
    private ListView lv;
    private String checkIn,checkOut, from, to;

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        checkIn = getIntent().getStringExtra("checkIn");
        checkOut = getIntent().getStringExtra("checkOut");
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");

      //  Toast.makeText(getApplicationContext(), from + to + checkIn + checkOut, Toast.LENGTH_LONG).show();

        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Result.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://api.sandbox.amadeus.com/v1.2/flights/extensive-search?apikey=GMGRaaEkyZI20SgUDtUYOkxihT9VPnQF&origin=BOS&destination=NYC&departure_date=2018-06-25";
           // String url = "https://api.sandbox.amadeus.com/v1.2/flights/extensive-search?apikey=GMGRaaEkyZI20SgUDtUYOkxihT9VPnQF&origin="+from+"&destination="+to+"&departure_date="+checkIn;
           // String url = "http://api.sandbox.amadeus.com/v1.2/hotels/search-circle?latitude=43.6&longitude=7.2&radius=50&check_in="+from+"&check_out="+to+"&number_of_results=50&apikey=GMGRaaEkyZI20SgUDtUYOkxihT9VPnQF";
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
                      //  String origen = c.getString("origen");
                      //  String address = c.getString("address");
                      //  String total_price = c.getString("total_price");
                        String price = c.getString("price");



                        // tmp hash map for single contact
                        HashMap<String, String> result = new HashMap<>();

                        // adding each child node to HashMap key => value
                     //   result.put("origen", origen);
                     //   result.put("address", address);
                     //   result.put("total_price", total_price);
                        result.put("price", price);


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
                 //   R.layout.list_item, new String[]{ "property_name","total_price","price"},
                 //   new int[]{R.id.address,R.id.total_price,R.id.price});
                    R.layout.list_item, new String[]{ "price"},
                    new int[]{R.id.price});
            lv.setAdapter(adapter);
        }
    }
}

package four.easytravel;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Result extends AppCompatActivity {

    private String TAG = Result.class.getSimpleName();
    private ListView lv;
    private String checkIn,checkOut,location;
    public static double lat;
    public static double lng;
    Button buttonSearch;
    TextView dateCheckInCheckOut;
    String property_name,line1,amount,currency,images,amenity;
    ImageView imageView, imagePool,imagePets,imageParking;
    int imageId[]={R.drawable.hotel,R.drawable.hotel1,R.drawable.hotel2,R.drawable.hotel3,R.drawable.hotel4,
            R.drawable.hotel5,R.drawable.hotel6,R.drawable.hotel7,R.drawable.hotel8,R.drawable.hotel9,R.drawable.hotel10,
            R.drawable.hotel,R.drawable.hotel1,R.drawable.hotel2,R.drawable.hotel3,R.drawable.hotel4,
            R.drawable.hotel5,R.drawable.hotel6,R.drawable.hotel7,R.drawable.hotel8,R.drawable.hotel9,R.drawable.hotel10};

    ArrayList<HashMap<String, String>> amadeusList;
    private Activity rootView;

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



        amadeusList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);



        new GetAmadeus().execute();



    }




    private class GetAmadeus extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   Toast.makeText(Result.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }



        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response

            String url = "http://api.sandbox.amadeus.com/v1.2/hotels/search-circle?latitude="+lat+"&longitude="+lng+"&radius=50&check_in="+checkIn+"&check_out="+checkOut+"&number_of_results=20&apikey="+ getString(R.string.amadeus_api_key);

            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);



                    // Getting JSON Array node
                    JSONArray results = jsonObj.getJSONArray("results");




                    // looping through All Result
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject c = results.getJSONObject(i);


                        property_name = c.getString("property_name");

                        JSONObject address = c.getJSONObject("address");
                        line1 = address.getString("line1");

                        JSONObject total_price = c.getJSONObject("total_price");
                        amount = total_price.getString("amount");
                        currency = total_price.getString("currency");

                        JSONObject location = c.getJSONObject("location");
                        String latString = String.valueOf(location.getDouble("latitude"));
                        String lngString = String.valueOf(location.getDouble("longitude"));

                        JSONArray amenities = c.getJSONArray("amenities");
                        for(int n = 0; n < amenities.length(); n++) {
                            amenity = amenities.getString(n);
                        }




                        /*String property_name_nospace = property_name.replaceAll(" ", "+");
                        String placeUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+property_name_nospace+"&location="+latString+","+lngString+"&radius=100&key=" + getString(R.string.google_api_key);
                        String jsonPlaceStr = sh.makeServiceCall(placeUrl);
                        Log.e(TAG, "Response from place url: " + jsonPlaceStr );*/

                        //  hash map f
                        HashMap<String, String> result = new HashMap<>();

                        //  adding each child node to HashMap key => value



                        boolean resultPool = amenity.contains("POOL") || amenity.contains("Pool");
                        if(resultPool){
                            result.put("pool", "android.resource://four.easytravel/" + R.drawable.outline_pool_black_24dp);
                        }else{
                            result.put("pool", "android.resource://four.easytravel/" + R.drawable.outline_pool_black_g_24dp);
                        }
                        boolean resultPets = amenity.contains("PETS") || amenity.contains("Pets");
                        if(resultPets){
                            result.put("pets", "android.resource://four.easytravel/" + R.drawable.outline_pets_black_g_24dp);
                        }else{
                            result.put("pets", "android.resource://four.easytravel/" + R.drawable.outline_pets_black_24dp);
                        }
                        boolean resultParking = amenity.contains("PARKING") || amenity.contains("Parking");
                        if(resultParking){
                            result.put("parking", "android.resource://four.easytravel/" + R.drawable.round_local_parking_black_24dp);
                        }else{
                            result.put("parking", "android.resource://four.easytravel/" + R.drawable.round_local_parking_black_g_24dp);
                        }




                        result.put("property_name", property_name);
                        result.put("images", "android.resource://four.easytravel/" + imageId[i]);
                        result.put("line1", line1);
                        result.put("amount", amount);
                        result.put("currency", currency);
                        result.put("latitude", latString);
                        result.put("longitude", lngString);



                        // adding resul list
                        amadeusList.add(result);



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
            ListAdapter adapter = new SimpleAdapter(Result.this, amadeusList,
                    R.layout.list_item, new String[]{ "property_name","line1","amount","currency","images","pool","pets","parking"},
                    new int[]{R.id.property_name,R.id.line1,R.id.amount,R.id.currency,R.id.imageView,R.id.imagePool,R.id.imagePets,R.id.imageParking});

            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                    Map<String, Object> map = (Map<String, Object>)lv.getItemAtPosition(position);
                    String _property_name = (String) map.get("property_name");
                    String _line1 = (String) map.get("line1");
                    //get latlng from strings
                    LatLng latLng = new LatLng(Double.parseDouble( (String) map.get("latitude")),(Double.parseDouble( (String) map.get("longitude"))));


                    Intent i = new Intent(Result.this, GooglePlace.class);
                    i.putExtra("property_name", _property_name);
                    i.putExtra("cityLocate", _line1);
                    i.putExtra("latLng", latLng);

                    startActivity(i);
                }
            });





        }
    }




    public void back(View view){
        finish();

    }



}
package four.easytravel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

public class GooglePlace extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQ_CODE_SPEECH_INPUT = 100;

    String TAG = "GooglePlace";

    public TextView city, property;
    public String property_name, cityLocate;
    LatLng hotelLatLng;

    RecyclerView placeListRecyclerView;

    String voiceInput;

    View progress;

    ArrayList<NearbyPlace> placeList;

    Gson gson;
    Type type;

    GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_place);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        progress = findViewById(R.id.place_progress);

        placeListRecyclerView = findViewById(R.id.place_list_recycler_view);

        cityLocate = getIntent().getStringExtra("cityLocate");
        city = findViewById(R.id.city);
        city.setText(cityLocate);

        property_name = getIntent().getStringExtra("property_name");
        property = findViewById(R.id.property);
        property.setText(property_name);

        hotelLatLng = getIntent().getParcelableExtra("latLng");

        gson = new Gson();
        type = new TypeToken<Place>() {
        }.getType();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(hotelLatLng, 15);
        map.animateCamera(cameraUpdate);

        if (property_name != null) {
            map.addMarker(new MarkerOptions().position(hotelLatLng).title(property_name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
    }

    public void speakButtonPressed(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something!");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    voiceInput = (result.get(0)).replaceAll("\\s+", "");

                    map.clear();
                    if (property_name != null) {
                        map.addMarker(new MarkerOptions().position(hotelLatLng).title(property_name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    }
                    new GetNearbyPlaces().execute();
                }
                break;
            }

        }
    }

    private class GetNearbyPlaces extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            placeList = new ArrayList<>();

            HttpHandler sh = new HttpHandler();

            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + hotelLatLng.latitude + "," + hotelLatLng.longitude + "&radius=1000&keyword=" + voiceInput + "&key=" + getString(R.string.google_api_key);

            String jsonStr = sh.makeServiceCall(url);

            //Log.d(TAG, "Response for url: " + jsonStr);

            if (jsonStr != null) {


                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray results = jsonObj.getJSONArray("results");


                    for (int i = 0; i < results.length(); i++) {
                        JSONObject j = results.getJSONObject(i);

                        String name = j.getString("name");
                        JSONObject JsonGeometry = j.getJSONObject("geometry");
                        JSONObject JsonLocation = JsonGeometry.getJSONObject("location");
                        String placeId = j.getString("place_id");
                        String address = j.getString("vicinity");

                        JSONArray JsonPhotos = j.getJSONArray("photos");
                        JSONObject JsonPhotoObject = JsonPhotos.getJSONObject(0);
                        String photoRef = JsonPhotoObject.getString("photo_reference");

                        LatLng latLng = new LatLng(JsonLocation.getDouble("lat"), JsonLocation.getDouble("lng"));

                        NearbyPlace result = new NearbyPlace(name, latLng, placeId, address, photoRef);

                        String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=200&maxheight=200&photoreference=" + photoRef + "&key=" + getString(R.string.google_api_key);

                        Bitmap image = sh.makeServiceCallForImage(photoUrl);

                        result.setImage(image);

                        placeList.add(result);
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            showProgress(false);

            Log.d(TAG, "onPostExecute: " + placeList.size());

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(hotelLatLng, 12);
            map.animateCamera(cameraUpdate);

            for (NearbyPlace n : placeList) {
                map.addMarker(new MarkerOptions().position(n.getLatLng()).title(n.getName().toString()));
            }

            setupRecyclerView();
        }
    }

    public void setupRecyclerView() {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        GooglePlaceListAdapter mAdapter = new GooglePlaceListAdapter(this, mLayoutManager, placeList);

        placeListRecyclerView.setLayoutManager(mLayoutManager);
        placeListRecyclerView.setAdapter(mAdapter);
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            progress.setVisibility(show ? View.VISIBLE : View.GONE);
            progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progress.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}

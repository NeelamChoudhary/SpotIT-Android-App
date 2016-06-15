package com.example.tanyasanjay.giveandtake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MapActivity extends Activity {
    private GoogleMap googleMap;
    private LatLng latlong;
    double latitude, longitude;
    float zoomLevel = 16.0f;
    String sessionToken = "";

    JSONArray items;

    static LatLng place = new LatLng(37, -121);
    String json = "[ {\"name\":\"Table\", \"latlng\":[37.325954, -121.953122]} ,{\"name\":\"Tea Pot\",\"latlng\":[37.329102, -121.950751]}]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");
        latitude = intent.getDoubleExtra("latitude", 0.0);
        longitude = intent.getDoubleExtra("longitude", 0.0);

        Log.d("received log, lat", Double.toString(latitude).concat(Double.toString(longitude)));
        sessionToken = intent.getStringExtra("sessionToken");

        /*Toast.makeText(MapActivity.this, "Location: " + location + " Latitude: " + latitude + "Longitude: " + longitude, Toast.LENGTH_LONG).show();*/

        if (latitude == 0.0) {
            latitude = 37.4142184;
            longitude = -122.0764602;
        }
        place = new LatLng(latitude, longitude);
    }

    @Override
    protected void onResume(){
        super.onResume();
        invokeBackEnd(new RequestParams());
    }

    private void setupMap(){
        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.pinningmap)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setTrafficEnabled(true);
            googleMap.setBuildingsEnabled(true);

            googleMap.getUiSettings().setZoomControlsEnabled(true);
            Marker marker = googleMap.addMarker(new MarkerOptions().position(place).title("Your are here!!"));
            //call setupMarkers here
            setupMarkers(items);

            LatLng pos = marker.getPosition();
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(pos, 16.0f); //zoom to location
            googleMap.animateCamera(yourLocation);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setupMarkers(JSONArray items) throws JSONException{
        Log.d("testing", items.toString());
        for(int i=0;i<items.length();i++){
            final JSONObject jObj = items.getJSONObject(i);
            LatLng tempLatLngObj = new LatLng( Double.parseDouble(jObj.getString("pinLatitude")), Double.parseDouble(jObj.getString("pinLongitude")));
            googleMap.addMarker(new MarkerOptions()
                            .title(jObj.getString("item"))
                            .snippet(jObj.getString("email"))
                            .position(new LatLng(
                                    Double.parseDouble(jObj.getString("pinLatitude")),
                                    Double.parseDouble(jObj.getString("pinLongitude"))
                            ))

            );
            //+
            // googleMap.setOnInfoWindowClickListener();
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (marker.getTitle().isEmpty()) {
                        return false;
                    } else {
                        itemDetails(new RequestParams());
                        Intent intent = new Intent(MapActivity.this, markerDescription.class);
                        intent.putExtra("email", marker.getSnippet());
                        intent.putExtra("name", marker.getTitle());
                        intent.putExtra("sessionToken", sessionToken);
                        startActivity(intent);
                        return true;
                    }
                }
            });
        }
    }




    public void invokeBackEnd(final RequestParams requestParams) {
        String url = "http://54.191.124.21:5000/PinCollection";
        Log.d("string", url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String s = new String(responseBody);
                    Log.d("string", s);
                    JSONObject json = new JSONObject(s);

                    items = json.getJSONArray("_items");
                    Log.d("JSONArray", items.toString());

                    setupMap();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occurred [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void itemDetails(final RequestParams requestParams) {
        String url = "http://54.191.124.21:5000/PinCollection";
        Log.d("string", url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String s = new String(responseBody);
                    Log.d("string", s);
                    JSONObject json = new JSONObject(s);

                    items = json.getJSONArray("_items");
                    Log.d("JSONArray", items.toString());

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occurred [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}

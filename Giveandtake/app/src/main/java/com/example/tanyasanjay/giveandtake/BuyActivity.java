package com.example.tanyasanjay.giveandtake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class BuyActivity extends Activity {

    private static final LatLngBounds longlat = new LatLngBounds(new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    int Place_Picker_Request = 1;
    private TextView mname, maddress, mattributions, location;
    private String loc, in, lnglat;
    private EditText input;
    private LatLng longnlat;
    private double Latitude, Longitude;
    String sessionToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        mname = (TextView) findViewById(R.id.name);
        maddress = (TextView) findViewById(R.id.address);
        mattributions = (TextView) findViewById(R.id.attribution);
        location = (TextView) findViewById(R.id.getLocation);
        /*input = (EditText) findViewById(R.id.inputObject);*/

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            sessionToken = extras.getString("sessionToken");
        }

        Button loc = (Button) findViewById(R.id.findLocation);
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    builder.setLatLngBounds(longlat);
                    Intent intent = builder.build(BuyActivity.this);
                    startActivityForResult(intent/*builder.build(MainActivity.this)*/, Place_Picker_Request);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Place_Picker_Request) {
            if (resultCode == RESULT_OK) {
                final Place place = PlacePicker.getPlace(this, data);
                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();
                longnlat = place.getLatLng();
                Latitude = longnlat.latitude;
                Longitude = longnlat.longitude;


                /*Toast.makeText(BuyActivity.this, " Latitude:" + Latitude + "Longitude: " + Longitude, Toast.LENGTH_SHORT).show();*/


                String attributions = (String) place.getAttributions();
                if (attributions == null) {
                    attributions = "";
                }
                loc = name+" "+address+" "+attributions;
                location.setText(loc);
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void findObject(View view) {
        Log.d("neelam","in find obj");
        Intent intent = new Intent(BuyActivity.this,MapActivity.class);
        intent.putExtra("location",loc);
        intent.putExtra("object",in);
        intent.putExtra("latitude",Latitude);
        intent.putExtra("longitude",Longitude);
        intent.putExtra("sessionToken",sessionToken);
        startActivity(intent);

    }
}

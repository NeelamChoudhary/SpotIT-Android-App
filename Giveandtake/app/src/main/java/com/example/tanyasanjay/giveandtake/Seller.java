package com.example.tanyasanjay.giveandtake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

public class Seller extends AppCompatActivity {

    EditText location, itemName, price, description;
    LatLng latlong;
    String sessionToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        location = (EditText) findViewById(R.id.merchandise_loc);
        itemName = (EditText) findViewById(R.id.merchandize_text);
        price = (EditText) findViewById(R.id.mprice);
        description = (EditText) findViewById(R.id.desc_merchandise);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            sessionToken = extras.getString("sessionToken");
        }

        Button locPicker = (Button)findViewById(R.id.setlocation);
        locPicker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                            Intent intent = intentBuilder.build(Seller.this);
                            startActivityForResult(intent, 1);
                        } catch (GooglePlayServicesRepairableException
                                | GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Button save = (Button) findViewById(R.id.save_next);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double lat = latlong.latitude;
                Double longit = latlong.longitude;
                Intent intent = new Intent(Seller.this, UploadActivity.class);
                intent.putExtra("latitude", lat.toString());
                intent.putExtra("longitude",longit.toString());
                intent.putExtra("location", location.getText().toString());
                intent.putExtra("price", price.getText().toString());
                intent.putExtra("description", description.getText().toString());
                intent.putExtra("itemName", itemName.getText().toString());
                intent.putExtra("sessionToken",sessionToken);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(this, data);
            location.setText(place.getAddress());
            latlong =place.getLatLng();
            Log.d("latlong", latlong.toString());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

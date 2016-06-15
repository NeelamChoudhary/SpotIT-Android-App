package com.example.tanyasanjay.giveandtake;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Register extends AppCompatActivity {

    EditText userName, email, password, phone, city, state, street, zip;
    String sessionToken = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        email = (EditText)findViewById(R.id.reg_edemail);
        password = (EditText)findViewById(R.id.reg_edpswd);
        userName = (EditText)findViewById(R.id.userName);
        phone = (EditText)findViewById(R.id.phno_reg);
        city = (EditText)findViewById(R.id.city_edreg);
        state = (EditText)findViewById(R.id.state_edreg);
        street = (EditText)findViewById(R.id.street_edreg);
        zip = (EditText)findViewById(R.id.zip_edreg);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        Button reg = (Button)findViewById(R.id.regi_btn);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    postlDetail("UserAuth");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Register.this, Nav.class);
                startActivity(intent);
            }
        });


    }

    public void postlDetail(String tablename) throws JSONException, UnsupportedEncodingException {

        RequestParams params = new RequestParams();

        JSONObject json = new JSONObject();

        json.put("email", email.getText().toString());
        json.put("password", password.getText().toString());
        json.put("username", userName.getText().toString());

        //phone
        List<String> listPhoneMap = new ArrayList<String>();
        listPhoneMap.add(phone.getText().toString());
        json.put("phone", new JSONArray(listPhoneMap));

        // Address
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> addressMap = new HashMap<String, String>();
        addressMap.put("city", city.getText().toString());
        addressMap.put("state", state.getText().toString());
        addressMap.put("street", street.getText().toString());
        addressMap.put("zip", zip.getText().toString());
        list.add(addressMap);

        //File[] files = { new File("pic.jpg"), new File("pic1.jpg") };

        json.put("address", new JSONArray(list));

        //Log.d("Rentals", params.toString());
        Log.d("Details", json.toString());

        StringEntity entity = new StringEntity(json.toString());

        RestClient.post(this.getApplicationContext(), tablename, entity, "application/json", new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("response", response.toString());
                Intent intent = new Intent(Register.this, Nav.class);
                sessionToken = email.getText().toString();
                intent.putExtra("sessionToken", sessionToken);
                startActivity(intent);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                Log.d("response", response.toString());
                Intent intent = new Intent(Register.this, MainActivity.class);
                intent.putExtra("Error", "Server Error Try Again Later");
                startActivity(intent);
            }

        });
    }
}

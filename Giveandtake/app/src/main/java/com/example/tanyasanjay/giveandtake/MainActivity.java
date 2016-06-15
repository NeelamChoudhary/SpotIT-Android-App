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
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    EditText Username;
    EditText Password;
    String sessionToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Username = (EditText) findViewById(R.id.usrname);
        Password = (EditText) findViewById(R.id.password);
        final Button login = (Button) findViewById(R.id.login_btn);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Toast.makeText(getApplicationContext(), extras.getString("Error"), Toast.LENGTH_LONG).show();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin(view);
            }
        });

        Button register = (Button) findViewById(R.id.register_btn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callRegister();
            }
        });
    }

    public void userLogin(View view) {
        String usremail = Username.getText().toString();
        String pwd = Password.getText().toString();
        RequestParams params = new RequestParams();
        if ((!usremail.isEmpty()) && (!pwd.isEmpty())) {
            // When Email entered is Valid
            if (usremail.contains("@")) {
                // Put Http parameter username with value of Email Edit View control
                params.put("userName",usremail);
                // Put Http parameter password with value of Password Edit Value control
                params.put("password", pwd);
                // Invoke RESTful Web Service with Http parameters
                invokeBackEnd(usremail, pwd, params);
            }
            // When Email is invalid
            else {
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }

    }


    /**
     * INVOKING THE BACKEND TO LOGIN
     */
    public void invokeBackEnd(final String userName, final String pwd, final RequestParams requestParams) {
        String url = "http://54.191.124.21:5000/UserAuth?where=email=='";
        url = url.concat(userName).concat("'");
        Log.d("string", url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String s = new String(responseBody);
                    Log.d("string", s);
                    JSONObject json = new JSONObject(s);

                    JSONArray items = json.getJSONArray("_items");
                    Log.d("JSONArray", items.toString());

                    if (items.length() != 0 && pwd.equals(items.getJSONObject(0).getString("password"))) {
                        Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        sessionToken = userName;
                        // Navigate to Home screen
                        callNav();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Enter correct email/password", Toast.LENGTH_LONG).show();
                    }
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

    public void callNav()
    {
        Intent intent = new Intent(MainActivity.this, Nav.class);
        intent.putExtra("sessionToken",sessionToken);
        startActivity(intent);
    }

    public void callRegister()
    {
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
    }

}



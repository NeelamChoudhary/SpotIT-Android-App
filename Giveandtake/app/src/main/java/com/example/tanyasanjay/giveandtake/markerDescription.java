package com.example.tanyasanjay.giveandtake;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.services.s3.AmazonS3Client;
import com.example.tanyasanjay.giveandtake.VideoPlayback.ui.ActivityList.ActivityLauncher;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.Header;

public class markerDescription extends Activity {
    TextView Desc;
    String key = "IMG-20160427-WA0010.jpg";
    AmazonS3Client sS3Client;
    URL url, videoUrl;
    ImageView imageView;
    String itemName;
    JSONObject itemDetails;
    JSONArray temp;
    Button notify,viewAR;
    String URL_LINK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_description);

        notify = (Button) findViewById(R.id.notify);
        viewAR = (Button) findViewById(R.id.viewAR); //neelam

        imageView = (ImageView) findViewById(R.id.imageView12);
        Intent in = getIntent();

        final String email = in.getStringExtra("email");
        itemName = in.getStringExtra("name");
        final String sessionToken = in.getStringExtra("sessionToken");
        Log.d("ItemName", itemName);

        Desc = (TextView) findViewById(R.id.desc);
        Desc.setText(email.concat(itemName));

        //invokeBackEnd(email, new RequestParams());

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        RequestParams requestParams = new RequestParams();

        String u = "http://54.191.124.21:5000/ItemCollection?where=itemSeller=='@'";

        asyncHttpClient.get(u, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("failed", "failed");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject json = new JSONObject(responseString);

                    JSONArray items = json.getJSONArray("_items");
                    Log.d("JSONArray", items.toString());
                    Log.d("URL", Integer.toString(items.length()));
                    if (items.length() != 0) {
                        for (int i = 0; i < items.length(); i++) {
                            Log.d("Json Item names", items.getJSONObject(i).getString("itemName"));
                            if (itemName.equals(items.getJSONObject(i).getString("itemName"))) {

                                Log.d("Match Found", items.getJSONObject(i).toString());
                                itemDetails = items.getJSONObject(i);
                                Log.d("ITems Details", itemDetails.toString());

                                Log.d("Moment of trth", itemDetails.getJSONArray("itemImage").toString());
                                JSONArray temp = itemDetails.getJSONArray("itemImage");

                                Log.d("item Video", itemDetails.getJSONArray("itemVideo").toString()); //neelam
                                JSONArray tempVideo = itemDetails.getJSONArray("itemVideo");   //neelam

                                key = temp.getString(0);
                                String keyVideo= tempVideo.getString(0); //neelam

                                sS3Client = Util.getS3Client(markerDescription.this);
                                url = sS3Client.getUrl(Constants.BUCKET_NAME, key);
                                videoUrl= sS3Client.getUrl(Constants.BUCKET_NAME, keyVideo); //neelam

                                // Create an object for subclass of AsyncTask
                                GetXMLTask task = new GetXMLTask();
                                // Execute the task
//                                if(url==null)
//                                    Toast.makeText(getApplicationContext(), "No images provided", Toast.LENGTH_LONG).show();
//                                else
                                task.execute(url.toString());

                                Log.d("videoUrl.",videoUrl.toString());
                                URL_LINK= videoUrl.toString();
                            }
                        }
                        Log.d("URL", "--------------------------------------------------------");
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter correct email/password", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occurred [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }
        });




        viewAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {

                Log.d("LINK..", URL_LINK);
                long SPLASH_MILLIS = 450;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(markerDescription.this, ActivityLauncher.class);
                        intent.putExtra("ACTIVITY_TO_LAUNCH", "VideoPlayback.app.VideoPlayback.VideoPlayback");
                        intent.putExtra("URL_LINK", URL_LINK);
                        startActivity(intent);
                    }

                }, SPLASH_MILLIS);
                }
            });




        notify.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
                SendEmail sm = new SendEmail(email, "Buyer Interested for ".concat(itemName), sessionToken.concat("is intereseted in buying your ").concat(itemName).concat(". --Admin"));
                //Executing sendmail to send email
                sm.execute();

                //neelam temp

//                Log.d("LINK..",URL_LINK);
//                    long SPLASH_MILLIS = 450;
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable()
//                    {
//
//                        @Override
//                        public void run()
//                        {
//
//                            Intent intent = new Intent(markerDescription.this, ActivityLauncher.class);
//                            intent.putExtra("ACTIVITY_TO_LAUNCH", "VideoPlayback.app.VideoPlayback.VideoPlayback");
//                            intent.putExtra("URL_LINK",URL_LINK);
//                            startActivity(intent);

//                Intent intent = new Intent(BuyActivity.this,
//                        AboutScreen.class);
//                intent.putExtra("ACTIVITY_TO_LAUNCH",
//                        "VideoPlayback.app.VideoPlayback.VideoPlayback"); //vuforia.samples.VideoPlayback.app.VideoPlayback.VideoPlayback
//                intent.putExtra("ABOUT_TEXT_TITLE", "Video Playback");
//                intent.putExtra("ABOUT_TEXT", "VideoPlayback/VP_about.html");
//                startActivity(intent);

//                        }
//
//                    }, SPLASH_MILLIS);

        /*Intent intent = new Intent(BuyActivity.this,MapActivity.class);
        intent.putExtra("location",loc);
        intent.putExtra("object",in);
        intent.putExtra("latitude",Latitude);
        intent.putExtra("longitude",Longitude); */
                    // startActivity(intent);


            }
        });

    } //onCreate ends


    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
    /*
        @Override
        protected Object doInBackground(Object[] params) {
            int i = 0;
            String[] urls;
            for (Object s: params) {
                String(s);

            }
            return null;
        }*/

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            Log.d("check", result.toString());
            imageView.setImageBitmap(result);
            System.out.println("finished");
        }

        @Override
        protected void onProgressUpdate(Void... values) {}

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;
            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                //stream.close();
            } catch (Exception e1) {
                Log.d("checkpoint", e1.toString());
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpsURLConnection httpConnection = (HttpsURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpsURLConnection.HTTP_OK ||
                        httpConnection.getResponseCode() == HttpsURLConnection.HTTP_NOT_MODIFIED ) {

                    stream = httpConnection.getInputStream();
                } else { // just in case..

                    //log.d("Surprize HTTP status was: " ,httpConnection.getResponseCode());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }

    }

}
